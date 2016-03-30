package com.thunisoft.sswy.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.dialog.CustomConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.util.AppSecretUtil;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 添加案件
 * 
 * @author zhaoy
 * 
 */
@EActivity(R.layout.activity_add_case)
public class AddCaseActivity extends BaseActivity {
	
	private static final String TAG = "AddCaseActivity";

    @ViewById(R.id.tv_add_case_num_year)
    EditText et_add_case_num_year;

    @ViewById(R.id.tv_add_case_num_word)
    EditText et_add_case_num_word;

    @ViewById(R.id.tv_add_case_num_num)
    EditText et_add_case_num_num;

    @ViewById(R.id.tv_query_num)
    EditText et_query_num;
    
    @ViewById(R.id.button_clear)
    Button clearButton;
    
    @ViewById(R.id.tv_tips_err_info)
    TextView tv_tips_err_info;
    
    @ViewById(R.id.tv_tips_err)
    LinearLayout tv_tips_err;
    
    @ViewById(R.id.tv_add_case_num)
    TextView etAddCaseNum;
    

    @Bean
    ResponseUtilExtr responseUtil;
    
    @Bean
    LoginCache loginCache;
    
    @Extra("tempSid")
    String tempSid;
    
    @Pref
    ConfigUtils_ configUtils;
    
    /**
     * 默认在办案件
     */
    private int sfja = 1;
    

    @AfterViews
    public void initViews() {
    	setTitle("添加案件");
        findViewById(R.id.btn_add).setOnClickListener(this);
        et_query_num.addTextChangedListener(mTextWatcher);
        clearButton.setOnClickListener(this);
        setBtnBack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_add:
        	tv_tips_err_info.setText("");
        	tv_tips_err.setVisibility(View.GONE);
            addCaseClick();
            break;
        case R.id.button_clear:
        	et_query_num.setText("");
            break;
        }
        super.onClick(v);
    }

    public void addCaseClick() {
    	
    	String ajNum = etAddCaseNum.getText().toString().trim();
    	
        String caseYear = et_add_case_num_year.getText().toString().trim();
        String caseWord = et_add_case_num_word.getText().toString().trim();
        String caseNum = et_add_case_num_num.getText().toString().trim();
        String caseQueryNum = et_query_num.getText().toString().trim();
        if (StringUtils.isBlank(ajNum)) {
        	tv_tips_err_info.setText("请输入完整案号");
        	tv_tips_err.setVisibility(View.VISIBLE);
            return;
        }
        if (StringUtils.isBlank(caseQueryNum)) {
        	tv_tips_err_info.setText("请输入查询码");
        	tv_tips_err.setVisibility(View.VISIBLE);
            return;
        }
        doAddCase(ajNum,caseYear,caseWord,caseNum,caseQueryNum, loginCache.getXm());
    }

    @Background
    public void doAddCase(String ajNum,String caseYear,String caseWord,String caseNum,String caseQueryNum, String name) {
    	int currNetdr;
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	currNetdr = ResponseUtilExtr.LOAD_ADD_CASE;
    	params.add(new BasicNameValuePair("CAh", AppSecretUtil.encodeAppString(ajNum)));
        params.add(new BasicNameValuePair("nh", AppSecretUtil.encodeAppString(caseYear)));
        params.add(new BasicNameValuePair("spzh", AppSecretUtil.encodeAppString(caseWord)));
        params.add(new BasicNameValuePair("phh", AppSecretUtil.encodeAppString(caseNum)));
        params.add(new BasicNameValuePair("cxm", AppSecretUtil.encodeAppString(caseQueryNum)));
        Log.i(TAG, "OUT-caseYear="+caseYear+"caseWord"+caseWord+"caseNum"+caseNum+"caseQueryNum"+caseQueryNum);
    	BaseResponseExtr br = responseUtil.getResponse(currNetdr, params);
    	loadDataDone(br);

    }
    
    
    @Background
    public void doAddCaseOld(String ajNum,String caseYear,String caseWord,String caseNum,String caseQueryNum, String name) {
    	int currNetdr;
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	currNetdr = ResponseUtilExtr.LOAD_ADD_CASE;
    	params.add(new BasicNameValuePair("CAh", AppSecretUtil.encodeAppString(ajNum)));
        params.add(new BasicNameValuePair("nh", AppSecretUtil.encodeAppString(caseYear)));
        params.add(new BasicNameValuePair("spzh", AppSecretUtil.encodeAppString(caseWord)));
        params.add(new BasicNameValuePair("phh", AppSecretUtil.encodeAppString(caseNum)));
        params.add(new BasicNameValuePair("cxm", AppSecretUtil.encodeAppString(caseQueryNum)));
        Log.i(TAG, "OUT-caseYear="+caseYear+"caseWord"+caseWord+"caseNum"+caseNum+"caseQueryNum"+caseQueryNum);
    	BaseResponseExtr br = responseUtil.getResponse(currNetdr, params);
    	loadDataDone(br);

    }
    
    public Handler handler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		super.handleMessage(msg);
    		Bundle b = msg.getData();
    		String str = b.getString("msg");
    		tv_tips_err_info.setText(str);
    		tv_tips_err.setVisibility(View.VISIBLE);
    	}
    };
    
    public void loadDataDone(BaseResponseExtr br) {
    	if(br.getMsg() != null){
    		if("本案已存在".equals(br.getMsg())){
    			JSONObject json = null;
        		try {
        			json = br.getResJson().getJSONObject("data");
    				String  sfjaSb = json.getString("sfja");
    				if(null!=sfjaSb){
    					sfja = Integer.parseInt(sfjaSb);
    				}
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
                Intent intent = new Intent(this, CustomConfirmDialogActivity_.class);
                intent.putExtra("message", "本案已存在");
                intent.putExtra("negative", "返回");
                intent.putExtra("positive", "查看");
                startActivityForResult(intent, Constants.REQUEST_CODE_ADDCASESUCESS);
    		}else{
    			Message msg = new Message();
    			Bundle b = new Bundle();// 存放数据
    			b.putString("msg", br.getMsg());
    			msg.setData(b);
    			handler.sendMessage(msg);
    		}
    	}else{
    		
    		JSONObject json = null;
    		try {
    			json = br.getResJson().getJSONObject("data");
				String  sfjaSb = json.getString("sfja");
				if(null!=sfjaSb){
					sfja = Integer.parseInt(sfjaSb);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
            Intent intent = new Intent(this, CustomConfirmDialogActivity_.class);
            intent.putExtra("message", "添加成功");
            intent.putExtra("negative", "返回");
            intent.putExtra("positive", "查看");
            startActivityForResult(intent, Constants.REQUEST_CODE_ADDCASESUCESS);
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_CODE_ADDCASESUCESS) {
            if(resultCode == RESULT_OK) {
                Intent itt = new Intent();
                itt.setClass(this, AjxxActy_.class);
                if(sfja==Constants.AJ_LIST_SCOPE_CXM_ZB){
                	itt.putExtra("flag", Constants.AJ_LIST_SCOPE_CXM_ZB);//在办
                }else{
                	itt.putExtra("flag", Constants.AJ_LIST_SCOPE_YZM_LS);//历史
                }
                startActivity(itt);
            }
        }
    }
    
	TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (et_query_num.getText().toString() != null
					&& !et_query_num.getText().toString().equals("")) {
				clearButton.setVisibility(View.VISIBLE);
			} else {
				clearButton.setVisibility(View.INVISIBLE);
			}
		}
	};

    @UiThread
    public void showTips(String message) {
    }
}
