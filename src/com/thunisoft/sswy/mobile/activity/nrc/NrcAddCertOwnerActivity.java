package com.thunisoft.sswy.mobile.activity.nrc;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcCertOwnAgentAdapter;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcCertOwnLitigantAdapter;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.view.NoScrollListView;

/**
 * 网上立案  证件所属人
 * 
 */

@EActivity(R.layout.activity_nrc_add_certificate_owner)
public class NrcAddCertOwnerActivity extends BaseActivity{
	
	/**
	 * 原告列表
	 */
	@ViewById(R.id.nrc_aco_plaintiff_list)
	protected NoScrollListView plaintiffListView;
	
	/**
	 * 原告代理人 提示
	 */
	@ViewById(R.id.nrc_aco_agent_tip)
	protected TextView agentTipTV;
	
	/**
	 * 原告代理人列表
	 */
	@ViewById(R.id.nrc_aco_plaintiff_agent_list)
	protected NoScrollListView plaintiffAgentListView;
	
	/**
	 * 原告人员 List
	 */
	private List<TLayyDsr> plaintiffList;
	
	/**原告 列表 adapter*/
	private NrcCertOwnLitigantAdapter plaintiffAdapter;
	
	/**
	 * 原告代理 人员 List
	 */
	private List<TLayyDlr> plaintiffAgentList;
	
	/**被原告代理人 列表 adapter*/
	private NrcCertOwnAgentAdapter plaintiffAgentAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@AfterViews
	protected void onAfterView() {
		setBtnBack();
		setTitle("证件所属人");
	}
	
	@Override
	protected void onResume() {
		refreshCertificateOwnerList();
		super.onResume();
	}
	
	/**
	 * 刷新 原告、原告代理人 列表
	 */
	private void refreshCertificateOwnerList() {
		plaintiffList = NrcEditData.getPlaintiffList();
		if (null == plaintiffAdapter) {
			if (null != NrcEditData.getPlaintiffList()) {
				plaintiffAdapter = new NrcCertOwnLitigantAdapter(this, plaintiffList);
				plaintiffListView.setAdapter(plaintiffAdapter);
			}
		} else {
			plaintiffAdapter.notifyDataSetChanged();
		}
		
		plaintiffAgentList = NrcEditData.getAgentList();
		if (null == plaintiffAgentAdapter) {
			if (null != plaintiffAgentList) {
				plaintiffAgentAdapter = new NrcCertOwnAgentAdapter(this, plaintiffAgentList);
				plaintiffAgentListView.setAdapter(plaintiffAgentAdapter);
			}
		} else {
			plaintiffAgentAdapter.notifyDataSetChanged();
		}
		if (null != plaintiffAgentList && plaintiffAgentList.size() > 0) {
			agentTipTV.setVisibility(View.VISIBLE);
		} else {
			agentTipTV.setVisibility(View.GONE);
		}
	}
}
