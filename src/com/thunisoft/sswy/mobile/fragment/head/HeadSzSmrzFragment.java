package com.thunisoft.sswy.mobile.fragment.head;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.util.Log;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.fragment.BaseFragment;

@EFragment(R.layout.head_sz_smrz)
public class HeadSzSmrzFragment extends BaseFragment {
    
    @ViewById(R.id.tv_big)
    TextView tv_big;
    
    @Bean
    LoginCache loginCache;
    
    @AfterViews
    public void initViews() {
        tv_big.setText(loginCache.getXm());
        getView().findViewById(R.id.img_arrow).setOnClickListener(this);
    }
    
    @Click(R.id.img_arrow)
    public void imgArrowBtnClicked() {
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        tv_big.setText(loginCache.getXm());
        getView().findViewById(R.id.img_arrow).setOnClickListener(this);
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
    	super.onHiddenChanged(hidden);
    	tv_big.setText(loginCache.getXm());
    }
}
