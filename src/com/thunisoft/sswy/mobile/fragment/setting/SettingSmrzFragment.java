package com.thunisoft.sswy.mobile.fragment.setting;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.view.View;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.cache.CourtCache_;
import com.thunisoft.sswy.mobile.fragment.BaseFragment;
import com.thunisoft.sswy.mobile.util.AppSecretUtil;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;

@EFragment(R.layout.fragment_setting_smrz)
public class SettingSmrzFragment extends BaseFragment {
    @ViewById(R.id.tv_court_name)
    TextView tv_court_name;

    @Pref
    ConfigUtils_ configUtils;
    

    public void initCourtName() {
        String courtName = configUtils.getSharedPreferences().getString("current_court_name", "");
        tv_court_name.setText(courtName);
    }
    
    @AfterViews
    public void initViews() {
        initCourtName();
        if(AppSecretUtil.isLawyerCheck()){
        	getView().findViewById(R.id.list_item_lsrz).setOnClickListener(this);        	
        }else{
        	getView().findViewById(R.id.list_item_lsrz).setVisibility(View.GONE);
        }
        getView().findViewById(R.id.item_list_xgmm).setOnClickListener(this);
        getView().findViewById(R.id.item_list_zxdl).setOnClickListener(this);
        getView().findViewById(R.id.layout_jcgx).setOnClickListener(this);
        getView().findViewById(R.id.layout_pzfy).setOnClickListener(this);
        
        TextView version = (TextView) getView().findViewById(R.id.version);
        version.setText(getVersion(getActivity()));
    }
    
    @Override
    public void onResume() {
        super.onResume();
        tv_court_name.setText(CourtCache_.getInstance_(getActivity()).getCourtName());
    }
}
