package com.thunisoft.sswy.mobile.activity.nrc;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.activity.court.CourtListActivity;
import com.thunisoft.sswy.mobile.activity.court.CourtListActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcAgentAdapter;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcEvidenceAdapter;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcIndictmentAdapter;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcLitigantAdapter;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcWitnessAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.datasource.NrcBasicDao;
import com.thunisoft.sswy.mobile.datasource.NrcDlrDao;
import com.thunisoft.sswy.mobile.datasource.NrcDsrDao;
import com.thunisoft.sswy.mobile.datasource.NrcDsrSsclDao;
import com.thunisoft.sswy.mobile.datasource.NrcSfrzclDao;
import com.thunisoft.sswy.mobile.datasource.NrcShDao;
import com.thunisoft.sswy.mobile.datasource.NrcSsclDao;
import com.thunisoft.sswy.mobile.datasource.NrcSsclFjDao;
import com.thunisoft.sswy.mobile.datasource.NrcZjDao;
import com.thunisoft.sswy.mobile.datasource.NrcZjclDao;
import com.thunisoft.sswy.mobile.datasource.NrcZrDao;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.logic.NrcSubmitLogic;
import com.thunisoft.sswy.mobile.logic.net.NRCReviewResponseUtil;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.NRCReviewResponse;
import com.thunisoft.sswy.mobile.model.PicModel;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.model.TLayySsclInfo;
import com.thunisoft.sswy.mobile.model.TLayyZjInfo;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySh;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.CollectionUtils;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.PhoneStateUtils;
import com.thunisoft.sswy.mobile.util.ServiceUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcCheckUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;
import com.thunisoft.sswy.mobile.view.NoScrollListView;

/**
 * 网上立案
 * 
 */
@EActivity(R.layout.activity_net_register_case)
public class NetRegisterCaseActivity extends BaseActivity {

	/**
	 * 显示所有信息的父控件
	 */
	@ViewById(R.id.nrc_content)
	protected LinearLayout contentLinLayout;

	/** 创建时间 */
	@ViewById(R.id.nrc_create_time)
	protected TextView createTimeTV;

	/** 最后修改时间 */
	@ViewById(R.id.nrc_update_time)
	protected TextView updateTimeTV;

	/** 法院名称 */
	@ViewById(R.id.nrc_court_name)
	protected TextView courtNameTV;

	/** 案件类型 */
	@ViewById(R.id.nrc_case_type)
	protected TextView caseTypeTV;

	/** 审判程序 */
	@ViewById(R.id.nrc_judge_program)
	protected TextView judgeProgramTV;

	/** 申请人姓名 */
	@ViewById(R.id.nrc_applicant_name)
	protected TextView applicantNameTV;

	/** 证件类型 */
	@ViewById(R.id.nrc_applicant_certificate_type)
	protected TextView applicantCertificateTypeTV;

	/** 证件号码 */
	@ViewById(R.id.nrc_applicant_certificate_num)
	protected TextView applicantCertificateNumTV;

	/** 申请人手机号 */
	@ViewById(R.id.nrc_applicant_tel)
	protected TextView applicantTelTV;

	/** 审核 父控件 */
	@ViewById(R.id.nrc_check)
	protected LinearLayout checkLinLayout;

	/** 审核状态 */
	@ViewById(R.id.nrc_check_status)
	protected TextView checkStatusTV;

	/** 审核时间 */
	@ViewById(R.id.nrc_check_time)
	protected TextView checkTimeTV;

	/** 原告人列表 */
	@ViewById(R.id.nrc_plaintiff_list)
	protected NoScrollListView plaintiffListView;

	/** 原告查看更多 */
	@ViewById(R.id.nrc_plaintiff_view_more)
	protected LinearLayout plaintiffViewMoreLinLayout;

	/** 原告查看更多 提示 */
	@ViewById(R.id.nrc_plaintiff_view_more_text)
	protected TextView plaintiffViewMoreTV;

	/** 被告人列表 */
	@ViewById(R.id.nrc_defendant_list)
	protected NoScrollListView defendantListView;

	/** 被告查看更多 */
	@ViewById(R.id.nrc_defendant_view_more)
	protected LinearLayout defendantViewMoreLinLayout;

	/** 被告查看更多 提示 */
	@ViewById(R.id.nrc_defendant_view_more_text)
	protected TextView defendantViewMoreTV;

	/** 代理人列表 */
	@ViewById(R.id.nrc_agent_list)
	protected NoScrollListView agentListView;

	/** 代理人查看更多 */
	@ViewById(R.id.nrc_agent_view_more)
	protected LinearLayout agentViewMoreLinLayout;

	/** 代理人查看更多 提示 */
	@ViewById(R.id.nrc_agent_view_more_text)
	protected TextView agentViewMoreTV;

	/** 证人列表 */
	@ViewById(R.id.nrc_witness_list)
	protected NoScrollListView witnessListView;

	/** 证人查看更多 */
	@ViewById(R.id.nrc_witness_view_more)
	protected LinearLayout witnessViewMoreLinLayout;

	/** 证人查看更多 提示 */
	@ViewById(R.id.nrc_witness_view_more_text)
	protected TextView witnessViewMoreTV;

	/** 诉讼材料列表 */
	@ViewById(R.id.nrc_indictment_list)
	protected NoScrollListView indictmentListView;

	/** 诉讼材料查看更多 */
	@ViewById(R.id.nrc_indictment_view_more)
	protected LinearLayout indictmentViewMoreLinLayout;

	/** 诉讼材料查看更多 提示 */
	@ViewById(R.id.nrc_indictment_view_more_text)
	protected TextView indictmentViewMoreTV;

	/** 证件添加情况提示 */
	@ViewById(R.id.nrc_certificate_tip_text)
	protected TextView certificateTipTV;

	/** 证据列表 */
	@ViewById(R.id.nrc_evidence_list)
	protected NoScrollListView evidenceListView;

	/** 证据查看更多 */
	@ViewById(R.id.nrc_evidence_view_more)
	protected LinearLayout evidenceViewMoreLinLayout;

	/** 证据查看更多 提示 */
	@ViewById(R.id.nrc_evidence_view_more_text)
	protected TextView evidenceViewMoreTV;

	/** 其它材料列表 ------ 1.0 暂时不用 */
	@ViewById(R.id.nrc_other_material_list)
	@Deprecated
	protected ListView otherMaterialListView;

	/** 其它材料查看更多 ------ 1.0 暂时不用 */
	@ViewById(R.id.nrc_other_material_view_more)
	@Deprecated
	protected TextView otherMaterialViewMoreTV;

	/** 父控件 */
	@ViewById(R.id.nrc_pro_user_sfrz)
	LinearLayout proUserSfrzPLinLayout;

	/** 身份验证 */
	@ViewById(R.id.nrc_applicant_identify)
	protected LinearLayout proUserSfrzLinLayout;

	/** 手持证件照 */
	@ViewById(R.id.nrc_hold_idcard_img)
	protected RelativeLayout holdIdcardImgRL;

	/** 手持证件照_扩展名背景 */
	@ViewById(R.id.nrc_hold_idcard_suffix_bg)
	protected FrameLayout holdIdcardSuffixBgFL;

	/** 手持证件照_扩展名 */
	@ViewById(R.id.nrc_hold_idcard_suffix_name)
	protected TextView holdIdcardSuffixNameTV;

	/** 身份证正面照 */
	@ViewById(R.id.nrc_idcard_face_img)
	protected RelativeLayout idcardFaceImgRL;

	/** 身份证正面照_扩展名背景 */
	@ViewById(R.id.nrc_idcard_face_suffix_bg)
	protected FrameLayout idcardFaceSuffixBgFL;

	/** 身份证正面照_扩展名 */
	@ViewById(R.id.nrc_idcard_face_suffix_name)
	protected TextView idcardFaceSuffixNameTV;

