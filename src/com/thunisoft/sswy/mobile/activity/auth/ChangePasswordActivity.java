package com.thunisoft.sswy.mobile.activity.auth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.cache.CourtCache_;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;

/**
 * 修改密码
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_change_password)
public class ChangePasswordActivity extends BaseActivity {

    @ViewById(R.id.tv_ymm)
    TextView tv_ymm;

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
    boolean isZhmp;
    
    @AfterViews
    public void initViews() {
        isZhmp = getIntent().getBooleanExtra("isZhmp", false);
        if (isZhmp) {
            setTitle("密码找回");
            tv_ymm.setVisibility(View.VISIBLE);
        } else {
            setTitle(R.string.item_text_xgmm);
        }
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
        String oldPass = tv_ymm.getText().toString();
        String newPass1 = tv_xmm.getText().toString();
        String newPass2 = tv_qrmm.getText().toString();
        String courtId = CourtCache_.getInstance_(this).getCourtId("");
        doXgmm(courtId, oldPass, newPass1, newPass2);
    }

    @Background
    public void doXgmm(String courtId, String oldPass, String newPass1, String newPass2) {
        BaseResponse br = authLogic.changePassword(courtId, oldPass, newPass1, newPass2, tempSid);
        if (!br.isSuccess()) {
            if (br.isXtcw()) {
                showToast(br.getMessage());
            } else {
                showTips(br.getMessage());
            }
        } else {
        	showToast("修改密码成功");
            finish();
        }
    }

    @UiThread
    public void showTips(String message) {
        tv_tips.setText(message);
    }
}
