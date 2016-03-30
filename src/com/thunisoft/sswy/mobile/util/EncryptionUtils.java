/*
 * Copyright 2001-2013 Thunisoft,Inc. All rights reserved.
 * Company: 北京紫光华宇软件股份有限公司
 */
package com.thunisoft.sswy.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.util.Base64;
import android.util.Log;


/**
 * @author qiw
 * @version 1.0, 2013-3-21
 */
@EBean(scope=Scope.Singleton)
public class EncryptionUtils {
    private static final String TAG = "EncryptionUtils";
    public static final String PUB_KEY = "ELKWKLKNNRD22I3U4IU2I34LNKNKENR";
    
    /**
     * 加密后并转化为16进制表示的字符串
     * 
     * @param str
     * @return
     */
    public  String encrypt(String str) {
        if (str == null) {
            return str;
        }
        try {
            byte[] bytes = PUB_KEY.getBytes(SSWYConstants.CHARSET_UTF8);
            byte[] data = str.getBytes(SSWYConstants.CHARSET_UTF8);
            int len = bytes.length;
            for (int i = 0, j = 0; i < data.length; i++, j++) {
                if (i % len == 0) {
                    j = 0;
                }
                data[i] = (byte) (data[i] ^ bytes[j]);
            }
            return encodeHex(data);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "contains non utf-8 character", e);
        }
        return null;
    }

    /**
     * 解密字符串
     * 
     * @param str
     * @return
     */
    public  String decrypt(String str) {
        if (str == null) {
            return str;
        }
        try {
            byte[] bytes = PUB_KEY.getBytes(SSWYConstants.CHARSET_UTF8);
            byte[] data = decodeHex(str);
            int len = bytes.length;
            for (int i = 0, j = 0; i < data.length; i++, j++) {
                if (i % len == 0) {
                    j = 0;
                }
                data[i] = (byte) (data[i] ^ bytes[j]);
            }
            return new String(data, SSWYConstants.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "contains non utf-8 character", e);
        }
        return null;
    }

    /**
     * 对字符串的字节数组使用位异或算法加密后再使用Base64加密成字符串
     * 
     * @param str
     *            原字符串
     * @return 加密后的字符串
     */
    public  String encryptBase64(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] byteArr = str.getBytes(SSWYConstants.CHARSET_UTF8);
            byte[] keyBytes = PUB_KEY.getBytes(SSWYConstants.CHARSET_UTF8);
            int len = keyBytes.length;
            for (int i = 0, j = 0; i < byteArr.length; i++, j++) {
                if (i % len == 0) {
                    j = 0;
                }
                byteArr[i] = (byte) (byteArr[i] ^ keyBytes[j]);
            }
            return Base64.encodeToString(byteArr, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "contains non utf-8 character", e);
        }
        return null;
    }

    /**
     * 对base64字符串进行解密后再使用位异或算法解密成字符串
     * 
     * @param base64Str
     * @return 解密后的字符串
     */
    public  String decryptBase64(String base64Str) {
        String result = null;
        try {
            result = new String(decryptBase64ToBytes(base64Str), SSWYConstants.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "contains non utf-8 character", e);
        }
        return result;
    }

    /**
     * 对base64字符串进行解密后再使用位异或算法解密成byte[]
     * 
     * @param base64Str
     * @return 解密后的字符串
     */
    public  byte[] decryptBase64ToBytes(String base64Str) {
        if (base64Str == null) {
            return null;
        }
        try {
            byte[] byteArr = Base64.decode(base64Str, Base64.DEFAULT);
            byte[] keyBytes = PUB_KEY.getBytes(SSWYConstants.CHARSET_UTF8);
            int len = keyBytes.length;
            for (int i = 0, j = 0; i < byteArr.length; i++, j++) {
                if (i % len == 0) {
                    j = 0;
                }
                byteArr[i] = (byte) (byteArr[i] ^ keyBytes[j]);
            }
            return byteArr;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "contains non utf-8 character", e);
        }
        return null;
    }

    /**
     * 把byte数组编码为16进制字符串
     * 
     * @param raw
     * @return
     */
    public  String encodeHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        StringBuilder hex = new StringBuilder(2 * raw.length);
        for (byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4));
            hex.append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    /**
     * 把16进制字符串解码为byte数组
     * 
     * @param str
     * @return
     */
    public  byte[] decodeHex(String str) {
        if (str == null) {
            return null;
        }
        char[] chars = str.toCharArray();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; (i < chars.length && i + 1 < chars.length); i = i + 2) {
            int high = HEXES.indexOf(chars[i]);
            int low = HEXES.indexOf(chars[i + 1]);
            out.write(high * 16 + low);
        }
        return out.toByteArray();
    }

    public static final String HEXES = "0123456789ABCDEF";

}
