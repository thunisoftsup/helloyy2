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
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddLitigantActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddLitigantActivity_;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.nrc.NrcCheckUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 当事人 人员列表 adapter
 * 
 * @author gewx
 *
 */
public class NrcLitigantAdapter extends BaseAdapter {

	private Activity activity;

	/**网上立案 当事人List*/
	private List<TLayyDsr> dsrList;
	
	public NrcLitigantAdapter(Activity activity, List<TLayyDsr> dsrList) {
		this.activity = activity;
		this.dsrList = dsrList;
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
			convertView = inflater.inflate(R.layout.activity_nrc_list_item, null);
			viewHolder.litigantNameTV = (TextView) convertView.findViewById(R.id.nrc_list_item_name);
			viewHolder.infoTipTV = (TextView) convertView.findViewById(R.id.nrc_list_item_info_tip);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TLayyDsr dsr = dsrList.get(position);
		viewHolder.litigantNameTV.setText(dsr.getCName());

		String sqrId = NrcEditData.getLayy().getCSqrId();
		if (NrcCheckUtils.checkLitigantData(sqrId, dsr)) {//检查当事人信息项填写是否完全
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
		 * 当事人 姓名
		 */
		TextView litigantNameTV;
		
		/**
		 * 信息项添加是否完全校验（信息不全）
		 */
		TextView infoTipTV;
	}

	private class ItemOnClickListener implements OnClickListener {

		public int position;

		@Override
		public void onClick(View v) {
			TLayyDsr dsr = dsrList.get(position);
			Intent intent = new Intent();
			intent.setClass(activity, NrcAddLitigantActivity_.class);
			intent.putExtra(NrcAddLitigantActivity.K_LITIGANT, dsr);
			activity.startActivity(intent);
		}
	}
}
