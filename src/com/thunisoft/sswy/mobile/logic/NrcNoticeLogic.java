package com.thunisoft.sswy.mobile.logic;

import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;

/**
 * 网上立案_须知
 * 
 * @author gewx
 * 
 */
@EBean(scope = Scope.Singleton)
public class NrcNoticeLogic {

	private static final String TAG = NrcNoticeLogic.class.getSimpleName();

	@Bean
	NetUtils netUtils;

	/**
	 * 获取网上立案_须知
	 * @param courtId 法院Id
	 * @param tempSid sessionId
	 * @return
	 */
	public NrcNoticeResponse requestNrcNotice(String courtId) {
		NrcNoticeParsor noticeParcor = new NrcNoticeParsor();
		String url = netUtils.getMainAddress() + "/api/wsla/getLaxz";
		noticeParcor.clearParams();
		noticeParcor.addParam("fyid", courtId);
		return noticeParcor.getResponse(url, noticeParcor.getParams());
	}

	private class NrcNoticeParsor extends JSONParsor<NrcNoticeResponse> {

		@Override
		public NrcNoticeResponse parseToBean(String result) {
			Gson gson = new Gson();
	        return gson.fromJson(result, new TypeToken<BaseResponse>() {
	        }.getType());
		}

		@Override
		public NrcNoticeResponse getResponse(String url, List<NameValuePair> params) {
			NrcNoticeResponse response = new NrcNoticeResponse();
	        try {
	            String result = netUtils.post(url, params);
	            if (result != null) {
	            	JSONObject resJson = new JSONObject(result);
	            	boolean success = resJson.getBoolean("success");
	                if (success) {
	                	JSONObject data = resJson.getJSONObject("data");
	                	response.setSuccess(true);
	                	response.setContent(data.getString("content"));
	                	response.setTitle(data.getString("title"));
	                } else {
	                	String message = resJson.getString("message");
	                	response.setSuccess(false);
	                	response.setMessage(message);
	                }
	            }
	        } catch (SSWYNetworkException e) {
	            Log.e(TAG, e.getMessage(), e);
	            response.setSuccess(false);
	            response.setMessage(e.getMessage());
	        } catch (Exception e) {
	            Log.e(TAG, e.getMessage(), e);
	            response.setSuccess(false);
	            response.setMessage(e.getMessage());
	        }
	        return response;
		}
	}
	
	public class NrcNoticeResponse extends BaseResponse{
		
		private String content;

		private String title;
		
		/**  
		 * 获取  data  
		 * @return data  
		 */
		public String getContent() {
			return content;
		}

		/**  
		 * 设置  data  
		 * @param data
		 */
		public void setContent(String content) {
			this.content = content;
		}

		/**  
		 * 获取  title  
		 * @return title  
		 */
		public String getTitle() {
			return title;
		}

		/**  
		 * 设置  title  
		 * @param title
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}
}
