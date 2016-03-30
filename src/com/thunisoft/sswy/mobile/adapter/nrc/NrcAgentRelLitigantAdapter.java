package com.thunisoft.sswy.mobile.adapter.nrc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NrcRelAgentedActivity;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 关联当事人 人员列表 adapter
 * 
 * @author gewx
 *
 */
public class NrcAgentRelLitigantAdapter extends BaseAdapter {

	/** 关联类型_关联被代理人 */
	public static final int REL_TYPE_AGENTED = 1;

	private NrcRelAgentedActivity agentRelLitigantAty;

	private List<TLayyDsr> dsrList;

	private Map<String, TLayyDsr> checkedMap;

	/** 全选按钮 */
	private TextView selectAllTV;

	/** 代理人 */
	private TLayyDlr layyDlr;

	public NrcAgentRelLitigantAdapter(NrcRelAgentedActivity agentRelLitigantAty, List<TLayyDsr> dsrList, Map<String, TLayyDsr> checkedMap) {
		this.agentRelLitigantAty = agentRelLitigantAty;
		this.dsrList = dsrList;
		this.checkedMap = checkedMap;
	}

	@Override
	public int getCount() {
		return dsrList.size();
	}

	@Override
	public TLayyDsr getItem(int position) {
		return dsrList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(agentRelLitigantAty);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_nrc_rel_litigant_item, null);
			viewHolder.litigantNameTV = (TextView) convertView.findViewById(R.id.nrc_rel_litigant_item_name);
			viewHolder.litigantCheckFLayout = (FrameLayout) convertView.findViewById(R.id.nrc_rel_litigant_item_check);
			viewHolder.litigantCheckRB = (RadioButton) convertView.findViewById(R.id.nrc_rel_litigant_item_radio);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TLayyDsr dsr = dsrList.get(position);
		viewHolder.litigantNameTV.setText(dsr.getCName());
		if (null != checkedMap.get(dsr.getCId())) {
			viewHolder.litigantCheckRB.setChecked(true);
		} else {
			viewHolder.litigantCheckRB.setChecked(false);
		}

		CheckOnClickListener checkOnClickListener = new CheckOnClickListener();
		checkOnClickListener.checkRB = viewHolder.litigantCheckRB;
		checkOnClickListener.position = position;
		viewHolder.litigantCheckFLayout.setOnClickListener(checkOnClickListener);
		return convertView;
	}

	private static class ViewHolder {

		/**
		 * 当事人 姓名
		 */
		TextView litigantNameTV;

		/**
		 * 关联当事人 选中点击
		 */
		FrameLayout litigantCheckFLayout;

		/**
		 * 关联当事人 选中
		 */
		RadioButton litigantCheckRB;
	}

	private class CheckOnClickListener implements OnClickListener {

		public int position;

		private RadioButton checkRB;

		@Override
		public void onClick(View v) {
			TLayyDsr dsr = dsrList.get(position);
			if (null == checkedMap.get(dsr.getCId())) {
				
				Map<String, String> relPlaintiffMap = getAgentedRelPlaintiff(layyDlr.getCId(), dsr.getCId());
				int dsrAgented = Integer.parseInt(relPlaintiffMap.get("count"));
				if (NrcConstants.AGENT_REL_PLAINTIFF_COUNT <= dsrAgented) {
					String dlrNamesStr = relPlaintiffMap.get("dsrNames");
					showToastMsg(dsr.getCName(), dlrNamesStr);
					return;
				}
				relPlaintiffMap = null;
				
				checkedMap.put(dsr.getCId(), dsr);
				checkRB.setChecked(true);
				refreshSelectAll();
			} else {
				checkedMap.remove(dsr.getCId());
				checkRB.setChecked(false);
				refreshSelectAll();
			}
		}

		/**
		 * 刷新全选按钮
		 */
		private void refreshSelectAll() {
			if (null != selectAllTV) {
				if (checkedMap.size() < dsrList.size()) {
					String selectAll = agentRelLitigantAty.getResources().getString(R.string.nrc_select_all);
					selectAllTV.setText(selectAll);
				} else {
					String selectAllCancel = agentRelLitigantAty.getResources().getString(R.string.nrc_select_all_cancel);
					selectAllTV.setText(selectAllCancel);
				}
			}
		}
	}

	/**
	 * 获取当前原告已经关联代理人的数量
	 * @param dlrId 代理人id
	 * @param dsrid 当事人id
	 * @return
	 */
	private Map<String, String> getAgentedRelPlaintiff(String dlrId, String dsrId) {
		Map<String, String> relPlaintiffMap = new HashMap<String, String>();
		int dsrAgentedCount = 0;
		String dlrNamesStr = "";
		List<TLayyDlr> dlrList = NrcEditData.getAgentList();
		if (null != dlrList && dlrList.size() > 0) {
			StringBuffer dlrNamesSb = new StringBuffer("");
			for (TLayyDlr dlr : dlrList) {
				String bdlrIds = dlr.getCBdlrId();
				if (StringUtils.isNotBlank(bdlrIds)) {
					String[] bdlrIdArray = bdlrIds.split(NrcConstants.REL_NAME_SPLIT);
					if (null != bdlrIdArray && bdlrIdArray.length > 0) {
						for (int i = 0; i < bdlrIdArray.length; i++) {
							String bdlrId = bdlrIdArray[i];
							if (bdlrId.equals(dsrId) && !dlrId.equals(dlr.getCId())) { //需要增加不是当前代理人的，原告和其他代理人关联的数据
								dlrNamesSb.append(dlr.getCName()).append(",");
								dsrAgentedCount++;
							}
						}
					}
				}
			}
			dlrNamesStr = dlrNamesSb.toString();
			if (StringUtils.isNotBlank(dlrNamesStr)) {
				int lastSplitIndex = dlrNamesStr.lastIndexOf(NrcConstants.REL_NAME_SPLIT);
				dlrNamesStr = dlrNamesStr.substring(0, lastSplitIndex);
			}
		}
		relPlaintiffMap.put("count", String.valueOf(dsrAgentedCount));
		relPlaintiffMap.put("dsrNames", dlrNamesStr);
		return relPlaintiffMap;
	}

	/**
	 * 提示
	 * 
	 * @param dsrName
	 * @param dlrNamesStr
	 */
	private void showToastMsg(String dsrName, String dlrNamesStr) {
		StringBuffer toastMsgs = new StringBuffer("");
		toastMsgs.append("原告：").append(dsrName);
		toastMsgs.append("，\n已关联代理人：").append(dlrNamesStr);
		toastMsgs.append("，\n最多允许关联").append(NrcConstants.AGENT_REL_PLAINTIFF_COUNT).append("人");
		Toast.makeText(agentRelLitigantAty, toastMsgs.toString(), Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取 全选按钮
	 * 
	 * @return selectAllTV
	 */
	public TextView getSelectAllTV() {
		return selectAllTV;
	}

	/**
	 * 设置 全选按钮
	 * 
	 * @param selectAllTV
	 */
	public void setSelectAllTV(TextView selectAllTV) {
		this.selectAllTV = selectAllTV;
	}

	/**
	 * 获取 代理人
	 * 
	 * @return layyDlr
	 */
	public TLayyDlr getLayyDlr() {
		return layyDlr;
	}

	/**
	 * 设置 代理人
	 * 
	 * @param layyDlr
	 */
	public void setLayyDlr(TLayyDlr layyDlr) {
		this.layyDlr = layyDlr;
	}
}
