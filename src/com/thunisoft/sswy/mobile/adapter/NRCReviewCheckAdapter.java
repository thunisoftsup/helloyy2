package com.thunisoft.sswy.mobile.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NRCReviewCheckActivity;
import com.thunisoft.sswy.mobile.pojo.TLayySh;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案查看界面的审核列表adapter
 * 
 */

public class NRCReviewCheckAdapter extends BaseAdapter {

	private static final String TAG = "NRCReviewCheckAdapter";

	private NRCReviewCheckActivity activity;

	private List<TLayySh> checkList = new ArrayList<TLayySh>();
	
	private Map<Integer, String> checkStatusMap;

	public NRCReviewCheckAdapter(NRCReviewCheckActivity activity, List<TLayySh> ShList) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.checkList = ShList;
		checkStatusMap = NrcUtils.getCheckStatusMap();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return checkList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return checkList.get(position);
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(activity);
			convertView = inflater.inflate(R.layout.nrc_review_check_item, null);
			viewHolder.checkResult = (TextView) convertView.findViewById(R.id.check_result);
			viewHolder.checkTime = (TextView) convertView.findViewById(R.id.check_time);
			viewHolder.checkBhyy = (TextView) convertView.findViewById(R.id.check_bhyy);
			viewHolder.checkUser = (TextView) convertView.findViewById(R.id.check_user);
			viewHolder.checkDescription = (TextView) convertView.findViewById(R.id.check_description);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TLayySh checkInfo = checkList.get(position);
		viewHolder.checkResult.setText(NrcUtils.getCheckResultByCode(checkInfo.getNShjg()));
		viewHolder.checkResult.setBackgroundColor(Color.parseColor(checkStatusMap.get(checkInfo.getNShjg())));
		viewHolder.checkTime.setText("更新于：" + NrcUtils.getFormatDate(checkInfo.getDShsj()));
		if (NrcUtils.CHECK_RESULT_NO == checkInfo.getNShjg()) {
			viewHolder.checkBhyy.setVisibility(View.VISIBLE);
			viewHolder.checkBhyy.setText("驳回原因：" + checkInfo.getCBhyy());
		} else {
			viewHolder.checkBhyy.setVisibility(View.GONE);
		}
		viewHolder.checkUser.setText("审核人：" + checkInfo.getCShrName() + "");
		viewHolder.checkDescription.setText("审核意见：" + checkInfo.getCShyj() + "");

		return convertView;
	}

	public class ViewHolder {
		
		public TextView checkResult;
		public TextView checkTime;
		public TextView checkBhyy;
		public TextView checkUser;
		public TextView checkDescription;
	}

}
