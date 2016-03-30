package com.thunisoft.sswy.mobile.logic.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.datasource.NrcSsclFjDao;
import com.thunisoft.sswy.mobile.datasource.NrcZjclDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 网上立案 诉讼材料附件 文件
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class DownloadSsclResUtil extends JSONParsor<BaseResponse> {

	private static final String TAG = DownloadSsclResUtil.class.getSimpleName();

	public static final int TYPE_SSCL = 1;
	
	public static final int TYPE_ZJ = 2;
	
	@Bean
	NetUtils netUtils;

	public Activity activity;

	private InputStream inputStream;

	private String path;

	private String backupPath;
	
	WaittingDialog waitDialog;
	
	private int type;
	
	private TLayySsclFj ssclFj;
	
	@Bean
	NrcSsclFjDao ssclFjDao;
	
	private TZjcl zjcl;

	@Bean
	NrcZjclDao zjclDao;

	@Override
	public BaseResponse parseToBean(String result) {
		Gson gson = new Gson();
		return gson.fromJson(result, new TypeToken<BaseResponse>() {
		}.getType());
	}

	@Override
	public BaseResponse getResponse(String url, List<NameValuePair> params) {
		BaseResponse cr = new BaseResponse();
		waitDialog = new WaittingDialog(activity, R.style.CustomDialogStyle, "下载中...");
		waitDialog.setIsCanclable(false);
		waitDialog.show();
		DownloadThread downloadThread = new DownloadThread();
		downloadThread.url = url;
		downloadThread.params = params;
		downloadThread.start();
		return cr;
	}

	public void openFile(String path) {
		Intent intent = ResponseUtilExtr.openFileWithAllPath(path);
		try {
			activity.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(activity, "打开文件失败", Toast.LENGTH_SHORT).show();
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (null != inputStream) {
					if (type == TYPE_ZJ) {
						path = FileUtils.getImageFilePath(activity, zjcl.getCOriginName());
					} else {
						path = FileUtils.getImageFilePath(activity, ssclFj.getCOriginName());
					}
					backupPath = FileUtils.getBackupFilePath(activity, path);
					SaveFileThread sf = new SaveFileThread();
					sf.start();
				} else {
					waitDialog.dismiss();
					waitDialog = null;
					Toast.makeText(activity, "下载失败，请检查网络连接或重试", Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				if (StringUtils.isNotBlank(path)) {
					if (TYPE_ZJ == type) {
						zjcl.setCPath(path);
						List<TZjcl> zjclList = new ArrayList<TZjcl>();
						zjclList.add(zjcl);
						zjclDao.updateOrSaveZjcl(zjclList);
					} else {
						ssclFj.setCPath(path);
						List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
						ssclFjList.add(ssclFj);
						ssclFjDao.updateOrSaveSsclFj(ssclFjList);
					}
					openFile(backupPath);
				} else {
					Toast.makeText(activity, "保存文件出错，请重试", Toast.LENGTH_SHORT).show();
				}
				waitDialog.dismiss();
				waitDialog = null;
			}
		}
	};

	class DownloadThread extends Thread {

		private String url;

		private List<NameValuePair> params;

		@Override
		public void run() {
			try {
				inputStream = netUtils.postStream(url, params);
			} catch (ClientProtocolException e) {
				Log.e(TAG, e.getMessage());
			} catch (SSWYNetworkException e) {
				Log.e(TAG, e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			} finally {
				Message m = new Message();
				m.what = 1;
				mHandler.sendMessage(m);
			}
		}
	};

	class SaveFileThread extends Thread {

		@Override
		public void run() {
			OutputStream os = null;
			OutputStream os2 = null;
			try {
				File file = new File(path);
				if (!file.getParentFile().exists()) {
					file.mkdirs();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				os = new FileOutputStream(file);

				File backupFile = new File(backupPath);
				if (!backupFile.getParentFile().exists()) {
					backupFile.mkdirs();
				}
				if (!backupFile.exists()) {
					backupFile.createNewFile();
				}
				os2 = new FileOutputStream(backupFile);

				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
					os.write(buffer, 0, bytesRead);
					os2.write(buffer, 0, bytesRead);
				}

			} catch (FileNotFoundException e) {
				path = "";
				backupPath = "";
				Log.e(TAG, e.getMessage());
			} catch (IOException e) {
				path = "";
				backupPath = "";
				Log.e(TAG, e.getMessage());
			} finally {
				try {
					if (null != os) {
						os.close();
					}
					if (null != os2) {
						os2.close();
					}
				} catch (IOException e) {
					path = "";
					backupPath = "";
					Log.e(TAG, e.getMessage());
				}
				Message m = new Message();
				m.what = 2;
				mHandler.sendMessage(m);
			}
		}
	}

	/**  
	 * 设置  type  
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * 设置 ssclFj
	 * 
	 * @param ssclFj
	 */
	public void setSsclFj(TLayySsclFj ssclFj) {
		this.ssclFj = ssclFj;
	}

	/**  
	 * 设置  zjcl  
	 * @param zjcl
	 */
	public void setZjcl(TZjcl zjcl) {
		this.zjcl = zjcl;
	}
}
