package me.piitex.renjava.events.types;

import me.piitex.renjava.events.Event;

public class ScrollUpEvent extends Event {
    private boolean displayPreviousScene = true;

    public boolean isDisplayPreviousScene() {
        return displayPreviousScene;
    }

    public void setDisplayPreviousScene(boolean displayPreviousScene) {
        this.displayPreviousScene = displayPreviousScene;
    }
}
