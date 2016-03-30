package com.thunisoft.sswy.mobile.fragment.nrc;

import java.util.Calendar;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.SelectAddressActivity;
import com.thunisoft.sswy.mobile.activity.SelectAddressActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.DateDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.DateDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ZjlxDioagActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ZjlxDioagActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.GenderDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.GenderDialogActivity_;
import com.thunisoft.sswy.mobile.adapter.CertTypeAdapter;
import com.thunisoft.sswy.mobile.datasource.NrcBasicDao;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.IDCard;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 当事人：添加自然人
 * 
 * @author gewx
 *
 */
@EFragment(R.layout.fragment_nrc_add_litigant_natural)
public class LitigantAddNaturalFragment extends LitigantAddBaseFragment {

	/** 当事人 姓名 */
	@ViewById(R.id.nrc_aln_name)
	protected EditText nameET;

	/** 当事人 性别 */
	@ViewById(R.id.nrc_aln_gender)
	protected TextView genderTV;

	/** 当事人 证件类型名称 */
	@ViewById(R.id.nrc_aln_certificate_type)
	protected LinearLayout certificateTypeLinLayout;

	/** 当事人 证件类型名称 */
	@ViewById(R.id.nrc_aln_certificate_type_name)
	protected TextView certificateTypeNameTV;

	/** 当事人 证件号码 */
	@ViewById(R.id.nrc_aln_certificate_num)
	protected EditText certificateNumET;

	/** 当事人 出生日期 */
	@ViewById(R.id.nrc_aln_birthday)
	protected TextView birthdayTV;

	/** 当事人 手机号 */
	@ViewById(R.id.nrc_aln_tel)
	protected EditText telET;

	/** 当事人 住址 省、市、区 */
	@ViewById(R.id.nrc_aln_address)
	protected TextView addressTV;

	/** 当事人 住址 详细地址 */
	@ViewById(R.id.nrc_aln_address_detail)
	protected EditText addressDetailET;

	private Activity activity;

	/**
	 * 当事人：自然人
	 */
	private TLayyDsr naturalDsr;

	@Bean
	NrcBasicDao basicDao;

	@SuppressLint("SimpleDateFormat")
	@AfterViews
	protected void onAfterViews() {
		activity = getActivity();
		naturalDsr = getDsr();
		if (StringUtils.isNotBlank(naturalDsr.getCId())) {
			nameET.setText(naturalDsr.getCName());
			certificateNumET.setText(naturalDsr.getCIdcard());
			telET.setText(naturalDsr.getCSjhm());
			String addressId = naturalDsr.getCAddressId();
			if (StringUtils.isNotBlank(addressId)) {
				String address = NrcUtils.getAddress(naturalDsr.getCAddress(), addressId);
				String addressDetail = NrcUtils.getAddressDetail(naturalDsr.getCAddress(), addressId);
				addressTV.setText(address);
				addressDetailET.setText(addressDetail);
			}
		} else {
			naturalDsr.setCId(UUIDHelper.getUuid());
			String birthdayStr = NrcUtils.getBirthDay();
			naturalDsr.setDCsrq(birthdayStr);
			naturalDsr.setNXb(Constants.GENDER_MAN);
			naturalDsr.setNIdcardType(Constants.CERTIFICATE_TYPE_ID_CARD);
		}
		if (null == naturalDsr.getNIdcardType()) {
			naturalDsr.setNIdcardType(Constants.CERTIFICATE_TYPE_ID_CARD);
		}
		setCertNumETInputType(naturalDsr.getNIdcardType());
		genderTV.setText(NrcUtils.getGenderNameByCode(naturalDsr.getNXb()));
		certificateTypeNameTV.setText(NrcUtils.getCertificateNameByCode(naturalDsr.getNIdcardType()));
		String birthDayDate = NrcUtils.getFormatDate(naturalDsr.getDCsrq());
		birthdayTV.setText(birthDayDate);
		setEditEnable();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Constants.RESULT_OK != resultCode) {
			return;
		}

