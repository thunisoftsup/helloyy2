package com.thunisoft.sswy.mobile.activity.nrc;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.adapter.NRCReviewAgentAdapter;
import com.thunisoft.sswy.mobile.adapter.NRCReviewWitnessAdapter;
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
import com.thunisoft.sswy.mobile.logic.net.NRCReviewResponseUtil;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.NRCReviewResponse;
import com.thunisoft.sswy.mobile.model.PicModel;
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
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;
import com.thunisoft.sswy.mobile.view.NoScrollListView;

/**
 * 网上立案查看界面
 * 
 */

@EActivity(R.layout.activity_net_register_case_review)
public class NetRegisterCaseReviewActivity extends BaseActivity {

	/** loading */
	@ViewById(R.id.nrc_detail_loading)
	View nrcLoading;

	/** 详情内容 */
	@ViewById(R.id.nrc_detail_content)
	View nrcLoadone;

	/** 审核信息父控件 **/
	@ViewById(R.id.nrc_review_check)
	protected LinearLayout revivewCheckLL;
	
	/**审核信息图标**/
	@ViewById(R.id.nrc_review_check_icon)
	protected TextView revivewCheckIcon;

	/** 审核信息 */
	@ViewById(R.id.nrc_review_check_tip)
	protected TextView checkTipTV;

	/** 审核结果 */
	@ViewById(R.id.nrc_review_check_result)
	protected TextView checkResultTV;

	/** 审核时间 */
	@ViewById(R.id.nrc_review_check_time)
	protected TextView checkTimeTV;

	/** 创建时间 */
	@ViewById(R.id.nrc_review_base_create_time)
	protected TextView baseCreateTimeTV;

	/** 更新时间 */
	@ViewById(R.id.nrc_review_base_update_time)
	protected TextView baseUpdateTimeTV;

	/** 申请人姓名 */
	@ViewById(R.id.nrc_review_base_user_name)
	protected TextView baseUserNameTV;

	/** 证件号码 */
	@ViewById(R.id.nrc_review_base_user_card)
	protected TextView baseUserCardTV;

	/** 申请人手机号 */
	@ViewById(R.id.nrc_review_base_user_phone)
	protected TextView baseUserPhoneTV;

	/** 法院信息 */
	@ViewById(R.id.nrc_review_base_court)
	protected LinearLayout baseCourtTV;

	/** 法院名称 */
	@ViewById(R.id.nrc_review_base_court_name)
	protected TextView baseCourtNameTV;

	/** 案件类型 */
	@ViewById(R.id.nrc_review_base_case_type)
	protected TextView baseCaseTypeTV;

	/** 审判程序 */
	@ViewById(R.id.nrc_review_base_case_level)
	protected TextView baseCaseLevelTV;

	/** 原告姓名 */
	@ViewById(R.id.nrc_review_plaintiff_name)
	protected TextView plaintiffNameTV;

	/** 原告数量 */
	@ViewById(R.id.nrc_review_plaintiff_count)
	protected TextView plaintiffCountTV;

	/** 被告信息 */
	@ViewById(R.id.nrc_review_defendant)
	protected LinearLayout defendantTV;

	/** 被告姓名 */
	@ViewById(R.id.nrc_review_defendant_name)
	protected TextView defendantNameTV;

	/** 被告数量 */
	@ViewById(R.id.nrc_review_defendant_count)
	protected TextView defendantCountTV;

	/** 代理人列表 */
	@ViewById(R.id.agent_list)
	protected NoScrollListView agentListView;

	@ViewById(R.id.witness_info)
	protected LinearLayout witnessInfo;

	/** 证人列表 */
	@ViewById(R.id.witness_list)
	protected NoScrollListView witnessListView;

	// /**证人姓名*/
	// @ViewById(R.id.nrc_review_witness_name)
	// protected TextView witnessNameTV;
	//
	// /**证人有利方*/
	// @ViewById(R.id.nrc_review_witness_direction)
	// protected TextView witnessDirectionTV;
	//
	/** 起诉状 */
	@ViewById(R.id.nrc_review_indictment)
	protected LinearLayout indictmentInfo;

	/** 起诉状数量 */
	@ViewById(R.id.nrc_review_indictment_count)
	protected TextView indictmentCountTV;

	/** 原告证件 */
	@ViewById(R.id.nrc_review_plaintiff_card)
	protected LinearLayout plaintiffCardInfo;

