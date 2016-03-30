package com.thunisoft.sswy.mobile.activity.auth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FromHtml;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.camara.CaptureActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.logic.response.GywResponse;

/**
 * 律师认证界面
 * 
 */

@EActivity(R.layout.activity_lsrz)
public class LsrzActivity extends BaseActivity {

    private static final String TAG = "LsrzActivity";

    @FromHtml(R.string.text_sm_lsrz)
    @ViewById(R.id.tv_sm_lsrz)
    TextView tv_sm_lsrz;

    @Bean
    AuthLogic authLogic;

    @ViewById(R.id.tv_lsmc)
    EditText tv_lsmc;

    @ViewById(R.id.tv_zjhm)
    TextView tv_zjhm;

    @ViewById(R.id.tv_zyzh)
    EditText tv_zyzh;

    @ViewById(R.id.tv_rzm)
    EditText tv_rzm;

    @Bean
    LoginCache loginCache;

    @ViewById
    TextView tv_tips;
    
    @ViewById(R.id.btn_lsrz)
    Button btn_lsrz;
    
    public static LsrzActivity instanse;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanse = this;
    }

    @AfterViews
    public void initViews() {
        setTitle(R.string.btn_text_lsrz);
        btn_lsrz.setOnClickListener(this);
        findViewById(R.id.layout_ewmsm).setOnClickListener(this);
        setBtnBack();
        if (getIntent().getStringExtra("change") != null) {
            loginCache.setSfrzbg(true);
            tv_lsmc.setFocusable(false);
            tv_lsmc.setFocusableInTouchMode(false);
            tv_zjhm.setFocusable(false);
            tv_zjhm.setFocusableInTouchMode(false);
        } else {
            loginCache.setSfrzbg(false);
        }
       /* if (getIntent().getStringExtra("change") != null) {
            tv_lsmc.setFilters(new InputFilter[] {   
                    new InputFilter() {   
                        public CharSequence filter(CharSequence source, int start,   
                                int end, Spanned dest, int dstart, int dend) {   
                            return source.length() < 1 ? dest.subSequence(dstart, dend) : "";   
                        }   
                    }   
            });
            tv_zjhm.setFilters(new InputFilter[] {   
                    new InputFilter() {   
                        public CharSequence filter(CharSequence source, int start,   
                                int end, Spanned dest, int dstart, int dend) {   
                            return source.length() < 1 ? dest.subSequence(dstart, dend) : "";   
                        }   
                    }   
            });
        }*/
        btn_lsrz.setClickable(false);
        loadDatas();
    }
    
    @Background
    public void loadDatas() {
        GywResponse gr = authLogic.loadBasicUserInfo();
        if(gr.isSuccess()) {
            loadDataComplete(gr.getXm(), gr.getZjhm(), gr.getZjlx());
        } else {
            loadDataComplete(loginCache.getXm(), loginCache.getZjhm(), loginCache.getZjLx());
        }
    }
    
    @UiThread
    public void loadDataComplete(String name, String zjhm, String zjlx) {
        tv_lsmc.setText(name);
        if (zjlx.equals("身份证") || "1".equals(zjlx)) {
        	tv_zjhm.setText(zjhm);
        } else {
        	tv_zjhm.setText("");
        }
        btn_lsrz.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_lsrz:
            doLsrz("1");// 验证冲突
            break;
        case R.id.layout_ewmsm:
            smerm();
            break;
        }
        super.onClick(v);
    }
    
    public void smerm() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_SCAN_CODE);
    }

    public void doLsrz(String verifyConfilict) {
        String name = tv_lsmc.getText().toString();
        String cardId = tv_zjhm.getText().toString();
        String zyCardId = tv_zyzh.getText().toString();
        String verifyCode = tv_rzm.getText().toString();
        doLsrz(name, cardId, zyCardId, verifyCode, verifyConfilict);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_CONFIRM_LSRZ_FGXX && resultCode == Activity.RESULT_OK) {
            doLsrz("0");// 不验证冲突
        } else if(requestCode == Constants.REQUEST_CODE_SCAN_CODE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, data.getStringExtra("message"));
            String message = data.getStringExtra("message");
            Intent intent = new Intent(this, LsrzSmjgActivity_.class);
            intent.putExtra("message", message);
            startActivity(intent);
        }
    }

    @Background
    public void doLsrz(String name, String cardId, String zyCardId, String verifyCode, String verifyConflict) {
        BaseResponse br = authLogic.lsrz(name, cardId, zyCardId, verifyCode, verifyConflict);
        if (br.isSuccess()) {
            loginCache.setLoginType(LoginCache.LOGIN_TYPE_LS_VERIFID);
            showToast("律师认证成功");
            setResult(RESULT_OK, getIntent());
            finish();
        } else {
            Integer errorType = br.getErrorShowType();
            if (errorType != null && errorType.intValue() == Constants.ERROR_SHOW_CONFIRM) {
                hideTips();
                Intent intent = new Intent(this, ConfirmDialogActivity_.class);
                intent.putExtra("message", br.getMessage());
                startActivityForResult(intent, Constants.REQUEST_CODE_CONFIRM_LSRZ_FGXX);
            } else if (br.isXtcw()) {
                hideTips();
                showToast(br.getMessage());
            } else {
                shoTips(br.getMessage());
            }
        }
    }

    @UiThread
    public void hideTips() {
        tv_tips.setVisibility(View.GONE);
    }

    @UiThread
    public void shoTips(String message) {
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setText(message);
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
