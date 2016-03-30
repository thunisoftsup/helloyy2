package com.thunisoft.sswy.mobile.util;

import java.security.PublicKey;

import android.util.Base64;
import android.util.Log;

public class AppSecretUtil {

    private static final String APP_PUBLIC = "30819f300d06092a864886f70d010101050003818d00308189028181008db77285a817a696483d11d67307868fbfe4f2f4e786699100132132626c2c555d54d784886333f872b48a2ec3a891003160063cb01f9c2e3d1f8d1522870ce365d309662cc12f5b027e730de7cb222c1d63c53129622a6ab6644d56f4752efa85039d2c411c33c68e202938f92dedb7fa31f1df2623b5fc17bafef71cdc6d330203010001";
    
    private static PublicKey pubKey;
    //是否需要律师认证  吉林电子法院不需要，无忧需要
    private static boolean isLawyerCheck = false;

	private static PublicKey getPublicKey() {
        if(pubKey == null) {
            pubKey = RSAUtil.getPublicKey(APP_PUBLIC); 
        }
        return pubKey;
    }

    public static String encodeAppString(String message) {
        try {
            if (StringUtils.isBlank(message)) {
                return "";
            }
            PublicKey pubKey = getPublicKey();
            byte[] encryptCode = RSAUtil.encrypt(pubKey, message.getBytes("utf-8"));
            return Base64.encodeToString(encryptCode, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("AppSecretUtil", "编码app信息出错", e);
            throw new IllegalArgumentException("编码app信息出错");
        }
    }
    
    public static boolean isLawyerCheck() {
		return isLawyerCheck;
	}
}
