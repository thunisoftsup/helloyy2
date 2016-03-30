package com.thunisoft.sswy.mobile.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 获取服务器端时间，然后自行维护
 * @author gewx
 *
 */
@EService
public class ServerTimeService extends Service{

	private static final String TAG = ServerTimeService.class.getSimpleName();
	
	/** service 对外的信道 IBinder **/
	private ServerTimeServiceBinder binder;
	
	@Bean
    NetUtils netUtils;
	
	/**自动校准时间 15分钟一次**/
	private static final int UPDATE_TIME_INTERVAL = 15*60*1000;
	
	private int updateTimeInterval = -1;
	
	private Date currTime = new Date();
	
	//秒表计时器2
    private ScheduledExecutorService scheduledThreadPool = null;
    
    private static final String UPDATE_TIME_RECEIVER = "com.thunisoft.sswy.mobile.service.updateTime";
    
	@Override
	public void onCreate() {
		this.binder = new ServerTimeServiceBinder();
		registerBoradcastReceiver();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		this.initScheduled();
		this.startScheduled();
		return this.binder;
	}
	
	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		this.initScheduled();
		this.startScheduled();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	/**
	 * 系统版本2.0~，推荐使用<br>
	 * onStart已经被@Deprecated
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_REDELIVER_INTENT;
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}
	
	/**
	 * 初始化Scheduled
	 */
	private void initScheduled(){
		this.stopScheduled();
		/** 
	     * 使用工厂方法初始化一个ScheduledThreadPool 
	     */  
		if(this.scheduledThreadPool == null){
			this.scheduledThreadPool = Executors.newScheduledThreadPool(1);
		}
	}
	
	/**
	 * 开始Scheduled
	 */
	private void startScheduled(){
		this.scheduledThreadPool.scheduleAtFixedRate(new UpdateTimerTask(), 0, 1, TimeUnit.SECONDS);
	}
	
	/**
	 * 停止Scheduled
	 */
	private void stopScheduled(){
		if(this.scheduledThreadPool != null){
			this.scheduledThreadPool.shutdown();
		}
		this.scheduledThreadPool = null;
	}
	
	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(UPDATE_TIME_RECEIVER);
		// 注册广播
		this.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (UPDATE_TIME_RECEIVER.equals(action)) {
				requestServerTime();
			}
		}
	};
	
	/**
	 * 更新服务器时间计时器
	 * @author gewx
	 *
	 */
	public class UpdateTimerTask implements Runnable{ 
		
		@Override  
        public void run() {
			if (updateTimeInterval < 0 || updateTimeInterval >= UPDATE_TIME_INTERVAL) {
				updateTimeInterval = 0;
				//自动校准时间
	        	Intent bIntent = new Intent();
	        	bIntent.setAction(UPDATE_TIME_RECEIVER);
	        	sendBroadcast(bIntent);
			}
			updateTimeInterval++;
			long currTimes = currTime.getTime();
			currTime.setTime(currTimes + 1000);
        }
    }
	
	/**
	 * 返回当前服务器时间
	 * @return
	 */
	public String getCurrServerTimeStr() {
		return NrcUtils.formatDate(currTime, NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);
	}
	
/****************************************华丽的分割线********************************************/
	
	/**
	 * 绑定时（bindService） 用于通讯的 中间件
	 * @author gewx
	 *
	 */
	public class ServerTimeServiceBinder extends Binder {
		
		/**
		 * 通过Binder获取Service对象
		 * @return
		 */
		public ServerTimeService getService(){
			return ServerTimeService.this;
		}
	}
	
	/**
	 * 向服务器发送请求，获取服务器当前时间
	 */
	@Background
	protected void requestServerTime() {
		ServerTimeResponse serverTimeResponse = new ServerTimeResponse();
		String url = netUtils.getMainAddress() + "/api/server/getTime";
		serverTimeResponse.clearParams();
		serverTimeResponse.getResponse(url, serverTimeResponse.getParams());
	}
	
	private class ServerTimeResponse extends JSONParsor<BaseResponse> {

		@Override
		public BaseResponse parseToBean(String result) {
			Gson gson = new Gson();
	        return gson.fromJson(result, new TypeToken<BaseResponse>() {
	        }.getType());
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public BaseResponse getResponse(String url, List<NameValuePair> params) {
			BaseResponse cr = new BaseResponse();
	        try {
	        	long startTime = new Date().getTime();
	            String result = netUtils.post(url, params);
	            if (result != null) {
	            	JSONObject resJson = new JSONObject(result);
	            	boolean success = resJson.getBoolean("success");
	                if (success) {
	                	long endTime = new Date().getTime();
	                	String serverTimeStr = resJson.getString("data");
	                	currTime = NrcUtils.string2Date(serverTimeStr, NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);
	                	long serverTimes = currTime.getTime();
	                	long subTimes = (endTime-startTime)/2;
	                	currTime.setTime(serverTimes + subTimes);
	                }
	            }
	        } catch (SSWYNetworkException e) {
	            Log.e(TAG, e.getMessage(), e);
	            cr.setSuccess(false);
	            cr.setMessage(e.getMessage());
	        } catch (Exception e) {
	            Log.e(TAG, e.getMessage(), e);
	            cr.setSuccess(false);
	            cr.setMessage(e.getMessage());
	        }
	        return cr;
		}
	}
}
