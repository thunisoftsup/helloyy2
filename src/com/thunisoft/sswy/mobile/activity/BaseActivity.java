package com.thunisoft.sswy.mobile.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;

/**
 * Activity基类
 * 
 * @author lulg
 * 
 */
public class BaseActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_back:
            finish();
            break;
        }

    }

    /**
     * 设置标题
     */
    public void setTitle(String text) {
        ((TextView) findViewById(R.id.action_title)).setText(text);
    }

    /**
     * 设置标题
     */
    public void setTitle(int resId) {
        setTitle(getResources().getString(resId));
    }

    /**
     * 设置回退按钮
     */
    protected void setBtnBack() {
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    protected String getTextString(int resId) {
        return ((TextView) findViewById(resId)).getText().toString();
    }

    protected void setTextString(int resId, String text) {
        ((TextView) findViewById(resId)).setText(text);
    }

    public void showToast(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
