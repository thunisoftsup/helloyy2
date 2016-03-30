package com.thunisoft.sswy.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.HomeActivity_;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.activity.court.CourtListActivity_;
import com.thunisoft.sswy.mobile.activity.transition.GuidPageActivity_;
import com.thunisoft.sswy.mobile.adapter.listener.AnimationListenerAdapter;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.CourtAttachLogic;
import com.thunisoft.sswy.mobile.logic.CourtLogic;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.logic.response.CourtAttachResponse;
import com.thunisoft.sswy.mobile.logic.response.CourtResponse;
import com.thunisoft.sswy.mobile.logic.response.DownloadAttachResponse;
import com.thunisoft.sswy.mobile.pojo.TCourt;
import com.thunisoft.sswy.mobile.pojo.TCourtAttach;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;

@EActivity(R.layout.activity_main)
public class Susong51Activity extends Activity {
    @Bean
    CourtLogic logic;
    @Bean
    LoginCache logninCatch;
    @Pref
    ConfigUtils_ configUtils;

    @Bean
    FileUtils fileUtils;

    @Bean
    CourtAttachLogic courtAttachLogic;
    String courtId;

    @Bean
    CourtLogic courtLogic;

    @ViewById(R.id.img_gdtp)
    ImageView img_gdtp;
    
    @ViewById(R.id.img_splash)
    ImageView img_splash;

    Handler handler;
    @Bean
    LoginCache loginCache;
    @Bean
    CourtCache courtCache;
    @Bean
    NetUtils netUtils;

    int xstpCs = 0;

    boolean goToGuidPage;
    boolean isNetworkFail;
    
    private WaittingDialog waitDialog;

    @AfterViews
    public void initViews() {
    	waitDialog = new WaittingDialog(this, R.style.LoginDialogStyle, "加载中...");
    	waitDialog.setIsCanclable(false);
		waitDialog.show();
        if (isNetworkFail)
            return;
        handler = new Handler();
        courtId = courtCache.intCourt();
        
        if (StringUtils.isNotBlank(courtId)) {
            int current_ydtp_version = configUtils.ydtpVersion().get();
            int pre_ydtp_version = configUtils.getSharedPreferences().getInt("current_ydtp_version", 0);
            if (current_ydtp_version > pre_ydtp_version) {
                goToGuidPage = true;
                configUtils.getSharedPreferences().edit().putInt("current_ydtp_version", current_ydtp_version).commit();
            }
            loadCourtInfo();
        } else {
            xzfy();
        }
        initLoginCatch();
        requestLawyerServerAddress();
    }

    @Override
    protected void onDestroy() {
    	if (null != waitDialog) {
    		waitDialog.dismiss();
    	}
    	super.onDestroy();
    }
    
