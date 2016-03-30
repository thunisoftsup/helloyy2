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

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddWitnessActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddWitnessActivity_;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;

/**
 * 网上立案 证人 人员列表 adapter
 * @author gewx
 *
 */
public class NrcWitnessAdapter extends BaseAdapter {

	private Activity activity;
	
	private List<TLayyZr> zrList;
	
	public NrcWitnessAdapter(Activity activity, List<TLayyZr> zrList) {
		this.activity = activity;
		this.zrList = zrList;
	}
	
	@Override
	public int getCount() {
		return zrList.size();
	}

	@Override
	public TLayyZr getItem(int position) {
		return zrList.get(position);
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
			viewHolder.wintnessNameTV = (TextView)convertView.findViewById(R.id.nrc_list_item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		TLayyZr dlr = zrList.get(position);
		viewHolder.wintnessNameTV.setText(dlr.getCName());	
		
		ItemOnClickListener itemOnClickListener = new ItemOnClickListener();
		itemOnClickListener.position = position;
		convertView.setOnClickListener(itemOnClickListener);
		
		return convertView;
	}
	
   private static class ViewHolder {
		
		/**
		 * 证人 姓名
		 */
		TextView wintnessNameTV;
	}
   
   private class ItemOnClickListener implements OnClickListener {
	   
	   public int position;

	   @Override
	   public void onClick(View v) {
		   
		   TLayyZr zr = zrList.get(position);
		    Intent intent = new Intent();
		    intent.setClass(activity, NrcAddWitnessActivity_.class);
		    intent.putExtra(NrcAddWitnessActivity.K_WITNESS, zr);
		    activity.startActivity(intent);
	   }
	   
	   
   }
}
