package com.thunisoft.sswy.mobile.logic.net;

import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.datasource.CourtDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.response.CourtResponse;
import com.thunisoft.sswy.mobile.util.GZipUtil;

@EBean(scope = Scope.Singleton)
public class CourtResponseUtil extends JSONParsor<CourtResponse> {
    private static final String TAG = "CourtResponseUtil";

    @Bean
    NetUtils netUtils;
    
    @Bean
    CourtDao courtDao;

    @Override
    public CourtResponse parseToBean(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, new TypeToken<CourtResponse>() {
        }.getType());
    }
    
    @Override
    public CourtResponse getResponse(String url, List<NameValuePair> params) {
        CourtResponse cr = new CourtResponse();
        try {
            String result = netUtils.post(url, params);
            if (result != null) {
                result = GZipUtil.gunzip(result);
                cr = parseToBean(result);
                courtDao.saveCourt(cr);
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
    
    public CourtResponse getResponseNoUnzip(String url, List<NameValuePair> params) {
        CourtResponse cr = new CourtResponse();
        try {
            String result = netUtils.post(url, params);
            if (result != null) {
                cr = parseToBean(result);
                courtDao.saveCourt(cr);
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
