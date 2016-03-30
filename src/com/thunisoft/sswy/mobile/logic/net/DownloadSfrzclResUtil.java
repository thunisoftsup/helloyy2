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
import com.thunisoft.sswy.mobile.activity.nrc.PicPreviewActivity;
import com.thunisoft.sswy.mobile.activity.nrc.PicPreviewActivity_;
import com.thunisoft.sswy.mobile.datasource.NrcSfrzclDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.JSONParsor;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.model.PicModel;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;

/**
 * 网上立案 身份认证材料 文件
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class DownloadSfrzclResUtil extends JSONParsor<BaseResponse> {

	private static final String TAG = DownloadSfrzclResUtil.class.getSimpleName();

	@Bean
	NetUtils netUtils;

	public Activity activity;

	private InputStream inputStream;

	private String path;

	WaittingDialog waitDialog;

	/**
	 * 申请人身份认证材料
	 */
	private TProUserSfrzCl sfrzCl;

	@Bean
	NrcSfrzclDao sfrzclDao;

	/**
	 * 打开文件方式
	 */
	private int openType = NrcConstants.OPEN_TYPE_EDIT;

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

	public void openSfrzCl(String path) {
		Intent intent = new Intent();
		ArrayList<PicModel> picModelList = new ArrayList<PicModel>();
		PicModel picModel = new PicModel();
		picModel.setRelId(sfrzCl.getCBh());
		picModel.setRelPid(sfrzCl.getCBh());
		picModel.setName(sfrzCl.getCClName());
		picModel.setPath(sfrzCl.getCClPath());
		picModel.setType(PicModel.TYPE_SFRZ_CL);
		picModelList.add(picModel);
		intent.setClass(activity, PicPreviewActivity_.class);
		intent.putExtra(PicPreviewActivity.K_CURR_POSITION, 0);
		intent.putExtra(PicPreviewActivity.IS_SHOW_PERCENT, false);
		intent.putExtra(PicPreviewActivity.K_PIC_IST, picModelList);
		intent.putExtra(PicPreviewActivity.K_OPEN_TYPE, openType);
		activity.startActivity(intent);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (null != inputStream) {
					path = FileUtils.getImageFilePath(activity, sfrzCl.getCClName());
					SaveFileThread sf = new SaveFileThread();
					sf.start();
				} else {
					waitDialog.dismiss();
					Toast.makeText(activity, "下载失败，请检查网络连接或重试", Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				if (StringUtils.isNotBlank(path)) {
					List<TProUserSfrzCl> sfrzclList = new ArrayList<TProUserSfrzCl>();
					sfrzCl.setCClPath(path);
					sfrzclList.add(sfrzCl);
					sfrzclDao.updateOrSaveSfrzcl(sfrzclList);
					openSfrzCl(path);
				} else {
					Toast.makeText(activity, "保存文件出错，请重试", Toast.LENGTH_SHORT).show();
				}
				waitDialog.dismiss();
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
			try {
				File file = new File(path);
				if (!file.getParentFile().exists()) {
					file.mkdirs();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				os = new FileOutputStream(file);

				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
					os.write(buffer, 0, bytesRead);
				}

			} catch (FileNotFoundException e) {
				path = "";
				Log.e(TAG, e.getMessage());
			} catch (IOException e) {
				path = "";
				Log.e(TAG, e.getMessage());
			} finally {
				try {
					if (null != os) {
						os.close();
					}
				} catch (IOException e) {
					path = "";
					Log.e(TAG, e.getMessage());
				}
				Message m = new Message();
				m.what = 2;
				mHandler.sendMessage(m);
			}
		}
	}

	/**
	 * 设置 sfrzCl
	 * 
	 * @param sfrzCl
	 */
	public void setSfrzCl(TProUserSfrzCl sfrzCl) {
		this.sfrzCl = sfrzCl;
	}

	/**
	 * 设置 打开文件方式
	 * 
	 * @param openType
	 */
	public void setOpenType(int openType) {
		this.openType = openType;
	}
}
