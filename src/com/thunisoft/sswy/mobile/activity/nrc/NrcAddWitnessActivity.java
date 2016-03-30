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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmOtherDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.GenderDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.nrc.GenderDialogActivity_;
import com.thunisoft.sswy.mobile.datasource.NrcZrDao;
import com.thunisoft.sswy.mobile.datasource.RegionDao;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcCheckUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案 添加证人
 * 
 */

@EActivity(R.layout.activity_nrc_add_witness)
public class NrcAddWitnessActivity extends BaseActivity{
	
	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_aw_title)
	protected TextView titleTV;
	
	/**证人 姓名*/
	@ViewById(R.id.nrc_aw_name)
	protected EditText nameET;
	
	/**证人 性别*/
	@ViewById(R.id.nrc_aw_gender)
	protected TextView genderTV;
	
	/**证人 手机号*/
	@ViewById(R.id.nrc_aw_tel)
	protected EditText telET;
	
	/**证人 住所*/
	@ViewById(R.id.nrc_aw_zrzs)
	protected EditText zrzsET;
	
	/**证人 有利方姓名拼串*/
	@ViewById(R.id.nrc_aw_avail_names)
	protected TextView availNamesTV;
	
	/**证人 删除 按钮*/
	@ViewById(R.id.nrc_aw_delete)
	protected Button deleteBtn;
	
	/**证人 intent key*/
	public static final String K_WITNESS = "witness";
	
	/** 关联 有利方 */
	private static final int REQ_CODE_REL_AVAIL = 5;
	
	private TLayyZr layyZr;
	
	@Bean
	NrcZrDao nrcZrDao;
	
	@Bean
	RegionDao regionDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		layyZr = (TLayyZr)intent.getSerializableExtra(K_WITNESS);
	}
	
	@AfterViews
	protected void onAfterView() {
		if (StringUtils.isBlank(layyZr.getCId())) {
			layyZr.setNXb(Constants.GENDER_MAN);
			layyZr.setCId(UUIDHelper.getUuid());
		} else {
			nameET.setText(layyZr.getCName());
			telET.setText(layyZr.getCSjhm());
			zrzsET.setText(layyZr.getCZsd());
			availNamesTV.setText(layyZr.getCYlfMc());
		}
		genderTV.setText(NrcUtils.getGenderNameByCode(layyZr.getNXb()));
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
		case Constants.REQUEST_CODE_SELECT_GENDER_TYPE:
			int gender = data.getIntExtra(GenderDialogActivity.K_GENDER_TYPE, Constants.GENDER_MAN);
			layyZr.setNXb(gender);
			genderTV.setText(NrcUtils.getGenderNameByCode(layyZr.getNXb()));
			break;

//		case Constants.REQUEST_CODE_SELECT_ADDRESS:
//			String addressId = data.getStringExtra(SelectAddressActivity.K_SELECTED_ADDRESS_ID);
//			String address = data.getStringExtra(SelectAddressActivity.K_SELECTED_ADDRESS);
//			addressTV.setText(address);
//			layyZr.setCAddressId(addressId);
//			break;
			
		case REQ_CODE_REL_AVAIL:
			layyZr = (TLayyZr)data.getSerializableExtra(NrcRelAvailActivity.K_WITNESS);
			availNamesTV.setText(layyZr.getCYlfMc());
			break;
			
		case Constants.REQUEST_CODE_DELETE_WITNESS:
			deleteBtnSure();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 点击  证人 返回
	 */
	@Click(R.id.nrc_aw_back)
	protected void clickBack() {
		Intent intent = new Intent(this, ConfirmDialogActivity_.class);
		intent.putExtra("message", "数据尚未保存，确定取消？");
		startActivityForResult(intent, NrcConstants.REQ_CODE_CANCEL);
	}
	/**
	 * 点击  证人 确定
	 */
	@Click(R.id.nrc_aw_sure)
	protected void clickSure() {
		if (checkWitnessData()) {
			List<TLayyZr> layyZrList = NrcEditData.getWitnessList();
			int delPosition = getDelPosition(layyZrList);
			if (delPosition >= 0) {
				layyZrList.remove(delPosition);
				layyZrList.add(delPosition, layyZr); // 先保存到静态变量中，在立案预约中，点击暂存或者提交，统一保存
			} else {
				layyZrList.add(layyZr); // 先保存到静态变量中，在立案预约中，点击暂存或者提交，统一保存
			}
			
			NrcAddWitnessActivity.this.finish();
		}
	}
	/**
	 * 点击  证人 性别
	 */
	@Click(R.id.nrc_aw_gender)
	protected void clickGender() {
		Intent intent = new Intent(this, GenderDialogActivity_.class);
        intent.putExtra(GenderDialogActivity.K_GENDER_TYPE, layyZr.getNXb());
        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_GENDER_TYPE);
	}
	
