package com.thunisoft.sswy.mobile.activity.nrc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcRelLitigantAdapter;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 证人 关联 有利方
 * 
 */

@EActivity(R.layout.activity_nrc_rel_litigant) //与 起诉状，关联当事人共用layout
public class NrcRelAvailActivity extends BaseActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_rel_litigant_title)
	protected TextView titleTV;

	/**
	 * 关联有利方 提示
	 */
	@ViewById(R.id.nrc_rel_litigant_tip)
	protected TextView tipTV;
	
	/**
	 * 关联有利方 全选
	 */
	@ViewById(R.id.nrc_rel_litigant_select_all)
	protected TextView selectAllTV;
	
	/**
	 * 有利方列表
	 */
	@ViewById(R.id.nrc_rel_litigant_list)
	protected ListView advantageListView;
	
	/** 当事人 诉讼地位（原告、被告） intent Key */
	public static final String K_LITIGANT_SSDW = "litigantSsdw";
	
	/** 证人 对象  intent Key */
	public static final String K_WITNESS = "witness";
	
	private TLayyZr layyZr;
	
	/** 所有符合条件的当事人 */
	private List<TLayyDsr> dsrList = new ArrayList<TLayyDsr>();
	
	/** 有利方列表 adapter 与起诉状，关联当事人共用 */
	private NrcRelLitigantAdapter relAvailAdapter;
	
	/** 有利方人员 id Map[有利方人id，有利方人] */
	private Map<String, TLayyDsr> checkedAvailMap = new HashMap<String, TLayyDsr>();

	/** 当事人 诉讼地位（原告、被告）*/
	private String litigantSsdw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		litigantSsdw = intent.getStringExtra(K_LITIGANT_SSDW);
		layyZr = (TLayyZr)intent.getSerializableExtra(K_WITNESS);
	}

	@AfterViews
	protected void onAfterView() {
		titleTV.setText("有利方");
		tipTV.setText(litigantSsdw);
		selectAllTV.setVisibility(View.INVISIBLE);
		refreshAvailList();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	/**
	 * 点击 关联当事人 返回
	 */
	@Click(R.id.nrc_rel_litigant_back)
	protected void clickBack() {
		NrcRelAvailActivity.this.finish();
	}

	/**
	 * 点击 关联当事人 确定
	 */
	@Click(R.id.nrc_rel_litigant_sure)
	protected void clickSure() {
		String availIds = "";
		String availMcs = "";
		if (checkedAvailMap.size() > 0) {
			StringBuffer availIdsTemp = new StringBuffer("");
			StringBuffer availMcsTemp = new StringBuffer("");
			for (Map.Entry<String, TLayyDsr> entry : checkedAvailMap.entrySet()) {
				TLayyDsr dsr = entry.getValue();
				availIdsTemp.append(dsr.getCId()).append(NrcConstants.REL_NAME_SPLIT);
				availMcsTemp.append(dsr.getCName()).append(NrcConstants.REL_NAME_SPLIT);
			}
			availIds = availIdsTemp.toString();
			int lastSplitIndex = availIds.lastIndexOf(NrcConstants.REL_NAME_SPLIT);
			availIds = availIds.substring(0, lastSplitIndex);
			
			availMcs = availMcsTemp.toString();
			lastSplitIndex = availMcs.lastIndexOf(NrcConstants.REL_NAME_SPLIT);
			availMcs = availMcs.substring(0, lastSplitIndex);
		}
		layyZr.setCYlfId(availIds);
		layyZr.setCYlfMc(availMcs);
		Intent intent = new Intent();
		intent.putExtra(K_WITNESS, layyZr);
		setResult(Constants.RESULT_OK, intent);
		NrcRelAvailActivity.this.finish();
	}

	/**
	 * 点击全选或取消全选
	 */
	@Click(R.id.nrc_rel_litigant_select_all)
	protected void clickSelectAll() {
		String selectAllType = selectAllTV.getText().toString();
		String selectAll = getResources().getString(R.string.nrc_select_all);
		String selectAllCancel = getResources().getString(R.string.nrc_select_all_cancel);
		if (selectAll.equals(selectAllType)) { //全选
			selectAllTV.setText(selectAllCancel);
			for (TLayyDsr dsr : dsrList) {
				checkedAvailMap.put(dsr.getCId(), dsr);
				relAvailAdapter.notifyDataSetChanged();
			}
		} else { //取消全选
			selectAllTV.setText(selectAll);
			checkedAvailMap.clear();
			relAvailAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 刷新当事人列表
	 */
	private void refreshAvailList() {
		dsrList.clear();
		checkedAvailMap.clear();
		Map<String, String> availIdMap = new HashMap<String, String>();
		String availIds = layyZr.getCYlfId();
		if (StringUtils.isNotBlank(availIds)) {
			String[] availIdArray = availIds.split(NrcConstants.REL_NAME_SPLIT);
			if (null != availIdArray && availIdArray.length > 0) {
				for (int i=0; i<availIdArray.length; i++) {
					String availId = availIdArray[i];
					availIdMap.put(availId, availId);
				}
			}
		}
		
		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(litigantSsdw)) {
			dsrList.addAll(NrcEditData.getPlaintiffList());
			for (TLayyDsr plaintiff : NrcEditData.getPlaintiffList()) {
				String availId = availIdMap.get(plaintiff.getCId());
				if (StringUtils.isNotBlank(availId)) {
					checkedAvailMap.put(plaintiff.getCId(), plaintiff);
				}
			}
			
		} else {
			dsrList.addAll(NrcEditData.getDefendantList());
			for (TLayyDsr defendant : NrcEditData.getDefendantList()) {
				String availId = availIdMap.get(defendant.getCId());
				if (StringUtils.isNotBlank(availId)) {
					checkedAvailMap.put(defendant.getCId(), defendant);
				}
			}
		}
		
		if (null == relAvailAdapter) {
			relAvailAdapter = new NrcRelLitigantAdapter(this, dsrList, checkedAvailMap);
			relAvailAdapter.setSelectType(NrcConstants.SELECT_TYPE_SINGLE);
			relAvailAdapter.setSelectAllTV(selectAllTV);
			advantageListView.setAdapter(relAvailAdapter);
		} else {
			relAvailAdapter.notifyDataSetChanged();
		}
		
		if (checkedAvailMap.size() < dsrList.size()) {
			String selectAll = getResources().getString(R.string.nrc_select_all);
			selectAllTV.setText(selectAll);
		} else {
			String selectAllCancel = getResources().getString(R.string.nrc_select_all_cancel);
			selectAllTV.setText(selectAllCancel);
		}
	}
}
