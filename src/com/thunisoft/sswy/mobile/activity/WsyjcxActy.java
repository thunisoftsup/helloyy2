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

@EActivity(R.layout.activity_wsyjcx)
public class WsyjcxActy extends BaseActivity{
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.wsyj_list)
    PullToRefreshListView wsyjListView;
    WsyjAdapter aWsyjAdapter;
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
        wsyjListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                loadDatas();
            }
        });
        aWsyjAdapter = new WsyjAdapter();
        wsyjListView.setAdapter(aWsyjAdapter);
        setBtnBack();
        setTitle("网上阅卷查询");
        loadDatas();
    }
    
    @Background
    public void loadDatas() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_WSYJCX, params);
        loadDataDone(br);
    }

    @Background
    public void downloadFile(String CBh, String fjlx) {
        BaseResponseExtr br = responseUtil.getResponseDldFile(CBh, fjlx);
        loadDataDone(br);
    }
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        wsyjListView.onRefreshComplete();
        if (br.getMsg() != null) {
            if (br.getMsg().contains("hasdownload;")) {
                startActivity(ResponseUtilExtr.openFile(br.getMsg().split(";")[1]));
                return;
            }
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else if (br.getResJson().has("fileName")) {
            // 说明文件下载成功了...f
            try {
                startActivity(ResponseUtilExtr.openFile(br.getResJson().getString("fileName")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            aWsyjAdapter.notifyDataSetChanged();
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
                aWsyjAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class WsyjAdapter extends BaseAdapter {

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
                convertView = laytInf.inflate(R.layout.wsyjcx_list_item, null);
            }
            try {
                JSONObject aJSONObj = resJSON.getJSONObject(position);
                TextView tvCFyMc = (TextView)convertView.findViewById(R.id.tv_CFyMc);
                String aCFyMc = "";
                if (!aJSONObj.isNull("CFyMc")) {
                    aCFyMc = aJSONObj.getString("CFyMc");
                }
                tvCFyMc.setText(aCFyMc);
                
                String aCZt = aJSONObj.getString("CZt");
                TextView tvCZt = (TextView)convertView.findViewById(R.id.tv_CZt);
                if (aCZt.equals("待审核") || aCZt.equals("审核中")) {
                    //待审查
                    tvCZt.setBackgroundColor(Color.parseColor("#999999"));
                } else if (aCZt.equals("审核合格") || aCZt.equals("审核通过")) {
                    //审查合格
                    tvCZt.setBackgroundColor(Color.parseColor("#65a75f"));
                } else if (aCZt.equals("未提交") || aCZt.equals("审核不合格") || aCZt.equals("审核不通过")) {
                    //审查不合格
                    tvCZt.setBackgroundColor(Color.parseColor("#ea7a79"));
                } 
                tvCZt.setText(aCZt);
                
                TextView tvCAh = (TextView)convertView.findViewById(R.id.tv_CAh);
                String aCAh = "";
                if (!aJSONObj.isNull("CAh")) {
                    aCAh = aJSONObj.getString("CAh");
                }
                tvCAh.setText(aCAh);
                
                TextView tvDSqsj = (TextView)convertView.findViewById(R.id.tv_DSqsj);
                String aDSqsj = "";
                if (!aJSONObj.isNull("DSqsj")) {
                    aDSqsj = aJSONObj.getString("DSqsj");
                }
                tvDSqsj.setText(aDSqsj);
                
                TextView tvDUpdate = (TextView)convertView.findViewById(R.id.tv_DUPdate);
                String aDUpdate = "";
                if (!aJSONObj.isNull("DUpdate")) {
                    aDUpdate = aJSONObj.getString("DUpdate");
                }
                tvDUpdate.setText("更新:"+aDUpdate);
                
                JSONArray cljgList = aJSONObj.optJSONArray("cljgList");
                if (cljgList != null && cljgList.length() != 0) {
                    LinearLayout contentView = (LinearLayout) convertView.findViewById(R.id.wsyj_content_lyt);
                    int len = cljgList.length();
                    for (int i=0; i<len; i++) {
                        JSONObject acljgJSON = cljgList.getJSONObject(i);
                        View cljgListView = laytInf.inflate(R.layout.wsyj_elemnt_cljg, null);
                        contentView.addView(cljgListView);
                        TextView tvCShyj = (TextView)convertView.findViewById(R.id.tv_CShyj);
                        String aCShyj = "";
                        if (!acljgJSON.isNull("CShyj")) {
                            aCShyj = acljgJSON.getString("CShyj");
                        }
                        tvCShyj.setText(aCShyj);
                        
                        TextView tvCShrMc = (TextView)convertView.findViewById(R.id.tv_CShrMc);
                        String aCShrMc = "";
                        if (!acljgJSON.isNull("CShrMc")) {
                            aCShrMc = acljgJSON.getString("CShrMc");
                        }
                        tvCShrMc.setText(aCShrMc);
                    }
                }
                
                //JSONArray clList = aJSONObj.optJSONArray("clList");
                String hasJz = aJSONObj.optString("hasJz");
                if (hasJz != null && !hasJz.equals("")) {
                	LinearLayout contentView = (LinearLayout) convertView.findViewById(R.id.wsyj_content_lyt);
                	final View clListView = laytInf.inflate(R.layout.wsyj_elemnt_cl, null);
                	contentView.addView(clListView);
                	TextView tvCWjmc = (TextView)convertView.findViewById(R.id.tv_CWjmc);
                	String DYjyxq = aJSONObj.optString("DYjyxq");
                	String isInvalid = aJSONObj.optString("isInvalid");
                	if (isInvalid == null || !isInvalid.equals("YES")) {
                		tvCWjmc.setText("查看卷宗\n查看期限："+DYjyxq);
                		clListView.setTag(aJSONObj.getString("CBh")+";"+hasJz);
                		clListView.findViewById(R.id.wsyjcl_content).setOnClickListener(new OnClickListener() {
                			@Override
                			public void onClick(View v_) {
                				clListView.setBackgroundColor(Color.parseColor("#efefef"));
                				String strs[] = clListView.getTag().toString().split(";");
                				downloadFile(strs[0], strs[1]);
                			}
                		});
                	} else {
                		tvCWjmc.setSingleLine(false);
                		tvCWjmc.setText("您已经超过阅卷期限，请重新申请\n查看期限："+DYjyxq);
                		convertView.findViewById(R.id.indicator).setVisibility(View.GONE);
                	}
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }
}
