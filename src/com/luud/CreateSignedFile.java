package com.luud;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class CreateSignedFile {
    public static void main(String[] args) {
        try {
            //Initialize the signature
            PrivateKey privateKey = getPrivateKey();
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);

            //Update the signature according to the text
            FileInputStream fis = new FileInputStream("input.ext");
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufin.read(buffer)) >= 0) {
                signature.update(buffer, 0, len);
            }
            bufin.close();

            //Sign the signature
            byte[] signatureBytes = signature.sign();

            //Create the new file
            File output = new File("input(signedbylk).ext");
            output.createNewFile();

            //Add contents to the file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output));
            oos.writeInt(signatureBytes.length);
            oos.write(signatureBytes);
            oos.writeObject(readInputFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private static String readInputFile() {
        String text = "";

        try {
            text = new String(Files.readAllBytes(Paths.get("input.ext")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        try {
            File privateKeyFile = new File("private");

            FileInputStream fis = new FileInputStream(privateKeyFile);
            byte[] pkBytes = new byte[(int) privateKeyFile.length()];
            fis.read(pkBytes);

            KeyFactory keyFactory = null;

            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(pkBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return privateKey;
    }
}
