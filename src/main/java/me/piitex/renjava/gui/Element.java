package me.piitex.renjava.gui;

import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.scenes.transitions.Transitions;
import me.piitex.renjava.events.types.*;
import me.piitex.renjava.gui.overlay.*;
import me.piitex.renjava.loggers.RenLogger;

import java.io.File;
import java.net.MalformedURLException;

public class Element {
    private final Overlay overlay;
    private Node node;
    private Transitions transitions;

    public Element(Overlay overlay) {
        this.overlay = overlay;
        //FIXME: When scaling the height and width the x and y positions need to be scaled to match the modified width and height.

        if (overlay instanceof Region region) {
            double scaleX = region.scaleX();
            double scaleY = region.scaleY();
            if (scaleX > 0) {
                region.setWidth(region.width() * scaleX);
                overlay.setX(overlay.x() * scaleX);
            }
            if (scaleY > 0) {
                region.setHeight(region.height() * scaleY);
                overlay.setY(overlay.y() * scaleY);
            }
        }
        if (overlay instanceof ImageOverlay imageOverlay) {
            RenLogger.LOGGER.debug("Processing {}", imageOverlay.getFileName());
            Image image = imageOverlay.getImage();
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            if (imageOverlay.width() != 0) {
                imageView.setFitWidth(imageOverlay.width());
            }
            if (imageOverlay.height() != 0) {
                imageView.setFitHeight(imageOverlay.height());
            }
            imageView.setTranslateX(imageOverlay.x());
            imageView.setTranslateY(imageOverlay.y());
            this.node = imageView;
        } else if (overlay instanceof TextOverlay textOverlay) {
            Text text = textOverlay.getText();
            if (textOverlay.getFontLoader() != null) {
                Font font = textOverlay.getFontLoader().getFont();
                text.setFont(font);
            }
            if (textOverlay.getTextFillColor() != null) {
                text.setFill(textOverlay.getTextFillColor());
            }
            text.setTranslateX(textOverlay.x());
            text.setTranslateY(textOverlay.y());
            this.node = text;
        } else if (overlay instanceof ButtonOverlay buttonOverlay) {
            Button button = buttonOverlay.build();
            button.setTranslateX(buttonOverlay.x());
            button.setTranslateY(buttonOverlay.y());
            this.node = button;
        } else if (overlay instanceof TextFlowOverlay textFlowOverlay) {
            TextFlow textFlow = textFlowOverlay.build();
            textFlow.setTranslateX(textFlowOverlay.x());
            textFlow.setTranslateY(textFlowOverlay.y());
            this.node = textFlow;
        } else if (overlay instanceof SliderOverlay sliderOverlay) {
            //TODO Finish sliders
            Slider slider = new Slider(sliderOverlay.getMinValue(), sliderOverlay.getMaxValue(), sliderOverlay.getCurrentValue());
            slider.setTranslateX(sliderOverlay.x());
            slider.setTranslateY(sliderOverlay.y());
            slider.setBlockIncrement(sliderOverlay.getBlockIncrement());
            // To design sliders we NEED a css file which contains the styling. I'm not able to inline this via code which sucks.
            // Hopefully the slider gets improvements in JavaFX.

            // Check if they have css files
            File sliderCss = new File(System.getProperty("user.dir") + "/game/css/slider.css");
            if (!sliderCss.exists()) {
                RenLogger.LOGGER.error("Slider css file does not exist. Please run or copy the css files from RSDK. Without a slider css file, the sliders won't properly be designed.");
            } else {
                try {
                    slider.getStylesheets().add(sliderCss.toURI().toURL().toExternalForm());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
            // Handle slider events
            slider.setOnMouseDragged(event -> {
                SliderChangeEvent changeEvent = new SliderChangeEvent(sliderOverlay, slider.getValue());
                sliderOverlay.getSliderChange().onSliderChange(changeEvent);
                RenJava.callEvent(changeEvent);

                RenJava.callEvent(new OverlayClickReleaseEvent(overlay, event));
            });

            this.node = slider;
        } else if (overlay instanceof HyperlinkOverlay hyperlinkOverlay) {
            Hyperlink hyperlink = new Hyperlink(hyperlinkOverlay.getLabel());
            hyperlink.setTranslateX(hyperlinkOverlay.x());
            hyperlink.setTranslateY(hyperlinkOverlay.y());
            if (hyperlinkOverlay.getFont() != null) {
                hyperlink.setFont(hyperlinkOverlay.getFont().getFont());
            }
            hyperlink.setOnAction(actionEvent -> {
                RenJava.getInstance().getHost().showDocument(hyperlinkOverlay.getLink());
            });

            this.node = hyperlink;
        }

        // Handle specific input
        handleInput();

        // Render transition for specific overlay.
        if (overlay.getTransition() != null) {
            setTransition(overlay.getTransition());
        }
    }

    public Overlay getOverlay() {
        return overlay;
    }

    public Element setTransition(Transitions transitions) {
        this.transitions = transitions;
        return this;
    }

    public void render(Pane root) {
        if (transitions != null) {
            transitions.play(node);
        }
        root.getChildren().add(node);
    }

    public void render(Pane root, double width, double height) {
        if (transitions != null) {
            transitions.play(node);
        }
        root.getChildren().add(node);
    }

    private void handleInput() {
        node.setOnMouseEntered(event -> {
            RenJava.callEvent(new OverlayHoverEvent(overlay, event));
        });
        node.setOnMouseClicked(event -> {
            RenJava.callEvent(new OverlayClickEvent(overlay, event));
        });
        node.setOnMouseExited(event -> {
            RenJava.callEvent(new OverlayExitEvent(overlay, event));
        });
        if (node.getOnMouseReleased() == null) {
            node.setOnMouseReleased(event -> {
                RenJava.callEvent(new OverlayClickReleaseEvent(overlay, event));
            });
        }
        if (node.getOnMouseDragged() == null) {
            node.setOnMouseDragged(event -> {
                RenJava.callEvent(new OverlayDragEvent(overlay, event));
            });
        }
    }

    public Node getNode() {
        return node;
    }
}
