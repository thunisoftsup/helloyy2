package com.thunisoft.sswy.mobile.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apaches.commons.codec.binary.Base64;
import org.apaches.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.datasource.PayInfoDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.PayInfo;
import com.thunisoft.sswy.mobile.util.RSAUtil;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;
@EService
public class UpLoadPayInfoService extends Service {
	
	@Bean
    NetUtils netUtils;
	
	@Bean
    ResponseUtilExtr responseUtil;
	
	private static int UPLOAD_INTERNAL = 5*10 * 1000;

	
	@Bean
	PayInfoDao payInfoDao;

	private final IBinder binder = new myBinder();

	public class myBinder extends Binder {
		public UpLoadPayInfoService getService() {

			return UpLoadPayInfoService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		return binder;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}
	
	/**
	 * 
	 */
	Handler handler = new Handler();

	/**
	 * 
	 */
	private Runnable runable;
	
	/**
	 * 上报
	 */
	public void startUploadStorage() {
		runable = new Runnable() {
			@Override
			public void run() {
				handler.postDelayed(runable, UPLOAD_INTERNAL);
				List<PayInfo> payList = payInfoDao.getPayList(0);
				for (PayInfo payInfo : payList) {
					requestServerTime(payInfo);
				}

			}
		};
		handler.post(runable);
	}
	
	
	/**
	 * 向服务器发送请求，获取服务器当前时间
	 */
	@Background
	protected void requestServerTime(PayInfo payInfo) {
		ServerTimeResponse serverTimeResponse = new ServerTimeResponse();
		String url = netUtils.getMainAddress() + "/api/server/getTime";//
		serverTimeResponse.clearParams();
		serverTimeResponse.getResponse(url, serverTimeResponse.getParams(),payInfo);
	}
	
	private class ServerTimeResponse extends JSONParsor<BaseResponse> {

		@Override
		public BaseResponse parseToBean(String result) {
			Gson gson = new Gson();
	        return gson.fromJson(result, new TypeToken<BaseResponse>() {
	        }.getType());
		}

		@SuppressLint("SimpleDateFormat")
		public BaseResponse getResponse(String url, List<NameValuePair> params,PayInfo payInfo) {
			BaseResponse cr = new BaseResponse();
	        try {
	            String result = netUtils.post(url, params);
	            if (result != null) {
	            	JSONObject resJson = new JSONObject(result);
	            	boolean success = resJson.getBoolean("success");
	                if (success) {
	                	String serverTimeStr = resJson.getString("data");
	                	Date currTime = NrcUtils.string2Date(serverTimeStr, NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);	                	 
	                	
	                	loadDate(currTime.getTime(),payInfo);
	                }
	            }
	        } catch (SSWYNetworkException e) {
	            cr.setSuccess(false);
	            cr.setMessage(e.getMessage());
	        } catch (Exception e) {
	            cr.setSuccess(false);
	            cr.setMessage(e.getMessage());
	        }
	        return cr;
		}

		@Override
		public BaseResponse getResponse(String url, List<NameValuePair> params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	
	
    @Background
    public void loadDate(Long timestamp,PayInfo payInfo){    	
    	
		Map<String, Object> params = new HashMap<String, Object>();
		
		double num = Double.parseDouble(payInfo.getCjfje());
		
		String payMoney = String.valueOf(num);
		
		params.put("id", payInfo.getCId());
		params.put("jfje", payMoney);
		params.put("jfzh", payInfo.getCjfzh());
		params.put("sfzh", payInfo.getCsfzh());
		params.put("dzph", payInfo.getCdzph());
		params.put("jffs", payInfo.getCjffs());
		params.put("timestamp", String.valueOf(timestamp));
//		BaseResponse br = new BaseResponse();
		String encryptedParams = null;
		try {
			encryptedParams = encryptParams(params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String key = payInfo.getCId() +"-"+ payMoney+"-"+payInfo.getCjfzh()+"-"+payInfo.getCsfzh()+"-"+payInfo.getCdzph()+"-"+payInfo.getCjffs()+"-"+String.valueOf(timestamp)+"-"+"ilovedzfy";
		try {
			key = encryptKey(key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<NameValuePair> paramsNew = new ArrayList<NameValuePair>();
		paramsNew.add(new BasicNameValuePair("jfjg", encryptedParams));
		paramsNew.add(new BasicNameValuePair("rzm", key));

		if(StringUtils.isNotBlank(netUtils.getSid())){
			BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_WSJFHD, paramsNew);
			if(StringUtils.isNotBlank(br.getMsg())) {	    	 	    	  
				Log.i("22", "loginBackground建立会话失败:"+br.getMsg());
				
			}else {	
				Log.i("22", "loginBackground建立会话成功:");
				payInfo.setCsuccess(1);
				List<PayInfo> payList = new ArrayList<PayInfo>();
				payList.add(payInfo);
				payInfoDao.UpdatePayInfo(payList);
				
			}   	
			
		}
    }
    
	/**
	 * 律协参数加密
	 */
	private String encryptParams(Map<String, Object> params)
			throws UnsupportedEncodingException, Exception {
		String paramsJson = JSON.toJSONString(params);
		String encryptedParams = Base64.encodeBase64URLSafeString(RSAUtil
				.encrypt(RSAUtil.getPublicKey(RSAUtil.QRCODE_PRIVATE_NEW),
						paramsJson.getBytes("UTF-8")));
		return encryptedParams;
	}
	
	
	/**
	 * 律协参数key加密
	 */
	private String encryptKey(String key) throws UnsupportedEncodingException {

		key = DigestUtils.md5Hex(key.toString());

		return key;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		startUploadStorage();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}



}
