package com.thunisoft.sswy.mobile.activity.nrc;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmOtherDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ZjlxDioagActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ZjlxDioagActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.AgentTypeDioagActivity;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.AgentTypeDioagActivity_;
import com.thunisoft.sswy.mobile.adapter.CertTypeAdapter;
import com.thunisoft.sswy.mobile.datasource.NrcDlrDao;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.util.IDCard;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcCheckUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案 添加代理人
 * 
 */

@EActivity(R.layout.activity_nrc_add_agent)
public class NrcAddAgentActivity extends BaseActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_aa_title)
	protected TextView titleTV;

	/** 代理人 类型 */
	@ViewById(R.id.nrc_aa_type)
	protected LinearLayout typeLinLayout;
	
	/** 代理人 类型 名称 */
	@ViewById(R.id.nrc_aa_type_name)
	protected TextView typeNameTV;

	/** 代理人 姓名 */
	@ViewById(R.id.nrc_aa_name)
	protected EditText nameET;

	/** 代理人 证件 */
	@ViewById(R.id.nrc_aa_certificate_type)
	protected LinearLayout certificateTypeLinLayout;
	
	/** 代理人 证件名称 */
	@ViewById(R.id.nrc_aa_certificate_type_name)
	protected TextView certificateTypeNameTV;

	/** 代理人 证件号码 */
	@ViewById(R.id.nrc_aa_certificate_type_num)
	protected EditText certificateTypeNumET;

	/** 代理人 手机号 */
	@ViewById(R.id.nrc_aa_tel)
	protected EditText telET;

	/** 代理人 执业证号 信息 */
	@ViewById(R.id.nrc_aa_practice)
	protected LinearLayout practiceLinLayout;

	/** 代理人 执业证号 号码 */
	@ViewById(R.id.nrc_aa_practice_num)
	protected EditText practiceNumET;

	/** 代理人 律所 信息 */
	@ViewById(R.id.nrc_aa_law)
	protected LinearLayout lawInfoLinLayout;

	/** 代理人 律所 名称 */
	@ViewById(R.id.nrc_aa_law_name)
	protected EditText lawNameET;

	/** 代理人 被代理人名称 */
	@ViewById(R.id.nrc_aa_agented_names)
	protected TextView litigantedNamesTV;

	/** 代理人 删除 按钮 */
	@ViewById(R.id.nrc_aa_delete)
	protected Button deleteBtn;

	/** 关联 被代理人 */
	private static final int REQ_CODE_REL_AGENTED = 5;
	
	public static final String K_AGENT = "agent";
	
	private TLayyDlr layyDlr;

	@Bean
	NrcDlrDao nrcDlrDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		Intent intent = getIntent();
		layyDlr = (TLayyDlr) intent.getSerializableExtra(K_AGENT);
		if (StringUtils.isNotBlank(layyDlr.getCId())) {
			nameET.setText(layyDlr.getCName());
			certificateTypeNumET.setText(layyDlr.getCIdcard());
			telET.setText(layyDlr.getCSjhm());
			litigantedNamesTV.setText(layyDlr.getCBdlrMc());
			setLawyerVisible();
			/** 代理人id和网上立案申请人id相同，则不能删除 */
			setEditEnable();
		} else {
			layyDlr.setCId(UUIDHelper.getUuid());
			layyDlr.setNDlrType(Constants.AGENT_TYPE_LAWYER);
			layyDlr.setNIdcardType(NrcConstants.CERTIFICATE_TYPE_IDCARD);
			String digits = getString(R.string.id_card_regex);
			certificateTypeNumET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NrcConstants.CERTIFICATE_LENGTH_ID_CARD)}); 
			certificateTypeNumET.setKeyListener(DigitsKeyListener.getInstance(digits));
		}
		typeNameTV.setText(NrcUtils.getAgentNameByCode(layyDlr.getNDlrType()));
		certificateTypeNameTV.setText(NrcUtils.getCertificateNameByCode(layyDlr.getNIdcardType()));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Constants.RESULT_OK != resultCode && Activity.RESULT_OK != resultCode) {
			return;
		}

		switch (requestCode) {
		case NrcConstants.REQ_CODE_CANCEL:
			this.finish();
			break;
		case Constants.REQUEST_CODE_SELECT_AGENT_TYPE:
			int dlrType = data.getIntExtra(AgentTypeDioagActivity.K_AGENT_TYPE_CODE, Constants.AGENT_TYPE_LAWYER);
			layyDlr.setNDlrType(dlrType);
			if (Constants.AGENT_TYPE_LAWYER == layyDlr.getNDlrType()
					|| Constants.AGENT_TYPE_ASSIST_LAWYER == layyDlr.getNDlrType()) {
				lawInfoLinLayout.setVisibility(View.VISIBLE);
				practiceLinLayout.setVisibility(View.VISIBLE);
			} else {
				lawInfoLinLayout.setVisibility(View.GONE);
				practiceLinLayout.setVisibility(View.GONE);
				practiceNumET.getText().clear();
				lawNameET.getText().clear();
			}
			typeNameTV.setText(NrcUtils.getAgentNameByCode(layyDlr.getNDlrType()));
			break;

		case Constants.REQUEST_CODE_XZZJ:
			String certCodeStr = data.getStringExtra(CertTypeAdapter.K_SELECTED_CODE);
			int certCode = Integer.parseInt(certCodeStr);
			if (layyDlr.getNIdcardType() != certCode) {
				layyDlr.setNIdcardType(certCode);
				certificateTypeNumET.getText().clear();
				String certName = NrcUtils.getCertificateNameByCode(certCode);
				certificateTypeNameTV.setText(certName);
				if (Constants.CERTIFICATE_TYPE_ID_CARD != Integer.parseInt(certCodeStr)) {
					certificateTypeNumET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NrcConstants.CERTIFICATE_LENGTH_OTHER)});
					certificateTypeNumET.setInputType(InputType.TYPE_CLASS_TEXT);
				} else {
					certificateTypeNumET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NrcConstants.CERTIFICATE_LENGTH_ID_CARD)});  
					String digits = getString(R.string.id_card_regex);
					certificateTypeNumET.setKeyListener(DigitsKeyListener.getInstance(digits));
				}
			}
			break;
			
		case REQ_CODE_REL_AGENTED:
			
			layyDlr = (TLayyDlr)data.getSerializableExtra(NrcRelAgentedActivity.K_AGENT);
			litigantedNamesTV.setText(layyDlr.getCBdlrMc());
			break;
			
		case Constants.REQUEST_CODE_DELETE_AGENT:
			deleteBtnSure();
			break;

		default:
			break;
		}
	}

	/**
	 * 点击 当事人 返回
	 */
	@Click(R.id.nrc_aa_back)
	protected void clickBack() {
		Intent intent = new Intent(this, ConfirmDialogActivity_.class);
		intent.putExtra("message", "数据尚未保存，确定取消？");
		startActivityForResult(intent, NrcConstants.REQ_CODE_CANCEL);
	}

	/**
	 * 点击 当事人 确定
	 */
	@Click(R.id.nrc_aa_sure)
	protected void clickSure() {
		if (checkAgentData()) {
			List<TLayyDlr> layyDlrList = NrcEditData.getAgentList();
			int delPosition = getDelPosition(layyDlrList);
			if (delPosition >= 0) {
				layyDlrList.remove(delPosition);
				layyDlrList.add(delPosition, layyDlr); // 先保存到静态变量中，在立案预约中，点击暂存或者提交，统一保存
			} else {
				layyDlrList.add(layyDlr); // 先保存到静态变量中，在立案预约中，点击暂存或者提交，统一保存
			}
			NrcAddAgentActivity.this.finish();
		}
	}

	/**
	 * 点击 代理人类型
	 */
	@Click(R.id.nrc_aa_type)
	protected void clickAgentType() {
		Intent intent = new Intent(this, AgentTypeDioagActivity_.class);
		intent.putExtra(AgentTypeDioagActivity.K_AGENT_TYPE_CODE, layyDlr.getNDlrType());
		startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_AGENT_TYPE);
	}

	/**
	 * 点击 证件类型
	 */
	@Click(R.id.nrc_aa_certificate_type)
	protected void clickCertificateType() {
		Intent intent = new Intent(this, ZjlxDioagActivity_.class);
		intent.putExtra(ZjlxDioagActivity.K_CERT_CODE, String.valueOf(layyDlr.getNIdcardType()));
		startActivityForResult(intent, Constants.REQUEST_CODE_XZZJ);
	}

	/**
	 * 点击 被代理人
	 */
	@Click(R.id.nrc_aa_agented)
	protected void clickAgented() {
		Intent intent = new Intent();
		intent.setClass(this, NrcRelAgentedActivity_.class);
		intent.putExtra(NrcRelAgentedActivity.K_AGENT, layyDlr);
		intent.putExtra(NrcRelAgentedActivity.K_LITIGANT_SSDW, "原告");
		startActivityForResult(intent, REQ_CODE_REL_AGENTED);
	}

	/**
	 * 点击 当事人 删除
	 */
	@Click(R.id.nrc_aa_delete)
	protected void clickDeleteBtn() {
		Intent intent = new Intent(NrcAddAgentActivity.this, ConfirmOtherDialogActivity_.class);
        intent.putExtra("message", getResources().getString(R.string.text_delete));
        NrcAddAgentActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_DELETE_AGENT);
	}
	
	public void deleteBtnSure(){
		List<TLayyDlr> agentList = NrcEditData.getAgentList();
		int position = getDelPosition(agentList);
		if (position >= 0) {
			agentList.remove(position);
		}
		NrcAddAgentActivity.this.finish();
	}
	
	/**
	 * 检查代理人输入项
	 * @return
	 */
	private boolean checkAgentData() {
		String name = nameET.getText().toString().trim();
		String certificateNum = certificateTypeNumET.getText().toString().trim();
		String tel = telET.getText().toString().trim();
		String practiceNum = practiceNumET.getText().toString().trim();
		String lawName = lawNameET.getText().toString().trim();
		layyDlr.setCName(name);
		layyDlr.setCIdcard(certificateNum);
		layyDlr.setCSjhm(tel);
		layyDlr.setCZyzh(practiceNum);
		layyDlr.setCSzdw(lawName);
        if (StringUtils.isBlank(layyDlr.getCName())) {
        	Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
        	return false;
        }
        if (StringUtils.isBlank(layyDlr.getCIdcard())) {
			String zjlx = NrcUtils.getCertificateNameByCode(layyDlr.getNIdcardType());
			Toast.makeText(this, "请输入"+zjlx+"号码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (NrcConstants.CERTIFICATE_TYPE_IDCARD == layyDlr.getNIdcardType()) {//身份证需要校验格式
			if (!IDCard.isValid(layyDlr.getCIdcard())) {
				Toast.makeText(this, "身份证号格式不正确", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
        if (StringUtils.isBlank(layyDlr.getCSjhm())) {
        	Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        	return false;
        } else {
			if (!NrcCheckUtils.isMobileNO(layyDlr.getCSjhm())) {
				Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
        if (Constants.AGENT_TYPE_LAWYER == layyDlr.getNDlrType()
				|| Constants.AGENT_TYPE_ASSIST_LAWYER == layyDlr.getNDlrType()) {
        	if (StringUtils.isBlank(layyDlr.getCZyzh())) {
            	Toast.makeText(this, "请输入执业证号", Toast.LENGTH_SHORT).show();
            	return false;
            }
        	if (StringUtils.isBlank(layyDlr.getCSzdw())) {
            	Toast.makeText(this, "请输入所在单位", Toast.LENGTH_SHORT).show();
            	return false;
            }
		} else {
			layyDlr.setCZyzh("");
			layyDlr.setCSzdw("");
		}
		if (StringUtils.isBlank(layyDlr.getCBdlrId())) {
			Toast.makeText(this, "请关联被代理人", Toast.LENGTH_SHORT).show();
        	return false;
		}
		return true;
	}
	
	/**
	 * 设置是否允许编辑
	 */
	private void setEditEnable() {
		String sqrId = NrcEditData.getLayy().getCSqrId();
		if (sqrId.equals(layyDlr.getCId())) {
			deleteBtn.setVisibility(View.GONE);
			nameET.setEnabled(false);
			typeLinLayout.setClickable(false);
			certificateTypeLinLayout.setClickable(false);
			certificateTypeNumET.setEnabled(false);
			telET.setEnabled(false);
		} else {
			deleteBtn.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 设置是否显示律师相关的 控件 <br>
	 * 执业证号、所在单位
	 */
	private void setLawyerVisible() {
		if (Constants.AGENT_TYPE_LAWYER == layyDlr.getNDlrType() //代理人类型为：律师 或 承担法律援助的律师 需要显示 执业证号 和 所在单位
				|| Constants.AGENT_TYPE_ASSIST_LAWYER == layyDlr.getNDlrType()) {
			lawInfoLinLayout.setVisibility(View.VISIBLE);
			practiceLinLayout.setVisibility(View.VISIBLE);
			practiceNumET.setText(layyDlr.getCZyzh());
			lawNameET.setText(layyDlr.getCSzdw());
		} else {
			lawInfoLinLayout.setVisibility(View.GONE);
			practiceLinLayout.setVisibility(View.GONE);
		}
	}
	
	private int getDelPosition(List<TLayyDlr> layyDlrList) {
		int delPosition = -1;
		if (null != layyDlrList && layyDlrList.size() > 0) {
			for (int i=0; i<layyDlrList.size(); i++) {
				TLayyDlr dlr = layyDlrList.get(i);
				if (dlr.getCId().equals(layyDlr.getCId())) {
					delPosition = i;
					break;
				}
			}
		}
		return delPosition;
	}
}
