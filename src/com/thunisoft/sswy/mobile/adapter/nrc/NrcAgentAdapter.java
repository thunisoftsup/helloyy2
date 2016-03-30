package com.thunisoft.sswy.mobile.adapter.nrc;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddAgentActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddAgentActivity_;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.util.nrc.NrcCheckUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 代理人 人员列表 adapter
 * 
 * @author gewx
 *
 */
public class NrcAgentAdapter extends BaseAdapter {

	private Activity activity;

	private List<TLayyDlr> dlrList;
	
	public NrcAgentAdapter(Activity activity, List<TLayyDlr> dlrList) {
		this.activity = activity;
		this.dlrList = dlrList;
	}

	@Override
	public int getCount() {
		return dlrList.size();
	}

	@Override
	public TLayyDlr getItem(int position) {
		return dlrList.get(position);
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
			convertView = inflater.inflate(R.layout.activity_nrc_list_item, null);
			viewHolder.agentNameTV = (TextView) convertView.findViewById(R.id.nrc_list_item_name);
			viewHolder.infoTipTV = (TextView) convertView.findViewById(R.id.nrc_list_item_info_tip);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TLayyDlr dlr = dlrList.get(position);
		viewHolder.agentNameTV.setText(dlr.getCName());

		String sqrId = NrcEditData.getLayy().getCSqrId();
		if (NrcCheckUtils.checkAgentData(sqrId, dlr)) {
			viewHolder.infoTipTV.setVisibility(View.GONE);
		} else {
			viewHolder.infoTipTV.setVisibility(View.VISIBLE);
		}
		
		ItemOnClickListener itemOnClickListener = new ItemOnClickListener();
		itemOnClickListener.position = position;
		convertView.setOnClickListener(itemOnClickListener);

		return convertView;
	}

	private static class ViewHolder {

		/**
		 * 代理人 姓名
		 */
		TextView agentNameTV;
		
		/**
		 * 信息项添加是否完全校验（信息不全）
		 */
		TextView infoTipTV;
	}

	private class ItemOnClickListener implements OnClickListener {

		public int position;

		@Override
		public void onClick(View v) {
			TLayyDlr dlr = dlrList.get(position);
			Intent intent = new Intent();
			intent.setClass(activity, NrcAddAgentActivity_.class);
			intent.putExtra(NrcAddAgentActivity.K_AGENT, dlr);
			activity.startActivity(intent);
		}
	}
}
