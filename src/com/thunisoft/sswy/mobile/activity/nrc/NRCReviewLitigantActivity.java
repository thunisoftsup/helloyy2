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
import com.thunisoft.sswy.mobile.adapter.NRCReviewLitigantAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案查看界面的当事人列表
 * 
 */

@EActivity(R.layout.activity_nrc_review_litigant)
public class NRCReviewLitigantActivity extends BaseActivity{

	private static final String TAG = "NRCReviewLitigantActivity";
	
    @ViewById(R.id.litigant_list)
    ListView litigantListView;
    
    private NRCReviewLitigantAdapter reviewLitigantAdapter;
    
    private List<TLayyDsr> litigantList = new ArrayList<TLayyDsr>();
    
    private int litigantType = 0;
	
	/**
	 * 登录信息
	 */
	@Bean
    LoginCache loginCache;
	
	@Bean
    ResponseUtilExtr responseUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		litigantType = intent.getIntExtra("litigant_type", NrcConstants.LITIGANT_TYPE_PLAINTIFF);
		litigantList = (List<TLayyDsr>) intent.getSerializableExtra("dsrList");  
	}
	
	@AfterViews
	protected void onAfterView() {
		List<TLayyDsr> tempLitigantList = new ArrayList<TLayyDsr>();
		if (litigantType == NrcConstants.LITIGANT_TYPE_PLAINTIFF) {
			setTitle("原告");
		} else {
			setTitle("被告");
		}
		tempLitigantList = getLitigantList(litigantList,litigantType);
		reviewLitigantAdapter = new NRCReviewLitigantAdapter(this,
				tempLitigantList);
		litigantListView.setAdapter(reviewLitigantAdapter);

	}
	
	private List<TLayyDsr> getLitigantList(List<TLayyDsr> litigantList,int litigantType){
		List<TLayyDsr> tempLitigantList = new ArrayList<TLayyDsr>();
		for(int i=0;i<litigantList.size();i++){
			String cSsdw = litigantList.get(i).getCSsdw();
			if(NrcUtils.getLitigantStateCodeByName(cSsdw)==litigantType){
				tempLitigantList.add(litigantList.get(i));
			}
		}
		return tempLitigantList;
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
