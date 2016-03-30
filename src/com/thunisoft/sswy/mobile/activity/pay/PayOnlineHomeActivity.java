package com.thunisoft.sswy.mobile.activity.pay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apaches.commons.codec.binary.Base64;
import org.apaches.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.auth.LoginActivity_;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.datasource.PayInfoDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.DownloadRegion;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pay.PayDemoActivity;
import com.thunisoft.sswy.mobile.pojo.PayInfo;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.RSAUtil;
import com.thunisoft.sswy.mobile.util.ServiceUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上支付
 * 
 * @author zcc
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
@EActivity(R.layout.activity_pay_online_home)
public class PayOnlineHomeActivity extends BaseActivity {

	@ViewById(R.id.waitForPay)
	RadioButton waitForPay;

	@ViewById(R.id.payYet)
	RadioButton payYet;

	@ViewById(R.id.payInvalid)
	RadioButton payInvalid;

	@ViewById(R.id.buttonArea)
	LinearLayout buttonArea;
	
	@ViewById(R.id.downloadBtn)
	Button downloadBtn;
	
	@ViewById(R.id.frameLayoutArea)
	FrameLayout frameLayoutArea;
	
	@Bean
	LoginCache aLoginCache;

	@Bean
	ResponseUtilExtr responseUtil;

	@Pref
	ConfigUtils_ configUtils;

	@Bean
	AuthLogic authLogic;

	@Bean
	CourtCache courtCache;

	@Bean
	NetUtils netUtils;

	@Bean
	DownloadRegion downloadRegion;

	@Bean
	PayInfoDao payInfoDao;

	String TAG = "susong51";

	@ViewById(R.id.webView)
	static
	android.webkit.WebView webView;

	public static Cookie cookie = null;

	String payData = "";

	private int type = 1;

	private String cId = "";

	private String moneyNum = "";

	private String moneyType = "";

	private String payNo = "";

	private String time = "";

	private String tempSid = "";

	private String url = "";
	
	static int screenWidth;
	
