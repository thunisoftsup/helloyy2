package com.thunisoft.sswy.mobile.util;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

@EBean
public class NetworkStateUtils {
	@SystemService
	ConnectivityManager cm;
	@SystemService
	WifiManager wifi;

	public boolean isWifiAvailable() {
		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
	}

	public boolean isWifiConnected() {
		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}

	public boolean isMobileAvailable() {
		return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
	}

	public boolean isMobileConnected() {
		return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
	}

	public boolean isOnline() {
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	public String getMacAddress() {
		if (wifi != null) {
			try {
				WifiInfo info = wifi.getConnectionInfo();
				if (info != null) {
					return info.getMacAddress();
				}
			} catch (Exception e) {
				Log.e("test", "getMacAddress", e);
			}
		}
		return "";
	}
}