	/** 原告证件数量 */
	@ViewById(R.id.nrc_review_plaintiff_card_count)
	protected TextView plaintiffCardCountTV;
	//
	// /**被告证件*/
	// @ViewById(R.id.nrc_review_defendant_card)
	// protected LinearLayout defendantCardTV;

	/** 代理人证件 */
	@ViewById(R.id.nrc_review_agent_card)
	protected LinearLayout agentCardInfo;

	/** 代理人证件数量 */
	@ViewById(R.id.nrc_review_agent_card_count)
	protected TextView agentCardCountTV;

	/** 证据 */
	@ViewById(R.id.nrc_review_evidence)
	protected LinearLayout evidenceInfo;

	/** 证据数量 */
	@ViewById(R.id.nrc_review_evidence_count)
	protected TextView evidenceCountTV;

	/** 父控件 */
	@ViewById(R.id.nrcr_pro_user_sfrz)
	LinearLayout proUserSfrzPLinLayout;

	/** 身份验证 */
	@ViewById(R.id.nrcr_applicant_identify)
	protected LinearLayout proUserSfrzLinLayout;

	/** 手持证件照 */
	@ViewById(R.id.nrcr_hold_idcard_img)
	protected RelativeLayout holdIdcardImgRL;

	/** 手持证件照_扩展名背景 */
	@ViewById(R.id.nrcr_hold_idcard_suffix_bg)
	protected FrameLayout holdIdcardSuffixBgFL;

	/** 手持证件照_扩展名 */
	@ViewById(R.id.nrcr_hold_idcard_suffix_name)
	protected TextView holdIdcardSuffixNameTV;

	/** 身份证正面照 */
	@ViewById(R.id.nrcr_idcard_face_img)
	protected RelativeLayout idcardFaceImgRL;

	/** 身份证正面照_扩展名背景 */
	@ViewById(R.id.nrcr_idcard_face_suffix_bg)
	protected FrameLayout idcardFaceSuffixBgFL;

	/** 身份证正面照_扩展名 */
	@ViewById(R.id.nrcr_idcard_face_suffix_name)
	protected TextView idcardFaceSuffixNameTV;

	/** 身份证背面照 */
	@ViewById(R.id.nrcr_idcard_back_img)
	protected RelativeLayout idcardBackImgRL;

	/** 身份证背面照_扩展名背景 */
	@ViewById(R.id.nrcr_idcard_back_suffix_bg)
	protected FrameLayout idcardBackSuffixBgFL;

	/** 身份证背面照_扩展名 */
	@ViewById(R.id.nrcr_idcard_back_suffix_name)
	protected TextView idcardBackSuffixNameTV;

	public static final String K_LAYY = "layy";

	private TLayy layy;
	private List<TLayySh> shList;
	private List<TLayyDsr> dsrList;
	private List<TLayyDlr> dlrList;
	private List<TLayySsclInfo> ssclInfoList;
	private List<TLayyZjInfo> zjInfoList;
	private List<TLayyZr> zrList;

	private List<TLayySsclInfo> indictmentList = new ArrayList<TLayySsclInfo>();
	private List<TLayySsclInfo> certificatesList = new ArrayList<TLayySsclInfo>();
	private List<TLayySsclFj> plaintiffCardList = new ArrayList<TLayySsclFj>();
	private List<TLayySsclFj> agentCardList = new ArrayList<TLayySsclFj>();
	private Map<Integer, TProUserSfrzCl> sfrzclMap = NrcEditData
			.getProUserSfrzclMap();
	// private Map<String,List<String>> ssdwMap = new HashMap<String,
	// List<String>>();
	// private List<String> plaintiffIdList = new ArrayList<String>();
	// private List<String> defendantIdList = new ArrayList<String>();
	// private List<String> agentIdList = new ArrayList<String>();

	public static Map<String, String> plaintiffIdMap = new HashMap<String, String>();

	private NRCReviewAgentAdapter nrcReviewAgentAdapter;
	private NRCReviewWitnessAdapter nrcReviewWitnessAdapter;

	private String plaintiffName = "";
	private String defendantName = "";
	private int plaintiffCount = 0;
	private int defendantCount = 0;
	private int indictmentCount = 0;
	private int plaintiffCardCount = 0;
	private int agentCardCount = 0;

