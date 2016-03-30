package com.thunisoft.sswy.mobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.fragment.BaseFragment;
import com.thunisoft.sswy.mobile.fragment.FyZxFragment;
import com.thunisoft.sswy.mobile.fragment.HomeFragment;
import com.thunisoft.sswy.mobile.fragment.HomeFragment_;
import com.thunisoft.sswy.mobile.fragment.head.HeadSzLsrzFragment_;
import com.thunisoft.sswy.mobile.fragment.head.HeadSzSmrzFragment_;
import com.thunisoft.sswy.mobile.fragment.head.HeadSzWdlFragment_;
import com.thunisoft.sswy.mobile.fragment.head.HeadWdssLsrzFragment_;
import com.thunisoft.sswy.mobile.fragment.head.HeadWdssSmrzFragment_;
import com.thunisoft.sswy.mobile.fragment.head.HeadWdssWdlFragment_;
import com.thunisoft.sswy.mobile.fragment.head.HeadWrzFragment_;
import com.thunisoft.sswy.mobile.fragment.setting.LsrzFragment_;
import com.thunisoft.sswy.mobile.fragment.setting.SettingSmrzFragment_;
import com.thunisoft.sswy.mobile.fragment.setting.SettingWdlFragment_;
import com.thunisoft.sswy.mobile.fragment.setting.SettingWrzLsrzFragment_;
import com.thunisoft.sswy.mobile.interfaces.IFragmentChangeNotifier;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.NetworkStateUtils;
import com.thunisoft.sswy.mobile.util.UpdateUtils;

@EActivity(R.layout.activity_home)
public class HomeActivity extends Activity implements OnCheckedChangeListener, IFragmentChangeNotifier {
    @Bean
    ResponseUtilExtr responseUtil;
    private static final String TAG = "HomeActivity";
    private static final int FYZX = 0;
    private static final int WDSS = 1;
    private static final int SETTING = 2;
    private static int currModel;
    
    public static Activity instance;
    
    FragmentManager fagmentManager;
    FragmentTransaction fragmentTransaction;

    @Bean
    LoginCache loginCache;

//    @ViewById(R.id.nav_fyzx)
//    RadioButton nav_fyzx;

    @ViewById(R.id.nav_wdss)
    RadioButton nav_wdss;

    @ViewById(R.id.nav_sz)
    RadioButton nav_sz;

    Fragment mHeadFragment;
    Fragment mCenterFragment;

    RadioButton currentRadio;

    @Bean
    NetUtils netUtils;

    @Bean
    UpdateUtils updateUtils;

    @Pref
    ConfigUtils_ configUtils;
    
    @Bean
    CourtCache courtCache;

    @Bean
    NetworkStateUtils networkStateUtils;

    public interface AjCountData {
        public void loadAjData(long zbCount, long lsCount, long zbUpdataCount);
    }
    
    Map<String, Fragment> fragmentMap = new HashMap<String, Fragment>();

    Stack<Fragment[]> backStack = new Stack<Fragment[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fagmentManager = getFragmentManager();
        instance = this;
    }

    
    
    @AfterViews
    public void initViews() {
        initNavBtns();
//        if (currModel == FYZX) {
//            nav_fyzx.setChecked(true); 
//        } else 
        if (currModel == SETTING) {
            nav_sz.setChecked(true);
        } else {
            nav_wdss.setChecked(true);
        }
        if (networkStateUtils.isWifiConnected()) {
            if (System.currentTimeMillis() - configUtils.getSharedPreferences().getLong("last_update", 0) > 1000*60*60*12) {
                configUtils.getSharedPreferences().edit().putLong("last_update", System.currentTimeMillis()).commit();
                updateUtils.checkUpdateAuto(this);
            }
        }

    }

    boolean checked = false;

    public void initNavBtns() {
//        nav_fyzx.setOnCheckedChangeListener(this);
        nav_wdss.setOnCheckedChangeListener(this);
        nav_sz.setOnCheckedChangeListener(this);
    }

//    private Fragment getFyZxFragment() {
//        Fragment fyzxFrag = fragmentMap.get("center_fyzx");
//        if (fyzxFrag == null) {
//            fyzxFrag = new FyZxFragment_();
//            fragmentMap.put("center_fyzx", fyzxFrag);
//        }
//        return fyzxFrag;
//    }
//
//    private void replaceFyzxFragment() {
//        Fragment fragment = getFyZxFragment();
//        switchFragment(null, fragment, true, false);
//    }

