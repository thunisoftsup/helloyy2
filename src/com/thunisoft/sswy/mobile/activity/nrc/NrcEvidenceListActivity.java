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
import com.thunisoft.sswy.mobile.adapter.nrc.NrcEvidenceAdapter;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案_查看更多_证据列表
 * 
 */

@EActivity(R.layout.activity_nrc_litigant_list)
public class NrcEvidenceListActivity extends BaseActivity {

	@ViewById(R.id.nrc_litigant_add_tip)
	protected TextView addTipTV;
	
	/**
	 * 证据 列表
	 */
	@ViewById(R.id.nrc_litigant_list)
	protected ListView evidenceListView;

	/** 证据 列表 adapter */
	private NrcEvidenceAdapter evidenceAdapter;

	private List<TZj> evidenceList = new ArrayList<TZj>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		setBtnBack();
		setTitle("证据");
		addTipTV.setText("添加证据");
	}

	@Override
	protected void onResume() {
		refreshEvidenceList();
		super.onResume();
	}

	/**
	 * 点击添加证据
	 */
	@Click(R.id.nrc_litigant_add)
	protected void clickAdd() {
		TZj zj = new TZj();
		String layyId = NrcEditData.getLayy().getCId();
		zj.setCYwBh(layyId);
		Intent intent = new Intent();
		intent.setClass(this, NrcAddEvidenceActivity_.class);
		intent.putExtra(NrcAddEvidenceActivity.K_ZJ, zj);
		startActivity(intent);
	}

	/**
	 * 刷新证据列表
	 */
	private void refreshEvidenceList() {
		evidenceList.clear();
		evidenceList.addAll(NrcEditData.getEvidenceList());
		if (null == evidenceAdapter) {
			evidenceAdapter = new NrcEvidenceAdapter(this, evidenceList);
			evidenceListView.setAdapter(evidenceAdapter);
		} else {
			evidenceAdapter.notifyDataSetChanged();
		}
	}
}
