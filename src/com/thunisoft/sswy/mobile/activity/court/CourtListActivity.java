package com.thunisoft.sswy.mobile.activity.court;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.HomeActivity;
import com.thunisoft.sswy.mobile.activity.HomeActivity_;
import com.thunisoft.sswy.mobile.activity.nrc.NetRegisterCaseBasicActivity_;
import com.thunisoft.sswy.mobile.activity.transition.LoadingActivity_;
import com.thunisoft.sswy.mobile.adapter.ProvinceAdapter;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.fragment.CourtListFragment_;
import com.thunisoft.sswy.mobile.interfaces.IOnCourtSelectedListener;
import com.thunisoft.sswy.mobile.interfaces.IOnNeedLoadingListener;
import com.thunisoft.sswy.mobile.logic.CourtLogic;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.logic.response.CourtResponse;
import com.thunisoft.sswy.mobile.pojo.TCourt;
import com.thunisoft.sswy.mobile.pojo.TProvince;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 法院选择
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_court_list)
public class CourtListActivity extends BaseActivity implements IOnCourtSelectedListener {
    @Bean
    CourtLogic courtLogic;
    
    @Bean
    CourtCache courtCahce;

    @Pref
    ConfigUtils_ configUtils;
    
    @Bean
    ResponseUtilExtr responseUtil;
    
   
    ListView provinceListView;

    List<TProvince> provinceList = new ArrayList<TProvince>();

    ProvinceAdapter provinceAdapter;

    TProvince currentProvince;
    View currentItemView;

    List<CourtListFragment_> courtListFragmentList;

    FragmentManager fragmentManager;

    FragmentTransaction transaction;

    TCourt selectedCourt;

   
    LinearLayout selectOkLayout;

    TextView action_title;

    @Extra("showBack")
    boolean showBack;
    
    boolean isNotClickPrinvice;
    String currfjm;
    CourtListFragment_ fragment;
    boolean isNetworkFail;
    
    /**
     * 是否为：网上立案 选择法院， intent key
     */
    public static final String K_IS_NRC_SELECT_COURT = "isNrcSelectCourt";
    
    /**
     * 是否为：网上立案 选择法院， intent key
     */
    public static final String K_IS_NRC_SAVAE_OR_SUBMIT_COURT = "isNrcSaveOrSubmitCourt";
    
    /**
     * 是否为：网上立案 选择法院
     */
    private boolean isNrcSelectCourt = false; 
    
    /**
     * 网上立案提交和暂存时选择法院
     */
    private boolean isNrcSaveOrSubmitCourt = false;
    
    /**
     * 是否为 网上立案查询列表  添加_网上立案  intent key
     */
    public static final String K_IS_WSLACX_ADD = "isWslacxAdd";
    
    /**
     * 是否为 网上立案 添加
     */
    private boolean isWslacxAdd = false;
    
