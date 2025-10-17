package me.piitex.renjava.api.scenes.text;

import me.piitex.renjava.RenJava;

import me.piitex.renjava.api.loaders.FontLoader;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.overlays.Overlay;
import me.piitex.renjava.gui.overlays.TextOverlay;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class StringFormatter {

    public static LinkedList<Overlay> formatText(String dialogue) {
        // {i} Italic text {/i}
        // {b} Bold text {/b}
        // {s} Strikeout text {/s}
        RenJavaConfiguration configuration = RenJava.CONFIGURATION;
        FontLoader currentFont = configuration.getDialogueFont();
        FontLoader italicFont = configuration.getItalicFont();
        FontLoader boldFont = configuration.getBoldFont();

        LinkedList<Overlay> toReturn = new LinkedList<>();
        String[] split = dialogue.split("\\{");
        for (String s : split) {
            s = s.replaceFirst("/" + "(.*?)" + Pattern.quote("}"), "");
            if (s.isEmpty()) continue;
            if (s.matches("/" + "(.*?)" + Pattern.quote("}"))) {
                continue;
            }
            if (s.startsWith("b}")) {
                s = s.replace("b}", "");
                TextOverlay textOverlay = new TextOverlay(s);
                textOverlay.setFont(boldFont);
                toReturn.add(textOverlay);
            } else if (s.startsWith("i}")) {
                s = s.replace("i}", "");
                TextOverlay textOverlay = new TextOverlay(s);
                textOverlay.setFont(italicFont);
                toReturn.add(textOverlay);
            } else if (s.startsWith("s}")) {
                s = s.replace("b}", "");
                TextOverlay textOverlay = new TextOverlay(s);
                textOverlay.setFont(currentFont);
                textOverlay.setStrikeout(true);
                toReturn.add(textOverlay);
            } else {
                TextOverlay textOverlay = new TextOverlay(s);
                textOverlay.setFont(currentFont);
                toReturn.add(textOverlay);
            }
        }
        return toReturn;
    }
}