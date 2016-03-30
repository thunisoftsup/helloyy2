package com.thunisoft.sswy.mobile.logic.net;

import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.logic.DeleteLogic.DelSsclFjResponseListener;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.SSWYConstants;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 删除 诉讼材料附件
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class DeleteSsclFjResUtil extends JSONParsor<BaseResponse> {

	private static final String TAG = DeleteSsclFjResUtil.class.getSimpleName();

	@Bean
	NetUtils netUtils;

	/**
	 * 当前打开的activity
	 */
	public Activity activity;

	/**
	 * 删除的诉讼材料附件Id
	 */
	private String ssclFjId;

	/**
	 * 服务器相应返回结果
	 */
	private BaseResponse response;

	/**
	 * 删除请求服务器，返回结果监听
	 */
	private DelSsclFjResponseListener deleteResultListener;

	@Override
	public BaseResponse parseToBean(String result) {
		Gson gson = new Gson();
		return gson.fromJson(result, new TypeToken<BaseResponse>() {
		}.getType());
	}

	@Override
	public BaseResponse getResponse(String url, List<NameValuePair> params) {
		response = null;
		request(url, params);
		return response;
	}

	@Background
	protected void request(String url, List<NameValuePair> params) {
		String result;
		try {
			result = netUtils.post(url, params);
			response = new BaseResponse();
			if (StringUtils.isNotBlank(result)) {
				response.setXtcw(true);
				JSONObject resJson = new JSONObject(result);
				response.setSuccess(resJson.getBoolean("success"));
				response.setMessage(resJson.getString("message"));
			} else {
				response.setXtcw(false);
				response.setMessage(SSWYConstants.ERROR_NETWORK);
			}
		} catch (SSWYException e) {
			response = new BaseResponse();
			response.setXtcw(false);
			response.setMessage(SSWYConstants.ERROR_NETWORK);
		} catch (JSONException e) {
			response = new BaseResponse();
			response.setXtcw(false);
			response.setMessage(SSWYConstants.ERROR_NETWORK);
		}
		deleteResult();
	}
	
	@UiThread
	protected void deleteResult() {
		deleteResultListener.deleteResult(response, ssclFjId);
	}

	/**
	 * 设置 删除的诉讼材料附件Id
	 * 
	 * @param ssclFjId
	 */
	public void setSsclFjId(String ssclFjId) {
		this.ssclFjId = ssclFjId;
	}

	/**
	 * 设置 删除请求服务器，返回结果监听
	 * 
	 * @param deleteResultListener
	 */
	public void setDeleteResultListener(DelSsclFjResponseListener deleteResultListener) {
		this.deleteResultListener = deleteResultListener;
	}
}
