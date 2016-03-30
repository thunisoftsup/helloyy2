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

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

@EActivity(R.layout.activity_lyaj)
public class CxAjActy extends BaseActivity{
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.lyaj_list)
    PullToRefreshListView lyajListView;
    @ViewById(R.id.txtSearch)
    EditText txtSearch;
    @ViewById(R.id.search_text_del)
    View txtSearchDel;
    
    LyajAdapter aLyajAdapter;
    JSONArray resJSON;
    LayoutInflater laytInf;
    boolean isHasMoreData;
    private static final int MAX_ONEPAGE_SIZE = 20;
    private int pageIndex;
    String searchValue;
    
    @AfterViews
    public void initViews() {
        isHasMoreData = true;
        pageIndex = 1;
        laytInf = LayoutInflater.from(this);
        lyajListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                loadDatas();
            }
        });
        lyajListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (view.getTag() == null) return;
                String CBh = view.getTag().toString();
                // 跳转到 创建联系法官页面。。。
                Intent itt = new Intent();
                itt.setClass(CxAjActy.this, LxfgEditActy_.class);
                itt.putExtra("CBh", CBh);
                startActivity(itt);
                finish();
            }
        });
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    txtSearchDel.setVisibility(View.GONE);
                } else {
                    txtSearchDel.setVisibility(View.VISIBLE);
                }
            }
        });
        txtSearchDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearch.setText("");
                searchValue = "";
                loadDatas();
            }
        });
        aLyajAdapter = new LyajAdapter();
        lyajListView.setAdapter(aLyajAdapter);
        setBtnBack();
        setTitle("选择留言案件");
        loadDatas();
    }
    
    @Click(R.id.search_commt)
    public void searchCommt() {
        searchValue = txtSearch.getText().toString();
        if (searchValue == null || searchValue.equals("")) {
            Toast.makeText(this, "请先输入搜索条件...", Toast.LENGTH_LONG).show();
            return;
        }
        loadDatas();
    }
    
    @Background
    public void loadDatas() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        if (searchValue != null && !searchValue.equals("")) {
            params.add(new BasicNameValuePair("searchValue", searchValue));
        }
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_LYAJ, params);
        loadDataDone(br);
    }
    
    @Background
    public void loadSearchDatas() {
        pageIndex = 1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_LYAJ, params);
        loadDataDone(br);
    }
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        lyajListView.onRefreshComplete();
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
                int len = br.getResJson().getJSONArray("ajList").length();
                if (len == 0) {
                    Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
                }
                if (len == MAX_ONEPAGE_SIZE) {
                    isHasMoreData = true;
                } else {
                    isHasMoreData = false;
                }
                if (pageIndex == 1) {
                    resJSON = br.getResJson().getJSONArray("ajList");
                } else {
                    JSONArray dataList = br.getResJson().getJSONArray("ajList");
                    int lenDataList = dataList.length();
                    for (int i=0; i<lenDataList; i++) {
                        resJSON.put(resJSON.length(), dataList.getJSONObject(i));
                    }
                }
                aLyajAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class LyajAdapter extends BaseAdapter {

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
                convertView = laytInf.inflate(R.layout.lyaj_list_item, null);
            }
            try {
                JSONObject aJSONObj = resJSON.getJSONObject(position);
                TextView tvCAjzt_mc = (TextView)convertView.findViewById(R.id.tv_CAjzt_mc);
                String aCAjzt_mc = "";
                aCAjzt_mc = "["+aJSONObj.getString("CAjzt")+"]";
                aCAjzt_mc += aJSONObj.getString("CAjmc");
                tvCAjzt_mc.setText(aCAjzt_mc);
                
                String aCCbr = aJSONObj.getString("CCbr");
                TextView tvCCbr = (TextView)convertView.findViewById(R.id.tv_CCbr);
                tvCCbr.setText(aCCbr);
                convertView.setTag(aJSONObj.getString("CBh"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }
}
