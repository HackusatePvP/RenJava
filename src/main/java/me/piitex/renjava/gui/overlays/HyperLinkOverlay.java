package me.piitex.renjava.gui.overlays;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.loaders.FontLoader;

public class HyperLinkOverlay extends Overlay {
    private final String link;
    private String text;
    private FontLoader font;

    public HyperLinkOverlay(String link) {
        this.link = link;
    }

    public HyperLinkOverlay(String link, String text) {
        this.link = link;
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public FontLoader getFont() {
        return font;
    }

    public void setFont(FontLoader font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Node render() {
        Hyperlink hyperlink;
        if (text == null || text.isEmpty()) {
            hyperlink = new Hyperlink(link);
            if (font != null) {
                hyperlink.setFont(font.getFont());
            }
        } else {
            Text label = new Text(text);
            if (font != null) {
                label.setFont(font.getFont());
            }
            hyperlink = new Hyperlink(link, label);
        }
        hyperlink.setTranslateX(getX());
        hyperlink.setTranslateY(getY());
        hyperlink.setOnMouseClicked(event -> {
            RenJava.getInstance().getHost().showDocument(link);
        });
        setInputControls(hyperlink);
        renderTransitions(hyperlink);

        return hyperlink;
    }
}
