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
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.nrc.NetRegisterCaseReviewActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NetRegisterCaseReviewActivity_;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.util.StringUtils;

@EActivity(R.layout.activity_ajxx)
public class AjxxActy extends BaseActivity{
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.ajxx_list)
    PullToRefreshListView ajxxListView;
    @ViewById(R.id.txtSearch)
    EditText txtSearch;
    @ViewById(R.id.search_text_del)
    View txtSearchDel;
    @ViewById(R.id.search_elm)
    View searchElm;
    @ViewById(R.id.tab_left)
    ViewGroup tabLeft;
    @ViewById(R.id.tab_right)
    ViewGroup tabRight;
    @ViewById(R.id.search_commt)
    View searchCommt;
    AjxxAdapter ajxxAdapter;
    JSONArray resJSONZb, resJSONLs;
    LayoutInflater laytInf;
    boolean isHasMoreDataZb, isHasMoreDataLs;
    private static final int MAX_ONEPAGE_SIZE = 20;
    private int pageIndexZb, pageIndexLs;
    String searchValueZb, searchValueLs;
    private int currScop;
    ListView refreshList;
    private int[] currPosLeft = new int[]{0, 0};
    private int[] currPosRight = new int[]{0, 0};
    
    @AfterViews
    public void initViews() {
    	refreshList = ajxxListView.getRefreshableView();
        isHasMoreDataZb = true;
        isHasMoreDataLs = true;
        pageIndexZb = 1;
        pageIndexLs = 1;
        currScop = getIntent().getIntExtra("flag", -1);
        if (currScop == -1 || currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
            currScop = Constants.AJ_LIST_SCOPE_CXM_ZB;
            tabLeft.performClick();
        } else {
            currScop = Constants.AJ_LIST_SCOPE_YZM_LS;
            tabRight.performClick();
        }
        laytInf = LayoutInflater.from(this);
        ajxxListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                    pageIndexZb = 1;
                } else {
                    pageIndexLs = 1;
                }
                loadDatas();
            }
        });
        ajxxListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (view.getTag(R.id.tag_first) == null||view.getTag(R.id.tag_second)==null) return;
                String CAjBh = view.getTag(R.id.tag_first).toString();
                String CLayyId = view.getTag(R.id.tag_second).toString();
                // 跳转到 创建联系法官页面。。。
                if(StringUtils.isNotBlank(CAjBh)){
                	Intent itt = new Intent();
                	itt.setClass(AjxxActy.this, AjxxDetailActy_.class);
                	itt.putExtra("CBh", CAjBh);
                	startActivity(itt);
                }else{
                	Intent intent = new Intent();
                	TLayy layy = new TLayy();
                	layy.setCId(CLayyId);
                	intent.setClass(AjxxActy.this, NetRegisterCaseReviewActivity_.class);
					intent.putExtra(NetRegisterCaseReviewActivity.K_LAYY, layy);
					startActivity(intent);
                }
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
        txtSearch.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {    
                    searchCommt.performClick();
                }
                return false;
            }
        });
        txtSearchDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearch.setText("");
                if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                    searchValueZb = "";
                } else {
                    searchValueLs = "";
                }
                loadDatas();
            }
        });
        ajxxAdapter = new AjxxAdapter();
        ajxxListView.setAdapter(ajxxAdapter);
        setBtnBack();
        setTitle("我的案件");
        txtSearch.setHint("请输入案号、案由或案件名称搜索");
    }
    
    @Click(R.id.search_commt)
    public void searchCommt() {
        String searchValue = "";
        if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
            pageIndexZb = 1;
        } else {
            pageIndexLs = 1;
        }
        searchValue = txtSearch.getText().toString();
        if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
            searchValueZb = searchValue;
        } else {
            searchValueLs = searchValue;
        }
        if (searchValue == null || searchValue.equals("")) {
            Toast.makeText(this, "请先输入搜索条件...", Toast.LENGTH_LONG).show();
            return;
        }
        loadDatas();
    }
    
    public void onTabLeftClick(View view) {
    	searchElm.setVisibility(View.GONE);
        tabLeft.setBackgroundResource(R.drawable.corner_style_tableft_press);
        tabRight.setBackgroundResource(R.drawable.corner_style_tabright_nomal);
        ((TextView)tabLeft.getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));
        ((TextView)tabRight.getChildAt(0)).setTextColor(Color.parseColor("#676767"));
        txtSearch.setText(searchValueZb);
