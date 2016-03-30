package com.thunisoft.sswy.mobile.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

public class DzfyCryptanalysis {

	static KeyPair keyPair = null;
    static{
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            keyPair = keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
    public static String encrypt(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Generate keys
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        byte[] e = encrypt(publicKey, str.getBytes());
        return bytesToString(e);
    }

    public static String decrypt(String encryptStr) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // Generate keys
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        byte[] de = decrypt(privateKey, StringTobytes(encryptStr));
        return new String(de);
    }

    /**
     * Change byte array to String.
     * @return byte[]
     */
    protected static String bytesToString(byte[] encrytpByte) {
        StringBuilder result = new StringBuilder();
        for (Byte bytes : encrytpByte) {
            result.append(bytes);
            result.append(";");
        }
        return result.toString();
    }
    
    /**
     * Change byte array to String.
     * @return byte[]
     */
    protected static byte[] StringTobytes(String str) {
        String[] s = str.split(";");
        byte[] b = new byte[s.length];
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                continue;
            }
            b[i] = Byte.parseByte(s[i]);
        }

        return b;
    }

    /**
     * Encrypt String.
     * @return byte[]
     */
    protected static byte[] encrypt(RSAPublicKey publicKey, byte[] obj) {
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** */
    /**
    * Basic decrypt method
    * @return byte[]
    */
    protected static byte[] decrypt(RSAPrivateKey privateKey, byte[] obj) {
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
