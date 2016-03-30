package com.thunisoft.sswy.mobile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NRCReviewLitigantActivity;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案查看界面的当事人列表adapter
 * 
 */

public class NRCReviewLitigantAdapter extends BaseAdapter{
	
	private static final String TAG = "NRCReviewLitigantAdapter";
	
	private NRCReviewLitigantActivity activity;
	
	private List<TLayyDsr> litigantList = new ArrayList<TLayyDsr>();
	
	public NRCReviewLitigantAdapter(NRCReviewLitigantActivity activity,List<TLayyDsr> litigantList) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.litigantList = litigantList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return litigantList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return litigantList.get(position);
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
			convertView = inflater.inflate(R.layout.nrc_review_litigant_item, null);
			viewHolder.litigantName = (TextView) convertView.findViewById(R.id.litigant_name);
			viewHolder.litigantType = (TextView) convertView.findViewById(R.id.litigant_type);
			viewHolder.litigantDetail = (TextView) convertView.findViewById(R.id.litigant_detail);
			viewHolder.litigantCardInfo = (LinearLayout) convertView.findViewById(R.id.litigant_card_info);
			viewHolder.litigantCard = (TextView) convertView.findViewById(R.id.litigant_card);
			viewHolder.litigantBrithdayInfo = (LinearLayout) convertView.findViewById(R.id.litigant_brithday_info);
			viewHolder.litigantBrithday = (TextView) convertView.findViewById(R.id.litigant_brithday);
			viewHolder.litigantPhoneInfo = (LinearLayout) convertView.findViewById(R.id.litigant_phone_info);
			viewHolder.litigantPhone = (TextView) convertView.findViewById(R.id.litigant_phone);
			viewHolder.litigantAddressInfo = (LinearLayout) convertView.findViewById(R.id.litigant_address_info);
			viewHolder.litigantAddress = (TextView) convertView.findViewById(R.id.litigant_address);
			viewHolder.representativeInfo =  (LinearLayout) convertView.findViewById(R.id.representative_info);
			viewHolder.representativeName = (TextView) convertView.findViewById(R.id.representative_name);
			viewHolder.representativePhoneInfo =  (LinearLayout) convertView.findViewById(R.id.representative_phone_info);
			viewHolder.representativePhone = (TextView) convertView.findViewById(R.id.representative_phone);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		TLayyDsr litigant = litigantList.get(position);
		int nType = litigant.getNType();
		if(nType ==NrcConstants.DSR_TYPE_NORMAL){
			int nXb = litigant.getNXb();
			Integer nAge = litigant.getNAge();
			String cAge = "";
			if (nAge == null || Integer.valueOf(0).equals(nAge)) {
				cAge = "";
			} else {
				cAge = String.valueOf(nAge) + "岁";
			}
			viewHolder.litigantDetail.setText(NrcUtils.getGenderNameByCode(nXb)+" "+ cAge);
			String cIdcard = litigant.getCIdcard();
			int nIdcardType = litigant.getNIdcardType();
			String cAddress = litigant.getCAddress();//北京市-北京市西城区-2
			viewHolder.litigantAddress.setText(cAddress);
			if(cIdcard.equals("")){
				viewHolder.litigantCardInfo.setVisibility(View.GONE);
			}else{
				viewHolder.litigantCard.setText(NrcUtils.getCertificateNameByCode(nIdcardType)+":"+cIdcard);										
			}
			String dCsrq = litigant.getDCsrq();
			if(dCsrq == null){
				viewHolder.litigantBrithdayInfo.setVisibility(View.GONE);
			}else{
				viewHolder.litigantBrithday.setText(NrcUtils.getFormatDate(dCsrq));					
			}
			String cSjhm = litigant.getCSjhm();
			if(cSjhm.equals("")){
				viewHolder.litigantPhoneInfo.setVisibility(View.GONE);
			}else{
				viewHolder.litigantPhone.setText(cSjhm);					
			}
			viewHolder.representativeInfo.setVisibility(View.GONE);
		}else{
			viewHolder.litigantDetail.setVisibility(View.GONE);
			viewHolder.litigantCardInfo.setVisibility(View.GONE);
			viewHolder.litigantBrithdayInfo.setVisibility(View.GONE);
			String cCompanyAddress = litigant.getCDwdz();//北京市-北京市西城区-2
			viewHolder.litigantAddress.setText(cCompanyAddress);
			String cLxdh = litigant.getCLxdh();
			if(cLxdh.equals("")){
				viewHolder.litigantPhoneInfo.setVisibility(View.GONE);
			}else{				
				viewHolder.litigantPhone.setText(cLxdh);
			}
			String cFddbr = litigant.getCFddbr();
			if(cFddbr.equals("")){
				viewHolder.representativeName.setVisibility(View.GONE);
			}else{
				String representativeType = (nType == NrcConstants.DSR_TYPE_CORPORATION?"法定人代表：":"负责人代表：") + cFddbr;
				viewHolder.representativeName.setText(representativeType);
				String cFddbrSjhm = litigant.getCFddbrSjhm();
				if(cFddbrSjhm.equals("")){
					viewHolder.representativePhoneInfo.setVisibility(View.GONE);
				}else{					
					viewHolder.representativePhone.setText(cFddbrSjhm);	
				}
			}
		}
		String cName = litigant.getCName();
		viewHolder.litigantName.setText(cName);
		viewHolder.litigantType.setText(NrcUtils.getLitigantTypeNameByCode(nType));
		return convertView;
	}
	
	public class ViewHolder{
		
		public TextView litigantName;
		public TextView litigantType;
		public TextView litigantDetail;
		public LinearLayout litigantCardInfo;
		public TextView litigantCard;
		public LinearLayout litigantBrithdayInfo;
		public TextView litigantBrithday;
		public LinearLayout litigantPhoneInfo;
		public TextView litigantPhone;
		public LinearLayout litigantAddressInfo;
		public TextView litigantAddress;
		public LinearLayout representativeInfo;
		public TextView representativeName;
		public LinearLayout representativePhoneInfo;
		public TextView representativePhone;
		
	}

}