    private FyZxFragment mFyZxFragment;
    public void switchFragment(Fragment toHead, Fragment toCenter, boolean clearStack, boolean pushLastToStack) {
        if (toCenter instanceof FyZxFragment) {
        	mFyZxFragment = (FyZxFragment)toCenter;
        } else {
        	mFyZxFragment = null;
        }
    	if (toCenter == null) {
            return;
        }
        if (clearStack && !backStack.isEmpty()) {
            backStack.removeAllElements();
        }
        fragmentTransaction = fagmentManager.beginTransaction();

        if (mHeadFragment != null) {
            fragmentTransaction.hide(mHeadFragment);
        }
        if (mCenterFragment != null) {
            fragmentTransaction.hide(mCenterFragment);
            if (pushLastToStack) {
                Fragment[] fragArr = { mHeadFragment, mCenterFragment };
                backStack.push(fragArr);
            }

        }
        if (toHead != null) {
            if (toHead.isAdded()) {
                fragmentTransaction.show(toHead);
            } else {
                fragmentTransaction.add(R.id.layout_head_base, toHead);
            }
            mHeadFragment = toHead;
        } else {
            mHeadFragment = null;
        }
        if (toCenter != null) {
            if (toCenter.isAdded()) {
                fragmentTransaction.show(toCenter);
            } else {
                fragmentTransaction.add(R.id.layout_center_base, toCenter);
            }
            mCenterFragment = toCenter;
        }

        fragmentTransaction.commit();
    }

    private Fragment getWdssHeader() {
        Fragment fragment = null;
        if (!loginCache.isLogined()) {
            fragment = fragmentMap.get("head_wdss_wdl");
        } else if (loginCache.isNotVerified()) {
            fragment = fragmentMap.get("head_wdss_wrz");
        } else if (loginCache.isSmrz()) {
            fragment = fragmentMap.get("head_wdss_smrz");
        } else if (loginCache.isLsrz()) {
            fragment = fragmentMap.get("head_wdss_lsrz");
        }

        if (fragment == null) {
            if (!loginCache.isLogined()) {
                fragment = new HeadWdssWdlFragment_();
                fragmentMap.put("head_wdss_wdl", fragment);
            } else if (loginCache.isNotVerified()) {
                fragment = new HeadWrzFragment_();
                fragmentMap.put("head_wdss_wrz", fragment);
                ((BaseFragment) fragment).setFragmentChangeNotifier(this);
            } else if (loginCache.isSmrz()) {
                fragment = new HeadWdssSmrzFragment_();
                fragmentMap.put("head_wdss_smrz", fragment);
            } else if (loginCache.isLsrz()) {
                fragment = new HeadWdssLsrzFragment_();
                fragmentMap.put("head_wdss_lsrz", fragment);
            }
        }
        
        if (loginCache.isLogined()) {
            AjCountData ajCountData = (AjCountData) fragment;
            loadAjCount(ajCountData);
        }
        return fragment;
    }

