package com.luud.two;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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
                byte[] salt = generateSalt();
                byte[] encrypted = encryptMessage(message, passwordCharArray, salt);
                nullCharArray(passwordCharArray);
                System.out.println("Encrypted: " + encrypted.toString());
                System.out.println("Salt: " + new String(salt));

                File output = new File(fileName);
                output.createNewFile();

                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output));
                oos.writeObject(salt);
                oos.writeObject(encrypted);
            } else {
                System.out.println("Enter the name of the file");
                String fileName = br.readLine();
                System.out.println("Enter the password");
                char[] passwordCharArray = br.readLine().toCharArray();

                FileInputStream fis = new FileInputStream(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                byte[] salt = (byte[]) ois.readObject();
                byte[] encrypted = (byte[])ois.readObject();
                String decrypted = decryptMessage(encrypted, passwordCharArray, salt);
                if(!decrypted.equals("")) {
                    System.out.println("Message: " + decrypted);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void nullCharArray(char[] chars) {
        for(int i = 0; i < chars.length; i++) {
            chars[i] = 0;
        }
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] encryptMessage(String message, char[] password, byte[] salt) {
        byte[] encrypted = null;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

            PBEKeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "PBEWithMD5AndDES");

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 65536);
            cipher.init(Cipher.ENCRYPT_MODE, secret, pbeParameterSpec);
            encrypted = cipher.doFinal(message.getBytes());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return encrypted;
    }

    public static String decryptMessage(byte[] message, char[] password, byte[] salt) {
        String decrypted = "";
        try {
            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 65536);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
            pbeKeySpec.clearPassword();

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);

            byte[] resultbytes = cipher.doFinal(message);
            decrypted = new String(resultbytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("Wrong password");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return decrypted;
    }
}