    @AfterViews
    public void initViews() {
//    	if (isNetworkFail) {
//    		return;
//    	}
    	Intent intent = getIntent();
    	isNrcSelectCourt = intent.getBooleanExtra(K_IS_NRC_SELECT_COURT, false);
    	isWslacxAdd = intent.getBooleanExtra(K_IS_WSLACX_ADD, false);
    	isNrcSaveOrSubmitCourt = intent.getBooleanExtra(K_IS_NRC_SAVAE_OR_SUBMIT_COURT, false);
    	action_title = (TextView) findViewById(R.id.action_title);
    	selectOkLayout = (LinearLayout)findViewById(R.id.select_court_ok);
        if (courtCahce.getCourtId() != null) {
            setBtnBack();
        } else {
        	findViewById(R.id.btn_back).setVisibility(View.INVISIBLE);
        }
        action_title.setText("请选择法院");
        fragmentManager = getFragmentManager();
        provinceAdapter = new ProvinceAdapter(CourtListActivity.this, R.layout.province_list_item, provinceList);
        currfjm = "";//configUtils.getSharedPreferences().getString("current_province", "");
        provinceAdapter.setCurrFjm(currfjm);
        isNotClickPrinvice = true;
        provinceListView = (ListView) findViewById(R.id.province_list);
        provinceListView.setAdapter(provinceAdapter);
        provinceAdapter.setNotifyOnChange(false);
        provinceListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	TextView tv = (TextView) view.findViewById(R.id.tv_province_name);
            	if (tv.getText().equals("")) {
            		return;
            	}
                TProvince province = (TProvince) parent.getItemAtPosition(position);
                selectOkLayout.setVisibility(View.GONE);
                if (province != null
                        && (currentProvince == null || !province.getCId().equals(currentProvince.getCId()))) {
                    isNotClickPrinvice = false;
                    if (currentItemView == null) {
                        currentItemView = provinceAdapter.getDefaltCheckedVeiw();
                    }
                    if (currentItemView != null) {
                        currentItemView.setBackgroundResource(R.color.province_item_default);
                    } 
                    currentProvince = province;
                    currentItemView = view;
                    view.setBackgroundResource(R.color.province_item_sel);
                    final String fjm = currentProvince.getCFjm();
                    showNewFragment(fjm);
                    provinceAdapter.setCurrFjm(fjm);
                }
            }
        });
        loadProvinceList();
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    private void showNewFragment(final String fjm) {
        final String fragTag = fjm + "Fragment";
        fragment = (CourtListFragment_) fragmentManager.findFragmentByTag(fragTag);
        if (fragment == null) {
            fragment = new CourtListFragment_();
            fragment.setOnNeedLoadingListener(new IOnNeedLoadingListener<TCourt>() {
                @Override
                public List<TCourt> load() {
                    long DUpdate = configUtils.getSharedPreferences().getLong("fjm_update_"+fjm, 0L);
                    CourtResponse cr = courtLogic.loadCourtList(fjm, DUpdate);
                    if (!cr.isSuccess()) {
                        showToast(cr.getMessage());
                        return new ArrayList<TCourt>();
                    }
                    List<TCourt> courtList = cr.getData();
                    if(courtList!=null) {
                        configUtils.getSharedPreferences().edit().putLong("fjm_update_"+fjm, cr.getDUpdate());
                    }
                    return courtList;
                }
            });
            fragment.setOnCourtSelectedListener(CourtListActivity.this);
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.layout_court_list, fragment, fragTag);
            transaction.commitAllowingStateLoss();
        }
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Background
    public void loadProvinceList() {
    	 List<NameValuePair> params = new ArrayList<NameValuePair>();
         BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_SF_LIST, params);
         loadProvinceDone(br);
    }

    @UiThread
    public void loadProvinceDone(BaseResponseExtr br) {
        provinceList.clear();
        if (StringUtils.isNotBlank(br.getMsg())) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
            if (courtCahce.getCourtId() == null) {
            	isNetworkFail = true;
            } else {
                if(HomeActivity.instance == null) {
                    Intent intent = new Intent(CourtListActivity.this, HomeActivity_.class);
                    startActivity(intent);
                }
                finish();
            }
            findViewById(R.id.nonettip).setVisibility(View.VISIBLE);
            findViewById(R.id.content).setVisibility(View.GONE);
            findViewById(R.id.btn_refresh_nonet).setOnClickListener(new OnClickListener() {
            	@Override
            	public void onClick(View v) {
            		loadProvinceList();
            	}
            });
        } else {
        	findViewById(R.id.nonettip).setVisibility(View.GONE);
        	findViewById(R.id.content).setVisibility(View.VISIBLE);
        	try {
				JSONArray data = br.getResJson().getJSONArray("data");
				int len = data.length();
				for (int i=0; i<len; i++) {
					TProvince aTProvince = new TProvince();
					JSONObject aJsnObj = data.getJSONObject(i);
					aTProvince.setCFjm(aJsnObj.getString("CFjm"));
					aTProvince.setCId(aJsnObj.getString("CId"));
					aTProvince.setCName(aJsnObj.getString("CName"));
					aTProvince.setNOrder(i);
					provinceList.add(aTProvince);
					if (currfjm == null && i == 0) {
						currfjm = aJsnObj.getString("CFjm");
						provinceAdapter.setCurrFjm(currfjm);
					}
				}
				TProvince aTProvince = new TProvince();
				aTProvince.setCFjm("");
				aTProvince.setCId("");
				aTProvince.setCName("全部");
				aTProvince.setNOrder(0);
				provinceList.add(0, aTProvince);
			} catch (JSONException e) {
				Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
			}
        }
        int len = provinceList.size();
        for (int i=0; i<len; i++) {
            TProvince aTProvince = provinceList.get(i);
            if (isNotClickPrinvice && i!=0 && aTProvince != null && currfjm != null && currfjm.equals(aTProvince.getCFjm())) {
                provinceListView.setSelection(i-1);
            }
        }
        provinceList.add(null);
        provinceList.add(null);
        provinceList.add(null);
        provinceAdapter.notifyDataSetChanged();
        if (currfjm != null) {
            showNewFragment(currfjm);
        }
    }

    @Override
    public void select(TCourt court) {
        TextView tv_court_name = (TextView) selectOkLayout.findViewById(R.id.tv_select_court_name);
        tv_court_name.setText(court.getCName());
        action_title.setText(court.getCName());
        //if (selectedCourt == null) {
        Animation ani = AnimationUtils.loadAnimation(CourtListActivity.this, R.anim.slide_in_from_bottom);
        selectOkLayout.startAnimation(ani);
        selectOkLayout.setVisibility(View.VISIBLE);
        //}
        selectedCourt = court;
    }
    
    public void makeDefaltSelect() {
        TextView tv_court_name = (TextView) selectOkLayout.findViewById(R.id.tv_select_court_name);
        String cName = configUtils.getSharedPreferences().getString("current_court_name", "");
        tv_court_name.setText(cName);
        action_title.setText(cName);
        //if (selectedCourt == null) {
        Animation ani = AnimationUtils.loadAnimation(CourtListActivity.this, R.anim.slide_in_from_bottom);
        selectOkLayout.startAnimation(ani);
        selectOkLayout.setVisibility(View.VISIBLE);
        //}
    }

    @Click(R.id.btn_select_court_ok)
    public void okClick() {
    	
        Intent intent = new Intent();
        if (selectedCourt != null) {
            intent.putExtra("courtId", selectedCourt.getCId());
            intent.putExtra("courtName", selectedCourt.getCName());
            if (!isNrcSaveOrSubmitCourt && !isNrcSaveOrSubmitCourt && !isNrcSelectCourt && !isWslacxAdd) { //在网上立案查询列表，或者网上立案 跳转过来的时候，不需要更新courtCache
            	courtCahce.setCourtId(selectedCourt.getCId());
                courtCahce.setCourtName(selectedCourt.getCName());
                configUtils.getSharedPreferences().edit().putString("current_fjm", selectedCourt.getCFjm()).commit();
                if (currentProvince != null) {
                    configUtils.getSharedPreferences().edit().putString("current_province", "").commit();
                }
            }
        } else {
            String courtId = courtCahce.getCourtId("");
            if (courtId.equals("")) {
            	courtId = fragment.getCid();
            	if (!isNrcSaveOrSubmitCourt && !isNrcSelectCourt && !isWslacxAdd) { //在网上立案查询列表，或者网上立案 跳转过来的时候，不需要更新courtCache
            		courtCahce.setCourtId(courtId);
                	courtCahce.setCourtName(fragment.getCurrCountName());
                    configUtils.getSharedPreferences().edit().putString("current_fjm", currfjm).commit();
            	}
            }
            intent.putExtra("courtId", courtId);
            intent.putExtra("courtName", courtCahce.getCourtName());
        }
        
        if (isNrcSaveOrSubmitCourt) {
        	setResult(Constants.RESULT_OK, intent);
        } else if (isNrcSelectCourt) {
        	setResult(Constants.RESULT_OK, intent);
        } else {
        	if (isWslacxAdd) {
        		intent.setClass(this, NetRegisterCaseBasicActivity_.class);
        	} else {
        		intent.setClass(this, LoadingActivity_.class);
        	}
        	startActivity(intent);
        }
        finish();
    }

    /**
     * 不做任何事情，防止点击事件透过层
     */
    @Click(R.id.select_court_ok)
    public void okLayoutClick() {

    }
}
