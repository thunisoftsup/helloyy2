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
import com.thunisoft.sswy.mobile.adapter.nrc.NrcWitnessAdapter;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案_查看更多_证人列表
 * 
 */

@EActivity(R.layout.activity_nrc_litigant_list)
public class NrcWitnessListActivity extends BaseActivity {

	@ViewById(R.id.nrc_litigant_add_tip)
	protected TextView addTipTV;
	
	/**
	 * 证人 列表
	 */
	@ViewById(R.id.nrc_litigant_list)
	protected ListView witnessListView;

	/** 证人 列表 adapter */
	private NrcWitnessAdapter witnessAdapter;

	private List<TLayyZr> witnessList = new ArrayList<TLayyZr>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		setBtnBack();
		setTitle("证人");
		addTipTV.setText("添加证人");
	}

	@Override
	protected void onResume() {
		refreshWitnessList();
		super.onResume();
	}

	/**
	 * 点击添加证人
	 */
	@Click(R.id.nrc_litigant_add)
	protected void clickAdd() {
		Intent intent = new Intent();
		intent.setClass(this, NrcAddWitnessActivity_.class);
		TLayyZr layyZr = new TLayyZr();
		String layyId = NrcEditData.getLayy().getCId();
		layyZr.setCLayyId(layyId);
		intent.putExtra(NrcAddWitnessActivity.K_WITNESS, layyZr);
		startActivity(intent);
	}

	/**
	 * 刷新证人列表
	 */
	private void refreshWitnessList() {
		witnessList.clear();
		witnessList.addAll(NrcEditData.getWitnessList());
		if (null == witnessAdapter) {
			witnessAdapter = new NrcWitnessAdapter(this, witnessList);
			witnessListView.setAdapter(witnessAdapter);
		} else {
			witnessAdapter.notifyDataSetChanged();
		}
	}
}
