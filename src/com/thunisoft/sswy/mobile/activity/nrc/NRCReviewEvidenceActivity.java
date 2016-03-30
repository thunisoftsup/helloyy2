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
import android.widget.ListView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.adapter.NRCReviewEvidenceAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.interfaces.IWaitingDialogNotifier;
import com.thunisoft.sswy.mobile.logic.net.NRCReviewResponseUtil;
import com.thunisoft.sswy.mobile.model.TLayyZjInfo;

/**
 * 网上立案查看界面的证据列表
 * 
 */

@EActivity(R.layout.activity_nrc_review_evidence)
public class NRCReviewEvidenceActivity extends BaseActivity implements IWaitingDialogNotifier{
	
	private static final String TAG = "NRCReviewEvidenceActivity";
	
	/**
	 * 登录信息
	 */
	@Bean
	LoginCache loginCache;
	
	@Bean
	NRCReviewResponseUtil responseUtil;
	
    @ViewById(R.id.evidence_list)
    ListView evidenceListView;
    
    private NRCReviewEvidenceAdapter reviewEvidenceAdapter;
    
    private WaittingDialog waitDialog;
    
    private List<TLayyZjInfo> infoList = new ArrayList<TLayyZjInfo>();
    
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		infoList = (List<TLayyZjInfo>) intent.getSerializableExtra("zjInfoList");  
		
	}
	
	@AfterViews
	protected void onAfterView() {
		setTitle("证据");			
		reviewEvidenceAdapter = new NRCReviewEvidenceAdapter(this,infoList,responseUtil);
		evidenceListView.setAdapter(reviewEvidenceAdapter);
		reviewEvidenceAdapter.setWaitingDialogNotifier(this);
        waitDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "...");
        waitDialog.setIsCanclable(false);
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

	@Override
	public void showDialog(String text) {
		// TODO Auto-generated method stub
        waitDialog.setWaittingTxt(text);
        waitDialog.show();
	}

	@Override
	public void dismissDialog() {
		// TODO Auto-generated method stub
		waitDialog.dismiss();
	}
}
