package com.thunisoft.sswy.mobile.service;

import org.androidannotations.annotations.Bean;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.thunisoft.sswy.mobile.datasource.PayInfoDao;

public class MsgService extends Service {
	


	
	@Bean
	PayInfoDao payInfoDao;

	private final IBinder binder = new myBinder();

	public class myBinder extends Binder {
		public MsgService getService() {

			return MsgService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		return binder;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}
	

	
	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		
	}
	


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}



}
