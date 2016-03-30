package com.thunisoft.sswy.mobile.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import com.thunisoft.dzfy.mobile.R;

@EActivity(R.layout.activity_single_image)
public class SingleImageActivity extends BaseActivity {
    
    @Extra("courtId")
    String courtId;
    
    @AfterViews
    public void initViews() {
        
    }
}
