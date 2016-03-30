package com.thunisoft.sswy.mobile.activity.nrc;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案查看界面的代理人和证人界面
 * 
 */

@EActivity(R.layout.activity_nrc_review_single_data)
public class NRCReviewSingleDataActivity extends BaseActivity{
	
	private static final String TAG = "NRCReviewSingleDataActivity";
	
    @ViewById(R.id.name)
    TextView nameTV;
    
    @ViewById(R.id.sex)
    TextView sexTV;
    
    @ViewById(R.id.occupation)
    TextView occupationTV;
    
    @ViewById(R.id.cardInfo)
    LinearLayout cardInfo;
    
    @ViewById(R.id.card)
    TextView cardTV;
    
    @ViewById(R.id.phoneInfo)
    LinearLayout phoneInfo;
    
    @ViewById(R.id.phone)
    TextView phoneTV;
    
    @ViewById(R.id.addressInfo)
    LinearLayout addressInfo;
    
    @ViewById(R.id.address)
    TextView addressTV;
    
    @ViewById(R.id.other_name_Info)
    LinearLayout otherNameInfo;
    
    @ViewById(R.id.other_name)
    TextView other_nameTV;
    
    @ViewById(R.id.Lawyer_info)
    LinearLayout Lawyer_info;
    
    @ViewById(R.id.Lawyer_card_Info)
    LinearLayout LawyerCardInfo;
    
    @ViewById(R.id.Lawyer_card)
    TextView Lawyer_cardTV;
    
    @ViewById(R.id.company_name_Info)
    LinearLayout companyNameInfo;
    
    @ViewById(R.id.company_name)
    TextView company_nameTV;
    
    @ViewById(R.id.lawyer_other_name_Info)
    LinearLayout lawyerOtherNameInfo;
    
    @ViewById(R.id.lawyer_other_name)
    TextView lawyer_other_nameTV;
    
    
    private TLayyDlr agent;
    
    private TLayyZr witness;
    
    private int type = 1;
	
	/**
	 * 登录信息
	 */
	@Bean
    LoginCache loginCache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		type = intent.getIntExtra("single_data_type", 1);
		if(type == 1){
			agent = (TLayyDlr)intent.getSerializableExtra("agent");			
		}else{
			witness = (TLayyZr)intent.getSerializableExtra("witness");			
		}
	}
	
	@AfterViews
	protected void onAfterView() {
		if(type == 2){
			setTitle("证人");			
			Lawyer_info.setVisibility(View.GONE);
			nameTV.setText(witness.getCName());
			sexTV.setText(NrcUtils.getGenderNameByCode(witness.getNXb()));
			occupationTV.setVisibility(View.GONE);
			cardInfo.setVisibility(View.GONE);
			if(witness.getCSjhm().equals("")){
				phoneInfo.setVisibility(View.GONE);
			}else{
				phoneTV.setText(witness.getCSjhm());				
			}
			if(witness.getCAddress().equals("")){
				addressInfo.setVisibility(View.GONE);
			}else{			
				addressTV.setText(witness.getCAddress());
			}
			if(witness.getCYlfMc().equals("")){
				otherNameInfo.setVisibility(View.GONE);
			}else{
				other_nameTV.setText("有利方："+witness.getCYlfMc());				
			}
		}else{
			setTitle("代理人");	
			nameTV.setText(agent.getCName());
			sexTV.setVisibility(View.GONE);
			otherNameInfo.setVisibility(View.GONE);
			occupationTV.setText(NrcUtils.getAgentNameByCode(agent.getNDlrType()));
			addressInfo.setVisibility(View.GONE);
			cardTV.setText(NrcUtils.getCertificateNameByCode(agent.getNIdcardType())+"："+agent.getCIdcard());
			phoneTV.setText(agent.getCSjhm());
			Lawyer_info.setVisibility(View.VISIBLE);
			if(agent.getCBdlrMc().equals("")){
				lawyerOtherNameInfo.setVisibility(View.GONE);
			}else{
				lawyer_other_nameTV.setText("被代理人："+agent.getCBdlrMc());				
			}
			if(agent.getCZyzh().equals("")){
				LawyerCardInfo.setVisibility(View.GONE);
			}else{
				Lawyer_cardTV.setText("职业证号："+agent.getCZyzh());
			}
			if(agent.getCSzdw().equals("")){
				companyNameInfo.setVisibility(View.GONE);
			}else{
				company_nameTV.setText(agent.getCSzdw());
			}
		}
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
