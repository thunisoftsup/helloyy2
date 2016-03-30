package com.thunisoft.sswy.mobile.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FromHtml;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.WslacxActy_;
import com.thunisoft.sswy.mobile.activity.auth.LoginActivity_;
import com.thunisoft.sswy.mobile.activity.auth.LsrzActivity_;
import com.thunisoft.sswy.mobile.activity.auth.SmrzActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.BuildingAlertDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.RzfsDioagActivity;
import com.thunisoft.sswy.mobile.activity.dialog.RzfsDioagActivity_;
import com.thunisoft.sswy.mobile.activity.dzsd.SdListActivity_;
import com.thunisoft.sswy.mobile.activity.pay.PayOnlineHomeActivity_;
import com.thunisoft.sswy.mobile.adapter.CertTypeAdapter;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.DownloadRegion;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.StringUtils;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    @Bean
    LoginCache aLoginCache;
    @Bean
    ResponseUtilExtr responseUtil;
    @Pref
    ConfigUtils_ configUtils;
    @FromHtml(R.string.text_tgcxmcxaj)
    @ViewById(R.id.tv_ajcx)
    TextView tv_ajcx;

    @FromHtml(R.string.text_tgqmmqsws)
    @ViewById(R.id.tv_wsqs)
    TextView tv_view;

    @Bean
    LoginCache loginCache;
    
    @Bean
    CourtCache courtCache;

    @ViewById(R.id.layout_ajcx)
    View layout_ajcx;

    @ViewById(R.id.layout_wsqs)
    View layout_wsqs;
    boolean isRepeatClick = true;
    
    @Bean
    DownloadRegion downloadRegion;
    
    @AfterViews
    public void initViews() {
        getView().findViewById(R.id.layout_ajcx).setOnClickListener(this);
        getView().findViewById(R.id.layout_wsqs).setOnClickListener(this);
        getView().findViewById(R.id.tv_module_wsqs).setOnClickListener(this);
        dealWithWiewHidden();
        downloadRegion.activity = this.getActivity();
        downloadRegion.downloadRegion();
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.tv_module_wsqs:
            gotoWsqs(R.id.tv_module_wsqs);
            break;
        }
    }
    
    public void dealWidthOpenModuls() throws JSONException {
    	if (getView() == null) return;
    	getView().findViewById(R.id.tv_module_ssxfcx).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.tv_module_wsqs).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.tv_module_wslacx).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.tv_module_wsyjcx).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.tv_module_zxxsjb).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.tv_module_lxfg).setVisibility(View.VISIBLE);
		/**
		 * 此版本写死6个模块，如下个版本按后台配置，请打开此注解，并设初始值为gone
		 * */
