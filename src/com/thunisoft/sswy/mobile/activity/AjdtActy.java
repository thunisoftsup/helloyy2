package com.thunisoft.sswy.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

/**
 * 案件动态界面
 * 
 */

@EActivity(R.layout.activity_ajdt)
public class AjdtActy extends BaseActivity{
    @Bean
    ResponseUtilExtr responseUtil;
    @Bean
    LoginCache loginc;
    @ViewById(R.id.ajdt_list)
    PullToRefreshListView ajdtListView;
    AjdtAdapter aAjdtAdapter;
    JSONArray resJSON;
    LayoutInflater laytInf;
    boolean isHasMoreData;
    private static final int MAX_ONEPAGE_SIZE = 20;
    private int pageIndex;
    String ajbh;
    String zjhm;
    
    @AfterViews
    public void initViews() {
        ajbh = getIntent().getStringExtra("ajbh");
        zjhm = getIntent().getStringExtra("zjhm");
        isHasMoreData = true;
        pageIndex = 1;
        laytInf = LayoutInflater.from(this);
        ajdtListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                loadDatas();
            }
        });
        aAjdtAdapter = new AjdtAdapter();
        ajdtListView.setAdapter(aAjdtAdapter);
        setBtnBack();
        setTitle("案件动态");
        loadDatas();
    }
    
    @Background
    public void loadDatas() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("ajbh", ajbh));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        int curDjdt = ResponseUtilExtr.LOAD_AJDT;
        if (!loginc.isLogined()) {
        	curDjdt = ResponseUtilExtr.LOAD_AJDTPUB;
        	params.add(new BasicNameValuePair("zjhm", zjhm));
        }
        BaseResponseExtr br = responseUtil.getResponse(curDjdt, params);
        loadDataDone(br);
    }
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        ajdtListView.onRefreshComplete();
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
                int len = br.getResJson().getJSONArray("ajdtList").length();
                if (len == 0) {
                    Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
                }
                if (len == MAX_ONEPAGE_SIZE) {
                    isHasMoreData = true;
                } else {
                    isHasMoreData = false;
                }
                if (pageIndex == 1) {
                    resJSON = br.getResJson().getJSONArray("ajdtList");
                } else {
                    JSONArray dataList = br.getResJson().getJSONArray("ajdtList");
                    int lenDataList = dataList.length();
                    for (int i=0; i<lenDataList; i++) {
                        resJSON.put(resJSON.length(), dataList.getJSONObject(i));
                    }
                }
                aAjdtAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class AjdtAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (resJSON == null) {
                return 0;
            }
            int len = resJSON.length();
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
            if (position == resJSON.length()) {
                convertView = laytInf.inflate(R.layout.loadmore_list_item, null);
                convertView.findViewById(R.id.btn_load_more).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pageIndex++;
                        loadDatas();
                    }
                });
                return convertView;
            } else {
                convertView = laytInf.inflate(R.layout.ajdt_list_item, null);
            }
            try {
                JSONObject aJSONObj = resJSON.getJSONObject(position);
                String aCAjzt = aJSONObj.getString("CAjzt");
                TextView tvCAjzt = (TextView)convertView.findViewById(R.id.tv_CAjzt);
                tvCAjzt.setText(aCAjzt);
                
                TextView tvDCreate = (TextView)convertView.findViewById(R.id.tv_DCreate);
                String aDCreate = "";
                if (!aJSONObj.isNull("DCreate")) {
                    aDCreate = aJSONObj.getString("DCreate");
                }
                tvDCreate.setText(aDCreate);
                
                TextView tvCTemplateMc = (TextView)convertView.findViewById(R.id.tv_CTemplateMc);
                String aCTemplateMc = "";
                if (!aJSONObj.isNull("CTemplateMc")) {
                    aCTemplateMc = aJSONObj.getString("CTemplateMc");
                }
                tvCTemplateMc.setText(aCTemplateMc);
                
                TextView tvCXxnr = (TextView)convertView.findViewById(R.id.tv_CXxnr);
                String aCXxnr = "";
                if (!aJSONObj.isNull("CXxnr")) {
                    aCXxnr = aJSONObj.getString("CXxnr");
                }
                tvCXxnr.setText("更新:"+aCXxnr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }
}
