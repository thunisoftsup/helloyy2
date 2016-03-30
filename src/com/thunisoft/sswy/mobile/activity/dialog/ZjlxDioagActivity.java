package com.thunisoft.sswy.mobile.activity.dialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.adapter.CertTypeAdapter;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 证件类型选择框
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.dialog_list_select)
public class ZjlxDioagActivity extends Activity {

	@ViewById(R.id.dialog_list_view)
	ListView listView;

	CertTypeAdapter adapter;

	public static final String K_CERT_CODE = "certCode";

	private String certCode;
	
	@AfterViews
	public void onAfterViews() {
		setFinishOnTouchOutside(true);// 设置为true点击区域外消失
		Intent intent = getIntent();
		certCode = intent.getStringExtra(K_CERT_CODE);
		refreshCertType();
	}

	/**
	 * 刷新 证件类型 列表
	 */
	private void refreshCertType() {
		if (null == adapter) {
			adapter = new CertTypeAdapter(this, NrcUtils.getCertificateTypeList(), certCode);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}
}