	/** 身份证背面照 */
	@ViewById(R.id.nrc_idcard_back_img)
	protected RelativeLayout idcardBackImgRL;

	/** 身份证背面照_扩展名背景 */
	@ViewById(R.id.nrc_idcard_back_suffix_bg)
	protected FrameLayout idcardBackSuffixBgFL;

	/** 身份证背面照_扩展名 */
	@ViewById(R.id.nrc_idcard_back_suffix_name)
	protected TextView idcardBackSuffixNameTV;

	/** 网上立案_id主键 intent key */
	public static final String K_LAYY = "layy";

	/** 网上立案_基本信息 */
	private TLayy layy;

	private TLayySh layySh;

	/** 原告 List */
	private ArrayList<TLayyDsr> plaintiffList = new ArrayList<TLayyDsr>();

	/** 原告 列表 adapter */
	private NrcLitigantAdapter plaintiffAdapter;

	/** 被告 List */
	private ArrayList<TLayyDsr> defendantList = new ArrayList<TLayyDsr>();

	/** 被告 列表 adapter */
	private NrcLitigantAdapter defendantAdapter;

	/** 代理人 List */
	private List<TLayyDlr> agentList = new ArrayList<TLayyDlr>();

	/** 代理人 列表 adapter */
	private NrcAgentAdapter agentAdapter;

	/** 证人 List */
	private List<TLayyZr> witnessList = new ArrayList<TLayyZr>();

	/** 证人 列表 adapter */
	private NrcWitnessAdapter witnessAdapter;

	/** 起诉状 List */
	private List<TLayySscl> indictmentList = new ArrayList<TLayySscl>();

	/** 起诉状 列表 adapter */
	private NrcIndictmentAdapter indictmentAdapter;

	/** 证据 List */
	private List<TZj> evidenceList = new ArrayList<TZj>();

	/** 证据 列表 adapter */
	private NrcEvidenceAdapter evidenceAdapter;

	Map<Integer, TProUserSfrzCl> sfrzclMap = NrcEditData.getProUserSfrzclMap();

	/** 登录信息 */
	@Bean
	LoginCache loginCache;

	/** 网上立案_基本信息 */
	@Bean
	NrcBasicDao nrcBasicDao;

	/** 当事人 */
	@Bean
	NrcShDao nrcShDao;

	/** 当事人 */
	@Bean
	NrcDsrDao nrcDsrDao;

	/** 代理人 */
	@Bean
	NrcDlrDao nrcDlrDao;

	/** 证人 */
	@Bean
	NrcZrDao nrcZrDao;

	/** 诉讼材料 */
	@Bean
	NrcSsclDao nrcSsclDao;

	/** 诉讼材料_附件 */
	@Bean
	NrcSsclFjDao nrcSsclFjDao;

	/** 当事人_诉讼材料 */
	@Bean
	NrcDsrSsclDao nrcDsrSsclDao;

	/** 证据 */
	@Bean
	NrcZjDao nrcZjDao;

	/** 证据材料 */
	@Bean
	NrcZjclDao nrcZjclDao;

	/** 身份认证_材料 */
	@Bean
	NrcSfrzclDao nrcSfrzclDao;

	/** 提交 网上立案 */
	@Bean
	NrcSubmitLogic nrcSubmitLogic;

	@Bean
	NetUtils netUtils;

	@Bean
	NRCReviewResponseUtil responseUtil;

	@Bean
	DownloadLogic downloadLogic;

