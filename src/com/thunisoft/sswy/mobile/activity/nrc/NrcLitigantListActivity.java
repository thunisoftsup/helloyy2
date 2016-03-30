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

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcLitigantAdapter;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案_查看更多_当事人列表
 * 
 */

@EActivity(R.layout.activity_nrc_litigant_list)
public class NrcLitigantListActivity extends BaseActivity {

	@ViewById(R.id.nrc_litigant_add_tip)
	protected TextView addTipTV;
	
	/**
	 * 当事人列表
	 */
	@ViewById(R.id.nrc_litigant_list)
	protected ListView litigantListView;

	/**
	 * 当事人类型
	 */
	public static final String K_LITIGANT_TYPE = "litigantType";

	/** 当事人 列表 adapter */
	private NrcLitigantAdapter litigantAdapter;

	private List<TLayyDsr> litigantList = new ArrayList<TLayyDsr>();

	private int litigantType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		setBtnBack();
		Intent intent = getIntent();
		litigantType = intent.getIntExtra(K_LITIGANT_TYPE, NrcConstants.LITIGANT_TYPE_PLAINTIFF);
		if (NrcConstants.LITIGANT_TYPE_PLAINTIFF == litigantType) {
			setTitle("原告");
			addTipTV.setText("添加原告");
		} else {
			setTitle("被告");
			addTipTV.setText("添加被告");
		}
	}

	@Override
	protected void onResume() {
		refreshLitigantList();
		super.onResume();
	}

	/**
	 * 点击添加当事人
	 */
	@Click(R.id.nrc_litigant_add)
	protected void clickAdd() {
		Intent intent = new Intent();
		intent.setClass(this, NrcAddLitigantActivity_.class);
		TLayyDsr layyDsr = new TLayyDsr();
		String layyId = NrcEditData.getLayy().getCId();
		layyDsr.setCLayyId(layyId);
		layyDsr.setCSsdw(Constants.LITIGANT_SSDW_PLAINTIFF);
		intent.putExtra(NrcAddLitigantActivity.K_LITIGANT, layyDsr);
		startActivity(intent);
	}

	/**
	 * 刷新当事人列表
	 */
	private void refreshLitigantList() {
		litigantList.clear();
		if (NrcConstants.LITIGANT_TYPE_PLAINTIFF == litigantType) {
			litigantList.addAll(NrcEditData.getPlaintiffList());
		} else {
			litigantList.addAll(NrcEditData.getDefendantList());
		}
		if (null == litigantAdapter) {
			litigantAdapter = new NrcLitigantAdapter(this, litigantList);
			litigantListView.setAdapter(litigantAdapter);
		} else {
			litigantAdapter.notifyDataSetChanged();
		}
	}
}
