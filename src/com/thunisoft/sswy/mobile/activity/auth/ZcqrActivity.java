package com.thunisoft.sswy.mobile.activity.auth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AlertDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;

/**
 * 注册确认界面
 * 
 */


@EActivity(R.layout.activity_zcqr)
public class ZcqrActivity extends BaseActivity {

    @Bean
    AuthLogic authLogic;

    @AfterViews
    public void initViews() {
        findViewById(R.id.btn_zc_smrz).setOnClickListener(this);
        findViewById(R.id.btn_lsrz).setOnClickListener(this);
        findViewById(R.id.btn_qtxxrz).setOnClickListener(this);
        findViewById(R.id.btn_brz).setOnClickListener(this);
        setBtnBack();
        setTitle("认证确认");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_zc_smrz:
            zcxxsmrz();
            break;
        case R.id.btn_lsrz:
            lsrz();
            break;
        case R.id.btn_qtxxrz:
            qtxxSmrz();
            break;
        case R.id.btn_brz:
            finish();
            break;
        }
        super.onClick(v);
    }

    public void zcxxsmrz() {
        doSmrz();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_FINISH_WIDTH＿NEXT && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Background
    public void doSmrz() {
        BaseResponse br = authLogic.zcxxsmrz();
        if (br.isSuccess()) {
            showToast("实名认证成功");
            finish();
        } else {
            if (br.isXtcw()) {
                showToast(br.getMessage());
            } else {
                shoTips(br.getMessage());
            }
        }
    }

    public void lsrz() {
        Intent intent = new Intent(this, LsrzActivity_.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_FINISH_WIDTH＿NEXT);
    }

    public void qtxxSmrz() {
        Intent intent = new Intent(this, SmrzActivity_.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_FINISH_WIDTH＿NEXT);
    }

    @UiThread
    public void shoTips(String message) {
        Intent intent = new Intent(this, AlertDialogActivity_.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
