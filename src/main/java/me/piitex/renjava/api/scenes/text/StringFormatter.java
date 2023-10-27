package me.piitex.renjava.api.scenes.text;

import java.util.HashMap;
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

    // Just some testing.
    public static void main(String[] args) {
        String text = "Hey {i}there!{/i} I {b}HOPE{/b} you are doing good!";
        /*Map<String, String> formatMap = getFormattedParts(text);
        formatMap.entrySet().forEach(stringStringEntry -> {
            String content = stringStringEntry.getKey();
            String format = stringStringEntry.getValue();
            System.out.println("Content: " + content);
            System.out.println("Format: " + format);
        });*/
    }
}