//	/**
//	 * 点击  证人 省、市、区
//	 */
//	@Click(R.id.nrc_aw_address)
//	protected void clickAddress() {
//		Intent intent = new Intent(this, SelectAddressActivity_.class);
//		intent.putExtra(SelectAddressActivity.K_SELECTED_ADDRESS_ID, layyZr.getCAddressId());
//        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_ADDRESS);
//	}
	
	/**
	 * 点击  有利方
	 */
	@Click(R.id.nrc_aw_avail)
	protected void clickAvail() {
		Intent intent = new Intent();
		intent.setClass(this, NrcRelAvailActivity_.class);
		intent.putExtra(NrcRelAvailActivity.K_WITNESS, layyZr);
		intent.putExtra(NrcRelAvailActivity.K_LITIGANT_SSDW, "原告");
		startActivityForResult(intent, REQ_CODE_REL_AVAIL);
	}
	
	/**
	 * 点击  证人 删除
	 */
	@Click(R.id.nrc_aw_delete)
	protected void clickDeleteBtn() {
		Intent intent = new Intent(NrcAddWitnessActivity.this, ConfirmOtherDialogActivity_.class);
        intent.putExtra("message", getResources().getString(R.string.text_delete));
        NrcAddWitnessActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_DELETE_WITNESS);
	}
	
	public void deleteBtnSure(){
		List<TLayyZr> witnessList = NrcEditData.getWitnessList();
		int position = getDelPosition(witnessList);
		if (position >= 0) {
			witnessList.remove(position);
		}
		NrcAddWitnessActivity.this.finish();
	}
	
	/**
	 * 检查证人输入项
	 * @return
	 */
	private boolean checkWitnessData() {
		String name = nameET.getText().toString().trim();
		String tel = telET.getText().toString().trim();
		String zsd = zrzsET.getText().toString();
		layyZr.setCName(name);
		layyZr.setCSjhm(tel);
		layyZr.setCZsd(zsd);
        if (StringUtils.isBlank(layyZr.getCName())) {
        	Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
        	return false;
        }
        if (StringUtils.isNotBlank(layyZr.getCSjhm())
        		&& !NrcCheckUtils.isMobileNO(layyZr.getCSjhm())) {
        	Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        	return false;
        }
//        if (StringUtils.isBlank(layyZr.getCAddress())) {
//			Toast.makeText(this, "请输入经常居住地", Toast.LENGTH_SHORT).show();
//			return false;
//		} else {
//			String[] addressArray = layyZr.getCAddressId().split(NrcConstants.ADDRESS_SPLIT);
//			if (StringUtils.isBlank(addressArray[0])) {
//				Toast.makeText(this, "请选择经常居住地：省、市、区", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//			
//			if (addressArray.length > 1 && StringUtils.isBlank(addressArray[1])) {
//				if (StringUtils.isBlank(addressArray[0])) {
//					Toast.makeText(this, "请输入经常居住地", Toast.LENGTH_SHORT).show();
//					return false;
//				}
//			}
//		}
//        
//		if (StringUtils.isBlank(layyZr.getCYlfId())) {
//			Toast.makeText(this, "请关联有利方", Toast.LENGTH_SHORT).show();
//        	return false;
//		}
		return true;
	}
	
	private int getDelPosition(List<TLayyZr> layyZrList) {
		int delPosition = -1;
		if (null != layyZrList && layyZrList.size() > 0) {
			for (int i=0; i<layyZrList.size(); i++) {
				TLayyZr zr = layyZrList.get(i);
				if (zr.getCId().equals(layyZr.getCId())) {
					delPosition = i;
					break;
				}
			}
		}
		return delPosition;
	}
}
