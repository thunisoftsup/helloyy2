package com.thunisoft.sswy.mobile.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.thunisoft.sswy.mobile.activity.nrc.NRCReviewEvidenceActivity;
import com.thunisoft.sswy.mobile.interfaces.IAttachmentOpenListener;
import com.thunisoft.sswy.mobile.interfaces.IWaitingDialogNotifier;
import com.thunisoft.sswy.mobile.logic.net.DownLoadAttachmentUtil;
import com.thunisoft.sswy.mobile.logic.net.NRCReviewResponseUtil;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.model.TLayyZjInfo;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.view.NoScrollListView;

/**
 * 网上立案查看界面的证据列表adapter
 * 
 */

public class NRCReviewEvidenceAdapter extends BaseAdapter{
	
	private static final String TAG = "NRCReviewEvidenceAdapter";
	
	private NRCReviewEvidenceActivity activity;
	
	private EvidenceFileAdapter evidenceFileAdapter;
	
	private List<TLayyZjInfo> evidenceList = new ArrayList<TLayyZjInfo>();
	
	private List<TZjcl> attachmentList = new ArrayList<TZjcl>();
	
	private NRCReviewResponseUtil responseUtil;
	
	private IWaitingDialogNotifier waitingDialogNotifier;
	
	
	public NRCReviewEvidenceAdapter(NRCReviewEvidenceActivity activity,List<TLayyZjInfo> evidenceList,NRCReviewResponseUtil responseUtil) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.evidenceList = evidenceList;
		this.responseUtil = responseUtil;

		loadData();

	}
	
    public void setWaitingDialogNotifier(IWaitingDialogNotifier waitingDialogNotifier) {
        this.waitingDialogNotifier = waitingDialogNotifier;
    }
	
	public void loadData(){
		for(int i=0;i<evidenceList.size();i++){
			attachmentList.addAll(evidenceList.get(i).getTZjclList());
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return evidenceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return evidenceList.get(position);
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
			convertView = inflater.inflate(R.layout.nrc_review_evidence_item, null);
			viewHolder.evidenceName = (TextView) convertView.findViewById(R.id.evidence_name);
			viewHolder.evidenceOwner = (TextView) convertView.findViewById(R.id.evidence_owner);
			viewHolder.evidenceProblem = (TextView) convertView.findViewById(R.id.evidence_problem);
			viewHolder.evidenceFrom = (TextView) convertView.findViewById(R.id.evidence_from);
			viewHolder.horizontalListView = (NoScrollListView) convertView.findViewById(R.id.evidence_image);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		TLayyZjInfo evidenceInfo = evidenceList.get(position);
		TZj evidence = evidenceInfo.getTZj();
		evidenceFileAdapter = new EvidenceFileAdapter(attachmentList,evidence);
		viewHolder.horizontalListView.setAdapter(evidenceFileAdapter);
		viewHolder.evidenceName.setText("证据名称："+evidence.getCName());
		viewHolder.evidenceOwner.setText("归属人："+evidence.getCSsryMc());
		viewHolder.evidenceProblem.setText("证明问题："+evidence.getCZmwt());
		viewHolder.evidenceFrom.setText("证据来源："+evidence.getCZjly());
//		int nType = litigant.getNType();
		return convertView;
	}
	
	public class EvidenceFileAdapter extends BaseAdapter{

		public List<TZjcl> attachmentList = new ArrayList<TZjcl>();
		public TZj evidence;
		
		public EvidenceFileAdapter(List<TZjcl> attachmentList,TZj evidence) {
			// TODO Auto-generated constructor stub
//			this.attachmentList = attachmentList;
			this.evidence = evidence;
			loadData(attachmentList);
		}
		
		public void loadData(List<TZjcl> attachmentList){
			for(int i=0;i<attachmentList.size();i++){
				if(evidence.getCId().equals(attachmentList.get(i).getCZjBh())){
					this.attachmentList.add(attachmentList.get(i));					
				}
			}
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
						downLoadAttachment(position,evidence.getCYwBh());
					}
				}
			});
			return convertView;
		}
		
		public class ViewHolder{
			public TextView attachmentNameTV;
		}
		
	}
	
	public void downLoadAttachment(int position,String layyId){	
		TZjcl attachment = attachmentList.get(position);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("layyId", layyId));
		params.add(new BasicNameValuePair("ssclFjId", attachmentList.get(position).getCId()));
		params.add(new BasicNameValuePair("type", "3"));
//		String url = "http://172.16.192.236:9090/susong51/api/wsla/downloadSscl";
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
		
		public TextView evidenceName;
		public TextView evidenceOwner;
		public TextView evidenceProblem;
		public TextView evidenceFrom;
		public NoScrollListView horizontalListView;
	}

}
