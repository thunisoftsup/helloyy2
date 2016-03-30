package com.thunisoft.sswy.mobile.activity.dialog.nrc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 代理人类型 列表页面
 * 
 * @author gewx
 * 
 */
@EActivity(R.layout.dialog_agent_type)
public class AgentTypeDioagActivity extends Activity {

	/**代理人类型 列表*/
    @ViewById(R.id.dialog_agent_type_list)
    ListView agentTypelistView;

    private AgentTypeAdapter agentTypeAdapter;
    
    public static final String K_AGENT_TYPE_CODE = "code";
    
    private int agentTypeCode;
    
    private List<Map<String, String>> agentMapList = new ArrayList<Map<String,String>>();
    
    @AfterViews
    public void onAfterViews() {
	    setFinishOnTouchOutside(true);// 设置为true点击区域外消失   
    	Intent intent = getIntent();
    	agentTypeCode = intent.getIntExtra(K_AGENT_TYPE_CODE, Constants.AGENT_TYPE_LAWYER);
    	refreshAgentType();
    }
    
    /**
     * 刷新 代理人 列表
     */
    private void refreshAgentType() {
    	agentMapList.clear();
    	agentMapList.addAll(NrcUtils.getAgentTypeList());
    	if (null == agentTypeAdapter) {
    		agentTypeAdapter = new AgentTypeAdapter(this, agentMapList);
    		agentTypelistView.setAdapter(agentTypeAdapter);
    	} else {
    		agentTypeAdapter.notifyDataSetChanged();
    	}
    }
    
    private class AgentTypeAdapter extends BaseAdapter {

    	private Context context;
    	
    	private List<Map<String, String>> agentTypeList;
    	
    	public AgentTypeAdapter(Context context, List<Map<String, String>> agentTypeList) {
    		
    		this.context = context;
    		this.agentTypeList = agentTypeList;
    	}
    	
		@Override
		public int getCount() {
			return agentTypeList.size();
		}

		@Override
		public Object getItem(int position) {
			return agentTypeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				LayoutInflater inflater = LayoutInflater.from(context);
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.dialog_agent_type_item, null);
				viewHolder.agentTypeNameTV = (TextView)convertView.findViewById(R.id.dialog_agent_type_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			Map<String, String> agentTypeMap = agentTypeList.get(position);
			viewHolder.agentTypeNameTV.setText(agentTypeMap.get(NrcUtils.KEY_NAME));
			
			ItemOnClick itemOnClick = new ItemOnClick();
			itemOnClick.position = position;
			convertView.setOnClickListener(itemOnClick);
			
            if (agentTypeMap.get(NrcUtils.KEY_CODE).equals(agentTypeCode)) {
            	convertView.setBackgroundResource(R.color.province_item_default);
			} else {
				convertView.setBackgroundResource(R.color.white);
			}
			return convertView;
		}
    	
		/**
		 * 代理人类型 点击选中
		 * @author gewx
		 *
		 */
		private class ItemOnClick implements OnClickListener {

	    	public int position;
	    	
			@Override
			public void onClick(View v) {
				Map<String, String> agentTypeMap = agentTypeList.get(position);
				Intent intent = new Intent();
				String currCode = agentTypeMap.get(NrcUtils.KEY_CODE);
				intent.putExtra(AgentTypeDioagActivity.K_AGENT_TYPE_CODE, Integer.parseInt(currCode));
				AgentTypeDioagActivity.this.setResult(Constants.RESULT_OK, intent);
				AgentTypeDioagActivity.this.finish();
			}
	    }
    }
    
    private static class ViewHolder {
		
		/**
		 * 代理人类型 名称
		 */
		TextView agentTypeNameTV;
	}
}
