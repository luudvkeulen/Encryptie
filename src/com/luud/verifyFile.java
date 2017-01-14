package com.luud;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class verifyFile {
    public static void main(String[] args) {
        PublicKey publicKey = getPublicKey();

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("output(signbylk).ext"));
            int signatureSize = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PublicKey getPublicKey() {
        PublicKey publicKey = null;
        try {
            File publicKeyFile = new File("public");

            FileInputStream fis = new FileInputStream(publicKeyFile);
            byte[] pkBytes = new byte[(int) publicKeyFile.length()];
            fis.read(pkBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(pkBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            e.printStackTrace();
        }

        return publicKey;
    }
}
