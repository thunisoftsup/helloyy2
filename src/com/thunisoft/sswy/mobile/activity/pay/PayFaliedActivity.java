package com.thunisoft.sswy.mobile.activity.pay;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;

@EActivity(R.layout.activity_pay_failed)
public class PayFaliedActivity extends BaseActivity{
	
	@ViewById(R.id.webView)
	static
	android.webkit.WebView webView;
		
	String cId = "";
	String moneyNum = "";
	String moneyType = "";
	String payNo = "";
	
	String signInfo = "";
	
	String payData = "";
	
	String url = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		cId = intent.getStringExtra("cId");
		moneyNum = intent.getStringExtra("moneyNum");
		moneyType = intent.getStringExtra("moneyType");
		payNo = intent.getStringExtra("payNo");
		signInfo = intent.getStringExtra("payInfo");
		payData = intent.getStringExtra("payData");
	}

	@AfterViews
	public void initView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webView.setDrawingCacheEnabled(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "zfb");
		webView.loadUrl("file:///android_asset/www/" + url);
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
			mHandler.post(new Runnable() {
				public void run() {
					webView.loadUrl("file:///android_asset/www/" + argument);
					
				}
			});
		}
		
		@JavascriptInterface
		public String jsOnAndroidGetJsonData() {
			return payData;
		}
		
		@JavascriptInterface
		public void jsOnAndroidPayAgain() {
			Intent intent = new Intent();
			intent.putExtra("cId", cId);
			intent.putExtra("moneyNum", moneyNum);
			intent.putExtra("moneyType", moneyType);
			intent.putExtra("payNo", payNo);
			intent.putExtra("payInfo", signInfo);
			intent.putExtra("payData", payData);
			setResult(8001, intent);
			finish();
		}
		
		@JavascriptInterface
		public void jsOnAndroidClose() {
			setResult(8002);
			finish();
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {			
			setResult(8002);
			finish();
		}
		return true;

	}

}
