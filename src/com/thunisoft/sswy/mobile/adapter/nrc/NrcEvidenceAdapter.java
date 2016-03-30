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
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddEvidenceActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddEvidenceActivity_;
import com.thunisoft.sswy.mobile.pojo.TZj;

/**
 * 网上立案 证据 adapter
 * 
 * @author gewx
 *
 */
public class NrcEvidenceAdapter extends BaseAdapter {

	private Activity activity;

	private List<TZj> zjList;

	public NrcEvidenceAdapter(Activity activity, List<TZj> zjList) {
		this.activity = activity;
		this.zjList = zjList;
	}

	@Override
	public int getCount() {
		return zjList.size();
	}

	@Override
	public TZj getItem(int position) {
		return zjList.get(position);
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
			viewHolder.ssclNameTV = (TextView) convertView.findViewById(R.id.nrc_list_item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TZj zj = zjList.get(position);
		viewHolder.ssclNameTV.setText(zj.getCName());
		
		ItemOnClickListener itemOnClickListener = new ItemOnClickListener();
		itemOnClickListener.position = position;
		convertView.setOnClickListener(itemOnClickListener);
		return convertView;
	}

	private static class ViewHolder {

		/**
		 * 诉讼材料 名称
		 */
		TextView ssclNameTV;
	}

	private class ItemOnClickListener implements OnClickListener {

		public int position;

		@Override
		public void onClick(View v) {
			TZj zj = zjList.get(position);
			Intent intent = new Intent();
			intent.setClass(activity, NrcAddEvidenceActivity_.class);
			intent.putExtra(NrcAddEvidenceActivity.K_ZJ, zj);
			activity.startActivity(intent);
		}
	}
}
