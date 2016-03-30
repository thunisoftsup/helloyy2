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
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcAgentRelLitigantAdapter;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 代理人关联被代理人
 * 
 */

@EActivity(R.layout.activity_nrc_rel_litigant) //与 起诉状，关联当事人共用layout
public class NrcRelAgentedActivity extends BaseActivity {

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
	protected ListView advantageListView;
	
	/** 当事人 诉讼地位（原告、被告） intent Key */
	public static final String K_LITIGANT_SSDW = "litigantSsdw";
	
	/** 代理人 对象  intent Key */
	public static final String K_AGENT = "agent";
	
	private TLayyDlr layyDlr;
	
	/** 所有符合条件的当事人 */
	private List<TLayyDsr> dsrList = new ArrayList<TLayyDsr>();
	
	/** 被代理人列表 adapter 与起诉状，关联当事人共用 */
	private NrcAgentRelLitigantAdapter relAgentedAdapter;
	
	/** 被代理人 id Map[被代理人id，被代理人] */
	private Map<String, TLayyDsr> checkedAgentedMap = new HashMap<String, TLayyDsr>();

	/** 当事人 诉讼地位（原告、被告）*/
	private String litigantSsdw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		litigantSsdw = intent.getStringExtra(K_LITIGANT_SSDW);
		layyDlr = (TLayyDlr)intent.getSerializableExtra(K_AGENT);
	}

	@AfterViews
	protected void onAfterView() {
		titleTV.setText("被代理人");
		tipTV.setText(litigantSsdw);
		refreshAgentedList();
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
		NrcRelAgentedActivity.this.finish();
	}

	/**
	 * 点击 关联当事人 确定
	 */
	@Click(R.id.nrc_rel_litigant_sure)
	protected void clickSure() {
		String bdlrIds = "";
		String bdlrMcs = "";
		if (checkedAgentedMap.size() > 0) {
			StringBuffer bdlrIdsTemp = new StringBuffer("");
			StringBuffer bdlrMcsTemp = new StringBuffer("");
			for (Map.Entry<String, TLayyDsr> entry : checkedAgentedMap.entrySet()) {
				TLayyDsr dsr = entry.getValue();
				bdlrIdsTemp.append(dsr.getCId()).append(NrcConstants.REL_NAME_SPLIT);
				bdlrMcsTemp.append(dsr.getCName()).append(NrcConstants.REL_NAME_SPLIT);
			}
			bdlrIds = bdlrIdsTemp.toString();
			int lastSplitIndex = bdlrIds.lastIndexOf(NrcConstants.REL_NAME_SPLIT);
			bdlrIds = bdlrIds.substring(0, lastSplitIndex);
			
			bdlrMcs = bdlrMcsTemp.toString();
			lastSplitIndex = bdlrMcs.lastIndexOf(NrcConstants.REL_NAME_SPLIT);
			bdlrMcs = bdlrMcs.substring(0, lastSplitIndex);
		}
		layyDlr.setCBdlrId(bdlrIds);
		layyDlr.setCBdlrMc(bdlrMcs);
		Intent intent = new Intent();
		intent.putExtra(K_AGENT, layyDlr);
		setResult(Constants.RESULT_OK, intent);
		NrcRelAgentedActivity.this.finish();
	}

	@Click(R.id.nrc_rel_litigant_select_all)
	protected void clickSelectAll() {
		String selectAllType = selectAllTV.getText().toString();
		String selectAll = getResources().getString(R.string.nrc_select_all);
		String selectAllCancel = getResources().getString(R.string.nrc_select_all_cancel);
		if (selectAll.equals(selectAllType)) { //全选
			Map<String, Integer> agentedRelCountMap = getAgentedRelCountMap(layyDlr.getCId());
			selectAllTV.setText(selectAllCancel);
			for (TLayyDsr dsr : dsrList) {
				int relCount = 0;
				if (null != agentedRelCountMap.get(dsr.getCId())) {
					relCount = agentedRelCountMap.get(dsr.getCId());
				}
				if (NrcConstants.AGENT_REL_PLAINTIFF_COUNT > relCount) {
					checkedAgentedMap.put(dsr.getCId(), dsr);
					relAgentedAdapter.notifyDataSetChanged();
				} else {
					selectAllTV.setText(selectAll);
				}
			}
		} else { //取消全选
			selectAllTV.setText(selectAll);
			checkedAgentedMap.clear();
			relAgentedAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 刷新当事人列表
	 */
	private void refreshAgentedList() {
		dsrList.clear();
		checkedAgentedMap.clear();
		Map<String, String> agentedIdMap = new HashMap<String, String>();
		String bdlrIds = layyDlr.getCBdlrId();
		if (StringUtils.isNotBlank(bdlrIds)) {
			String[] bdlrIdArray = bdlrIds.split(NrcConstants.REL_NAME_SPLIT);
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
					checkedAgentedMap.put(plaintiff.getCId(), plaintiff);
				}
			}
			
		} else {
			dsrList.addAll(NrcEditData.getDefendantList());
			for (TLayyDsr defendant : NrcEditData.getDefendantList()) {
				String agentedId = agentedIdMap.get(defendant.getCId());
				if (StringUtils.isNotBlank(agentedId)) {
					checkedAgentedMap.put(defendant.getCId(), defendant);
				}
			}
		}
		
		if (null == relAgentedAdapter) {
			relAgentedAdapter = new NrcAgentRelLitigantAdapter(this, dsrList, checkedAgentedMap);
			relAgentedAdapter.setLayyDlr(layyDlr);
			relAgentedAdapter.setSelectAllTV(selectAllTV);
			advantageListView.setAdapter(relAgentedAdapter);
		} else {
			relAgentedAdapter.notifyDataSetChanged();
		}
		
		if (checkedAgentedMap.size() < dsrList.size()) {
			String selectAll = getResources().getString(R.string.nrc_select_all);
			selectAllTV.setText(selectAll);
		} else {
			String selectAllCancel = getResources().getString(R.string.nrc_select_all_cancel);
			selectAllTV.setText(selectAllCancel);
		}
	}
	
	/**
	 * 获取当前原告已经关联代理人的数量
	 * @param dlrId 代理人id
	 * @param dsrid 当事人id
	 * @return
	 */
	private Map<String, Integer> getAgentedRelCountMap(String dlrId) {
		//当事人关联其他代理人的数量
		Map<String, Integer> agentedRelCountMap = new HashMap<String, Integer>();
		List<TLayyDlr> dlrList = NrcEditData.getAgentList();
		if (null != dlrList && dlrList.size() > 0) {
			for (TLayyDlr dlr : dlrList) {
				String bdlrIds = dlr.getCBdlrId();
				if (StringUtils.isNotBlank(bdlrIds)) {
					String[] bdlrIdArray = bdlrIds.split(NrcConstants.REL_NAME_SPLIT);
					if (null != bdlrIdArray && bdlrIdArray.length > 0) {
						for (int i = 0; i < bdlrIdArray.length; i++) {
							String bdlrId = bdlrIdArray[i];
							if (!dlrId.equals(dlr.getCId())) { //需要增加不是当前代理人的，原告和其他代理人关联的数据
								Integer countTemp = agentedRelCountMap.get(bdlrId);
								if (null == countTemp) {
									countTemp = 1;
									agentedRelCountMap.put(bdlrId, countTemp);
								} else {
									countTemp++;
									agentedRelCountMap.put(bdlrId, countTemp);
								}
							}
						}
					}
				}
			}
		}
		return agentedRelCountMap;
	}
}
