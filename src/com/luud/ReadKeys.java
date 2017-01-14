package com.luud;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadKeys {
    public static void main(String[] args) {
        try {
            File privateKeyFile = new File("private");

            FileInputStream fis = new FileInputStream(privateKeyFile);
            byte[] pkBytes = new byte[(int)privateKeyFile.length()];
            fis.read(pkBytes);

            System.out.println(Base64.encode(pkBytes));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
