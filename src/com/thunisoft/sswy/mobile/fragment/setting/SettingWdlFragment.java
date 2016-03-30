package com.thunisoft.sswy.mobile.fragment.setting;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.cache.CourtCache_;
import com.thunisoft.sswy.mobile.fragment.BaseFragment;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;

@EFragment(R.layout.fragment_setting_wdl)
public class SettingWdlFragment extends BaseFragment {

    @ViewById(R.id.tv_court_name)
    TextView tv_court_name;

    @Pref
    ConfigUtils_ configUtils;

    @AfterViews
    public void initViews() {
        getView().findViewById(R.id.layout_pzfy).setOnClickListener(this);
        getView().findViewById(R.id.layout_jcgx).setOnClickListener(this);
        TextView version = (TextView) getView().findViewById(R.id.version);
        version.setText(getVersion(getActivity()));
        initCourtName();
    }

    public void initCourtName() {
        String courtName = configUtils.getSharedPreferences().getString("current_court_name", "");
        tv_court_name.setText(courtName);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        tv_court_name.setText(CourtCache_.getInstance_(getActivity()).getCourtName());
    }
}