		switch (requestCode) {
		case Constants.REQUEST_CODE_SELECT_GENDER_TYPE:
			int gender = data.getIntExtra(GenderDialogActivity.K_GENDER_TYPE, Constants.GENDER_MAN);
			naturalDsr.setNXb(gender);
			genderTV.setText(NrcUtils.getGenderNameByCode(naturalDsr.getNXb()));
			break;

		case Constants.REQUEST_CODE_XZZJ:
			String certCodeStr = data.getStringExtra(CertTypeAdapter.K_SELECTED_CODE);
			int certCode = Integer.parseInt(certCodeStr);
			if (naturalDsr.getNIdcardType() != certCode) {
				naturalDsr.setNIdcardType(certCode);
				certificateNumET.getText().clear();
				String certName = NrcUtils.getCertificateNameByCode(certCode);
				certificateTypeNameTV.setText(certName);
				setCertNumETInputType(certCode);
			}
			break;

		case Constants.REQUEST_CODE_SELECT_DATE:
			Calendar cal = (Calendar) data.getSerializableExtra(DateDialogActivity.K_DATE);
			String birthdayStr = NrcUtils.formatDate(cal.getTime(), NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);
			naturalDsr.setDCsrq(birthdayStr);
			String birthDayDate = NrcUtils.getFormatDate(naturalDsr.getDCsrq());
			birthdayTV.setText(birthDayDate);
			break;

		case Constants.REQUEST_CODE_SELECT_ADDRESS:
			String address = data.getStringExtra(SelectAddressActivity.K_SELECTED_ADDRESS);
			String addressId = data.getStringExtra(SelectAddressActivity.K_SELECTED_ADDRESS_ID);
			addressTV.setText(address);
			naturalDsr.setCAddressId(addressId);
			break;

		default:
			break;
		}
	}

	/**
	 * 点击 当事人 性别
	 */
	@Click(R.id.nrc_aln_gender)
	protected void clickGender() {
		Intent intent = new Intent(activity, GenderDialogActivity_.class);
		intent.putExtra(GenderDialogActivity.K_GENDER_TYPE, naturalDsr.getNXb());
		startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_GENDER_TYPE);
	}

	/**
	 * 点击 当事人 证件类型
	 */
	@Click(R.id.nrc_aln_certificate_type)
	protected void clickCertificateType() {
		Intent intent = new Intent(activity, ZjlxDioagActivity_.class);
		intent.putExtra(ZjlxDioagActivity.K_CERT_CODE, String.valueOf(naturalDsr.getNIdcardType()));
		startActivityForResult(intent, Constants.REQUEST_CODE_XZZJ);
	}

	/**
	 * 点击 当事人 出生日期
	 */
	@Click(R.id.nrc_aln_birthday)
	protected void clickBirthday() {
		String birthdayStr = naturalDsr.getDCsrq();
		if (StringUtils.isBlank(birthdayStr)) {
			birthdayStr = NrcUtils.getBirthDay();
		}
		Date birthdayTime = NrcUtils.string2Date(birthdayStr, NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);
		Calendar cal = Calendar.getInstance();
		cal.setTime(birthdayTime);
		Intent intent = new Intent(activity, DateDialogActivity_.class);
		intent.putExtra(DateDialogActivity.K_DATE, cal);
		startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_DATE);
	}

	/**
	 * 点击 当事人 省、市、区
	 */
	@Click(R.id.nrc_aln_address)
	protected void clickAddress() {
		Intent intent = new Intent(activity, SelectAddressActivity_.class);
		intent.putExtra(SelectAddressActivity.K_SELECTED_ADDRESS_ID, naturalDsr.getCAddressId());
		startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_ADDRESS);
	}

	/**
	 * 证件号码输入时监听
	 */
	@TextChange(R.id.nrc_aln_certificate_num)
	protected void certificateTextChange() {
		// 如果证件类型为身份证，需要根据输入的身份证号，获取出生日期
		if (Integer.valueOf(Constants.CERTIFICATE_TYPE_ID_CARD).equals(naturalDsr.getNIdcardType())) {
			String certificateNum = certificateNumET.getText().toString();
			if (IDCard.isValid(certificateNum)) {
				String birthdayTime = IDCard.getBirthDay(certificateNum);
				String birthdayDate = birthdayTime.split(" ")[0];
				birthdayTV.setText(birthdayDate);
				naturalDsr.setDCsrq(birthdayTime);
			}
		}
	}

	/**
	 * 设置是否允许编辑
	 */
	private void setEditEnable() {
		/** 如果是原告，当事人id和网上立案中申请人id相同时，不允许编辑 姓名、证件、手机号码 */
		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(naturalDsr.getCSsdw())) {
			String sqrId = NrcEditData.getLayy().getCSqrId();
			if (sqrId.equals(naturalDsr.getCId())) {
				nameET.setEnabled(false);
				certificateTypeLinLayout.setClickable(false);
				certificateNumET.setEnabled(false);
				telET.setEnabled(false);
			}
		}
	}

	/**
	 * 封装 当事人（自然人）
	 */
	@Override
	public void buildLayyDsr() {
		String name = nameET.getText().toString().trim();
		String certificateNum = certificateNumET.getText().toString().trim();
		String tel = telET.getText().toString().trim();
		String address = addressTV.getText().toString();
		String addressDetail = addressDetailET.getText().toString().trim();
		naturalDsr.setCName(name);
		naturalDsr.setCIdcard(certificateNum);
		naturalDsr.setCSjhm(tel);
		if (StringUtils.isNotBlank(addressDetail)) {
			naturalDsr.setCAddress(address + NrcConstants.ADDRESS_SPLIT + addressDetail);
		} else {
			naturalDsr.setCAddress(address);
		}
	}

	/**
	 * 为 证件的输入框，增加输入类型的显示
	 * 
	 * @param certificateType
	 */
	private void setCertNumETInputType(int certificateType) {
		if (Constants.CERTIFICATE_TYPE_ID_CARD != certificateType) {
			certificateNumET.setFilters(new InputFilter[] { new InputFilter.LengthFilter(NrcConstants.CERTIFICATE_LENGTH_OTHER) });
			certificateNumET.setInputType(InputType.TYPE_CLASS_TEXT);
		} else {
			certificateNumET.setFilters(new InputFilter[] { new InputFilter.LengthFilter(NrcConstants.CERTIFICATE_LENGTH_ID_CARD) });
			String digits = getString(R.string.id_card_regex);
			certificateNumET.setKeyListener(DigitsKeyListener.getInstance(digits));
		}
	}

	/**
	 * 获取 当事人：自然人
	 * 
	 * @return naturalDsr
	 */

	public TLayyDsr getNaturalDsr() {
		return naturalDsr;
	}

	/**
	 * 设置 当事人：自然人
	 * 
	 * @param naturalDsr
	 */
	public void setNaturalDsr(TLayyDsr naturalDsr) {
		this.naturalDsr = naturalDsr;
	}

}