	private WaittingDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		contentLinLayout.setVisibility(View.INVISIBLE);
		waitDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "下载中...");
		waitDialog.setIsCanclable(false);
		waitDialog.show();
		downloadLogic.activity = this;
		layy = NrcEditData.getLayy();
		if (NrcConstants.SYNC_FALSE == layy.getNSync()) {
			loadLocalDatas();
		} else {
			loadDatas(layy.getCId());
		}
		if (LoginCache.LOGIN_TYPE_LS_VERIFID == loginCache.getLoginType()) { // 律协登录的用户不需要身份验证
			proUserSfrzPLinLayout.setVisibility(View.GONE);
		} else {
			int screenWidth = PhoneStateUtils.getScreenWidth(this);
			int height = screenWidth / 3;
			LayoutParams applicantParams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
			proUserSfrzLinLayout.setLayoutParams(applicantParams);
			proUserSfrzPLinLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!waitDialog.isShowing()) {
			refreshPlaintiffList();
			refreshDefendantList();
			refreshAgentList();
			refreshWitnessList();
			refreshIndictmentList();
			refreshCertificateCountTip();
			refreshEvidenceList();
			refreshProUserSfrz();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case NrcConstants.REQ_CODE_CANCEL:
			if (Activity.RESULT_OK == resultCode) {
				NrcEditData.clearData();
				this.finish(); // 如果点击确定，需要关闭当前页面，取消：无响应
			}
			break;

		case NrcConstants.REQ_CODE_SFRZ_SCSFZ:
			if (Constants.RESULT_OK == resultCode) {
				SerializableSOMap scsfzSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
				addProUserSfrzCl(scsfzSoMap.getMap(), NrcConstants.PRO_USER_SFRZ_SCSFZ);
			}
			break;

		case NrcConstants.REQ_CODE_SFRZ_ZM:
			if (Constants.RESULT_OK == resultCode) {
				SerializableSOMap sfzZmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
				addProUserSfrzCl(sfzZmSoMap.getMap(), NrcConstants.PRO_USER_SFRZ_ZM);
			}
			break;

		case NrcConstants.REQ_CODE_SFRZ_BM:
			if (Constants.RESULT_OK == resultCode) {
				SerializableSOMap sfzBmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
				addProUserSfrzCl(sfzBmSoMap.getMap(), NrcConstants.PRO_USER_SFRZ_BM);
			}
			break;

		case NrcConstants.REQ_CODE_SELECT_COURT:
			if (Constants.RESULT_OK == resultCode) {
				String courtname = data.getStringExtra("courtName");
				String courtId = data.getStringExtra("courtId");
				layy.setCCourtId(courtId);
				layy.setCCourtName(courtname);
				courtNameTV.setText(layy.getCCourtName());
			}

		default:
			break;
		}
	}

	/**
	 * 
	 * 返回值表示:是否能完全处理该事件<br>
	 * 在此处返回false,所以会继续传播该事件
	 **/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK: // 监听Back键按下事件
			Intent intent = new Intent(this, ConfirmDialogActivity_.class);
			intent.putExtra("message", "数据尚未保存，确定取消？");
			startActivityForResult(intent, NrcConstants.REQ_CODE_CANCEL);
			return true;

		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		NrcEditData.clearData();
		if (null != waitDialog) {
			waitDialog.dismiss();
		}
		super.onDestroy();
	}

	/**
	 * 点击 取消
	 */
	@Click(R.id.nrc_cancel)
	protected void clickCancel() {
		Intent intent = new Intent(this, ConfirmDialogActivity_.class);
		intent.putExtra("message", "数据尚未保存，确定取消？");
		startActivityForResult(intent, NrcConstants.REQ_CODE_CANCEL);
	}

	/**
	 * 点击 暂存
	 */
	@Click(R.id.nrc_save)
	protected void clickSave() {
		if (NrcCheckUtils.checkTempSaveData(this, layy)) {
			updateAjmc();
			if (StringUtils.isBlank(layy.getCCourtName())) {
				Intent intent = new Intent(this, CourtListActivity_.class);
				intent.putExtra(CourtListActivity.K_IS_NRC_SAVAE_OR_SUBMIT_COURT, true);
				startActivityForResult(intent, NrcConstants.REQ_CODE_SELECT_COURT);
			} else {
				saveNetRegisterCaseData();
				List<TLayy> layyList = new ArrayList<TLayy>();
				if (null == layy.getNStatus()) { //只有当新创建的数据保存的时候为待提交，其余状态待提交的时候不修改
					layy.setNStatus(NrcUtils.NRC_STATUS_DTJ);
				}
				layyList.add(layy);
				nrcBasicDao.updateOrSaveLayy(layyList);
				layyList = null;
				NrcEditData.clearData();
				this.finish();
			}
		}
	}

	/**
	 * 点击 提交
	 */
	@Click(R.id.nrc_submit)
	protected void clickSubmit() {
		waitDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "提交立案中...");
    	waitDialog.setIsCanclable(false);
		waitDialog.show();
		if (NrcCheckUtils.checkSubmitData(this, layy, nrcDsrDao, loginCache.getLoginType())) {
			updateAjmc();
			if (StringUtils.isBlank(layy.getCCourtName())) {
				Intent intent = new Intent(this, CourtListActivity_.class);
				intent.putExtra(CourtListActivity.K_IS_NRC_SAVAE_OR_SUBMIT_COURT, true);
				startActivityForResult(intent, NrcConstants.REQ_CODE_SELECT_COURT);
			} else {
				saveNetRegisterCaseData();
				layy.setNStatus(NrcUtils.NRC_STATUS_DSC);
				List<TLayy> layyList = new ArrayList<TLayy>();
				layyList.add(layy);
				nrcBasicDao.updateOrSaveLayy(layyList);
				layyList = null;
				NrcEditData.clearData();
				this.finish();
			}
		}
		if (null != waitDialog) {
    		waitDialog.dismiss();
    	}
		
	}
	
	/**
	 * 点击审核信息
	 */
	@Click(R.id.nrc_check)
	public void nrcCheck(){
		List<TLayySh> shList = new ArrayList<TLayySh>();
		if (null != layySh) {
			shList.add(layySh);
		}
		if(null != shList && shList.size() > 0){
			Intent intent = new Intent(this,NRCReviewCheckActivity_.class);
			intent.putExtra("shList", (Serializable)shList);
			startActivity(intent);			
		}
	}

	/**
	 * 点击 添加原告
	 */
	@Click(R.id.nrc_add_plaintiff)
	protected void clickAddPlaintiff() {
		Intent intent = new Intent();
		intent.setClass(this, NrcAddLitigantActivity_.class);
		TLayyDsr layyDsr = new TLayyDsr();
		layyDsr.setCLayyId(layy.getCId());
		layyDsr.setCSsdw(Constants.LITIGANT_SSDW_PLAINTIFF);
		intent.putExtra(NrcAddLitigantActivity.K_LITIGANT, layyDsr);
		startActivity(intent);
	}

	/**
	 * 点击 原告 查看更多
	 */
	@Click(R.id.nrc_plaintiff_view_more)
	protected void clickPlaintiffViewMore() {
		Intent intent = new Intent();
		intent.setClass(this, NrcLitigantListActivity_.class);
		intent.putExtra(NrcLitigantListActivity.K_LITIGANT_TYPE, NrcConstants.LITIGANT_TYPE_PLAINTIFF);
		startActivity(intent);
	}

	/**
	 * 点击 添加被告
	 */
	@Click(R.id.nrc_add_defendant)
	protected void clickAddDefendant() {
		Intent intent = new Intent();
		intent.setClass(this, NrcAddLitigantActivity_.class);
		TLayyDsr layyDsr = new TLayyDsr();
		layyDsr.setCLayyId(layy.getCId());
		layyDsr.setCSsdw(Constants.LITIGANT_SSDW_DEFENDANT);
		intent.putExtra(NrcAddLitigantActivity.K_LITIGANT, layyDsr);
		startActivity(intent);
	}

	/**
	 * 点击 被告 查看更多
	 */
	@Click(R.id.nrc_defendant_view_more)
	protected void clickDefendantViewMore() {
		Intent intent = new Intent();
		intent.setClass(this, NrcLitigantListActivity_.class);
		intent.putExtra(NrcLitigantListActivity.K_LITIGANT_TYPE, NrcConstants.LITIGANT_TYPE_DEFENDANT);
		startActivity(intent);
	}

	/**
	 * 点击 添加代理人
	 */
	@Click(R.id.nrc_add_agent)
	protected void clickAddAgent() {
		List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
		if (plaintiffList.size() == 0) {
			Toast.makeText(this, "请先添加原告", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, NrcAddAgentActivity_.class);
		TLayyDlr layyDlr = new TLayyDlr();
		layyDlr.setCLayyId(layy.getCId());
		intent.putExtra(NrcAddAgentActivity.K_AGENT, layyDlr);
		startActivity(intent);

	}

	/**
	 * 点击 代理人 查看更多
	 */
	@Click(R.id.nrc_agent_view_more)
	protected void clickAgentViewMore() {
		Intent intent = new Intent();
		intent.setClass(this, NrcAgentListActivity_.class);
		startActivity(intent);
	}

	/**
	 * 点击 添加证人
	 */
	@Click(R.id.nrc_add_witness)
	protected void clickAddWitness() {
		List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
		if (plaintiffList.size() == 0) {
			Toast.makeText(this, "请先添加原告", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, NrcAddWitnessActivity_.class);
		TLayyZr layyZr = new TLayyZr();
		layyZr.setCLayyId(layy.getCId());
		intent.putExtra(NrcAddWitnessActivity.K_WITNESS, layyZr);
		startActivity(intent);
	}

	/**
	 * 点击 证人 查看更多
	 */
	@Click(R.id.nrc_witness_view_more)
	protected void clickWitnessViewMore() {
		Intent intent = new Intent();
		intent.setClass(this, NrcWitnessListActivity_.class);
		startActivity(intent);
	}

	/**
	 * 点击 添加诉讼 材料
	 */
	@Click(R.id.nrc_add_indictment)
	protected void clickAddIndictment() {
		List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
		if (plaintiffList.size() == 0) {
			Toast.makeText(this, "请先添加原告", Toast.LENGTH_SHORT).show();
			return;
		}
		List<TLayyDsr> defendantList = NrcEditData.getDefendantList();
		if (defendantList.size() == 0) {
			Toast.makeText(this, "请先添加被告", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, NrcAddIndictmentActivity_.class);
		TLayySscl sscl = new TLayySscl();
		sscl.setCLayyId(layy.getCId());
		intent.putExtra(NrcAddIndictmentActivity.K_SSCL, sscl);
		startActivity(intent);
	}

	/**
	 * 点击 诉讼材料 查看更多
	 */
	@Click(R.id.nrc_indictment_view_more)
	protected void clickIndictmentViewMore() {
		Intent intent = new Intent();
		intent.setClass(this, NrcIndictmentListActivity_.class);
		startActivity(intent);
	}

	/**
	 * 点击 添加证件
	 */
	@Click(R.id.nrc_add_certificate)
	protected void clickAddCertificate() {
		List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
		if (plaintiffList.size() == 0) {
			Toast.makeText(this, "请先添加原告", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, NrcAddCertOwnerActivity_.class);
		startActivity(intent);
	}

	/**
	 * 点击 证件添加情况，查看证件所属人员列表
	 */
	@Click(R.id.nrc_certificate_tip)
	protected void clickCertificateTip() {
		List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
		if (plaintiffList.size() == 0) {
			Toast.makeText(this, "请先添加原告", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, NrcAddCertOwnerActivity_.class);
		startActivity(intent);
	}

	/**
	 * 点击 添加证据
	 */
	@Click(R.id.nrc_add_evidence)
	protected void clickAddEvidence() {
		List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
		if (plaintiffList.size() == 0) {
			Toast.makeText(this, "请先添加原告", Toast.LENGTH_SHORT).show();
			return;
		}
		TZj zj = new TZj();
		zj.setCYwBh(layy.getCId());
		zj.setCXh(NrcUtils.getZjbh(evidenceList));
		Intent intent = new Intent();
		intent.setClass(this, NrcAddEvidenceActivity_.class);
		intent.putExtra(NrcAddEvidenceActivity.K_ZJ, zj);
		startActivity(intent);
	}

	/**
	 * 点击 证据 查看更多
	 */
	@Click(R.id.nrc_evidence_view_more)
	protected void clickEvidenceViewMore() {
		Intent intent = new Intent();
		intent.setClass(this, NrcEvidenceListActivity_.class);
		startActivity(intent);
	}

	/**
	 * 点击 添加其它材料 <br>
	 * 1.0版本暂时不用
	 */
	@Click(R.id.nrc_add_other_material)
	@Deprecated
	protected void clickAddOtherMaterial() {
		Intent intent = new Intent();
		intent.setClass(this, NrcAddOtherMaterialActivity_.class);
		startActivity(intent);
	}

	/**
	 * 点击 其它材料 查看更多 <br>
	 * 1.0版本暂时不用
	 */
	@Click(R.id.nrc_other_material_view_more)
	@Deprecated
	protected void clickOtherMaterialViewMore() {

	}

	/**
	 * 点击 手持证件照
	 */
	@Click(R.id.nrc_hold_idcard_img)
	protected void clickHoldIdcardImg() {
		TProUserSfrzCl sfrzCl = getSfrzCl(NrcConstants.PRO_USER_SFRZ_SCSFZ);
		startSfrzClActivity(sfrzCl, NrcConstants.REQ_CODE_SFRZ_SCSFZ);
	}

	/**
	 * 点击 身份证正面照
	 */
	@Click(R.id.nrc_idcard_face_img)
	protected void clickIdcardFacePhoto() {
		TProUserSfrzCl sfrzCl = getSfrzCl(NrcConstants.PRO_USER_SFRZ_ZM);
		startSfrzClActivity(sfrzCl, NrcConstants.REQ_CODE_SFRZ_ZM);
	}

	/**
	 * 点击 身份证背面照
	 */
	@Click(R.id.nrc_idcard_back_img)
	protected void clickIdcardBackPhoto() {
		TProUserSfrzCl sfrzCl = getSfrzCl(NrcConstants.PRO_USER_SFRZ_BM);
		startSfrzClActivity(sfrzCl, NrcConstants.REQ_CODE_SFRZ_BM);
	}

	/**
	 * 打开身份认证的activity
	 * 
	 * @param sfrzCl
	 *            身份认证材料
	 */
	private void startSfrzClActivity(TProUserSfrzCl sfrzCl, int requestCode) {
		Intent intent = new Intent();
		if (null == sfrzCl) {
			intent.setClass(this, AddPhotoDialogActivity_.class);
			intent.putExtra(AddPhotoDialogActivity.K_SELECT_TYPE, NrcConstants.SELECT_TYPE_SINGLE);
			intent.putExtra(AddPhotoDialogActivity.K_FILE_TYPE, NrcConstants.FILE_TYPE_PIC);
			startActivityForResult(intent, requestCode);
		} else {
			String path = sfrzCl.getCClPath();
			File file = new File(path);
			if (file.exists()) {
				ArrayList<PicModel> picModelList = new ArrayList<PicModel>();
				PicModel picModel = new PicModel();
				picModel.setRelId(sfrzCl.getCBh());
				picModel.setRelPid(sfrzCl.getCBh());
				picModel.setName(sfrzCl.getCClName());
				picModel.setPath(sfrzCl.getCClPath());
				picModelList.add(picModel);
				picModel.setType(PicModel.TYPE_SFRZ_CL);
				intent.setClass(this, PicPreviewActivity_.class);
				intent.putExtra(PicPreviewActivity.IS_SHOW_PERCENT, false);
				intent.putExtra(PicPreviewActivity.K_CURR_POSITION, 0);
				intent.putExtra(PicPreviewActivity.K_PIC_IST, picModelList);
				startActivity(intent);
			} else {
				downloadLogic.downloadSmrzcl(sfrzCl, NrcConstants.OPEN_TYPE_EDIT);
			}
		}
	}

	/**
	 * 更新案件名称
	 */
	private void updateAjmc() {
		StringBuffer ajmc = new StringBuffer("");
		ajmc.append(plaintiffList.get(0).getCName());
		if (plaintiffList.size() > 1) {
			ajmc.append("等");
		}
		ajmc.append("诉").append(defendantList.get(0).getCName());
		if (defendantList.size() > 1) {
			ajmc.append("等");
		}
		ajmc.append("一案");
		layy.setCAjmc(ajmc.toString());
	}
	
	/**
	 * 添加诉讼材料到List中
	 * 
	 * @param pathList
	 */
	private void addProUserSfrzCl(Map<String, Object> selectedPathMap, int clLx) {
		if (null != selectedPathMap && selectedPathMap.size() > 0) {
			for (Map.Entry<String, Object> entry : selectedPathMap.entrySet()) {
				String path = entry.getKey();
				File file = new File(path);
				TProUserSfrzCl sfrzcl = sfrzclMap.get(clLx);
				if (null == sfrzcl) {
					sfrzcl = new TProUserSfrzCl();
					sfrzcl.setCBh(UUIDHelper.getUuid());
					sfrzcl.setCClName(file.getName());
					sfrzcl.setCClPath(path);
					sfrzcl.setCLayyId(layy.getCId());
					sfrzcl.setCProUserId(loginCache.getUserId());
					sfrzcl.setDCreate(ServiceUtils.getServerTimeStr(this));
					sfrzcl.setNSync(NrcConstants.SYNC_FALSE);
					sfrzcl.setNClLx(clLx);
					sfrzclMap.put(sfrzcl.getNClLx(), sfrzcl);
				} else {
					sfrzcl.setCClPath(path);
					sfrzcl.setCClName(file.getName());
				}
				break;
			}
		}
	}

	@Background
	public void loadDatas(String layyId) {
		loadDataDone(responseUtil.getWslayyInfo(layyId));
	}

	/**
	 * 加载本地数据
	 * 
	 * @param layyId
	 */
	@Background
	public void loadLocalDatas() {
		layySh = nrcShDao.getShListBylayyId(layy.getCId());

		List<TLayyDsr> plaintiffList = nrcDsrDao.getLayyDsrList(layy.getCId(), Constants.LITIGANT_SSDW_PLAINTIFF, NrcDsrDao.TOP_ALL);
		NrcEditData.getPlaintiffList().addAll(plaintiffList);

		List<TLayyDsr> defendantList = nrcDsrDao.getLayyDsrList(layy.getCId(), Constants.LITIGANT_SSDW_DEFENDANT, NrcDsrDao.TOP_ALL);
		NrcEditData.getDefendantList().addAll(defendantList);

		List<TLayyDlr> agentList = nrcDlrDao.getLayyDlrList(layy.getCId(), NrcDlrDao.TOP_ALL);
		NrcEditData.getAgentList().addAll(agentList);

		List<TLayyZr> witnessList = nrcZrDao.getZrListBylayyId(layy.getCId(), NrcZrDao.TOP_ALL);
		NrcEditData.getWitnessList().addAll(witnessList);

		List<TLayySscl> indictmentList = nrcSsclDao.getSsclListBylayyId(layy.getCId(), NrcConstants.SSCL_TYPE_INDICTMENT, NrcSsclDao.TOP_ALL);
		NrcEditData.getIndictmentList().addAll(indictmentList);
		if (null != indictmentList && indictmentList.size() > 0) {
			for (TLayySscl sscl : indictmentList) {
				ArrayList<TLayySsclFj> ssclFjList = (ArrayList<TLayySsclFj>) nrcSsclFjDao.getSsclFjListBySsclId(sscl.getCId(), NrcConstants.SYNC_ALL);
				if (null != ssclFjList && ssclFjList.size() > 0) {
					NrcEditData.getIndictmentFjListMap().put(sscl.getCId(), ssclFjList);
				}
				ArrayList<TLayyDsrSscl> dsrSsclList = (ArrayList<TLayyDsrSscl>) nrcDsrSsclDao.getDsrSsclListByLayySsclId(sscl.getCLayyId(), sscl.getCId());
				if (null != dsrSsclList && dsrSsclList.size() > 0) {
					NrcEditData.getDsrIndictmentListMap().put(sscl.getCId(), dsrSsclList);
				}
			}
		}

		List<TLayySscl> localCertList = nrcSsclDao.getSsclListBylayyId(layy.getCId(), NrcConstants.SSCL_TYPE_CERTIFICATE, NrcZrDao.TOP_ALL);
		if (null != localCertList && localCertList.size() > 0) {
			for (TLayySscl cert : localCertList) {
				ArrayList<TLayySscl> certificateList = NrcEditData.getCertificateListMap().get(cert.getCSsryId());
				if (null == certificateList) {
					certificateList = new ArrayList<TLayySscl>();
					NrcEditData.getCertificateListMap().put(cert.getCSsryId(), certificateList);
				}
				certificateList.add(cert);

				ArrayList<TLayySsclFj> certFjList = (ArrayList<TLayySsclFj>) nrcSsclFjDao.getSsclFjListBySsclId(cert.getCId(), NrcConstants.SYNC_ALL);
				if (null != certFjList && certFjList.size() > 0) {
					NrcEditData.getCertificateFjListMap().put(cert.getCId(), certFjList);
				}

				ArrayList<TLayyDsrSscl> dsrSsclList = (ArrayList<TLayyDsrSscl>) nrcDsrSsclDao.getDsrSsclListByLayySsclId(layy.getCId(), cert.getCId());
				if (null != dsrSsclList && dsrSsclList.size() > 0) {
					NrcEditData.getDsrCertificateListMap().put(cert.getCId(), dsrSsclList);
				}
			}
		}

		ArrayList<TZj> evidenceList = (ArrayList<TZj>) nrcZjDao.getZjListBylayyId(layy.getCId(), NrcZjDao.TOP_ALL);
		NrcEditData.getEvidenceList().addAll(evidenceList);
		if (null != evidenceList && evidenceList.size() > 0) {
			for (TZj zj : evidenceList) {
				ArrayList<TZjcl> evidenceMaterialList = (ArrayList<TZjcl>) nrcZjclDao.getZjclListByZjId(zj.getCId(), NrcZjclDao.TOP_ALL, NrcConstants.SYNC_ALL);
				if (null != evidenceMaterialList && evidenceMaterialList.size() > 0) {
					NrcEditData.getEvidenceMaterial().put(zj.getCId(), evidenceMaterialList);
				}
			}
		}

		if (View.VISIBLE == proUserSfrzLinLayout.getVisibility()) { // 非律协用户，需要显示身份验证控件，准备控件的数据
			List<TProUserSfrzCl> sfrzclList = nrcSfrzclDao.getCurrSfrzClList(loginCache.getUserId(), layy.getCId());
			if (null == sfrzclList || sfrzclList.size() == 0) {
				TLayy layy = nrcBasicDao.getCurrUserLayy(loginCache.getUserId());
				if (null != layy) {
					sfrzclList = nrcSfrzclDao.getCurrSfrzClList(loginCache.getUserId(), layy.getCId());
				}
			}
			if (null != sfrzclList && sfrzclList.size() > 0) {
				for (TProUserSfrzCl sfrzcl : sfrzclList) {
					sfrzclMap.put(sfrzcl.getNClLx(), sfrzcl);
				}
			}
		}
		refreshActivity();
	}

	public void loadDataDone(NRCReviewResponse br) {
		if (br != null && null != br.getLayy()) {
			NrcEditData.setLayy(br.getLayy());
			layy = NrcEditData.getLayy();
			List<TLayyDsr> dsrList = br.getDsrList();
			if (null != dsrList && dsrList.size() > 0) {
				for (TLayyDsr dsr : dsrList) {
					if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(dsr.getCSsdw())) {
						NrcEditData.getPlaintiffList().add(dsr);
					} else if (Constants.LITIGANT_SSDW_DEFENDANT.equals(dsr.getCSsdw())) {
						NrcEditData.getDefendantList().add(dsr);
					}
				}
			}

			if (null != br.getShList() && br.getShList().size() > 0) {
				int last = br.getShList().size() - 1;
				layySh = br.getShList().get(last);
			}

			List<TLayyDlr> dlrList = br.getDlrList();
			if (null != dlrList && dlrList.size() > 0) {
				NrcEditData.getAgentList().addAll(dlrList);
			}

			List<TLayySsclInfo> ssclInfoList = br.getSsclInfoList();
			if (null != ssclInfoList && ssclInfoList.size() > 0) {
				for (TLayySsclInfo ssclInfo : ssclInfoList) {
					TLayySscl sscl = ssclInfo.getSscl();
					ArrayList<TLayySsclFj> ssclFjList = (ArrayList<TLayySsclFj>) ssclInfo.getSsclfjList();
					ArrayList<TLayyDsrSscl> dsrSsclList = (ArrayList<TLayyDsrSscl>) ssclInfo.getSsdsrClList();
					
					if (null != dsrSsclList && dsrSsclList.size() > 0) {
						NrcEditData.getDsrIndictmentListMap().put(sscl.getCId(), dsrSsclList);
					}
					
					if (NrcConstants.SSCL_TYPE_INDICTMENT == sscl.getNType()) {
						NrcEditData.getIndictmentList().add(sscl);
						if (null != ssclFjList && ssclFjList.size() > 0) {
							NrcEditData.getIndictmentFjListMap().put(sscl.getCId(), ssclFjList);
						}

					} else if (NrcConstants.SSCL_TYPE_CERTIFICATE == sscl.getNType()) {
						Map<String, ArrayList<TLayySscl>> certificateListMap = NrcEditData.getCertificateListMap();
						TLayyDsrSscl dsrSscl = dsrSsclList.get(0);
//						ArrayList<TLayySscl> certificateList = certificateListMap.get(sscl.getCSsryId());
						ArrayList<TLayySscl> certificateList = certificateListMap.get(dsrSscl.getCDsrId());
						sscl.setCSsryId(dsrSscl.getCDsrId());
						sscl.setCSsryMc(dsrSscl.getCDsrName());
						if (null == certificateList) {
							certificateList = new ArrayList<TLayySscl>();
							certificateListMap.put(dsrSscl.getCDsrId(), certificateList);
						}
						certificateList.add(sscl);

						if (null != ssclFjList && ssclFjList.size() > 0) {
							NrcEditData.getCertificateFjListMap().put(sscl.getCId(), ssclFjList);
						}
					}
				}
			}

			List<TLayyZr> zrList = br.getZrList();
			if (null != zrList && zrList.size() > 0) {
				NrcEditData.getWitnessList().addAll(zrList);
			}

			List<TLayyZjInfo> zjInfoList = br.getZjInfoList();
			if (null != zjInfoList && zjInfoList.size() > 0) {
				for (TLayyZjInfo zjInfo : zjInfoList) {
					TZj zj = zjInfo.getTZj();
					NrcEditData.getEvidenceList().add(zj);
					ArrayList<TZjcl> zjclList = (ArrayList<TZjcl>) zjInfo.getTZjclList();
					if (null != zjclList && zjclList.size() > 0) {
						NrcEditData.getEvidenceMaterial().put(zj.getCId(), zjclList);
					}
				}
			}

			if (View.VISIBLE == proUserSfrzLinLayout.getVisibility()) {
				List<TProUserSfrzCl> sfrzClList = br.getSfrzClList();
				if (null != sfrzClList && sfrzClList.size() > 0) {
					for (TProUserSfrzCl sfrzCl : sfrzClList) {
						sfrzclMap.put(sfrzCl.getNClLx(), sfrzCl);
					}
				}
			}
			refreshActivity();
		} else {
			this.finish();
		}
	}

	/**
	 * 刷新整个页面
	 */
	@UiThread
	protected void refreshActivity() {
		if (null != waitDialog) {
			waitDialog.dismiss();
		}
		contentLinLayout.setVisibility(View.VISIBLE);
		refreshBasicInfo();
		refreshPlaintiffList();
		refreshDefendantList();
		refreshAgentList();
		refreshWitnessList();
		refreshIndictmentList();
		refreshCertificateCountTip();
		refreshEvidenceList();
		refreshProUserSfrz();
	}

	/**
	 * 刷新基本信息
	 */
	private void refreshBasicInfo() {
		String createDate = NrcUtils.getFormatDate(layy.getDCreate());
		String updateDate = createDate;
		if (StringUtils.isNotBlank(layy.getDUpdate())) {
			updateDate = NrcUtils.getFormatDate(layy.getDUpdate());
		}
		createTimeTV.setText(createDate);
		updateTimeTV.setText(updateDate);
		courtNameTV.setText(layy.getCCourtName());
		caseTypeTV.setText(layy.getCAjlx());
		judgeProgramTV.setText(layy.getCSpcx());
		applicantNameTV.setText(layy.getCProUserName());
		applicantCertificateTypeTV.setText(NrcUtils.getCertificateNameByCode(layy.getNIdcardType())+"：");
		applicantCertificateNumTV.setText(layy.getCIdcard());
		applicantTelTV.setText(layy.getCSjhm());
		if (NrcUtils.NRC_STATUS_SCBTG == layy.getNStatus() && null != layySh) {
			checkLinLayout.setVisibility(View.VISIBLE);
			checkStatusTV.setText(NrcUtils.getCheckResultByCode(layySh.getNShjg()));
			String shrq = NrcUtils.getFormatDate(layySh.getDShsj());
			checkTimeTV.setText(shrq);
		} else {
			checkLinLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 刷新原告列表
	 */
	private void refreshPlaintiffList() {
		plaintiffList.clear();
		List<TLayyDsr> localPlaintiffList = NrcEditData.getPlaintiffList();
		if (localPlaintiffList.size() > NrcDsrDao.TOP_N) {
			for (int i = 0; i < NrcDsrDao.TOP_N; i++) {
				TLayyDsr dsr = localPlaintiffList.get(i);
				plaintiffList.add(dsr);
			}
			plaintiffViewMoreTV.setText("查看全部（" + localPlaintiffList.size() + "个）");
			plaintiffViewMoreLinLayout.setVisibility(View.VISIBLE);
		} else {
			plaintiffList.addAll(localPlaintiffList);
			plaintiffViewMoreLinLayout.setVisibility(View.GONE);
		}

		if (null == plaintiffAdapter) {
			plaintiffAdapter = new NrcLitigantAdapter(this, plaintiffList);
			plaintiffListView.setAdapter(plaintiffAdapter);
		} else {
			plaintiffAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新被告列表
	 */
	private void refreshDefendantList() {
		defendantList.clear();
		List<TLayyDsr> localDefendantList = NrcEditData.getDefendantList();
		if (localDefendantList.size() > NrcDsrDao.TOP_N) {
			for (int i = 0; i < NrcDsrDao.TOP_N; i++) {
				TLayyDsr dsr = localDefendantList.get(i);
				defendantList.add(dsr);
			}
			defendantViewMoreTV.setText("查看全部（" + localDefendantList.size() + "个）");
			defendantViewMoreLinLayout.setVisibility(View.VISIBLE);
		} else {
			defendantList.addAll(localDefendantList);
			defendantViewMoreLinLayout.setVisibility(View.GONE);
		}
		if (null == defendantAdapter) {
			defendantAdapter = new NrcLitigantAdapter(this, defendantList);
			defendantListView.setAdapter(defendantAdapter);
		} else {
			defendantAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新代理人列表
	 */
	private void refreshAgentList() {
		agentList.clear();
		List<TLayyDlr> localAgentList = NrcEditData.getAgentList();
		if (localAgentList.size() > NrcDlrDao.TOP_N) {
			for (int i = 0; i < NrcDlrDao.TOP_N; i++) {
				TLayyDlr dlr = localAgentList.get(i);
				agentList.add(dlr);
			}
			agentViewMoreTV.setText("查看全部（" + localAgentList.size() + "个）");
			agentViewMoreLinLayout.setVisibility(View.VISIBLE);
		} else {
			agentList.addAll(localAgentList);
			agentViewMoreLinLayout.setVisibility(View.GONE);
		}
		if (null == agentAdapter) {
			agentAdapter = new NrcAgentAdapter(this, agentList);
			agentListView.setAdapter(agentAdapter);
		} else {
			agentAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新证人列表
	 */
	private void refreshWitnessList() {
		witnessList.clear();
		List<TLayyZr> localWitnessList = NrcEditData.getWitnessList();
		if (localWitnessList.size() > NrcDlrDao.TOP_N) {
			for (int i = 0; i < NrcDlrDao.TOP_N; i++) {
				TLayyZr zr = localWitnessList.get(i);
				witnessList.add(zr);
			}
			witnessViewMoreTV.setText("查看全部（" + localWitnessList.size() + "个）");
			witnessViewMoreLinLayout.setVisibility(View.VISIBLE);
		} else {
			witnessList.addAll(localWitnessList);
			witnessViewMoreLinLayout.setVisibility(View.GONE);
		}
		if (null == witnessAdapter) {
			witnessAdapter = new NrcWitnessAdapter(this, witnessList);
			witnessListView.setAdapter(witnessAdapter);
		} else {
			witnessAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新起诉状列表
	 */
	private void refreshIndictmentList() {
		indictmentList.clear();
		List<TLayySscl> localSsclList = NrcEditData.getIndictmentList();
		if (localSsclList.size() > NrcSsclDao.TOP_N) {
			for (int i = 0; i < NrcDlrDao.TOP_N; i++) {
				TLayySscl sscl = localSsclList.get(i);
				indictmentList.add(sscl);
			}
			indictmentViewMoreTV.setText("查看全部（" + localSsclList.size() + "个）");
			indictmentViewMoreLinLayout.setVisibility(View.VISIBLE);
		} else {
			indictmentList.addAll(localSsclList);
			indictmentViewMoreLinLayout.setVisibility(View.GONE);
		}
		if (null == indictmentAdapter) {
			indictmentAdapter = new NrcIndictmentAdapter(this, indictmentList);
			indictmentListView.setAdapter(indictmentAdapter);
		} else {
			indictmentAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新证件已添加、未添加的数量提示
	 */
	private void refreshCertificateCountTip() {
		int totalCount = 0;
		totalCount = totalCount + NrcEditData.getPlaintiffList().size();
		totalCount = totalCount + NrcEditData.getAgentList().size();
		
		List<TLayyDsr> dsrList = new ArrayList<TLayyDsr>();
		for (TLayyDlr layyDlr : NrcEditData.getAgentList()) {
			if(layyDlr.getCBdlrId()!=null){
				String[] bdlrIdArray = layyDlr.getCBdlrId().split(NrcConstants.REL_NAME_SPLIT);
				List<String> bdlrIdList = Arrays.asList(bdlrIdArray);
				Map<String, TLayyDsr> plaintiffMap = new HashMap<String, TLayyDsr>();
				for (TLayyDsr plaintiff : NrcEditData.getPlaintiffList()) {
					plaintiffMap.put(plaintiff.getCId(), plaintiff);
				}
				for (String bdrId : bdlrIdList) {
					if (null != plaintiffMap.get(bdrId)) {
						dsrList.add(plaintiffMap.get(bdrId));
					}
				}
			}	
		}
		Map<String, ArrayList<TLayySscl>> ssclByDsrIdMap = NrcEditData.getCertificateListMap();
		Map<String, String> dsrIdMap = new HashMap<String, String>(); //用人员id作为key来计算当前上传证件的个数，防止一个人上传多个证件，计算多了
		Map<String, String> certNameMap = NrcUtils.getCertNameMap(dsrList);
		for (Map.Entry<String, ArrayList<TLayySscl>> ssclByDsrIdMapEntry : ssclByDsrIdMap.entrySet()) {
			ArrayList<TLayySscl> ssclList = ssclByDsrIdMapEntry.getValue();
			if (CollectionUtils.isNotEmpty(ssclList)) {
				for (TLayySscl sscl : ssclList) {
					if (null != certNameMap.get(sscl.getCName())) {
						dsrIdMap.put(ssclByDsrIdMapEntry.getKey(), ssclByDsrIdMapEntry.getKey());
					}
				}
			}
		}

		int unUploadCount = totalCount - dsrIdMap.size();
		certificateTipTV.setText("已添加" + dsrIdMap.size() + "，" + unUploadCount + "人未添加");
	}

	/**
	 * 刷新证据列表
	 */
	private void refreshEvidenceList() {
		evidenceList.clear();
		List<TZj> localEvidenceList = NrcEditData.getEvidenceList();
		if (localEvidenceList.size() > NrcDlrDao.TOP_N) {
			for (int i = 0; i < NrcZjDao.TOP_N; i++) {
				TZj zj = localEvidenceList.get(i);
				evidenceList.add(zj);
			}
			evidenceViewMoreTV.setText("查看全部（" + localEvidenceList.size() + "个）");
			evidenceViewMoreLinLayout.setVisibility(View.VISIBLE);
		} else {
			evidenceList.addAll(localEvidenceList);
			evidenceViewMoreLinLayout.setVisibility(View.GONE);
		}
		if (null == evidenceAdapter) {
			evidenceAdapter = new NrcEvidenceAdapter(this, evidenceList);
			evidenceListView.setAdapter(evidenceAdapter);
		} else {
			evidenceAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新申请人身份认证信息
	 */
	private void refreshProUserSfrz() {
		if (View.GONE == proUserSfrzLinLayout.getVisibility()) {
			return;
		}
		holdIdcardImgRL.setBackgroundResource(R.drawable.nrc_file_item_add);
		holdIdcardSuffixBgFL.setVisibility(View.GONE);
		idcardFaceImgRL.setBackgroundResource(R.drawable.nrc_file_item_add);
		idcardFaceSuffixBgFL.setVisibility(View.GONE);
		idcardBackImgRL.setBackgroundResource(R.drawable.nrc_file_item_add);
		idcardBackSuffixBgFL.setVisibility(View.GONE);
		if (null != sfrzclMap && sfrzclMap.size() > 0) {
			for (Map.Entry<Integer, TProUserSfrzCl> entry : sfrzclMap.entrySet()) {
				int clLx = entry.getKey();
				String path = entry.getValue().getCClPath();
				String fileName = entry.getValue().getCClName();
				if (StringUtils.isNotBlank(path)) {
					if (NrcConstants.PRO_USER_SFRZ_SCSFZ == clLx) {
						File scsfzFile = new File(path);
						if (scsfzFile.exists()) {
							holdIdcardImgRL.setBackgroundDrawable(FileUtils.getThumbnailDrawable(this, path));
						} else {
							holdIdcardSuffixBgFL.setVisibility(View.VISIBLE);
							holdIdcardSuffixNameTV.setText(FileUtils.getFileSuffix(fileName));
							holdIdcardImgRL.setBackgroundResource(R.drawable.file_item_bg);
						}
					} else if (NrcConstants.PRO_USER_SFRZ_ZM == clLx) {
						File zmFile = new File(path);
						if (zmFile.exists()) {
							idcardFaceImgRL.setBackgroundDrawable(FileUtils.getThumbnailDrawable(this, path));
						} else {
							idcardFaceSuffixBgFL.setVisibility(View.VISIBLE);
							idcardFaceSuffixNameTV.setText(FileUtils.getFileSuffix(fileName));
							idcardFaceImgRL.setBackgroundResource(R.drawable.file_item_bg);
						}
					} else if (NrcConstants.PRO_USER_SFRZ_BM == clLx) {
						File bmFile = new File(path);
						if (bmFile.exists()) {
							idcardBackImgRL.setBackgroundDrawable(FileUtils.getThumbnailDrawable(this, path));
						} else {
							idcardBackSuffixBgFL.setVisibility(View.VISIBLE);
							idcardBackSuffixNameTV.setText(FileUtils.getFileSuffix(fileName));
							idcardBackImgRL.setBackgroundResource(R.drawable.file_item_bg);
						}
					}
				} else {
					if (NrcConstants.PRO_USER_SFRZ_SCSFZ == clLx) {
						holdIdcardSuffixBgFL.setVisibility(View.VISIBLE);
						holdIdcardSuffixNameTV.setText(FileUtils.getFileSuffix(fileName));
						idcardBackImgRL.setBackgroundResource(R.drawable.file_item_bg);
					} else if (NrcConstants.PRO_USER_SFRZ_ZM == clLx) {
						idcardFaceSuffixBgFL.setVisibility(View.VISIBLE);
						idcardFaceSuffixNameTV.setText(FileUtils.getFileSuffix(fileName));
						idcardFaceImgRL.setBackgroundResource(R.drawable.file_item_bg);
					} else if (NrcConstants.PRO_USER_SFRZ_BM == clLx) {
						idcardBackSuffixBgFL.setVisibility(View.VISIBLE);
						idcardBackSuffixNameTV.setText(FileUtils.getFileSuffix(fileName));
						idcardBackImgRL.setBackgroundResource(R.drawable.file_item_bg);
					}
				}
			}
		}
	}

	/**
	 * 根据材料类型，判断时候已经有了证据材料，<br>
	 * （手持身份证、身份证正面、身份证背面）
	 * 
	 * @param clLx
	 * @return
	 */
	private TProUserSfrzCl getSfrzCl(int clLx) {
		if (null != sfrzclMap && sfrzclMap.size() > 0) {
			return sfrzclMap.get(clLx);
		}
		return null;
	}

	/**
	 * 保存网上立案数据
	 */
	private void saveNetRegisterCaseData() {
		StringBuffer dsrsSb = new StringBuffer("");

		List<TLayyDsr> dsrList = new ArrayList<TLayyDsr>(); // 保存当事人
		dsrList.addAll(NrcEditData.getPlaintiffList());
		dsrList.addAll(NrcEditData.getDefendantList());
		for (TLayyDsr dsr : dsrList) {
			dsrsSb.append(dsr.getCSsdw()).append(":").append(dsr.getCName()).append("、");
		}
		nrcDsrDao.deleteDsrByLayyId(layy.getCId());
		if (null != dsrList && dsrList.size() > 0) {
			nrcDsrDao.saveLayyDsr(dsrList);
		}
		dsrList = null;

		nrcDlrDao.deleteDlrByLayyId(layy.getCId());
		List<TLayyDlr> agentList = NrcEditData.getAgentList(); // 保存代理人
		if (null != agentList && agentList.size() > 0) {
			nrcDlrDao.saveLayyDlr(agentList);
		}

		nrcZrDao.deleteZrByLayyId(layy.getCId());
		List<TLayyZr> witnessList = NrcEditData.getWitnessList();
		if (null != witnessList && witnessList.size() > 0) {
			nrcZrDao.saveLayyZr(witnessList); // 保存证人
		}
		saveIndictment();
		saveCertificate(); // 保存证件
		saveEvidence(); // 保存证据
		saveSfrzcl(); // 保存身份认证材料
		String dsrs = "";
		if (StringUtils.isNotBlank(dsrsSb.toString())) {
			String temp = dsrsSb.toString();
			dsrs = temp.substring(0, temp.length() - 1);
		}
		List<TLayySh> shList = new ArrayList<TLayySh>();
		if (null != layySh) {
			shList.add(layySh);
		}
		nrcShDao.updateOrSaveSh(shList);
		// 保存 网上立案
		layy.setCDsr(dsrs);
		layy.setNSync(NrcConstants.SYNC_FALSE);
		layy.setDUpdate(ServiceUtils.getServerTimeStr(this)); // 在暂存和提交按钮处保存数据库

	}

	/**
	 * 保存 起诉状
	 */
	private void saveIndictment() {
		// 获取当前网上立案下的所有 起诉状 数据，用于后面删除起诉状附件
		List<String> localSsclIdList = nrcSsclDao.getSsclIdList(layy.getCId(), NrcConstants.SSCL_TYPE_INDICTMENT, NrcSsclDao.TOP_ALL);
		nrcSsclDao.deleteSscl(localSsclIdList);
		List<TLayySscl> indictmentList = NrcEditData.getIndictmentList();
		if (null != indictmentList && indictmentList.size() > 0) {
			nrcSsclDao.saveSscl(indictmentList);
		}
		Map<String, ArrayList<TLayySsclFj>> indictmentListMap = NrcEditData.getIndictmentFjListMap(); // 保存起诉状_附件
		List<TLayySsclFj> allIndictmentFjList = new ArrayList<TLayySsclFj>();
		for (Map.Entry<String, ArrayList<TLayySsclFj>> entry : indictmentListMap.entrySet()) {
			ArrayList<TLayySsclFj> indictmentFjList = entry.getValue();
			if (null != indictmentFjList && indictmentFjList.size() > 0) {
				for (TLayySsclFj ssclFj : indictmentFjList) {
					allIndictmentFjList.add(ssclFj);
				}
			}
		}

		nrcSsclFjDao.deleteSsclFjBySsclId(localSsclIdList);
		if (null != allIndictmentFjList && allIndictmentFjList.size() > 0) {
			nrcSsclFjDao.saveSsclFj(allIndictmentFjList);
		}
		allIndictmentFjList = null;

		Map<String, ArrayList<TLayyDsrSscl>> dsrIndictmentListMap = NrcEditData.getDsrIndictmentListMap(); // 保存起诉状关联的人员
		List<TLayyDsrSscl> allDsrIndictmentList = new ArrayList<TLayyDsrSscl>();
		for (Map.Entry<String, ArrayList<TLayyDsrSscl>> entry : dsrIndictmentListMap.entrySet()) {
			ArrayList<TLayyDsrSscl> dsrIndictmentList = entry.getValue();
			if (null != dsrIndictmentList && dsrIndictmentList.size() > 0) {
				for (TLayyDsrSscl dsrSscl : dsrIndictmentList) {
					allDsrIndictmentList.add(dsrSscl);
				}
			}
		}
		nrcDsrSsclDao.deleteDsrSsclBySsclIdList(localSsclIdList);
		if (null != allDsrIndictmentList && allDsrIndictmentList.size() > 0) {
			nrcDsrSsclDao.saveDsrSscl(allDsrIndictmentList);
		}
		allDsrIndictmentList = null;
	}

	/**
	 * 保存证件
	 */
	private void saveCertificate() {
		Map<String, ArrayList<TLayySscl>> certificateListMap = NrcEditData.getCertificateListMap(); // 保存证件
		List<TLayySscl> allCertificateList = new ArrayList<TLayySscl>();
		for (Map.Entry<String, ArrayList<TLayySscl>> entry : certificateListMap.entrySet()) {
			ArrayList<TLayySscl> certificateList = entry.getValue();
			if (null != certificateList && certificateList.size() > 0) {
				for (TLayySscl sscl : certificateList) {
					allCertificateList.add(sscl);
				}
			}
		}
		// 获取当前网上立案下的所有证件数据，用于后面删除证件附件
		List<String> localCertList = nrcSsclDao.getSsclIdList(layy.getCId(), NrcConstants.SSCL_TYPE_CERTIFICATE, NrcSsclDao.TOP_ALL);
		nrcSsclDao.deleteSsclByLayyId(layy.getCId(), NrcConstants.SSCL_TYPE_CERTIFICATE);
		if (null != allCertificateList && allCertificateList.size() > 0) {
			nrcSsclDao.saveSscl(allCertificateList);
		}
		allCertificateList = null;

		Map<String, ArrayList<TLayySsclFj>> certificateFjListMap = NrcEditData.getCertificateFjListMap(); // 保存证件_附件
		List<TLayySsclFj> allCertificateFjList = new ArrayList<TLayySsclFj>();
		for (Map.Entry<String, ArrayList<TLayySsclFj>> entry : certificateFjListMap.entrySet()) {
			ArrayList<TLayySsclFj> certificateFjList = entry.getValue();
			if (null != certificateFjList && certificateFjList.size() > 0) {
				for (TLayySsclFj ssclFj : certificateFjList) {
					allCertificateFjList.add(ssclFj);
				}
			}
		}
		nrcSsclFjDao.deleteSsclFjBySsclId(localCertList);
		if (null != allCertificateFjList && allCertificateFjList.size() > 0) {
			nrcSsclFjDao.saveSsclFj(allCertificateFjList);
		}
		allCertificateFjList = null;

		Map<String, ArrayList<TLayyDsrSscl>> dsrCertificateListMap = NrcEditData.getDsrCertificateListMap(); // 保存证件关联的人员
		List<TLayyDsrSscl> allDsrCertificateList = new ArrayList<TLayyDsrSscl>();
		for (Map.Entry<String, ArrayList<TLayyDsrSscl>> entry : dsrCertificateListMap.entrySet()) {
			ArrayList<TLayyDsrSscl> dsrCertificateList = entry.getValue();
			if (null != dsrCertificateList && dsrCertificateList.size() > 0) {
				for (TLayyDsrSscl dsrSscl : dsrCertificateList) {
					allDsrCertificateList.add(dsrSscl);
				}
			}
		}
		nrcDsrSsclDao.deleteDsrSsclBySsclIdList(localCertList);
		if (null != allDsrCertificateList && allDsrCertificateList.size() > 0) {
			nrcDsrSsclDao.saveDsrSscl(allDsrCertificateList);
		}
		allDsrCertificateList = null;
	}

	/**
	 * 保存证据
	 */
	private void saveEvidence() {
		// 获取当前网上立案下的所有 证据 数据，用于后面删除证件附件
		List<String> localZjIdList = nrcZjDao.getZjIdList(layy.getCId());
		nrcZjDao.deleteZjByLayyId(layy.getCId());
		List<TZj> zjList = NrcEditData.getEvidenceList();
		if (null != zjList && zjList.size() > 0) {
			nrcZjDao.saveZj(zjList); // 保存证据
		}
		Map<String, ArrayList<TZjcl>> zjclListMap = NrcEditData.getEvidenceMaterial(); // 保存证据材料
		List<TZjcl> allZjclList = new ArrayList<TZjcl>();
		for (Map.Entry<String, ArrayList<TZjcl>> entry : zjclListMap.entrySet()) {
			ArrayList<TZjcl> zjclList = entry.getValue();
			if (null != zjclList && zjclList.size() > 0) {
				for (TZjcl zjcl : zjclList) {
					allZjclList.add(zjcl);
				}
			}
		}
		nrcZjclDao.deleteZjclByZjId(localZjIdList);
		if (null != allZjclList && allZjclList.size() > 0) {
			nrcZjclDao.saveZjcl(allZjclList);
		}
		allZjclList = null;
	}

	/**
	 * 保存身份认证材料
	 */
	private void saveSfrzcl() {
		if (null != sfrzclMap && sfrzclMap.size() > 0) {
			List<TProUserSfrzCl> sfrzClList = new ArrayList<TProUserSfrzCl>();
			for (Map.Entry<Integer, TProUserSfrzCl> entry : sfrzclMap.entrySet()) {
				sfrzClList.add(entry.getValue());
			}
			nrcSfrzclDao.updateOrSaveSfrzcl(sfrzClList);
		}
	}
}
