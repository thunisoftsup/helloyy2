package com.thunisoft.sswy.mobile.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NRCReviewIndictmentActivity;
import com.thunisoft.sswy.mobile.interfaces.IAttachmentOpenListener;
import com.thunisoft.sswy.mobile.interfaces.IWaitingDialogNotifier;
import com.thunisoft.sswy.mobile.logic.net.DownLoadAttachmentUtil;
import com.thunisoft.sswy.mobile.logic.net.NRCReviewResponseUtil;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.model.TLayySsclInfo;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.view.NoScrollListView;

/**
 * 网上立案查看界面的诉讼状列表adapter
 * 	
 */

public class NRCReviewIndictmentAdapter extends BaseAdapter{
	
	private static final String TAG = "NRCReviewIndictmentAdapter";

	private NRCReviewIndictmentActivity activity;
	
	private IndictmentFileAdapter indictmentFileAdapter;
	
	private NRCReviewResponseUtil responseUtil;
	
	private IWaitingDialogNotifier waitingDialogNotifier;
	
	private List<TLayySsclInfo> indictmentList = new ArrayList<TLayySsclInfo>();
	
	private Map<String,String> plaintiffIdMap = new HashMap<String,String>();
	
	
	public NRCReviewIndictmentAdapter(NRCReviewIndictmentActivity activity,List<TLayySsclInfo> infoList,
			Map<String,String> plaintiffIdMap,NRCReviewResponseUtil responseUtil) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.indictmentList = infoList;
		this.responseUtil = responseUtil;
		this.plaintiffIdMap = plaintiffIdMap;

	}
	
    public void setWaitingDialogNotifier(IWaitingDialogNotifier waitingDialogNotifier) {
        this.waitingDialogNotifier = waitingDialogNotifier;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return indictmentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return indictmentList.get(position);
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
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.nrc_review_indictment_item, null);
			viewHolder.indictmentNameTV = (TextView) convertView.findViewById(R.id.indictment_name);
			viewHolder.indictmentPlaintiffTV = (TextView) convertView.findViewById(R.id.indictment_plaintiff);
			viewHolder.indictmentDefendantTV = (TextView) convertView.findViewById(R.id.indictment_defendant);
			viewHolder.recyclerView = (NoScrollListView) convertView.findViewById(R.id.indictment_image);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		TLayySsclInfo indictmentInfo = indictmentList.get(position);
		TLayySscl sscl = indictmentInfo.getSscl();
        List<TLayySsclFj> ssclfjList = indictmentInfo.getSsclfjList();
        List<TLayyDsrSscl> ssdsrClList = indictmentInfo.getSsdsrClList();
        viewHolder.indictmentNameTV.setText("材料名称："+sscl.getCName());
        String plaintiff = "";
        String defendant = "";
        for(TLayyDsrSscl dsrSscl:ssdsrClList){
        	if(plaintiffIdMap.containsKey(dsrSscl.getCDsrId())){
        		plaintiff = plaintiff + (plaintiff.equals("")?"":"、") + dsrSscl.getCDsrName();
        	}else{
        		defendant = defendant + (defendant.equals("")?"":"、") + dsrSscl.getCDsrName();
        	}
        }

        viewHolder.indictmentPlaintiffTV.setText("原告："+plaintiff);
        viewHolder.indictmentDefendantTV.setText("被告："+defendant);
		indictmentFileAdapter = new IndictmentFileAdapter(ssclfjList);
		viewHolder.recyclerView.setAdapter(indictmentFileAdapter);
		return convertView;
	}
	
	public class IndictmentFileAdapter extends BaseAdapter{

		public List<TLayySsclFj> attachmentList;
		
		public IndictmentFileAdapter(List<TLayySsclFj> attachmentList) {
			// TODO Auto-generated constructor stub
			this.attachmentList = attachmentList;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return attachmentList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return attachmentList.get(position);
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
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				convertView = inflater.inflate(R.layout.nrc_review_attachment_item, null);
				viewHolder.attachmentNameTV = (TextView) convertView.findViewById(R.id.attachment_name);
				convertView.setTag(viewHolder);
				
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.attachmentNameTV.setText("文件名称："+attachmentList.get(position).getCOriginName());
			viewHolder.attachmentNameTV.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TLayySsclFj tempAttachment = responseUtil.getSsclFjFromDb(attachmentList.get(position).getCId());
					if(checkWsExists(tempAttachment)){
						openAttachment(tempAttachment.getCPath());
					}else{
						downLoadAttachment(position);
					}
				}
			});
			return convertView;
		}
		
		public void downLoadAttachment(int position){
			TLayySsclFj attachment = attachmentList.get(position);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("layyId", attachment.getCLayyId()));
			params.add(new BasicNameValuePair("ssclFjId", attachment.getCId()));
			params.add(new BasicNameValuePair("type", "2"));
//			String url = netUtils.getMainAddress()+"/api/wsla/downloadSscl";
			String url = responseUtil.getServerAddress()+"/api/wsla/downloadSscl";
			waitingDialogNotifier.showDialog("下载中...");
			DownLoadAttachmentUtil util = new DownLoadAttachmentUtil(params, attachment.getCOriginName(),attachment.getCId(),responseUtil);
			util.setWaitingDialogNotifier(waitingDialogNotifier);
			util.setAttachmentOpenlistener(listener);
			util.execute(url);								
		}
		
		public IAttachmentOpenListener listener = new IAttachmentOpenListener(){

			@Override
			public void open(String path) {
				// TODO Auto-generated method stub
				openAttachment(path);
			}
			
		};
		
		public void openAttachment(String path) {
			Intent intent = null;
			intent = ResponseUtilExtr.openFileWithAllPath(path);
			try {
				activity.startActivity(intent);
			} catch (Exception e) {
				Log.e("", "打开文件出错", e);
				activity.showToast("打开文件出错");
			}
		}
		
		public boolean checkWsExists(TLayySsclFj tempAttachment) {
			if(tempAttachment == null) {
				return false;
			}
			if(StringUtils.isBlank(tempAttachment.getCPath())) {
				return false;
			}
			File file = new File(tempAttachment.getCPath());
			if(!file.exists() || !file.isFile()) {
				return false;
			}
			
			return true;
		}
		
		public class ViewHolder{
			public TextView attachmentNameTV;
		}
	}
    
	public class ViewHolder{
		public TextView indictmentNameTV;
		public TextView indictmentPlaintiffTV;
		public TextView indictmentDefendantTV;
		public NoScrollListView recyclerView;
	}
	


}