//    	if (loginCache.getOpenModule() != null) {
//    		int len = loginCache.getOpenModule().length();
//    		for (int i = 0; i < len; i++) {
//    			String modleId = loginCache.getOpenModule().getString(i);
//    			if (modleId.equals(Constants.APP_ID_WSJF)) {
//    				getView().findViewById(R.id.tv_module_ssxfcx).setVisibility(View.VISIBLE);
//    			} else if (modleId.equals(Constants.APP_ID_DZSD)) {
//    				getView().findViewById(R.id.tv_module_wsqs).setVisibility(View.VISIBLE);
//    			} else if (modleId.equals(Constants.APP_ID_WSLA)) {
//    				getView().findViewById(R.id.tv_module_wslacx).setVisibility(View.VISIBLE);
//    			} else if (modleId.equals(Constants.APP_ID_WSYJ)) {
//    				getView().findViewById(R.id.tv_module_wsyjcx).setVisibility(View.VISIBLE);
//    			} else if (modleId.equals(Constants.APP_ID_ZXXSJB)) {
//    				getView().findViewById(R.id.tv_module_zxxsjb).setVisibility(View.VISIBLE);
//    			} else if (modleId.equals(Constants.APP_ID_LXFG)) {
//    				getView().findViewById(R.id.tv_module_lxfg).setVisibility(View.VISIBLE);
//    			}
//    		}
//    	}
    }
    
    public void dealWithWiewHidden() {
        if (loginCache.isLogined() && (loginCache.isSmrz() || loginCache.isLsrz())) {
            layout_ajcx.setVisibility(View.GONE);
            layout_wsqs.setVisibility(View.GONE);
        } else {
            layout_ajcx.setVisibility(View.VISIBLE);
            layout_wsqs.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            dealWithWiewHidden();
            try {
				dealWidthOpenModuls();
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e(TAG, "attach");
    }

    @Click(R.id.layout_ajcx)
    public void gotoCxmCxaj() {

    }

    public void gotoWsqs(int resId) {
        if (!isRepeatClick) {
            return;
        }
        if(!loginCache.isLogined()) {
            gotoLogin(R.id.tv_module_wsqs);
            return;
        }
        if (!isLsOrSmRz()) {
        	rzfs(resId, "此功能需要认证才能使用!");
            return;
        }
        loadOpenTheModel(Constants.APP_ID_DZSD, SdListActivity_.class);
    }

    @Click(R.id.tv_module_wslacx)
    public void gotoWslacx() {
        if (!isRepeatClick) {
            return;
        }
        if (!isLogin()) {
            gotoLogin(R.id.tv_module_wslacx);
            return;
        }
        loadOpenTheModel(Constants.APP_ID_WSLA, WslacxActy_.class);
    }

    @Click(R.id.tv_module_ssxfcx)
    public void gotoSsxfcx() {
        if (!isRepeatClick) {
            return;
        }
        if (!isLogin()) {
            gotoLogin(R.id.tv_module_ssxfcx);
            return;
        }
//        showBuildingDialog();
        loadOpenTheModel(Constants.APP_ID_WSJF, PayOnlineHomeActivity_.class);

    }

    @Click(R.id.tv_module_wsyjcx)
    public void gotoWsyjcx() {
        if (!isRepeatClick) {
            return;
        }
//        if (!isLogin()) {
//            gotoLogin(R.id.tv_module_wsyjcx);
//            return;
//        }
        showBuildingDialog();
//        loadOpenTheModel(Constants.APP_ID_WSYJ, WsyjcxActy_.class);
    }

    @Click(R.id.tv_module_zxxsjb)
    public void gotoZxxsjb() {
        if (!isRepeatClick) {
            return;
        }
//        if (!isLogin()) {
//            gotoLogin(R.id.tv_module_zxxsjb);
//            return;
//        }
        showBuildingDialog();
//        loadOpenTheModel(Constants.APP_ID_ZXXSJB, ZxxsjbActy_.class);
    }

    @Click(R.id.tv_module_lxfg)
    public void gotoLxfg() {
        if (!isRepeatClick) {
            return;
        }
//        if (!isLogin()) {
//            gotoLogin(R.id.tv_module_lxfg);
//            return;
//        }
//        
//        if (!isLsOrSmRz()) {
//        	rzfs(R.id.tv_module_lxfg, "此功能需要认证才能使用!");
//        	//Toast.makeText(getActivity(), "此功能需要认证才能使用!", Toast.LENGTH_LONG).show();
//            return;
//        }
        showBuildingDialog();
//        loadOpenTheModel(Constants.APP_ID_LXFG, LxfgActy_.class);
    }
    
    private void showBuildingDialog(){
        Intent intent = new Intent(getActivity(), BuildingAlertDialogActivity_.class);
        intent.putExtra("message", "功能建设中\n  敬请期待");
        startActivity(intent);
    }
    
    private boolean isLogin() {
        return aLoginCache.isLogined();
    }
    
    private boolean isLsOrSmRz() {
        return aLoginCache.isSmrz()||aLoginCache.isLsrz();
    }
    
    @Background
    public void loadOpenTheModel(String moduleId, Class<?> goToClss) {
//        isRepeatClick = false;
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("moduleId", moduleId+""));
//        String courtId = courtCache.getCourtId();
//        if (courtId == null) {
//            Toast.makeText(getActivity(), "没有找到相应的法院...", Toast.LENGTH_LONG).show();
//            return;
//        }
//        params.add(new BasicNameValuePair("courtId", courtId+""));
//        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_OPEN_MODEL_FLAG, params);
        onDataDone(moduleId, goToClss);
    }
    
    @UiThread
    public void onDataDone(String moduleId, Class<?> goToClss) {
        if (getActivity() == null) {
        	isRepeatClick = true;
            return;
        }
//        if (br.getMsg() != null) {
//        	isRepeatClick = true;
//            Toast.makeText(getActivity(), br.getMsg(), Toast.LENGTH_LONG).show();
//        } else {
            Intent itt = new Intent();
            itt.setClass(getActivity(), goToClss);
            startActivity(itt);
//        }
    }
    
    private void gotoLogin(int resId) {
        Intent itt = new Intent();
        itt.setClass(getActivity(), LoginActivity_.class);
        itt.putExtra("resId", resId);
        itt.putExtra("message", "此功能需要登录才能使用!");
        startActivityForResult(itt, Constants.REQUEST_CODE_LOGINSUCESS);
        //Toast.makeText(getActivity(), "此功能需要登录才能使用!", Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	isRepeatClick = true;
    	  try {
  			dealWidthOpenModuls();
  		} catch (JSONException e) {
  			e.printStackTrace();
  		}
    }
    
    private void gotoSmrz(int resId) {
        Intent itt = new Intent();
        itt.putExtra("resId", resId);
        itt.setClass(getActivity(), SmrzActivity_.class);
        startActivityForResult(itt, Constants.REQUEST_CODE_SMRZ);
    }
    
    private void gotoLsrz(int resId) {
    	Intent itt = new Intent();
    	itt.putExtra("resId", resId);
    	itt.setClass(getActivity(), LsrzActivity_.class);
    	startActivityForResult(itt, Constants.REQUEST_CODE_LSRZ);
    }
    
    public void rzfs(int resId, String message) {
        Intent intent = new Intent(getActivity(), RzfsDioagActivity_.class);
        intent.putExtra(RzfsDioagActivity.K_RZFS_CODE, resId);
        if(StringUtils.isNotBlank(message)) {
            intent.putExtra("message", message);
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_XZZJ);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
        case Constants.REQUEST_CODE_XZZJ:
            if (resultCode == Activity.RESULT_OK) {
                String zjlxId = data.getStringExtra(CertTypeAdapter.K_SELECTED_CODE);
                if (zjlxId == null) return;
                int resId = data.getIntExtra("resId", 0);
        		if (resId != 0) {
        			if (zjlxId.equals("1")) {
        				gotoSmrz(resId);
        			} else {
        				gotoLsrz(resId);
        			}
        		}
            }
            break;
        case Constants.REQUEST_CODE_LOGINSUCESS:
        	if (resultCode == Activity.RESULT_OK) {
        		if (data == null) return;
        		int resId = data.getIntExtra("resId", 0);
        		if (resId != 0) {
        			getView().findViewById(resId).performClick();
        		}
            }
        	break;
        case Constants.REQUEST_CODE_SMRZ:
        	if (resultCode == Activity.RESULT_OK) {
        		if (data == null) return;
        		int resId = data.getIntExtra("resId", 0);
        		if (resId != 0) {
        			getView().findViewById(resId).performClick();
        		}
            }
        	break;
        case Constants.REQUEST_CODE_LSRZ:
        	if (resultCode == Activity.RESULT_OK) {
        		if (data == null) return;
        		int resId = data.getIntExtra("resId", 0);
        		if (resId != 0) {
        			getView().findViewById(resId).performClick();
        		}
            }
        	break;
        }
    }
}
