package com.thunisoft.sswy.mobile.logic.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.NameValuePair;
import org.textmining.text.extraction.WordExtractor;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.thunisoft.sswy.mobile.interfaces.IAttachmentOpenListener;
import com.thunisoft.sswy.mobile.interfaces.IWaitingDialogNotifier;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;

public class DownLoadAttachmentUtil extends AsyncTask<String, Integer, Void>{
	
	private List<NameValuePair> nameValuePairs;
	private String path="";
	private String fileName ="";
	private String attachmenCId = "";
	private NRCReviewResponseUtil responseUtil;
	private IWaitingDialogNotifier waitingDialogNotifier;
	private IAttachmentOpenListener attachmentOpenlistener;

	public DownLoadAttachmentUtil(List<NameValuePair> params,String fileName,String CId,NRCReviewResponseUtil responseUtil) {
		// TODO Auto-generated constructor stub
		this.nameValuePairs = params;
		File file = Environment.getExternalStorageDirectory();
//		file.getAbsolutePath()+;"/susong51/ssclfj/"+CId+"/"+
		path = file.getAbsolutePath()+"/susong51/ssclfj/"+CId;
		this.fileName = fileName;
		this.responseUtil = responseUtil;
		this.attachmenCId = CId;
	}

	@Override
	protected Void doInBackground(String... params) {
		String url = params[0];
		InputStream is = responseUtil.getResponseStream(url, nameValuePairs);
		if(is == null){
			System.out.println("文件读取失败");
			if(waitingDialogNotifier != null){
				waitingDialogNotifier.dismissDialog();			
			}
		}
		else{
			if(fileName.endsWith(".doc")){
				saveFileDoc(is);
//				saveFileTxt(is);
			}else{
				saveFile(is);					
			}
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	} 
	
	/**
	 * 保存作为doc文件
	 * **/
	private void saveFileDoc(InputStream is) {
		File dir = new File(path);
        if(!dir.exists()){
        	dir.mkdirs();
        }
        File saveFile = new File(path+"/"+fileName);
        if(saveFile.exists()){
        	saveFile.delete();
        }
        try {
        	saveFile.createNewFile();
        } catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
            int b = -1;
            while ((b = is.read()) != -1) {
                bos.write(b);
            }
            bos.close();
			System.out.println("文件保存成功");
			saveToDB();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(waitingDialogNotifier != null){
				waitingDialogNotifier.dismissDialog();			
			}
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
        
	}
	
	private void saveFile(InputStream is) {
		File dir = new File(path);
        if(!dir.exists()){
        	dir.mkdirs();
        }
        File saveFile = new File(path+"/"+fileName);
        if(saveFile.exists()){
        	saveFile.delete();
        }
        try {
        	saveFile.createNewFile();
        } catch (IOException e) {
        	// TODO Auto-generated catch block
    		if(waitingDialogNotifier != null){
    			waitingDialogNotifier.dismissDialog();			
    		}
        	e.printStackTrace();
        }
        
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
            int b = -1;
            while ((b = is.read()) != -1) {
                bos.write(b);
            }
            bos.close();
			System.out.println("文件保存成功");
			saveToDB();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
	}
	
	public void saveToDB(){
		TLayySsclFj attachment = new TLayySsclFj();
		attachment.setCId(attachmenCId);
		attachment.setCOriginName(fileName);
		attachment.setCPath(path+"/"+fileName);
		responseUtil.saveAttachment(attachment);
		if(waitingDialogNotifier != null){
			waitingDialogNotifier.dismissDialog();			
		}
		if(attachmentOpenlistener != null){
			attachmentOpenlistener.open(attachment.getCPath());			
		}
	}
	
	
	public IWaitingDialogNotifier getWaitingDialogNotifier() {
		return waitingDialogNotifier;
	}

	public void setWaitingDialogNotifier(
			IWaitingDialogNotifier waitingDialogNotifier) {
		this.waitingDialogNotifier = waitingDialogNotifier;
	}
	
	public IAttachmentOpenListener getAttachmentOpenlistener() {
		return attachmentOpenlistener;
	}

	public void setAttachmentOpenlistener(
			IAttachmentOpenListener attachmentOpenlistener) {
		this.attachmentOpenlistener = attachmentOpenlistener;
	}

}
