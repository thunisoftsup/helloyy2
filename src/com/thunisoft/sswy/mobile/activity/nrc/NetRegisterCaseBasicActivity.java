package com.thunisoft.sswy.mobile.activity.nrc;

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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.AgentTypeDioagActivity;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.AgentTypeDioagActivity_;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcBasicListAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.datasource.NrcBasicDao;
import com.thunisoft.sswy.mobile.datasource.NrcDlrDao;
import com.thunisoft.sswy.mobile.logic.NrcNoticeLogic;
import com.thunisoft.sswy.mobile.logic.NrcNoticeLogic.NrcNoticeResponse;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.DensityUtil;
import com.thunisoft.sswy.mobile.util.IDCard;
import com.thunisoft.sswy.mobile.util.ServiceUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;
import com.thunisoft.sswy.mobile.view.HorizontalListView;

/**
 * 网上立案 基本信息添加
 * 
 */

@EActivity(R.layout.activity_net_register_case_basic)
public class NetRegisterCaseBasicActivity extends BaseActivity {

	/** 申请法院 */
	@ViewById(R.id.nrc_basic_court_name)
	protected TextView courtNameTV;

	/** 案件类型列表 */
	@ViewById(R.id.nrc_basic_case_type_list)
	protected HorizontalListView caseTypeListView;

	/** 审判程序列表 */
	@ViewById(R.id.nrc_basic_judge_program_list)
	protected HorizontalListView judgeProgramListView;

	/** 申请人类型列表 */
	@ViewById(R.id.nrc_basic_applicant_list)
	protected HorizontalListView applicantListView;

	/** 代理人类型 */
	@ViewById(R.id.nrc_basic_agent_type)
	public LinearLayout agentTypeLinLayout;

	/** 代理人类型 显示文字 */
	@ViewById(R.id.nrc_basic_agent_type_text)
	public TextView agentTypeTextTV;

	/** 立案须知 */
	@ViewById(R.id.nrc_basic_notice_court)
	protected TextView noticeCourtTV;
	
	/** 立案须知 */
	@ViewById(R.id.nrc_basic_notice)
	protected TextView noticeTV;

	/** 法院Id intent Key */
	public static final String K_COURT_ID = "courtId";

	/** 法院名称 intent Key */
	public static final String K_COURT_NAME = "courtName";

	/** 立案预约_基本信息 */
	private TLayy layy;

	/** 为他人申请_代理人 */
	private TLayyDlr layyDlr = new TLayyDlr();

	/** 为自己申请_当事人 */
	private TLayyDsr layyDsr;

	/** 案件类型 */
	private NrcBasicListAdapter caseTypeAdapter;

	/** 申请类别 */
	private NrcBasicListAdapter judgeProgramAdapter;

	/** 申请人类型 */
	private NrcBasicListAdapter applicantTypeAdapter;

	@Bean
	NrcBasicDao nrcBasicDao;

	@Bean
	NrcDlrDao nrcDlrDao;

	/**
	 * 登录信息
	 */
	@Bean
	LoginCache loginCache;

	@Bean
	NrcNoticeLogic noticeLogic;
	
