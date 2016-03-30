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
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 证据 关联 所属人
 * 
 */

@EActivity(R.layout.activity_nrc_rel_litigant) //与 起诉状，关联当事人共用layout
public class NrcEvidenceRelOwnerActivity extends BaseActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_rel_litigant_title)
	protected TextView titleTV;

	/**
	 * 关联被代理人 提示
	 */
	@ViewById(R.id.nrc_rel_litigant_tip)
	protected TextView tipTV;
	
	/**
	 * 关联被代理人 全选
	 */
	@ViewById(R.id.nrc_rel_litigant_select_all)
	protected TextView selectAllTV;
	
	/**
	 * 被代理人列表
	 */
	@ViewById(R.id.nrc_rel_litigant_list)
	protected ListView ownerListView;
	
	/** 当事人 诉讼地位（原告、被告） intent Key */
	public static final String K_LITIGANT_SSDW = "litigantSsdw";
	
	/** 证据 对象  intent Key */
	public static final String K_EVIDENCE = "evidence";
	
	private TZj zj;
	
	/** 所有符合条件的当事人 */
	private List<TLayyDsr> dsrList = new ArrayList<TLayyDsr>();
	
	/** 证据所属人 列表 adapter 与起诉状，关联当事人共用 */
	private NrcRelLitigantAdapter relOwnerAdapter;
	
	/** 证据所属人 id Map[证据所属人id，证据所属人] */
	private Map<String, TLayyDsr> checkedOwnerMap = new HashMap<String, TLayyDsr>();

	/** 当事人 诉讼地位（原告、被告）*/
	private String litigantSsdw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		litigantSsdw = intent.getStringExtra(K_LITIGANT_SSDW);
		zj = (TZj)intent.getSerializableExtra(K_EVIDENCE);
	}

	@AfterViews
	protected void onAfterView() {
		titleTV.setText("所属人");
		tipTV.setText(litigantSsdw);
		selectAllTV.setVisibility(View.GONE);
		refreshEvidenceList();
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
		NrcEvidenceRelOwnerActivity.this.finish();
	}

	/**
	 * 点击 关联当事人 确定
	 */
	@Click(R.id.nrc_rel_litigant_sure)
	protected void clickSure() {
		String ssryIds = "";
		String ssryMcs = "";
		if (checkedOwnerMap.size() > 0) {
			StringBuffer ssryIdsTemp = new StringBuffer("");
			StringBuffer ssryMcsTemp = new StringBuffer("");
			for (Map.Entry<String, TLayyDsr> entry : checkedOwnerMap.entrySet()) {
				TLayyDsr dsr = entry.getValue();
				ssryIdsTemp.append(dsr.getCId()).append(NrcConstants.REL_NAME_SPLIT);
				ssryMcsTemp.append(dsr.getCName()).append(NrcConstants.REL_NAME_SPLIT);
			}
			ssryIds = ssryIdsTemp.toString();
			int lastSplitIndex = ssryIds.lastIndexOf(NrcConstants.REL_NAME_SPLIT);
			ssryIds = ssryIds.substring(0, lastSplitIndex);
			
			ssryMcs = ssryMcsTemp.toString();
			lastSplitIndex = ssryMcs.lastIndexOf(NrcConstants.REL_NAME_SPLIT);
			ssryMcs = ssryMcs.substring(0, lastSplitIndex);
		}
		zj.setCSsryId(ssryIds);
		zj.setCSsryMc(ssryMcs);
		Intent intent = new Intent();
		intent.putExtra(K_EVIDENCE, zj);
		setResult(Constants.RESULT_OK, intent);
		NrcEvidenceRelOwnerActivity.this.finish();
	}

	@Click(R.id.nrc_rel_litigant_select_all)
	@Deprecated
	protected void clickSelectAll() {
		String selectAllType = selectAllTV.getText().toString();
		String selectAll = getResources().getString(R.string.nrc_select_all);
		String selectAllCancel = getResources().getString(R.string.nrc_select_all_cancel);
		if (selectAll.equals(selectAllType)) { //全选
			selectAllTV.setText(selectAllCancel);
			for (TLayyDsr dsr : dsrList) {
				checkedOwnerMap.put(dsr.getCId(), dsr);
				relOwnerAdapter.notifyDataSetChanged();
			}
		} else { //取消全选
			selectAllTV.setText(selectAll);
			checkedOwnerMap.clear();
			relOwnerAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 刷新当事人列表
	 */
	private void refreshEvidenceList() {
		dsrList.clear();
		checkedOwnerMap.clear();
		Map<String, String> agentedIdMap = new HashMap<String, String>();
		String ssryIds = zj.getCSsryId();
		if (StringUtils.isNotBlank(ssryIds)) {
			String[] bdlrIdArray = ssryIds.split(NrcConstants.REL_NAME_SPLIT);
			if (null != bdlrIdArray && bdlrIdArray.length > 0) {
				for (int i=0; i<bdlrIdArray.length; i++) {
					String bdlrId = bdlrIdArray[i];
					agentedIdMap.put(bdlrId, bdlrId);
				}
			}
		}
		
		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(litigantSsdw)) {
			dsrList.addAll(NrcEditData.getPlaintiffList());
			for (TLayyDsr plaintiff : NrcEditData.getPlaintiffList()) {
				String agentedId = agentedIdMap.get(plaintiff.getCId());
				if (StringUtils.isNotBlank(agentedId)) {
					checkedOwnerMap.put(plaintiff.getCId(), plaintiff);
				}
			}
			
		} else {
			dsrList.addAll(NrcEditData.getDefendantList());
			for (TLayyDsr defendant : NrcEditData.getDefendantList()) {
				String agentedId = agentedIdMap.get(defendant.getCId());
				if (StringUtils.isNotBlank(agentedId)) {
					checkedOwnerMap.put(defendant.getCId(), defendant);
				}
			}
		}
		
		if (null == relOwnerAdapter) {
			relOwnerAdapter = new NrcRelLitigantAdapter(this, dsrList, checkedOwnerMap);
			relOwnerAdapter.setSelectType(NrcConstants.SELECT_TYPE_SINGLE);
			relOwnerAdapter.setSelectAllTV(selectAllTV);
			ownerListView.setAdapter(relOwnerAdapter);
		} else {
			relOwnerAdapter.notifyDataSetChanged();
		}
		
		if (checkedOwnerMap.size() < dsrList.size()) {
			String selectAll = getResources().getString(R.string.nrc_select_all);
			selectAllTV.setText(selectAll);
		} else {
			String selectAllCancel = getResources().getString(R.string.nrc_select_all_cancel);
			selectAllTV.setText(selectAllCancel);
		}
	}
}
