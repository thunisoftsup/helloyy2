package com.thunisoft.sswy.mobile.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NRCReviewCheckActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NRCReviewSingleDataActivity_;
import com.thunisoft.sswy.mobile.activity.nrc.NetRegisterCaseReviewActivity;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayySh;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 网上立案查看界面的证人列表adapter
 * 
 */

public class NRCReviewWitnessAdapter extends BaseAdapter{
	
	private static final String TAG = "NRCReviewWitnessAdapter";
	
	private NetRegisterCaseReviewActivity activity;
	
	private List<TLayyZr> witnessList;
	
	public void setWitnessList(List<TLayyZr> witnessList) {
		this.witnessList = witnessList;
	}

	public NRCReviewWitnessAdapter(NetRegisterCaseReviewActivity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return witnessList == null?0:witnessList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return witnessList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(activity);
			convertView = inflater.inflate(R.layout.nrc_review_agent_witness_item, null);
			viewHolder.witnessName = (TextView) convertView.findViewById(R.id.name);
			viewHolder.otherName = (TextView) convertView.findViewById(R.id.other_name);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		final int index = position;
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity,NRCReviewSingleDataActivity_.class);
				intent.putExtra("single_data_type", 2);
				intent.putExtra("witness", witnessList.get(index));
				activity.startActivity(intent);
			}
		});
		viewHolder.witnessName.setText(witnessList.get(position).getCName());
		if(witnessList.get(position).getCYlfMc().equals("")){
			viewHolder.otherName.setVisibility(View.GONE);
		}else{
			viewHolder.otherName.setText("有利方："+witnessList.get(position).getCYlfMc());				
		}
		return convertView;
	}
	
	public class ViewHolder{
		
		public TextView witnessName;
		public TextView otherName;
	}

}
