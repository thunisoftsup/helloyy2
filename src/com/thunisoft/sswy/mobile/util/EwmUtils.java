package com.thunisoft.sswy.mobile.util;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;

import android.util.Base64;
import android.util.Log;

import com.thunisoft.sswy.mobile.exception.SSWYException;

/**
 * 二维码操作工具类
 * 
 * @author lulg
 * 
 */
public class EwmUtils {
    private static final String TAG = "EwmUtils";

    /**
     * 编码二维码信息
     * 
     * @param message
     * @return
     * @throws SSWYException
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public static String enCodeEwmCode(String message) throws SSWYException {
        try {
            PublicKey pubKey = RSAUtil.getPublicKey(RSAUtil.EWM_PUBLIC);
            byte[] encryptCode = RSAUtil.encrypt(pubKey, message.getBytes("utf-8"));
            return Base64.encodeToString(encryptCode, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "编码二维码信息出错", e);
            throw new SSWYException("编码二维码信息出错");
        }
    }

    /**
     * 解码二维码信息
     * 
     * @param secretCode
     * @throws SSWYException
     */
    public static String decodeEwmCode(String secretCode) throws SSWYException {
        if (StringUtils.isBlank(secretCode)) {
            return secretCode;
        }
        try {
            byte[] decodeBytes = Base64.decode(secretCode, Base64.DEFAULT);
            PrivateKey privateKey = RSAUtil.getPrivateKey(RSAUtil.EWM_PRIVATE);
            return RSAUtil.decryptFromBytes(privateKey, decodeBytes);
        } catch (Exception e) {
            Log.e(TAG, "编码二维码信息出错", e);
            throw new SSWYException("解析二维码出错");
        }
    }
}
