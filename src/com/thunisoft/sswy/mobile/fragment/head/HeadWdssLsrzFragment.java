package com.thunisoft.sswy.mobile.fragment.head;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.content.Intent;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.AddCaseActivity_;
import com.thunisoft.sswy.mobile.activity.AjxxActy_;
import com.thunisoft.sswy.mobile.activity.HomeActivity.AjCountData;
import com.thunisoft.sswy.mobile.cache.LoginCache;

@EFragment(R.layout.head_wdss_lsrz)
public class HeadWdssLsrzFragment extends Fragment implements AjCountData {
    @ViewById(R.id.tv_user_name)
    TextView tv_user_name;

    @ViewById(R.id.tv_aj_number)
    TextView tv_aj_number;

    @ViewById(R.id.tv_number_zbaj)
    TextView tv_number_zbaj;

    @ViewById(R.id.tv_number_lsaj)
    TextView tv_number_lsaj;
    
    @Bean
    LoginCache loginCache;

    @Click(R.id.ajxx_zb)
    public void ajxxZb() {
        Intent itt = new Intent();
        itt.setClass(getActivity(), AjxxActy_.class);
        itt.putExtra("flag", Constants.AJ_LIST_SCOPE_CXM_ZB);
        startActivity(itt);
    }
    
    @Click(R.id.ajxx_ls)
    public void ajxxLs() {
        Intent itt = new Intent();
        itt.setClass(getActivity(), AjxxActy_.class);
        itt.putExtra("flag", Constants.AJ_LIST_SCOPE_YZM_LS);
        startActivity(itt);
    }
    
    /**
     * 添加案件按钮
     * */
    @Click(R.id.btn_add_case)
    public void addCase(){
        Intent itt = new Intent();
        itt.setClass(getActivity(), AddCaseActivity_.class);
        startActivity(itt);
    }

    @Override
    public void loadAjData(long zbCount, long lsCount, long zbUpdataCount) {
        tv_aj_number.setText(zbUpdataCount+"");
        tv_number_zbaj.setText(zbCount+"");
        tv_number_lsaj.setText(lsCount+"");
        tv_user_name.setText(loginCache.getXm());
    }
    
    @Override
    public void onResume() {
        super.onResume();
        tv_user_name.setText(loginCache.getXm());
    }
    
}
