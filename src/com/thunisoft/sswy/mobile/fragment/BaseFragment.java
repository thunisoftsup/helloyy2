package com.thunisoft.sswy.mobile.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.ajxx.AjcxActivity_;
import com.thunisoft.sswy.mobile.activity.auth.ChangePasswordActivity_;
import com.thunisoft.sswy.mobile.activity.auth.GywActivity_;
import com.thunisoft.sswy.mobile.activity.auth.LoginActivity_;
import com.thunisoft.sswy.mobile.activity.auth.LsrzActivity_;
import com.thunisoft.sswy.mobile.activity.auth.SmrzActivity_;
import com.thunisoft.sswy.mobile.activity.auth.ZcActivity_;
import com.thunisoft.sswy.mobile.activity.auth.ZhmmActivity_;
import com.thunisoft.sswy.mobile.activity.court.CourtListActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dzsd.QmmQswsWdlActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.cache.LoginCache_;
import com.thunisoft.sswy.mobile.interfaces.IFragmentChangeNotifier;
import com.thunisoft.sswy.mobile.util.UpdateUtils_;

/**
 * Fragment基类
 * 
 * @author lulg
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
    protected IFragmentChangeNotifier fragmentChangeNotifier;
    LoginCache loginCache;

    public void setFragmentChangeNotifier(IFragmentChangeNotifier fragmentChangeNotifier) {
        this.fragmentChangeNotifier = fragmentChangeNotifier;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginCache = LoginCache_.getInstance_(getActivity());
    }

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_dl:
            login();
            break;
        case R.id.btn_zc:
            zc();
            break;
        case R.id.list_item_lsrz:
            lsrz();
            break;
        case R.id.btn_lsrz:
            lsrz();
            break;
        case R.id.btn_smrz:
            smrz();
            break;
        case R.id.item_list_xgmm:
            xgmm();
            break;
        case R.id.item_list_zxdl:
            logout();
            break;
        case R.id.img_arrow:
            gyw();
            break;
        case R.id.layout_ajcx:
            ajcx();
            break;
        case R.id.layout_wsqs:
            qmmqsws();
            break;
        case R.id.layout_pzfy:
            pzfy();
            break;
        case R.id.layout_jcgx:
            jcgx();
            break;
        case R.id.btn_zhmm:
            zhmm();
            break;
        }
    }

  //获取版本号  
    public String getVersion(Context context) { 
        try {  
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
            return "当前版本"+pi.versionName;  
        } catch (NameNotFoundException e) {  
            return null;  
        }  
    }  
    
    /**
     * 找回密码
     */
    public void zhmm() {
        Intent intent = new Intent(getActivity(), ZhmmActivity_.class);
        startActivity(intent);
    }

    /**
     * 检查更新
     */
    public void jcgx() {
        UpdateUtils_ updateUtil = UpdateUtils_.getInstance_(getActivity());
        updateUtil.checkUpdate(getActivity());
    }

    /**
     * 配置法院
     */
    public void pzfy() {
        Intent intent = new Intent(getActivity(), CourtListActivity_.class);
        intent.putExtra("showBack", true);
        getActivity().startActivity(intent);
    }

    /**
     * 案件查询
     */
    public void ajcx() {
        Intent intent = new Intent(getActivity(), AjcxActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 签名吗签收文书
     */
    public void qmmqsws() {
        Intent intent = new Intent(getActivity(), QmmQswsWdlActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 进入注册页面
     */
    public void zc() {
        Intent intent = new Intent(getActivity(), ZcActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 进入关于我界面
     */
    public void gyw() {
        Intent intent = new Intent(getActivity(), GywActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 进入修改密码页面
     */
    public void xgmm() {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 点击登录按钮，跳转到登录页
     */
    public void login() {
        Intent intent = new Intent(getActivity(), LoginActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 点击注销按钮，弹出确认框
     */
    public void logout() {
        Intent intent = new Intent(getActivity(), ConfirmDialogActivity_.class);
        intent.putExtra("message", getResources().getString(R.string.text_qrzx));
        getActivity().startActivityForResult(intent, Constants.REQUEST_CODE_CONFIRM_ZX);
    }

    /**
     * 进入实名认证页面
     */
    public void smrz() {
        Intent intent = new Intent(getActivity(), SmrzActivity_.class);
        getActivity().startActivity(intent);
    }

    /**
     * 进入律师认证页面(原来算是在Fragment里替换，现在用单独Activity)
     */
    public void lsrz() {
        Intent intent = new Intent(getActivity(), LsrzActivity_.class);
        getActivity().startActivityForResult(intent, Constants.REQUEST_CODE_LSRZ);
        /*
         * if (fragmentChangeNotifier != null) {
         * fragmentChangeNotifier.notifyToLsrz(); }
         */
    }

}
