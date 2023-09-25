package me.piitex.renjava.api.saves;

public class SaveFileUtils {

    public static String toFormat(String data) {
        StringBuilder builder = new StringBuilder();
        String[] clasz = data.split(";;;;");
        for (String s : clasz) {
            String classname = "Unknown";
            String[] fields = s.split("@@@@");
            boolean clazDetect = false;
            for (String s1 : fields) {
                if (!clazDetect) {
                    classname = s1;
                    builder.append(classname).append(":").append("\n");
                    clazDetect = true;
                } else {
                    String field = s1.split("!!!!")[0];
                    String value = s1.split("!!!!")[1];
                    builder.append("    ").append(field).append(": ").append(value);
                    System.out.println("Class: " + classname);
                    System.out.println("Field: " + field);
                    System.out.println("Value: " + value);
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
