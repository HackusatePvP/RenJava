package me.piitex.renjava.api.scenes.text;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;

import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.overlays.Overlay;
import me.piitex.renjava.gui.overlays.TextOverlay;

import java.util.LinkedList;

public class StringFormatter {

    public static LinkedList<Overlay> formatText(String dialogue) {
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

        LinkedList<Overlay> texts = new LinkedList<>();

        RenJavaConfiguration configuration = RenJava.getInstance().getConfiguration();
        FontLoader currentFont = configuration.getDialogueFont();
        FontLoader italicFont = new FontLoader(currentFont.getFont(), FontWeight.NORMAL, FontPosture.ITALIC, currentFont.getSize());
        FontLoader boldFont = new FontLoader(currentFont.getFont(), FontWeight.BOLD, FontPosture.REGULAR, currentFont.getSize());

        for (String s : parts) {
            TextOverlay text1 = new TextOverlay(s);
            if (s.startsWith("bbbb: ")) {
                s = s.replace("bbbb: ", "");
                text1.setFontLoader(boldFont);
                texts.add(text1);
            } else if (s.startsWith("iiii: ")) {
                s = s.replace("iiii: ", "");
                text1.setFontLoader(italicFont);
                texts.add(text1);
            } else if (s.startsWith("ssss: ")) {
                s = s.replace("ssss: ", "");
                text1.setFontLoader(currentFont);
                text1.setStrikeout(true);
                texts.add(text1);
            } else {
                text1.setFontLoader(currentFont);
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