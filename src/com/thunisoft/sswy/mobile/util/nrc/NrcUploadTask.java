package com.thunisoft.sswy.mobile.util.nrc;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.UiThread;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.datasource.NrcBasicDao;
import com.thunisoft.sswy.mobile.datasource.NrcSfrzclDao;
import com.thunisoft.sswy.mobile.datasource.NrcSsclDao;
import com.thunisoft.sswy.mobile.datasource.NrcSsclFjDao;
import com.thunisoft.sswy.mobile.datasource.NrcZjDao;
import com.thunisoft.sswy.mobile.datasource.NrcZjclDao;
import com.thunisoft.sswy.mobile.logic.NrcSubmitLogic;
import com.thunisoft.sswy.mobile.logic.UploadLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;

/**
 * 网上立案 上传文件
 */
@EBean(scope = Scope.Singleton)
public class NrcUploadTask {

	private static final String TAG = NrcUploadTask.class.getSimpleName();

	public Activity activity;

	/**
	 * 当前是否正在运行上传服务器的任务
	 */
	private boolean isRunning = false;

	/** 提交 网上立案 */
	@Bean
	NrcSubmitLogic nrcSubmitLogic;

	/** 提交 网上立案 */
	@Bean
	UploadLogic uploadLogic;

	/**
	 * 登录信息
	 */
	@Bean
	LoginCache loginCache;

	/**
	 * 网上立案_基本信息
	 */
	@Bean
	NrcBasicDao nrcBasicDao;

	/**
	 * 网上立案_诉讼材料
	 */
	@Bean
	NrcSsclDao nrcSsclDao;

	/**
	 * 网上立案_诉讼材料_附件
	 */
	@Bean
	NrcSsclFjDao nrcSsclFjDao;

	/**
	 * 网上立案_证据材料
	 */
	@Bean
	NrcZjclDao nrcZjclDao;

	/**
	 * 网上立案_证据
	 */
	@Bean
	NrcZjDao nrcZjDao;

	/**
	 * 网上立案_申请人身份认证材料
	 */
	@Bean
	NrcSfrzclDao nrcSfrzclDao;

	@Background
	public void startUploadTask(Context context) {
		if (isRunning) {
			return;
		} else {
			isRunning = true;
			uploadLayyList();
		}
	}

	/**
	 * 循环上传 网上立案
	 */
	private void uploadLayyList() {
		List<TLayy> layyList = nrcBasicDao.getLocalLayyList(NrcUtils.NRC_STATUS_ALL);
		if (null != layyList && layyList.size() > 0) {
			TLayy layy = layyList.get(0);
			BaseResponse layyResponse = nrcSubmitLogic.submitNetRegisterCase(layy, loginCache.getSessionId());
			if (layyResponse.isXtcw()) {
				if (layyResponse.isSuccess()) {
					uploadSsclFjList(layy.getCId());
					uploadZjclList(layy.getCId());
					uploadSfrzclList(layy.getCId());
					TLayy localLayy = nrcBasicDao.getLayyById(layy.getCId());
					if (layy.getDUpdate().equals(localLayy.getDUpdate())) { // 如果上传之前的更新时间和现在的更新时间不同，说明上传过程中，数据有变化
						layy.setNSync(NrcConstants.SYNC_TRUE);
						List<TLayy> saveLayyList = new ArrayList<TLayy>();
						saveLayyList.add(layy);
						nrcBasicDao.updateOrSaveLayy(saveLayyList);
					}
					uploadLayyList();
				} else {
					nrcBasicDao.deleteLayy(activity, layy.getCId()); //如果提交服务器返回失败的话，需要删除本地的该条数据，因为服务器端可能做了修改
					uploadLayyList();
				}
			} else {
				showToast(layyResponse.getMessage());
				isRunning = false;
			}
		} else { // 网上立案 数据上完完毕之后，开始上传 诉讼材料_附件 数据
			uploadLocalSsclFjList();
			uploadLocalZjclList();
			uploadLocalSfrzclList();
			isRunning = false;
		}
	}

