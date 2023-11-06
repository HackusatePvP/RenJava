package me.piitex.renjava.api.scenes.text;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.configuration.RenJavaConfiguration;

import java.util.LinkedList;

public class StringFormatter {


    public static LinkedList<Text> formatText(String dialogue) {
        boolean italic = false;
        boolean bold = false;
        int formatBeginChar = 0;

        LinkedList<String> parts = new LinkedList<>();
        String beforeText = "";
        for (int i = 0; i < dialogue.length(); i++) {
            char c = dialogue.charAt(i);
            if (c == '{' && dialogue.charAt(i + 1) == 'i') {
                formatBeginChar = i + 1;
                italic = true;
            } else if (c == '{' && dialogue.charAt(i + 1) == 'b') {
                formatBeginChar = i + 3;
                bold = true;
            } else if (c == '{' && italic) {
                String italicText = dialogue.substring(formatBeginChar, i);
                parts.add(beforeText.replace(italicText, "").replace("/i}", "").replace("/b}", ""));
                parts.add("iiii: " + italicText.replace("i}", ""));
                beforeText = "";
                italic = false;
            } else if (c == '{' && bold) {
                String boldText = dialogue.substring(formatBeginChar, i);
                parts.add(beforeText.replace(boldText, "").replace("/i}", "").replace("/b}", ""));
                parts.add("bbbb " + boldText);
                beforeText = "";
                bold = false;
            } else {
                // Process text that is not formatted.
                beforeText += c;
            }
        }
        beforeText = beforeText.replace("/i}", "").replace("/b}", "");
        parts.add(beforeText);

        LinkedList<Text> texts = new LinkedList<>();
        String text = "";

        RenJavaConfiguration configuration = RenJava.getInstance().getConfiguration();
        Font currentFont = configuration.getDefaultFont().getFont();
        Font italicFont = configuration.getItalicFont().getFont();
        Font boldFont = configuration.getBoldFont().getFont();

        for (String s : parts) {
            if (s.startsWith("bbbb: ")) {
                s = s.replace("bbbb: ", "");
                Text text1 = new Text(s);
                text1.setFont(boldFont);
                texts.add(text1);
            } else if (s.startsWith("iiii: ")) {
                s = s.replace("iiii: ", "");
                Text text1 = new Text(s);
                text1.setFont(italicFont);
                texts.add(text1);
            } else {
                Text text1 = new Text(s);
                text1.setFont(currentFont);
                texts.add(text1);
            }
            text += s;
        }
        return texts;
    }

    // Testing function. Should be removed later
    public static void main(String[] args) {
        String data = "I can’t tell you {i}who{/i} made it...and I sure as hell can’t tell you why {i}you{/i} were special enough to be given something like this.";

        boolean italic = false;
        boolean bold = false;

        int formatBeginChar = 0;

        LinkedList<String> parts = new LinkedList<>();
        String beforeText = "";
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == '{' && data.charAt(i + 1) == 'i') {
                formatBeginChar = i + 1;
                italic = true;
            } else if (c == '{' && data.charAt(i + 1) == 'b') {
                formatBeginChar = i + 3;
                bold = true;
            } else if (c == '{' && italic) {
                String italicText = data.substring(formatBeginChar, i);
                System.out.println("Italic Text: " + italicText);
                parts.add(beforeText.replace(italicText, "").replace("/i}", "").replace("/b}", ""));
                parts.add(italicText.replace("i}", ""));
                beforeText = "";
                italic = false;
            } else if (c == '{' && bold) {
                String boldText = data.substring(formatBeginChar, i);
                System.out.println("Bold Text: " + boldText);
                System.out.println("Before Text: " + beforeText);
                parts.add(beforeText.replace(boldText, "").replace("/i}", "").replace("/b}", ""));
                parts.add(boldText);
                beforeText = "";
                bold = false;
            } else {
                // Process text that is not formatted.
                beforeText += c;
                System.out.println("Before Text: " + beforeText);
            }
        }
        beforeText = beforeText.replaceAll("/i}", "").replace("/b}", "");
        parts.add(beforeText);
        String text = "";
        for (String s : parts) {
            text += s;
        }
        System.out.println("Text: " + text);
    }
}