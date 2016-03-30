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
import com.thunisoft.sswy.mobile.adapter.NRCReviewCertificatesAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.interfaces.IWaitingDialogNotifier;
import com.thunisoft.sswy.mobile.logic.net.NRCReviewResponseUtil;
import com.thunisoft.sswy.mobile.model.TLayySsclInfo;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;

/**
 * 网上立案查看界面的证件列表
 * 
 */

@EActivity(R.layout.activity_nrc_review_certificates)
public class NRCReviewCertificatesActivity extends BaseActivity implements IWaitingDialogNotifier{
	

	private static final String TAG = "NRCReviewCertificatesActivity";
	
	/**
	 * 登录信息
	 */
	@Bean
	LoginCache loginCache;
	
    @ViewById(R.id.certificates_list)
    ListView certificatesListView;
    
	@Bean
    NRCReviewResponseUtil responseUtil; 
    
    private NRCReviewCertificatesAdapter reviewCertificatesAdapter;
    
    private WaittingDialog waitDialog;
    
    private List<TLayySsclInfo> infoList = new ArrayList<TLayySsclInfo>() ;
    
    private List<TLayySsclFj> attachmentList = new ArrayList<TLayySsclFj>();
    
    private int certificatesOwnerType = 0;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		certificatesOwnerType = intent.getIntExtra("certificates_type",
				NrcConstants.CERTIFICATES_TYPE_PLAINTIFF);
		infoList = (List<TLayySsclInfo>) intent.getSerializableExtra("certificatesList");
		attachmentList = (List<TLayySsclFj>) intent.getSerializableExtra("attachmentList");
	}
	
	@AfterViews
	protected void onAfterView() {
		if(certificatesOwnerType == NrcConstants.CERTIFICATES_TYPE_PLAINTIFF){
			setTitle("原告证件");	
		}else if(certificatesOwnerType == NrcConstants.CERTIFICATES_TYPE_DEFENDANT){
			setTitle("被告证件");	
		}else{
			setTitle("代理人证件");			
		}
		reviewCertificatesAdapter = new NRCReviewCertificatesAdapter(this,infoList,attachmentList,responseUtil);
		certificatesListView.setAdapter(reviewCertificatesAdapter);
		reviewCertificatesAdapter.setWaitingDialogNotifier(this);
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
