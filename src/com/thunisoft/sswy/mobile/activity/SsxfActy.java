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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

@EActivity(R.layout.activity_ssxf)
public class SsxfActy extends BaseActivity{
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.ssxf_list)
    PullToRefreshListView ssxfListView;
    Ssxfdapter aSsxfAdapter;
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
        ssxfListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                loadDatas();
            }
        });
        aSsxfAdapter = new Ssxfdapter();
        ssxfListView.setAdapter(aSsxfAdapter);
        setBtnBack();
        setTitle("申诉信访查询");
        loadDatas();
    }
    
    @Background
    public void loadDatas() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_SSXFCX, params);
        loadDataDone(br);
    }
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        ssxfListView.onRefreshComplete();
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
                aSsxfAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class Ssxfdapter extends BaseAdapter {

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
                convertView = laytInf.inflate(R.layout.ssxf_list_item, null);
            }
            try {
                JSONObject aJSONObj = resJSON.getJSONObject(position);
                TextView tvCCourtName = (TextView)convertView.findViewById(R.id.tv_CCourtName);
                String aCCourtName = "";
                if (!aJSONObj.isNull("CCourtName")) {
                    aCCourtName = aJSONObj.getString("CCourtName");
                }
                tvCCourtName.setText(aCCourtName);
                
                String aC_Status = aJSONObj.getString("C_Status");
                
                TextView tvC_Status = (TextView)convertView.findViewById(R.id.tv_C_Status);
                if (aC_Status.equals("办理中")) {
                    //待审查
                    tvC_Status.setBackgroundColor(Color.parseColor("#999999"));
                } else if (aC_Status.equals("已反馈")) {
                    //审查合格
                    tvC_Status.setBackgroundColor(Color.parseColor("#ea7a79"));
                } else if (aC_Status.equals("未提交") || aC_Status.equals("待提交")) {
                    //审查不合格
                    tvC_Status.setBackgroundColor(Color.parseColor("#65a75f"));
                } 
                tvC_Status.setText(aC_Status);
                
                
                TextView tvCAh = (TextView)convertView.findViewById(R.id.tv_CAh);
                String aCAh = "";
                if (!aJSONObj.isNull("CAh")) {
                    aCAh = aJSONObj.getString("CAh");
                }
                tvCAh.setText(aCAh);
                
                TextView tvDXfrq = (TextView)convertView.findViewById(R.id.tv_DXfrq);
                String aDXfrq = "";
                if (!aJSONObj.isNull("DXfrq")) {
                    aDXfrq = aJSONObj.getString("DXfrq");
                }
                tvDXfrq.setText(aDXfrq);
                
                TextView tvDUpdate = (TextView)convertView.findViewById(R.id.tv_DUPdate);
                String aDUpdate = "";
                if (!aJSONObj.isNull("DUpdate")) {
                    aDUpdate = aJSONObj.getString("DUpdate");
                }
                tvDUpdate.setText("更新:"+aDUpdate);
                
                TextView tvCSsxxSyMs = (TextView)convertView.findViewById(R.id.tv_CSsxxSyMs);
                String aCSsxxSyMs = "";
                if (!aJSONObj.isNull("CSsxxSyMs")) {
                    aCSsxxSyMs = aJSONObj.getString("CSsxxSyMs");
                }
                tvCSsxxSyMs.setText(aCSsxxSyMs);
                // 动态加入布局
                JSONArray cljgList = aJSONObj.optJSONArray("cljgList");
                if (cljgList != null && cljgList.length() != 0) {
                    LinearLayout contentView = (LinearLayout) convertView.findViewById(R.id.ssxf_content_lyt);
                    int len = cljgList.length();
                    for (int i=0; i<len; i++) {
                        JSONObject acljgJSON = cljgList.getJSONObject(i);
                        View cljgListView = laytInf.inflate(R.layout.ssxf_elemnt_cljg, null);
                        contentView.addView(cljgListView);
                        TextView tvCNbyj = (TextView)convertView.findViewById(R.id.tv_CNbyj);
                        String aCNbyj = "";
                        if (!acljgJSON.isNull("CNbyj")) {
                            aCNbyj = acljgJSON.getString("CNbyj");
                        }
                        tvCNbyj.setText(aCNbyj);
                        
                        TextView tvCHfnr = (TextView)convertView.findViewById(R.id.tv_CHfnr);
                        String aCHfnr = "";
                        if (!acljgJSON.isNull("CHfnr")) {
                            aCHfnr = acljgJSON.getString("CHfnr");
                        }
                        tvCHfnr.setText(aCHfnr);
                        
                        TextView tvCBlrMc = (TextView)convertView.findViewById(R.id.tv_CBlrMc);
                        String aCBlrMc = "";
                        if (!acljgJSON.isNull("CBlrMc")) {
                            aCBlrMc = acljgJSON.getString("CBlrMc");
                        }
                        tvCBlrMc.setText(aCBlrMc);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }
}
