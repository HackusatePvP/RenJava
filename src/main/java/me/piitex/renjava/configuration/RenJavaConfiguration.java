package me.piitex.renjava.configuration;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.builders.ImageLoader;

import java.util.Map;

public class RenJavaConfiguration {
    private final String gameTitle;
    private final int width;
    private final int height;
    private final ImageLoader gameIcon;

    private FontLoader defaultFont;
    private FontLoader dialogueFont;
    private FontLoader uiFont;
    private Paint dialogueColor = Color.BLACK;

    private int dialogueBoxWidth = 1000;
    private int dialogueBoxHeight = 600;

    private int dialogueBoxX = 10;
    private int dialogueBoxY = 800;
    private int dialogueOffsetX = 0;
    private int dialogueOffsetY = 0;

    private final int textSize = 24;
    private int characterTextSize = 36;

    private int textX = 400;
    private int textY = 900;
    private int textOffsetX = 0;
    private int textOffsetY = 0;

    private int characterTextX = 500;
    private int characterTextY = 850;
    private int characterTextOffsetX = 0;
    private int characterTextOffsetY = 0;

    /**
     * Used to create your own customized configuration easily.
     *
     * @param gameTitle - This is the window title display at the top of the window bar
     * @param width     - Width of the game
     * @param height    - Height of the game.
     * @param gameIcon  - The game icon is used for the windows bar on the top as well as the icon for the taskbar.
     */
    public RenJavaConfiguration(String gameTitle, int width, int height, ImageLoader gameIcon) {
        this.gameTitle = gameTitle;
        this.width = width;
        this.height = height;
        this.gameIcon = gameIcon;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getCurrentHeight() {
        return 1080;
    }

    public int getCurrentWidth() {
        return 1920;
    }

    public ImageLoader getGameIcon() {
        return gameIcon;
    }

    public FontLoader getDialogueFont() {
        if (dialogueFont != null) {
            return dialogueFont;
        }
        return defaultFont;
    }

    public void setDialogueFont(FontLoader dialogueFont) {
        this.dialogueFont = dialogueFont;
    }

    public FontLoader getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(FontLoader defaultFont) {
        this.defaultFont = defaultFont;
    }

    public FontLoader getUiFont() {
        return uiFont;
    }

    public void setUiFont(FontLoader uiFont) {
        this.uiFont = uiFont;
    }

    public Paint getDialogueColor() {
        return dialogueColor;
    }

    public void setDialogueColor(Paint dialogueColor) {
        this.dialogueColor = dialogueColor;
    }

    public int getDialogueBoxWidth() {
        return dialogueBoxWidth;
    }

    public void setDialogueBoxWidth(int dialogueBoxWidth) {
        this.dialogueBoxWidth = dialogueBoxWidth;
    }

    public int getDialogueBoxHeight() {
        return dialogueBoxHeight;
    }

    public void setDialogueBoxHeight(int dialogueBoxHeight) {
        this.dialogueBoxHeight = dialogueBoxHeight;
    }

    public int getDialogueBoxX() {
        return dialogueBoxX ;
    }

    public void setDialogueBoxX(int dialogueBoxX) {
        this.dialogueBoxX = dialogueBoxX;
    }

    public int getDialogueBoxY() {
        return dialogueBoxY;
    }

    public void setDialogueBoxY(int dialogueBoxY) {
        this.dialogueBoxY = dialogueBoxY;
    }

    public int getDialogueOffsetX() {
        return dialogueOffsetX;
    }

    public void setDialogueOffsetX(int dialogueOffsetX) {
        this.dialogueOffsetX = dialogueOffsetX;
    }

    public int getDialogueOffsetY() {
        return dialogueOffsetY;
    }

    public void setDialogueOffsetY(int dialogueOffsetY) {
        this.dialogueOffsetY = dialogueOffsetY;
    }


    public int getCharacterTextSize() {
        return characterTextSize;
    }

    /**
     * Sets the Characters name font size.
     * @param characterTextSize
     */
    public void setCharacterTextSize(int characterTextSize) {
        this.characterTextSize = characterTextSize;
    }

    public int getTextX() {
        return textX;
    }

    public void setTextX(int textX) {
        this.textX = textX;
    }

    public int getTextY() {
        return textY;
    }

    public void setTextY(int textY) {
        this.textY = textY;
    }

    public int getTextOffsetX() {
        return textOffsetX;
    }

    /**
     * Sets how far to the right the dialogue will be displayed. This is already aligned to the corner of the image.
     * @param textOffsetX
     */
    public void setTextOffsetX(int textOffsetX) {
        this.textOffsetX = textOffsetX;
    }


    public int getTextOffsetY() {
        return textOffsetY;
    }

    /**
     * Sets how far down the dialogue will be displayed. This is already aligned to the corner of the image.
     * @param textOffsetY
     */
    public void setTextOffsetY(int textOffsetY) {
        this.textOffsetY = textOffsetY;
    }

    public int getCharacterTextX() {
        return characterTextX;
    }

    public void setCharacterTextX(int characterTextX) {
        this.characterTextX = characterTextX;
    }

    public int getCharacterTextY() {
        return characterTextY;
    }

    public void setCharacterTextY(int characterTextY) {
        this.characterTextY = characterTextY;
    }

    public int getCharacterTextOffsetX() {
        return characterTextOffsetX;
    }

    /**
     * Sets how far to the right the character display name will be displayed. This is already aligned to the corner of the image.
     * @param characterTextOffsetX
     */
    public void setCharacterTextOffsetX(int characterTextOffsetX) {
        this.characterTextOffsetX = characterTextOffsetX;
    }

    public int getCharacterTextOffsetY() {
        return characterTextOffsetY;
    }

    /**
     * Sets how far down the character display name will be displayed. This is already aligned to the corner of the image.
     * @param characterTextOffsetY
     */
    public void setCharacterTextOffsetY(int characterTextOffsetY) {
        this.characterTextOffsetY = characterTextOffsetY;
    }

    // bottom left is 0 on the x and max on the y
    public Map.Entry<Integer, Integer> getBottomLeft() {
        return Map.entry(5, getCurrentHeight());
    }

    // Bottom right is max on both
    public Map.Entry<Integer, Integer> getBottomRight() {
        //return Map.entry(height + 950, width + 600);
        return Map.entry(getCurrentWidth(), getCurrentHeight());
    }

    // Top Left is 0,0
    public Map.Entry<Integer, Integer> getTopLeft() {
        return Map.entry(5, 15);
    }

    public Map.Entry<Integer, Integer> getTopRight() {
        return Map.entry(getCurrentWidth(), 5);
    }

    public double getHeightScale() {
        return height / 1080d;
    }

    public double getWidthScale() {
        return width / 1920d;
    }
}