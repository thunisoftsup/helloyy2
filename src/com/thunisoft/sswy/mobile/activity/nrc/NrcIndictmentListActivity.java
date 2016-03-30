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
import com.thunisoft.sswy.mobile.adapter.nrc.NrcIndictmentAdapter;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案_查看更多_起诉状列表
 * 
 */

@EActivity(R.layout.activity_nrc_litigant_list)
public class NrcIndictmentListActivity extends BaseActivity {

	@ViewById(R.id.nrc_litigant_add_tip)
	protected TextView addTipTV;
	
	/**
	 * 起诉状 列表
	 */
	@ViewById(R.id.nrc_litigant_list)
	protected ListView indictmentListView;

	/** 起诉状 列表 adapter */
	private NrcIndictmentAdapter indictmentAdapter;

	private List<TLayySscl> indictmentList = new ArrayList<TLayySscl>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		setBtnBack();
		setTitle("起诉状");
		addTipTV.setText("添加起诉状");
	}

	@Override
	protected void onResume() {
		refreshIndictmentList();
		super.onResume();
	}

	/**
	 * 点击添加起诉状
	 */
	@Click(R.id.nrc_litigant_add)
	protected void clickAdd() {
		Intent intent = new Intent();
		intent.setClass(this, NrcAddIndictmentActivity_.class);
		TLayySscl sscl = new TLayySscl();
		String layyId = NrcEditData.getLayy().getCId();
		sscl.setCLayyId(layyId);
		intent.putExtra(NrcAddIndictmentActivity.K_SSCL, sscl);
		startActivity(intent);
	}

	/**
	 * 刷新起诉状列表
	 */
	private void refreshIndictmentList() {
		indictmentList.clear();
		indictmentList.addAll(NrcEditData.getIndictmentList());
		if (null == indictmentAdapter) {
			indictmentAdapter = new NrcIndictmentAdapter(this, indictmentList);
			indictmentListView.setAdapter(indictmentAdapter);
		} else {
			indictmentAdapter.notifyDataSetChanged();
		}
	}
}
