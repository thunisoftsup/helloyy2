package com.thunisoft.sswy.mobile.logic.net;

import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.datasource.NrcBasicDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.util.SSWYConstants;

/**
 * 网上立案 提交 response
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcSubmitResponseUtil extends JSONParsor<BaseResponse> {
	
    private static final String TAG = NrcSubmitResponseUtil.class.getSimpleName();
    
    private TLayy layy;
    
    @Bean
    NetUtils netUtils;
    
    @Bean
    NrcBasicDao nrcBasicDao;
    
    @Override
    public BaseResponse parseToBean(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result, new TypeToken<BaseResponse>() {
        }.getType());
    }
    
    @Override
    public BaseResponse getResponse(String url, List<NameValuePair> params) {
    	BaseResponse cr = new BaseResponse();
        try {
            String result = netUtils.post(url, params);
            if (result != null) {
            	cr.setXtcw(true);
            	JSONObject resJson = new JSONObject(result);
//            	String dataStr = resJson.getString("data");
            	boolean success = resJson.getBoolean("success");
            	String message = resJson.getString("message");
                if (success) {
                	cr.setSuccess(true);
                } else {
                	cr.setSuccess(false);
                	cr.setMessage(message);
                }
            } else {
            	cr.setXtcw(false);
            	cr.setMessage(SSWYConstants.ERROR_NETWORK);
            }
        } catch (SSWYNetworkException e) {
            Log.e(TAG, e.getMessage(), e);
            cr.setXtcw(false);
            cr.setSuccess(false);
            cr.setMessage(SSWYConstants.ERROR_NETWORK);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            cr.setXtcw(false);
            cr.setSuccess(false);
            cr.setMessage(SSWYConstants.ERROR_NETWORK);
        }
        return cr;
    }
    
	/**  
	 * 获取  立案预约 
	 * @return layy
	 */
	public TLayy getLayy() {
		return layy;
	}

	/**  
	 * 设置  立案预约 
	 * @param layy
	 */
	public void setLayy(TLayy layy) {
		this.layy = layy;
	}
}
