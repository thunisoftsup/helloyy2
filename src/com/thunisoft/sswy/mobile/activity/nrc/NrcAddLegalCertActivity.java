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
 * 网上立案 当事人_法人 添加证件
 * 
 */
@EActivity(R.layout.activity_nrc_add_legal_cert)
public class NrcAddLegalCertActivity extends BaseActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_alc_title)
	protected TextView titleTV;

	/**
	 * 营业执照复印件_提示
	 */
	@ViewById(R.id.nrc_alc_yyzz_fyj_tip)
	protected TextView yyzzTipTV;

	/**
	 * 营业执照复印件_附件
	 */
	@ViewById(R.id.nrc_alc_yyzz_fj_list)
	protected NoScrollGridView yyzzGridView;

	/**
	 * 法定代表人身份证明书_附件_提示
	 */
	@ViewById(R.id.nrc_alc_legal_id_zms_tip)
	protected TextView frSfzmsTipTV;

	/**
	 * 法定代表人身份证明书_附件
	 */
	@ViewById(R.id.nrc_alc_fr_sfzms_fj_list)
	protected NoScrollGridView frSfzmsGridView;

	/** 证件所属人列表，点击的当事人 intent key */
	public static final String K_LITIGANT = "litigant";

	/** 证件所属人列表，点击的 当事人 */
	private TLayyDsr dsr;

	private static final String K_YYZZ = "yyzz";
	
	private static final String K_FR_SFZMS = "frSfzms";
	
	private Map<String, TLayySscl> ssclMap = new HashMap<String, TLayySscl>();

	/** 营业执照 */
	private List<TLayySsclFj> yyzzFjList = new ArrayList<TLayySsclFj>();

	/** 法定代表人身份证明 */
	private List<TLayySsclFj> frSfzmsFjList = new ArrayList<TLayySsclFj>();

	/**
	 * 添加_营业执照复印件
	 */
	private static final int REQ_CODE_ADD_YYZZ_FYJ = 3;

	/**
	 * 添加_法定代表人身份证明书
	 */
	private static final int REQ_CODE_ADD_FR_SF_ZMS = 4;

	/** 营业执照 证件 */
	private NrcSsclFjAdapter yyzzFjAdapter;

	/** 法定代表人身份证明书 证件 */
	private NrcSsclFjAdapter frSfzmsFjAdapter;
	
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
		yyzzTipTV.setText(NrcUtils.ZJ_YYZZ_FYJ);
		frSfzmsTipTV.setText(NrcUtils.ZJ_FDDBR_SF_ZMS);
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
		case REQ_CODE_ADD_YYZZ_FYJ:
			SerializableSOMap zmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> zmMap = zmSoMap.getMap();
			if (null != zmMap && zmMap.size() > 0) {
				for (Map.Entry<String, Object> entry : zmMap.entrySet()) {
					String facePhotoPath = entry.getKey();
					buildDsrCertPhoto(facePhotoPath, NrcUtils.ZJ_YYZZ_FYJ);
					break;
				}
			}
			break;

		case REQ_CODE_ADD_FR_SF_ZMS:
			SerializableSOMap bmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> bmMap = bmSoMap.getMap();
			if (null != bmMap && bmMap.size() > 0) {
				for (Map.Entry<String, Object> entry : bmMap.entrySet()) {
					String reversePhotoPath = entry.getKey();
					buildDsrCertPhoto(reversePhotoPath, NrcUtils.ZJ_FDDBR_SF_ZMS);
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
	@Click(R.id.nrc_alc_back)
	protected void clickBack() {
		NrcAddLegalCertActivity.this.finish();
	}

	/**
	 * 点击 当事人 保存
	 */
	@Click(R.id.nrc_alc_save)
	protected void clickSave() {
		if (checkLitigantCert()) {
			saveDsrCertPhoto();
			NrcAddLegalCertActivity.this.finish();
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
				if (NrcUtils.ZJ_YYZZ_FYJ.equals(sscl.getCName())) { // 营业执照复印件
					ssclMap.put(K_YYZZ, sscl);
					if (null != localCertFjList) { // 存在附件，需要放到当前类中对象
						yyzzFjList.addAll(localCertFjList);
					}
				} else if (NrcUtils.ZJ_FDDBR_SF_ZMS.equals(sscl.getCName())) { // 法定代表人身份证明书
					ssclMap.put(K_FR_SFZMS, sscl);
					if (null != localCertFjList) { // 存在附件，需要放到当前类中对象
						frSfzmsFjList.addAll(localCertFjList);
					}
				}
			}
		}
	}

	/**
	 * 刷新整个页面
	 */
	private void refreshActivity() {
		if (null == yyzzFjAdapter) {
			TLayySscl sscl = ssclMap.get(K_YYZZ);
			yyzzFjAdapter = new NrcSsclFjAdapter(this, sscl, yyzzFjList);
			yyzzFjAdapter.setSelectType(NrcConstants.SELECT_TYPE_SINGLE);
			yyzzFjAdapter.setDownloadLogic(downloadLogic);
			yyzzFjAdapter.setDeleteLogic(deleteLogic);
			yyzzFjAdapter.setRequestCode(REQ_CODE_ADD_YYZZ_FYJ);
			yyzzFjAdapter.setSsclFjType(NrcConstants.SSCL_TYPE_CERTIFICATE);
			yyzzFjAdapter.setFjType(NrcSsclFjAdapter.FJ_TYPE_SINGLE);
			yyzzGridView.setAdapter(yyzzFjAdapter);
		} else {
			yyzzFjAdapter.notifyDataSetChanged();
		}

		if (null == frSfzmsFjAdapter) {
			TLayySscl sscl = ssclMap.get(K_FR_SFZMS);
			frSfzmsFjAdapter = new NrcSsclFjAdapter(this, sscl, frSfzmsFjList);
			frSfzmsFjAdapter.setDownloadLogic(downloadLogic);
			frSfzmsFjAdapter.setDeleteLogic(deleteLogic);
			frSfzmsFjAdapter.setRequestCode(REQ_CODE_ADD_FR_SF_ZMS);
			frSfzmsFjAdapter.setSsclFjType(NrcConstants.SSCL_TYPE_CERTIFICATE);
			frSfzmsFjAdapter.setFjType(NrcSsclFjAdapter.FJ_TYPE_SINGLE);
			frSfzmsGridView.setAdapter(frSfzmsFjAdapter);
		} else {
			frSfzmsFjAdapter.notifyDataSetChanged();
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
		if (NrcUtils.ZJ_YYZZ_FYJ.equals(certName)) {
			cert = ssclMap.get(K_YYZZ);
			if (null == cert) {
				cert = initLitigangCert(certName);
				ssclMap.put(K_YYZZ, cert);
			}
		} else if (NrcUtils.ZJ_FDDBR_SF_ZMS.equals(certName)) {
			cert = ssclMap.get(K_FR_SFZMS);
			if (null == cert) {
				cert = initLitigangCert(certName);
				ssclMap.put(K_FR_SFZMS, cert);
			}
		}
		
		if (NrcUtils.ZJ_YYZZ_FYJ.equals(certName)) {
			if (yyzzFjList.size() == 0) {
				File photoFile = new File(filePath);
				TLayySsclFj newCertFj = new TLayySsclFj();
				newCertFj.setCId(UUIDHelper.getUuid());
				newCertFj.setCLayyId(dsr.getCLayyId());
				newCertFj.setCOriginName(photoFile.getName());
				newCertFj.setCPath(photoFile.getAbsolutePath());
				newCertFj.setCSsclId(cert.getCId());
				newCertFj.setNXssx(0);
				newCertFj.setNSync(NrcConstants.SYNC_FALSE);
				yyzzFjList.add(newCertFj);
			} else { // 替换路径 ------此时诉讼材料和诉讼材料附件为一对一，所以 下面值获取 newCertFjList.get(0)
				// 因为在和服务器同步的时候，sync=false，是不需要上传的，所以这里不能改变 sync的值
				TLayySsclFj newCertFj = yyzzFjList.get(0);
				newCertFj.setNSync(NrcConstants.SYNC_FALSE);
				newCertFj.setCPath(filePath);
			}
		} else if (NrcUtils.ZJ_FDDBR_SF_ZMS.equals(certName)) {
			if (frSfzmsFjList.size() == 0) {
				File photoFile = new File(filePath);
				TLayySsclFj newCertFj = new TLayySsclFj();
				newCertFj.setCId(UUIDHelper.getUuid());
				newCertFj.setCLayyId(dsr.getCLayyId());
				newCertFj.setCOriginName(photoFile.getName());
				newCertFj.setCPath(photoFile.getAbsolutePath());
				newCertFj.setCSsclId(cert.getCId());
				newCertFj.setNXssx(0);
				newCertFj.setNSync(NrcConstants.SYNC_FALSE);
				frSfzmsFjList.add(newCertFj);
			} else { // 替换路径 ------此时诉讼材料和诉讼材料附件为一对一，所以 下面值获取 newCertFjList.get(0)
				// 因为在和服务器同步的时候，sync=false，是不需要上传的，所以这里不能改变 sync的值
				TLayySsclFj newCertFj = frSfzmsFjList.get(0);
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
		localZjList.add(ssclMap.get(K_YYZZ));
		localZjList.add(ssclMap.get(K_FR_SFZMS));

		for (TLayySscl zj : localZjList) {
			ArrayList<TLayySsclFj> localZjFjList = NrcEditData.getCertificateFjListMap().get(zj.getCId());
			if (null == localZjFjList) {
				localZjFjList = new ArrayList<TLayySsclFj>();
				NrcEditData.getCertificateFjListMap().put(zj.getCId(), localZjFjList);
			} else {
				localZjFjList.clear();
			}
			if (NrcUtils.ZJ_YYZZ_FYJ.equals(zj.getCName())) {
				localZjFjList.addAll(yyzzFjList);
			} else if (NrcUtils.ZJ_FDDBR_SF_ZMS.equals(zj.getCName())) {
				localZjFjList.addAll(frSfzmsFjList);
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
		if (yyzzFjList.size() == 0 && frSfzmsFjList.size() == 0) {
			Toast.makeText(this, "请上传证件照片", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (yyzzFjList.size() == 0) {
			Toast.makeText(this, "请上传营业执照复印件", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (frSfzmsFjList.size() == 0) {
			Toast.makeText(this, "请上传法定代表人身份证明书", Toast.LENGTH_SHORT).show();
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
