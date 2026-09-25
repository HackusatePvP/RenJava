package me.piitex.renjava.api.scenes.types;

import me.piitex.engine.ui.color.Color;
import me.piitex.engine.ui.containers.Container;
import me.piitex.engine.ui.image.ImageLoader;
import me.piitex.engine.ui.layout.HorizontalLayout;
import me.piitex.engine.ui.layout.Layout;
import me.piitex.engine.ui.layout.VerticalLayout;
import me.piitex.engine.ui.overlays.ButtonOverlay;
import me.piitex.engine.ui.overlays.ImageOverlay;
import me.piitex.engine.ui.overlays.TextFlowOverlay;
import me.piitex.engine.ui.overlays.TextOverlay;
import me.piitex.engine.utils.flags.Nullable;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.characters.Character;
import me.piitex.renjava.api.scenes.Scene;
import me.piitex.renjava.gui.StageType;
import org.jetbrains.skia.Image;

import java.io.File;
import java.util.List;

public class ImageScene extends Scene {
    @Nullable private final ImageOverlay background;
    @Nullable private Character character;
    @Nullable private String dialogue;

    private final RenJava renJava = RenJava.getInstance();

    public ImageScene(String id,
                      @Nullable ImageOverlay background,
                      @Nullable Character character,
                      @Nullable String dialogue) {
        super(id);
        this.background = background;
        this.character = character;
        this.dialogue = dialogue;
    }

    @Override
    public Container build() {
        double w = RenJava.getConfiguration().getWidth();
        double h = RenJava.getConfiguration().getHeight();
        double s = w / 1920.0; // Ren'Py's default gui is authored at 1920x1080

        Container container = new Container(w, h);

        // 1. Background (back-most)
        if (background != null) {
            container.addElement(background);
        } else {
            container.getStyling().setBackgroundColor(Color.BLACK);
        }

        // 2. Character sprites go here, before the textbox so the box draws over them.

        if (dialogue != null) {
            // 3. Textbox: full width, anchored to the bottom (gui.textbox_height = 278)
            File textboxFile = new File(renJava.getGuiDirectory(), "textbox.png");
            Image image = ImageLoader.load(textboxFile);
            double boxW = w;
            double boxH = image.getHeight() * (w / image.getWidth()); // keep aspect ratio
            double boxY = h - boxH;

            ImageOverlay textBox = new ImageOverlay(textboxFile, boxW, boxH);
            textBox.setPosition(0, boxY);
            container.addElement(textBox);

            String characterName = character.getDisplayName();
            if (characterName != null) {
                TextOverlay name = new TextOverlay(characterName, (float) (45 * s), character.getColor());
                name.setPosition(360 * s, boxY);
                container.addElement(name);
            }

            // 5. Dialogue (gui.dialogue_xpos = 402, dialogue_ypos = 75, dialogue_width = 1116, text_size = 33)
            TextFlowOverlay text = new TextFlowOverlay(dialogue, 1116 * s, (float) (33 * s), Color.WHITE);
            text.setPosition(402 * s, boxY + 75 * s);
            container.addElement(text);
        }

        // 6. Quick menu: centered along the bottom edge
        container.addElement(buildQuickMenu(w, h, s));

        return container;
    }

    private HorizontalLayout buildQuickMenu(double w, double h, double s) {
        double menuH = 30 * s;
        HorizontalLayout quickMenu = new HorizontalLayout(0, h - menuH, w, menuH);
        quickMenu.setAlignment(Layout.Alignment.CENTER);
        quickMenu.setSpacing((float) (15 * s));

        for (String label : List.of("Back", "History", "Skip", "Auto", "Save", "Q.Save", "Q.Load", "Prefs")) {
            ButtonOverlay button = new ButtonOverlay(label, (float) (21 * s), Color.WHITE, 70 * s, menuH);
            // ButtonOverlay picks up the theme's button box and text color; these setters override that
            button.getStyling().setBackgroundColor(Color.TRANSPARENT);
            button.getStyling().setHoverColor(Color.TRANSPARENT);
            button.getStyling().setBorderThickness(0f);
            button.setTextColor(new Color(0.8f, 0.8f, 0.8f, 1f));
            button.getStyling().setTextHoverColor(Color.WHITE);
            button.onAction(() -> { /* hook into RenJava's action for this label */ });
            quickMenu.addElement(button);
        }
        return quickMenu;
    }
    @Override
    public StageType getStageType() {
        return StageType.IMAGE_SCENE;
    }
}
