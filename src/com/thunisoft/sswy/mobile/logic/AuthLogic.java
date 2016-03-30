package com.thunisoft.sswy.mobile.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import org.apache.http.NameValuePair;
import org.apaches.commons.codec.binary.Base64;
import org.apaches.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Environment;
//import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.thunisoft.sswy.mobile.activity.auth.LoginActivity;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.net.GywResponseUtil;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtil;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.logic.response.GywResponse;
import com.thunisoft.sswy.mobile.util.EncryptionUtils;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.RSAUtil;
import com.thunisoft.sswy.mobile.util.SSWYConstants;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;

/**
 * 用户验证逻辑类
 * 
 * @author lulg
 * 
 */
@EBean(scope = Scope.Singleton)
public class AuthLogic {
	private static final String TAG = "AuthLogic";

	@Bean
	NetUtils netUtils;

	@Bean
	LoginCache loginCache;

	@Bean
	CourtCache courtCache;

	@Bean
	EncryptionUtils encryptionUtils;

	@Bean
	ResponseUtil responseUtil;

	@Bean
	GywResponseUtil gywResponseUtil;

	/**
	 * 修改密码
	 * 
	 * @param oldPass
	 * @param newPass1
	 * @param newPass2
	 * @return
	 */
	public BaseResponse changePassword(String courtId, String oldPass,
			String newPass1, String newPass2, String tempSid) {
		String url = netUtils.getServerAddress()
				+ "/mobile/pro/updatePassword.htm";
		responseUtil.clearParams().addSecretParam("oldPass", oldPass)
				.addSecretParam("courtId", courtId)
				.addSecretParam("newPass1", newPass1)
				.addSecretParam("newPass2", newPass2);
		if (StringUtils.isNotBlank(tempSid)) {
			responseUtil.addParam("sid", tempSid);
		}
		return responseUtil.getResponse(url, responseUtil.getParams());
	}

	/**
	 * 修改密码
	 * 
	 * @param oldPass
	 * @param newPass1
	 * @param newPass2
	 * @return
	 */
	public BaseResponse changePassword(String courtId, String newPass1,
			String newPass2, String tempSid) {
		String url = netUtils.getServerAddress()
				+ "/mobile/pub/updatePassword.htm";
		responseUtil.clearParams().addSecretParam("courtId", courtId)
				.addSecretParam("newPass1", newPass1)
				.addSecretParam("newPass2", newPass2);
		if (StringUtils.isNotBlank(tempSid)) {
			responseUtil.addParam("tempSid", tempSid);
		}
		return responseUtil.getResponse(url, responseUtil.getParams());
	}

	/**
	 * 关于我
	 * 
	 * @return
	 */
	public GywResponse gyw() {
		String url = netUtils.getServerAddress() + "/mobile/pro/gyw.htm";
		return gywResponseUtil.getResponse(url, responseUtil.getEmptyParams());
	}

	/**
	 * 加载基本用户信息
	 * 
	 * @return
	 */
	public GywResponse loadBasicUserInfo() {
		String url = netUtils.getServerAddress()
				+ "/mobile/pro/loadBasicUserInfo.htm";
		return gywResponseUtil.getResponse(url, responseUtil.getEmptyParams());
	}

