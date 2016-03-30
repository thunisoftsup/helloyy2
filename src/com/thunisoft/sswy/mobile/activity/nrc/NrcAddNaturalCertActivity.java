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
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity_;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DeleteLogic.DelSsclFjResponseListener;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.PhoneStateUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案 当事人_自然人添加证件
 * 
 */
@EActivity(R.layout.activity_nrc_add_natural_cert)
public class NrcAddNaturalCertActivity extends BaseActivity implements DelSsclFjResponseListener {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_anc_title)
	protected TextView titleTV;

	/**
	 * 证件类型提示
	 */
	@ViewById(R.id.nrc_anc_tip)
	protected TextView tipIV;

	/**
	 * 证件正面 背面父控件
	 */
	@ViewById(R.id.nrc_anc_zm_bm)
	protected LinearLayout zmBmLinLayout;

	/** 正面 父控件 */
	@ViewById(R.id.nrc_anc_zm_fj)
	protected RelativeLayout zmFjRL;

	/** 正面扩展名背景 */
	@ViewById(R.id.nrc_anc_zm_fj_suffix_bg)
	protected FrameLayout zmFjSuffixBgFL;

	/** 正面扩展名 名称 */
	@ViewById(R.id.nrc_ac_zm_fj_suffix_name)
	protected TextView zmFjSuffixNameTV;

	/** 正面 删除按钮 */
	@ViewById(R.id.nrc_anc_zm_fj_del)
	protected Button zmFjDelBtn;

	/** 背面 父控件 */
	@ViewById(R.id.nrc_anc_bm_fj)
	protected RelativeLayout bmFjRL;

	/** 背面 扩展名背景 */
	@ViewById(R.id.nrc_anc_bm_fj_suffix_bg)
	protected FrameLayout bmFjSuffixBgFL;

	/** 背面扩展名 名称 */
	@ViewById(R.id.nrc_anc_bm_fj_suffix_name)
	protected TextView bmFjSuffixNameTV;

	/** 背面 删除按钮 */
	@ViewById(R.id.nrc_anc_bm_fj_del)
	protected Button bmFjDelBtn;

	/** 删除功能 */
	@ViewById(R.id.nrc_anc_fj_delete)
	protected RelativeLayout fjDeleteRFL;

	/** 证件所属人列表，点击的当事人 intent key */
	public static final String K_LITIGANT = "litigant";

	/** 证件所属人列表，点击的 当事人 */
	private TLayyDsr dsr;

	/** 获取静态代码中保存的 该人的所有诉讼材料 */
	private List<TLayySscl> certificateList = new ArrayList<TLayySscl>();

	/** 证件id， List[证件附件] */
	private Map<String, ArrayList<TLayySsclFj>> certFjListMap = new HashMap<String, ArrayList<TLayySsclFj>>();

	/**
	 * 添加正面照片
	 */
	private static final int REQ_CODE_ADD_ZM = 3;

	/**
	 * 添加背面照片
	 */
	private static final int REQ_CODE_ADD_BM = 4;

	@Bean
	protected DownloadLogic downloadLogic;

	@Bean
	protected DeleteLogic deleteLogic;

	/**
	 * 删除提示框
	 */
	private WaittingDialog deleteDialog;

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
		tipIV.setText(NrcUtils.getCertificateNameByCode(dsr.getNIdcardType()));
		int screenWidth = PhoneStateUtils.getScreenWidth(this);
		int height = screenWidth / 4;
		LayoutParams applicantParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		zmBmLinLayout.setLayoutParams(applicantParams);
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
		case REQ_CODE_ADD_ZM:
			SerializableSOMap zmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> zmMap = zmSoMap.getMap();
			if (null != zmMap && zmMap.size() > 0) {
				for (Map.Entry<String, Object> entry : zmMap.entrySet()) {
					String facePhotoPath = entry.getKey();
					buildDsrCertPhoto(facePhotoPath, NrcUtils.ZJ_SUFFIX_ZM);
					break;
				}
			}
			break;

		case REQ_CODE_ADD_BM:
			SerializableSOMap bmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> bmMap = bmSoMap.getMap();
			if (null != bmMap && bmMap.size() > 0) {
				for (Map.Entry<String, Object> entry : bmMap.entrySet()) {
					String reversePhotoPath = entry.getKey();
					buildDsrCertPhoto(reversePhotoPath, NrcUtils.ZJ_SUFFIX_BM);
					break;
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 点击 返回
	 */
	@Click(R.id.nrc_anc_back)
	protected void clickBack() {
		NrcAddNaturalCertActivity.this.finish();
	}

	/**
	 * 点击 保存
	 */
	@Click(R.id.nrc_anc_save)
	protected void clickSave() {
		if (checkLitigantCert()) {
			saveDsrCertPhoto();
			NrcAddNaturalCertActivity.this.finish();
		}
	}

	/**
	 * 点击证件正面
	 */
	@Click(R.id.nrc_anc_zm_fj)
	protected void clickZmFj() {
		Intent intent = new Intent();
		String certName = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
		String zmFyjName = certName + NrcUtils.ZJ_SUFFIX_ZM;
		TLayySsclFj zmFyjFj = getSsclFjByName(zmFyjName);
		if (null == zmFyjFj) { // 没上传过
			intent.setClass(this, AddPhotoDialogActivity_.class);
			intent.putExtra(AddPhotoDialogActivity.K_SELECT_TYPE, NrcConstants.SELECT_TYPE_SINGLE);
			intent.putExtra(AddPhotoDialogActivity.K_FILE_TYPE, NrcConstants.FILE_TYPE_PIC);
			startActivityForResult(intent, REQ_CODE_ADD_ZM);
		} else { // 上传过
			openFile(zmFyjFj);
		}
	}

	/**
	 * 点击证件背面照
	 */
	@Click(R.id.nrc_anc_bm_fj)
	protected void clickBmFj() {
		Intent intent = new Intent();
		String certName = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
		String bmFyjName = certName + NrcUtils.ZJ_SUFFIX_BM;
		TLayySsclFj bmFyjFj = getSsclFjByName(bmFyjName);
		if (null == bmFyjFj) { // 没上传过
			intent.setClass(this, AddPhotoDialogActivity_.class);
			intent.putExtra(AddPhotoDialogActivity.K_SELECT_TYPE, NrcConstants.SELECT_TYPE_SINGLE);
			intent.putExtra(AddPhotoDialogActivity.K_FILE_TYPE, NrcConstants.FILE_TYPE_PIC);
			startActivityForResult(intent, REQ_CODE_ADD_BM);
		} else { // 上传过
			openFile(bmFyjFj);
		}
	}

	private void openFile(TLayySsclFj ssclFj) {
		if (StringUtils.isNotBlank(ssclFj.getCPath())) {
			File organFile = new File(ssclFj.getCPath());
			if (organFile.exists()) {
				String backupPath = FileUtils.getBackupFilePath(this, organFile.getAbsolutePath());
				File backupFile = new File(backupPath);
				if (backupFile.exists()) {
					openFile(backupPath);
				} else {
					boolean success = FileUtils.copyfile(organFile, backupFile);
					if (success) {
						openFile(backupPath);
					} else {
						organFile.delete();
						Toast.makeText(this, "打开文件失败，请重试", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				downloadLogic.downloadSscl(ssclFj, NrcConstants.SSCL_TYPE_CERTIFICATE);
			}
		} else {
			downloadLogic.downloadSscl(ssclFj, NrcConstants.SSCL_TYPE_CERTIFICATE);
		}
	}

	private void openFile(String path) {
		Intent intent = ResponseUtilExtr.openFileWithAllPath(path);
		try {
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, "打开文件失败，请重试", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 点击删除按钮
	 */
	@Click(R.id.nrc_anc_fj_delete)
	protected void clickFjDelete() {
		if (View.VISIBLE == zmFjDelBtn.getVisibility() || View.VISIBLE == bmFjDelBtn.getVisibility()) {
			zmFjDelBtn.setVisibility(View.GONE);
			bmFjDelBtn.setVisibility(View.GONE);
		} else {
			String certName = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
			String zmFyjName = certName + NrcUtils.ZJ_SUFFIX_ZM;
			String bmFyjName = certName + NrcUtils.ZJ_SUFFIX_BM;
			if (null != getSsclFjByName(zmFyjName)) {
				zmFjDelBtn.setVisibility(View.VISIBLE);
			}
			if (null != getSsclFjByName(bmFyjName)) {
				bmFjDelBtn.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 删除正面附件
	 */
	@Click(R.id.nrc_anc_zm_fj_del)
	protected void clickZmFjDel() {
		if (null != certificateList && certificateList.size() > 0) {
			String certificateName = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
			String zmFyjName = certificateName + NrcUtils.ZJ_SUFFIX_ZM;
			int zmPos = -1;
			for (int i = 0; i < certificateList.size(); i++) {
				if (zmFyjName.equals(certificateList.get(i).getCName())) {
					zmPos = i;
					break;
				}
			}
			if (zmPos >= 0) {
				TLayySscl sscl = certificateList.get(zmPos);
				List<TLayySsclFj> ssclFjList = certFjListMap.get(sscl.getCId());
				if (null != ssclFjList && ssclFjList.size() > 0) {
					TLayySsclFj ssclFj = ssclFjList.get(0);
					if (null == ssclFj.getNSync() || NrcConstants.SYNC_TRUE == ssclFj.getNSync()) {
						deleteDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "正在删除...");
						deleteDialog.setIsCanclable(false);
						deleteDialog.show();
						deleteLogic.setDeleteResultListener(this);
						deleteLogic.deleteSsclFj(sscl, ssclFj);
					} else {
						certFjListMap.remove(sscl.getCId());
						certificateList.remove(zmPos);
					}
				}
			}
		}
		if (null == certificateList || certificateList.size() == 0) {
			fjDeleteRFL.setVisibility(View.INVISIBLE);
		}
		zmFjDelBtn.setVisibility(View.GONE);
		zmFjSuffixBgFL.setVisibility(View.GONE);
		zmFjRL.setBackgroundResource(R.drawable.nrc_file_item_add);
	}

	/**
	 * 删除背面附件
	 */
	@Click(R.id.nrc_anc_bm_fj_del)
	protected void clickBmFjDel() {
		if (null != certificateList && certificateList.size() > 0) {
			String certificateName = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
			String bmFyjName = certificateName + NrcUtils.ZJ_SUFFIX_BM;
			int bmPos = -1;
			for (int i = 0; i < certificateList.size(); i++) {
				if (bmFyjName.equals(certificateList.get(i).getCName())) {
					bmPos = i;
					break;
				}
			}
			if (bmPos >= 0) {
				TLayySscl sscl = certificateList.get(bmPos);
				List<TLayySsclFj> ssclFjList = certFjListMap.get(sscl.getCId());
				if (null != ssclFjList && ssclFjList.size() > 0) {
					TLayySsclFj ssclFj = ssclFjList.get(0);
					if (null == ssclFj.getNSync() || NrcConstants.SYNC_TRUE == ssclFj.getNSync()) {
						deleteDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "正在删除...");
						deleteDialog.setIsCanclable(false);
						deleteDialog.show();
						deleteLogic.setDeleteResultListener(this);
						deleteLogic.deleteSsclFj(sscl, ssclFj);
					} else {
						certFjListMap.remove(sscl.getCId());
						certificateList.remove(bmPos);
					}
				}
			}
		}
		if (null == certificateList || certificateList.size() == 0) {
			fjDeleteRFL.setVisibility(View.INVISIBLE);
		}
		bmFjDelBtn.setVisibility(View.GONE);
		bmFjSuffixBgFL.setVisibility(View.GONE);
		bmFjRL.setBackgroundResource(R.drawable.nrc_file_item_add);
	}

	@Override
	public void deleteResult(BaseResponse response, String ssclFjId) {
		if (null != deleteDialog) {
			deleteDialog.dismiss();
			deleteDialog = null;
		}
		if (response.isXtcw()) {
			if (response.isSuccess()) {
				TLayySsclFj delSsclFj = null;
				for (Map.Entry<String, ArrayList<TLayySsclFj>> entry : certFjListMap.entrySet()) {
					ArrayList<TLayySsclFj> ssclFjList = entry.getValue();
					if (null != ssclFjList && ssclFjList.size() > 0) {
						for (TLayySsclFj ssclFj : ssclFjList) {
							if (ssclFj.equals(ssclFjId)) {
								delSsclFj = ssclFj;
								break;
							}
						}
					}
				}
				if (null != delSsclFj) {
					int delSsclPos = -1;
					if (null != certificateList && certificateList.size() > 0) {
						for (int i = 0; i < certificateList.size(); i++) {
							TLayySscl sscl = certificateList.get(i);
							if (sscl.getCId().equals(delSsclFj.getCSsclId())) {
								delSsclPos = i;
							}
						}
					}
					if (delSsclPos >= 0) {
						certificateList.remove(delSsclPos);
					}
					certFjListMap.remove(delSsclFj.getCSsclId());
				}
			} else {
				Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		List<TLayySscl> localZjList = NrcEditData.getCertificateListMap().get(dsr.getCId());// 获取之前存放的该人员的证件List
		if (null != localZjList) {
			certificateList.addAll(localZjList);
		}
		if (null != certificateList && certificateList.size() > 0) {
			for (TLayySscl sscl : certificateList) {// 证件
				ArrayList<TLayySsclFj> localCertFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
				if (null != localCertFjList) { // 存在附件，需要放到当前类中对象
					ArrayList<TLayySsclFj> certFjListTemp = new ArrayList<TLayySsclFj>();
					certFjListTemp.addAll(localCertFjList);
					certFjListMap.put(sscl.getCId(), certFjListTemp);
				}
			}
		}
	}

	/**
	 * 刷新整个页面
	 */
	private void refreshActivity() {
		zmFjDelBtn.setVisibility(View.GONE);
		bmFjDelBtn.setVisibility(View.GONE);
		String certificateName = "";// 证件名称
		certificateName = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
		zmFjRL.setBackgroundResource(R.drawable.nrc_file_item_add);
		bmFjRL.setBackgroundResource(R.drawable.nrc_file_item_add);
		if (null != certificateList && certificateList.size() > 0) {
			fjDeleteRFL.setVisibility(View.VISIBLE);
			String zmFyjName = certificateName + NrcUtils.ZJ_SUFFIX_ZM;
			String bmFyjName = certificateName + NrcUtils.ZJ_SUFFIX_BM;
			for (TLayySscl sscl : certificateList) {

				if (zmFyjName.equals(sscl.getCName())) { // 证件正面照
					List<TLayySsclFj> certFaceFjList = certFjListMap.get(sscl.getCId());
					if (null != certFaceFjList && certFaceFjList.size() > 0) {
						TLayySsclFj certFj = certFaceFjList.get(0);
						refreshCertView(certFj, zmFjRL, zmFjSuffixBgFL, zmFjSuffixNameTV);
					}
				} else if (bmFyjName.equals(sscl.getCName())) { // 证件背面照
					List<TLayySsclFj> certReverseFjList = certFjListMap.get(sscl.getCId());
					if (null != certReverseFjList && certReverseFjList.size() > 0) {
						TLayySsclFj certFj = certReverseFjList.get(0);
						refreshCertView(certFj, bmFjRL, bmFjSuffixBgFL, bmFjSuffixNameTV);
					}
				}
			}
		} else {
			fjDeleteRFL.setVisibility(View.INVISIBLE);
			zmFjDelBtn.setVisibility(View.GONE);
			zmFjSuffixBgFL.setVisibility(View.GONE);
			bmFjDelBtn.setVisibility(View.GONE);
			bmFjSuffixBgFL.setVisibility(View.GONE);
		}
	}

	/** 刷新证件View */
	private void refreshCertView(TLayySsclFj certFj, RelativeLayout fjRL, FrameLayout fjSuffixBgFL, TextView fjSuffixNameTV) {
		String suffix = FileUtils.getFileSuffix(certFj.getCOriginName());
		if (null != FileUtils.getImgSuffixMap().get(suffix.toLowerCase())) {// 扩展名为符合格式的图片
			fjSuffixBgFL.setVisibility(View.GONE);
			File imgFile = new File(certFj.getCPath());
			if (imgFile.exists()) {
				fjRL.setBackgroundDrawable(FileUtils.getThumbnailDrawable(this, certFj.getCPath()));
			} else {
				fjRL.setBackgroundResource(R.drawable.file_item_bg);
				fjSuffixBgFL.setVisibility(View.VISIBLE);
				fjSuffixNameTV.setText(suffix.toUpperCase());
			}
		} else {
			fjRL.setBackgroundResource(R.drawable.file_item_bg);
			fjSuffixBgFL.setVisibility(View.VISIBLE);
			fjSuffixNameTV.setText(suffix.toUpperCase());
		}
	}

	/**
	 * 构造当事人证件数据到当前类的变量
	 * 
	 * @param filePath
	 * @param direction
	 */
	private void buildDsrCertPhoto(String filePath, String direction) {
		String idType = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
		String certName = idType + direction;
		TLayySscl newCert = null;
		if (certificateList.size() > 0) {
			for (int i = 0; i < certificateList.size(); i++) {
				TLayySscl cert = certificateList.get(i);
				if (cert.getCName().equals(certName)) {
					newCert = cert;
					break;
				}
			}
			if (null == newCert) {
				newCert = initLitigangCert(certName);
				certificateList.add(newCert);
			}
		} else {
			newCert = initLitigangCert(certName);
			certificateList.add(newCert);
		}

		ArrayList<TLayySsclFj> newCertFjList = certFjListMap.get(newCert.getCId());
		if (null == newCertFjList) {
			newCertFjList = new ArrayList<TLayySsclFj>();
			certFjListMap.put(newCert.getCId(), newCertFjList);
			File photoFile = new File(filePath);
			TLayySsclFj newCertFj = new TLayySsclFj();
			newCertFj.setCId(UUIDHelper.getUuid());
			newCertFj.setCLayyId(dsr.getCLayyId());
			newCertFj.setCOriginName(photoFile.getName());
			newCertFj.setCPath(photoFile.getAbsolutePath());
			newCertFj.setCSsclId(newCert.getCId());
			newCertFj.setNXssx(0);
			newCertFj.setNSync(NrcConstants.SYNC_FALSE);
			newCertFjList.add(newCertFj);
		} else { // 替换路径 ------此时诉讼材料和诉讼材料附件为一对一，所以 下面值获取 newCertFjList.get(0)
			// 因为在和服务器同步的时候，sync=false，是不需要上传的，所以这里不能改变 sync的值
			TLayySsclFj newCertFj = newCertFjList.get(0);
			newCertFj.setNSync(NrcConstants.SYNC_FALSE);
			newCertFj.setCPath(filePath);
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
		localZjList.addAll(certificateList);

		for (TLayySscl zj : localZjList) {
			ArrayList<TLayySsclFj> localZjFjList = NrcEditData.getCertificateFjListMap().get(zj.getCId());
			if (null == localZjFjList) {
				localZjFjList = new ArrayList<TLayySsclFj>();
				NrcEditData.getCertificateFjListMap().put(zj.getCId(), localZjFjList);
			} else {
				localZjFjList.clear();
			}
			localZjFjList.addAll(certFjListMap.get(zj.getCId()));

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

	/**
	 * 根据诉讼材料附件名称获取诉讼材料附件
	 * 
	 * @param ssclFjName
	 * @return
	 */
	private TLayySsclFj getSsclFjByName(String ssclFjName) {
		TLayySsclFj ssclFj = null;
		if (null != certificateList && certificateList.size() > 0) {
			for (TLayySscl sscl : certificateList) {
				if (sscl.getCName().equals(ssclFjName)) {
					List<TLayySsclFj> ssclFjList = certFjListMap.get(sscl.getCId());
					if (null != ssclFjList && ssclFjList.size() > 0) {
						ssclFj = ssclFjList.get(0);
					}
					break;
				}
			}
		}
		return ssclFj;
	}

	private boolean checkLitigantCert() {
		String certName = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
		String zmFyjName = certName + NrcUtils.ZJ_SUFFIX_ZM;
		String bmFyjName = certName + NrcUtils.ZJ_SUFFIX_BM;
		if (null == getSsclFjByName(zmFyjName)) {
			Toast.makeText(this, "请上传证件正面照", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (null == getSsclFjByName(bmFyjName)) {
			Toast.makeText(this, "请上传证件背面照", Toast.LENGTH_SHORT).show();
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
