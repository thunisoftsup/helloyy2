package com.thunisoft.sswy.mobile.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.pojo.JsonBean;
import com.thunisoft.sswy.mobile.util.StringUtils;

@EActivity(R.layout.activity_ajxx_spf_detail)
public class AjxxSpfDetailActy extends BaseActivity {
	private static final Pattern patt = Pattern.compile("<a.*>(.*)</a>");
    @Bean
    ResponseUtilExtr responseUtil;
    JSONArray resAllhfJSON;
    LayoutInflater laytInf;
    String ajbh;
    String tablename;
    String sfzjhm;
    
    @Click(R.id.btn_back)
    public void back() {
        finish();
    }
    
    @AfterViews
    public void setUpViews() {
        laytInf = LayoutInflater.from(this);
        ajbh = getIntent().getStringExtra("ajbh");
        sfzjhm = getIntent().getStringExtra("sfzjhm");
        tablename = getIntent().getStringExtra("tablename");
        if (ajbh == null || ajbh.equals("")) {
            Toast.makeText(this, "找不到对应的案件...", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        loadAjData();
    }
    
    
    @Background
    public void loadAjData() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ajbh", ajbh));
        params.add(new BasicNameValuePair("tablename", tablename));
        int flag;
        if (sfzjhm == null) {
            flag = ResponseUtilExtr.LOAD_AjXX_SPF_DETAIL_PRO;
        } else {
            flag = ResponseUtilExtr.LOAD_AjXX_SPF_DETAIL_PUB;
        }
        BaseResponseExtr br = responseUtil.getResponse(flag, params);
        loadAjDataDone(br);
    }
    
    private View dealAblockUi(JSONArray ablockContent, String title) throws JSONException {
        int len = ablockContent.length();
        View ajxxSpfLyt = laytInf.inflate(R.layout.ajxx_spfdetail_item, null);
        TextView tvTitle = (TextView)ajxxSpfLyt.findViewById(R.id.tv_ajspf_title);
        if (title.equals("")) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        LinearLayout ajxxSpfContentLyt = (LinearLayout)ajxxSpfLyt.findViewById(R.id.ajxx_spf_dcontent);
        for (int i=0; i<len; i++) {
            JSONObject ablockItem = ablockContent.getJSONObject(i);
            String label = ablockItem.getString("label");
            String value = ablockItem.getString("value");
            
            
            if (label.equals("案号")) {
            	Matcher mch = patt.matcher(value);
            	if (mch.find()) {
            		value = mch.group(1);
            	}
            }
            View akeyLyt = laytInf.inflate(R.layout.ajxx_ajgy_item, null);
            TextView tvlabel = (TextView)akeyLyt.findViewById(R.id.label);
            TextView tvvalue = (TextView)akeyLyt.findViewById(R.id.value);
            tvlabel.setText(label);
            tvvalue.setText(value);
            if (i == 0 && len != 1) {
                tvlabel.setBackgroundResource(R.drawable.corner_style_lxfg_top);
            } else if (i == len-1) {
                if (i == 0) {
                    tvlabel.setBackgroundResource(R.drawable.corner_style_lxfg_all);
                    akeyLyt.findViewById(R.id.dividor).setVisibility(View.GONE);
                } else {
                    tvlabel.setBackgroundResource(R.drawable.corner_style_lxfg_bottom);
                    akeyLyt.findViewById(R.id.dividor).setVisibility(View.GONE);
                }
            } else {
                tvlabel.setBackgroundColor(Color.parseColor("#f2f2f2"));
            }
            ajxxSpfContentLyt.addView(akeyLyt);
        }
        
        return ajxxSpfLyt;
    }
    
    @UiThread
    public void loadAjDataDone(BaseResponseExtr br) {
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
                LinearLayout ajxxSpfLyt = (LinearLayout) findViewById(R.id.ajxx_spf_lyt);
                String navMainStr = br.getResJson().getString("navMain");
                JSONObject navMain = new JSONObject(navMainStr);
                String detail_title = navMain.getString("detail_title");
                setTitle(detail_title);
                String blockArrStr = navMain.getString("blockArr");
                JSONArray blockArr = new JSONArray(blockArrStr);
                int len = blockArr.length();
                List<JsonBean> jsonBeanList = new ArrayList<JsonBean>();
                for (int i=0; i<len; i++) {
                    JSONObject ablockGrp = blockArr.getJSONObject(i);
                    JSONArray ablockContent = ablockGrp.getJSONArray("dataArr");
                    String title = ablockGrp.optString("title");
                    if (title == null || title.equals("null")) title = "";                   
                    JsonBean jsonBean = new JsonBean();
                    jsonBean.setAblockContent(ablockContent);
                    jsonBean.setTitle(title);
                    if(StringUtils.isNotBlank(title)&&title.equals("当事人基本信息")){
                    	jsonBeanList.add(0, jsonBean);
                    }else{
                    	jsonBeanList.add(jsonBean);
                    }
                    
                    if (ablockContent.length() == 0) {
                        continue;
                    }
                }
                
                for (int i = 0; i < jsonBeanList.size(); i++) {
                	View aBlockView = dealAblockUi(jsonBeanList.get(i).getAblockContent(),jsonBeanList.get(i).getTitle());
                    ajxxSpfLyt.addView(aBlockView);
				}
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
}