	/**
	 * 找回密码发送验证码
	 * 
	 * @param userName
	 * @param phone
	 * @return
	 */
	public BaseResponse zhmmfsyzm(String userName, String phone) {
		String url = netUtils.getServerAddress()
				+ "/mobile/sendCheckCodeForFindBackPassword.htm";
		List<NameValuePair> params = responseUtil.clearParams()
				.addSecretParam("userName", userName)
				.addSecretParam("phone", phone)
				.addParam("courtId", courtCache.getCourtId("")).getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 验证码实名认证，发送验证码
	 * 
	 * @param phone
	 * @return
	 */
	public BaseResponse srmzfsyzm(String phone) {
		String url = netUtils.getServerAddress()
				+ "/mobile/pro/sendCheckCodeForRealnameAuthen.htm";
		List<NameValuePair> params = responseUtil.clearParams()
				.addSecretParam("phone", phone)
				.addParam("courtId", courtCache.getCourtId("")).getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 注册，发送验证码
	 * 
	 * @param phone
	 * @return
	 */
	public BaseResponse zcfsyzm(String phone) {
		String url = netUtils.getServerAddress()
				+ "/mobile/sendCheckCodeForRegister.htm";
		List<NameValuePair> params = responseUtil.clearParams()
				.addSecretParam("phone", phone)
				.addParam("courtId", courtCache.getCourtId("")).getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 注册
	 * 
	 * @param phone
	 * @return
	 */
	public BaseResponse zc(String userName, String password, String repwd,
			String realName, String cardType, String cardID, String phone,
			String sjyzm, String tempSid) {
		String url = netUtils.getServerAddress() + "/mobile/doRegister.htm";
		List<NameValuePair> params = responseUtil.clearParams()
				.addSecretParam("userName", userName)
				.addSecretParam("password", password)
				.addSecretParam("repwd", repwd)
				.addSecretParam("realName", realName)
				.addSecretParam("cardType", cardType)
				.addSecretParam("cardID", cardID)
				.addSecretParam("phone", phone).addSecretParam("sjyzm", sjyzm)
				.addParam("tempSid", tempSid).getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 使用注册信息实名认证
	 * 
	 * @return
	 */
	public BaseResponse zcxxsmrz() {
		String url = netUtils.getServerAddress()
				+ "/mobile/pro/doVerifyWidthRegistInfo.htm";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 验证码实名认证
	 * 
	 * @param nameYzm
	 * @param cardIdYzm
	 * @param phoneCode
	 * @param verifyConflict
	 * @return
	 */
	public BaseResponse yzmsmrz(String nameYzm, String cardIdYzm, String phone,
			String phoneCode, String verifyConflict) {
		String url = netUtils.getServerAddress()
				+ "/mobile/pro/doYzmVerify.htm";
		List<NameValuePair> params = responseUtil.clearParams()
				.addSecretParam("nameYzm", nameYzm)
				.addSecretParam("cardIdYzm", cardIdYzm)
				.addSecretParam("phone", phone)
				.addSecretParam("phoneCode", phoneCode)
				.addParam("verifyConflict", verifyConflict).getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 找回密码
	 * 
	 * @param userName
	 * @param phone
	 * @param yzm
	 * @return
	 */
	public BaseResponse zhmm(String userName, String phone, String yzm) {
		String url = netUtils.getServerAddress()
				+ "/mobile/checkProRetrievePwdInfo.htm";
		List<NameValuePair> params = responseUtil.clearParams()
				.addSecretParam("userName", userName)
				.addSecretParam("phone", phone).addSecretParam("yzm", yzm)
				.getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 查询码实名认证
	 * 
	 * @param nameCxm
	 * @param cardIdCxm
	 * @param cxm
	 * @param verifyConflict
	 * @return
	 */
	public BaseResponse cxmsmrz(String nameCxm, String cardIdCxm, String cxm,
			String verifyConflict) {
		String url = netUtils.getServerAddress()
				+ "/mobile/pro/doCxmVerify.htm";

		List<NameValuePair> params = responseUtil.clearParams()
				.addSecretParam("nameCxm", nameCxm)
				.addSecretParam("cardIdCxm", cardIdCxm)
				.addSecretParam("cxm", cxm)
				.addParam("verifyConflict", verifyConflict).getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 二维码律师认证
	 * 
	 * @param secretCode
	 * @return
	 */
	public BaseResponse ewmlsrz(String secretCode, String verifyConflict) {
		String url = netUtils.getServerAddress()
				+ "/mobile/pro/doEwmVerify.htm";
		List<NameValuePair> params = responseUtil.clearParams()
				.addParam("secretCode", secretCode)
				.addParam("sfrzbg", loginCache.isSfrzbg() ? "1" : "0")
				.addParam("verifyConflict", verifyConflict).getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 二维码律师认证
	 * 
	 * @param secretCode
	 * @return
	 */
	public BaseResponse getQRCode(String userName, long timestamp) {
		String url = loginCache.getLxUrl() + "serviceAction/lawyerEwm.htm";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", userName);
		params.put("timestamp", timestamp);
		try {
			String encryptedParams = encryptParams(params);
			String key = userName + timestamp + "LxxtDzfyEwm";
			key = encryptKey(key);
			List<NameValuePair> postParams = responseUtil.clearParams()
					.addParam("params", encryptedParams).addParam("key", key)
					.getParams();
			responseUtil.getQRCodeResponse(url, postParams);
		} catch (Exception e) {
			Log.e(TAG, "拉取二维码失败");
		}
		return null;
	}

	/**
	 * 律协参数加密
	 */
	private String encryptParams(Map<String, Object> params)
			throws UnsupportedEncodingException, Exception {
		String paramsJson = JSON.toJSONString(params);
		String encryptedParams = Base64.encodeBase64URLSafeString(RSAUtil
				.encrypt(RSAUtil.getPublicKey(RSAUtil.QRCODE_PUBLIC),
						paramsJson.getBytes("UTF-8")));
		return encryptedParams;
	}

	/**
	 * 律协参数key加密
	 */
	private String encryptKey(String key) throws UnsupportedEncodingException {
		// String str = UUIDHelper.digest(key);
		// key = URLEncoder.encode(str, "UTF-8");

		key = URLEncoder.encode(DigestUtils.md5Hex(key.toString()), "utf-8");

		return key;
	}

	/**
	 * 检测图片验证码
	 * 
	 * @return
	 */
	public BaseResponse jcyzm(String yzm) {
		String url = netUtils.getServerAddress() + "/mobile/checkTpyzm.htm";
		responseUtil.clearParams().addParam("yzm", yzm);
		return responseUtil.getResponse(url, responseUtil.getParams());
	}

	/**
	 * 获得临时sessionID
	 * 
	 * @return
	 */
	public BaseResponse getTempSid() {
		String url = netUtils.getServerAddress() + "/mobile/getTempSid.htm";
		return responseUtil.getResponse(url, responseUtil.clearParams()
				.getParams());
	}

	/**
	 * 查询码查询案件, 获得临时sessionID
	 * 
	 * @return
	 */
	public BaseResponse getAjxxTempSid() {
		String url = netUtils.getServerAddress()
				+ "/mobile/ajxx/getTempSid.htm";
		return responseUtil.getResponse(url, responseUtil.clearParams()
				.getParams());
	}

	/**
	 * 律师认证
	 * 
	 * @param name
	 * @param cardId
	 * @param zyCardId
	 * @param verifyCode
	 * @param verifyConflict
	 * @return
	 */
	public BaseResponse lsrz(String name, String cardId, String zyCardId,
			String verifyCode, String verifyConflict) {
		String url = netUtils.getServerAddress() + "/mobile/pro/doLsVerify.htm";
		List<NameValuePair> params = responseUtil.clearParams()
				.addSecretParam("name", name).addSecretParam("cardId", cardId)
				.addSecretParam("zyCardId", zyCardId)
				.addSecretParam("verifyCode", verifyCode)
				.addParam("verifyConflict", verifyConflict).getParams();
		return responseUtil.getResponse(url, params);
	}

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public BaseResponse login(String tempSid, String yzm, String username,
			String password, int userType, String token, String phoneCode,
			String phoneNumber) {
		netUtils.lockWrite();
		try {
			netUtils.setSId(null);
		} finally {
			netUtils.unlockWrite();
		}
		String loginUrl = netUtils.getServerAddress()
				+ "/mobile/v3_0/login.htm";
		;
		if (userType == LoginCache.LOGIN_TYPE_LAYWER_PHONE) {
			responseUtil.clearParams().addSecretParam("j_username", username)
					.addParam("loginType", "pro").addParam("login_to", "to_lx")
					.addSecretParam("login_lx_login_type", "3")
					.addSecretParam("login_lx_sjhm", phoneNumber)
					.addSecretParam("login_lx_sjyzm", phoneCode);
		} else if (userType == LoginCache.LOHIN_TYPE_LAYWER_TOKEN) {
			responseUtil.clearParams().addSecretParam("j_username", username)
					.addSecretParam("j_password", password)
					.addParam("loginType", "pro").addParam("login_to", "to_lx")
					.addSecretParam("login_lx_login_type", "2")
					.addSecretParam("login_lx_dzlp", token);
		} else {
			responseUtil.clearParams().addSecretParam("j_username", username)
					.addSecretParam("j_password", password)
					.addParam("loginType", "pro");
		}
		// if(userType == LoginCache.LOGIN_TYPE_LS_VERIFID){
		// responseUtil.addParam("login_to", "to_lx");
		// }
		if (StringUtils.isNotBlank(tempSid)) {
			responseUtil.addParam("tempSid", tempSid);
		}
//		 if (StringUtils.isNotBlank(yzm)) {
//		 responseUtil.addParam("yzm", yzm);
//		 }

		BaseResponse br = new BaseResponse();
		try {
			String result = netUtils.post(loginUrl, responseUtil.getParams());
			if (result != null) {
				JSONObject jsonObj = new JSONObject(result);
				boolean success = jsonObj.getBoolean("success");
				if (success) {
					loginCache.setLogined(true);
					loginCache.setUserType(userType);
					loginCache.setUserName(username);
					loginCache.setPassword(password);
					loginCache.setPhone(jsonObj.has("phone") ? jsonObj
							.getString("phone") : "");
					loginCache.setXm(jsonObj.has("xm") ? jsonObj
							.getString("xm") : "");
					loginCache.setLoginType(jsonObj.has("loginType") ? jsonObj
							.getInt("loginType") : 0);
					loginCache.setLoginType(jsonObj.has("loginType") ? jsonObj
							.getInt("loginType") : 0);
					loginCache.setUserId(jsonObj.has("userId") ? jsonObj
							.getString("userId") : "");					
					loginCache.setZjhm(jsonObj.has("zjhm") ? jsonObj
							.getString("zjhm") : "");
					loginCache.setZjLx(jsonObj.has("zjLx") ? jsonObj
							.getString("zjLx") : "");
					br.setTempSid(jsonObj.has("tempSid") ? jsonObj
							.getString("tempSid") : null);
					br.setErrorShowType(jsonObj.has("errorShowType") ? jsonObj
							.getInt("errorShowType") : null);
					br.setXtcw(false);
					netUtils.lockWrite();
					try {
						netUtils.setSId(jsonObj.getString("sid"));
					} finally {
						netUtils.unlockWrite();
					}
					br.setSuccess(true);
				} else {
					loginCache.clear();
					br.setSuccess(false);
					br.setTempSid(jsonObj.has("tempSid") ? jsonObj
							.getString("tempSid") : null);
					br.setErrorShowType(jsonObj.has("errorShowType") ? jsonObj
							.getInt("errorShowType") : null);
					br.setXtcw(false);
					br.setMessage(jsonObj.getString("message"));
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, SSWYConstants.ERROR_JSONPARSE, e);
			br.setSuccess(false);
			br.setXtcw(true);
			br.setMessage(SSWYConstants.ERROR_JSONPARSE);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			br.setSuccess(false);
			br.setXtcw(true);
			br.setMessage(e.getMessage());
		}
		return br;
	}

	public BaseResponse getPhoneCode(String userName, String phoneNumber) {

		 String url = loginCache.getLxUrl()+"serviceAction/yzm.htm";
//		String url = "http://172.18.9.39:8080/susong51-lx/serviceAction/yzm.htm";
		String timestamp = String.valueOf(System.currentTimeMillis());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("zyzh", userName);
		params.put("sjhm", phoneNumber);
		params.put("timestamp", timestamp);
		BaseResponse br = new BaseResponse();
		try {
			String encryptedParams = encryptParams(params);
			String key = userName + phoneNumber + timestamp + "LxxtDzfyEwm";
			key = encryptKey(key);
			List<NameValuePair> postParams = responseUtil.clearParams()
					.addParam("params", encryptedParams).addParam("key", key)
					.getParams();
			String result = netUtils.post(url, postParams);
			if (StringUtils.isNotBlank(result)) {
				PrivateKey privateKey = RSAUtil
						.getPrivateKey(RSAUtil.QRCODE_PRIVATE);
				byte[] resultRSA = Base64.decodeBase64(result);
				String resultStr = new String(RSAUtil.decrypt(privateKey,
						resultRSA), "utf-8");
				JSONObject jsonObj = new JSONObject(resultStr);
				boolean success = jsonObj.getBoolean("success");
				if (success) {
					br.setSuccess(true);
				}else{
					br.setSuccess(false);
					br.setMessage(jsonObj.getString("message"));
				}
			}
		} catch (Exception e) {
			br.setMessage(e.getMessage());
			br.setSuccess(false);
			br.setXtcw(true);
		}
		return br;
	}
}
