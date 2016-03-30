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
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddLegalCertActivity_;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddNaturalCertActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddNaturalCertActivity_;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddNonLegalCertActivity_;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 证件所属人_当事人  adapter
 * @author gewx
 *
 */
public class NrcCertOwnLitigantAdapter extends BaseAdapter {

	private Activity activity;
	
	private List<TLayyDsr> dsrList;
	
	public NrcCertOwnLitigantAdapter(Activity activity, List<TLayyDsr> dsrList) {
		this.activity = activity;
		this.dsrList = dsrList;
	}
	
	@Override
	public int getCount() {
		return dsrList.size();
	}

	@Override
	public TLayyDsr getItem(int position) {
		return dsrList.get(position);
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
			viewHolder.nameTV = (TextView)convertView.findViewById(R.id.nrc_coi_name);
			viewHolder.uploadStatusLinLayout = (LinearLayout)convertView.findViewById(R.id.nrc_coi_upload_status);
			viewHolder.uploadStatusTV = (TextView)convertView.findViewById(R.id.nrc_coi_upload_status_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		TLayyDsr dsr = dsrList.get(position);
		int certificateCount = 0; //证件复印件上传的数量 ，当事人只需上传 证件正反面照
		List<TLayySscl> ssclList = NrcEditData.getCertificateListMap().get(dsr.getCId());
		if (null != ssclList && ssclList.size() > 0) {
			if (Constants.LITIGANT_TYPE_LEGAL == dsr.getNType()) {
				for (TLayySscl sscl : ssclList) {
					if (NrcUtils.ZJ_YYZZ_FYJ.equals(sscl.getCName())) {
						List<TLayySsclFj> yyzzFyjFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != yyzzFyjFjList && yyzzFyjFjList.size() > 0) {
							certificateCount++;
						}
					} else if (NrcUtils.ZJ_FDDBR_SF_ZMS.equals(sscl.getCName())) {
						List<TLayySsclFj> frSfZmsFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != frSfZmsFjList && frSfZmsFjList.size() > 0) {
							certificateCount++;
						}
					}
				}
				
			} else if (Constants.LITIGANT_TYPE_NON_LEGAL == dsr.getNType()) {
				for (TLayySscl sscl : ssclList) {
					if (NrcUtils.ZJ_ZZJG_DMZ_FYJ.equals(sscl.getCName())) {
						List<TLayySsclFj> zzjgDmzFyjFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != zzjgDmzFyjFjList && zzjgDmzFyjFjList.size() > 0) {
							certificateCount++;
						}
					} else if (NrcUtils.ZJ_FZR_ID_ZMS.equals(sscl.getCName())) {
						List<TLayySsclFj> fzrSfZmsFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != fzrSfZmsFjList && fzrSfZmsFjList.size() > 0) {
							certificateCount++;
						}
					}
				}
			} else {
				String certificateName = NrcUtils.getCertificateNameByCode(dsr.getNIdcardType());
				String zmFyjName = certificateName + NrcUtils.ZJ_SUFFIX_ZM;
				String bmFyjName = certificateName + NrcUtils.ZJ_SUFFIX_BM;
				for (TLayySscl sscl : ssclList) {
					if (zmFyjName.equals(sscl.getCName())) { //证件正面照
						List<TLayySsclFj> faceFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != faceFjList && faceFjList.size() > 0) {
							certificateCount++;
						}
					} else if (bmFyjName.equals(sscl.getCName())) { //证件背面照
						List<TLayySsclFj> faceFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
						if (null != faceFjList && faceFjList.size() > 0) {
							certificateCount++;
						}
					}
				}
			}
		}
		
		if (0 == certificateCount) {
			viewHolder.uploadStatusTV.setText("未上传");
			viewHolder.uploadStatusTV.setTextColor(Color.parseColor("#db3838"));
		} else if (certificateCount >= 2) {
			viewHolder.uploadStatusTV.setText("已上传 ");
			viewHolder.uploadStatusTV.setTextColor(Color.parseColor("#999999"));
		} else {
			viewHolder.uploadStatusTV.setText("不完整 ");
			viewHolder.uploadStatusTV.setTextColor(Color.parseColor("#ff9800"));
		}
		viewHolder.nameTV.setText(dsr.getCName());
		
		ViewZjListOnClick viewZjListOnClick = new ViewZjListOnClick();
		viewZjListOnClick.position = position;
		viewHolder.uploadStatusLinLayout.setOnClickListener(viewZjListOnClick);
		return convertView;
	}
	
   static class ViewHolder {
		
		/**
		 * 当事人 名称
		 */
		TextView nameTV;
		
		/**
		 * 当事人 点击查看证件
		 */
		LinearLayout uploadStatusLinLayout;
		
		/**
		 * 当事人 上传证件 状态 提示 （未上传、已上传）
		 */
		TextView uploadStatusTV;
	}
   
    /**
     * 查看证件所属人，证件列表
     * @author gewx
     *
     */
    private class ViewZjListOnClick implements OnClickListener {

    	public int position;
    	
		@Override
		public void onClick(View v) {
			TLayyDsr dsr = dsrList.get(position);
			Intent intent = new Intent();
			intent.putExtra(NrcAddNaturalCertActivity.K_LITIGANT, dsr);
			if (Constants.LITIGANT_TYPE_LEGAL == dsr.getNType()) {
				intent.setClass(activity, NrcAddLegalCertActivity_.class);
			} else if (Constants.LITIGANT_TYPE_NON_LEGAL == dsr.getNType()) {
				intent.setClass(activity, NrcAddNonLegalCertActivity_.class);
			} else {
				intent.setClass(activity, NrcAddNaturalCertActivity_.class);
			}
			activity.startActivity(intent);
		}
    }
}
