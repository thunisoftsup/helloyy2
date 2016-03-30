package com.thunisoft.sswy.mobile.cache;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.sharedpreferences.Pref;

import com.thunisoft.sswy.mobile.util.ConfigUtils_;

@EBean(scope = Scope.Singleton)
public class CourtCache {
    
    String courtId;
    String courtName;
    boolean zj;
    String wapUrl;
    String ssfwUrl;
    
    String preCourtId;
    Boolean preZj;
    
    @Pref
    ConfigUtils_ configUtils;
    
    public String intCourt() {
        courtId = configUtils.getSharedPreferences().getString("current_court_id", null);
        courtName = configUtils.getSharedPreferences().getString("current_court_name", null);
        preCourtId = courtId;
        return courtId;
    }

    public String getCourtId() {
        return courtId;
    }
    
    public String getCourtId(String defaultValue) {
        return courtId == null ? defaultValue : courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
        configUtils.getSharedPreferences().edit().putString("current_court_id", courtId).commit();
    }
    
    public String getCourtName() {
        return courtName == null ? "" : courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
        configUtils.getSharedPreferences().edit().putString("current_court_name", courtName).commit();
    }

    public String getPreCourtId() {
        return preCourtId;
    }

    public void setPreCourtId(String preCourtId) {
        this.preCourtId = preCourtId;
    }

    public boolean isZj() {
        return zj;
    }

    public void setZj(boolean zj) {
        this.zj = zj;
        configUtils.getSharedPreferences().edit().putBoolean("JSFS_ZJ", zj).commit();
    }

    public String getWapUrl(String defaultValue) {
        return wapUrl == null ? defaultValue : wapUrl;
    }

    public void setWapUrl(String wapUrl) {
        this.wapUrl = wapUrl;
    }

    public String getSsfwUrl(String defaultValue) {
        return ssfwUrl == null ? defaultValue : ssfwUrl;
    }

    public void setSsfwUrl(String ssfwUrl) {
        this.ssfwUrl = ssfwUrl;
    }

    public Boolean getPreZj() {
        return preZj;
    }

    public void setPreZj(Boolean preZj) {
        this.preZj = preZj;
    }

    
}
