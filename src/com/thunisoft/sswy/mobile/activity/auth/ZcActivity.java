package com.thunisoft.sswy.mobile.activity.auth;

import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ZjlxDioagActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ZjlxDioagActivity_;
import com.thunisoft.sswy.mobile.adapter.CertTypeAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 注册Activity
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_zc)
public class ZcActivity extends BaseActivity {
    @ViewById(R.id.tv_tips)
    TextView tv_tips;

    @Bean
    AuthLogic authLogic;

    @ViewById(R.id.tv_xzzj)
    EditText tv_xzzj;

    @ViewById(R.id.tv_zjhm)
    EditText tv_zjhm;
    @ViewById(R.id.btn_send_yzm)
    Button fsyzm;

    @ViewById(R.id.check_ty)
    CheckBox check_ty;

    String zjlxId;

    String tempSid;// 临时sessionId

    @AfterViews
    public void initViews() {
        setTitle(R.string.text_zc);
        setBtnBack();
        findViewById(R.id.btn_xyb).setOnClickListener(this);
        tv_xzzj.setOnClickListener(this);
        fsyzm.setOnClickListener(this);
        findViewById(R.id.tv_sswywlptxy).setOnClickListener(this);
    }

    public void xzzj() {
        Intent intent = new Intent(this, ZjlxDioagActivity_.class);
        intent.putExtra(ZjlxDioagActivity.K_CERT_CODE, zjlxId);
        startActivityForResult(intent, Constants.REQUEST_CODE_XZZJ);
    }
    
    public void gotoZcxy() {
        Intent intent = new Intent(this, YhzcxyActivity_.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_xyb:
            zc();
            break;
        case R.id.tv_xzzj:
            xzzj();
            break;
        case R.id.btn_send_yzm:
            fsyzm();
            break;
        case R.id.tv_sswywlptxy:
            gotoZcxy();
            break;
        }
        super.onClick(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if (Constants.RESULT_OK != resultCode) {
    		return;
    	}
    	
        switch (requestCode) {
        case Constants.REQUEST_CODE_XZZJ:
            zjlxId = data.getStringExtra(CertTypeAdapter.K_SELECTED_CODE);
            String zjlxText = NrcUtils.getCertificateNameByCode(Integer.parseInt(zjlxId));
            tv_xzzj.setText(zjlxText == null ? "" : zjlxText);
            if (StringUtils.isNotBlank(zjlxText)) {
                tv_zjhm.requestFocus();
            }
        }
    }

    public void zc() {
        if (!check_ty.isChecked()) {
            showTips("你必须同意《诉讼无忧网络平台协议》才能进行注册");
            return;
        }
        String userName = getTextString(R.id.tv_yhm);
        String password = getTextString(R.id.tv_mm);
        String repwd = getTextString(R.id.tv_qrmm);
        String realName = getTextString(R.id.tv_zsxm);
        String cardType = zjlxId;
        String cardID = getTextString(R.id.tv_zjhm);
        String phone = getTextString(R.id.tv_sjhm);
        String sjyzm = getTextString(R.id.tv_sjyzm);
        doZc(userName, password, repwd, realName, cardType, cardID, phone, sjyzm, tempSid);
    }

    @Background
    public void doZc(String userName, String password, String repwd, String realName, String cardType, String cardID,
            String phone, String sjyzm, String tempSid) {
        BaseResponse br = authLogic.zc(userName, password, repwd, realName, cardType, cardID, phone, sjyzm, tempSid);
        if (!br.isSuccess()) {
            if (br.isXtcw()) {
                hideTips();
                showToast(br.getMessage());
            } else {
                showTips(br.getMessage());
            }
        } else {
            login();
        }
    }

    @UiThread
    public void login() {
        String userName = getTextString(R.id.tv_yhm);
        String password = getTextString(R.id.tv_mm);
        login(userName, password);
    }

    @Background
    public void login(String userName, String password) {
        BaseResponse tempBr = authLogic.getTempSid();
        BaseResponse br = authLogic.login(tempBr.getTempSid(), null,userName, password,LoginCache.LOGIN_TYPE_VERIFID, null, null,null);
        if (br.isSuccess()) {
        	finish();
//        	屏蔽掉认证界面
//            gouToNextPage();
        } else if (br.isXtcw()) {
            showToast(br.getMessage());
        } else {
            showTips(br.getMessage());
        }
    }
//吉林电子法院屏蔽掉验证页
//    @UiThread
//    public void gouToNextPage() {
//        setResult(RESULT_OK);
//        Intent intent = new Intent(this, ZcqrActivity_.class);
//        startActivity(intent);
//        finish();
//    }

    public void fsyzm() {
        fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_sel);
        fsyzm.setClickable(false);
        String phone = getTextString(R.id.tv_sjhm);
        fsyzm(phone);
    }

    @Background
    public void fsyzm(String phone) {
        BaseResponse br = authLogic.zcfsyzm(phone);
        if (!br.isSuccess()) {
            if (br.isXtcw()) {
                hideTips();
                showToast(br.getMessage());
            } else {
                showTips(br.getMessage());
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_default);
                    fsyzm.setClickable(true);
                }
            });
        } else {
            tempSid = br.getTempSid();
            showToast("验证码已发送");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        int flag = 60;
                        @Override
                        public void run() {
                            flag -- ;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    fsyzm.setText(flag+"s后重发送");
                                    fsyzm.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_input_text_size_));
                                }
                            });
                            if (flag == 0) {
                                timer.cancel();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_default);
                                        fsyzm.setClickable(true);
                                        fsyzm.setText("获取验证码");
                                        fsyzm.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_input_text_size));
                                    }
                                });
                            }
                        }
                    }, 0, 1000);
                }
            });
        }
    }

    @UiThread
    public void hideTips() {
        tv_tips.setText("");
        tv_tips.setVisibility(View.GONE);
    }

    @UiThread
    public void showTips(String message) {
        tv_tips.setText(message);
        tv_tips.setVisibility(View.VISIBLE);
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
