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
import com.thunisoft.sswy.mobile.activity.nrc.NRCReviewCertificatesActivity;
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

/**
 * 网上立案查看界面的证件列表adapter
 * 
 */

public class NRCReviewCertificatesAdapter extends BaseAdapter{
	
	private static final String TAG = "NRCReviewCertificatesAdapter";
	
	private NRCReviewCertificatesActivity activity;
	
	private IWaitingDialogNotifier waitingDialogNotifier;
	
	private NRCReviewResponseUtil responseUtil;
	
	private List<TLayySsclFj> CertificatesList = new ArrayList<TLayySsclFj>();
	
	private List<TLayySsclInfo> infoList = new ArrayList<TLayySsclInfo>();
	
	private Map<String,String> cNameMap = new HashMap<String, String>();
	
	private Map<String,String> litigantNameMap = new HashMap<String, String>();
	
	public NRCReviewCertificatesAdapter(NRCReviewCertificatesActivity activity,
			List<TLayySsclInfo> infoList,List<TLayySsclFj> CertificatesList,NRCReviewResponseUtil responseUtil) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.infoList = infoList;
		this.CertificatesList = CertificatesList;
		this.responseUtil = responseUtil;
		loadMapData();
	}
	
    public void setWaitingDialogNotifier(IWaitingDialogNotifier waitingDialogNotifier) {
        this.waitingDialogNotifier = waitingDialogNotifier;
    }
	
	public void loadMapData(){
		for(TLayySsclInfo info:infoList){
			TLayySscl sscl = info.getSscl();
			List<TLayyDsrSscl> dsrSsclList = info.getSsdsrClList();
			cNameMap.put(sscl.getCId(), sscl.getCName());
			for(TLayyDsrSscl dsrSscl : dsrSsclList){
				litigantNameMap.put(dsrSscl.getCSsclId(), dsrSscl.getCDsrName());
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CertificatesList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return CertificatesList.get(position);
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
			LayoutInflater inflater = LayoutInflater.from(activity);
			convertView = inflater.inflate(R.layout.nrc_review_attachment_item, null);
			viewHolder.certificatesName = (TextView) convertView.findViewById(R.id.attachment_name);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
//		viewHolder.certificatesName.setText(CertificatesList.get(position).getCOriginName());
		String certificatesName = cNameMap.get(CertificatesList.get(position).getCSsclId());
		String litigantName = litigantNameMap.get(CertificatesList.get(position).getCSsclId());
		viewHolder.certificatesName.setText(certificatesName+"("+litigantName+")");
		viewHolder.certificatesName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TLayySsclFj tempAttachment = responseUtil.getSsclFjFromDb(CertificatesList.get(position).getCId());
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
		TLayySsclFj attachment = CertificatesList.get(position);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("layyId", attachment.getCLayyId()));
		params.add(new BasicNameValuePair("ssclFjId", attachment.getCId()));
		params.add(new BasicNameValuePair("type", "1"));
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
		
		public TextView certificatesName;
		
	}

}