	private WaittingDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		waitDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "获取网上立案须知...");
		waitDialog.setIsCanclable(false);
		waitDialog.show();
		Intent intent = getIntent();
		String courtId = intent.getStringExtra(K_COURT_ID);
		String courtName = intent.getStringExtra(K_COURT_NAME);
		layy = NrcEditData.getLayy();
		layy.setCCourtId(courtId);
		layy.setCCourtName(courtName);
		layy.setNAjlx(NrcUtils.CASE_TYPE_MS);
		layy.setCAjlx(NrcUtils.getCaseTypeNameByCode(NrcUtils.CASE_TYPE_MS));
		layy.setNSpcx(NrcUtils.JUDGE_PROGRAM_QS);
		layy.setCSpcx(NrcUtils.getJudgeProgramNameByCode(NrcUtils.JUDGE_PROGRAM_QS));
		layy.setNSqrSf(NrcUtils.APPLICANT_FOR_ME);
		layy.setCSqrSf(NrcUtils.getApplicantNameByCode(NrcUtils.APPLICANT_FOR_ME));
		layy.setNSync(NrcConstants.SYNC_FALSE);
		courtNameTV.setText(courtName);
		int dividerWidth = DensityUtil.dip2px(this, 10);
		caseTypeListView.setDividerWidth(dividerWidth);
		judgeProgramListView.setDividerWidth(dividerWidth);
		applicantListView.setDividerWidth(dividerWidth);
		refreshCaseType();
		refreshJudgeProgram();
		refreshApplicantType();
		if (LoginCache.LOGIN_TYPE_LS_VERIFID == loginCache.getLoginType()) {
			String agentTypeName = NrcUtils.getAgentNameByCode(Constants.AGENT_TYPE_LAWYER);
			agentTypeTextTV.setText(agentTypeName);
			layyDlr.setNDlrType(Constants.AGENT_TYPE_LAWYER);
			agentTypeLinLayout.setVisibility(View.VISIBLE);
		} else {
			agentTypeLinLayout.setVisibility(View.GONE);
		}
		requestNrcNotice();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Constants.RESULT_OK != resultCode) {
			return;
		}

		switch (requestCode) {
		case Constants.REQUEST_CODE_SELECT_COURT:
			layy.setCCourtId(data.getStringExtra("courtId"));
			layy.setCCourtName(data.getStringExtra("courtName"));
			courtNameTV.setText(layy.getCCourtName());
			break;

		case Constants.REQUEST_CODE_SELECT_AGENT_TYPE:
			int code = data.getIntExtra(AgentTypeDioagActivity.K_AGENT_TYPE_CODE, Constants.AGENT_TYPE_LAWYER);
			layyDlr.setNDlrType(code);
			agentTypeTextTV.setText(NrcUtils.getAgentNameByCode(code));
			break;

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
		case KeyEvent.KEYCODE_BACK: //监听Back键按下事件
			NrcEditData.clearData();
			return super.onKeyDown(keyCode, event);

		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 点击取消
	 */
	@Click(R.id.nrc_basic_cancel)
	protected void clickCancel() {
		NrcEditData.clearData();
		this.finish();
	}

//	/**
//	 * 点击申请法院
//	 */
//	@Click(R.id.nrc_basic_court)
//	protected void clickCourt() {
//		Intent intent = new Intent();
//		intent.putExtra(CourtListActivity.K_IS_NRC_SELECT_COURT, true);
//		intent.putExtra(CourtListActivity.K_COURT_ID, layy.getCCourtId());
//		intent.setClass(this, CourtListActivity_.class);
//		startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_COURT);
//	}

	/**
	 * 点击 代理人类型
	 */
	@Click(R.id.nrc_basic_agent_type_click)
	protected void clickAgentType() {
		Intent intent = new Intent(this, AgentTypeDioagActivity_.class);
		intent.putExtra(AgentTypeDioagActivity.K_AGENT_TYPE_CODE, layyDlr.getNDlrType());
		startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_AGENT_TYPE);
	}

	/**
	 * 点击 同意并创建立案申请
	 */
	@Click(R.id.nrc_basic_agree_create)
	protected void clickAgreeCreate() {
		layy.setCId(UUIDHelper.getUuid());
		if (View.VISIBLE == agentTypeLinLayout.getVisibility()) {
			buildLayyDlr();
			layy.setCSqrId(layyDlr.getCId());
			layy.setCSqrName(layyDlr.getCName());
			NrcEditData.getAgentList().add(layyDlr);
		} else {
			buildLayyDsr();
			layy.setCSqrId(layyDsr.getCId());
			layy.setCSqrName(layyDsr.getCName());
			NrcEditData.getPlaintiffList().add(layyDsr);
		}

		buildLayy();

		Intent intent = new Intent();// layy使用静态变量了，不再传bean
		// intent.putExtra(NetRegisterCaseActivity.K_LAYY, layy);
		intent.setClass(this, NetRegisterCaseActivity_.class);
		startActivity(intent);
		this.finish();
	}

	/**
	 * 请求服务器获取网上立案须知
	 */
	@Background
	public void requestNrcNotice() {
		NrcNoticeResponse response = noticeLogic.requestNrcNotice(layy.getCCourtId());
		responseNrcNotice(response);
	}
	
	@UiThread
	protected void responseNrcNotice(NrcNoticeResponse response) {
		waitDialog.dismiss();
		waitDialog = null;
		if (response.isSuccess()) {
			noticeCourtTV.setText(response.getTitle());
			noticeTV.setText(response.getContent());
		} else {
			Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}
	
	/**
	 * 刷新 案件类型
	 */
	private void refreshCaseType() {
		List<Map<String, String>> caseTypeList = NrcUtils.getCaseTypeList();

		if (null == caseTypeAdapter) {
			caseTypeAdapter = new NrcBasicListAdapter(this, caseTypeList, false);

			Map<String, Map<String, String>> selectedItemMap = caseTypeAdapter.getSelectedItemBeanMap();
			Map<String, String> itemMap = caseTypeList.get(0);
			selectedItemMap.put(itemMap.get(NrcUtils.KEY_CODE), itemMap);

			caseTypeListView.setAdapter(caseTypeAdapter);
		} else {
			caseTypeAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新 审判程序
	 */
	private void refreshJudgeProgram() {
		List<Map<String, String>> judgeProgramList = NrcUtils.getJudgeProgramList();

		if (null == judgeProgramAdapter) {
			judgeProgramAdapter = new NrcBasicListAdapter(this, judgeProgramList, false);

			Map<String, Map<String, String>> selectedItemMap = judgeProgramAdapter.getSelectedItemBeanMap();
			Map<String, String> itemMap = judgeProgramList.get(0);
			selectedItemMap.put(itemMap.get(NrcUtils.KEY_CODE), itemMap);

			judgeProgramListView.setAdapter(judgeProgramAdapter);
		} else {
			judgeProgramAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新 申请人类型
	 */
	private void refreshApplicantType() {
		List<Map<String, String>> applicantTypeList = NrcUtils.getApplicantTypeList(loginCache.getLoginType());
		if (null == applicantTypeAdapter) {
			applicantTypeAdapter = new NrcBasicListAdapter(this, applicantTypeList, false);
			applicantTypeAdapter.setApplicantType(true);

			Map<String, Map<String, String>> selectedItemMap = applicantTypeAdapter.getSelectedItemBeanMap();
			Map<String, String> itemMap = applicantTypeList.get(0);
			selectedItemMap.put(itemMap.get(NrcUtils.KEY_CODE), itemMap);

			applicantListView.setAdapter(applicantTypeAdapter);
		} else {
			applicantTypeAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 构造 立案预约实体bean
	 */
	@SuppressLint("SimpleDateFormat")
	private void buildLayy() {
		String nowTime = ServiceUtils.getServerTimeStr(this);
		layy.setDCreate(nowTime);
		layy.setDUpdate(nowTime);
		layy.setCProUserId(loginCache.getUserId());
		layy.setCProUserName(loginCache.getXm());
		layy.setCSjhm(loginCache.getPhone());
		layy.setCIdcard(loginCache.getZjhm());
		layy.setNIdcardType(NrcUtils.getCertificateCodeByName(loginCache.getZjLx()));
		Map<String, Map<String, String>> caseTypeSelectedMap = caseTypeAdapter.getSelectedItemBeanMap();
		for (Map.Entry<String, Map<String, String>> itemBeanEntry : caseTypeSelectedMap.entrySet()) {
			Map<String, String> itemMap = itemBeanEntry.getValue();
			layy.setNAjlx(Integer.parseInt(itemMap.get(NrcUtils.KEY_CODE)));
			layy.setCAjlx(itemMap.get(NrcUtils.KEY_NAME));
			break;
		}

		Map<String, Map<String, String>> judgeProgramSelectedMap = judgeProgramAdapter.getSelectedItemBeanMap();
		for (Map.Entry<String, Map<String, String>> itemBeanEntry : judgeProgramSelectedMap.entrySet()) {
			Map<String, String> itemMap = itemBeanEntry.getValue();
			layy.setNSpcx(Integer.parseInt(itemMap.get(NrcUtils.KEY_CODE)));
			layy.setCSpcx(itemMap.get(NrcUtils.KEY_NAME));
			break;
		}

		Map<String, Map<String, String>> applicantTypeSelectedMap = applicantTypeAdapter.getSelectedItemBeanMap();
		for (Map.Entry<String, Map<String, String>> itemBeanEntry : applicantTypeSelectedMap.entrySet()) {
			Map<String, String> itemMap = itemBeanEntry.getValue();
			layy.setNSqrRzqk(Integer.parseInt(itemMap.get(NrcUtils.KEY_CODE)));
			layy.setCSqrRzqk(itemMap.get(NrcUtils.KEY_NAME));
			break;
		}
		layy.setNStatus(NrcUtils.NRC_STATUS_DTJ);
		layy.setNSync(NrcConstants.SYNC_FALSE);
	}

	/**
	 * 构造 立案预约_原告
	 */
	private void buildLayyDsr() {
		layyDsr = new TLayyDsr();
		layyDsr.setCId(UUIDHelper.getUuid());
		layyDsr.setCLayyId(layy.getCId());
		layyDsr.setNIdcardType(NrcUtils.getCertificateCodeByName(loginCache.getZjLx()));
		layyDsr.setCIdcard(loginCache.getZjhm());
		layyDsr.setCSjhm(loginCache.getPhone());
		layyDsr.setCName(loginCache.getXm());
		layyDsr.setNXb(Constants.GENDER_MAN);
		layyDsr.setCSsdw(Constants.LITIGANT_SSDW_PLAINTIFF);
		layyDsr.setNType(Constants.LITIGANT_TYPE_NATURAL);

		if (NrcConstants.CERTIFICATE_TYPE_IDCARD == layyDsr.getNIdcardType()) {
			if (IDCard.isValid(layyDsr.getCIdcard())) {
				String birthdayTime = IDCard.getBirthDay(layyDsr.getCIdcard());
				layyDsr.setDCsrq(birthdayTime);
			} else {
				layyDsr.setDCsrq(NrcUtils.getBirthDay());
			}
		} else {
			layyDsr.setDCsrq(NrcUtils.getBirthDay());
		}
	}

	/**
	 * 构造 立案预约_代理人
	 */
	private void buildLayyDlr() {
		layyDlr.setCId(UUIDHelper.getUuid());
		layyDlr.setCLayyId(layy.getCId());
		layyDlr.setCIdcard(loginCache.getZjhm());
		layyDlr.setNIdcardType(NrcUtils.getCertificateCodeByName(loginCache.getZjLx()));
		layyDlr.setCName(loginCache.getXm());
		layyDlr.setCSjhm(loginCache.getPhone());
	}

	/**
	 * 构造 立案预约_当事人
	 * 
	 * @return layyDlr
	 */
	public TLayyDlr getLayyDlr() {
		return this.layyDlr;
	}

	/**
	 * 设置 代理人
	 * 
	 * @param layyDlr
	 */
	public void setLayyDlr(TLayyDlr layyDlr) {
		this.layyDlr = layyDlr;
	}
}
