package com.thunisoft.sswy.mobile;

import java.lang.Thread.UncaughtExceptionHandler;

import org.androidannotations.annotations.EApplication;

import android.app.Application;
import android.content.Intent;

import com.thunisoft.sswy.mobile.datasource.DBHelper;
import com.thunisoft.sswy.mobile.service.UpLoadPayInfoService_;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

@EApplication
public class Susong51Application extends Application implements UncaughtExceptionHandler{

    @Override
    public void onCreate() {
        DBHelper.init(this);
        
		Intent intent = new Intent(this,UpLoadPayInfoService_.class);
		this.startService(intent);
    }
    
    @Override
    public void onTerminate() {
    	super.onTerminate();
    }

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		NrcEditData.clearData();
	}
	
}
