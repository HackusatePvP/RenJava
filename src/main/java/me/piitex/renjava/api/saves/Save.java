package me.piitex.renjava.api.saves;

import javafx.scene.image.WritableImage;
import javafx.stage.StageStyle;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.saves.exceptions.SaveFileEncryptedState;
import me.piitex.renjava.api.saves.file.SaveFileState;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.overlays.ImageOverlay;
import me.piitex.renjava.loggers.RenLogger;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.stories.Story;
import me.piitex.renjava.tasks.Tasks;
import me.piitex.renjava.utils.FileCrypter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.*;
import java.util.*;

// Class which represents a save file.
public class Save {
    private final File file;
    private String name;
    private final int page, slot;
    private long updatedTime;
    private final SaveManager saveManager;

    // Keeps track if the file is encrypted or decrypted
    private SaveFileState fileState = SaveFileState.ENCRYPTED;

    // Cache saves for the file states
    private static final Map<String, Save> saveCache = new HashMap<>();

    protected Save(int page, int slot) {
        this.page = page;
        this.slot = slot;
        this.file = new File(RenJava.getInstance().getBaseDirectory(), "/game/saves/save-" + page + "-" + slot + ".dat");
        this.saveManager = new SaveManager(this);

        saveCache.put(page + "-" + slot, this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        // If the name was changed re-write save file.
        System.out.println("Updating...");
        saveManager.update();
    }

    public SaveFileState getFileState() {
        return fileState;
    }

    public void setFileState(SaveFileState fileState) {
        this.fileState = fileState;
    }

    public int getSlot() {
        return slot;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public String getLocalizedCreationDate() {
        if (!(updatedTime > 0)) {
            return "";
        }

        LocalDateTime localDate = LocalDateTime.ofInstant(new Date(updatedTime).toInstant(), ZoneId.systemDefault());
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        Month month = localDate.getMonth();
        int year = localDate.getYear();
        int day = localDate.getDayOfMonth();
        int hour = localDate.getHour();
        int minute = localDate.getMinute();

        String minuteFix = String.valueOf(minute);
        if (minuteFix.length() < 2) {
            minuteFix = "0" + minute;
        }

        // Monday, December 09 2024, 12:45
        return dayOfWeek.name() + ' ' + month.name() + ' ' + day + ' ' + year + ' ' + hour + ":" + minuteFix;
    }

    public boolean exists() {
        return file.exists();
    }

    public void canModify() throws SaveFileEncryptedState {
        if (fileState == SaveFileState.ENCRYPTED && RenJava.CONFIGURATION.isEncryptSaves()) {
            throw new SaveFileEncryptedState(file);
        }
//        return fileState == FileState.DECRYPTED || !file.exists() || !RenJava.CONFIGURATION.isEncryptSaves();
    }

    public String retrieveCurrentData() {
        try {
            return new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            RenLogger.LOGGER.error("Could not pull data from save file '{}'!", getFile().getName(), e);
            RenJava.writeStackTrace(e);
        }
        return null;
    }


    public ImageOverlay buildPreview(int page) {
        if (!file.exists()) {
            return null;
        }

        saveManager.load(false);

        ImageOverlay saveImage;
        Object o = saveManager.getSceneSection().get("currentStory");
        if (o == null) {
            RenLogger.LOGGER.error("Invalid or corrupt save file!");
            return null;
        }

        Story story = RenJava.PLAYER.getStory((String) o);

        // FIXME: This will produce a lot of programming debt. This is an extremely cheap unoptimized hack.
        // Set the player to the current story (which is off to a horrible start)
        RenJava.PLAYER.setCurrentStory(story.getId());

        // Initialize the story to process the scenes. (Used to execute the `addScene` functions which maps the scenes to the story.)
        story.init();

        // Render the scene to the load button.
        RenScene currentScene = story.getScene((String) saveManager.getSceneSection().get("currentScene"));
        if (currentScene == null) {
            RenLogger.LOGGER.error("Save slot '" + slot + "' appears to be corrupt or has missing information. Unable to render save preview for the file. '" + saveManager.getSceneSection().get("currentScene") + "'");
            return new ImageOverlay("gui/button/slot_idle_background.png");
        }

        // When the render function is called, the stage type will be set to scene type. This will cause issues as the player is technically in the save/load screen.
        // To prevent the white flash when loading preview use diff window.
        // On slower machines the window may pop-up for a few seconds but if that's the case your pc doesn't meet spec requirements to begin with.
        Window hiddenWindow = new Window("", StageStyle.DECORATED, null, 1920, 1080, false, false);

//            hiddenWindow.clear(); // Required (This prevents white boxes from being rendered)
        Container container = currentScene.build(true);
        hiddenWindow.addContainers(container);
        hiddenWindow.build(true);

        WritableImage snapshot = hiddenWindow.getRoot().getScene().snapshot(null);
        saveImage = new ImageOverlay(snapshot);
        hiddenWindow.close();

        saveImage.setWidth(384);
        saveImage.setHeight(216);

        // Since the Renpy assets account for Text they made a transparency space.
        // To circumvent this extra space we need to add the length of the space so everything is properly aligned.
        saveImage.setY(15); // Position inside the button

        // Re-encrypt file in async
        if (RenJava.CONFIGURATION.isEncryptSaves())
            encrypt();

        return saveImage;
    }

    public void encrypt() {
        File encrypt = new File(RenJava.getInstance().getBaseDirectory(), "/game/saves/" + "save-" + slot + "-enc.dat");

        // Create the file
        try {
            Files.createFile(encrypt.toPath());
        } catch (IOException e) {
            RenLogger.LOGGER.error("Failed to create placeholder file for encryption!", e);
        }

        if (fileState == SaveFileState.DECRYPTED || !file.exists()) {
            FileCrypter.encryptFile(file, encrypt);
            fileState = SaveFileState.ENCRYPTED;

            // Copy encrypted file and replace
            try {
                Files.copy(encrypt.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                RenLogger.LOGGER.error("Failed to copy save file!", e);
            }

        } else {
            RenLogger.LOGGER.warn("Could not encrypt file '{}'. The file is already encrypted.", file.getAbsolutePath());
        }

        encrypt.delete();
    }

    public void decrypt() {
        if (!file.exists()) return; // Do not process if the file does not exist. Cannot decrypt nothingness.

        File dir = new File(System.getenv("APPDATA") + "/RenJava/" + RenJava.getInstance().getID() + "/decrypted/");
        dir.mkdirs();
        File decrypt =  new File(dir, file.getName());

        // Create the file
        try {
            decrypt.createNewFile();
        } catch (IOException e) {
            RenLogger.LOGGER.error("Failed to create placeholder file for decryption!", e);
        }

        if (fileState == SaveFileState.ENCRYPTED) {
            FileCrypter.decryptFile(file, decrypt);
            try {
                Files.copy(decrypt.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                decrypt.delete();
                fileState = SaveFileState.DECRYPTED;
            } catch (IOException e) {
                RenLogger.LOGGER.error("Failed to copy save file!", e);
            }
        } else {
            RenLogger.LOGGER.error("Could not decrypt file '{}'. The file is already encrypted.", file.getAbsolutePath());
        }
    }

    public File getFile() {
        return file;
    }

    /* Static methods */
    public static Save createSave(int page, int slot) {
        if (saveCache.containsKey(page + "-" + slot)) {
            return saveCache.get(page + "-" + slot);
        }

        return new Save(page, slot);
    }

    public static Save getSave(int page, int slot) {
        return saveCache.get(page + "-" + slot);
    }
}
