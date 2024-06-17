package me.piitex.renjava.events.defaults;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
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
                    buttonOverlay.setTextFill(button.getTextFill());
                    button.setTextFill(buttonOverlay.getHoverColor());
                } else {
                    button.setTextFill(RenJava.getInstance().getConfiguration().getHoverColor());
                }
                if (buttonOverlay.getHoverImage() != null) {
                    Image bg = buttonOverlay.getHoverImage().getImage();
                    ImageView imageView = new ImageView(bg);
                    imageView.setFitHeight(buttonOverlay.height());
                    imageView.setFitWidth(buttonOverlay.width());
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
        if (overlay.getOnHoverExit() != null) {
            overlay.getOnHoverExit().onHoverExit(event);
        }
        if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.getButton();
            if (buttonOverlay.isHover()) {
                if (buttonOverlay.getImage() != null) {
                    Image bg = buttonOverlay.getImage().getImage();
                    ImageView imageView = new ImageView(bg);
                    imageView.setFitHeight(buttonOverlay.height());
                    imageView.setFitWidth(buttonOverlay.width());
                }

                if (buttonOverlay.getTextFill() != null) {

                    button.setTextFill(buttonOverlay.getTextFill());
                }
            }
        }
    }
}
