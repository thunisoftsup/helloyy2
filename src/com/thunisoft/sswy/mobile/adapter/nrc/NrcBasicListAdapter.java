package com.thunisoft.sswy.mobile.adapter.nrc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NetRegisterCaseBasicActivity;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 填写网上立案_基本信息（案件类型、审判角色、申请人类型） adapter
 * @author gewx
 *
 */
public class NrcBasicListAdapter extends BaseAdapter{

	private NetRegisterCaseBasicActivity nrcBasicActivity;
	
	/**
	 * 需要传入的数据
	 */
	private List<Map<String, String>> dataMapList;
	
	/**
	 * 选中的 item
	 */
	private Map<String, Map<String, String>> selectedItemMap = new HashMap<String, Map<String, String>>();
	
	/**
	 * 是否为多选
	 */
	private boolean isMultiselect = false;
	
	/**
	 * 是否为申请类型
	 */
	private boolean isApplicantType = false;
	
	/**
	 * 申请人类型：为他人申请
	 */
	private static final int APPLICANT_TYPE_OTHER = 2;
	
	/**
	 * （案件类型、审判角色、申请人类型） adapter
	 * @param context
	 * @param itemBeanList 传入的数据
	 * @param isMultiselect 是否为多选
	 */
	public NrcBasicListAdapter(NetRegisterCaseBasicActivity nrcBasicActivity, List<Map<String, String>> itemBeanList, boolean isMultiselect) {
		this.nrcBasicActivity = nrcBasicActivity;
		this.dataMapList = itemBeanList;
		this.isMultiselect = isMultiselect;
	}
	
	@Override
	public int getCount() {
		return dataMapList.size();
	}

	@Override
	public Map<String, String> getItem(int position) {
		return dataMapList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(nrcBasicActivity);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_nrc_basic_item_radio, null);
			viewHolder.basicItemRB = (RadioButton)convertView.findViewById(R.id.nrc_basic_item_radio);
			viewHolder.basicItemTV = (TextView)convertView.findViewById(R.id.nrc_basic_item_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
			
		Map<String, String> itemMap = dataMapList.get(position);
		if (null != selectedItemMap.get(itemMap.get(NrcUtils.KEY_CODE))) {
			viewHolder.basicItemRB.setChecked(true);
		} else {
			viewHolder.basicItemRB.setChecked(false);
		}
		viewHolder.basicItemTV.setText(itemMap.get(NrcUtils.KEY_NAME));
		ItemOnClick itemOnClick = new ItemOnClick();
		itemOnClick.position = position;
		convertView.setOnClickListener(itemOnClick);
		return convertView;
	}

	/**
	 * 网上立案基本信息_adapter item
	 * @author gewx
	 *
	 */
	private static class ViewHolder {
		
		/**
		 * 单选图标
		 */
		RadioButton basicItemRB;
		
		/**
		 * 要显示的文字
		 * （案件类型、审判角色、申请人类型）
		 */
		TextView basicItemTV;
		
	}
	
	/**
	 * 点击 item
	 * @author gewx
	 *
	 */
	private class ItemOnClick implements OnClickListener {

		public int position;
		
		@Override
		public void onClick(View v) {
			Map<String, String> itemMap = dataMapList.get(position);
			if (isMultiselect) { //多选
				if (null != selectedItemMap.get(itemMap.get(NrcUtils.KEY_CODE))) {
					selectedItemMap.remove(itemMap.get(NrcUtils.KEY_CODE));
				} else {
					selectedItemMap.put(itemMap.get(NrcUtils.KEY_CODE), itemMap);
				}
			} else { //单选
				if (null == selectedItemMap.get(itemMap.get(NrcUtils.KEY_CODE))) {
					selectedItemMap.clear();
					selectedItemMap.put(itemMap.get(NrcUtils.KEY_CODE), itemMap);
				}
			}
			
			if (isApplicantType) { //如果是申请人类型，需要判断申请人类型为：为他人申请，需要显示出，代理人类型
				if (APPLICANT_TYPE_OTHER == Integer.parseInt(itemMap.get(NrcUtils.KEY_CODE))) {
					nrcBasicActivity.agentTypeLinLayout.setVisibility(View.VISIBLE);
					String agentTypeName = NrcUtils.getAgentNameByCode(Constants.AGENT_TYPE_LAWYER);
					nrcBasicActivity.agentTypeTextTV.setText(agentTypeName);
					TLayyDlr layyDlr = nrcBasicActivity.getLayyDlr();
					layyDlr.setNDlrType(Constants.AGENT_TYPE_LAWYER);
				} else {
					nrcBasicActivity.agentTypeLinLayout.setVisibility(View.GONE);
				}
			}
			
			NrcBasicListAdapter.this.notifyDataSetChanged();
		}
	}
	
	/**  
	 * 获取  是否为多选
	 * @return isMultiselect  
	 */
	
	public boolean isMultiselect() {
		return isMultiselect;
	}

	/**  
	 * 设置  是否为多选
	 * @param isMultiselect
	 */
	public void setMultiselect(boolean isMultiselect) {
		this.isMultiselect = isMultiselect;
	}

	/**  
	 * 获取  选中的 item
	 * @return selectedItemBeanMap  
	 */
	
	public Map<String, Map<String, String>> getSelectedItemBeanMap() {
		return selectedItemMap;
	}

	/**  
	 * 设置  选中的 item
	 * @param selectedItemBeanMap
	 */
	public void setSelectedItemBeanMap(Map<String, Map<String, String>> selectedItemBeanMap) {
		this.selectedItemMap = selectedItemBeanMap;
	}

	/**  
	 * 获取  是否为申请类型  
	 * @return isApplicantType  
	 */
	
	public boolean isApplicantType() {
		return isApplicantType;
	}

	/**  
	 * 设置  是否为申请类型  
	 * @param isApplicantType
	 */
	public void setApplicantType(boolean isApplicantType) {
		this.isApplicantType = isApplicantType;
	}
}