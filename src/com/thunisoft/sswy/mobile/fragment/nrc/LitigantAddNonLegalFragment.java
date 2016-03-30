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
 * 当事人：添加非法人组织
 * @author gewx
 *
 */
@EFragment(R.layout.fragment_nrc_add_litigant_non_legal)
public class LitigantAddNonLegalFragment extends LitigantAddBaseFragment {

	/**单位 名称*/
	@ViewById(R.id.nrc_alnl_corp_name)
	protected EditText corpNameET;
	
	/**单位 联系电话*/
	@ViewById(R.id.nrc_alnl_corp_tel)
	protected EditText corpTelET;
	
	/**单位 地址 省、市、区*/
	@ViewById(R.id.nrc_alnl_corp_address)
	protected TextView corpAddressTV;
	
	/**单位 地址 详细地址*/
	@ViewById(R.id.nrc_alnl_corp_address_detail)
	protected EditText corpAddressDetailET;
	
	/**主要负责人 姓名*/
	@ViewById(R.id.nrc_alnl_leader_name)
	protected EditText leaderNameET;
	
	/**主要负责人 手机号码*/
	@ViewById(R.id.nrc_alnl_leader_tel)
	protected EditText leaderTelET;
	
	private Activity activity;
	
	/**
	 * 当事人：非法人组织
	 */
	private TLayyDsr nlegalDsr;
	
	@AfterViews
	protected void onAfterViews() {
		activity = getActivity();
		nlegalDsr = getDsr();
		if (StringUtils.isNotBlank(nlegalDsr.getCId())) {
			corpNameET.setText(nlegalDsr.getCName());
			corpTelET.setText(nlegalDsr.getCLxdh());
			String dwdzId = nlegalDsr.getCDwdzId();
			if (StringUtils.isNotBlank(dwdzId)) {
				String dwdz = NrcUtils.getAddress(nlegalDsr.getCDwdz(), dwdzId);
				String dwdzDetail = NrcUtils.getAddressDetail(nlegalDsr.getCDwdz(), dwdzId);
				corpAddressTV.setText(dwdz);
				corpAddressDetailET.setText(dwdzDetail);
			}
			leaderNameET.setText(nlegalDsr.getCFddbr());
			leaderTelET.setText(nlegalDsr.getCFddbrSjhm());
		} else {
			nlegalDsr.setCId(UUIDHelper.getUuid());
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (Constants.RESULT_OK != resultCode) {
			return;
		}
		switch(requestCode) {
			case Constants.REQUEST_CODE_SELECT_ADDRESS:
				String address = data.getStringExtra(SelectAddressActivity.K_SELECTED_ADDRESS);
				String addressId = data.getStringExtra(SelectAddressActivity.K_SELECTED_ADDRESS_ID);
				corpAddressTV.setText(address);
				nlegalDsr.setCDwdzId(addressId);
				break;
	
			default:
				break;
		}
	}

	/**
	 * 点击  当事人 省、市、区
	 */
	@Click(R.id.nrc_alnl_corp_address)
	protected void clickAddress() {
		Intent intent = new Intent(activity, SelectAddressActivity_.class);
		intent.putExtra(SelectAddressActivity.K_SELECTED_ADDRESS_ID, nlegalDsr.getCAddressId());
        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_ADDRESS);
	}
	
	/**
	 * 封装 当事人（非法人组织）
	 */
	public void buildLayyDsr() {
		String gzdw = corpNameET.getText().toString().trim();
		String gzdwTel = corpTelET.getText().toString();
		String address = corpAddressTV.getText().toString();
		String addressDetail = corpAddressDetailET.getText().toString().trim();
		String leaderName = leaderNameET.getText().toString().trim();
		String leaderTel = leaderTelET.getText().toString().trim();
		nlegalDsr.setCName(gzdw);
		nlegalDsr.setCLxdh(gzdwTel);
		if (StringUtils.isNotBlank(addressDetail)) {
			nlegalDsr.setCDwdz(address + NrcConstants.ADDRESS_SPLIT + addressDetail);
		} else {
			nlegalDsr.setCDwdz(address);
		}
		nlegalDsr.setCFddbr(leaderName);
		nlegalDsr.setCFddbrSjhm(leaderTel);
	}
	
	/**  
	 * 获取  当事人：非法人组织
	 * @return naturalNfr  
	 */
	
	public TLayyDsr getNaturalDsr() {
		return nlegalDsr;
	}

	/**  
	 * 设置  当事人：非法人组织
	 * @param naturalNfr
	 */
	public void setNaturalDsr(TLayyDsr naturalNfr) {
		this.nlegalDsr = naturalNfr;
	}
}