	boolean payFlag = false;
	
	
	@AfterViews
	public void initView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webView.setDrawingCacheEnabled(true);
		webView.getSettings().setSupportZoom(false);  
		webView.setWebChromeClient(new WebChromeClient());
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "zfb");
//		 webView.loadUrl("http://wx.thunisoft.com/tsxty-lulg/ajxx/ajxxList");
		// url ="xzjf.html";
		webLoadUrl("xzjf.html", true);
	}

	@Click(R.id.waitForPay)
	public void waitForPay() {
		Resources resource = (Resources) this.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.color.white);
		ColorStateList cslBlue = (ColorStateList) resource
				.getColorStateList(R.color.blue);
		waitForPay.setTextColor(csl);
		payYet.setTextColor(cslBlue);
		payInvalid.setTextColor(cslBlue);
		setType(1);
		webView.loadUrl("file:///android_asset/www/xzjf.html");
		url = "xzjf.html";
	}

	@Click(R.id.payYet)
	public void payYet() {
		Resources resource = (Resources) this.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.color.white);
		ColorStateList cslBlue = (ColorStateList) resource
				.getColorStateList(R.color.blue);
		waitForPay.setTextColor(cslBlue);
		payYet.setTextColor(csl);
		payInvalid.setTextColor(cslBlue);
		setType(2);
		webView.loadUrl("file:///android_asset/www/xzjf.html");
		url = "xzjf.html";
	}

	@Click(R.id.payInvalid)
	public void payInvalid() {
		Resources resource = (Resources) this.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.color.white);
		ColorStateList cslBlue = (ColorStateList) resource
				.getColorStateList(R.color.blue);
		waitForPay.setTextColor(cslBlue);
		payYet.setTextColor(cslBlue);
		payInvalid.setTextColor(csl);
		setType(3);
		webView.loadUrl("file:///android_asset/www/xzjf.html");
		url = "xzjf.html";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = 1;
		WindowManager windowManager = getWindowManager();    
	     Display display = windowManager.getDefaultDisplay();    
	     screenWidth = display.getWidth();    
	}

	@Override
	protected void onResume() {
		super.onResume();

		
	}

	/**
	 * 同步一下cookie
	 */
	public static void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		cookieManager.setCookie(url, "33");// cookies是在HttpClient中获得的cookie
		CookieSyncManager.getInstance().sync();
	}
	
	@Click(R.id.downloadBtn)
	public void downloadPic(){
		String sdcard = Environment.getExternalStorageDirectory().toString();
		String filePath = sdcard + "/susong51/";
		// filename
		String fileName = filePath + new Date().getTime() + ".jpg";
		saveFile(getViewBitmap(webView), fileName);
		Toast.makeText(this, "下载成功，请在相册中查看", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File file = new File(fileName);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		this.sendBroadcast(intent);
	}
	
	 public Bitmap getViewBitmap(View v) {

		 v.clearFocus(); //

		 v.setPressed(false); //

		 // 能画缓存就返回false

		 boolean willNotCache = v.willNotCacheDrawing();

		 v.setWillNotCacheDrawing(false);

		 int color = v.getDrawingCacheBackgroundColor();

		 v.setDrawingCacheBackgroundColor(0);

		 if (color != 0) {

		 v.destroyDrawingCache();

		 }

		 v.buildDrawingCache();
		 LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) webView.getLayoutParams(); //取控件textView当前的布局参数  
		 linearParams.height = v.getHeight();// 控件的高强制设成20  
		   
		 linearParams.width = v.getWidth();// 控件的宽强制设成30  
		 
		 Bitmap cacheBitmap = v.getDrawingCache();

		 if (cacheBitmap == null) {

		 return null;

		 }
		 int minHeight;
		 if(url.equals("jfcgx.html")){
			 minHeight = screenWidth*106/360; 
		 }else{
			 minHeight = screenWidth*56/360; 
		 }
		 
		 Bitmap bitmap = Bitmap.createBitmap(cacheBitmap, 0,
				 minHeight, v.getWidth(), v.getHeight()-minHeight);

		 // Restore the view

		 v.destroyDrawingCache();

		 v.setWillNotCacheDrawing(willNotCache);

		 v.setDrawingCacheBackgroundColor(color);

		 return bitmap;

		 }
	 
	 
	 /**
		 * 
		 * 保存Bitmap图片为本地文件
		 */

		public static void saveFile(Bitmap bitmap, String filename) {

			FileOutputStream fileOutputStream = null;

			try {

				fileOutputStream = new FileOutputStream(filename);

				if (fileOutputStream != null) {

					bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);

					fileOutputStream.flush();

					fileOutputStream.close();

				}

			} catch (FileNotFoundException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	private void webLoadUrl(final String argument, final boolean flag) {
		mHandler.post(new Runnable() {
			public void run() {
				webView.loadUrl("file:///android_asset/www/" + argument);
				if (flag) {
					frameLayoutArea.setVisibility(View.VISIBLE);
					buttonArea.setVisibility(View.VISIBLE);
					downloadBtn.setVisibility(View.INVISIBLE);
				} else {
					if(argument.equals("jfcg.html")||argument.equals("jfcgx.html")||argument.equals("jfcga.html")){
						frameLayoutArea.setVisibility(View.VISIBLE);
						buttonArea.setVisibility(View.INVISIBLE);
						downloadBtn.setVisibility(View.VISIBLE);
					}else{
						frameLayoutArea.setVisibility(View.GONE);
					}
				}
				url = argument;
			}
		});
	}
	
	private void webLoadFailUrl(final String argument) {
		Intent intent = new Intent();
		intent.putExtra("url", "jfsbx.html");
		intent.putExtra("cId", cId);
		intent.putExtra("moneyNum", moneyNum);
		intent.putExtra("moneyType", moneyType);
		intent.putExtra("payNo", payNo);
		intent.putExtra("payData", payData);
		intent.setClass(PayOnlineHomeActivity.this, PayFaliedActivity_.class);
		startActivityForResult(intent, 0);
	}

	private Handler mHandler = new Handler();

	final class DemoJavaScriptInterface {

		DemoJavaScriptInterface() {

		}

		/**
		 * This is not called on the UI thread. Post a runnable to invoke
		 * loadUrl on the UI thread.
		 */
		@JavascriptInterface
		public void clickOnAndroid(final String argument) {
			webLoadUrl(argument, true);
		}

		@JavascriptInterface
		public void clickOnCheckPZ(final String argument, final String data) {
			payData = data;
			webLoadUrl(argument, false);
		}

		@JavascriptInterface
		public void clickOnCheckPZ(final String data,final String cIdNew,final String moneyNumNew,final String moneyTypeNew) {
			payData = data;
			cId = cIdNew;
			moneyNum = moneyNumNew;
			moneyType = moneyTypeNew;
		}

		@JavascriptInterface
		public void jsOnAndroidPay(final String cId, final String moneyNum, final String moneyType) {
			mHandler.post(new Runnable() {
				public void run() {
					getPayParams(cId,moneyNum,moneyType);
				}
			});
			

		}

		@JavascriptInterface
		public String jsOnAndroidGetSid() {

			return netUtils.getSid();

		}

		@JavascriptInterface
		public String jsOnAndroidGetJsonData() {
			return payData;
		}

		@JavascriptInterface
		public void jsOnAndroidClose() {
			finish();
		}

		@JavascriptInterface
		public int jsOnAndroidPayType() {
			return getType();
		}

		@JavascriptInterface
		public String jsOnAndroidDzph() {
			return payNo;
		}

		@JavascriptInterface
		public String jsOnAndroidGetTime() {
			return time;
		}

		@JavascriptInterface
		public String jsOnAndroidGetUrl() {
			return netUtils.getServerAddress();
		}

		@JavascriptInterface
		public void jsOnAndroidRelogin() {

			if (aLoginCache.getUserType() == LoginCache.LOGIN_TYPE_NORMAL) {// 普通
				loginBackgroundNormal(aLoginCache.getUserName(),
						aLoginCache.getPassword());

			} else if (aLoginCache.getUserType()  == LoginCache.LOGIN_TYPE_LAYWER_PHONE) {// 验证码
				Intent itt = new Intent();
				itt.setClass(PayOnlineHomeActivity.this, LoginActivity_.class);
				itt.putExtra("message", "此功能需要登录才能使用!");
				startActivityForResult(itt, Constants.REQUEST_CODE_PAY);
			} else {
				Intent itt = new Intent();
				itt.setClass(PayOnlineHomeActivity.this, LoginActivity_.class);
				itt.putExtra("message", "此功能需要登录才能使用!");
				startActivityForResult(itt, Constants.REQUEST_CODE_PAY);
			}

		}
		
		
		@JavascriptInterface
		public void jsOnAndroidCheckPay(String cId) {
			payFlag = false;
			getPayInfoById(cId);
		}
		
		
		
		@JavascriptInterface
		public void jsOnAndroidCheckPzNew(String cIdnew) {
//			getPayInfoByIdAndChangeData(cId);
			cId = cIdnew;
			webLoadUrl("jfcga.html", false);
		}
		
		@JavascriptInterface
		public String jsOnAndroidGetCId() {
			return cId;
		}


		/**
		 * 时间校正
		 * 
		 * @param time
		 * @return
		 */
		@JavascriptInterface
		public long jsOnAndroidcheckTime(String time) {
			String serverTime = ServiceUtils
					.getServerTimeStr(PayOnlineHomeActivity.this);
			Date serverDate = NrcUtils.string2Date(serverTime,
					NrcUtils.YYYY_MM_DD);
			Date timeDate = NrcUtils.string2Date(time, NrcUtils.YYYY_MM_DD);
			if (timeDate.before(serverDate)) {// 早于今天 ，失效
				return 1;
			} else {
				return -2;
			}
		}
	}
	
	/**
	 * 本地判断是否缴费
	 * @param cId
	 */
	private void checkPayOrYet(final String cId) {
		List<PayInfo> payList = payInfoDao.getPayList(0);
		for (int i = 0; i < payList.size(); i++) {
			if (payList.get(i).getCId().equals(cId)) {				
				payFlag = true;
			}
		}
		if(payFlag){
			mHandler.post(new Runnable() {
				public void run() {
					webView.loadUrl("javascript:show()");
				}
			});		
		}else{
			
			mHandler.post(new Runnable() {
				public void run() {
					getPayParams(cId,moneyNum,moneyType);
				}
			});
			
			
			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch (resultCode) {
		case 9000:
			cId = intent.getStringExtra("cId");
			moneyNum = intent.getStringExtra("moneyNum");
			moneyType = intent.getStringExtra("moneyType");
			payNo = intent.getStringExtra("payNo");
			savePayInfo(cId, moneyNum, payNo);
			requestServerTime();
			break;

		case 8002:
			mHandler.post(new Runnable() {
				public void run() {
					webLoadUrl("xzjf.html", true);
				}
			});

			break;
		case 8001:
			mHandler.post(new Runnable() {
				public void run() {
					webLoadFailUrl("jfsb.html");
				}
			});

			break;

		default:
			break;
		}
	}

	private void savePayInfo(String cId,String moneyNum,String payNo) {
		List<PayInfo> payList = new ArrayList<PayInfo>();
		PayInfo payInfo = new PayInfo();
		payInfo.setCId(cId);
		payInfo.setCjfje(moneyNum);
		payInfo.setCjfzh("123");
		payInfo.setCsfzh(PayDemoActivity.SELLER);
		payInfo.setCdzph(payNo);
		payInfo.setCjffs("2");
		payInfo.setCsuccess(0);// 0 失败 1 成功
		payList.add(payInfo);
		payInfoDao.SavePayInfo(payList);
	}
	
	@Background
	public void getPayParams(String cId, String moneyNum, String moneyType){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("wsjfId", cId));
//		paramsNew.add(new BasicNameValuePair("createOrderId", false));
		BaseResponseExtr br = responseUtil.getResponse(
				ResponseUtilExtr.LOAD_GETAPLIPAY, params);
		if (StringUtils.isNotBlank(br.getMsg())) {

			Log.i("22", "loginBackground建立会话失败:" + br.getMsg());
			mHandler.post(new Runnable() {
				public void run() {
					Toast.makeText(PayOnlineHomeActivity.this,
							"未开通企业账号，请联系管理员", Toast.LENGTH_SHORT)
							.show();
				}
			});

		} else {
			Log.i("22", "loginBackground建立会话成功:");
			JSONObject result = br.getResJson();
			try {
				JSONObject jsonData = result.getJSONObject("data");
				String partner = jsonData.getString("partner");
				String sellerEmail = jsonData.getString("sellerEmail");
				
				PayDemoActivity.PARTNER = partner;
				PayDemoActivity.SELLER = sellerEmail;
				getPayNo(cId,partner, sellerEmail,moneyType,"detail",moneyNum);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
		}
	}
	

	@Background
	public void loadDate(Long timestamp) {

		Map<String, Object> params = new HashMap<String, Object>();
		double num = Double.parseDouble(moneyNum);
		String payMoney = String.valueOf(num);
		params.put("id", cId);
		params.put("jfje", payMoney);
		params.put("jfzh", "123");
		params.put("sfzh", PayDemoActivity.SELLER);
		params.put("dzph", payNo);
		params.put("jffs", "2");
		params.put("timestamp", String.valueOf(timestamp));
		String encryptedParams = null;
		try {
			encryptedParams = encryptParams(params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String key = cId + "-" + payMoney + "-" + "123" + "-"
				+ PayDemoActivity.SELLER + "-" + payNo + "-" + "2" + "-"
				+ String.valueOf(timestamp) + "-" + "ilovedzfy";
		try {
			key = encryptKey(key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<NameValuePair> paramsNew = new ArrayList<NameValuePair>();
		paramsNew.add(new BasicNameValuePair("jfjg", encryptedParams));
		paramsNew.add(new BasicNameValuePair("rzm", key));

		BaseResponseExtr br = responseUtil.getResponse(
				ResponseUtilExtr.LOAD_WSJFHD, paramsNew);
		if (StringUtils.isNotBlank(br.getMsg())) {

			Log.i("22", "loginBackground建立会话失败:" + br.getMsg());
//			webLoadUrl("jfsbx.html", false);
			webLoadFailUrl("jfsbx.html");

		} else {
			Log.i("22", "loginBackground建立会话成功:");
			webLoadUrl("jfcgx.html", false);
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
	 * 支付宝参数加密
	 * @throws UnsupportedEncodingException 
	 */
	private String encryptPayParams(String paramsJson) throws UnsupportedEncodingException {
		String encryptedParams = Base64.encodeBase64URLSafeString(paramsJson.getBytes("UTF-8"));
		return encryptedParams;
	}

	/**
	 * 律协参数key加密
	 */
	private String encryptKey(String key) throws UnsupportedEncodingException {

		key = DigestUtils.md5Hex(key.toString());

		return key;
	}

	/**
	 * 向服务器发送请求，获取服务器当前时间
	 */
	@Background
	protected void requestServerTime() {
		ServerTimeResponse serverTimeResponse = new ServerTimeResponse();
		String url = netUtils.getMainAddress() + "/api/server/getTime";//
		serverTimeResponse.clearParams();
		serverTimeResponse.getResponse(url, serverTimeResponse.getParams());
	}

	private class ServerTimeResponse extends JSONParsor<BaseResponse> {

		@Override
		public BaseResponse parseToBean(String result) {
			Gson gson = new Gson();
			return gson.fromJson(result, new TypeToken<BaseResponse>() {
			}.getType());
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public BaseResponse getResponse(String urls, List<NameValuePair> params) {
			BaseResponse cr = new BaseResponse();
			try {
				String result = netUtils.post(urls, params);
				if (result != null) {
					JSONObject resJson = new JSONObject(result);
					boolean success = resJson.getBoolean("success");
					if (success) {
						String serverTimeStr = resJson.getString("data");
						time = serverTimeStr.substring(0, 19);
						Date currTime = NrcUtils.string2Date(serverTimeStr,
								NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);
						loadDate(currTime.getTime());
					} else {

					}
				} else {
					webLoadFailUrl("jfsbx.html");
				}
			} catch (SSWYNetworkException e) {
				cr.setSuccess(false);
				cr.setMessage(e.getMessage());
				webLoadFailUrl("jfsbx.html");
			} catch (Exception e) {
				cr.setSuccess(false);
				cr.setMessage(e.getMessage());
			}
			return cr;
		}

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 普通用户登录时后台处理
	 */
	@Background
	public void loginBackgroundNormal(String userName, String userPassword) {

		BaseResponse br = authLogic.getTempSid();
		if (!br.isSuccess()) {
			Log.i(TAG, "loginBackground建立会话失败:" + br.getMessage());;
		} else {
			tempSid = br.getTempSid();
			Log.i(TAG, "loginBackground建立会话成功:" + tempSid);
			loginNormal(userName, userPassword);
		}

	}

	/**
	 * 普通用户登录
	 * 
	 * @param userName
	 * @param userPassword
	 */
	@Background
	public void loginNormal(String userName, String userPassword) {

		BaseResponse br = authLogic.login(tempSid, "", userName, userPassword,
				LoginCache.LOGIN_TYPE_NORMAL, null, null, null);
		if (br.isSuccess()) {

		} else if (br.isXtcw()) {

		} else {
			if (br.getErrorShowType() != null) {
				if (Constants.ERROR_SHOW_YZM == br.getErrorShowType()
						.intValue()) {
					Log.i(TAG, "重新获取验证码");

				} else if (Constants.ERROR_REQUEST_SID == br.getErrorShowType()
						.intValue()) {
					Log.e(TAG, "Tempsid不存在，重新请求。。。。");
					BaseResponse tempBr = authLogic.getTempSid();
					if (tempBr.isSuccess()) {
						Log.i(TAG, "重新获取会话成功，重新登录");
						tempSid = tempBr.getTempSid();
						loginNormal(userName, userPassword);
					} else {
						Log.i(TAG, "重新获取会话失败");
					}
				}
			} else {
				showToast(br.getMessage());
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (url.equals("xzjf.html")) {
				finish();
			} else {
				mHandler.post(new Runnable() {
					public void run() {
						webView.loadUrl("file:///android_asset/www/xzjf.html");
						url = "xzjf.html";
						frameLayoutArea.setVisibility(View.VISIBLE);
						buttonArea.setVisibility(View.VISIBLE);
						downloadBtn.setVisibility(View.INVISIBLE);
					}
				});
			}
			return false;
		}
		return true;

	}
	
	/**
	 * 获取订单号
	 * @param partner
	 * @param seller_id
	 * @param subject
	 * @param body
	 * @param price
	 */
	@Background
	public void getPayNo(String cId,String partner,String seller_id, String subject, String body, String price){
		List<NameValuePair> paramsNew = new ArrayList<NameValuePair>();
		paramsNew.add(new BasicNameValuePair("payType", "2"));
		paramsNew.add(new BasicNameValuePair("wsjfId", cId));
		BaseResponseExtr br = responseUtil.getResponse(
				ResponseUtilExtr.LOAD_PAYNO, paramsNew);
		if (StringUtils.isNotBlank(br.getMsg())) {

			Log.i("22", "loginBackground建立会话失败:" + br.getMsg());

		} else {
			Log.i("22", "loginBackground建立会话成功:");
			JSONObject result = br.getResJson();
			String out_trade_no= "";
			try {
				JSONObject jsonData = result.getJSONObject("data");
				out_trade_no = jsonData.getString("orderId");
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			getSignInfo(cId,partner, seller_id, out_trade_no, subject, body, price);
		}
	}
	
	/**
	 * 参数签名
	 * @param partner
	 * @param seller_id
	 * @param out_trade_no
	 * @param subject
	 * @param body
	 * @param price
	 */
	@Background
	public void getSignInfo(String cId,String partner,String seller_id,String out_trade_no,String subject, String body, String price){
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("partner", partner);
//		params.put("seller_id", seller_id);
//		params.put("out_trade_no", out_trade_no);
//		params.put("subject", subject);
//		params.put("body", body);
//		params.put("total_fee", price);
//		params.put("notify_url", "http://notify.msp.hk/notify.htm");
//		params.put("service", "mobile.securitypay.pay");
//		params.put("payment_type", "1");
//		params.put("_input_charset", "utf-8");
//		params.put("it_b_pay", "30m");
//		params.put("return_url", "m.alipay.com");
		
		String orderInfo = getOrderInfo(partner, seller_id, out_trade_no, subject, body, price);
		
		String encryptedParams = null;
		try {
			encryptedParams = encryptPayParams(orderInfo);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<NameValuePair> paramsNew = new ArrayList<NameValuePair>();
		paramsNew.add(new BasicNameValuePair("partner", partner));
		paramsNew.add(new BasicNameValuePair("signType", "RSA"));
		paramsNew.add(new BasicNameValuePair("signParams", encryptedParams));
		paramsNew.add(new BasicNameValuePair("paramType", "2"));
		paramsNew.add(new BasicNameValuePair("charset", "utf-8"));
		
		BaseResponseExtr br = responseUtil.getResponse(
				ResponseUtilExtr.LOAD_SIGNPRAMAS, paramsNew);
		if (StringUtils.isNotBlank(br.getMsg())) {

			Log.i("22", "loginBackground建立会话失败:" + br.getMsg());

		} else {
			Log.i("22", "loginBackground建立会话成功:");
			JSONObject result = br.getResJson();
			String payInfo= "";
			try {
				JSONObject jsonData = result.getJSONObject("data");
				payInfo = jsonData.getString("sign");
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Intent intent = new Intent();
			intent.putExtra("cId", cId);
			intent.putExtra("moneyNum", price);
			intent.putExtra("moneyType", subject);
			intent.putExtra("payNo", out_trade_no);
			intent.putExtra("payInfo", payInfo);
			intent.putExtra("payData", payData);
			intent.setClass(PayOnlineHomeActivity.this, PayDemoActivity.class);
			startActivityForResult(intent, 0);
		}
		
	}
	
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String partner,String seller_id,String out_trade_no,String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + partner + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + seller_id + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	@Background
	public void getPayInfoById(String cId){
		List<NameValuePair> paramsNew = new ArrayList<NameValuePair>();
		paramsNew.add(new BasicNameValuePair("id", cId));
		BaseResponseExtr br = responseUtil.getResponse(
				ResponseUtilExtr.LOAD_GETPAYORNOT, paramsNew);
		if (StringUtils.isNotBlank(br.getMsg())) {

			Log.i("22", "loginBackground建立会话失败:" + br.getMsg());

		} else {
			Log.i("22", "loginBackground建立会话成功:");
			JSONObject result = br.getResJson();
			String nState= "";
			try {
				JSONObject jsonData = result.getJSONObject("data");
				nState = jsonData.getString("NState");
				if(nState.equals("1")){//代缴费
					checkPayOrYet(cId);
				}else{
					mHandler.post(new Runnable() {
						public void run() {
							webView.loadUrl("javascript:show()");
						}
					});
					
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Background
	public void getPayInfoByIdAndChangeData(String cId){
		List<NameValuePair> paramsNew = new ArrayList<NameValuePair>();
		paramsNew.add(new BasicNameValuePair("id", cId));
		BaseResponseExtr br = responseUtil.getResponse(
				ResponseUtilExtr.LOAD_GETPAYORNOT, paramsNew);
		if (StringUtils.isNotBlank(br.getMsg())) {

			Log.i("22", "loginBackground建立会话失败:" + br.getMsg());

		} else {
			Log.i("22", "loginBackground建立会话成功:");
			JSONObject result = br.getResJson();
			String nState= "";
			try {
				JSONObject jsonData = result.getJSONObject("data");
				nState = jsonData.getString("NState");
				payData = JSON.toJSONString(jsonData);				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
