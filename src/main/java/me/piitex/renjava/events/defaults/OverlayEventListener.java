package me.piitex.renjava.events.defaults;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.overlays.ButtonOverlay;
import me.piitex.renjava.gui.overlays.Overlay;
import me.piitex.renjava.loggers.RenLogger;


public class OverlayEventListener implements EventListener {

    @Listener
    public void onOverlayClick(OverlayClickEvent event) {
        Overlay overlay = event.getOverlay();
        if (overlay instanceof ButtonOverlay buttonOverlay) {
            RenLogger.LOGGER.debug("Button: " + buttonOverlay.getId());
        }

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
                    buttonOverlay.setTextFill(button.getTextFill());
                    button.setTextFill(buttonOverlay.getHoverColor());
                } else {
                    button.setTextFill(RenJava.getInstance().getConfiguration().getHoverColor());
                }
                if (buttonOverlay.getHoverImage() != null) {
                    Image bg = buttonOverlay.getHoverImage().getImage();
                    ImageView imageView = new ImageView(bg);
                    imageView.setFitHeight(buttonOverlay.getHeight());
                    imageView.setFitWidth(buttonOverlay.getWidth());
                }
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
        if (overlay.getOnHoverExit() != null) {
            overlay.getOnHoverExit().onHoverExit(event);
        }
        if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.getButton();
            if (buttonOverlay.isHover()) {
                if (buttonOverlay.getImage() != null) {
                    Image bg = buttonOverlay.getImage().getImage();
                    ImageView imageView = new ImageView(bg);
                    imageView.setFitHeight(buttonOverlay.getHeight());
                    imageView.setFitWidth(buttonOverlay.getWidth());
                }

                if (buttonOverlay.getTextFill() != null) {

                    button.setTextFill(buttonOverlay.getTextFill());
                }
            }
        }
    }

    @Listener
    public void onInputSet(InputSetEvent event) {
        RenScene renScene = RenJava.getInstance().getPlayer().getCurrentScene();
        if (renScene == null) return;
        if (renScene instanceof InputScene inputScene) {
            inputScene.getSetInterface().onInputSet(event);
        }
    }
}
