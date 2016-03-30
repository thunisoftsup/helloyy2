package com.thunisoft.sswy.mobile.activity.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.adapter.LsrzjlAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.GywResponse;
import com.thunisoft.sswy.mobile.pojo.LsrzJl;
import com.thunisoft.sswy.mobile.util.AppSecretUtil;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.PhoneStateUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 关于我
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_gyw)
public class GywActivity extends BaseActivity {
	@Bean
	LoginCache loginCache;
	@ViewById(R.id.list_rzjl)
	ListView list_rzjl;

	List<LsrzJl> datas = new ArrayList<LsrzJl>();

	ArrayAdapter<LsrzJl> adapter;

	@ViewById(R.id.tv_xm)
	TextView tv_xm;
	@ViewById(R.id.tv_sfz)
	TextView tv_sfz;
	@ViewById(R.id.tv_sjhm)
	TextView tv_sjhm;
	@ViewById(R.id.rl_zyzh)
	RelativeLayout zyzhRL;
	@ViewById(R.id.tv_zyzh)
	TextView tv_zyzh;

	@ViewById(R.id.lawyer_qr_code)
	LinearLayout iv_QRCode;

	// @ViewById(R.id.lawyer_qr_code_info)
	// LinearLayout iv_QRCodeInfo;

	@Bean
	AuthLogic authLogic;

	@AfterViews
	public void initViews() {
		if (AppSecretUtil.isLawyerCheck()) {
			adapter = new LsrzjlAdapter(this, R.layout.list_item_gyw, datas);
			adapter.setNotifyOnChange(false);
			list_rzjl.setAdapter(adapter);
		} else {
			list_rzjl.setVisibility(View.GONE);
		}
		setBtnBack();

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadDatas();
	}

	@Background
	public void loadDatas() {
		GywResponse gr = authLogic.gyw();
		if (!gr.isSuccess()) {
			showToast(gr.getMessage());
		} else {
			showUserInfo(gr);
		}
	}

	@UiThread
	public void showUserInfo(GywResponse gr) {
		if (gr.getRzjlList() != null && !gr.getRzjlList().isEmpty()) {
			datas.clear();
			datas.addAll(gr.getRzjlList());
		}
		if (loginCache.isLsrz()) {
			datas.add(null);// 律师认证的时候准备的。
		}
		if (AppSecretUtil.isLawyerCheck()) {
			adapter.notifyDataSetChanged();
		}
		tv_xm.setText(gr.getXm());
		setTitle(gr.getXm());
		tv_sfz.setText(gr.getZjhm());
		tv_sjhm.setText(gr.getSjhm());
		List<LsrzJl> rzList = gr.getRzjlList();
		zyzhRL.setVisibility(View.GONE);
		if (null != rzList && rzList.size() > 0) {
			LsrzJl rzjl = rzList.get(rzList.size() - 1);
			if (StringUtils.isNotBlank(rzjl.getZyzh())) {
				zyzhRL.setVisibility(View.VISIBLE);
				tv_zyzh.setText(rzjl.getZyzh());
			}
		}
		if (loginCache.isLsrz()) {
			Bitmap bitmap = FileUtils.getLoacalBitmap(
					Environment.getExternalStorageDirectory()
							+ "/susong51/QRCode.jpg", 333, 333);
			// iv_QRCodeInfo.setVisibility(View.VISIBLE);
			int imgWidth = PhoneStateUtils.getScreenWidth(this) / 2;// 设置图片的高度
			LayoutParams layoutParams = new LayoutParams(imgWidth, imgWidth);
			layoutParams.gravity=Gravity.CENTER_HORIZONTAL;
			layoutParams.topMargin=30;
			iv_QRCode.setLayoutParams(layoutParams);
			if (bitmap != null) {
				BitmapDrawable bd= new BitmapDrawable(this.getResources(), bitmap);  
				iv_QRCode.setBackgroundDrawable(bd);
			}
			if(bitmap!=null){
				iv_QRCode.setVisibility(View.VISIBLE);
				
			}else{
				iv_QRCode.setVisibility(View.GONE);
			}
			iv_QRCode.setOnLongClickListener(new OnLongClickListener() {
				@SuppressWarnings("resource")
				@Override
				public boolean onLongClick(View v) {

					Intent intent = new Intent(GywActivity.this,
							ConfirmDialogActivity_.class);
					intent.putExtra("message",
							getResources().getString(R.string.text_download));
					startActivityForResult(intent,
							Constants.REQUEST_CODE_DOWNLOAD);
					return false;
				}
			});
		}
		setTextString(R.id.tv_zjlx, gr.getZjlx());
	}

	@UiThread
	public void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			downloadImage();
		}
	}

	/**
	 * 下载二维码到本地
	 */
	public void downloadImage() {
		String sdcard = Environment.getExternalStorageDirectory().toString();

		// 路径
		String filePath = sdcard + "/susong51/";
		// filename
		String fileName = new Date().getTime() + ".jpg";
		File bitmapFile = new File(Environment.getExternalStorageDirectory()
				+ "/susong51/QRCode.jpg");
		StringBuilder filePathSB = new StringBuilder();
		filePathSB.append(filePath);
		filePathSB.append(fileName);
		boolean flag = FileUtils.copyFileTo(bitmapFile.getPath(),
				filePathSB.toString());
		if (flag) {
			Toast.makeText(this, "已保存到本地", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File file = new File(filePathSB.toString());
			Uri uri = Uri.fromFile(file);
			intent.setData(uri);
			this.sendBroadcast(intent);
		} else {
			Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
		}
	}

}
