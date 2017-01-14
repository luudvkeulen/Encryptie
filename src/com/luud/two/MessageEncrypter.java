package com.luud.two;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class MessageEncrypter {
    private static boolean encrypt = false;

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Do you want to encrypt a message? y/n");
        String input;
        try {
            while((input = br.readLine()) != null) {
                if(!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                    System.out.println("Please use y or n");
                    continue;
                } else {
                    if(input.equalsIgnoreCase("y")) {
                        encrypt = true;
                    }
                    break;
                }
            }

            if(encrypt) {
                System.out.println("Enter a password to encrypt the text with");
                char[] passwordCharArray = br.readLine().toCharArray();
                System.out.println("Enter a filename");
                String fileName = br.readLine();
                System.out.println("Enter your message");
                String message = br.readLine();


            } else {
                System.out.println("Enter the name of the file");
                String fileName = br.readLine();
                System.out.println("Enter the password");
                char[] passwordCharArray = br.readLine().toCharArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String encryptMessage(String message, char[] password) {

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[128];
            random.nextBytes(salt);
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}
