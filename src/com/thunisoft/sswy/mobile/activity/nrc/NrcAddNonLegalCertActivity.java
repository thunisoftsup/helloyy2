package com.thunisoft.sswy.mobile.activity.nrc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcSsclFjAdapter;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;
import com.thunisoft.sswy.mobile.view.NoScrollGridView;

/**
 * 网上立案 当事人_非法人 添加证件
 * 
 */
@EActivity(R.layout.activity_nrc_add_nonlegal_cert)
public class NrcAddNonLegalCertActivity extends BaseActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_anlc_title)
	protected TextView titleTV;

	/**
	 * 组织机构代码证复印件_提示
	 */
	@ViewById(R.id.nrc_anlc_zzjgdmz_fyj_tip)
	protected TextView zzjgDmzFyjTipTV;

	/**
	 * 组织机构代码证复印件_附件
	 */
	@ViewById(R.id.nrc_alc_zzjgdmz_fj_list)
	protected NoScrollGridView zzjgDmzFjGridView;

	/**
	 * 主要负责人身份证明书_提示
	 */
	@ViewById(R.id.nrc_anlc_fzr_zms_tip)
	protected TextView fzrZmsTipTV;

	/**
	 * 主要负责人身份证明书_附件
	 */
	@ViewById(R.id.nrc_anlc_fzr_zms_fj_list)
	protected NoScrollGridView fzrSfzmsGridView;

	/** 证件所属人列表，点击的当事人 intent key */
	public static final String K_LITIGANT = "litigant";

	/** 证件所属人列表，点击的 当事人 */
	private TLayyDsr dsr;

	private static final String KEY_ZZJGDMZ_FYJ_FJ = "zzjgDmzFjyFj";

	private static final String KEY_FZR_ID_ZMS_FJ = "fzrIdZmsFj";

	private Map<String, TLayySscl> ssclMap = new HashMap<String, TLayySscl>();

	/** 组织机构代码证复印件_附件 */
	private List<TLayySsclFj> zzjgDmzFjList = new ArrayList<TLayySsclFj>();

	/** 主要负责人身份证明书_附件 */
	private List<TLayySsclFj> fzrSfzmsFjList = new ArrayList<TLayySsclFj>();
	
	/** 组织机构代码证复印件 证件 */
	private NrcSsclFjAdapter zzjgDmzFjAdapter;

	/** 主要负责人身份证明书 证件 */
	private NrcSsclFjAdapter fzrSfzmsFjAdapter;

	/**
	 * 添加_组织机构代码证复印件
	 */
	private static final int REQ_CODE_ADD_ZZJGDMZ_FYJ = 3;

	/**
	 * 添加_主要负责人身份证明书
	 */
	private static final int REQ_CODE_ADD_FZR_ID_ZMS = 4;

	@Bean
	protected DownloadLogic downloadLogic;
	
	@Bean
	protected DeleteLogic deleteLogic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		dsr = (TLayyDsr) intent.getSerializableExtra(K_LITIGANT);
	}

	@AfterViews
	protected void onAfterView() {
		downloadLogic.activity = this;
		deleteLogic.activity = this;
		titleTV.setText(dsr.getCName());
		zzjgDmzFyjTipTV.setText(NrcUtils.ZJ_ZZJG_DMZ_FYJ);
		fzrZmsTipTV.setText(NrcUtils.ZJ_FZR_ID_ZMS);
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshActivity();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Constants.RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case REQ_CODE_ADD_ZZJGDMZ_FYJ:
			SerializableSOMap zmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> zmMap = zmSoMap.getMap();
			if (null != zmMap && zmMap.size() > 0) {
				for (Map.Entry<String, Object> entry : zmMap.entrySet()) {
					String facePhotoPath = entry.getKey();
					buildDsrCertPhoto(facePhotoPath, NrcUtils.ZJ_ZZJG_DMZ_FYJ);
					break;
				}
			}
			break;

		case REQ_CODE_ADD_FZR_ID_ZMS:
			SerializableSOMap bmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> bmMap = bmSoMap.getMap();
			if (null != bmMap && bmMap.size() > 0) {
				for (Map.Entry<String, Object> entry : bmMap.entrySet()) {
					String reversePhotoPath = entry.getKey();
					buildDsrCertPhoto(reversePhotoPath, NrcUtils.ZJ_FZR_ID_ZMS);
					break;
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 点击 当事人 返回
	 */
	@Click(R.id.nrc_anlc_back)
	protected void clickBack() {
		NrcAddNonLegalCertActivity.this.finish();
	}

	/**
	 * 点击 当事人 保存
	 */
	@Click(R.id.nrc_anlc_save)
	protected void clickSave() {
		if (checkLitigantCert()) {
			saveDsrCertPhoto();
			NrcAddNonLegalCertActivity.this.finish();
		}
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		List<TLayySscl> localZjList = NrcEditData.getCertificateListMap().get(dsr.getCId());// 获取之前存放的该人员的证件List
		if (null != localZjList && localZjList.size() > 0) {
			for (TLayySscl sscl : localZjList) {// 证件
				ArrayList<TLayySsclFj> localCertFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
				if (NrcUtils.ZJ_ZZJG_DMZ_FYJ.equals(sscl.getCName())) { // 营业执照复印件
					ssclMap.put(KEY_ZZJGDMZ_FYJ_FJ, sscl);
					if (null != localCertFjList) {
						zzjgDmzFjList.addAll(localCertFjList);
					}
				} else if (NrcUtils.ZJ_FZR_ID_ZMS.equals(sscl.getCName())) { // 法定代表人身份证明书
					ssclMap.put(KEY_FZR_ID_ZMS_FJ, sscl);
					if (null != localCertFjList) {
						fzrSfzmsFjList.addAll(localCertFjList);
					}
				}
			}
		}
	}

	/**
	 * 刷新整个页面
	 */
	private void refreshActivity() {
		if (null == zzjgDmzFjAdapter) {
			TLayySscl sscl = ssclMap.get(KEY_ZZJGDMZ_FYJ_FJ);
			zzjgDmzFjAdapter = new NrcSsclFjAdapter(this, sscl, zzjgDmzFjList);
			zzjgDmzFjAdapter.setSelectType(NrcConstants.SELECT_TYPE_SINGLE);
			zzjgDmzFjAdapter.setDownloadLogic(downloadLogic);
			zzjgDmzFjAdapter.setDeleteLogic(deleteLogic);
			zzjgDmzFjAdapter.setRequestCode(REQ_CODE_ADD_ZZJGDMZ_FYJ);
			zzjgDmzFjAdapter.setSsclFjType(NrcConstants.SSCL_TYPE_CERTIFICATE);
			zzjgDmzFjAdapter.setFjType(NrcSsclFjAdapter.FJ_TYPE_SINGLE);
			zzjgDmzFjGridView.setAdapter(zzjgDmzFjAdapter);
		} else {
			zzjgDmzFjAdapter.notifyDataSetChanged();
		}

		if (null == fzrSfzmsFjAdapter) {
			TLayySscl sscl = ssclMap.get(KEY_FZR_ID_ZMS_FJ);
			fzrSfzmsFjAdapter = new NrcSsclFjAdapter(this, sscl, fzrSfzmsFjList);
			fzrSfzmsFjAdapter.setDownloadLogic(downloadLogic);
			fzrSfzmsFjAdapter.setDeleteLogic(deleteLogic);
			fzrSfzmsFjAdapter.setRequestCode(REQ_CODE_ADD_FZR_ID_ZMS);
			fzrSfzmsFjAdapter.setSsclFjType(NrcConstants.SSCL_TYPE_CERTIFICATE);
			fzrSfzmsFjAdapter.setFjType(NrcSsclFjAdapter.FJ_TYPE_SINGLE);
			fzrSfzmsGridView.setAdapter(fzrSfzmsFjAdapter);
		} else {
			fzrSfzmsFjAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 构造当事人证件数据到当前类的变量
	 * 
	 * @param filePath
	 * @param direction
	 */
	private void buildDsrCertPhoto(String filePath, String certName) {
		TLayySscl cert = null;
		if (NrcUtils.ZJ_ZZJG_DMZ_FYJ.equals(certName)) {
			cert = ssclMap.get(KEY_ZZJGDMZ_FYJ_FJ);
			if (null == cert) {
				cert = initLitigangCert(certName);
				ssclMap.put(KEY_ZZJGDMZ_FYJ_FJ, cert);
			}
		} else if (NrcUtils.ZJ_FZR_ID_ZMS.equals(certName)) {
			cert = ssclMap.get(KEY_FZR_ID_ZMS_FJ);
			if (null == cert) {
				cert = initLitigangCert(certName);
				ssclMap.put(KEY_FZR_ID_ZMS_FJ, cert);
			}
		}

		if (NrcUtils.ZJ_ZZJG_DMZ_FYJ.equals(certName)) {
			if (zzjgDmzFjList.size() == 0) {
				File photoFile = new File(filePath);
				TLayySsclFj newCertFj = new TLayySsclFj();
				newCertFj.setCId(UUIDHelper.getUuid());
				newCertFj.setCLayyId(dsr.getCLayyId());
				newCertFj.setCOriginName(photoFile.getName());
				newCertFj.setCPath(photoFile.getAbsolutePath());
				newCertFj.setCSsclId(cert.getCId());
				newCertFj.setNXssx(0);
				newCertFj.setNSync(NrcConstants.SYNC_FALSE);
				zzjgDmzFjList.add(newCertFj);
			} else { // 替换路径 ------此时诉讼材料和诉讼材料附件为一对一，所以 下面值获取 newCertFjList.get(0)
				// 因为在和服务器同步的时候，sync=false，是不需要上传的，所以这里不能改变 sync的值
				TLayySsclFj newCertFj = zzjgDmzFjList.get(0);
				newCertFj.setNSync(NrcConstants.SYNC_FALSE);
				newCertFj.setCPath(filePath);
			}
		} else if (NrcUtils.ZJ_FZR_ID_ZMS.equals(certName)) {
			if (fzrSfzmsFjList.size() == 0) {
				File photoFile = new File(filePath);
				TLayySsclFj newCertFj = new TLayySsclFj();
				newCertFj.setCId(UUIDHelper.getUuid());
				newCertFj.setCLayyId(dsr.getCLayyId());
				newCertFj.setCOriginName(photoFile.getName());
				newCertFj.setCPath(photoFile.getAbsolutePath());
				newCertFj.setCSsclId(cert.getCId());
				newCertFj.setNXssx(0);
				newCertFj.setNSync(NrcConstants.SYNC_FALSE);
				fzrSfzmsFjList.add(newCertFj);
			} else { // 替换路径 ------此时诉讼材料和诉讼材料附件为一对一，所以 下面值获取 newCertFjList.get(0)
				// 因为在和服务器同步的时候，sync=false，是不需要上传的，所以这里不能改变 sync的值
				TLayySsclFj newCertFj = fzrSfzmsFjList.get(0);
				newCertFj.setNSync(NrcConstants.SYNC_FALSE);
				newCertFj.setCPath(filePath);
			}
		}
	}

	/**
	 * 保存当事人证件到静态代码
	 */
	private void saveDsrCertPhoto() {
		ArrayList<TLayySscl> localZjList = NrcEditData.getCertificateListMap().get(dsr.getCId());
		if (null == localZjList) {
			localZjList = new ArrayList<TLayySscl>();
			NrcEditData.getCertificateListMap().put(dsr.getCId(), localZjList);
		} else {
			localZjList.clear();
		}
		localZjList.add(ssclMap.get(KEY_ZZJGDMZ_FYJ_FJ));
		localZjList.add(ssclMap.get(KEY_FZR_ID_ZMS_FJ));

		for (TLayySscl zj : localZjList) {
			ArrayList<TLayySsclFj> localZjFjList = NrcEditData.getCertificateFjListMap().get(zj.getCId());
			if (null == localZjFjList) {
				localZjFjList = new ArrayList<TLayySsclFj>();
				NrcEditData.getCertificateFjListMap().put(zj.getCId(), localZjFjList);
			} else {
				localZjFjList.clear();
			}
			if (NrcUtils.ZJ_ZZJG_DMZ_FYJ.equals(zj.getCName())) {
				localZjFjList.addAll(zzjgDmzFjList);
			} else if (NrcUtils.ZJ_FZR_ID_ZMS.equals(zj.getCName())) {
				localZjFjList.addAll(fzrSfzmsFjList);
			}

			ArrayList<TLayyDsrSscl> localDsrSsclList = NrcEditData.getDsrCertificateListMap().get(dsr.getCId());
			if (null == localDsrSsclList) {
				localDsrSsclList = new ArrayList<TLayyDsrSscl>();
				NrcEditData.getDsrCertificateListMap().put(dsr.getCId(), localDsrSsclList);
			} else {
				localDsrSsclList.clear();
			}

			TLayyDsrSscl dsrSscl = new TLayyDsrSscl();
			dsrSscl.setCId(UUIDHelper.getUuid());
			dsrSscl.setCDsrId(dsr.getCId());
			dsrSscl.setCDsrName(dsr.getCName());
			dsrSscl.setCLayyId(dsr.getCLayyId());
			dsrSscl.setCSsclId(zj.getCId());
			dsrSscl.setCSsclName(zj.getCName());
			localDsrSsclList.add(dsrSscl);
		}
	}

	private boolean checkLitigantCert() {
		if (zzjgDmzFjList.size() == 0 && fzrSfzmsFjList.size() == 0) {
			Toast.makeText(this, "请上传证件", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (zzjgDmzFjList.size() == 0 ) {
			Toast.makeText(this, "请上传组织机构代码证复印件", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (fzrSfzmsFjList.size() == 0) {
			Toast.makeText(this, "请上传主要负责人身份证明书", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 初始化当事人证件
	 * 
	 * @param certName
	 * @return
	 */
	private TLayySscl initLitigangCert(String certName) {
		TLayySscl newCert = new TLayySscl();
		newCert.setCId(UUIDHelper.getUuid());
		newCert.setCLayyId(dsr.getCLayyId());
		newCert.setCSsryId(dsr.getCId());
		newCert.setCSsryMc(dsr.getCName());
		newCert.setNType(NrcConstants.SSCL_TYPE_CERTIFICATE);
		newCert.setCName(certName);
		newCert.setNXssx(0);
		return newCert;
	}
}
