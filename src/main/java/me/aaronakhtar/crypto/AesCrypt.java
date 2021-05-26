package me.aaronakhtar.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AesCrypt {

    public static SecretKey rebuildKey(String encodedKey){
        final byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static String encodeKey(SecretKey secretKey){
        return new String(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }

    public static SecretKey makeKey(int bitSize) throws Exception {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(bitSize);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static boolean encryptFile(SecretKey key, File inputFile, File outputFile) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        copyFile(new FileInputStream(inputFile), new CipherOutputStream(new FileOutputStream(outputFile), cipher));
        return true;
    }

    public static boolean decryptFile(SecretKey key, File inputFile, File outputFile) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
         copyFile(new CipherInputStream(new FileInputStream(inputFile), cipher), new FileOutputStream(outputFile));
        return true;
    }


    private static void copyFile(InputStream inputStream1, OutputStream outputStream1) throws Exception {
        try(InputStream inputStream = inputStream1; OutputStream outputStream = outputStream1;)  {
            int i;
            byte[] barr = new byte[1024];
            while ((i = inputStream.read(barr)) != -1) {
                outputStream.write(barr, 0, i);
            }
        }
    }


}
