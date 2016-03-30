package com.thunisoft.sswy.mobile.activity.auth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;


/**
 * 用户注册协议界面
 * 
 */

@EActivity(R.layout.activity_yhzcxy)
public class YhzcxyActivity extends BaseActivity {
    
    @ViewById(R.id.web_zcxy)
    WebView web_zcxy;
    
    @AfterViews
    public void initViews() {
        setBtnBack();
        setTitle("用户注册协议");
        web_zcxy.getSettings().setSupportZoom(true);
        web_zcxy.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("URL", url);
                if (url.indexOf("tel:") < 0) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        
        web_zcxy.loadUrl("file:///android_asset/html/yhzcxy.html");
    }
}