	/**
	 * 登录信息
	 */
	@Bean
	LoginCache loginCache;

	@Bean
	NetUtils netUtils;

	@Bean
	NRCReviewResponseUtil responseUtil;

	/** 网上立案_基本信息 */
	@Bean
	NrcBasicDao nrcBasicDao;

	/** 网上立案_审核 */
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

	@Bean
	DownloadLogic downloadLogic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		layy = (TLayy) intent.getSerializableExtra(K_LAYY);
	}

	@AfterViews
	protected void onAfterView() {
		setTitle("网上立案详情");
		downloadLogic.activity = this;
		nrcReviewAgentAdapter = new NRCReviewAgentAdapter(this);
		nrcReviewWitnessAdapter = new NRCReviewWitnessAdapter(this);
		agentListView.setAdapter(nrcReviewAgentAdapter);
		witnessListView.setAdapter(nrcReviewWitnessAdapter);
		if (NrcConstants.SYNC_FALSE == layy.getNSync()) {
			refreshLitigantType(dsrList);
			refreshCertificatesList();
			refreshIndictmentList();
			showBaseInfo();
		} else {
			loadDatas();
		}
		if (LoginCache.LOGIN_TYPE_LS_VERIFID == loginCache.getLoginType()) { // 律协登录的用户不需要身份验证
			proUserSfrzPLinLayout.setVisibility(View.GONE);
		} else {
			int screenWidth = PhoneStateUtils.getScreenWidth(this);
			int height = screenWidth / 3;
			LayoutParams applicantParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, height);
			proUserSfrzLinLayout.setLayoutParams(applicantParams);
			proUserSfrzPLinLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (View.VISIBLE == nrcLoadone.getVisibility()) {
			refreshProUserSfrz();
		}
	}

	@Background
	public void loadDatas() {
		loadDataDone(responseUtil.getWslayyInfo(layy.getCId()));
	}

	@UiThread
	public void loadDataDone(NRCReviewResponse br) {
		if (br == null || null == br.getLayy()) {
			Toast.makeText(this, br.getMessage(), Toast.LENGTH_LONG).show();
			this.finish();
		} else {
			nrcLoading.setVisibility(View.GONE);
			nrcLoadone.setVisibility(View.VISIBLE);
			layy = br.getLayy();
			shList = br.getShList() == null ? new ArrayList<TLayySh>() : br
					.getShList();
			dsrList = br.getDsrList() == null ? new ArrayList<TLayyDsr>() : br
					.getDsrList();
			dlrList = br.getDlrList() == null ? new ArrayList<TLayyDlr>() : br
					.getDlrList();
			ssclInfoList = br.getSsclInfoList() == null ? new ArrayList<TLayySsclInfo>()
					: br.getSsclInfoList();
			zjInfoList = br.getZjInfoList() == null ? new ArrayList<TLayyZjInfo>()
					: br.getZjInfoList();
			zrList = br.getZrList() == null ? new ArrayList<TLayyZr>() : br
					.getZrList();
			refreshLitigantType(dsrList);
			refreshCertificatesList();
			refreshIndictmentList();
			if (View.VISIBLE == proUserSfrzLinLayout.getVisibility()) {
				List<TProUserSfrzCl> sfrzClList = br.getSfrzClList();
				if (null != sfrzClList && sfrzClList.size() > 0) {
					for (TProUserSfrzCl sfrzCl : sfrzClList) {
						sfrzclMap.put(sfrzCl.getNClLx(), sfrzCl);
					}
				}
			}
			showBaseInfo();
			// buildMapBySsdw();

			if (View.VISIBLE == proUserSfrzLinLayout.getVisibility()) {
				List<TProUserSfrzCl> sfrzClList = br.getSfrzClList();
				if (null != sfrzClList && sfrzClList.size() > 0) {
					for (TProUserSfrzCl sfrzCl : sfrzClList) {
						sfrzclMap.put(sfrzCl.getNClLx(), sfrzCl);
					}
				}
			}
		}
	}

	/**
	 * 加载本地数据
	 * 
	 * @param layyId
	 */
	@Background
	public void loadLocalDatas() {
		TLayySh layySh = nrcShDao.getShListBylayyId(layy.getCId());
		if (null != layySh) {
			shList.add(layySh);
		}
		List<TLayyDsr> plaintiffList = nrcDsrDao.getLayyDsrList(layy.getCId(),
				Constants.LITIGANT_SSDW_PLAINTIFF, NrcDsrDao.TOP_ALL);
		dsrList.addAll(plaintiffList);

		List<TLayyDsr> defendantList = nrcDsrDao.getLayyDsrList(layy.getCId(),
				Constants.LITIGANT_SSDW_DEFENDANT, NrcDsrDao.TOP_ALL);
		dsrList.addAll(defendantList);

		List<TLayyDlr> agentList = nrcDlrDao.getLayyDlrList(layy.getCId(),
				NrcDlrDao.TOP_ALL);
		dlrList.addAll(agentList);

		List<TLayyZr> witnessList = nrcZrDao.getZrListBylayyId(layy.getCId(),
				NrcZrDao.TOP_ALL);
		zrList.addAll(witnessList);

		List<TLayySscl> indictmentList = nrcSsclDao.getSsclListBylayyId(
				layy.getCId(), NrcConstants.SSCL_TYPE_INDICTMENT,
				NrcSsclDao.TOP_ALL);
		if (CollectionUtils.isNotEmpty(indictmentList)) {
			for (TLayySscl indictment : indictmentList) {
				TLayySsclInfo ssclInfo = new TLayySsclInfo();
				ssclInfo.setSscl(indictment);
				ArrayList<TLayySsclFj> ssclFjList = (ArrayList<TLayySsclFj>) nrcSsclFjDao
						.getSsclFjListBySsclId(indictment.getCId(),
								NrcConstants.SYNC_ALL);
				ssclInfo.setSsclfjList(ssclFjList);
				ArrayList<TLayyDsrSscl> dsrSsclList = (ArrayList<TLayyDsrSscl>) nrcDsrSsclDao
						.getDsrSsclListByLayySsclId(indictment.getCLayyId(),
								indictment.getCId());
				ssclInfo.setSsdsrClList(dsrSsclList);
				ssclInfoList.add(ssclInfo);
			}
		}

		List<TLayySscl> localCertList = nrcSsclDao.getSsclListBylayyId(
				layy.getCId(), NrcConstants.SSCL_TYPE_CERTIFICATE,
				NrcZrDao.TOP_ALL);
		if (null != localCertList && localCertList.size() > 0) {
			for (TLayySscl cert : localCertList) {
				TLayySsclInfo ssclInfo = new TLayySsclInfo();
				ssclInfo.setSscl(cert);
				ArrayList<TLayySsclFj> certFjList = (ArrayList<TLayySsclFj>) nrcSsclFjDao
						.getSsclFjListBySsclId(cert.getCId(),
								NrcConstants.SYNC_ALL);
				ssclInfo.setSsclfjList(certFjList);
				ArrayList<TLayyDsrSscl> dsrSsclList = (ArrayList<TLayyDsrSscl>) nrcDsrSsclDao
						.getDsrSsclListByLayySsclId(layy.getCId(),
								cert.getCId());
				ssclInfo.setSsdsrClList(dsrSsclList);
				ssclInfoList.add(ssclInfo);
			}
		}

		ArrayList<TZj> evidenceList = (ArrayList<TZj>) nrcZjDao
				.getZjListBylayyId(layy.getCId(), NrcZjDao.TOP_ALL);
		if (null != evidenceList && evidenceList.size() > 0) {
			for (TZj zj : evidenceList) {
				TLayyZjInfo zjInfo = new TLayyZjInfo();
				zjInfo.setTZj(zj);
				ArrayList<TZjcl> evidenceMaterialList = (ArrayList<TZjcl>) nrcZjclDao
						.getZjclListByZjId(zj.getCId(), NrcZjclDao.TOP_ALL,
								NrcConstants.SYNC_ALL);
				zjInfo.setTZjclList(evidenceMaterialList);
			}
		}

		if (View.VISIBLE == proUserSfrzLinLayout.getVisibility()) { // 非律协用户，需要显示身份验证控件，准备控件的数据
			List<TProUserSfrzCl> sfrzclList = nrcSfrzclDao.getCurrSfrzClList(
					loginCache.getUserId(), layy.getCId());
			if (null == sfrzclList || sfrzclList.size() == 0) {
				TLayy layy = nrcBasicDao
						.getCurrUserLayy(loginCache.getUserId());
				if (null != layy) {
					sfrzclList = nrcSfrzclDao.getCurrSfrzClList(
							loginCache.getUserId(), layy.getCId());
				}
			}
			if (null != sfrzclList && sfrzclList.size() > 0) {
				for (TProUserSfrzCl sfrzcl : sfrzclList) {
					sfrzclMap.put(sfrzcl.getNClLx(), sfrzcl);
				}
			}
		}
	}

	public void showBaseInfo() {
		// 审核部分
		if ((!Integer.valueOf(NrcUtils.NRC_STATUS_DSC)
				.equals(layy.getNStatus()))
				&& CollectionUtils.isNotEmpty(shList)) {
			int last = shList.size() - 1;
			TLayySh tempSh = shList.get(last);
			revivewCheckLL.setVisibility(View.VISIBLE);
			checkResultTV.setText(NrcUtils.getCheckResultByCode(tempSh
					.getNShjg()));
			checkTimeTV.setText(NrcUtils.getFormatDate(tempSh.getDShsj()));
		} else if (Integer.valueOf(NrcUtils.NRC_STATUS_DSC).equals(
				layy.getNStatus())) {
			revivewCheckLL.setVisibility(View.VISIBLE);
			checkTimeTV.setVisibility(View.GONE);
		} else {
			revivewCheckLL.setVisibility(View.GONE);
		}
		HashMap<Integer, HashMap<String, String>> statusMap;
		statusMap = NrcUtils.getNrcStatusMap();
		HashMap<String, String> statusMapTemp = statusMap.get(layy.getNStatus());
		String statusName = statusMapTemp.get("name");
		String statusBgColor = statusMapTemp.get("bgColor");
		changeColor(statusName,statusBgColor);
		// 基本信息部分
		baseCreateTimeTV.setText("创建于 "
				+ NrcUtils.getFormatDate(layy.getDCreate()));
		baseUpdateTimeTV.setText("更新于 "
				+ NrcUtils.getFormatDate(layy.getDUpdate()));
		baseUserNameTV.setText("申请人：" + layy.getCProUserName());
		baseUserCardTV.setText(NrcUtils.getCertificateNameByCode(layy
				.getNIdcardType()) + "：" + layy.getCIdcard());
		baseUserPhoneTV.setText("手机号：" + layy.getCSjhm());
		baseCourtNameTV.setText("法院：" + layy.getCCourtName());
		baseCaseTypeTV.setText("案件类型：" + layy.getCAjlx());
		baseCaseLevelTV.setText("申请类别："
				+ NrcUtils.getApplicationTypeByCode(layy.getNSpcx()));
		// 原被告
		plaintiffNameTV.setText(plaintiffName);
		defendantNameTV.setText(defendantName);
		plaintiffCountTV.setText(plaintiffCount + "");
		defendantCountTV.setText(defendantCount + "");
		plaintiffCardCountTV.setText(plaintiffCardCount + "");
		agentCardCountTV.setText(agentCardCount + "");
		indictmentCountTV.setText(indictmentCount + "");
		evidenceCountTV.setText(CollectionUtils.isEmpty(zjInfoList) ? ""
				: zjInfoList.size() + "");

		if (indictmentCount == 0) {
			indictmentInfo.setVisibility(View.GONE);
		} else {
			indictmentInfo.setVisibility(View.VISIBLE);
		}
		// 证件部分
		if (plaintiffCardCount == 0) {
			plaintiffCardInfo.setVisibility(View.GONE);
		} else {
			plaintiffCardInfo.setVisibility(View.VISIBLE);
		}
		if (agentCardCount == 0) {
			agentCardInfo.setVisibility(View.GONE);
		} else {
			agentCardInfo.setVisibility(View.VISIBLE);
		}

		// 证人部分
		if (CollectionUtils.isEmpty(zrList)) {
			witnessInfo.setVisibility(View.GONE);
		} else {
			witnessInfo.setVisibility(View.VISIBLE);
		}
		// 证据部分
		if (CollectionUtils.isEmpty(zjInfoList)) {
			evidenceInfo.setVisibility(View.GONE);
		} else {
			evidenceInfo.setVisibility(View.VISIBLE);
		}
		nrcReviewAgentAdapter.setAgentList(dlrList);
		nrcReviewAgentAdapter.notifyDataSetChanged();
		nrcReviewWitnessAdapter.setWitnessList(zrList);
		nrcReviewWitnessAdapter.notifyDataSetChanged();
		refreshProUserSfrz();
	}
	
	
	
    /**
     * 改变审核状态图标颜色
     * @param code
     */
	@Background
	public void changeColor(String statusName,String statusBgColor) {
		checkResultTV.setText(statusName);
		revivewCheckIcon.setBackgroundColor(Color.parseColor(statusBgColor));
	}

	// public void buildMapBySsdw(){
	// for(TLayyDlr dlr:dlrList){
	// agentIdMap.put(dlr.getCId(), "3");
	// }
	// }

	public void refreshLitigantType(List<TLayyDsr> dsrList) {
		if (null != dsrList && dsrList.size() > 0) {
			for (int i = 0; i < dsrList.size(); i++) {
				if (dsrList.get(i).getCSsdw().equals("原告")) {
					plaintiffIdMap.put(dsrList.get(i).getCId(), "1");
					plaintiffName = plaintiffName
							+ (plaintiffName.equals("") ? "" : "、")
							+ dsrList.get(i).getCName();
					plaintiffCount++;
				} else {
					// defendantIdMap.put(dsrList.get(i).getCId(), "2");
					defendantName = defendantName
							+ (defendantName.equals("") ? "" : "、")
							+ dsrList.get(i).getCName();
					defendantCount++;
				}
			}
		}
	}

	public void refreshIndictmentList() {
		indictmentList.clear();
		if (null != ssclInfoList && ssclInfoList.size() > 0) {
			for (int i = 0; i < ssclInfoList.size(); i++) {
				TLayySscl sscl = ssclInfoList.get(i).getSscl();
				if (sscl.getNType() == NrcConstants.SSCL_TYPE_INDICTMENT) {
					indictmentList.add(ssclInfoList.get(i));
					indictmentCount++;
				}
			}
		}
	}

	public void refreshCertificatesList() {
		plaintiffCardList.clear();
		agentCardList.clear();
		certificatesList.clear();
		if (null != ssclInfoList && ssclInfoList.size() > 0) {
			for (int i = 0; i < ssclInfoList.size(); i++) {
				TLayySsclInfo Info = ssclInfoList.get(i);
				TLayySscl sscl = Info.getSscl();
				if (sscl.getNType() == NrcConstants.SSCL_TYPE_CERTIFICATE) {
					certificatesList.add(Info);
					List<TLayySsclFj> ssclfjList = Info.getSsclfjList();
					List<TLayyDsrSscl> ssdsrClList = Info.getSsdsrClList();
					for (TLayyDsrSscl ssclDsr : ssdsrClList) {
						if (NetRegisterCaseReviewActivity.plaintiffIdMap
								.containsKey(ssclDsr.getCDsrId())) {
							plaintiffCardList.addAll(ssclfjList);
							plaintiffCardCount++;
						}

						if (!NetRegisterCaseReviewActivity.plaintiffIdMap
								.containsKey(ssclDsr.getCDsrId())) {
							agentCardList.addAll(ssclfjList);
							agentCardCount++;
						}
					}
				}
			}
		}
	}

	/**
	 * 点击 审核信息
	 */
	@Click(R.id.nrc_review_check)
	protected void clickCheckInfo() {
		if (null != shList && shList.size() > 0) {
			Intent intent = new Intent(this, NRCReviewCheckActivity_.class);
			intent.putExtra("shList", (Serializable) shList);
			startActivity(intent);
		}
	}

	/**
	 * 点击 原告信息
	 */
	@Click(R.id.nrc_review_plaintiff)
	protected void clickPlaintiffInfo() {
		Intent intent = new Intent(this, NRCReviewLitigantActivity_.class);
		intent.putExtra("litigant_type", NrcConstants.LITIGANT_TYPE_PLAINTIFF);
		intent.putExtra("dsrList", (Serializable) dsrList);
		startActivity(intent);
	}

	/**
	 * 点击 被告信息
	 */
	@Click(R.id.nrc_review_defendant)
	protected void clickDefendantiffInfo() {
		Intent intent = new Intent(this, NRCReviewLitigantActivity_.class);
		intent.putExtra("litigant_type", NrcConstants.LITIGANT_TYPE_DEFENDANT);
		intent.putExtra("dsrList", (Serializable) dsrList);
		startActivity(intent);
	}

	/**
	 * 点击 诉讼材料信息
	 */
	@Click(R.id.nrc_review_indictment)
	protected void clickIndictmentInfo() {
		Intent intent = new Intent(this, NRCReviewIndictmentActivity_.class);
		intent.putExtra("indictmentList", (Serializable) indictmentList);
		// SerializableMap pMap = new SerializableMap();
		// pMap.setMap(plaintiffIdMap);
		// Bundle bundle=new Bundle();
		// bundle.putSerializable("plaintiffIdMap", pMap);
		// intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 点击 原告证件
	 */
	@Click(R.id.nrc_review_plaintiff_card)
	protected void clickPlaintiffCard() {
		Intent intent = new Intent(this, NRCReviewCertificatesActivity_.class);
		intent.putExtra("certificates_type",
				NrcConstants.CERTIFICATES_TYPE_PLAINTIFF);
		intent.putExtra("certificatesList", (Serializable) certificatesList);
		intent.putExtra("attachmentList", (Serializable) plaintiffCardList);
		startActivity(intent);
	}

	/**
	 * 点击 代理人证件
	 */
	@Click(R.id.nrc_review_agent_card)
	protected void clickAgentCard() {
		Intent intent = new Intent(this, NRCReviewCertificatesActivity_.class);
		intent.putExtra("certificates_type",
				NrcConstants.CERTIFICATES_TYPE_AGENT);
		intent.putExtra("certificatesList", (Serializable) certificatesList);
		intent.putExtra("attachmentList", (Serializable) agentCardList);
		startActivity(intent);
	}

	/**
	 * 点击 证据
	 */
	@Click(R.id.nrc_review_evidence)
	protected void clickEvidenceInfo() {
		Intent intent = new Intent(this, NRCReviewEvidenceActivity_.class);
		intent.putExtra("zjInfoList", (Serializable) zjInfoList);
		startActivity(intent);
	}

	/**
	 * 点击 取消
	 */
	@Click(R.id.nrc_cancel)
	protected void clickCancel() {
		this.finish();
	}

	/**
	 * 点击 返回
	 */
	@Click(R.id.btn_back)
	protected void clickBack() {
		this.finish();
	}

	/**
	 * 点击 手持证件照
	 */
	@Click(R.id.nrcr_hold_idcard_img)
	protected void clickHoldIdcardImg() {
		TProUserSfrzCl sfrzCl = getSfrzCl(NrcConstants.PRO_USER_SFRZ_SCSFZ);
		startSfrzClActivity(sfrzCl, NrcConstants.REQ_CODE_SFRZ_SCSFZ);
	}

	/**
	 * 点击 身份证正面照
	 */
	@Click(R.id.nrcr_idcard_face_img)
	protected void clickIdcardFacePhoto() {
		TProUserSfrzCl sfrzCl = getSfrzCl(NrcConstants.PRO_USER_SFRZ_ZM);
		startSfrzClActivity(sfrzCl, NrcConstants.REQ_CODE_SFRZ_ZM);
	}

	/**
	 * 点击 身份证背面照
	 */
	@Click(R.id.nrcr_idcard_back_img)
	protected void clickIdcardBackPhoto() {
		TProUserSfrzCl sfrzCl = getSfrzCl(NrcConstants.PRO_USER_SFRZ_BM);
		startSfrzClActivity(sfrzCl, NrcConstants.REQ_CODE_SFRZ_BM);
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
	 * 打开身份认证的activity
	 * 
	 * @param sfrzCl
	 *            身份认证材料
	 */
	private void startSfrzClActivity(TProUserSfrzCl sfrzCl, int requestCode) {
		if (StringUtils.isNotBlank(sfrzCl.getCClPath())) {
			File organFile = new File(sfrzCl.getCClPath());
			if (organFile.exists()) {
				openFile(sfrzCl);
			} else {
				downloadLogic.downloadSmrzcl(sfrzCl,
						NrcConstants.OPEN_TYPE_DISPLAY);
			}
		} else {
			downloadLogic
					.downloadSmrzcl(sfrzCl, NrcConstants.OPEN_TYPE_DISPLAY);
		}
	}

	private void openFile(TProUserSfrzCl sfrzCl) {
		Intent intent = new Intent();
		ArrayList<PicModel> picModelList = new ArrayList<PicModel>();
		PicModel picModel = new PicModel();
		picModel.setRelId(sfrzCl.getCBh());
		picModel.setRelPid(sfrzCl.getCBh());
		picModel.setName(sfrzCl.getCClName());
		picModel.setPath(sfrzCl.getCClPath());
		picModel.setType(PicModel.TYPE_SFRZ_CL);
		picModelList.add(picModel);
		intent.setClass(this, PicPreviewActivity_.class);
		intent.putExtra(PicPreviewActivity.K_CURR_POSITION, 0);
		intent.putExtra(PicPreviewActivity.IS_SHOW_PERCENT, false);
		intent.putExtra(PicPreviewActivity.K_PIC_IST, picModelList);
		intent.putExtra(PicPreviewActivity.K_OPEN_TYPE,
				NrcConstants.OPEN_TYPE_DISPLAY);
		this.startActivity(intent);
	}

	// /**
	// * 序列化map供Bundle传递map使用
	// */
	// @SuppressWarnings("serial")
	// public class SerializableMap implements Serializable {
	//
	// private Map<String,Object> map;
	//
	// public Map<String, Object> getMap() {
	// return map;
	// }
	//
	// public void setMap(Map<String, Object> map) {
	// this.map = map;
	// }
	// }

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
			for (Map.Entry<Integer, TProUserSfrzCl> entry : sfrzclMap
					.entrySet()) {
				int clLx = entry.getKey();
				String path = entry.getValue().getCClPath();
				String fileName = entry.getValue().getCClName();
				if (StringUtils.isNotBlank(path)) {
					if (NrcConstants.PRO_USER_SFRZ_SCSFZ == clLx) {
						File scsfzFile = new File(path);
						if (scsfzFile.exists()) {
							holdIdcardImgRL.setBackgroundDrawable(FileUtils
									.getThumbnailDrawable(this, path));
						} else {
							holdIdcardSuffixBgFL.setVisibility(View.VISIBLE);
							holdIdcardSuffixNameTV.setText(FileUtils
									.getFileSuffix(fileName));
							holdIdcardImgRL
									.setBackgroundResource(R.drawable.file_item_bg);
						}
					} else if (NrcConstants.PRO_USER_SFRZ_ZM == clLx) {
						File zmFile = new File(path);
						if (zmFile.exists()) {
							idcardFaceImgRL.setBackgroundDrawable(FileUtils
									.getThumbnailDrawable(this, path));
						} else {
							idcardFaceSuffixBgFL.setVisibility(View.VISIBLE);
							idcardFaceSuffixNameTV.setText(FileUtils
									.getFileSuffix(fileName));
							idcardFaceImgRL
									.setBackgroundResource(R.drawable.file_item_bg);
						}
					} else if (NrcConstants.PRO_USER_SFRZ_BM == clLx) {
						File bmFile = new File(path);
						if (bmFile.exists()) {
							idcardBackImgRL.setBackgroundDrawable(FileUtils
									.getThumbnailDrawable(this, path));
						} else {
							idcardBackSuffixBgFL.setVisibility(View.VISIBLE);
							idcardBackSuffixNameTV.setText(FileUtils
									.getFileSuffix(fileName));
							idcardBackImgRL
									.setBackgroundResource(R.drawable.file_item_bg);
						}
					}
				} else {
					if (NrcConstants.PRO_USER_SFRZ_SCSFZ == clLx) {
						holdIdcardSuffixBgFL.setVisibility(View.VISIBLE);
						holdIdcardSuffixNameTV.setText(FileUtils
								.getFileSuffix(fileName));
						idcardBackImgRL
								.setBackgroundResource(R.drawable.file_item_bg);
					} else if (NrcConstants.PRO_USER_SFRZ_ZM == clLx) {
						idcardFaceSuffixBgFL.setVisibility(View.VISIBLE);
						idcardFaceSuffixNameTV.setText(FileUtils
								.getFileSuffix(fileName));
						idcardFaceImgRL
								.setBackgroundResource(R.drawable.file_item_bg);
					} else if (NrcConstants.PRO_USER_SFRZ_BM == clLx) {
						idcardBackSuffixBgFL.setVisibility(View.VISIBLE);
						idcardBackSuffixNameTV.setText(FileUtils
								.getFileSuffix(fileName));
						idcardBackImgRL
								.setBackgroundResource(R.drawable.file_item_bg);
					}
				}
			}
		}
	}
}
