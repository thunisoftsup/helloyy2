package com.thunisoft.sswy.mobile.fragment.head;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.widget.Button;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.HomeActivity.AjCountData;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.fragment.BaseFragment;

@EFragment(R.layout.head_wrz)
public class HeadWrzFragment extends BaseFragment implements AjCountData {
    @Bean
    LoginCache loginCache;
    
    @ViewById(R.id.btn_smrz)
    Button btn_smrz;
    @ViewById(R.id.tv_big)
    TextView tvName;
//    吉林电子法院暂时屏蔽掉律师认证
//    @ViewById(R.id.btn_lsrz)
//    Button btn_lsrz;
    
    @AfterViews
    public void initView() {
        getView().findViewById(R.id.btn_smrz).setOnClickListener(this);
//      吉林电子法院暂时屏蔽掉律师认证
//        getView().findViewById(R.id.btn_lsrz).setOnClickListener(this);
        getView().findViewById(R.id.img_arrow).setOnClickListener(this);
        tvName.setText(loginCache.getXm());
    }
    
    @Override
    public void onResume() {
        super.onResume();
        tvName.setText(loginCache.getXm());
        getView().findViewById(R.id.img_arrow).setOnClickListener(this);
    }

	@Override
	public void loadAjData(long zbCount, long lsCount, long zbUpdataCount) {
		tvName.setText(loginCache.getXm());		
	}

    
}
