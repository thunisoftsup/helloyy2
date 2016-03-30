package com.thunisoft.sswy.mobile.activity.nrc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.thunisoft.sswy.mobile.adapter.nrc.NrcAgentLicenseAdapter;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcSsclFjAdapter;
import com.thunisoft.sswy.mobile.datasource.NrcDsrDao;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;
import com.thunisoft.sswy.mobile.view.NoScrollGridView;
import com.thunisoft.sswy.mobile.view.NoScrollListView;

/**
 * 网上立案 其他代理人证件<br>
 * 监护人,亲友,人民团体推荐的人,所在单位推荐的人,法院许可的其他公民,法律工作者
 * 
 */
@EActivity(R.layout.activity_nrc_add_other_agent_cert)
public class NrcAddOtherAgentCertActivity extends BaseActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_aoac_title)
	protected TextView titleTV;

	/**
	 * 证件类型提示
	 */
	@ViewById(R.id.nrc_aoac_tip)
	protected TextView tipIV;

	/**
	 * 代理人证件列表
	 */
	@ViewById(R.id.nrc_aoac_cert_list)
	protected NoScrollGridView certGridView;

	/**
	 * 代为告诉人 证件提示
	 */
	@ViewById(R.id.nrc_aoac_teller_tip)
	protected TextView tellerTipIV;

	/**
	 * 代为告诉人 证件列表
	 */
	@ViewById(R.id.nrc_aoac_teller_list)
	protected NoScrollGridView tellerCertGridView;

	/**
	 * 授权委托书列表
	 */
	@ViewById(R.id.nrc_aoac_license_list)
	protected NoScrollListView licenseListView;

	/** 证件所属人列表，点击的人员 intent key */
	public static final String K_AGENT = "agent";

	/** 证件所属人列表，点击的 代理人 */
	private TLayyDlr dlr;

	/** 当前代理人的 证件 */
	private TLayySscl agentCert;

	/** 当前代为告诉人证件 */
	private TLayySscl tellerCert;

	/** 当前代理人的 附件 */
	private List<TLayySsclFj> certFjList = new ArrayList<TLayySsclFj>();

	/** 代为告诉人身份证明的 附件 */
	private List<TLayySsclFj> tellerCertFjList = new ArrayList<TLayySsclFj>();

	/** 除证件正反面，授权委托书 List */
	private List<TLayySscl> licenseList = new ArrayList<TLayySscl>();

	/** 证件id， List[证件附件] */
	private Map<String, ArrayList<TLayySsclFj>> certFjListMap = new HashMap<String, ArrayList<TLayySsclFj>>();

	/** 代理人身份证明 */
	private NrcSsclFjAdapter agentCertFjAdapter;

	/**
	 * 代为告诉人身份证明 adapter
	 */
	private NrcSsclFjAdapter tellerCertFjAdapter;

	/**
	 * 授权委托书列表 adapter
	 */
	private NrcAgentLicenseAdapter licenseAdapter;

	/**
	 * 添加代理人身份证明
	 */
	public static final int REQ_CODE_ADD_AGENT_CERT = 3;

	/**
	 * 添加代为告诉人 身份证明
	 */
	public static final int REQ_CODE_ADD_TELLER_CERT = 5;

	/**
	 * 授权委托书 position
	 */
	private int licensePos = -1;

	@Bean
	NrcDsrDao nrcDsrDao;

	@Bean
	DownloadLogic downloadLogic;

	@Bean
	DeleteLogic deleteLogic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		dlr = (TLayyDlr) intent.getSerializableExtra(K_AGENT);
	}

	@AfterViews
	protected void onAfterView() {
		downloadLogic.activity = this;
		deleteLogic.activity = this;
		titleTV.setText(dlr.getCName());
		tipIV.setText(NrcUtils.AGENT_IDENTFY_TIP);
		tellerTipIV.setText(NrcUtils.AGENT_TELLER_CERT_TIP);
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
		case REQ_CODE_ADD_AGENT_CERT:
			SerializableSOMap certSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> certMap = certSoMap.getMap();
			if (null != certMap && certMap.size() > 0) {
				for (Map.Entry<String, Object> entry : certMap.entrySet()) {
					String certFjPath = entry.getKey();
					buildDlrCertFj(agentCert, certFjPath);
					break;
				}
			}
			break;

		case REQ_CODE_ADD_TELLER_CERT:
			SerializableSOMap tellerCertSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> tellerCertMap = tellerCertSoMap.getMap();
			if (null != tellerCertMap && tellerCertMap.size() > 0) {
				for (Map.Entry<String, Object> entry : tellerCertMap.entrySet()) {
					String certFjPath = entry.getKey();
					buildDlrCertFj(tellerCert, certFjPath);
					break;
				}
			}
			break;

		case NrcAgentLicenseAdapter.REQ_CODE_ADD_LICENSE:
			SerializableSOMap licenseSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> licenseMap = licenseSoMap.getMap();
			if (null != licenseMap && licenseMap.size() > 0) {
				for (Map.Entry<String, Object> entry : licenseMap.entrySet()) {
					String reversePhotoPath = entry.getKey();
					buildDlrLicensePhoto(reversePhotoPath);
					break;
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 点击 代理人 返回
	 */
	@Click(R.id.nrc_aoac_back)
	protected void clickBack() {
		NrcAddOtherAgentCertActivity.this.finish();
	}

	/**
	 * 点击 代理人 保存
	 */
	@Click(R.id.nrc_aoac_save)
	protected void clickSave() {
		if (checkAgentCertData()) {
			saveDlrCertPhoto();
			NrcAddOtherAgentCertActivity.this.finish();
		}
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		List<TLayySscl> localCertList = NrcEditData.getCertificateListMap().get(dlr.getCId());// 获取之前存放的该人员的证件List
		Map<String, TLayySscl> localLicenseMap = new HashMap<String, TLayySscl>();
		if (null != localCertList && localCertList.size() > 0) {
			for (TLayySscl cert : localCertList) {
				if (NrcUtils.AGENT_IDENTFY_TIP.equals(cert.getCName())) {
					agentCert = cert;
				} else if (NrcUtils.AGENT_TELLER_CERT_TIP.equals(cert.getCName())) {
					tellerCert = cert;
				} else {
					localLicenseMap.put(cert.getCName(), cert);
					licenseList.add(cert);
				}
				ArrayList<TLayySsclFj> localCertFjList = NrcEditData.getCertificateFjListMap().get(cert.getCId());
				if (null != localCertFjList) { // 存在附件，需要放到当前类中对象
					ArrayList<TLayySsclFj> certFjListTemp = new ArrayList<TLayySsclFj>();
					certFjListTemp.addAll(localCertFjList);
					certFjListMap.put(cert.getCId(), certFjListTemp);
				}
			}
		}
		if (null == agentCert) {
			agentCert = initAgentCert(NrcUtils.AGENT_IDENTFY_TIP);
		}
		if (null == tellerCert) {
			tellerCert = initAgentCert(NrcUtils.AGENT_TELLER_CERT_TIP);
		}
		if (StringUtils.isNotBlank(dlr.getCBdlrId())) {
			String[] bdlrIdArray = dlr.getCBdlrId().split(NrcConstants.REL_NAME_SPLIT);
			List<TLayyDsr> dsrList = new ArrayList<TLayyDsr>();
			List<String> bdlrIdList = Arrays.asList(bdlrIdArray);
			Map<String, TLayyDsr> plaintiffMap = new HashMap<String, TLayyDsr>();
			for (TLayyDsr plaintiff : NrcEditData.getPlaintiffList()) {
				plaintiffMap.put(plaintiff.getCId(), plaintiff);
			}
			for (String bdrId : bdlrIdList) {
				dsrList.add(plaintiffMap.get(bdrId));
			}
			for (int i = 0; i < dsrList.size(); i++) {
				TLayyDsr dsr = dsrList.get(i);
				String ssclName = dsr.getCName() + NrcUtils.LICENSE_BOOK;
				if (null == localLicenseMap.get(ssclName)) {
					TLayySscl license = new TLayySscl();
					license.setCId(UUIDHelper.getUuid());
					license.setCLayyId(dlr.getCLayyId());
					license.setCName(ssclName);
					license.setCSsryId(dlr.getCId());
					license.setCSsryMc(dlr.getCName());
					license.setNType(NrcConstants.SSCL_TYPE_CERTIFICATE);
					licenseList.add(license);
				}
			}
		}
	}

	/**
	 * 刷新整个页面
	 */
	private void refreshActivity() {
		refreshAgentCertList();
		refreshTellerCertList();
		refreshLicenseFjList();
	}

	/**
	 * 刷新代理人身份证明
	 */
	private void refreshAgentCertList() {
		certFjList.clear();
		if (null != certFjListMap.get(agentCert.getCId())) {
			certFjList.addAll(certFjListMap.get(agentCert.getCId()));
		}
		if (null == agentCertFjAdapter) {
			agentCertFjAdapter = new NrcSsclFjAdapter(this, agentCert, certFjList);
			agentCertFjAdapter.setSelectType(NrcConstants.SELECT_TYPE_SINGLE);
			agentCertFjAdapter.setDownloadLogic(downloadLogic);
			agentCertFjAdapter.setDeleteLogic(deleteLogic);
			agentCertFjAdapter.setRequestCode(REQ_CODE_ADD_AGENT_CERT);
			agentCertFjAdapter.setSsclFjType(NrcConstants.SSCL_TYPE_CERTIFICATE);
			agentCertFjAdapter.setFjType(NrcSsclFjAdapter.FJ_TYPE_SINGLE);
			agentCertFjAdapter.setSsclFjListMap(certFjListMap);
			certGridView.setAdapter(agentCertFjAdapter);
		} else {
			agentCertFjAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新 代为告诉人身份证明
	 */
	private void refreshTellerCertList() {
		tellerCertFjList.clear();
		if (null != certFjListMap.get(tellerCert.getCId())) {
			tellerCertFjList.addAll(certFjListMap.get(tellerCert.getCId()));
		}
		if (null == tellerCertFjAdapter) {
			tellerCertFjAdapter = new NrcSsclFjAdapter(this, tellerCert, tellerCertFjList);
			tellerCertFjAdapter.setSelectType(NrcConstants.SELECT_TYPE_SINGLE);
			tellerCertFjAdapter.setDownloadLogic(downloadLogic);
			tellerCertFjAdapter.setDeleteLogic(deleteLogic);
			tellerCertFjAdapter.setRequestCode(REQ_CODE_ADD_TELLER_CERT);
			tellerCertFjAdapter.setSsclFjType(NrcConstants.SSCL_TYPE_CERTIFICATE);
			tellerCertFjAdapter.setFjType(NrcSsclFjAdapter.FJ_TYPE_SINGLE);
			tellerCertFjAdapter.setSsclFjListMap(certFjListMap);
			tellerCertGridView.setAdapter(tellerCertFjAdapter);
		} else {
			tellerCertFjAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新 授权委托书 List
	 */
	private void refreshLicenseFjList() {
		if (null == licenseAdapter) {
			licenseAdapter = new NrcAgentLicenseAdapter(this, licenseList, certFjListMap);
			licenseAdapter.setDownloadLogic(downloadLogic);
			licenseAdapter.setDeleteLogic(deleteLogic);
			licenseListView.setAdapter(licenseAdapter);
		} else {
			licenseAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 构造代理人证件数据到当前类的变量
	 * 
	 * @param sscl
	 * @param direction
	 */
	private void buildDlrCertFj(TLayySscl sscl, String filePath) {
		ArrayList<TLayySsclFj> newCertFjList = certFjListMap.get(sscl.getCId());
		if (null == newCertFjList || newCertFjList.size() == 0) {
			newCertFjList = new ArrayList<TLayySsclFj>();
			certFjListMap.put(sscl.getCId(), newCertFjList);
			File photoFile = new File(filePath);
			TLayySsclFj newCertFj = new TLayySsclFj();
			newCertFj.setCId(UUIDHelper.getUuid());
			newCertFj.setCLayyId(dlr.getCLayyId());
			newCertFj.setCOriginName(photoFile.getName());
			newCertFj.setCPath(photoFile.getAbsolutePath());
			newCertFj.setCSsclId(sscl.getCId());
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
	 * 构造代理人证件数据到当前类的变量
	 * 
	 * @param filePath
	 */
	private void buildDlrLicensePhoto(String filePath) {
		String licenseName = licenseList.get(licensePos).getCName();
		TLayySscl newLicense = null;
		if (licenseList.size() > 0) {
			for (int i = 0; i < licenseList.size(); i++) {
				TLayySscl license = licenseList.get(i);
				if (license.getCName().equals(licenseName)) {
					newLicense = license;
					break;
				}
			}
			if (null == newLicense) {
				newLicense = initAgentCert(licenseName);
				licenseList.add(newLicense);
			}
		} else {
			newLicense = initAgentCert(licenseName);
			licenseList.add(newLicense);
		}
		ArrayList<TLayySsclFj> newLicenseFjList = certFjListMap.get(newLicense.getCId());
		if (null == newLicenseFjList || newLicenseFjList.size() == 0) {
			newLicenseFjList = new ArrayList<TLayySsclFj>();
			certFjListMap.put(newLicense.getCId(), newLicenseFjList);
			File photoFile = new File(filePath);
			TLayySsclFj newCertFj = new TLayySsclFj();
			newCertFj.setCId(UUIDHelper.getUuid());
			newCertFj.setCLayyId(dlr.getCLayyId());
			newCertFj.setCOriginName(photoFile.getName());
			newCertFj.setCPath(photoFile.getAbsolutePath());
			newCertFj.setCSsclId(newLicense.getCId());
			newCertFj.setNXssx(0);
			newCertFj.setNSync(NrcConstants.SYNC_FALSE);
			newLicenseFjList.add(newCertFj);
		} else { // 替换路径 ------此时诉讼材料和诉讼材料附件为一对一，所以 下面值获取 newCertFjList.get(0)
			// 因为在和服务器同步的时候，sync=false，是不需要上传的，所以这里不能改变 sync的值
			TLayySsclFj newLicenseFj = newLicenseFjList.get(0);
			newLicenseFj.setCPath(filePath);
		}

	}

	/**
	 * 保存代理人证件到静态代码
	 */
	private void saveDlrCertPhoto() {
		ArrayList<TLayySscl> localCertList = NrcEditData.getCertificateListMap().get(dlr.getCId());
		if (null == localCertList) {
			localCertList = new ArrayList<TLayySscl>();
			NrcEditData.getCertificateListMap().put(dlr.getCId(), localCertList);
		} else {
			localCertList.clear();
		}
		localCertList.add(agentCert);
		localCertList.add(tellerCert);
		localCertList.addAll(licenseList);

		for (TLayySscl cert : localCertList) {
			ArrayList<TLayySsclFj> localCertFjList = NrcEditData.getCertificateFjListMap().get(cert.getCId());
			if (null == localCertFjList) {
				localCertFjList = new ArrayList<TLayySsclFj>();
				NrcEditData.getCertificateFjListMap().put(cert.getCId(), localCertFjList);
			} else {
				localCertFjList.clear();
			}
			if (null != certFjListMap.get(cert.getCId())) { //因为监护人上传授权委托书，所以此处增加空指针的判断
				localCertFjList.addAll(certFjListMap.get(cert.getCId()));
			}

			ArrayList<TLayyDsrSscl> localDlrSsclList = NrcEditData.getDsrCertificateListMap().get(dlr.getCId());
			if (null == localDlrSsclList) {
				localDlrSsclList = new ArrayList<TLayyDsrSscl>();
				NrcEditData.getDsrCertificateListMap().put(dlr.getCId(), localDlrSsclList);
			} else {
				localDlrSsclList.clear();
			}

			TLayyDsrSscl dsrSscl = new TLayyDsrSscl();
			dsrSscl.setCId(UUIDHelper.getUuid());
			dsrSscl.setCDsrId(dlr.getCId());
			dsrSscl.setCDsrName(dlr.getCName());
			dsrSscl.setCLayyId(dlr.getCLayyId());
			dsrSscl.setCSsclId(cert.getCId());
			dsrSscl.setCSsclName(cert.getCName());
			localDlrSsclList.add(dsrSscl);
		}
	}

	private boolean checkAgentCertData() {
		List<TLayySsclFj> certFjListTemp = certFjListMap.get(agentCert.getCId());
		if (null == certFjListTemp || certFjListTemp.size() == 0) {
			Toast.makeText(this, "请上传" + NrcUtils.AGENT_IDENTFY_TIP, Toast.LENGTH_SHORT).show();
			return false;
		}
		List<TLayySsclFj> tellerCertFjListTemp = certFjListMap.get(tellerCert.getCId());
		if (null == tellerCertFjListTemp || tellerCertFjListTemp.size() == 0) {
			Toast.makeText(this, "请上传" + NrcUtils.AGENT_TELLER_CERT_TIP, Toast.LENGTH_SHORT).show();
			return false;
		}

		if (Constants.AGENT_TYPE_JHR != dlr.getNDlrType()) {
			if (licenseList.size() == 0) {
				Toast.makeText(this, "请上传" + NrcUtils.LICENSE_BOOK, Toast.LENGTH_SHORT).show();
				return false;
			}
			for (TLayySscl license : licenseList) {
				List<TLayySsclFj> licenseFjListTemp = certFjListMap.get(license.getCId());
				if (null == licenseFjListTemp || licenseFjListTemp.size() == 0) {
					Toast.makeText(this, "请上传" + license.getCName(), Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 初始化代理人证件
	 * 
	 * @param certName
	 * @return
	 */
	private TLayySscl initAgentCert(String certName) {
		TLayySscl newCert = new TLayySscl();
		newCert.setCId(UUIDHelper.getUuid());
		newCert.setCLayyId(dlr.getCLayyId());
		newCert.setCSsryId(dlr.getCId());
		newCert.setCSsryMc(dlr.getCName());
		newCert.setNType(NrcConstants.SSCL_TYPE_CERTIFICATE);
		newCert.setCName(certName);
		newCert.setNXssx(0);
		return newCert;
	}

	/**
	 * 获取 授权委托书 position
	 * 
	 * @return licensePos
	 */
	public int getLicensePos() {
		return licensePos;
	}

	/**
	 * 设置 授权委托书 position
	 * 
	 * @param licensePos
	 */
	public void setLicensePos(int licensePos) {
		this.licensePos = licensePos;
	}
}
