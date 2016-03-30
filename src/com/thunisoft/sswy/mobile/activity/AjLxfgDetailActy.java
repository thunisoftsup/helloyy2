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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.auth.LoginActivity_;
import com.thunisoft.sswy.mobile.activity.auth.LsrzActivity_;
import com.thunisoft.sswy.mobile.activity.auth.SmrzActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.RzfsDioagActivity;
import com.thunisoft.sswy.mobile.activity.dialog.RzfsDioagActivity_;
import com.thunisoft.sswy.mobile.adapter.CertTypeAdapter;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

@EActivity(R.layout.activity_lxfg_detail)
public class AjLxfgDetailActy extends Activity  {
    @Bean
    ResponseUtilExtr responseUtil;
    @Bean
    LoginCache loginCatch;
    @ViewById(R.id.all_hf_list)
    ListView allHfList;
    
    JSONArray resAllhfJSON;
    boolean isHasMoreData;
    private static final int MAX_ONEPAGE_SIZE = 20;
    private int pageIndex = 1;
    private AllHfAdapter aAllHfAdapter;
    LayoutInflater laytInf;
    String ajbh;
    String dqlyId;
    String ajAh, ajCbr;
    String lyCount, yhfLyCount;
    
    @Click(R.id.btn_back)
    public void back() {
        finish();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	LxfgEditActy.isCreatSucess = false;
    }
    
