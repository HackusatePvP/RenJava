package me.piitex.renjava.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDUtils {

    public static String getFileCheckSum(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream inputStream = new FileInputStream(file);

            byte[] bytes = new byte[1024];

            int read = 0;
            while ((read = inputStream.read(bytes)) != -1) {
                md.update(bytes, 0, read);
            }
            byte[] mdBytes = md.digest();
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < mdBytes.length; i++) {
                buffer.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
