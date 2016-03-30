package com.thunisoft.sswy.mobile.activity.dialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.adapter.CertTypeAdapter;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 认证方式
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.dialog_list_select)
public class RzfsDioagActivity extends Activity {

	@ViewById(R.id.dialog_list_tips)
	TextView tipTV;
	
	@ViewById(R.id.dialog_list_view)
	ListView listView;

	CertTypeAdapter adapter;

	public static final String K_RZFS_CODE = "certCode";

	public static final String K_MSSGAGE = "message";
	
	private String message;
	
	private String rzfsCode;

	@AfterViews
	public void initViews() {
		setFinishOnTouchOutside(true);// 设置为true点击区域外消失
		Intent intent = getIntent();
		rzfsCode = intent.getStringExtra(K_RZFS_CODE);
		message = intent.getStringExtra(K_MSSGAGE);
		if (StringUtils.isNotBlank(message)) {
			tipTV.setVisibility(View.VISIBLE);
			tipTV.setText(message);
		} else {
			tipTV.setVisibility(View.GONE);
		}
		refreshRzfsList();
	}

	/**
	 * 刷新 认证方式 列表
	 */
	private void refreshRzfsList() {
		if (null == adapter) {
			adapter = new CertTypeAdapter(this, NrcUtils.getRzfsList(), rzfsCode);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}
}
