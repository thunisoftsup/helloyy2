package com.thunisoft.sswy.mobile.fragment.setting;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.view.View;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.cache.CourtCache_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.fragment.BaseFragment;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;

@EFragment(R.layout.fragment_setting_wrz_lsrz)
public class SettingWrzLsrzFragment extends BaseFragment {
    @ViewById(R.id.tv_court_name)
    TextView tv_court_name;
    
    @Pref
    ConfigUtils_ configUtils;
    
    @Bean
    LoginCache loginCache;

    @AfterViews
    public void initViews() {
        initCourtName();
        getView().findViewById(R.id.layout_pzfy).setOnClickListener(this);
        getView().findViewById(R.id.layout_jcgx).setOnClickListener(this);
        getView().findViewById(R.id.item_list_xgmm).setOnClickListener(this);
        getView().findViewById(R.id.item_list_zxdl).setOnClickListener(this);
        TextView version = (TextView) getView().findViewById(R.id.version);
        version.setText(getVersion(getActivity()));
    }

    public void initCourtName() {
        String courtName = configUtils.getSharedPreferences().getString("current_court_name", "");
        tv_court_name.setText(courtName);
        if(loginCache.getLoginType() == loginCache.LOGIN_TYPE_LS_VERIFID){
        	getView().findViewById(R.id.item_list_xgmm).setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        tv_court_name.setText(CourtCache_.getInstance_(getActivity()).getCourtName());
    }
}
