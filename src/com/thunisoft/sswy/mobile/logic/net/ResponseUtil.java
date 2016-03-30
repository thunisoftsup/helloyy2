package com.thunisoft.sswy.mobile.logic.net;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.apaches.commons.codec.binary.Base64;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;

@EBean(scope = Scope.Singleton)
public class ResponseUtil extends JSONParsor<BaseResponse> {
    private static final String TAG = "ResponseUtil";

    @Bean
    NetUtils netUtils;

    public BaseResponse parseToBean(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, new TypeToken<BaseResponse>() {
             
        }.getType());
    }

    public BaseResponse getResponse(String url, List<NameValuePair> params) {
        BaseResponse br = new BaseResponse();
        try {
            String result = netUtils.post(url, params);
            if (result != null) {
                Log.i(TAG, "result:"+result);
                br = (BaseResponse) parseToBean(result);
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
    
    public void getQRCodeResponse(String url, List<NameValuePair> params) {
        try {
            String result = netUtils.post(url, params);
            if (result != null) {
                Log.i(TAG, "result:"+result);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject json = new JSONObject(result);
                    	byte[] ewmBytes = Base64.decodeBase64(json.getString("data"));
                        if (ewmBytes != null) {
                            FileUtils.saveFile(ewmBytes, Environment.getExternalStorageDirectory()+"/susong51/", "QRCode.jpg");
                        }
                }

            }else{
            	String filePath = Environment.getExternalStorageDirectory()+"/susong51/"+"QRCode.jpg";
            	File file = new File(filePath);
            	if(file.exists()){
            		file.delete();
            	}
            	
            	
            }
        } catch (SSWYException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
