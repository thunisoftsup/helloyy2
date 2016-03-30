package com.thunisoft.sswy.mobile.logic;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.app.Activity;

import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.datasource.NrcSsclDao;
import com.thunisoft.sswy.mobile.logic.net.DownloadSfrzclResUtil;
import com.thunisoft.sswy.mobile.logic.net.DownloadSsclResUtil;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;

/**
 * 网上立案 下载文件
 * 
 * @author gewx
 * 
 */
@EBean(scope = Scope.Singleton)
public class DownloadLogic {

	private static final String TAG = DownloadLogic.class.getSimpleName();

	public Activity activity;
	
	@Bean
	NetUtils netUtils;

	@Bean
	DownloadSfrzclResUtil sfrzclResUtil;

	@Bean
	LoginCache loginCache;

	@Bean
	NrcSsclDao nrcSsclDao;

	@Bean
	DownloadSsclResUtil ssclResUtil;
	
	/**
	 * 下载 申请人身份认证材料
	 * 
	 * @return
	 */
	public BaseResponse downloadSmrzcl(TProUserSfrzCl sfrzCl, int openType) {
		String url = netUtils.getMainAddress() + "/api/wsla/downloadSmrzcl";
		sfrzclResUtil.clearParams();
		sfrzclResUtil.addParam("layyId", sfrzCl.getCLayyId());
		sfrzclResUtil.addParam("type", String.valueOf(sfrzCl.getNClLx()));
		sfrzclResUtil.activity = activity;
		sfrzclResUtil.setSfrzCl(sfrzCl);
		sfrzclResUtil.setOpenType(openType);
		return sfrzclResUtil.getResponse(url, sfrzclResUtil.getParams());
	}

	/**
	 * 下载 诉讼材料
	 * 
	 * @return
	 */
	public void downloadSscl(TLayySsclFj ssclFj, int type) {
		String url = netUtils.getMainAddress() + "/api/wsla/downloadSscl";
		ssclResUtil.clearParams();
		ssclResUtil.addParam("layyId", ssclFj.getCLayyId());
		ssclResUtil.addParam("ssclFjId", ssclFj.getCId());
		ssclResUtil.addParam("type", String.valueOf(type));
		ssclResUtil.activity = activity;
		ssclResUtil.setSsclFj(ssclFj);
		ssclResUtil.setType(DownloadSsclResUtil.TYPE_SSCL);
		ssclResUtil.getResponse(url, ssclResUtil.getParams());
	}
	
	/**
	 * 下载 证据
	 * 
	 * @return
	 */
	public void downloadZj(TZj zj, TZjcl zjcl) {
		String url = netUtils.getMainAddress() + "/api/wsla/downloadSscl";
		ssclResUtil.clearParams();
		ssclResUtil.addParam("layyId", zj.getCYwBh());
		ssclResUtil.addParam("ssclFjId", zjcl.getCId());
		ssclResUtil.addParam("type", String.valueOf(3));
		ssclResUtil.activity = activity;
		ssclResUtil.setZjcl(zjcl);
		ssclResUtil.setType(DownloadSsclResUtil.TYPE_ZJ);
		ssclResUtil.getResponse(url, ssclResUtil.getParams());
	}
}
