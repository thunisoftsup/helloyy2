package com.thunisoft.sswy.mobile.logic.net;

import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.response.GywResponse;

@EBean(scope = Scope.Singleton)
public class GywResponseUtil extends JSONParsor<GywResponse> {
    private static final String TAG = "ResponseUtil";

    @Bean
    NetUtils netUtils;

    @Override
    public GywResponse parseToBean(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, new TypeToken<GywResponse>() {

        }.getType());
    }

    @Override
    public GywResponse getResponse(String url, List<NameValuePair> params) {
        GywResponse br = new GywResponse();
        try {
            String result = netUtils.post(url, params);
            if (result != null) {
                br = parseToBean(result);
                if (!br.isSuccess()) {
                    br.setXtcw(false);
                }
            }
        } catch (SSWYException e) {
            Log.e(TAG, e.getMessage(), e);
            br.setSuccess(false);
            br.setMessage(e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            br.setSuccess(false);
            br.setMessage(e.getMessage());
        }
        return br;
    }

}
