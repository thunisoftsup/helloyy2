package com.thunisoft.sswy.mobile.activity.dzsd;

import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.AddCaseActivity_;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.fragment.SdListFragment_;
import com.thunisoft.sswy.mobile.interfaces.IWaitingDialogNotifier;

/**
 * 我的送达文书列表
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_sd_list)
public class SdListActivity extends BaseActivity implements OnCheckedChangeListener, IWaitingDialogNotifier {

    FragmentManager fagmentManager;
    FragmentTransaction fragmentTransaction;

    @ViewById(R.id.txtSearch)
    EditText txtSearch;

    @ViewById(R.id.radio_left)
    RadioButton radio_left;

    @ViewById(R.id.radio_right)
    RadioButton radio_right;

    Map<String, Fragment> framgmentMap = new HashMap<String, Fragment>();
    Fragment currentFragment;
    WaittingDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fagmentManager = getFragmentManager();
    }

    @AfterViews
    public void initViews() {
        setBtnBack();
        setTitle(R.string.text_wdws);
        txtSearch.setHint(getText(R.string.sd_search_hint));
        radio_left.setText(getText(R.string.text_dqs));
        radio_right.setText(getText(R.string.text_yqs));
        radio_left.setOnCheckedChangeListener(this);
        radio_right.setOnCheckedChangeListener(this);
        radio_left.setChecked(true);
        switchRadio(R.id.radio_left);
        waitDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "...");
        waitDialog.setIsCanclable(false);
    }

    public Fragment getWqsFragment() {
//        Fragment fragment = framgmentMap.get("fragment_wqs");
//        if (fragment == null) {
//            fragment = new SdListFragment_();
//            framgmentMap.put("fragment_wqs", fragment);
//        }
        Fragment fragment = new SdListFragment_();
        ((SdListFragment_)fragment).setWaitingDialogNotifier(this);
        return fragment;
    }

    public Fragment getYqsFragment() {
//        SdListFragment_ sdFragment = (SdListFragment_)framgmentMap.get("fragment_yqs");
//        if (sdFragment == null) {
//            sdFragment = new SdListFragment_();
//            framgmentMap.put("fragment_yqs", sdFragment);
//        }
        SdListFragment_ sdFragment = new SdListFragment_();
        sdFragment.setScope(Constants.SCOPE_DZSD_YQS);
        sdFragment.setWaitingDialogNotifier(this);
        return sdFragment;
    }
    
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_CODE_SHOW_SD_WRIT_LIST_AFTER_DOWNLOAD && resultCode == Activity.RESULT_OK) {
            switchRadio(R.id.radio_right);
        }
    }*/

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            int viewId = buttonView.getId();
            switchRadio(viewId);
        }
    }

    private void switchRadio(int viewId) {
        fragmentTransaction = fagmentManager.beginTransaction();
        Fragment fragment = null;
        switch (viewId) {
        case R.id.radio_left:
            fragment = getWqsFragment();
            break;
        case R.id.radio_right:
            fragment = getYqsFragment();
            break;
        }
        if(currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.layout_center, fragment);
        }
        currentFragment = fragment;
        fragmentTransaction.commit();
    }
    
    @Override
    public void showDialog(String text) {
        waitDialog.setWaittingTxt(text);
        waitDialog.show();
    }

    @Override
    public void dismissDialog() {
        waitDialog.dismiss();
    }
}
