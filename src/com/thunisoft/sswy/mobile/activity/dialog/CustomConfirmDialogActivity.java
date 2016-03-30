package com.thunisoft.sswy.mobile.activity.dialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;

@EActivity(R.layout.dialog_basic_confirm)
public class CustomConfirmDialogActivity extends Activity {

    @ViewById(R.id.tv_tip_message)
    TextView tv_tip_message;
    
    @ViewById(R.id.btn_negative)
    Button negativeButton;
    
    @ViewById(R.id.btn_positive)
    Button positiveButton;

    @Extra("message")
    String message;
    
    @Extra("negative")
    String negative;
    
    @Extra("positive")
    String positive;

    @AfterViews
    public void initViews() {
        tv_tip_message.setText(message);
        negativeButton.setText(negative);
        positiveButton.setText(positive);
    }

    @Click(R.id.btn_positive)
    public void positiveClick() {
        setResult(RESULT_OK);
        finish();
    }

    @Click(R.id.btn_negative)
    public void negativeClick() {
        setResult(RESULT_CANCELED);
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
