package com.thunisoft.sswy.mobile.activity.nrc;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.adapter.NRCReviewCheckAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayySh;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 网上立案查看界面的审核列表
 * 
 */

@EActivity(R.layout.activity_nrc_review_check)
public class NRCReviewCheckActivity extends BaseActivity{
	

	private static final String TAG = "NRCReviewCheckActivity";
	
    @ViewById(R.id.check_list)
    ListView checkListView;
    
    NRCReviewCheckAdapter reviewCheckAdapter;
    
    List<TLayySh> checkList = new ArrayList<TLayySh>();
	
	/**
	 * 登录信息
	 */
	@Bean
    LoginCache loginCache;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		checkList = (List<TLayySh>) intent.getSerializableExtra("shList");
	}
	
	@AfterViews
	protected void onAfterView() {
		setTitle("审核情况");
		reviewCheckAdapter = new NRCReviewCheckAdapter(this,checkList);
		checkListView.setAdapter(reviewCheckAdapter);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	/**
	 * 点击 返回
	 */
	@Click(R.id.btn_back)
	protected void clickBack() {
		this.finish();
	}
}
