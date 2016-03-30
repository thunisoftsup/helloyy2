package com.thunisoft.sswy.mobile.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.thunisoft.sswy.mobile.service.ServerTimeService;
import com.thunisoft.sswy.mobile.service.ServerTimeService.ServerTimeServiceBinder;
import com.thunisoft.sswy.mobile.service.ServerTimeService_;

/**
 * Android Service 工具类
 * @author gewx
 *
 */
public class ServiceUtils {

	private static ServiceConnection serverTimeServiceConn;
	
	private static ServerTimeService serverTimeService;
	
	/**
	 * 获取服务器时间
	 * @param context
	 * @return
	 */
	public static String getServerTimeStr(Context context) {
		startServiceTimeService(context);
		return serverTimeService.getCurrServerTimeStr();
	}
	
	public static void startServiceTimeService(Context context) {
		if (!ServiceUtils.isServiceRunning(context, ServerTimeService_.class.getName())) {
			Intent bindService = new Intent(context, ServerTimeService_.class);
			serverTimeServiceConn = new ServiceConnection() {
				@Override
				public void onServiceConnected(ComponentName className, IBinder service) {
					serverTimeService = ((ServerTimeServiceBinder) service).getService();//用绑定方法启动service，就是从这里绑定并得到service，然后就可以操作service了
				}
					
				@Override
				public void onServiceDisconnected(ComponentName arg0) {
					
				}
			};
			context.bindService(bindService, serverTimeServiceConn, Context.BIND_AUTO_CREATE);
		}
	}
	
	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}
