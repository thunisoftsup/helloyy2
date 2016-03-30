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
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

@EActivity(R.layout.activity_zxxsjb)
public class ZxxsjbActy extends BaseActivity{
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.zxxsjb_list)
    PullToRefreshListView zxxsjbListView;
    zxxsjbAdapter aZxxsjbAdapter;
    JSONArray resJSON;
    LayoutInflater laytInf;
    boolean isHasMoreData;
    private static final int MAX_ONEPAGE_SIZE = 10;
    private int pageIndex;
    
    @AfterViews
    public void initViews() {
        isHasMoreData = true;
        pageIndex = 1;
        laytInf = LayoutInflater.from(this);
        zxxsjbListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                loadDatas();
            }
        });
        aZxxsjbAdapter = new zxxsjbAdapter();
        zxxsjbListView.setAdapter(aZxxsjbAdapter);
        setBtnBack();
        setTitle("执行举报查询");
        loadDatas();
    }
    
    @Background
    public void loadDatas() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_ZXXSJB, params);
        loadDataDone(br);
    }
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        zxxsjbListView.onRefreshComplete();
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
                int len = br.getResJson().getJSONArray("dataList").length();
                if (len == 0 && pageIndex > 1) {
                    Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
                }
                if (len == MAX_ONEPAGE_SIZE) {
                    isHasMoreData = true;
                } else {
                    isHasMoreData = false;
                }
                if (pageIndex == 1) {
                    resJSON = br.getResJson().getJSONArray("dataList");
                    if (resJSON.length() == 0) {
                        resJSON.put(JSONObject.NULL);
                    }
                } else {
                    JSONArray dataList = br.getResJson().getJSONArray("dataList");
                    int lenDataList = dataList.length();
                    for (int i=0; i<lenDataList; i++) {
                        resJSON.put(resJSON.length(), dataList.getJSONObject(i));
                    }
                }
                aZxxsjbAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class zxxsjbAdapter extends BaseAdapter {

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
                try {
                    if (resJSON.length() == 1 && resJSON.get(0).equals(JSONObject.NULL)) {
                        convertView = laytInf.inflate(R.layout.nodata_element, null);
                        return convertView;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                convertView = laytInf.inflate(R.layout.zxxsjb_list_item, null);
            }
            try {
                JSONObject aJSONObj = resJSON.getJSONObject(position);
                TextView tvCJbfyName = (TextView)convertView.findViewById(R.id.tv_CJbfyName);
                String aCJbfyName = "";
                if (!aJSONObj.isNull("CJbfyName")) {
                    aCJbfyName = aJSONObj.getString("CJbfyName");
                }
                tvCJbfyName.setText(aCJbfyName);
                
                String aC_Zt = aJSONObj.getString("C_Zt");
                TextView tvC_Zt = (TextView)convertView.findViewById(R.id.tv_C_Zt);
                if (aC_Zt.equals("办理中")) {
                    //待审查
                    tvC_Zt.setBackgroundColor(Color.parseColor("#999999"));
                } else if (aC_Zt.equals("已反馈")) {
                    //审查合格
                    tvC_Zt.setBackgroundColor(Color.parseColor("#65a75f"));
                } else if (aC_Zt.equals("未提交")) {
                    //审查不合格
                    tvC_Zt.setBackgroundColor(Color.parseColor("#ea7a79"));
                } 
                tvC_Zt.setText(aC_Zt);
                
                TextView tvCBzxrMc = (TextView)convertView.findViewById(R.id.tv_CBzxrMc);
                String aCBzxrMc = "";
                if (!aJSONObj.isNull("CBzxrMc")) {
                    aCBzxrMc = aJSONObj.getString("CBzxrMc");
                }
                tvCBzxrMc.setText(aCBzxrMc);
                
                TextView tvDTjsj = (TextView)convertView.findViewById(R.id.tv_DTjsj);
                String aDTjsj = "";
                if (!aJSONObj.isNull("DTjsj")) {
                    aDTjsj = aJSONObj.getString("DTjsj");
                }
                tvDTjsj.setText(aDTjsj);
                
                TextView tvDUpdate = (TextView)convertView.findViewById(R.id.tv_DUPdate);
                String aDUpdate = "";
                if (!aJSONObj.isNull("DUpdate")) {
                    aDUpdate = aJSONObj.getString("DUpdate");
                }
                tvDUpdate.setText("更新:"+aDUpdate);
                
                JSONArray xsList = aJSONObj.optJSONArray("xsList");
                StringBuilder xsListSb = new StringBuilder();
                if (xsList != null) {
                    int len = xsList.length();
                    for (int i=0; i<len; i++) {
                        JSONObject aJs = xsList.getJSONObject(i);
                        String CBz = aJs.getString("CBz");
                        if (!CBz.equals("null")) {
                            xsListSb.append(aJs.getString("CBz")+",");
                        }
                    }
                    if (xsListSb.length() != 0) {
                        xsListSb.delete(xsListSb.length()-1, xsListSb.length());
                        xsListSb.append("举报");
                    } else {
                        xsListSb.append("无");
                    }
                } else {
                    xsListSb.append("无");
                }
                TextView tvXsList = (TextView)convertView.findViewById(R.id.tv_xsList);
                tvXsList.setText(xsListSb.toString());
                
                JSONArray cljgList = aJSONObj.optJSONArray("cljgList");
                StringBuilder cljgListSb = new StringBuilder();
                if (cljgList != null && cljgList.length() != 0) {
                    int len = cljgList.length();
                    for (int i=0; i<len; i++) {
                        JSONObject aJs = cljgList.getJSONObject(i);
                        cljgListSb.append(aJs.get("CFknr")+"\n");
                    }
                    cljgListSb.delete(cljgListSb.length()-1, cljgListSb.length());
                } else {
                    cljgListSb.append("无");
                    convertView.findViewById(R.id.zxxsjb_fklyt).setVisibility(View.GONE);
                }
                TextView tvcljgList = (TextView)convertView.findViewById(R.id.tv_cljgList);
                tvcljgList.setText(cljgListSb.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }
}
