package com.thunisoft.sswy.mobile.logic;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.activity.SelectAddressActivity;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.datasource.RegionDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TRegion;

@EBean(scope = Scope.Singleton)
public class DownloadRegion {

	private static final String TAG = DownloadRegion.class.getSimpleName();
	
	public Activity activity;
	
	@Bean
	protected NetUtils netUtils;
	
	@Bean
	LoginCache loginCache;
	
	@Bean
	RegionDao regionDao;
	
	/**
	 * 向服务器发送请求，获取获取地址
	 */
	@Background
	public void downloadRegion() {
		AddressResponse response = new AddressResponse();
		String url = netUtils.getMainAddress() + "/api/region/getAllRegions";
		response.clearParams();
		response.addParam("md5", loginCache.getAddressMD5());
		response.getResponse(url, response.getParams());
	}
	
	private class AddressResponse extends JSONParsor<BaseResponse> {

		@Override
		public BaseResponse parseToBean(String result) {
			Gson gson = new Gson();
	        return gson.fromJson(result, new TypeToken<BaseResponse>() {
	        }.getType());
		}

		@Override
		public BaseResponse getResponse(String url, List<NameValuePair> params) {
			BaseResponse cr = new BaseResponse();
			List<TRegion> regionList = new ArrayList<TRegion>();
	        try {
	            String result = netUtils.post(url, params);
	            if (result != null) {
	            	JSONObject resJson = new JSONObject(result);
	            	boolean success = resJson.getBoolean("success");
	                if (success) {
	                	JSONObject dataJson = resJson.getJSONObject("data");
	                	if (null != dataJson) {
	                		loginCache.setAddressMD5(dataJson.getString("md5"));
		                	JSONArray regionArray = dataJson.getJSONArray("regions");
		                	for (int i=0; i<regionArray.length(); i++ ) {
		                		JSONObject regionJson = regionArray.getJSONObject(i);
		                		TRegion region = new TRegion();
		                		region.setCId(regionJson.getString("CId"));
		                		region.setCParentId(regionJson.getString("CParentId"));
		                		region.setCJb(regionJson.getString("CJb"));
		                		region.setCName(regionJson.getString("CName"));
		                		region.setCParentId(regionJson.getString("CParentId"));
		                		region.setNOrder(regionJson.getInt("NOrder"));
		                		region.setNValid(regionJson.getInt("NValid"));
		                		regionList.add(region);
		                	}
		                	regionDao.clearTable();
		                	regionDao.saveSsclFj(regionList);
	                	} else {
	                		regionList = regionDao.getRegionList(RegionDao.VALID_TRUE);
	                	}
	                } else {
	                	regionList = regionDao.getRegionList(RegionDao.VALID_TRUE);
	                	if (null == regionList || regionList.size() == 0) {
	                		if (activity instanceof SelectAddressActivity) {
	                			Toast.makeText(activity, resJson.getString("message"), Toast.LENGTH_SHORT).show();
	        	        	}
	                	}
	                }
	            } else {
	            	regionList = regionDao.getRegionList(RegionDao.VALID_TRUE);
	            }
	        } catch (SSWYNetworkException e) {
	            Log.e(TAG, e.getMessage(), e);
	            cr.setSuccess(false);
	            cr.setMessage(e.getMessage());
	        } catch (Exception e) {
	            Log.e(TAG, e.getMessage(), e);
	            cr.setSuccess(false);
	            cr.setMessage(e.getMessage());
	        } finally {
            	if (null == regionList || regionList.size() == 0) {
            		if (activity instanceof SelectAddressActivity) {
            			Toast.makeText(activity, "获取行政地址失败，请检查网络或重试", Toast.LENGTH_SHORT).show();
    	        	}
            	}
	        }
	        if (null == regionList || regionList.size() == 0) {
	        	if (activity instanceof SelectAddressActivity) {
	        		activity.finish();
	        	}
	        } else {
	        	if (activity instanceof SelectAddressActivity) {
	        		SelectAddressActivity addressDialogActivity = (SelectAddressActivity)activity;
	        		addressDialogActivity.onAfterViews();
	        	}
	        }
	        return cr;
		}
	}
}
