package me.piitex.renjava.api.scenes.types.choices;

import me.piitex.renjava.gui.builders.ButtonBuilder;

public class Choice {
    private ButtonBuilder builder;
    private final String id;
    private String text; // This can be final but maybe someone wants to modify the text in some way? I don't know could be cool to see

    public Choice(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public ButtonBuilder getBuilder() {
        return builder;
    }

    public String getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


}
