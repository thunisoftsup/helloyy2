package com.thunisoft.sswy.mobile.activity.auth;

import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;

/**
 * 找回密码界面
 * 
 */

@EActivity(R.layout.activity_zhmm)
public class ZhmmActivity extends BaseActivity {

    @ViewById(R.id.tv_tips)
    TextView tv_tips;
    @ViewById(R.id.btn_send_yzm)
    Button fsyzm;
    @Bean
    AuthLogic authLogic;

    public static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @AfterViews
    public void initViews() {
        setBtnBack();
        setTitle("找回密码");
        fsyzm.setOnClickListener(this);
        findViewById(R.id.btn_xyb).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_send_yzm:
            fsyzm();
            break;
        case R.id.btn_xyb:
            zhmm();
            break;
        }

        super.onClick(v);
    }

    public void fsyzm() {
        fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_sel);
        fsyzm.setClickable(false);
        String userName = getTextString(R.id.tv_yhm);
        String phone = getTextString(R.id.tv_sjhm);
        doFsyzm(userName, phone);
    }

    @Background
    public void doFsyzm(String userName, String phone) {
        BaseResponse br = authLogic.zhmmfsyzm(userName, phone);
        if (!br.isSuccess()) {
            showToast(br.getMessage());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_default);
                    fsyzm.setClickable(true);
                }
            });
        } else {
            
            showToast("验证码发送成功");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        int flag = 60;
                        @Override
                        public void run() {
                            flag -- ;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    fsyzm.setText(flag+"s后重发送");
                                    fsyzm.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_input_text_size_));
                                }
                            });
                            if (flag == 0) {
                                timer.cancel();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_default);
                                        fsyzm.setClickable(true);
                                        fsyzm.setText("获取验证码");
                                        fsyzm.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_input_text_size));
                                    }
                                });
                            }
                        }
                    }, 0, 1000);
                }
            });
        }
    }

    public void zhmm() {
        String userName = getTextString(R.id.tv_yhm);
        String phone = getTextString(R.id.tv_sjhm);
        String yzm = getTextString(R.id.tv_yzm);
        zhmm(userName, phone, yzm);
    }

    @Background
    public void zhmm(String userName, String phone, String yzm) {
        BaseResponse br = authLogic.zhmm(userName, phone, yzm);
        if (!br.isSuccess()) {
            showToast(br.getMessage());
        } else {
            gotoChangePassword(br.getTempSid());
        }
    }

    @UiThread
    public void gotoChangePassword(String tempSid) {
        Intent intent = new Intent(this, PubChangePasswordActivity_.class);
        intent.putExtra("tempSid", tempSid);
        startActivity(intent);
    }
}
