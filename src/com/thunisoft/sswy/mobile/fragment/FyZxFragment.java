package com.thunisoft.sswy.mobile.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.CourtCache_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.CourtLogic;
import com.thunisoft.sswy.mobile.logic.response.CourtResponse;
import com.thunisoft.sswy.mobile.pojo.TCourt;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.view.SafeWebView;

@EFragment(R.layout.fragment_fyzx)
public class FyZxFragment extends Fragment {
	@Bean
	CourtCache courtCahce;
	@ViewById(R.id.webView)
    SafeWebView webview;

    @Bean
    CourtLogic courtLogic;

    @Pref
    ConfigUtils_ configUtils;
    
    @Bean
    CourtCache courtCache;

    String courtId;

    @ViewById(R.id.tv_tips)
    TextView tv_tips;

    @ViewById(R.id.btn_refresh)
    Button btn_refresh;
    @Bean
    LoginCache loginCache;
    String currentUrl;
    String preCourtId;
    @SuppressLint("SetJavaScriptEnabled")
    @AfterViews
    public void initViews() {
        courtId = courtCache.getCourtId();
        preCourtId = courtId;
//        webview.getSettings().setSupportZoom(true);
      //重新设置websettings  
        WebSettings s = webview.getSettings();
        s.setBuiltInZoomControls(true);   
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);  
        s.setUseWideViewPort(true);   
        s.setLoadWithOverviewMode(true);  
        s.setSavePassword(true);   
        s.setSaveFormData(true);  
        s.setJavaScriptEnabled(true);  
        s.setDomStorageEnabled(true);
        webview.requestFocus(); 
        webview.setScrollBarStyle(0);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("URL", url);
                if (url.indexOf("tel:") < 0) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("ErrorCode", errorCode + "");
                Log.e("Error", description);
                showNoData("抱歉，您访问的页面不存在");
            }
        });
        webview.addJavascriptInterface(new JsObject(), "onBackPress");
        btn_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCourtInfo();
            }
        });
        if (StringUtils.isBlank(courtId)) {
            showNoData("没有选择法院");
            return;
        }
        loadData();
        
    }
    
    class JsObject {
    	@JavascriptInterface
    	public void onBackPress() {
    		webview.post(new Runnable() {
				@Override
				public void run() {
					WebBackForwardList history = webview.copyBackForwardList();
					if (history != null && history.getCurrentIndex() != 0) {
						webview.goBack();
					}
				}
			});
    	}
    }
   
    @Override
    public void onResume() {
        super.onResume();
        if (!courtCache.getCourtId().equals(preCourtId)) {
        	 preCourtId = courtCache.getCourtId();
        	loadData();
        	WebBackForwardList history = webview.copyBackForwardList();
        	webview.clearHistory();
        }
    }
    
    @Background
    public void loadCourtInfo() {
        courtId = courtCache.getCourtId();
        CourtResponse cr = courtLogic.loadCourtInfo(courtId);
        if (!cr.isSuccess()) {
            showWebview(currentUrl);
        } else {
            TCourt court = cr.getCourt();
            if (court == null) {
                court = courtLogic.getCourt(courtId);
            }
            try {
            	if (cr.getOpenModules() != null) {
            		loginCache.setOpenModule(new JSONArray(cr.getOpenModules().toString()));
            	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
            if (court.getNJsfs() != null && court.getNJsfs().intValue() == Constants.JSFS_ZJ) {
                courtCache.setZj(true);
            } else {
                courtCache.setZj(false);
            }
            courtCache.setSsfwUrl(court.getCSsfwUrl());
            courtCache.setWapUrl(court.getCWapUrl());
            showWebview(court.getCWapUrl());
        }
    }
    
    @UiThread
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Background
    public void loadData() {
        /*TCourt court = courtLogic.getCourt(courtId);*/
        String wapUrl = CourtCache_.getInstance_(getActivity()).getWapUrl("");
        if (StringUtils.isNotBlank(wapUrl)) {
            showWebview(wapUrl);
        } else {
            showNoData("抱歉，您访问的页面不存在");
        }
    }

    @UiThread
    public void showWebview(String url) {
        tv_tips.setVisibility(View.GONE);
        btn_refresh.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
//        webview.loadUrl("http://172.16.161.252/susong51/app/fyonline/2400/cpws.htm");
        if(StringUtils.isBlank(url)) {
            showNoData("抱歉，您访问的页面不存在");
            return;
        }
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        currentUrl = url;
        webview.loadUrl(currentUrl);
    }

    @UiThread
    public void showNoData(String message) {
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setText(message);
        btn_refresh.setVisibility(View.VISIBLE);
        webview.setVisibility(View.GONE);
    }
    
    public boolean onBackPress() {
    	WebBackForwardList history = webview.copyBackForwardList();
        if (history != null && history.getCurrentIndex() != 0) {
        	webview.goBack();
        	return true;
        }
        return false;
    }


}