//        currPosRight = refreshList.getSelectedItemPosition();
        if (refreshList.getChildAt(0) != null) {
        	currPosRight[0] = refreshList.getFirstVisiblePosition()+refreshList.getHeaderViewsCount();
        	currPosRight[1] = refreshList.getChildAt(1).getTop();
        }
        currScop = Constants.AJ_LIST_SCOPE_CXM_ZB;
        if (resJSONZb != null) {
            ajxxAdapter.notifyDataSetChanged();
            refreshList.setSelectionFromTop(currPosLeft[0], currPosLeft[1]);
            return;
        }
        loadDatas();
    }

    public void onTabRightClick(View view) {
        tabRight.setBackgroundResource(R.drawable.corner_style_tabright_press);
        tabLeft.setBackgroundResource(R.drawable.corner_style_tableft_nomal);
        ((TextView)tabRight.getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));
        ((TextView)tabLeft.getChildAt(0)).setTextColor(Color.parseColor("#676767"));
        txtSearch.setText(searchValueLs);
        if (refreshList.getChildAt(0) != null) {
        	currPosLeft[0] = refreshList.getFirstVisiblePosition()+refreshList.getHeaderViewsCount();
        	currPosLeft[1] = refreshList.getChildAt(1).getTop();
        }
//        currPosLeft = refreshList.getSelectedItemPosition();
        currScop = Constants.AJ_LIST_SCOPE_YZM_LS;
        if (resJSONLs != null) {
            ajxxAdapter.notifyDataSetChanged();
            refreshList.setSelectionFromTop(currPosRight[0], currPosRight[1]);
            searchElm.setVisibility(View.VISIBLE);
            return;
        }
        loadDatas();
        searchElm.setVisibility(View.VISIBLE);
    }
    
    
    @Background
    public void loadDatas() {
        String searchValue = "";
        int pageIndex = 1;
        if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
            pageIndex = pageIndexZb;
            searchValue = searchValueZb;
        } else {
            pageIndex = pageIndexLs;
            searchValue = searchValueLs;
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("scope", currScop+""));
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        if (searchValue != null && !searchValue.equals("")) {
            params.add(new BasicNameValuePair("searchValue", searchValue));
        }
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_AjXX, params);
        loadDataDone(br);
    }
    
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        ajxxListView.onRefreshComplete();
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
                int pageIndex = 0;
                int len = br.getResJson().getJSONArray("ajList").length();
                if (len == 0) {
                    Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
                }
                if (len == MAX_ONEPAGE_SIZE) {
                    if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                        isHasMoreDataZb = true;
                    } else {
                        isHasMoreDataLs = true;
                    }
                } else {
                    if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                        isHasMoreDataZb = false;
                    } else {
                        isHasMoreDataLs = false;
                    }
                }
                if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                    pageIndex = pageIndexZb;
                } else {
                    pageIndex = pageIndexLs;
                }
                if (pageIndex == 1) {
                    if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                        resJSONZb = br.getResJson().getJSONArray("ajList");
                        if (resJSONZb.length() == 0) {
                            resJSONZb.put(JSONObject.NULL);
                        }
                    } else {
                        resJSONLs = br.getResJson().getJSONArray("ajList");
                        if (resJSONLs.length() == 0) {
                            resJSONLs.put(JSONObject.NULL);
                        }
                    }
                } else {
                    JSONArray dataList = br.getResJson().getJSONArray("ajList");
                    int lenDataList = dataList.length();
                    for (int i=0; i<lenDataList; i++) {
                        if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                            resJSONZb.put(resJSONZb.length(), dataList.getJSONObject(i));
                        } else {
                            resJSONLs.put(resJSONZb.length(), dataList.getJSONObject(i));
                        }
                    }
                }
                ajxxAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class AjxxAdapter extends BaseAdapter {
        JSONArray resJSON;
        boolean isHasMoreData;
        
        @Override
        public int getCount() {
            if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                resJSON = resJSONZb;
                isHasMoreData = isHasMoreDataZb;
            } else {
                resJSON = resJSONLs;
                isHasMoreData = isHasMoreDataLs;
            }
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
                        if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                            pageIndexZb++;
                        } else {
                            pageIndexLs++;
                        }
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
                convertView = laytInf.inflate(R.layout.ajxx_list_item, null);
            }
            try {
                JSONObject aJSONObj = resJSON.getJSONObject(position);
                TextView tvCAjzt_mc = (TextView)convertView.findViewById(R.id.tv_CAjzt_mc);
                String aCAjzt_mc = "";
                aCAjzt_mc = "["+aJSONObj.getString("CAjzt")+"]";
                aCAjzt_mc += aJSONObj.getString("CAjmc");
                tvCAjzt_mc.setText(aCAjzt_mc);
                JSONObject ajdt = aJSONObj.optJSONObject("ajdt");
                TextView tvCXxnr = (TextView)convertView.findViewById(R.id.tv_CXxnr);
                if (ajdt == null) {
                    tvCXxnr.setText("该案件暂无案件动态...");
                } else {
                    String aCXxnr = ajdt.getString("CXxnr");
                    tvCXxnr.setText("案件动态:"+aCXxnr);
                }
//                convertView.setTag(aJSONObj.getString("CAjBh"));
                convertView.setTag(R.id.tag_first, aJSONObj.getString("CAjBh"));
                convertView.setTag(R.id.tag_second, aJSONObj.getString("CLayyId"));
//                convertView.setTag(0, aJSONObj.getString("CAjBh"));//0位案件编号
//                convertView.setTag(1, aJSONObj.getString("CLayyId"));//1为案件添加标识
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }
}
