package me.piitex.renjava.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDUtils {

    public static String getFileCheckSum(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream inputStream = new FileInputStream(file);

            byte[] bytes = new byte[1024];

            int read;
            while ((read = inputStream.read(bytes)) != -1) {
                md.update(bytes, 0, read);
            }
            byte[] mdBytes = md.digest();
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < mdBytes.length; i++) {
                buffer.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getGameID(String fullLength) {
        // The game id is a sort of unique id given to each game.
        // This method takes the characters of the name and author and converts them to numbers.
        // Once converted into numbers it then takes that number and multiplies it by the position of the character.
        // a   b   c   d   e   f
        // *   *   *   *   *   *
        // 1   2   3   4   5   6
        // =   =   =   =   =   =
        // num1,num2,num3,num4,num5,num6

        // This will then return the sum of all the numbers.
        // sum = num1 + num2 + num3 + num4 + num5 + num6...

        // This is only sort of unique because it will generate the same id given the name and author are the same.

        // Extract character numbers
        int charNumbers = 0;
        int position = 1;
        for (char c : fullLength.toCharArray()) {
           charNumbers += getNumber(c) * position;
           position++;
        }
        return charNumbers;
    }

    private static int getNumber(char c) {
        switch (c) {
            case 'a':
                return 50;
            case 'b':
                return 61;
            case 'c':
                return 72;
            case 'd':
                return 83;
            case 'e':
                return 94;
            case 'f':
                return 105;
            case 'g':
                return 116;
            case 'h':
                return 127;
            case 'i':
                return 138;
            case 'j':
                return 149;
            case 'k':
                return 151;
            case 'l':
                return 162;
            case 'm':
                return 173;
            case 'n':
                return 184;
            case 'o':
                return 195;
            case 'p':
                return 206;
            case 'q':
                return 217;
            case 'r':
                return 228;
            case 's':
                return 239;
            case 't':
                return 241;
            case 'u':
                return 252;
            case 'v':
                return 263;
            case 'w':
                return 284;
            case 'x':
                return 295;
            case 'y':
                return 306;
            case 'z':
                return 317;
        }
        if (Character.isDigit(c)) {
            int a = Integer.parseInt(String.valueOf(c));
            return a * (a + 5);
        }

        return 38;
    }
}
