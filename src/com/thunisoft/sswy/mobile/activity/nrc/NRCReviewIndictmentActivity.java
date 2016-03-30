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
import com.thunisoft.sswy.mobile.adapter.NRCReviewIndictmentAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.interfaces.IWaitingDialogNotifier;
import com.thunisoft.sswy.mobile.logic.net.NRCReviewResponseUtil;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.model.TLayySsclInfo;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;

/**
 * 网上立案查看界面的起诉状列表
 * 
 */

@EActivity(R.layout.activity_nrc_review_indictment)
public class NRCReviewIndictmentActivity extends BaseActivity implements IWaitingDialogNotifier{
	
	private static final String TAG = "NRCReviewIndictmentActivity";
	
	/**
	 * 登录信息
	 */
	@Bean
	LoginCache loginCache;
	
	@Bean
	NetUtils netUtils;
	
	@Bean
	NRCReviewResponseUtil responseUtil;
	
    @ViewById(R.id.indictment_list)
    ListView indictmentListView;
    
    private NRCReviewIndictmentAdapter reviewIndictmentAdapter;
    
    private WaittingDialog waitDialog;
    
    private List<TLayySsclInfo> infoList;
    
    private List<TLayySsclFj> attachmentList = new ArrayList<TLayySsclFj>();
	
//    private Map<String,Object> plaintiffIdMap = new HashMap<String,Object>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		infoList = (List<TLayySsclInfo>) intent.getSerializableExtra("indictmentList");
//		Bundle bundle = getIntent().getExtras();
//        SerializableMap serializableMap = (SerializableMap) bundle.get("plaintiffIdMap");
//        plaintiffIdMap = serializableMap.getMap();
		loadData();
		
	}
	
	@AfterViews
	protected void onAfterView() {
		setTitle("起诉状");	
		
		reviewIndictmentAdapter = new NRCReviewIndictmentAdapter(this,infoList,
				NetRegisterCaseReviewActivity.plaintiffIdMap,responseUtil);
		indictmentListView.setAdapter(reviewIndictmentAdapter);
		reviewIndictmentAdapter.setWaitingDialogNotifier(this);
        waitDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "...");
        waitDialog.setIsCanclable(false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	public void loadData(){
		for(int i=0;i<infoList.size();i++){
			attachmentList.addAll(infoList.get(i).getSsclfjList());
		}
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