    @Background
    public void loadAjCount(AjCountData ajCountData) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fy", courtCache.getCourtId()));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_AjXX_COUNT, params);
        loadDataDone(br, ajCountData);
    }
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br, AjCountData ajCountData) {
        // 更新UI
        if (br.getMsg() != null) {
//            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
                long zbCount = br.getResJson().getLong("zbCount");
                long lsCount = br.getResJson().getLong("lsCount");
                long zbUpdataCount = br.getResJson().getLong("zbUpdateCount");
                String name = br.getResJson().getString("name");
                String zjhm = br.getResJson().optString("zjhm");
                String phone = br.getResJson().optString("phone");
                int loginType = br.getResJson().getInt("loginType");
                loginCache.setXm(name);
                loginCache.setZjhm(zjhm);
                loginCache.setPhone(phone);
                JSONArray openModules =  br.getResJson().getJSONArray("openModules");
                loginCache.setOpenModule(openModules);
                if (currModel == WDSS) {
                	try {
            			((HomeFragment) getWdssBody()).dealWidthOpenModuls();
            		} catch (JSONException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                }
                if (loginType != loginCache.getLoginType()) {
                	loginCache.setLoginType(loginType);
                	replaceWdssFragment();
                	return;
                }
                ajCountData.loadAjData(zbCount, lsCount, zbUpdataCount);
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private Fragment getWdssBody() {
        Fragment fragment = fragmentMap.get("center_wdss");
        if (fragment == null) {
            fragment = new HomeFragment_();
            fragmentMap.put("center_wdss", fragment);
        }
        return fragment;
    }

    private void replaceWdssFragment() {
        Fragment header = getWdssHeader();
        Fragment body = getWdssBody();
        switchFragment(header, body, true, false);
    }

    private Fragment getSettingHead() {
        Fragment fragment = null;
        if (!loginCache.isLogined()) {
            fragment = fragmentMap.get("head_set_wdl");
        } else if (loginCache.isNotVerified()) {
            fragment = fragmentMap.get("head_set_wrz");
        } else if (loginCache.isSmrz()) {
            fragment = fragmentMap.get("head_set_smrz");
        } else if (loginCache.isLsrz()) {
            fragment = fragmentMap.get("head_set_lsrz");
        }
        if (fragment == null) {
            if (!loginCache.isLogined()) {
                fragment = new HeadSzWdlFragment_();
                fragmentMap.put("head_set_wdl", fragment);
            } else if (loginCache.isNotVerified()) {
                fragment = new HeadWrzFragment_();
                fragmentMap.put("head_set_wrz", fragment);
                ((BaseFragment) fragment).setFragmentChangeNotifier(this);
            } else if (loginCache.isSmrz()) {
                fragment = new HeadSzSmrzFragment_();
                fragmentMap.put("head_set_smrz", fragment);
            } else if (loginCache.isLsrz()) {
                fragment = new HeadSzLsrzFragment_();
                fragmentMap.put("head_set_lsrz", fragment);
            }
        }
        return fragment;
    }

    private Fragment getSettingBody() {
        Fragment fragment = null;
        if (!loginCache.isLogined()) {
            fragment = fragmentMap.get("center_set_wdl");
        } else if (loginCache.isNotVerified()) {
            fragment = fragmentMap.get("center_set_wrz");
        } else if (loginCache.isSmrz()) {
            fragment = fragmentMap.get("center_set_smrz");
        } else if (loginCache.isLsrz()) {
            fragment = fragmentMap.get("center_set_lsrz");
        }
        if (fragment == null) {
            if (!loginCache.isLogined()) {
                fragment = new SettingWdlFragment_();
                fragmentMap.put("center_set_wdl", fragment);
            } else if (loginCache.isNotVerified()) {
                fragment = new SettingWrzLsrzFragment_();
                fragmentMap.put("center_set_wrz", fragment);
            } else if (loginCache.isSmrz()) {
                fragment = new SettingSmrzFragment_();
                fragmentMap.put("center_set_smrz", fragment);
            } else if (loginCache.isLsrz()) {
                fragment = new SettingWrzLsrzFragment_();
                fragmentMap.put("center_set_lsrz", fragment);
            }
        }
        ((BaseFragment) fragment).setFragmentChangeNotifier(this);
        return fragment;
    }

    private void replaceSettingFragment() {
        Fragment head = getSettingHead();
        Fragment body = getSettingBody();
        switchFragment(head, body, true, false);
    }

    private Fragment getLsrzFragment() {
        Fragment fragment = fragmentMap.get("center_lsrz");
        if (fragment == null) {
            fragment = new LsrzFragment_();
            ((BaseFragment) fragment).setFragmentChangeNotifier(this);
            fragmentMap.put("center_lsrz", fragment);
        }
        return fragment;
    }

    public void replaceLsrzFragment() {
        Fragment fragment = getLsrzFragment();
        switchFragment(null, fragment, false, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            currentRadio = (RadioButton) buttonView;
            switchRadio(buttonView.getId());
        }
    }

    private void switchRadio(int viewId) {
        switch (viewId) {
//        case R.id.nav_fyzx:
//            currModel = FYZX;
//            replaceFyzxFragment();
//            break;
        case R.id.nav_wdss:
            currModel = WDSS;
            replaceWdssFragment();
            break;
        case R.id.nav_sz:
            currModel = SETTING;
            replaceSettingFragment();
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, courtCache.getCourtId(""));
        Log.e(TAG, courtCache.getPreCourtId()+"");
        Log.e(TAG, courtCache.isZj()+"");
        if(!courtCache.getCourtId("").equals(courtCache.getPreCourtId()) && (courtCache.getPreZj() == null || courtCache.isZj() != courtCache.getPreZj().booleanValue())) {
            zxdl(RESULT_OK);
        }
        courtCache.setPreCourtId(courtCache.getCourtId());
        courtCache.setPreZj(courtCache.isZj());
        switchRadio(currentRadio.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "result");
        switch (requestCode) {
        case Constants.REQUEST_CODE_CONFIRM_ZX:
            zxdl(resultCode);
            break;
        }
    }

    /**
     * 注销登陆，
     * 
     * @param resultCode
     */
    public void zxdl(int resultCode) {
        if (resultCode == RESULT_OK) {
            loginCache.clear();
            netUtils.setSId(null);
        }
    }

    public void emptyClick(View v) {

    }

    @Override
    public void onBackPressed() {
        if (currentRadio.getId() != R.id.nav_wdss) {
     		nav_wdss.performClick();
     		return;
     	}

    	if (mFyZxFragment != null && mFyZxFragment.onBackPress()) {
    		return;
    	}
    	
        if (mCenterFragment == null
                || !(mCenterFragment instanceof BaseFragment && ((BaseFragment) mCenterFragment).onBackPressed())) {
            if (fagmentManager.getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                fagmentManager.popBackStack();
            }
        }
    }
    
    @Override
    public void back() {
        Fragment[] fragArry = backStack.pop();
        switchFragment(fragArry[0], fragArry[1], false, false);
     }

    @Override
    public void notifySuccessAndBack() {
        switchRadio(currentRadio.getId());
    }
    
    public void onSettingClick(View view) {
    	if (currentRadio.getId() != R.id.nav_wdss) {
    		nav_wdss.performClick();
    	} else {
    		nav_sz.performClick();
    	}
    	
    }

}
