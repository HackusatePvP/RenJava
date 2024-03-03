package me.piitex.renjava.api.scenes.text;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.configuration.RenJavaConfiguration;

import java.util.LinkedList;

public class StringFormatter {

    public static LinkedList<Text> formatText(String dialogue) {
        // If anyone wants to take a crack at this and optimize it be my guest.

        boolean italic = false;
        boolean bold = false;
        boolean strikeOut = false;
        int formatBeginChar = 0;

        LinkedList<String> parts = new LinkedList<>();
        String beforeText = "";
        for (int i = 0; i < dialogue.length(); i++) {
            char c = dialogue.charAt(i);
            if (c == '{' && dialogue.charAt(i + 1) == 'i') {
                formatBeginChar = i + 1;
                italic = true;
            } else if (c == '{' && dialogue.charAt(i + 1) == 'b') {
                formatBeginChar = i + 1;
                bold = true;
            } else if (c == '{' && dialogue.charAt(i + 1) == 's') {
                formatBeginChar = i + 1;
                strikeOut = true;
            } else if (c == '{' && italic) {
                String italicText = dialogue.substring(formatBeginChar, i);
                parts.add(beforeText.replace(italicText, "").replace("/i}", "").replace("/b}", "").replace("/s}", ""));
                parts.add("iiii: " + italicText.replace("i}", ""));
                beforeText = "";
                italic = false;
            } else if (c == '{' && bold) {
                String boldText = dialogue.substring(formatBeginChar, i);
                parts.add(beforeText.replace(boldText, "").replace("/i}", "").replace("/b}", "").replace("/s}", ""));
                parts.add("bbbb: " + boldText.replace("b}", ""));
                beforeText = "";
                bold = false;
            } else if (c == '{' && strikeOut) {
                String strikeOutText = dialogue.substring(formatBeginChar, i);
                parts.add(beforeText.replace(strikeOutText, "").replace("/s}", "").replace("/b}", "").replace("/i}", ""));
                parts.add("ssss: " + strikeOutText.replace("s}", ""));
                beforeText = "";
                strikeOut = false;
            } else {
                // Process text that is not formatted.
                beforeText += c;
            }
        }
        beforeText = beforeText.replace("/i}", "").replace("/b}", "").replace("/s}", "");
        parts.add(beforeText);

        LinkedList<Text> texts = new LinkedList<>();

        RenJavaConfiguration configuration = RenJava.getInstance().getConfiguration();
        Font currentFont = configuration.getDialogueFont().getFont();
        Font italicFont = Font.font(currentFont.getFamily(), FontWeight.NORMAL, FontPosture.ITALIC, currentFont.getSize());
        Font boldFont = Font.font(currentFont.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, currentFont.getSize());

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
            } else if (s.startsWith("ssss: ")) {
                s = s.replace("ssss: ", "");
                Text text1 = new Text(s);
                text1.setFont(currentFont);
                text1.setStrikethrough(true);
                texts.add(text1);
            } else {
                Text text1 = new Text(s);
                text1.setFont(currentFont);
                texts.add(text1);
            }
        }
        return texts;
    }

    // Testing function. Should be removed later
    public static void main(String[] args) {
        String data = "{i}Heey...{/i} {s}I've been waiting for you.{/s} You were {b}SUPPOSED{/b} to be here by now.";

        boolean italic = false;
        boolean bold = false;
        boolean strikeOut = false;

        int formatBeginChar = 0;

        LinkedList<String> parts = new LinkedList<>();
        String beforeText = "";
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == '{' && data.charAt(i + 1) == 'i') {
                formatBeginChar = i + 1;
                italic = true;
            } else if (c == '{' && data.charAt(i + 1) == 'b') {
                formatBeginChar = i + 1;
                bold = true;
            } else if (c == '{' && data.charAt(i + 1) == 's') {
                formatBeginChar = i + 1;
                strikeOut = true;
            } else if (c == '{' && italic) {
                String italicText = data.substring(formatBeginChar, i);
                System.out.println("Italic Text: " + italicText);
                parts.add(beforeText.replace(italicText, "").replace("/i}", "").replace("/b}", "").replace("/s}", ""));
                parts.add(italicText.replace("i}", ""));
                beforeText = "";
                italic = false;
            } else if (c == '{' && bold) {
                String boldText = data.substring(formatBeginChar, i);
                System.out.println("Bold Text: " + boldText);
                System.out.println("Before Text: " + beforeText);
                parts.add(beforeText.replace(boldText, "").replace("/i}", "").replace("/b}", "").replace("/s}", ""));
                parts.add(boldText.replace("b}", ""));
                beforeText = "";
                bold = false;
            } else if (c == '{' && strikeOut) {
                String strikeOutText = data.substring(formatBeginChar, i);
                System.out.println("StrikeOut Text: " + strikeOutText);
                System.out.println("Before Text: " + beforeText);
                parts.add(beforeText.replace(strikeOutText, "").replace("/s}", "").replace("/b}", "").replace("/i}", ""));
                parts.add(strikeOutText.replace("s}", ""));
                beforeText = "";
                strikeOut = false;
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