package com.thunisoft.sswy.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.auth.LoginActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

@EActivity(R.layout.activity_ajxx_detail)
public class AjxxDetailActy extends BaseActivity{
	@Bean
	LoginCache loginCache;
	@Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.ajxx_ajxq_lyt)
    LinearLayout ajxxAjxqLyt;
    @ViewById(R.id.ajxx_ajgy_lyt)
    LinearLayout ajxxAjgyLyt;
    @ViewById(R.id.tv_AjdtDUpdate)
    TextView tvAjdtDUpdate;
    @ViewById(R.id.goajdt)
    View ajdtLyt;
    @ViewById(R.id.tv_ly)
    TextView tvLy;
    @ViewById(R.id.tv_ly_udp)
    TextView tvLyUdp;
    @ViewById(R.id.tv_hf)
    TextView tvHf;
    @ViewById(R.id.tv_hf_udp)
    TextView tvHfUdp;
    @ViewById(R.id.img_AjdtDUpdate)
    ImageView imgAjdtDUpdate;
    @ViewById(R.id.lyt_detail_content)
    View lytLoadone;
    @ViewById(R.id.lyt_aj_loading)
    View lytLoading;
    LayoutInflater laytInf;
    String ajbh;
    String sfzjhm;
    String cbr;
    String cah;
    int lyCount;
    int cxfs;
    String tempSid;
    
    @Click(R.id.btn_back)
    public void back() {
        finish();
    }
    
    private void gotoLogin() {
        Intent itt = new Intent();
        itt.setClass(this, LoginActivity_.class);
        startActivityForResult(itt, Constants.REQUEST_CODE_LOGINSUCESS);
    }
    
    @Click(R.id.gotoLy)
    public void gotoLyfg() {
        Intent itt = new Intent();
        if (lyCount == 0) {
        	if (!loginCache.isLogined()) {
        		Toast.makeText(this,"此功能需要登录才能使用!", Toast.LENGTH_LONG).show();
        		gotoLogin();
        		return;
        	}
        	itt.setClass(AjxxDetailActy.this, LxfgEditActy_.class);
        	itt.putExtra("CBh", ajbh);
        	startActivity(itt);
        } else {
        	itt.setClass(AjxxDetailActy.this, AjLxfgDetailActy_.class);
        	itt.putExtra("CBh", ajbh);
        	itt.putExtra("CBR", cbr);
            itt.putExtra("CAH", cah);
        	startActivity(itt);
        }
    }

    @Click(R.id.goajdt)
    public void goajdt() {
        Intent itt = new Intent();
        itt.setClass(AjxxDetailActy.this, AjdtActy_.class);
        itt.putExtra("ajbh", ajbh);
        itt.putExtra("zjhm", sfzjhm);
        startActivity(itt);
    }
    
    @AfterViews
    public void setUpViews() {
    	LxfgEditActy.isCreatSucess = false;
        laytInf = LayoutInflater.from(this);
        ajbh = getIntent().getStringExtra("CBh");
        cxfs = getIntent().getIntExtra("cxfs", 1);
        tempSid = getIntent().getStringExtra("tempSid");
        sfzjhm = getIntent().getStringExtra("sfzjhm");
        if (ajbh == null || ajbh.equals("")) {
            Toast.makeText(this, "找不到对应的案件...", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        loadAjData(ajbh);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (LxfgEditActy.isCreatSucess) {
    		loadAjData(ajbh);
    	}
    }
    
    @Background
    public void loadAjData(String CBh) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ajbh", CBh));
        params.add(new BasicNameValuePair("appVersoin", getVersion(this)));
        params.add(new BasicNameValuePair("appInfo", "Android"));
        params.add(new BasicNameValuePair("tempSid", tempSid));
        int flag;
        if (sfzjhm == null) {
            flag = ResponseUtilExtr.LOAD_AjXX_DETAIL_PRO;
        } else {
            flag = ResponseUtilExtr.LOAD_AjXX_DETAIL_PUB;
            params.add(new BasicNameValuePair("sfzjhm", sfzjhm));
        }
        BaseResponseExtr br = responseUtil.getResponse(flag, params);
        loadAjDataDone(br);
    }
    
    //获取版本号  
    public static String getVersion(Context context) { 
        try {  
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
            return pi.versionName;  
        } catch (NameNotFoundException e) {  
            return null;  
        }  
    }  
    
    @UiThread
    public void loadAjDataDone(BaseResponseExtr br) {
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            lytLoading.setVisibility(View.GONE);
            lytLoadone.setVisibility(View.VISIBLE);
            try {
                boolean AjdtUpdated = br.getResJson().getBoolean("AjdtUpdated");
                if (AjdtUpdated) {
                    tvAjdtDUpdate.setText("更新："+br.getResJson().getString("AjdtDUpdate"));
                    imgAjdtDUpdate.setImageResource(R.drawable.arrow_right_mark);
                } else {
                    ajdtLyt.setVisibility(View.GONE);
                }
                boolean showLxfg = false;
                if(br.getResJson().has("showLxfg")) {
                    showLxfg = br.getResJson().getBoolean("showLxfg");
                }
                showLxfg = false;//吉林电子法院屏蔽联系法官
                if(!showLxfg) {
                    findViewById(R.id.layout_lxfg).setVisibility(View.GONE);
                }
                
//                String DHfrq = br.getResJson().getString("DHfrq");
//                String DLyrq = br.getResJson().getString("DLyrq");
//                if (!DHfrq.equals("")) {
//                    DHfrq = "最新：" + DHfrq;
//                } else {
//                    DHfrq = "最新：无";
//                }
//                if (!DLyrq.equals("")) {
//                    DLyrq = "最新：" + DLyrq;
//                } else {
//                    DLyrq = "最新：无";
//                }
//                lyCount = Integer.parseInt(br.getResJson().getString("lyCount"));
//                TextView lxfgtip = (TextView)findViewById(R.id.lxfgtip);
//                if (lyCount == 0) {
//                	lxfgtip.setText("留言");
//                } else {
//                	lxfgtip.setText("查看");
//                }
//                tvLy.setText("留言："+br.getResJson().getString("lyCount"));
//                tvHf.setText("回复："+br.getResJson().getString("hfCount"));
//                tvLyUdp.setText(DLyrq);
//                tvHfUdp.setText(DHfrq);
                
                String navMainStr = br.getResJson().getString("navMain");
                JSONObject navMain = new JSONObject(navMainStr);
                JSONArray ajgy = navMain.getJSONArray("ajgy");
                int len = ajgy.length();
                ajxxAjgyLyt.removeAllViews();
                for (int i=0; i<len; i++) {
                    JSONObject akey = ajgy.getJSONObject(i);
                    String label = akey.getString("label");
                    String value = akey.getString("value");
                    if (label.equals("CBR")) {
                    	cbr = value;
                    	continue;
                    } else if (label.equals("案号")) {
                    	cah = value;
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
                    ajxxAjgyLyt.addView(akeyLyt);
                }
                
                JSONArray ajxq = navMain.getJSONArray("ajxq");
                len = ajxq.length();
                ajxxAjxqLyt.removeAllViews();
                for (int i=0; i<len; i++) {
                    JSONObject akey = ajxq.getJSONObject(i);
                    String value = akey.getString("value");
                    String tag = akey.getString("data-tablename");
                    View akeyLyt = laytInf.inflate(R.layout.ajxx_ajxq_item, null);
                    akeyLyt.setTag(tag);
                    TextView tvlabel = (TextView)akeyLyt.findViewById(R.id.value);
                    tvlabel.setText(value);
                    ajxxAjxqLyt.addView(akeyLyt);
                    if (i == len - 1) {
                        akeyLyt.findViewById(R.id.dividor).setVisibility(View.GONE);
                    }
                    akeyLyt.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.getTag() == null) return;
                            String tablename = view.getTag().toString();
                            // 跳转到 创建联系法官页面。。。
                            Intent itt = new Intent();
                            itt.setClass(AjxxDetailActy.this, AjxxSpfDetailActy_.class);
                            itt.putExtra("ajbh", ajbh);
                            itt.putExtra("tablename", tablename);
                            itt.putExtra("sfzjhm", sfzjhm);
                            startActivity(itt);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == Constants.REQUEST_CODE_LOGINSUCESS && resultCode == Activity.RESULT_OK && data != null) {
    		Intent itt = new Intent();
            if (lyCount == 0) {
            	itt.setClass(AjxxDetailActy.this, LxfgEditActy_.class);
            	itt.putExtra("CBh", ajbh);
            	startActivity(itt);
            } else {
            	itt.setClass(AjxxDetailActy.this, AjLxfgDetailActy_.class);
            	itt.putExtra("CBh", ajbh);
            	itt.putExtra("CBR", cbr);
                itt.putExtra("CAH", cah);
            	startActivity(itt);
            }
    	}
    }
    
}
