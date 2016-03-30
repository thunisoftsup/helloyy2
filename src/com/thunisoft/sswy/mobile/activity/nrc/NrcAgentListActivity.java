package com.thunisoft.sswy.mobile.activity.nrc;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcAgentAdapter;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案_查看更多_代理人列表
 * 
 */

@EActivity(R.layout.activity_nrc_litigant_list)
public class NrcAgentListActivity extends BaseActivity {

	@ViewById(R.id.nrc_litigant_add_tip)
	protected TextView addTipTV;
	
	/**
	 * 代理人 列表
	 */
	@ViewById(R.id.nrc_litigant_list)
	protected ListView agentListView;

	/** 代理人 列表 adapter */
	private NrcAgentAdapter agentAdapter;

	private List<TLayyDlr> agentList = new ArrayList<TLayyDlr>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		setBtnBack();
		setTitle("代理人");
		addTipTV.setText("添加代理人");
	}

	@Override
	protected void onResume() {
		refreshAgentList();
		super.onResume();
	}

	/**
	 * 点击添加代理人
	 */
	@Click(R.id.nrc_litigant_add)
	protected void clickAdd() {
		Intent intent = new Intent();
		intent.setClass(this, NrcAddAgentActivity_.class);
		TLayyDlr layyDlr = new TLayyDlr();
		String layyId = NrcEditData.getLayy().getCId();
		layyDlr.setCLayyId(layyId);
		intent.putExtra(NrcAddAgentActivity.K_AGENT, layyDlr);
		startActivity(intent);
	}

	/**
	 * 刷新代理人列表
	 */
	private void refreshAgentList() {
		agentList.clear();
		agentList.addAll(NrcEditData.getAgentList());
		if (null == agentAdapter) {
			agentAdapter = new NrcAgentAdapter(this, agentList);
			agentListView.setAdapter(agentAdapter);
		} else {
			agentAdapter.notifyDataSetChanged();
		}
	}
}
