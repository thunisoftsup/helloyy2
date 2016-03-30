package com.thunisoft.sswy.mobile.activity.transition;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.HomeActivity;
import com.thunisoft.sswy.mobile.activity.HomeActivity_;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.CourtAttachLogic;
import com.thunisoft.sswy.mobile.logic.CourtLogic;
import com.thunisoft.sswy.mobile.logic.response.CourtAttachResponse;
import com.thunisoft.sswy.mobile.logic.response.CourtResponse;
import com.thunisoft.sswy.mobile.logic.response.DownloadAttachResponse;
import com.thunisoft.sswy.mobile.pojo.TCourt;
import com.thunisoft.sswy.mobile.pojo.TCourtAttach;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;

@EActivity(R.layout.activity_loading)
public class LoadingActivity extends BaseActivity implements AnimationListener {

    @Bean
    CourtAttachLogic courtAttachLogic;

    @Extra("courtId")
    String courtId;

    @Extra("courtName")
    String courtName;

    @ViewById(R.id.tv_court_name)
    TextView tv_court_name;

    @ViewById(R.id.tv_zzpz)
    TextView tv_zzpz;

    @ViewById(R.id.img_loading)
    ImageView img_loading;

    @Pref
    ConfigUtils_ configUtils;

    Animation court_translate;

    Animation court_fade_in;

    @Bean
    CourtLogic courtLogic;
    
    @Bean
    CourtCache courtCache;
    @Bean
    LoginCache loginCache;

    boolean readyToGoAway;

    @AfterViews
    public void initViews() {
        loadCourtInfo();
        downloadData();
        tv_court_name.setText(courtName);
       /* img_loading.setGifImage(R.drawable.loading_page_icon);
        int px = DensityUtil.dip2px(this, getResources().getInteger(R.integer.loading_icon_width));
        img_loading.setShowDimension(px, px);
        img_loading.setGifImageType(GifImageType.COVER);*/
        court_fade_in = AnimationUtils.loadAnimation(this, R.anim.loading_title_fadein);
        court_fade_in.setAnimationListener(this);
        tv_court_name.startAnimation(court_fade_in);
    }

    @Background
    public void loadCourtInfo() {
        CourtResponse cr = courtLogic.loadCourtInfo(courtId);
        if (!cr.isSuccess()) {
            showToast(cr.getMessage());
        } else {
            TCourt court = cr.getCourt();
            try {
            	if (cr.getOpenModules() != null) {
            		loginCache.setOpenModule(new JSONArray(cr.getOpenModules().toString()));
            	}
			} catch (JSONException e) {
				e.printStackTrace();
			}
            if (court == null) {
                court = courtLogic.getCourt(courtId);
            }
            if (court.getNJsfs() != null && court.getNJsfs().intValue() == Constants.JSFS_ZJ) {
                courtCache.setZj(true);
            } else {
                courtCache.setZj(false);
            }
            courtCache.setSsfwUrl(court.getCSsfwUrl());
            courtCache.setWapUrl(court.getCWapUrl());
        }
    }

    public void startTitleTranslate() {
        court_translate = AnimationUtils.loadAnimation(this, R.anim.loading_title_translate);
        court_translate.setAnimationListener(this);
        tv_court_name.startAnimation(court_translate);
    }

    private static final String FJLX_GDTP = "1";
    private static final String FJLX_YDTP = "2";

    @Background
    public void downloadData() {
        long time = configUtils.getSharedPreferences().getLong(courtId + "_court_attach_update", 0L);
        CourtAttachResponse car = courtAttachLogic.loadCourtAttach(courtId, time);
        boolean allSuccess = true;
        if (!car.isSuccess()) {
            allSuccess = false;
            showToast(car.getMessage());
            finish();
            return;
        } else {
            TCourtAttach gdtp = car.getGdtp();
            // List<TCourtAttach> ydtp = car.getYdtp();
            if (gdtp == null) {
                allSuccess = false;
            }
            allSuccess = downloadGdtp(allSuccess, gdtp);
            // allSuccess = downloadYdtp(allSuccess, ydtp);
        }
        if (allSuccess && car.getTime() != null) {
            putEdit(courtId + "_court_attach_update", car.getTime().longValue());
        }
        decideGoNext();
    }

    public void decideGoNext() {
        if (!readyToGoAway) {
            readyToGoAway = true;
        } else {
            goNext();
        }
    }

    @UiThread
    public void goNext() {
        int current_ydtp_version = configUtils.ydtpVersion().get();
        int pre_ydtp_version = configUtils.getSharedPreferences().getInt("current_ydtp_version", 0);
        if (current_ydtp_version > pre_ydtp_version) {
            configUtils.getSharedPreferences().edit().putInt("current_ydtp_version", current_ydtp_version).commit();
            Intent intent = new Intent(LoadingActivity.this, GuidPageActivity_.class);
            startActivity(intent);
            finish();
        } else {
            Log.e("===========", ""+HomeActivity.instance);
            if(HomeActivity.instance == null) {
                Log.e("~~~~", "new.......");
                Intent intent = new Intent(this, HomeActivity_.class);
                startActivity(intent);
            }
            finish();
        }
        
       /* boolean ydtp_showed = configUtils.getSharedPreferences().getBoolean("ydtp_showed", false);
        if (!ydtp_showed) {
            Intent intent = new Intent(this, HomeActivity_.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LoadingActivity.this, GuidPageActivity_.class);
            startActivity(intent);
            finish();

        }*/
    }

    /*
     * private boolean downloadYdtp(boolean allSuccess, List<TCourtAttach> ydtp)
     * { if (ydtp != null) { Set<String> set = new TreeSet<String>(); for
     * (TCourtAttach attach : ydtp) { DownloadAttachResponse dar =
     * courtAttachLogic.downloadAttachFile(attach, FJLX_YDTP, courtId); if
     * (!dar.isSuccess()) { allSuccess = false; showToast(dar.getMessage()); }
     * else { set.add(dar.getPath() + "/" + dar.getFileName()); } }
     * putEdit(courtId + "_ydtp_path", set); } return allSuccess; }
     */

    private boolean downloadGdtp(boolean allSuccess, TCourtAttach gdtp) {
        if (gdtp != null && !gdtp.isDeleted()) {
            DownloadAttachResponse dar = courtAttachLogic.downloadAttachFile(gdtp, FJLX_GDTP, courtId);
            if (!dar.isSuccess()) {
                allSuccess = false;
                showToast(dar.getMessage());
            } else {
                putEdit(courtId + "_gdtp_path", dar.getPath() + dar.getFileName());
            }
        }
        return allSuccess;
    }

    private void putEdit(String key, String value) {
        configUtils.getSharedPreferences().edit().putString(key, value).commit();
    }

    private void putEdit(String key, long value) {
        configUtils.getSharedPreferences().edit().putLong(key, value).commit();
    }

    /*
     * private void putEdit(String key, Set<String> values) {
     * configUtils.getSharedPreferences().edit().putStringSet(key,
     * values).commit(); }
     */

    @UiThread
    public void showToast(String msg) {
        Toast.makeText(LoadingActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == court_fade_in) {
            startTitleTranslate();
        } else if (animation == court_translate) {
            showImagLoading();
        }

    }

    @UiThread
    public void showImagLoading() {
        img_loading.setBackgroundResource(R.drawable.loading_animation_list);
        img_loading.setVisibility(View.VISIBLE);
        tv_zzpz.setVisibility(View.VISIBLE);
        AnimationDrawable ani = (AnimationDrawable) this.img_loading.getBackground();
        ani.start();
        
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                decideGoNext();
            }
        }, 2000);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
