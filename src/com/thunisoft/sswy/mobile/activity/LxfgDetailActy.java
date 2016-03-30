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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

@EActivity(R.layout.activity_lxfg_detail)
public class LxfgDetailActy extends Activity  {
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.all_hf_list)
    ListView allHfList;
    
    boolean isAllHuly;
    JSONArray resAllhfJSON;
    boolean isHasMoreData;
    private static final int MAX_ONEPAGE_SIZE = 20;
    private int pageIndex;
    private AllHfAdapter aAllHfAdapter;
    LayoutInflater laytInf;
    String lybh;
    String ajbh;
    String dqlyId;
    BaseResponseExtr br;
    
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
        isAllHuly = false;
        lybh = getIntent().getStringExtra("lybh");
        if (lybh == null || lybh.equals("")) {
            Toast.makeText(this, "找不到对应的留言...", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        loadLyDqData(lybh);
    }
    
    @Click(R.id.btn_blank)
    public void creatLxfgLy() {
        if (ajbh == null) {
            Toast.makeText(this, "数据未加载完成,无法创建留言...", Toast.LENGTH_LONG).show();
            return;
        }
        Intent itt = new Intent();
        itt.setClass(this, LxfgEditActy_.class);
        itt.putExtra("CBh", ajbh);
        startActivity(itt);
    }
    
    @Background
    public void loadLyDqData(String CBh) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", CBh));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_LXFG_DETAIL, params);
        loadLyDqDataDone(br);
    }
    
    @UiThread
    public void loadLyDqDataDone(BaseResponseExtr br) {
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            this.br = br;
            resAllhfJSON = new JSONArray();
            resAllhfJSON.put(null);
            aAllHfAdapter = new AllHfAdapter();
            allHfList.setAdapter(aAllHfAdapter);
        }
    }
    
    @Background
    public void loadHfAllData() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ajbh", ajbh));
        params.add(new BasicNameValuePair("dqlyId", dqlyId));
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_LXFG_LYALL, params);
        loadLyAllDataDone(br);
    }
    
    @UiThread
    public void loadLyAllDataDone(BaseResponseExtr br) {
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
                int len = br.getResJson().getJSONArray("lxfgList").length();
                if (len == 0) {
                	Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
                }
                if (len == MAX_ONEPAGE_SIZE) {
                    isHasMoreData = true;
                } else {
                    isHasMoreData = false;
                }
                if (pageIndex == 1) {
                	resAllhfJSON = new JSONArray();
                	resAllhfJSON.put(0, null);
                    JSONArray resAllhfJSON_ = br.getResJson().getJSONArray("lxfgList");
                    int len_ = resAllhfJSON_.length();
                    for (int i=0; i<len_; i++) {
                    	resAllhfJSON.put(resAllhfJSON.length(), resAllhfJSON_.get(i));
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
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (LxfgEditActy.isCreatSucess) {
    		isAllHuly = false;
    		resAllhfJSON = null;
    		pageIndex = 1;
    		loadLyDqData(lybh);
    	}
    }
    
    class AllHfAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (isAllHuly) {
                if (resAllhfJSON == null) {
                    return 0;
                }
                int len = resAllhfJSON.length();
                return len+(isHasMoreData?1:0);
            } else {
                return 1;
            }
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
                try {
                    convertView = laytInf.inflate(R.layout.lxfg_detail_list_item, null);
                    TextView tvlyCount = (TextView) convertView.findViewById(R.id.tv_lyCount);
                    TextView tvyhfLyCount = (TextView) convertView.findViewById(R.id.tv_yhfLyCount);
                    String lyCount = br.getResJson().getString("lyCount");
                    String yhfLyCount = br.getResJson().getString("yhfLyCount");
                    tvlyCount.setText(lyCount+"条留言");
                    if (Integer.parseInt(lyCount) < 2) {
                        convertView.findViewById(R.id.show_all_lyhf).setVisibility(View.GONE);
                    }
                    tvyhfLyCount.setText(yhfLyCount+"条回复");
                    JSONObject dqly = br.getResJson().getJSONObject("dqly");
                    dqlyId = dqly.getString("CBh");
                    ajbh = dqly.getString("CAjBh");
                    String ajh = dqly.getString("CAjAh");
                    TextView tvAJH = (TextView) convertView.findViewById(R.id.tv_AJH);
                    tvAJH.setText(ajh);
                    String lydx = dqly.optString("CCbfg");
                    if (lydx == null) lydx = "";
                    TextView tvLydx = (TextView) convertView.findViewById(R.id.tv_Lydx);
                    tvLydx.setText(lydx);
                    String CTitle = dqly.getString("CTitle");
                    String CContent = dqly.getString("CContent");
                    String DLyrq = dqly.getString("DLyrq");
                    String CLyrMc = dqly.getString("CLyrMc");
                    TextView tvCLyrMc = (TextView) convertView.findViewById(R.id.tv_CLyrMc);
                    tvCLyrMc.setText(CLyrMc);
                    TextView tvDLyrq = (TextView) convertView.findViewById(R.id.tv_DLyrq);
                    tvDLyrq.setText(DLyrq);
                    TextView tvCTitle = (TextView) convertView.findViewById(R.id.tv_CTitle);
                    tvCTitle.setText(CTitle);
                    TextView tvCContent = (TextView) convertView.findViewById(R.id.tv_CContent);
                    tvCContent.setText(CContent);
                   
                    JSONArray hfxxList = dqly.getJSONArray("hfxxList");
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
                    ImageView lxfgDqimg = (ImageView) convertView.findViewById(R.id.lxfg_dqimg);
                    if (isAllHuly) {
                        lxfgDqimg.setImageResource(R.drawable.lxfg_qb);
                    } else {
                        lxfgDqimg.setImageResource(R.drawable.lxfg_dq);
                    }
                    convertView.findViewById(R.id.show_all_lyhf).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 调用另外的接口 TODO
                            isAllHuly = !isAllHuly;
                            ImageView lxfgDqimg = (ImageView) v.findViewById(R.id.lxfg_dqimg);
                            if (isAllHuly) {
                                lxfgDqimg.setImageResource(R.drawable.lxfg_qb);
                                if (resAllhfJSON.length() == 1) {
                                    pageIndex = 1;
                                    loadHfAllData();
                                }
                            } else {
                                lxfgDqimg.setImageResource(R.drawable.lxfg_dq);
                                aAllHfAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                } catch (JSONException e) {
                    Toast.makeText(LxfgDetailActy.this, "数据解析失败...", Toast.LENGTH_LONG).show();
                }
                
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
