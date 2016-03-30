package com.thunisoft.sswy.mobile.activity.auth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.CourtCache_;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;

/**
 * 修改密码
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_change_password_pub)
public class PubChangePasswordActivity extends BaseActivity {

    @ViewById(R.id.tv_xmm)
    TextView tv_xmm;

    @ViewById(R.id.tv_qrmm)
    TextView tv_qrmm;

    @ViewById(R.id.tv_tips)
    TextView tv_tips;

    @Bean
    AuthLogic authLogic;
    
    @Extra("tempSid")
    String tempSid;
    
    @Pref
    ConfigUtils_ configUtils;
    

    @AfterViews
    public void initViews() {
        setTitle(R.string.item_text_zhmm);
        findViewById(R.id.btn_xgmm).setOnClickListener(this);
        setBtnBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_xgmm:
            xgmmClick();
            break;
        }
        super.onClick(v);
    }

    public void xgmmClick() {
        String newPass1 = tv_xmm.getText().toString();
        String newPass2 = tv_qrmm.getText().toString();
        String courtId = CourtCache_.getInstance_(this).getCourtId("");
        doXgmm(courtId, newPass1, newPass2);
    }

    @Background
    public void doXgmm(String courtId, String newPass1, String newPass2) {
        BaseResponse br = authLogic.changePassword(courtId,newPass1, newPass2, tempSid);
        if (!br.isSuccess()) {
            if (br.isXtcw()) {
                showToast(br.getMessage());
            } else {
                showTips(br.getMessage());
            }
        } else {
            showToast("修改密码成功");
            if(ZhmmActivity.instance != null) {
                ZhmmActivity.instance.finish();
            }
            finish();
        }
    }

    @UiThread
    public void showTips(String message) {
        tv_tips.setText(message);
    }
}
