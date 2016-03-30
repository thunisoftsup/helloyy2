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
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddIndictmentActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddIndictmentActivity_;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;

/**
 * 网上立案 起诉状列表 adapter
 * @author gewx
 *
 */
public class NrcIndictmentAdapter extends BaseAdapter {

	private Activity activity;
	
	private List<TLayySscl> ssclList;
	
	public NrcIndictmentAdapter(Activity activity, List<TLayySscl> ssclList) {
		this.activity = activity;
		this.ssclList = ssclList;
	}
	
	@Override
	public int getCount() {
		return ssclList.size();
	}

	@Override
	public TLayySscl getItem(int position) {
		return ssclList.get(position);
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
			viewHolder.ssclNameTV = (TextView)convertView.findViewById(R.id.nrc_list_item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		TLayySscl sscl = ssclList.get(position);
		viewHolder.ssclNameTV.setText(sscl.getCName());	
		
		ItemOnClickListener itemOnClickListener = new ItemOnClickListener();
		itemOnClickListener.position = position;
		convertView.setOnClickListener(itemOnClickListener);
		
		return convertView;
	}
	
   private static class ViewHolder {
		
		/**
		 * 诉讼材料名称
		 */
		TextView ssclNameTV;
	}
   
   private class ItemOnClickListener implements OnClickListener {
	   
	    public int position;

		@Override
		public void onClick(View v) {

			TLayySscl sscl = ssclList.get(position);
			Intent intent = new Intent();
			intent.setClass(activity, NrcAddIndictmentActivity_.class);
			intent.putExtra(NrcAddIndictmentActivity.K_SSCL, sscl);
			activity.startActivity(intent);
		}
   }
}
