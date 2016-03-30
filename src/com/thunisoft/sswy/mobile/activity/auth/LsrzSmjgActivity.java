package com.thunisoft.sswy.mobile.activity.auth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AlertDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.EwmUtils;


/**
 * 律师认证实名结果（结果？是猜的）界面
 * 
 */

@EActivity(R.layout.activity_lsrz_smjg)
public class LsrzSmjgActivity extends BaseActivity {
    private static final String TAG = "LsrzSmjgActivity";
    @Bean
    AuthLogic authLogic;

    @Bean
    LoginCache loginCache;

    @Extra("message")
    String secretCode;
    
    @ViewById(R.id.tv_lsmc)
    TextView tv_lsmc;
    
    @ViewById(R.id.tv_zjhm)
    TextView text_zjhm;
    
    @ViewById(R.id.tv_zyzh)
    TextView tv_zyzh;
    
    

    @AfterViews
    public void initValues() {
        setTitle(R.string.btn_text_lsrz);
        setBtnBack();
        findViewById(R.id.btn_lsrz).setOnClickListener(this);
        decodeSecretCode();
    }
    
    public void decodeSecretCode() {
        if(secretCode == null) {
            return;
        }
        try {
            String code = EwmUtils.decodeEwmCode(secretCode);
            JSONObject jo = new JSONObject(code);
            String xm = jo.getString("xm");
            String zjhm = jo.getString("zjhm");
            String zyzh = jo.getString("zyzh");
            tv_lsmc.setText(xm);
            text_zjhm.setText(zjhm);
            tv_zyzh.setText(zyzh);
        } catch (SSWYException e) {
            Log.e(TAG, e.getMessage(),e);
            setTextString(R.id.tv_sm_lsrz, "二维码解析失败！");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(),e);
            setTextString(R.id.tv_sm_lsrz, "二维码解析失败！");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_lsrz:
            doLsrz("1");// 验证冲突
            break;
        }
        super.onClick(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_CONFIRM_LSRZ_FGXX && resultCode == Activity.RESULT_OK) {
            doLsrz("0");// 不验证冲突
        }
    }

    @Background
    public void doLsrz(String verifyConflict) {
        BaseResponse br = authLogic.ewmlsrz(secretCode, verifyConflict);
        if (br.isSuccess()) {
            loginCache.setLoginType(LoginCache.LOGIN_TYPE_LS_VERIFID);
            showToast("律师认证成功");
            setResult(RESULT_OK);
            if(LsrzActivity_.instanse != null) {
                LsrzActivity_.instanse.finish();
            }
            finish();
            
        } else {
            Integer errorType = br.getErrorShowType();
            if (errorType != null && errorType.intValue() == Constants.ERROR_SHOW_CONFIRM) {
                Intent intent = new Intent(this, ConfirmDialogActivity_.class);
                intent.putExtra("message", br.getMessage());
                startActivityForResult(intent, Constants.REQUEST_CODE_CONFIRM_LSRZ_FGXX);
            } else if (br.isXtcw()) {
                showToast(br.getMessage());
            } else {
                shoTips(br.getMessage());
            }
        }
    }

    @UiThread
    public void shoTips(String message) {
        Intent intent = new Intent(this, AlertDialogActivity_.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
