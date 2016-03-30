package com.thunisoft.sswy.mobile.logic;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.app.Activity;

import com.thunisoft.sswy.mobile.logic.net.DeleteLayyResponse;
import com.thunisoft.sswy.mobile.logic.net.DeleteSsclFjResUtil;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;

/**
 * 网上立案 删除
 * 
 * @author gewx
 * 
 */
@EBean(scope = Scope.Singleton)
public class DeleteLogic {

	private static final String TAG = DeleteLogic.class.getSimpleName();

	@Bean
	protected NetUtils netUtils;

	@Bean
	protected DeleteSsclFjResUtil delSsclFjResUtils;
	
	@Bean
	protected DeleteLayyResponse deleteLayyResponse;
	
	public Activity activity;
	
	/**
	 * 删除请求服务器，返回结果监听  
	 */
	private DelSsclFjResponseListener deleteResultListener;

	/**
	 * 删除起诉状附件或者证件附件
	 * 
	 * @return
	 */
	public BaseResponse deleteSsclFj(TLayySscl sscl, TLayySsclFj ssclFj) {
		String url = netUtils.getMainAddress() + "/api/wsla/deleteFj";
		delSsclFjResUtils.activity = activity;
		delSsclFjResUtils.clearParams();
		delSsclFjResUtils.addParam("fjId", ssclFj.getCId());
		delSsclFjResUtils.addParam("layyId", sscl.getCLayyId());
		delSsclFjResUtils.addParam("type", String.valueOf(sscl.getNType()));
		delSsclFjResUtils.setSsclFjId(ssclFj.getCId());
		delSsclFjResUtils.setDeleteResultListener(deleteResultListener);
		return delSsclFjResUtils.getResponse(url, delSsclFjResUtils.getParams());
	}

	/**
	 * 删除证据
	 * 
	 * @return
	 */
	public BaseResponse deleteZj(TZj zj, TZjcl zjcl) {
		String url = netUtils.getMainAddress() + "/api/wsla/deleteFj";
		delSsclFjResUtils.activity = activity;
		delSsclFjResUtils.clearParams();
		delSsclFjResUtils.addParam("fjId", zjcl.getCId());
		delSsclFjResUtils.addParam("layyId", zj.getCYwBh());
		delSsclFjResUtils.addParam("type",String.valueOf(3));
		delSsclFjResUtils.setSsclFjId(zjcl.getCId());
		delSsclFjResUtils.setDeleteResultListener(deleteResultListener);
		return delSsclFjResUtils.getResponse(url, delSsclFjResUtils.getParams());
	}
	
	/**
	 * 删除请求服务器，返回结果监听  
	 */
	private DelLayyResponseListener delLayyResponseListener;
	
	/**
	 * 删除立案预约
	 * 
	 * @return
	 */
	public BaseResponse deleteLayy(TLayy layy) {
		String url = netUtils.getMainAddress() + "/api/wsla/deleteLayy";
		deleteLayyResponse.activity = activity;
		deleteLayyResponse.clearParams();
		deleteLayyResponse.addParam("layyId", layy.getCId());
		deleteLayyResponse.setDelLayyResponseListener(delLayyResponseListener);
		return deleteLayyResponse.getResponse(url, deleteLayyResponse.getParams());
	}
	
	/**  
	 * 设置  删除请求服务器，返回结果监听  
	 * @param deleteResultListener
	 */
	public void setDeleteResultListener(DelSsclFjResponseListener deleteResultListener) {
		this.deleteResultListener = deleteResultListener;
	}

	/**  
	 * 设置  删除立案预约 监听
	 * @param delLayyResponseListener
	 */
	public void setDelLayyResponseListener(DelLayyResponseListener delLayyResponseListener) {
		this.delLayyResponseListener = delLayyResponseListener;
	}

	/**
	 * 删除诉讼材料附件请求服务器，返回结果监听 
	 * @author gewx
	 *
	 */
	public interface DelSsclFjResponseListener {
		
		/**
		 * 删除请求服务器返回结果，和诉讼材料附件Id
		 * @param response
		 * @param ssclFjId
		 */
		public void deleteResult(BaseResponse response, String ssclFjId);

	}
	
	/**
	 * 删除立案预约附件请求服务器，返回结果监听 
	 * @author gewx
	 *
	 */
	public interface DelLayyResponseListener {
		
		/**
		 * 删除立案预约
		 * @param response
		 * 
		 * @param layyId
		 */
		public void deleteResult(BaseResponse response, String layyId);

	}
}
