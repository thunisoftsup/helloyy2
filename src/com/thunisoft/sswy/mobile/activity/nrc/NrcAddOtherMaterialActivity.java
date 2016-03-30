package com.thunisoft.sswy.mobile.activity.nrc;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.os.Bundle;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;

/**
 * 网上立案 添加其它材料
 * 
 */

@EActivity(R.layout.activity_nrc_add_other_material)
public class NrcAddOtherMaterialActivity extends BaseActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@AfterViews
	protected void onAfterView() {
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Click(R.id.nrc_aom_back)
	protected void clickBack() {
		NrcAddOtherMaterialActivity.this.finish();
	}
}
