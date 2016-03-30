package com.thunisoft.sswy.mobile.activity.dialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;

@EActivity(R.layout.dialog_building_alert)
public class BuildingAlertDialogActivity extends Activity {

    @ViewById(R.id.tv_tip_message)
    TextView tv_tip_message;

    @Extra("message")
    String message;

    @AfterViews
    public void initViews() {
    	setFinishOnTouchOutside(true);// 设置为true点击区域外消失   
        tv_tip_message.setText(message);
    }

    @Click(R.id.btn_positive)
    public void positiveClick() {
        setResult(RESULT_OK);
        finish();
    }
    
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
             return false; 
        }else { 
            return super.onKeyDown(keyCode, event); 
        } 
           
    } 

}
