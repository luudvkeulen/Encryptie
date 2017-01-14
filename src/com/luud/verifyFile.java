package com.luud;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class verifyFile {
    public static void main(String[] args) {
        PublicKey publicKey = getPublicKey();

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("input(signedbylk).ext"));
            int signatureSize = ois.readInt();
            byte[] signatureBytes = (byte[]) ois.readObject();
            String text = (String) ois.readObject();

            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);

            InputStream fis = new ByteArrayInputStream(text.getBytes());
            BufferedInputStream bis = new BufferedInputStream(fis);

            byte[] buffer = new byte[1024];
            int len;
            while (bis.available() != 0) {
                len = bis.read(buffer);
                signature.update(buffer, 0, len);
            }

            bis.close();

            boolean valid = signature.verify(signatureBytes);
            System.out.println("Signature valid: " + valid);
            if(valid) {
                System.out.println("Text:" + text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
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
