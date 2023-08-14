package me.piitex.renjava.configuration;

import javafx.scene.image.Image;

import java.util.Map;

// FIXME: 8/13/2023 Long time java 8 developer so records are a new concept to me. Depending on what I plan for this class it could be converted to a record at a later date.
public class RenJavaConfiguration {
    private final String gameTitle;
    private final int width;
    private final int height;
    private final Image gameIcon;

    /**
     * Used to create your own customized configuration easily.
     *
     * @param gameTitle - This is the window title display at the top of the window bar
     * @param width     - Width of the game
     * @param height    - Height of the game.
     * @param gameIcon  - The game icon is used for the windows bar on the top as well as the icon for the taskbar.
     */
    public RenJavaConfiguration(String gameTitle, int width, int height, Image gameIcon) {
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

    public Image getGameIcon() {
        return gameIcon;
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
}