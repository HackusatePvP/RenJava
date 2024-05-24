package me.piitex.renjava.events.defaults;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.overlay.ButtonOverlay;
import me.piitex.renjava.gui.overlay.Overlay;
import me.piitex.renjava.loggers.RenLogger;

public class OverlayEventListener implements EventListener {

    @Listener
    public void onOverlayClick(OverlayClickEvent event) {
        Overlay overlay = event.getOverlay();

        if (overlay.getOnClick() != null) {
            overlay.getOnClick().onClick(event);
        }
    }

    @Listener
    public void onOverlayHover(OverlayHoverEvent event) {
        Overlay overlay = event.getOverlay();

        if (overlay.getOnHover() != null) {
            overlay.getOnHover().onHover(event);
        }

        if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.getButton();

            if (buttonOverlay.isHover()) {
                if (buttonOverlay.getHoverColor() != null) {
                    button.setTextFill(buttonOverlay.getHoverColor());
                } else {
                    System.out.println("Setting default hover color for: " + buttonOverlay.getId());
                    button.setTextFill(RenJava.getInstance().getConfiguration().getHoverColor());
                }
                if (buttonOverlay.getHoverImage() != null) {
                    try {
                        button.setGraphic(new ImageView(buttonOverlay.getHoverImage().build()));
                    } catch (ImageNotFoundException e) {
                        RenLogger.LOGGER.error(e.getMessage());
                    }
                }
            } else {
                System.out.println("No hover was set for overlay");
            }
        }
    }

    @Listener
    public void onOverlayRelease(OverlayClickReleaseEvent event) {
        Overlay overlay = event.getOverlay();
        if (overlay.getOnRelease() != null) {
            overlay.getOnRelease().onClickRelease(event);
        }
    }

    @Listener
    public void onOverlayExit(OverlayExitEvent event) {
        Overlay overlay = event.getOverlay();

        if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.getButton();
            button.setTextFill(buttonOverlay.getTextFill());
            button.setStyle(button.getStyle()); // Re-init the style (hopefully this forces the button back to normal.)
            if (buttonOverlay.getImage() != null) {
                button.setGraphic(new ImageView(buttonOverlay.getImage().getImage()));
            }
        }
    }
}
