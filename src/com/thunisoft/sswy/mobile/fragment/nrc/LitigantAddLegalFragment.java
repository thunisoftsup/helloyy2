package com.thunisoft.sswy.mobile.fragment.nrc;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.SelectAddressActivity;
import com.thunisoft.sswy.mobile.activity.SelectAddressActivity_;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 当事人：添加法人
 * 
 * @author gewx
 *
 */
@EFragment(R.layout.fragment_nrc_add_litigant_legal)
public class LitigantAddLegalFragment extends LitigantAddBaseFragment {

	/** 单位 名称 */
	@ViewById(R.id.nrc_all_corp_name)
	protected EditText corpNameET;

	/** 单位 联系电话 */
	@ViewById(R.id.nrc_all_corp_tel)
	protected EditText corpTelET;

	/** 单位 地址 省、市、区 */
	@ViewById(R.id.nrc_all_corp_address)
	protected TextView corpAddressTV;

	/** 单位 地址 详细地址 */
	@ViewById(R.id.nrc_all_corp_address_detail)
	protected EditText corpAddressDetailET;

	/** 法人代表 姓名 */
	@ViewById(R.id.nrc_all_legal_name)
	protected EditText legalNameET;

	/** 法人代表 电话 */
	@ViewById(R.id.nrc_all_legal_tel)
	protected EditText legalTelET;

	private Activity activity;

	/**
	 * 当事人：法人
	 */
	private TLayyDsr legalDsr;

	@AfterViews
	protected void onAfterViews() {
		activity = getActivity();
		legalDsr = getDsr();
		if (StringUtils.isNotBlank(legalDsr.getCId())) {
			corpNameET.setText(legalDsr.getCName());
			corpTelET.setText(legalDsr.getCLxdh());
			String dwdzId = legalDsr.getCDwdzId();
			if (StringUtils.isNotBlank(dwdzId)) {
				String dwdz = NrcUtils.getAddress(legalDsr.getCDwdz(), dwdzId);
				String dwdzDetail = NrcUtils.getAddressDetail(legalDsr.getCDwdz(), dwdzId);
				corpAddressTV.setText(dwdz);
				corpAddressDetailET.setText(dwdzDetail);
			}
			legalNameET.setText(legalDsr.getCFddbr());
			legalTelET.setText(legalDsr.getCFddbrSjhm());
		} else {
			legalDsr.setCId(UUIDHelper.getUuid());
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Constants.RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case Constants.REQUEST_CODE_SELECT_ADDRESS:
			String addressId = data.getStringExtra(SelectAddressActivity.K_SELECTED_ADDRESS_ID);
			String address = data.getStringExtra(SelectAddressActivity.K_SELECTED_ADDRESS);
			corpAddressTV.setText(address);
			legalDsr.setCDwdzId(addressId);
			break;

		default:
			break;
		}
	}

	/**
	 * 封装 当事人（自然人）
	 */
	@Override
	public void buildLayyDsr() {
		String gzdw = corpNameET.getText().toString().trim();
		String gzdwTel = corpTelET.getText().toString();
		String address = corpAddressTV.getText().toString();
		String addressDetail = corpAddressDetailET.getText().toString().trim();
		String legalName = legalNameET.getText().toString().trim();
		String legalTel = legalTelET.getText().toString().trim();
		legalDsr.setCName(gzdw);
		legalDsr.setCLxdh(gzdwTel);
		if (StringUtils.isNotBlank(addressDetail)) {
			legalDsr.setCDwdz(address + NrcConstants.ADDRESS_SPLIT + addressDetail);
		} else {
			legalDsr.setCDwdz(address);
		}
		legalDsr.setCFddbr(legalName);
		legalDsr.setCFddbrSjhm(legalTel);
	}

	/**
	 * 点击 当事人 省、市、区
	 */
	@Click(R.id.nrc_all_corp_address)
	protected void clickAddress() {
		Intent intent = new Intent(activity, SelectAddressActivity_.class);
		intent.putExtra(SelectAddressActivity.K_SELECTED_ADDRESS_ID, legalDsr.getCAddressId());
		startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_ADDRESS);
	}

	/**
	 * 获取 当事人：法人
	 * 
	 * @return naturalDsr
	 */

	public TLayyDsr getNaturalDsr() {
		return legalDsr;
	}

	/**
	 * 设置 当事人：自然人
	 * 
	 * @param naturalDsr
	 */
	public void setNaturalDsr(TLayyDsr naturalFr) {
		this.legalDsr = naturalFr;
	}
}