    @Background
    public void loadCourtInfo() {
        CourtResponse cr = courtLogic.loadCourtInfo(courtId);
        if (!cr.isSuccess()) {
            showToast(cr.getMessage());
        } else {
            TCourt court = cr.getCourt();
            if (court == null) {
                court = courtLogic.getCourt(courtId);
            }
            try {
            	if (cr.getOpenModules() != null) {
            		loginCache.setOpenModule(new JSONArray(cr.getOpenModules().toString()));
            	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
            if (court.getNJsfs() != null && court.getNJsfs().intValue() == Constants.JSFS_ZJ) {
                courtCache.setZj(true);
            } else {
                courtCache.setZj(false);
            }
            courtCache.setSsfwUrl(court.getCSsfwUrl());
            courtCache.setWapUrl(court.getCWapUrl());
        }
        long time = configUtils.getSharedPreferences().getLong(courtId + "_court_attach_update", 0L);
        downloadData(time);
    }

    /**
     * 显示过渡图片
     * 
     * @param currentCourtId
     */
    @UiThread
    public void xsGdtp() {
        xstpCs++;
        Log.e("XSTPCS", String.valueOf(xstpCs));
        if (xstpCs > 2) {
            goHome();
            return;
        }
        String gdtpPath = configUtils.getSharedPreferences().getString(courtId + "_gdtp_path", null);

        if (gdtpPath == null) {
            downloadData(0L);
            goHome();// add for..... 
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory(), gdtpPath);
        if (file.exists() && file.isFile()) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(file);
                Bitmap bit = BitmapFactory.decodeStream(stream);
                img_gdtp.setScaleType(ScaleType.FIT_XY);
                img_gdtp.setImageBitmap(bit);
                Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.pic_fade_out);
                fadeOut.setAnimationListener(new AnimationListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        img_splash.setVisibility(View.GONE);
                    }
                });
                img_splash.startAnimation(fadeOut);
                
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.pic_fade_in);
                fadeIn.setAnimationListener(new AnimationListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        img_gdtp.setVisibility(View.VISIBLE);
                    }
                });
                img_gdtp.startAnimation(fadeIn);
            } catch (Exception e) {
                Log.e("", "显示过渡图片出错", e);
            } finally {
                fileUtils.closeQuietly(stream);
            }
            goHome();
        } else {
            downloadData(0L);
        }
    }

    @Background
    public void downloadData(long time) {
        CourtAttachResponse car = courtAttachLogic.loadCourtAttach(courtId, time);
        boolean allSuccess = true;
        if (!car.isSuccess()) {
            allSuccess = false;
            showToast("请检查您的网络连接");
            xsGdtp();
            return;
        } else {
            TCourtAttach gdtp = car.getGdtp();
            if (gdtp != null) {
                if (gdtp.isDeleted()) {
                    putEdit(courtId + "_court_attach_update", 0L);
                    goHome();
                } else {
                    downloadGdtp(allSuccess, gdtp);
                }
            } else {
                xsGdtp();
            }
        }
        if (car.getTime() != null) {
            putEdit(courtId + "_court_attach_update", car.getTime().longValue());
        }

    }

    /**
     * 下载过渡图片
     * 
     * @param allSuccess
     * @param gdtp
     * @return
     */
    private boolean downloadGdtp(boolean allSuccess, TCourtAttach gdtp) {
        if (gdtp != null) {
            DownloadAttachResponse dar = courtAttachLogic.downloadAttachFile(gdtp, "1", courtId);
            if (!dar.isSuccess()) {
                allSuccess = false;
                showToast(dar.getMessage());
            } else {
                putEdit(courtId + "_gdtp_path", dar.getPath() + dar.getFileName());
                xsGdtp();
            }
        }
        return allSuccess;
    }

    /**
     * 进入主页面
     */
    private void goHome() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (goToGuidPage) {
                    Intent intent = new Intent(Susong51Activity.this, GuidPageActivity_.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Susong51Activity.this, HomeActivity_.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, configUtils.gdtpXssj().get());
    }

    /**
     * 选择法院
     */
    private void xzfy() {
        Intent intent = new Intent(Susong51Activity.this, CourtListActivity_.class);
        startActivity(intent);
        finish();
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void putEdit(String key, String value) {
        configUtils.getSharedPreferences().edit().putString(key, value).commit();
    }

    private void putEdit(String key, long value) {
        configUtils.getSharedPreferences().edit().putLong(key, value).commit();
    }

    private void initLoginCatch() {
        logninCatch.initLoginCatch();
    }
    
	/**
	 * 向服务器发送请求，获取律协地址
	 */
	@Background
	protected void requestLawyerServerAddress() {
		LawyerServerAddressResponse serverAddressResponse = new LawyerServerAddressResponse();
		String url = netUtils.getMainAddress() + "/api/config/getLxUrl";
		serverAddressResponse.clearParams();
		serverAddressResponse.getResponse(url, serverAddressResponse.getParams());
	}
	
	private class LawyerServerAddressResponse extends JSONParsor<BaseResponse> {

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
	            String result = netUtils.post(url, params);
	            if (result != null) {
	            	JSONObject resJson = new JSONObject(result);
	            	boolean success = resJson.getBoolean("success");
	                if (success) {
	                	String serverAddress = resJson.getString("data");
	                	loginCache.setLxUrl(serverAddress);
	                }
	            }
	        } catch (SSWYNetworkException e) {
	            cr.setSuccess(false);
	            cr.setMessage(e.getMessage());
	        } catch (Exception e) {
	            cr.setSuccess(false);
	            cr.setMessage(e.getMessage());
	        }
	        return cr;
		}
	}
}
