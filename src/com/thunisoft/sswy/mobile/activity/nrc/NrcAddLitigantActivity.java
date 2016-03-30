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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmOtherDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.LitigantTypeDioagActivity;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.LitigantTypeDioagActivity_;
import com.thunisoft.sswy.mobile.datasource.NrcDsrDao;
import com.thunisoft.sswy.mobile.fragment.nrc.LitigantAddBaseFragment;
import com.thunisoft.sswy.mobile.fragment.nrc.LitigantAddLegalFragment_;
import com.thunisoft.sswy.mobile.fragment.nrc.LitigantAddNaturalFragment_;
import com.thunisoft.sswy.mobile.fragment.nrc.LitigantAddNonLegalFragment_;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.util.IDCard;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcCheckUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案 添加当事人
 * 
 */

@EActivity(R.layout.activity_nrc_add_litigant)
public class NrcAddLitigantActivity extends FragmentActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_al_title)
	protected TextView titleTV;

	/**
	 * 当事人类型
	 */
	@ViewById(R.id.nrc_al_type_name)
	protected TextView typeNameTV;

	/** 当事人 删除 按钮 */
	@ViewById(R.id.nrc_al_delete)
	protected Button deleteBtn;
	
	/** 删除按钮所在的父布局*/
	@ViewById(R.id.linearLayout_delete)
	protected LinearLayout linearLayoutDelete;
	
	/** 删除按钮上面的分割线*/
	@ViewById(R.id.halving_line)
	protected View halvingLine;

	/** 当事人 类型 intent Key */
	public static final String K_LITIGANT = "litigant";

	/**
	 * 当事人
	 */
	private TLayyDsr layyDsr;
	
	private LitigantAddBaseFragment currFragment;

	@Bean
	NrcDsrDao dsrDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		Intent intent = getIntent();
		layyDsr = (TLayyDsr) intent.getSerializableExtra(K_LITIGANT);
		if (StringUtils.isBlank(layyDsr.getCId())) {
			layyDsr.setNType(Constants.LITIGANT_TYPE_NATURAL);
		}
		deleteBtn.setVisibility(View.VISIBLE);
		// 原告
		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(layyDsr.getCSsdw())) {
			titleTV.setText("添加原告");
			/** 如果是原告，并且当事人id和网上立案申请人id相同，则不能删除 */
			String layySqrId = NrcEditData.getLayy().getCSqrId();
			if (StringUtils.isNotBlank(layyDsr.getCId()) && layySqrId.equals(layyDsr.getCId())) {
				linearLayoutDelete.setVisibility(View.GONE);
				halvingLine.setVisibility(View.GONE);
			}
		} else { // 被告
			titleTV.setText("添加被告");
		}
		resetFragment(layyDsr.getNType());
		currFragment.setDsr(layyDsr);
		showFragment(currFragment);
		typeNameTV.setText(NrcUtils.getLitigantTypeNameByCode(layyDsr.getNType()));
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
		case Constants.REQUEST_CODE_SELECT_LITIGANT_TYPE:
			int litigantTypeCode = data.getIntExtra(LitigantTypeDioagActivity.K_LITIGANT_TYPE_CODE, Constants.LITIGANT_TYPE_NATURAL);
			boolean success = resetFragment(litigantTypeCode);
			if (success) {
				resetDsr(litigantTypeCode);
				currFragment.setDsr(layyDsr);
				showFragment(currFragment);
				typeNameTV.setText(NrcUtils.getLitigantTypeNameByCode(layyDsr.getNType()));
			}
			break;
			
		case Constants.REQUEST_CODE_DELETE_LITIGANT:
			deleteBtnSure();
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 点击 当事人 返回
	 */
	@Click(R.id.nrc_al_back)
	protected void clickBack() {
		Intent intent = new Intent(this, ConfirmDialogActivity_.class);
		intent.putExtra("message", "数据尚未保存，确定取消？");
		startActivityForResult(intent, NrcConstants.REQ_CODE_CANCEL);
	}

	/**
	 * 点击 当事人 确定
	 */
	@Click(R.id.nrc_al_sure)
	protected void clickSure() {
		currFragment.buildLayyDsr();
		if (checkInputData()) {
			if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(layyDsr.getCSsdw())) {
				List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
				int delPosition = getDelIndex(plaintiffList);
				if (delPosition >= 0) {
					NrcEditData.getPlaintiffList().remove(delPosition);
					NrcEditData.getPlaintiffList().add(delPosition, layyDsr); // 先保存到静态变量中，在立案预约中，点击暂存或者提交，统一保存
				} else {
					NrcEditData.getPlaintiffList().add(layyDsr);
				}
			} else { // 被告
				List<TLayyDsr> defendantList = NrcEditData.getDefendantList();
				int delPosition = getDelIndex(defendantList);
				if (delPosition >= 0) {
					NrcEditData.getDefendantList().remove(delPosition);
					NrcEditData.getDefendantList().add(delPosition, layyDsr); // 先保存到静态变量中，在立案预约中，点击暂存或者提交，统一保存
				} else {
					NrcEditData.getDefendantList().add(layyDsr);
				}
			}
			NrcAddLitigantActivity.this.finish();
		}
	}

	/**
	 * 点击 当事人 类型
	 */
	@Click(R.id.nrc_al_type)
	protected void clickLitigantType() {
		Intent intent = new Intent(this, LitigantTypeDioagActivity_.class);
		intent.putExtra(LitigantTypeDioagActivity.K_LITIGANT_TYPE_CODE, String.valueOf(layyDsr.getNType()));
		startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_LITIGANT_TYPE);
	}

	/**
	 * 点击 当事人 删除
	 */
	@Click(R.id.nrc_al_delete)
	protected void clickDeleteBtn() {
		Intent intent = new Intent(NrcAddLitigantActivity.this, ConfirmOtherDialogActivity_.class);
        intent.putExtra("message", getResources().getString(R.string.text_delete));
        NrcAddLitigantActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_DELETE_LITIGANT);
	}
	
	public void deleteBtnSure(){
		if (StringUtils.isNotBlank(layyDsr.getCId())) {
			if (!deleteCheckRelAgent()) {
				return;
			}
			if (!deleteCheckRelWitness()) {
				return;
			}
            if (!deleteCheckRelIndictment()) {
            	return;
            }			
		}
		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(layyDsr.getCSsdw())) {
			List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
			int position = getDelIndex(plaintiffList);
			if (position >= 0) {
				plaintiffList.remove(position);
			}
		} else { // 被告
			List<TLayyDsr> defendantList = NrcEditData.getDefendantList();
			int position = getDelIndex(defendantList);
			if (position >= 0) {
				defendantList.remove(position);
			}
		}
		NrcAddLitigantActivity.this.finish();
	}

	/**
	 * 重置 当事人 对象的数据
	 * @param newType
	 */
	private void resetDsr(int newType) {
		int oldType = layyDsr.getNType();
		TLayyDsr tempDsr = new TLayyDsr();
		tempDsr.setCId(layyDsr.getCId());
		tempDsr.setCSsdw(layyDsr.getCSsdw());
		tempDsr.setNType(newType);
		tempDsr.setCLayyId(layyDsr.getCLayyId());
		if (Constants.LITIGANT_TYPE_NATURAL == oldType) { //自然人-》法人/非法人组织
			tempDsr.setCFddbr(layyDsr.getCName());
			tempDsr.setCFddbrSjhm(layyDsr.getCSjhm());
			tempDsr.setNIdcardType(layyDsr.getNIdcardType());
			tempDsr.setCIdcard(layyDsr.getCIdcard());
			layyDsr = tempDsr;
		} else {
			if (Constants.LITIGANT_TYPE_NATURAL == newType) { //法人/非法人组织-》自然人
				tempDsr.setCName(layyDsr.getCFddbr());
				tempDsr.setCSjhm(layyDsr.getCFddbrSjhm());
				tempDsr.setNIdcardType(layyDsr.getNIdcardType());
				tempDsr.setCIdcard(layyDsr.getCIdcard());
				String birthdayStr = NrcUtils.getBirthDay();
				tempDsr.setDCsrq(birthdayStr);
				tempDsr.setNXb(Constants.GENDER_MAN);
				layyDsr = tempDsr;
			} else {
				layyDsr.setNType(newType); //法人 与 非法人之前互相转换，不需要改变变量的值
			}
		}
	}

	/**
	 * 根据类型 切换内容区域
	 */
	private boolean resetFragment(int type) {
		boolean success = false;
		switch (type) {
		case Constants.LITIGANT_TYPE_NATURAL:
			boolean isNatural = currFragment instanceof LitigantAddNaturalFragment_;
			if (!isNatural) {
				currFragment = new LitigantAddNaturalFragment_();
				success = true;
			}
			break;

		case Constants.LITIGANT_TYPE_LEGAL:
			boolean isLegal = currFragment instanceof LitigantAddLegalFragment_;
			if (!isLegal) {
				currFragment = new LitigantAddLegalFragment_();
				success = true;
			}
			break;

		case Constants.LITIGANT_TYPE_NON_LEGAL:
			boolean isNonLegal = currFragment instanceof LitigantAddNonLegalFragment_;
			if (!isNonLegal) {
				currFragment = new LitigantAddNonLegalFragment_();
				success = true;
			}
			break;

		default:
			break;
		}

		return success;
	}

	/**
	 * 显示fragment
	 * 
	 * @param fragmentType
	 */
	public void showFragment(Fragment fragment) {
		if (null == fragment) {
			return;
		}
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.nrc_al_content, fragment);
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 获取将要删除的当事人index
	 * 
	 * @param dsrList
	 * @return
	 */
	private int getDelIndex(List<TLayyDsr> dsrList) {
		int position = -1;
		if (dsrList.size() > 0) {
			for (int i = 0; i < dsrList.size(); i++) {
				TLayyDsr dsr = dsrList.get(i);
				if (dsr.getCId().equals(layyDsr.getCId())) {
					position = i;
				}
			}
		}
		return position;
	}

	/**
	 * 检查输入项
	 * 
	 * @return
	 */
	private boolean checkInputData() {
		boolean success = false;
		switch (layyDsr.getNType()) {
		case Constants.LITIGANT_TYPE_NATURAL:
			success = checkNaturalInputData();
			break;

		case Constants.LITIGANT_TYPE_LEGAL:
			success = checkLegalInputData();
			break;

		case Constants.LITIGANT_TYPE_NON_LEGAL:
			success = checkNonLegalInputData();
			break;

		default:
			break;
		}
		return success;
	}

	/**
	 * 检查自然人输入项
	 * 
	 * @return
	 */
	private boolean checkNaturalInputData() {

		if (StringUtils.isBlank(layyDsr.getCName())) {
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(layyDsr.getCSsdw())) {
			if (StringUtils.isBlank(layyDsr.getCIdcard())) {
				String zjlx = NrcUtils.getCertificateNameByCode(layyDsr.getNIdcardType());
				Toast.makeText(this, "请输入"+zjlx+"号码", Toast.LENGTH_SHORT).show();
				return false;
			}

			if (NrcConstants.CERTIFICATE_TYPE_IDCARD == layyDsr.getNIdcardType()) {
				if (StringUtils.isBlank(layyDsr.getDCsrq())) {
					Toast.makeText(this, "请选择出生日期", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			
			if (NrcConstants.CERTIFICATE_TYPE_IDCARD == layyDsr.getNIdcardType()) {//身份证需要校验格式
				if (!IDCard.isValid(layyDsr.getCIdcard())) {
					Toast.makeText(this, "身份证号格式不正确", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			
			if (StringUtils.isBlank(layyDsr.getCSjhm())) {
				Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		if (StringUtils.isNotBlank(layyDsr.getCSjhm()) && !NrcCheckUtils.isMobileNO(layyDsr.getCSjhm())) {
			Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (StringUtils.isBlank(layyDsr.getCAddress())) {
			Toast.makeText(this, "请输入经常居住地", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			String[] addressArray = layyDsr.getCAddress().split(NrcConstants.ADDRESS_SPLIT);
			if (StringUtils.isBlank(addressArray[0])) {
				Toast.makeText(this, "请选择经常居住地：省、市、区", Toast.LENGTH_SHORT).show();
				return false;
			}
			if (addressArray.length > 1 && StringUtils.isBlank(addressArray[1])) {
				if (StringUtils.isBlank(addressArray[0])) {
					Toast.makeText(this, "请输入经常居住地", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 检查法人输入项
	 * 
	 * @return
	 */
	private boolean checkLegalInputData() {

		if (StringUtils.isBlank(layyDsr.getCName())) {
			Toast.makeText(this, "请输入单位名称", Toast.LENGTH_SHORT).show();
			return false;
		}

//		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(layyDsr.getCSsdw())) {
//			if (StringUtils.isBlank(layyDsr.getCLxdh())) {
//				Toast.makeText(this, "请输入联系电话", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//		}

		if (StringUtils.isBlank(layyDsr.getCDwdz())) {
			Toast.makeText(this, "请输入单位地址", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			String[] dwdzArray = layyDsr.getCDwdz().split(NrcConstants.ADDRESS_SPLIT);
			if (StringUtils.isBlank(dwdzArray[0])) {
				Toast.makeText(this, "请选择单位地址：省、市、区", Toast.LENGTH_SHORT).show();
				return false;
			}
			if (dwdzArray.length > 1 && StringUtils.isBlank(dwdzArray[1])) {
				if (StringUtils.isBlank(dwdzArray[0])) {
					Toast.makeText(this, "请输入单位地址", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(layyDsr.getCSsdw())) {
			if (StringUtils.isBlank(layyDsr.getCFddbr())) {
				Toast.makeText(this, "请输入法定代表人名称", Toast.LENGTH_SHORT).show();
				return false;
			}

			if (StringUtils.isBlank(layyDsr.getCFddbrSjhm())) {
				Toast.makeText(this, "请输入法定代表人手机号码", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				if (!NrcCheckUtils.isMobileNO(layyDsr.getCFddbrSjhm())) {
					Toast.makeText(this, "法定代表人手机号码格式不正确", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 检查非法人组织输入项
	 * 
	 * @return
	 */
	private boolean checkNonLegalInputData() {

		if (StringUtils.isBlank(layyDsr.getCName())) {
			Toast.makeText(this, "请输入单位名称", Toast.LENGTH_SHORT).show();
			return false;
		}

//		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(layyDsr.getCSsdw())) {
//			if (StringUtils.isBlank(layyDsr.getCLxdh())) {
//				Toast.makeText(this, "请输入联系电话", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//		}

		if (StringUtils.isBlank(layyDsr.getCDwdz())) {
			Toast.makeText(this, "请输入单位地址", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			String[] dwdzArray = layyDsr.getCDwdz().split(NrcConstants.ADDRESS_SPLIT);
			if (StringUtils.isBlank(dwdzArray[0])) {
				Toast.makeText(this, "请选择单位地址：省、市、区", Toast.LENGTH_SHORT).show();
				return false;
			}
			if (dwdzArray.length > 1 && StringUtils.isBlank(dwdzArray[1])) {
				if (StringUtils.isBlank(dwdzArray[0])) {
					Toast.makeText(this, "请输入单位地址", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}

		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(layyDsr.getCSsdw())) {
			if (StringUtils.isBlank(layyDsr.getCFddbr())) {
				Toast.makeText(this, "请输入主要负责人名称", Toast.LENGTH_SHORT).show();
				return false;
			}

			if (StringUtils.isBlank(layyDsr.getCFddbrSjhm())) {
				Toast.makeText(this, "请输入主要负责人手机号码", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				if (!NrcCheckUtils.isMobileNO(layyDsr.getCFddbrSjhm())) {
					Toast.makeText(this, "主要负责人手机号码格式不正确", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 删除当事人的时候，检查有没有相关联的代理人
	 * 
	 * @return
	 */
	private boolean deleteCheckRelAgent() {
		// 如果是被告，直接返回true，被告不能关联代理人
		if (Constants.LITIGANT_SSDW_DEFENDANT.equals(layyDsr.getCSsdw())) {
			return true;
		}
		List<TLayyDlr> agentList = NrcEditData.getAgentList(); // 检查是否有相关联的代理人
		for (TLayyDlr dlr : agentList) {
			String bdlrIds = dlr.getCBdlrId();
			if (StringUtils.isNotBlank(bdlrIds)) {
				String[] bdlrIdArray = bdlrIds.split(NrcConstants.REL_NAME_SPLIT);
				if (null != bdlrIdArray && bdlrIdArray.length > 0) {
					for (int i = 0; i < bdlrIdArray.length; i++) {
						String bdlrId = bdlrIdArray[i];
						if (layyDsr.getCId().equals(bdlrId)) {
							StringBuffer toastMsg = new StringBuffer("");
							toastMsg.append("原告：").append(layyDsr.getCName());
							toastMsg.append("，已关联代理人：").append(dlr.getCName());
							toastMsg.append("，\n请解除绑定，或删除相关代理人");
							Toast.makeText(this, toastMsg.toString(), Toast.LENGTH_SHORT).show();
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 删除当事人的时候，检查有没有相关联的证人
	 * 
	 * @return
	 */
	private boolean deleteCheckRelWitness() {
		// 如果是被告，直接返回true，被告不能关联代理人
		if (Constants.LITIGANT_SSDW_DEFENDANT.equals(layyDsr.getCSsdw())) {
			return true;
		}
		List<TLayyZr> witnessList = NrcEditData.getWitnessList();
		for (TLayyZr zr : witnessList) {
			if (layyDsr.getCId().equals(zr.getCYlfId())) {
				StringBuffer toastMsg = new StringBuffer("");
				toastMsg.append("原告：").append(layyDsr.getCName());
				toastMsg.append("，已关联证人：").append(zr.getCName());
				toastMsg.append("，\n请解除关联，或删除相关证人");
				Toast.makeText(this, toastMsg.toString(), Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	/**
	 * 删除当事人的时候，检查有没有相关联的起诉状
	 * 
	 * @return
	 */
	private boolean deleteCheckRelIndictment() {
		List<TLayySscl> indictmentList = NrcEditData.getIndictmentList();
		for (TLayySscl sscl : indictmentList) {
			List<TLayyDsrSscl> dsrSsclList = NrcEditData.getDsrIndictmentListMap().get(sscl.getCId());
			if (null != dsrSsclList && dsrSsclList.size() > 0) {
				for (TLayyDsrSscl dsrSscl : dsrSsclList) {
					if (layyDsr.getCId().equals(dsrSscl.getCDsrId())) {
						StringBuffer toastMsg = new StringBuffer("");
						toastMsg.append(layyDsr.getCSsdw()).append("：").append(layyDsr.getCName());
						toastMsg.append("，已关联起诉状：").append(sscl.getCName());
						toastMsg.append("，\n请解除关联，或删除相关诉讼材料");
						Toast.makeText(this, toastMsg.toString(), Toast.LENGTH_SHORT).show();
						return false;
					}
				}
			}
		}
		return true;
	}
}
