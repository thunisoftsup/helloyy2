package com.thunisoft.sswy.mobile.logic.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.sswy.mobile.datasource.NrcSsclFjDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.response.NRCReviewResponse;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.GZipUtil;

@EBean(scope = Scope.Singleton)
public class NRCReviewResponseUtil extends JSONParsor<NRCReviewResponse> {
	private static final String TAG = "NRCReviewResponseUtil";

	@Bean
	NetUtils netUtils;

	@Bean
	FileUtils fileUtils;

	@Bean
	NrcSsclFjDao nrcSsclFjDao;

	@Override
	public NRCReviewResponse parseToBean(String result) {
		Gson gson = new Gson();
        return gson.fromJson(result, new TypeToken<NRCReviewResponse>() {
        }.getType());
	}

	public NRCReviewResponse getWslayyInfo(String layyId) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("layyId", layyId));
		String url = netUtils.getMainAddress() + "/api/wsla/detail";
		return getResponseNoUnzip(url, params);
	}

	@Override
	public NRCReviewResponse getResponse(String url, List<NameValuePair> params) {
		NRCReviewResponse cr = new NRCReviewResponse();
		try {
			String result = netUtils.post(url, params);
			if (result != null) {
				result = GZipUtil.gunzip(result);
				cr = parseToBean(result);
				// courtDao.saveCourt(cr);
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

	public InputStream getResponseStream(String url, List<NameValuePair> params) {
		InputStream is = null;
		try {
			is = netUtils.postStream(url, params);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SSWYNetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}

	public NRCReviewResponse getResponseNoUnzip(String url, List<NameValuePair> params) {
		NRCReviewResponse cr = new NRCReviewResponse();
		try {
			String result = netUtils.post(url, params);
			if (result != null) {
				JSONObject resJson = new JSONObject(result);
				String dataStr = resJson.getString("data");
				String success = resJson.getString("success");
				String message = resJson.getString("message");
				if (!success.equals("true")) {
					// showToast(message);
				} else {
					// TLayy TLayy = br.getLayy();
					cr = parseToBean(dataStr);
				}
				// courtDao.saveCourt(cr);
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

	/**
	 * 从DB中加载诉讼材料附件
	 * 
	 * @param sdId
	 * @return
	 */
	public TLayySsclFj getSsclFjFromDb(String sdId) {
		return nrcSsclFjDao.getSsclFjById(sdId);
	}

	/**
	 * 从DB中加载诉讼材料附件
	 * 
	 * @param sdId
	 * @return
	 */
	public void saveAttachment(TLayySsclFj ssclFj) {
		List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
		ssclFjList.add(ssclFj);
		nrcSsclFjDao.updateOrSaveSsclFj(ssclFjList);
	}

	public String getServerAddress() {
		return netUtils.getMainAddress();
	}

}
