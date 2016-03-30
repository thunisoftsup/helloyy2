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

import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 网上立案查看界面的代理人列表adapter
 * 
 */

public class NRCReviewAgentAdapter extends BaseAdapter{
	
	private static final String TAG = "NRCReviewAgentAdapter";
	
	private NetRegisterCaseReviewActivity activity;
	
	private List<TLayyDlr> agentList;
	
	public void setAgentList(List<TLayyDlr> agentList) {
		this.agentList = agentList;
	}

	public NRCReviewAgentAdapter(NetRegisterCaseReviewActivity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return agentList == null?0:agentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return agentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(activity);
			convertView = inflater.inflate(R.layout.nrc_review_agent_witness_item, null);
			viewHolder.agentName = (TextView) convertView.findViewById(R.id.name);
			viewHolder.otherName = (TextView) convertView.findViewById(R.id.other_name);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity,NRCReviewSingleDataActivity_.class);
				intent.putExtra("single_data_type", 1);
				intent.putExtra("agent", agentList.get(position));
				activity.startActivity(intent);
			}
		});
		viewHolder.agentName.setText("代理人："+agentList.get(position).getCName());
		if(agentList.get(position).getCBdlrMc().equals("")){
			viewHolder.otherName.setVisibility(View.GONE);
		}else{
			viewHolder.otherName.setText("被代理人："+agentList.get(position).getCBdlrMc());					
		}
		return convertView;
	}
	
	public class ViewHolder{
		
		public TextView agentName;
		public TextView otherName;
	}

}
