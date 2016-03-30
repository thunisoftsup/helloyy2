package com.thunisoft.sswy.mobile.fragment.setting;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FromHtml;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.fragment.BaseFragment;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;

@EFragment(R.layout.fragment_lsrz)
public class LsrzFragment extends BaseFragment {
    private static final String TAG = "LsrzFragment";

    @FromHtml(R.string.text_sm_lsrz)
    @ViewById(R.id.tv_sm_lsrz)
    TextView tv_sm_lsrz;

    @ViewById(R.id.btn_lsrz)
    Button button;

    @ViewById(R.id.action_title)
    TextView action_title;

    @Bean
    AuthLogic authLogic;

    @ViewById(R.id.tv_lsmc)
    TextView tv_lsmc;

    @ViewById(R.id.tv_zjhm)
    TextView tv_zjhm;

    @ViewById(R.id.tv_zyzh)
    EditText tv_zyzh;

    @ViewById(R.id.tv_rzm)
    EditText tv_rzm;

    @Bean
    LoginCache loginCache;

    @ViewById(R.id.btn_back)
    View btn_back;

    @AfterViews
    public void initViews() {
        action_title.setText(getResources().getString(R.string.btn_text_lsrz));
        button.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        tv_lsmc.setText(loginCache.getXm());
        tv_zjhm.setText(loginCache.getZjhm());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_lsrz:
            doLsrz("1");
            break;
        case R.id.btn_back:
            fragmentChangeNotifier.back();
            break;
        }
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
            doLsrz("0");
        }
    }

    @Background
    public void doLsrz(String name, String cardId, String zyCardId, String verifyCode, String verifyConflict) {
        BaseResponse br = authLogic.lsrz(name, cardId, zyCardId, verifyCode, verifyConflict);
        if (!br.isSuccess() && br.getErrorShowType() != null
                && br.getErrorShowType().intValue() == Constants.ERROR_SHOW_CONFIRM) {
            Intent intent = new Intent(getActivity(), ConfirmDialogActivity_.class);
            intent.putExtra("message", br.getMessage());
            startActivityForResult(intent, Constants.REQUEST_CODE_CONFIRM_LSRZ_FGXX);
        } else if (!br.isSuccess()) {
            Intent intent = new Intent(getActivity(), ConfirmDialogActivity_.class);
            intent.putExtra("message", br.getMessage());
            startActivity(intent);
        } else {
            loginCache.setLoginType(LoginCache.LOGIN_TYPE_LS_VERIFID);
            Log.e(TAG, "回到上一页");
            success();
        }
    }

    @UiThread
    public void success() {
        fragmentChangeNotifier.notifySuccessAndBack();
    }

    @Override
    public boolean onBackPressed() {
        fragmentChangeNotifier.back();
        return true;
    }
    
    

}