    @AfterViews
    public void setUpViews() {
        laytInf = LayoutInflater.from(this);
        ajbh = getIntent().getStringExtra("CBh");
        ajAh = getIntent().getStringExtra("CAH");
        if (ajbh == null || ajbh.equals("")) {
            Toast.makeText(this, "找不到对应的案件...", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        allHfList.setBackgroundColor(Color.parseColor("#ffffff"));
        aAllHfAdapter = new AllHfAdapter();
        allHfList.setAdapter(aAllHfAdapter);
        loadHfAllData();
    }
    
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (LxfgEditActy.isCreatSucess) {
    		resAllhfJSON = null;
    		pageIndex = 1;
    		loadHfAllData();
    	}
    }
    
    @Click(R.id.btn_blank)
    public void creatLxfgLy() {
    	if (!loginCatch.isLogined()) {
    		gotoLogin(R.id.btn_blank);
    		return;
        }
    	
    	if (!(loginCatch.isSmrz() || loginCatch.isLsrz())) {
    		rzfs(R.id.btn_blank);
    		Toast.makeText(this, "此功能需要认证才能使用!", Toast.LENGTH_LONG).show();
    		return;
    	}
        if (ajbh == null) {
            Toast.makeText(this, "数据未加载完成,无法创建留言...", Toast.LENGTH_LONG).show();
            return;
        }
        Intent itt = new Intent();
        itt.setClass(this, LxfgEditActy_.class);
        itt.putExtra("CBh", ajbh);
        startActivity(itt);
    }
    
    public void rzfs(int resId) {
        Intent intent = new Intent(this, RzfsDioagActivity_.class);
        intent.putExtra(RzfsDioagActivity.K_RZFS_CODE, resId);
        startActivityForResult(intent, Constants.REQUEST_CODE_XZZJ);
    }
    
    private void gotoLogin(int resId) {
        Intent itt = new Intent();
        itt.setClass(this, LoginActivity_.class);
        itt.putExtra("resId", resId);
        startActivityForResult(itt, Constants.REQUEST_CODE_LOGINSUCESS);
        Toast.makeText(this, "此功能需要登录才能使用!", Toast.LENGTH_LONG).show();
    }
    
    private void gotoSmrz(int resId) {
        Intent itt = new Intent();
        itt.putExtra("resId", resId);
        itt.setClass(this, SmrzActivity_.class);
        startActivityForResult(itt, Constants.REQUEST_CODE_SMRZ);
    }
    
    private void gotoLsrz(int resId) {
    	Intent itt = new Intent();
    	itt.putExtra("resId", resId);
    	itt.setClass(this, LsrzActivity_.class);
    	startActivityForResult(itt, Constants.REQUEST_CODE_LSRZ);
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
        case Constants.REQUEST_CODE_XZZJ:
            if (resultCode == Activity.RESULT_OK) {
                String zjlxId = data.getStringExtra(CertTypeAdapter.K_SELECTED_CODE);
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
        			findViewById(resId).performClick();
        		}
            }
        	break;
        case Constants.REQUEST_CODE_SMRZ:
        	if (resultCode == Activity.RESULT_OK) {
        		if (data == null) return;
        		int resId = data.getIntExtra("resId", 0);
        		if (resId != 0) {
        			findViewById(resId).performClick();
        		}
            }
        	break;
        case Constants.REQUEST_CODE_LSRZ:
        	if (resultCode == Activity.RESULT_OK) {
        		if (data == null) return;
        		int resId = data.getIntExtra("resId", 0);
        		if (resId != 0) {
        			findViewById(resId).performClick();
        		}
            }
        	break;
        }
    }
    
    
    @Background
    public void loadHfAllData() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ajbh", ajbh));
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        int currLxfg = ResponseUtilExtr.LOAD_Aj_LXFG;
        if (!loginCatch.isLogined()) {
        	currLxfg = ResponseUtilExtr.LOAD_Aj_LXFGPUB;
        }
        BaseResponseExtr br = responseUtil.getResponse(currLxfg, params);
        loadLyAllDataDone(br);
    }
    
    @UiThread
    public void loadLyAllDataDone(BaseResponseExtr br) {
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
            	lyCount = br.getResJson().getString("lyCount");
                yhfLyCount = br.getResJson().getString("yhfLyCount");
                int len = br.getResJson().getJSONArray("lxfgList").length();
                ajCbr = br.getResJson().getString("cbr");
                
                if (len == 0) {
                	Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
                }
                if (len == MAX_ONEPAGE_SIZE) {
                    isHasMoreData = true;
                } else {
                    isHasMoreData = false;
                }
                if (pageIndex == 1) {
                    JSONArray resAllhfJSON_ = br.getResJson().getJSONArray("lxfgList");
                    resAllhfJSON = new JSONArray();
                    resAllhfJSON.put(0, null);
                    int len_ = resAllhfJSON_.length();
                    for (int i=0; i<len_; i++) {
                        resAllhfJSON.put(resAllhfJSON.length(), resAllhfJSON_.getJSONObject(i));
                    }
                } else {
                    JSONArray dataList = br.getResJson().getJSONArray("lxfgList");
                    int lenDataList = dataList.length();
                    for (int i=0; i<lenDataList; i++) {
                        resAllhfJSON.put(resAllhfJSON.length(), dataList.getJSONObject(i));
                    }
                }
                aAllHfAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class AllHfAdapter extends BaseAdapter {

        @Override
        public int getCount() {
        	if (resAllhfJSON == null) {
        		return 0;
        	}
        	int len = resAllhfJSON.length();
        	return len+(isHasMoreData?1:0);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
            	convertView = laytInf.inflate(R.layout.lxfg_detail_list_item, null);
            	TextView tvlyCount = (TextView) convertView.findViewById(R.id.tv_lyCount);
            	TextView tvyhfLyCount = (TextView) convertView.findViewById(R.id.tv_yhfLyCount);
            	tvlyCount.setText(lyCount+"条留言");
            	convertView.findViewById(R.id.show_all_lyhf).setVisibility(View.INVISIBLE);
            	tvyhfLyCount.setText(yhfLyCount+"条回复");
            	TextView tvAJH = (TextView) convertView.findViewById(R.id.tv_AJH);
            	tvAJH.setText(ajAh);
            	TextView tvLydx = (TextView) convertView.findViewById(R.id.tv_Lydx);
            	tvLydx.setText(ajCbr);
            	convertView.findViewById(R.id.dqly_lyt).setVisibility(View.GONE);
                return convertView;
            }
            if (position == resAllhfJSON.length()) {
                convertView = laytInf.inflate(R.layout.loadmore_list_item, null);
                convertView.findViewById(R.id.btn_load_more).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pageIndex++;
                        loadHfAllData();
                    }
                });
                return convertView;
            } else {
                convertView = laytInf.inflate(R.layout.lxfg_alllist_item, null);
            }
            try {
                JSONObject aJSONObj = resAllhfJSON.getJSONObject(position);
                String CTitle = aJSONObj.getString("CTitle");
                String CContent = aJSONObj.getString("CContent");
                String DLyrq = aJSONObj.getString("DLyrq");
                String CLyrMc = aJSONObj.getString("CLyrMc");
                TextView tvCLyrMc = (TextView) convertView.findViewById(R.id.tv_CLyrMc);
                tvCLyrMc.setText(CLyrMc);
                TextView tvDLyrq = (TextView) convertView.findViewById(R.id.tv_DLyrq);
                tvDLyrq.setText(DLyrq);
                TextView tvCTitle = (TextView) convertView.findViewById(R.id.tv_CTitle);
                tvCTitle.setText(CTitle);
                TextView tvCContent = (TextView) convertView.findViewById(R.id.tv_CContent);
                tvCContent.setText(CContent);
                JSONArray hfxxList = aJSONObj.getJSONArray("hfxxList");
                int len = hfxxList.length();
                LinearLayout dqlyLyt = (LinearLayout) convertView.findViewById(R.id.dqly_lyt);
                if (len == 0) {
                    View noHflyt = laytInf.inflate(R.layout.lxfg_elemnt_hf_none, null);
                    dqlyLyt.addView(noHflyt);
                } else {
                    for (int i=0; i<len; i++) {
                        View aHflyt = laytInf.inflate(R.layout.lxfg_elemnt_hf, null);
                        dqlyLyt.addView(aHflyt);
                        JSONObject aHfxx = hfxxList.getJSONObject(i);
                        String CHfnr = aHfxx.getString("CHfnr");
                        TextView tvCHfnr = (TextView) aHflyt.findViewById(R.id.tv_CHfnr);
                        tvCHfnr.setText(CHfnr);
                        String CHfr = aHfxx.getString("CHfr");
                        TextView tvCHfr = (TextView) aHflyt.findViewById(R.id.tv_CHfr);
                        tvCHfr.setText(CHfr);
                        String DHfrq = aHfxx.getString("DHfrq");
                        TextView tvDHfrq = (TextView) aHflyt.findViewById(R.id.tv_DHfrq);
                        tvDHfrq.setText(DHfrq);
                        aHflyt.setTag(aHfxx.getString("CLxfgBh"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }

}