	@UiThread
	protected void showToast(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 循环上传 诉讼材料_附件
	 */
	private void uploadSsclFjList(String layyId) {
		List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
		List<TLayySscl> ssclList = nrcSsclDao.getSsclListBylayyId(layyId, NrcSsclDao.TYPE_ALL, NrcSsclDao.TOP_ALL);
		if (null != ssclList && ssclList.size() > 0) {
			for (TLayySscl sscl : ssclList) {
				List<TLayySsclFj> localSsclFjList = nrcSsclFjDao.getSsclFjListBySsclId(sscl.getCId(), NrcConstants.SYNC_FALSE);
				if (null != localSsclFjList && localSsclFjList.size() > 0) {
					ssclFjList.addAll(localSsclFjList);
					break;
				}
			}
		}
		if (null != ssclFjList && ssclFjList.size() > 0) {
			TLayySsclFj ssclFj = ssclFjList.get(0);
			TLayySscl sscl = nrcSsclDao.getSsclById(ssclFj.getCSsclId());
			if (null == sscl) {
				nrcSsclFjDao.deleteSsclFjById(ssclFj.getCId());
			} else {
				BaseResponse response = uploadLogic.uploadSscl(sscl, ssclFj);
				if (response.isXtcw()) {
					if (response.isSuccess()) {
						uploadSsclFjList(layyId);
					} else {
						nrcSsclFjDao.deleteSsclFjById(ssclFj.getCId());
						uploadSsclFjList(layyId);
					}
				} else {
					showToast(response.getMessage());
				}
			}
		}
	}

	/**
	 * 循环上传 本地之前没上传到服务器端的诉讼材料_附件
	 */
	private void uploadLocalSsclFjList() {
		List<TLayySsclFj> ssclFjList = nrcSsclFjDao.getLocalSsclFjList();
		if (null != ssclFjList && ssclFjList.size() > 0) {
			TLayySsclFj ssclFj = ssclFjList.get(0);
			TLayySscl sscl = nrcSsclDao.getSsclById(ssclFj.getCSsclId());
			if (null == sscl) {
				nrcSsclFjDao.deleteSsclFjById(ssclFj.getCId());
			} else {
				BaseResponse response = uploadLogic.uploadSscl(sscl, ssclFj);
				if (response.isXtcw()) {
					if (response.isSuccess()) {
						uploadLocalSsclFjList();
					} else {
						uploadLocalSsclFjList();
						nrcSsclFjDao.deleteSsclFjById(ssclFj.getCId());
					}
				} else {
					showToast(response.getMessage());
				}
			}
		}
	}
	
	/**
	 * 循环上传 证据材料
	 */
	private void uploadZjclList(String layyId) {
		List<TZjcl> zjclList = new ArrayList<TZjcl>();
		List<TZj> zjList = nrcZjDao.getZjListBylayyId(layyId, NrcZjDao.TOP_ALL);
		if (null != zjList && zjList.size() > 0) {
			for (TZj zj : zjList) {
				List<TZjcl> localZjclList = nrcZjclDao.getZjclListByZjId(zj.getCId(), NrcZjclDao.TOP_ALL, NrcConstants.SYNC_FALSE);
				if (null != localZjclList && localZjclList.size() > 0) {
					zjclList.addAll(localZjclList);
					break;
				}
			}
		}
		if (null != zjclList && zjclList.size() > 0) {
			TZjcl zjcl = zjclList.get(0);
			TZj zj = nrcZjDao.getZjById(zjcl.getCZjBh());
			if (null == zj) {
				List<String> zjclIdList = new ArrayList<String>();
				zjclIdList.add(zjcl.getCId());
				nrcZjclDao.deleteZjclByZjId(zjclIdList);
			} else {
				BaseResponse response = uploadLogic.uploadZj(zj, zjcl);
				if (response.isXtcw()) {
					if (response.isSuccess()) {
						uploadZjclList(layyId);
					} else {
						List<String> idList = new ArrayList<String>();
						idList.add(zjcl.getCId());
						nrcZjclDao.deleteZjcl(idList);
						uploadZjclList(layyId);
					}
				} else {
					showToast(response.getMessage());
				}
			}
		}
	}

	private void uploadLocalZjclList() {
		List<TZjcl> zjclList = nrcZjclDao.getLocalZjclList();
		if (null != zjclList && zjclList.size() > 0) {
			TZjcl zjcl = zjclList.get(0);
			TZj zj = nrcZjDao.getZjById(zjcl.getCZjBh());
			if (null == zj) {
				List<String> zjclIdList = new ArrayList<String>();
				zjclIdList.add(zjcl.getCId());
				nrcZjclDao.deleteZjclByZjId(zjclIdList);
			} else {
				BaseResponse response = uploadLogic.uploadZj(zj, zjcl);
				if (response.isXtcw()) {
					if (response.isSuccess()) {
						uploadLocalZjclList();
					} else {
						List<String> idList = new ArrayList<String>();
						idList.add(zjcl.getCId());
						nrcZjclDao.deleteZjcl(idList);
						uploadLocalZjclList();
					}
				} else {
					showToast(response.getMessage());
				}
			}
		}
	}
	
	/**
	 * 循环上传 申请人身份认证材料
	 */
	private void uploadSfrzclList(String layyId) {
		List<TProUserSfrzCl> sfrzclList = nrcSfrzclDao.getUploadSfrzClList(layyId, NrcConstants.SYNC_FALSE);
		if (null != sfrzclList && sfrzclList.size() > 0) {
			TProUserSfrzCl sfrzcl = sfrzclList.get(0);
			BaseResponse response = uploadLogic.uploadSfrzcl(sfrzcl);
			if (response.isXtcw()) {
				if (response.isSuccess()) {
					uploadSfrzclList(layyId);
				} else {
					List<String> idList = new ArrayList<String>();
					idList.add(sfrzcl.getCBh());
					nrcSfrzclDao.deleteSfrzcl(idList);
					uploadSfrzclList(layyId);
				}
			} else {
				showToast(response.getMessage());
			}
		}
	}

	private void uploadLocalSfrzclList() {
		List<TProUserSfrzCl> sfrzclList = nrcSfrzclDao.getLocalSfrzClList();
		if (null != sfrzclList && sfrzclList.size() > 0) {
			TProUserSfrzCl sfrzcl = sfrzclList.get(0);
			BaseResponse response = uploadLogic.uploadSfrzcl(sfrzcl);
			if (response.isXtcw()) {
				if (response.isSuccess()) {
					uploadLocalSfrzclList();
				} else {
					List<String> idList = new ArrayList<String>();
					idList.add(sfrzcl.getCBh());
					nrcSfrzclDao.deleteSfrzcl(idList);
					uploadLocalSfrzclList();
				}
			} else {
				showToast(response.getMessage());
			}
		}
	}
	
	/**
	 * 获取 是否正在运行上传服务器的任务
	 * 
	 * @return isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * 设置 是否正在运行上传服务器的任务
	 * 
	 * @param isRunning
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}