package com.thunisoft.sswy.mobile.adapter.nrc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.view.NoScrollGridView;

/**
 * 代理人 授权委托书列表 adapter
 * 
 * @author gewx
 *
 */
public class NrcAgentLicenseAdapter extends BaseAdapter {

	private Activity activity;

	private List<TLayySscl> licenseList;

	/** 证件id， List[证件附件] */
	private Map<String, ArrayList<TLayySsclFj>> certFjListMap;
	
	private DownloadLogic downloadLogic;
	
	private DeleteLogic deleteLogic;
	
	/**
	 * 添加授权委托书
	 */
	public static final int REQ_CODE_ADD_LICENSE = 4;
	
	public NrcAgentLicenseAdapter(Activity activity, List<TLayySscl> licenseList, Map<String, ArrayList<TLayySsclFj>> certFjListMap) {
		this.activity = activity;
		this.licenseList = licenseList;
		this.certFjListMap = certFjListMap;
	}

	@Override
	public int getCount() {
		return licenseList.size();
	}

	@Override
	public TLayySscl getItem(int position) {
		return licenseList.get(position);
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
			convertView = inflater.inflate(R.layout.activity_nrc_add_agent_license_item, null);
			viewHolder.nameTV = (TextView) convertView.findViewById(R.id.nrc_aal_name);
			viewHolder.licenseGridView = (NoScrollGridView) convertView.findViewById(R.id.nrc_aal_list);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TLayySscl sscl = getItem(position);
		viewHolder.nameTV.setText(sscl.getCName());
		List<TLayySsclFj> licenseFjList = certFjListMap.get(sscl.getCId());
		NrcSsclFjAdapter licenseFjAdapter = new NrcSsclFjAdapter(activity, sscl, licenseFjList);
		licenseFjAdapter.setSelectType(NrcConstants.SELECT_TYPE_SINGLE);
		licenseFjAdapter.setDownloadLogic(downloadLogic);
		licenseFjAdapter.setDeleteLogic(deleteLogic);
	    licenseFjAdapter.setRequestCode(REQ_CODE_ADD_LICENSE);
	    licenseFjAdapter.setLicensePos(position);
	    licenseFjAdapter.setFjType(NrcSsclFjAdapter.FJ_TYPE_LICENSE);
	    licenseFjAdapter.setSsclFjType(NrcConstants.SSCL_TYPE_CERTIFICATE);
	    viewHolder.licenseGridView.setAdapter(licenseFjAdapter);
		return convertView;
	}

	static class ViewHolder {

		/**
		 * 授权委托书名称
		 */
		TextView nameTV;

		/**
		 * 授权委托书附件
		 */
		NoScrollGridView licenseGridView;
	}

	/**  
	 * 设置  downloadLogic  
	 * @param downloadLogic
	 */
	public void setDownloadLogic(DownloadLogic downloadLogic) {
		this.downloadLogic = downloadLogic;
	}

	/**  
	 * 设置  deleteLogic  
	 * @param deleteLogic
	 */
	public void setDeleteLogic(DeleteLogic deleteLogic) {
		this.deleteLogic = deleteLogic;
	}
}
