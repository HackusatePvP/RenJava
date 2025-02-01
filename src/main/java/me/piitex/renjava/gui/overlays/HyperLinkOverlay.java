package me.piitex.renjava.gui.overlays;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Border;
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

    public FontLoader getFontLoader() {
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
        } else {
            hyperlink = new Hyperlink(text);
        }
        if (font != null) {
            hyperlink.setFont(font.getFont());
        }
        hyperlink.setBorder(Border.EMPTY);
        hyperlink.setPadding(new Insets(4,0,4,0));
        hyperlink.setTranslateX(getX());
        hyperlink.setTranslateY(getY());
        hyperlink.setOnMouseClicked(event -> {
            RenJava.openLink(link);
            hyperlink.setVisited(false); // This is just kind of stupid. Turns the text transparent.
        });
        setInputControls(hyperlink);
        renderTransitions(hyperlink);

        return hyperlink;
    }
}
