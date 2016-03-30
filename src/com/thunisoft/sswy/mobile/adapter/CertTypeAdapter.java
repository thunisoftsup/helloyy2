package com.thunisoft.sswy.mobile.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 证件类型 adapter
 * @author gewx
 *
 */
public class CertTypeAdapter extends BaseAdapter {

	private Activity activity;
	
	List<Map<String, String>> certTypeList;

	LayoutInflater inflater;

	private ColorStateList selectedColor;

	private ColorStateList normalColor;
	
	public static final String K_SELECTED_CODE = "selectedCode";
	
	private String selectedCode;
	
	public CertTypeAdapter(Activity activity, List<Map<String, String>> certTypeList, String selectedCode) {
		this.activity = activity;
		this.certTypeList = certTypeList;
		this.selectedCode = selectedCode;
		inflater = LayoutInflater.from(activity);
		Resources res = activity.getResources();
		selectedColor = (ColorStateList) res.getColorStateList(R.color.nrc_dialog_item_selected_color);
		normalColor = (ColorStateList) res.getColorStateList(R.color.nrc_dialog_item_normal_color);
	}

	@Override
	public int getCount() {
		return certTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return certTypeList.get(position);
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
			convertView = inflater.inflate(R.layout.dialog_list_item, null);
			viewHolder.certNameTV = (TextView)convertView.findViewById(R.id.dialog_list_item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		Map<String, String> certTypeMap = certTypeList.get(position);
		viewHolder.certNameTV.setText(certTypeMap.get(NrcUtils.KEY_NAME));
		if (certTypeMap.get(NrcUtils.KEY_CODE).equals(selectedCode)) {
			viewHolder.certNameTV.setTextColor(selectedColor);
		} else {
			viewHolder.certNameTV.setTextColor(normalColor);
		}
		ItemOnClick itemOnClick = new ItemOnClick();
		itemOnClick.position = position;
		convertView.setOnClickListener(itemOnClick);
		return convertView;
	}
	
	/**
	 * 证件类型 点击选中
	 * @author gewx
	 *
	 */
	private class ItemOnClick implements OnClickListener {

    	public int position;
    	
		@Override
		public void onClick(View v) {
			Map<String, String> certTypeMap = certTypeList.get(position);
			Intent intent = new Intent();
			intent.putExtra(K_SELECTED_CODE, certTypeMap.get(NrcUtils.KEY_CODE));
			activity.setResult(Constants.RESULT_OK, intent);
			activity.finish();
		}
    }
	
	private static class ViewHolder {

		/**
		 * 证件 名称
		 */
		TextView certNameTV;
	}
}