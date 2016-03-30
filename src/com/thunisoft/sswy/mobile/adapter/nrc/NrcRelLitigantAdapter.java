package com.thunisoft.sswy.mobile.adapter.nrc;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;

/**
 * 网上立案 关联当事人 人员列表 adapter
 * 
 * @author gewx
 *
 */
public class NrcRelLitigantAdapter extends BaseAdapter {

	private Activity activity;

	private List<TLayyDsr> dsrList;

	private Map<String, TLayyDsr> checkedMap;

	/** 全选按钮 */
	private TextView selectAllTV;
	
	private int selectType = NrcConstants.SELECT_TYPE_MULTI;

	public NrcRelLitigantAdapter(Activity activity, List<TLayyDsr> dsrList, Map<String, TLayyDsr> checkedMap) {
		this.activity = activity;
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
			LayoutInflater inflater = LayoutInflater.from(activity);
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
				if (NrcConstants.SELECT_TYPE_SINGLE == selectType) { //单选
					checkedMap.clear();
					checkedMap.put(dsr.getCId(), dsr);
					NrcRelLitigantAdapter.this.notifyDataSetChanged();
				} else { //多选
					checkedMap.put(dsr.getCId(), dsr);
					checkRB.setChecked(true);
					refreshSelectAll();
				}
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
					String selectAll = activity.getResources().getString(R.string.nrc_select_all);
					selectAllTV.setText(selectAll);
				} else {
					String selectAllCancel = activity.getResources().getString(R.string.nrc_select_all_cancel);
					selectAllTV.setText(selectAllCancel);
				}
			}
		}
	}

	/**
	 * 获取 全选按钮
	 * @return selectAllTV
	 */
	public TextView getSelectAllTV() {
		return selectAllTV;
	}

	/**
	 * 设置 全选按钮
	 * @param selectAllTV
	 */
	public void setSelectAllTV(TextView selectAllTV) {
		this.selectAllTV = selectAllTV;
	}

	/**  
	 * 获取  选择类型：单选、多选  
	 * @return selectType  
	 */
	public int getSelectType() {
		return selectType;
	}

	/**  
	 * 设置  选择类型：单选、多选    
	 * @param selectType
	 */
	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}
}
