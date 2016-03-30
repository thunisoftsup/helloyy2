package com.thunisoft.sswy.mobile.adapter.nrc;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddAgentCertActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddAgentCertActivity_;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddOtherAgentCertActivity_;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 证件所属人_代理人 adapter
 * 
 * @author gewx
 *
 */
public class NrcCertOwnAgentAdapter extends BaseAdapter {

	private Activity activity;

	private List<TLayyDlr> dlrList;
	
	

	public NrcCertOwnAgentAdapter(Activity activity, List<TLayyDlr> dlrList) {
		this.activity = activity;
		this.dlrList = dlrList;
	}

	@Override
	public int getCount() {
		return dlrList.size();
	}

	@Override
	public TLayyDlr getItem(int position) {
		return dlrList.get(position);
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
			convertView = inflater.inflate(R.layout.activity_nrc_certificate_owner_item, null);
			viewHolder.nameTV = (TextView) convertView.findViewById(R.id.nrc_coi_name);
			viewHolder.uploadStatusLinLayout = (LinearLayout) convertView.findViewById(R.id.nrc_coi_upload_status);
			viewHolder.uploadStatusTV = (TextView) convertView.findViewById(R.id.nrc_coi_upload_status_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TLayyDlr dlr = dlrList.get(position);
		int needCertNum = 1;
		int certificateCount = 0; // 证件复印件上传的数量
		int otherCount = 0; // 其它复印件上传的数量，代理人需要上传 执业证 照片 和 原告授权委托书
		List<TLayySscl> ssclList = NrcEditData.getCertificateListMap().get(dlr.getCId());
		if (null != ssclList && ssclList.size() > 0) {
			for (TLayySscl sscl : ssclList) {
				if (Constants.AGENT_TYPE_ASSIST_LAWYER == dlr.getNDlrType()
						|| Constants.AGENT_TYPE_LAWYER == dlr.getNDlrType()) {
					if (NrcUtils.AGENT_CERT_TIP.equals(sscl.getCName())) { // 身份证正面照
						List<TLayySsclFj> faceFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != faceFjList && faceFjList.size() > 0) {
							certificateCount++;
						}
					} else {
						List<TLayySsclFj> faceFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != faceFjList && faceFjList.size() > 0) {
							otherCount++;
						}
					}
				} else {
					needCertNum = 2;
					if (NrcUtils.AGENT_IDENTFY_TIP.equals(sscl.getCName())) {
						List<TLayySsclFj> faceFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != faceFjList && faceFjList.size() > 0) {
							certificateCount++;
						}
					} else if (NrcUtils.AGENT_TELLER_CERT_TIP.equals(sscl.getCName())) {
						List<TLayySsclFj> faceFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != faceFjList && faceFjList.size() > 0) {
							certificateCount++;
						}
					} else {
						List<TLayySsclFj> faceFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != faceFjList && faceFjList.size() > 0) {
							otherCount++;
						}
					}
				}
			}
		}
		int licenseAllCount = 0;
		if (Constants.AGENT_TYPE_JHR != dlr.getNDlrType()) {
			if (StringUtils.isNotBlank(dlr.getCBdlrId())) {
				licenseAllCount = dlr.getCBdlrId().split(NrcConstants.REL_NAME_SPLIT).length;
			}
		}
		if (0 == certificateCount && 0 == otherCount) {
			viewHolder.uploadStatusTV.setTextColor(Color.parseColor("#db3838"));
			viewHolder.uploadStatusTV.setText("未上传");
		} else if (certificateCount >= needCertNum && otherCount >= licenseAllCount) {
			viewHolder.uploadStatusTV.setTextColor(Color.parseColor("#999999"));
			viewHolder.uploadStatusTV.setText("已上传 ");
		} else {
			viewHolder.uploadStatusTV.setTextColor(Color.parseColor("#ff9800"));
			viewHolder.uploadStatusTV.setText("不完整 ");
		}

		viewHolder.nameTV.setText(dlr.getCName());

		ViewZjListOnClick viewZjListOnClick = new ViewZjListOnClick();
		viewZjListOnClick.position = position;
		viewHolder.uploadStatusLinLayout.setOnClickListener(viewZjListOnClick);
		return convertView;
	}

	static class ViewHolder {

		/**
		 * 代理人 名称
		 */
		TextView nameTV;

		/**
		 * 代理人 点击查看证件
		 */
		LinearLayout uploadStatusLinLayout;

		/**
		 * 代理人 上传证件 状态 提示 （未上传、已上传）
		 */
		TextView uploadStatusTV;
	}

	/**
	 * 查看证件所属人，证件列表
	 * 
	 * @author gewx
	 *
	 */
	private class ViewZjListOnClick implements OnClickListener {

		public int position;

		@Override
		public void onClick(View v) {
			TLayyDlr dlr = dlrList.get(position);
			Intent intent = new Intent();
			intent.putExtra(NrcAddAgentCertActivity.K_AGENT, dlr);
			if (Constants.AGENT_TYPE_ASSIST_LAWYER == dlr.getNDlrType() 
					|| Constants.AGENT_TYPE_LAWYER == dlr.getNDlrType()) {
				intent.setClass(activity, NrcAddAgentCertActivity_.class);
			} else {
				intent.setClass(activity, NrcAddOtherAgentCertActivity_.class);
			}
			activity.startActivity(intent);
		}
	}
}
