package com.thunisoft.sswy.mobile.logic;

import java.io.InputStream;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.util.Log;

import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtil;
import com.thunisoft.sswy.mobile.logic.response.YzmResponse;

@EBean(scope = Scope.Singleton)
public class BaseLogic {
    private static final String TAG = "BaseLogic";
    
    @Bean
    ResponseUtil responseUtil;

    @Bean
    NetUtils netUtils;

    public YzmResponse getYzm() {
        String url = netUtils.getServerAddress() + "/yzm.jpg";
        YzmResponse yr = new YzmResponse();
        try {
            InputStream is = netUtils.getStream(url);
            if(is == null) {
                yr.setSuccess(false);
                yr.setMessage("获取验证码失败");
            } else {
                yr.setSuccess(true);
                yr.setInputStream(is);
            }
        }catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            yr.setSuccess(false);
            yr.setMessage("获取验证码失败");
        }
        return yr;
    }
}
