package me.piitex.renjava.api.builders;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;

public class TextFlowBuilder {
    private LinkedList<Text> texts = new LinkedList<>();

    private final int width, height;

    public TextFlowBuilder(String text, int width, int height) {
        this.width = width;
        this.height = height;
        texts.add(new Text(text));
    }

    public TextFlowBuilder(Text text, int width, int height) {
        this.width = width;
        this.height = height;
        texts.add(text);
    }

    public TextFlowBuilder(LinkedList<Text> texts, int width, int height) {
        this.width = width;
        this.height = height;
        this.texts = texts;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public LinkedList<Text> getTexts() {
        return texts;
    }

    public TextFlow build() {
        TextFlow textFlow = new TextFlow();
        for (Text text : texts) {
            textFlow.getChildren().add(text);
        }

        textFlow.setPrefSize(width, height);

        return textFlow;
    }
}
