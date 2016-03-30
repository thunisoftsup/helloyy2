package com.thunisoft.sswy.mobile.activity.auth;

import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.logic.response.GywResponse;

/**
 * 实名认证
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_smrz)
public class SmrzActivity extends BaseActivity implements OnCheckedChangeListener {

    @ViewById(R.id.tab_cxm)
    RadioButton tab_cxm;

    @ViewById(R.id.tab_dxyzm)
    RadioButton tab_dxyzm;

    @ViewById(R.id.layout_cxm_rz)
    View layout_cxm_rz;

    @ViewById(R.id.layout_yzm_rz)
    View layout_yzm_rz;

    @Bean
    AuthLogic authLogic;

    @ViewById(R.id.tv_tips)
    TextView tv_tips;

    @Bean
    LoginCache loginCache;
    @ViewById(R.id.btn_send_yzm)
    Button fsyzm;

    @AfterViews
    public void initViews() {
        setBtnBack();
        setTitle(R.string.btn_text_smrz);
        tab_cxm.setOnCheckedChangeListener(this);
        tab_dxyzm.setOnCheckedChangeListener(this);
        findViewById(R.id.btn_cxm_rz).setOnClickListener(this);
        findViewById(R.id.btn_yzm_rz).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        fsyzm.setOnClickListener(this);

        loadUserInfo();
    }

    @Background
    public void loadUserInfo() {
        GywResponse br = authLogic.loadBasicUserInfo();
        if (br.isSuccess()) {
            loginCache.setXm(br.getXm());
            loginCache.setZjhm(br.getZjhm());
            loginCache.setPhone(br.getSjhm());
        }
        loadUserInfoDone();
    }
    
    @UiThread
    public void loadUserInfoDone() {
        setTextString(R.id.tv_cxm_xm, loginCache.getXm());
        setTextString(R.id.tv_yzm_xm, loginCache.getXm());
        setTextString(R.id.tv_cxm_zjhm, loginCache.getZjhm());
        setTextString(R.id.tv_yzm_zjhm, loginCache.getZjhm());
        setTextString(R.id.tv_sjhm, loginCache.getPhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_cxm_rz:
            cxmrz("1");
            break;
        case R.id.btn_yzm_rz:
            yzmrz("1");
            break;
        case R.id.btn_send_yzm:
            fsyzm();
            break;
        }
        super.onClick(v);
    }

    public void fsyzm() {
        fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_sel);
        fsyzm.setClickable(false);
        String phone = getTextString(R.id.tv_sjhm);
        doFsYzm(phone);
    }

    @Background
    public void doFsYzm(String phone) {
        BaseResponse br = authLogic.srmzfsyzm(phone);
        if (br.isSuccess()) {
            showToast("验证码已发送");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        int flag = 60;

                        @Override
                        public void run() {
                            flag--;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    fsyzm.setText(flag + "s后重发送");
                                    fsyzm.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                            getResources().getDimension(R.dimen.default_input_text_size_));
                                }
                            });
                            if (flag == 0) {
                                timer.cancel();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_default);
                                        fsyzm.setClickable(true);
                                        fsyzm.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                getResources().getDimension(R.dimen.default_input_text_size));
                                        fsyzm.setText("获取验证码");
                                    }
                                });
                            }
                        }
                    }, 0, 1000);
                }
            });
        } else {
            if (br.isXtcw()) {
                hideTips();
                showToast(br.getMessage());
            } else {
                shoTips(br.getMessage());
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_default);
                    fsyzm.setClickable(true);
                }
            });
        }
    }

    public void cxmrz(String verifyConflict) {
        String name = getTextString(R.id.tv_cxm_xm);
        String zjhm = getTextString(R.id.tv_cxm_zjhm);
        String cxm = getTextString(R.id.tv_cxm);
        doCxmrz(name, zjhm, cxm, verifyConflict);
    }

    @Background
    public void doCxmrz(String name, String zjhm, String cxm, String verifyConflict) {
        BaseResponse br = authLogic.cxmsmrz(name, zjhm, cxm, verifyConflict);
        if (br.isSuccess()) {
            showToast("实名认证成功");
            loginCache.setLoginType(LoginCache.LOGIN_TYPE_VERIFID);
            loginCache.setXm(name);
            loginCache.setZjhm(zjhm);
            setResult(RESULT_OK, getIntent());
            finish();
        } else {
            Integer errorType = br.getErrorShowType();
            if (errorType != null && errorType.intValue() == Constants.ERROR_SHOW_CONFIRM) {
                hideTips();
                Intent intent = new Intent(this, ConfirmDialogActivity_.class);
                intent.putExtra("message", br.getMessage());
                startActivityForResult(intent, Constants.REQUEST_CODE_CONFIRM_SMRZ_CXM_FGXX);
            } else if (br.isXtcw()) {
                hideTips();
                showToast(br.getMessage());
            } else {
                shoTips(br.getMessage());
            }
        }
    }

    public void yzmrz(String verifyConflict) {
        String name = getTextString(R.id.tv_yzm_xm);
        String zjhm = getTextString(R.id.tv_yzm_zjhm);
        String yzm = getTextString(R.id.tv_yzm);
        String phone = getTextString(R.id.tv_sjhm);
        doYzmrz(name, zjhm, phone, yzm, verifyConflict);
    }

    @Background
    public void doYzmrz(String name, String zjhm, String phone, String yzm, String verifyConflict) {
        BaseResponse br = authLogic.yzmsmrz(name, zjhm, phone, yzm, verifyConflict);
        if (br.isSuccess()) {
            loginCache.setLoginType(LoginCache.LOGIN_TYPE_VERIFID);
            loginCache.setXm(name);
            loginCache.setZjhm(zjhm);
            showToast("实名认证成功");
            setResult(RESULT_OK, getIntent());
            finish();
        } else {
            Integer errorType = br.getErrorShowType();
            if (errorType != null && errorType.intValue() == Constants.ERROR_SHOW_CONFIRM) {
                hideTips();
                Intent intent = new Intent(this, ConfirmDialogActivity_.class);
                intent.putExtra("message", br.getMessage());
                startActivityForResult(intent, Constants.REQUEST_CODE_CONFIRM_SMRZ_YZM_FGXX);
            } else if (br.isXtcw()) {
                hideTips();
                showToast(br.getMessage());
            } else {
                shoTips(br.getMessage());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_CONFIRM_SMRZ_CXM_FGXX && resultCode == Activity.RESULT_OK) {
            cxmrz("0");// 不验证冲突
        } else if (requestCode == Constants.REQUEST_CODE_CONFIRM_SMRZ_YZM_FGXX && resultCode == Activity.RESULT_OK) {
            yzmrz("0");// 不验证冲突
        }
    }

    public void switchRadio(View v) {
        switch (v.getId()) {
        case R.id.tab_cxm:
            layout_cxm_rz.setVisibility(View.VISIBLE);
            layout_yzm_rz.setVisibility(View.GONE);
            break;
        case R.id.tab_dxyzm:
            layout_cxm_rz.setVisibility(View.GONE);
            layout_yzm_rz.setVisibility(View.VISIBLE);
            break;
        }
        tv_tips.setText("");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switchRadio(buttonView);
        }
    }

    @UiThread
    public void hideTips() {
        tv_tips.setText("");
    }

    @UiThread
    public void shoTips(String message) {
        tv_tips.setText(message);
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
