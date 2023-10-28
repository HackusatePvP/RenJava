package me.piitex.renjava.api.scenes.text;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.FontLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringFormatter {
    public static Map<String, String> getFormattedParts(String text) {
        Map<String, String> formatMap = new HashMap<>();
        String[] parts = text.split("\\{");

        for (String part : parts) {
            if (part.contains("}")) {
                String[] subParts = part.split("\\}");
                String format = subParts[0];
                if (format.startsWith("/")) {
                    continue;
                }
                String content = subParts.length > 1 ? subParts[1] : "";
                System.out.println("Format: " + format);
                System.out.println("Content: " + content);
                formatMap.put(content, format);
            }
        }

        return formatMap;
    }

    public static List<Text> formatString(String string) {
        List<Text> toReturn = new ArrayList<>(); // This may not be ordered properly.

        // Note: To format a string you must pull out all formatted text.
        // Example: {i}Heeeyyy{/i} I hope you are doing {i}good{/i}...
        // All text must be ordered once its separated as well.

        // Lets loop the string until a { is found.

        // Add a flag when parsing formatted part.
        boolean formatted = false;
        StringBuilder part = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            if (!formatted) {
                char c = string.charAt(i);
                if (c == '{') {
                    formatted = true;
                    // Once the format is hit create a new text object with the current part.
                    toReturn.add(new Text(part.toString()));
                    continue;
                }
                part.append(c);
            } else {
                // Once formatted the current char should {
                if (string.charAt(i) == '{') {
                    // Check the formatted type.
                    char formatChar = string.charAt(i + 1);

                    StringBuilder formatPart = new StringBuilder();
                    if (formatChar == 'i') {
                        // Once its italic simply follow the format.
                        // i}Italic Text here{
                        // End the text at the {
                        for (int a = i + 3; a < string.length(); a++) {
                            if (string.charAt(a) != '{') {
                                formatPart.append(string.charAt(a));
                            } else {
                                // Hit the end of the syntax {
                                Text text = new Text(formatPart.toString());
                                Font font = new FontLoader(RenJava.getInstance().getDefaultFont().getFont(), FontPosture.ITALIC, RenJava.getInstance().getConfiguration().getTextSize()).getFont();
                                text.setFont(font);
                                toReturn.add(text);
                                formatted = false;
                                i = a + 2;
                            }
                        }
                    } else if (formatChar == 'b') {
                        for (int a = i + 3; a < string.length(); a++) {
                            if (string.charAt(a) != '{') {
                                formatPart.append(string.charAt(a));
                            } else {
                                // Hit the end of the syntax {
                                Text text = new Text(formatPart.toString());
                                Font font = new FontLoader(RenJava.getInstance().getDefaultFont().getFont(), FontWeight.BOLD, RenJava.getInstance().getConfiguration().getTextSize()).getFont();
                                text.setFont(font);
                                toReturn.add(text);
                                formatted = false;
                                i = a + 2;
                            }
                        }
                    }
                }
            }
        }

        return toReturn;
    }

    // Just some testing.
    public static void main(String[] args) {
        List<String> toReturn = new ArrayList<>();
        String string = "Hey {i}there!{/i} I {b}HOPE{/b} you are doing good!";
        boolean formatted = false;
        StringBuilder part = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            if (!formatted) {
                char c = string.charAt(i);
                if (c == '{') {
                    formatted = true;
                    // Once the format is hit create a new text object with the current part.
                    toReturn.add(part.toString());
                    continue;
                }
                part.append(c);
            } else {
                // Once formatted the current char should {
                if (string.charAt(i - 1) == '{') {
                    System.out.println("Format Found...");
                    System.out.println("Before Format: " + part);
                    // Check the formatted type.
                    char formatChar = string.charAt(i);
                    System.out.println("Current index: " + i);
                    System.out.println("Found format char: " + formatChar);
                    StringBuilder formatPart = new StringBuilder();
                    if (formatChar == 'i') {
                        System.out.println("Italic format found.");
                        // Once its italic simply follow the format.
                        // i}Italic Text here{
                        // End the text at the {
                        System.out.println("Scanning italic content: ");
                        for (int a = i + 2; a < string.length(); a++) {
                            if (string.charAt(a) != '{') {
                                formatPart.append(string.charAt(a));
                            } else {
                                toReturn.add(formatPart.toString());
                                System.out.println("Hit end of Format...");
                                System.out.println("Content to Format: " + formatPart.toString());
                                formatted = false;
                                i = a + 4;

                                System.out.println("Expected Char for next run: " + string.charAt(i));
                                break;
                            }
                        }
                    } else if (formatChar == 'b') {
                        for (int a = i + 2; a < string.length(); a++) {
                            if (string.charAt(a) != '{') {
                                formatPart.append(string.charAt(a));
                            } else {
                                toReturn.add(formatPart.toString());
                                System.out.println("Hit end of Format...");
                                System.out.println("Content to Format: " + formatPart.toString());
                                formatted = false;
                                i = a + 4;
                                System.out.println("Expected Char for next run: " + string.charAt(i));
                                break;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("String: " + toReturn.toString());
    }
}