package com.thunisoft.sswy.mobile.fragment.head;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.widget.Button;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.fragment.BaseFragment;

@EFragment(R.layout.head_wdss_wdl)
public class HeadWdssWdlFragment extends BaseFragment {

    @ViewById(R.id.btn_dl)
    Button btn_dl;

    @ViewById(R.id.btn_zc)
    Button btn_zc;

    @ViewById(R.id.btn_zhmm)
    Button btn_zhmm;

    @AfterViews
    public void initViews() {
        btn_dl.setOnClickListener(this);
        btn_zc.setOnClickListener(this);
        btn_zhmm.setOnClickListener(this);
    }
}
