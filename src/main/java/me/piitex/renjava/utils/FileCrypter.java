package me.piitex.renjava.utils;

import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.loggers.RenLogger;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Utility used for encrypting save files. All encryption keys and algorithms are below. You can turn on file encryption in the {@link RenJavaConfiguration} class.
 * <p>
 *     The algorithm used is AES. The pass key is 'RenJava`. The security key is 'PBKDF2WithHmacSHA256'.
 */
public class FileCrypter {
    private static final String PBKDF_ALG = "PBKDF2WithHmacSHA256";

    // All files will be encrypted with the same pass key
    private static final String passKey = "RenJava";

    public static void encryptFile(File file, File outputFile) {
        SecretKeyFactory factory;

        try {
            factory = SecretKeyFactory.getInstance(PBKDF_ALG);
            KeySpec spec = new PBEKeySpec(passKey.toCharArray(), "salt".getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            FileInputStream inputStream = new FileInputStream(file);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
            inputStream.close();
            outputStream.close();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                 IllegalBlockSizeException | IOException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static void decryptFile(File file, File outputFile) {
        SecretKeyFactory factory;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            factory = SecretKeyFactory.getInstance(PBKDF_ALG);
            KeySpec spec = new PBEKeySpec(passKey.toCharArray(), "salt".getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
            inputStream.close();
            outputStream.close();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                  IOException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            RenLogger.LOGGER.error("Could not decrypt file. The file may not be in an encrypted state", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    RenLogger.LOGGER.error("Unable to close input stream!");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    RenLogger.LOGGER.error("Unable to close output stream!");
                }
            }
        }
    }
}
