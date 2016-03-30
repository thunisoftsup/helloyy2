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
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

@EActivity(R.layout.activity_lxfg)
public class LxfgActy extends BaseActivity{
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.lxfg_list)
    PullToRefreshListView lxfgListView;
    
    @ViewById(R.id.tab_left)
    ViewGroup tabLeft;
    @ViewById(R.id.tab_mid)
    ViewGroup tabMid;
    @ViewById(R.id.tab_right)
    ViewGroup tabRight;
    @ViewById(R.id.txtSearch)
    EditText txtSearch;
    @ViewById(R.id.search_text_del)
    View txtSearchDel;
    @ViewById(R.id.search_commt)
    View searchCommt;
    
    private static final int LXFG_ALL = 0;
    private static final int LXFG_DHF = -1;
    private static final int LXFG_YHF = 1;
    private int currState;
    
    LxfgAdapter aLxfgAdapter;
    JSONArray resJSONall, resJSONdhf, resJSONyhf;
    LayoutInflater laytInf;
    boolean isHasMoreDataAll, isHasMoreDataYhf, isHasMoreDataDhf;
    private static final int MAX_ONEPAGE_SIZE = 20;
    private int pageIndexAll, pageIndexDhf, pageIndexYhf;
    String searchValueAll, searchValueDhf, searchValueYhf;
    
    boolean isRefsh;
    ListView refreshList;
    private int[] currPosDhf = new int[]{0, 0};
    private int[] currPosYhf = new int[]{0, 0};
    private int[] currPosAll = new int[]{0, 0};
    int preState;
    
    public void onTabLeftClick(View view) {
        tabLeft.setBackgroundResource(R.drawable.corner_style_tableft_press);
        tabMid.setBackgroundResource(R.drawable.corner_style_tabmid_nomal);
        tabRight.setBackgroundResource(R.drawable.corner_style_tabright_nomal);
        ((TextView)tabLeft.getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));
        ((TextView)tabMid.getChildAt(0)).setTextColor(Color.parseColor("#676767"));
        ((TextView)tabRight.getChildAt(0)).setTextColor(Color.parseColor("#676767"));
        
        txtSearch.setText(searchValueAll);
        currState = LXFG_ALL;
        isRefsh = false;
        loadDatas();
    }

    public void onTabMidClick(View view) {
        tabMid.setBackgroundResource(R.drawable.corner_style_tabmid_press);
        tabLeft.setBackgroundResource(R.drawable.corner_style_tableft_nomal);
        tabRight.setBackgroundResource(R.drawable.corner_style_tabright_nomal);
        ((TextView)tabMid.getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));
        ((TextView)tabLeft.getChildAt(0)).setTextColor(Color.parseColor("#676767"));
        ((TextView)tabRight.getChildAt(0)).setTextColor(Color.parseColor("#676767"));
        
        txtSearch.setText(searchValueYhf);
        currState = LXFG_YHF;
        isRefsh = false;
        loadDatas();
    }

    public void onTabRightClick(View view) {
        tabRight.setBackgroundResource(R.drawable.corner_style_tabright_press);
        tabMid.setBackgroundResource(R.drawable.corner_style_tabmid_nomal);
        tabLeft.setBackgroundResource(R.drawable.corner_style_tableft_nomal);
        ((TextView)tabRight.getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));
        ((TextView)tabMid.getChildAt(0)).setTextColor(Color.parseColor("#676767"));
        ((TextView)tabLeft.getChildAt(0)).setTextColor(Color.parseColor("#676767"));
        
        txtSearch.setText(searchValueDhf);
        currState = LXFG_DHF;
        isRefsh = false;
        loadDatas();
    }
    
    @Click(R.id.btn_blank)
    public void creatLxfgLy() {
        Intent itt = new Intent();
        itt.setClass(this, LyAjActy_.class);
        startActivity(itt);
    }

    @Click(R.id.search_commt)
    public void searchCommt() {
        String searchValue = txtSearch.getText().toString();
        if (searchValue == null || searchValue.equals("")) {
            Toast.makeText(this, "请先输入搜索条件...", Toast.LENGTH_LONG).show();
            return;
        }
        if (currState == LXFG_ALL) {
            searchValueAll = searchValue;
        } else if (currState == LXFG_DHF) {
            searchValueDhf = searchValue;
        } else if (currState == LXFG_YHF) {
            searchValueYhf = searchValue;
        }
        isRefsh = true;
        loadDatas();
    }
    
    @AfterViews
    public void initViews() {
    	LxfgEditActy.isCreatSucess = false;
    	refreshList = lxfgListView.getRefreshableView();
        pageIndexAll = 1;
        pageIndexYhf = 1;
        pageIndexDhf = 1;
        laytInf = LayoutInflater.from(this);
        lxfgListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (currState == LXFG_ALL) {
                    pageIndexAll = 1;
                } else if (currState == LXFG_DHF) {
                    pageIndexDhf = 1;
                } else if (currState == LXFG_YHF) {
                    pageIndexYhf = 1;
                }
                isRefsh = true;
                loadDatas();
            }
        });
        
        lxfgListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (view.getTag() == null) return;
                String tag = view.getTag().toString();
                // 跳转到 创建联系法官页面。。。
                Intent itt = new Intent();
                itt.setClass(LxfgActy.this, LxfgDetailActy_.class);
                itt.putExtra("lybh", tag); // 这个是 留言编号
                startActivity(itt);
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
                if (currState == LXFG_ALL) {
                    searchValueAll = "";
                } else if (currState == LXFG_DHF) {
                    searchValueDhf = "";
                } else if (currState == LXFG_YHF) {
                    searchValueYhf = "";
                }
                isRefsh = true;
                loadDatas();
            }
        });
        aLxfgAdapter = new LxfgAdapter();
        lxfgListView.setAdapter(aLxfgAdapter);
        setBtnBack();
        setTitle("联系法官");
        tabLeft.performClick();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (LxfgEditActy.isCreatSucess) {
            LxfgEditActy.isCreatSucess = false;
            resJSONall = null; resJSONdhf = null; resJSONyhf = null;
            pageIndexAll = 1; pageIndexDhf = 1; pageIndexYhf = 1;
            tabLeft.performClick();
        }
    }
    
    @Background
    public void loadDatas() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        int pageIndex = 1;
        String searchValue = "";
        if (currState == LXFG_ALL) {
            searchValue = searchValueAll;
            if (resJSONall != null && !isRefsh) {
                loadDataDone(null);
                return;
            }
            pageIndex = pageIndexAll;
        } else if (currState == LXFG_DHF) {
            searchValue = searchValueDhf;
            if (resJSONdhf != null && !isRefsh) {
                loadDataDone(null);
                return;
            }
            pageIndex = pageIndexDhf;
            params.add(new BasicNameValuePair("status", Constants.STATUS_LXFG_DHF+""));
        } else if (currState == LXFG_YHF) {
            searchValue = searchValueYhf;
            if (resJSONyhf != null  && !isRefsh) {
                loadDataDone(null);
                return;
            }
            pageIndex = pageIndexYhf;
            params.add(new BasicNameValuePair("status", Constants.STATUS_LXFG_YHF+""));
        }
        if (searchValue != null && !searchValue.equals("")) {
            params.add(new BasicNameValuePair("searchValue", searchValue));
        }
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_LXFG, params);
        loadDataDone(br);
    }
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        lxfgListView.onRefreshComplete();
        if (br == null) {
        	if(preState == LXFG_ALL) {
        		if (refreshList.getChildAt(0) != null) {
                	currPosAll[0] = refreshList.getFirstVisiblePosition()+refreshList.getHeaderViewsCount();
                	currPosAll[1] = refreshList.getChildAt(1).getTop();
                }
        	} else if(preState == LXFG_DHF) {
        		if (refreshList.getChildAt(0) != null) {
                	currPosDhf[0] = refreshList.getFirstVisiblePosition()+refreshList.getHeaderViewsCount();
                	currPosDhf[1] = refreshList.getChildAt(1).getTop();
                }
        	} else if(preState == LXFG_YHF) {
        		if (refreshList.getChildAt(0) != null) {
                	currPosYhf[0] = refreshList.getFirstVisiblePosition()+refreshList.getHeaderViewsCount();
                	currPosYhf[1] = refreshList.getChildAt(1).getTop();
                }
        	}
        	
            aLxfgAdapter.notifyDataSetChanged();
            if(currState == LXFG_ALL) {
            	refreshList.setSelectionFromTop(currPosAll[0], currPosAll[1]);
            } else if(currState == LXFG_DHF) {
            	refreshList.setSelectionFromTop(currPosDhf[0], currPosDhf[1]);
            } else if(currState == LXFG_YHF) {
            	refreshList.setSelectionFromTop(currPosYhf[0], currPosYhf[1]);
            }
            preState = currState;
            return;
        }
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            try {
                int len = br.getResJson().getJSONArray("lxfgList").length();
                
                int pageIndex = 1;
                JSONArray targetJSON = null;
                if (currState == LXFG_ALL) {
                    if (len == MAX_ONEPAGE_SIZE) {
                        isHasMoreDataAll = true;
                    } else {
                        isHasMoreDataAll = false;
                    }
                    targetJSON = resJSONall;
                    pageIndex = pageIndexAll;
                } else if (currState == LXFG_DHF) {
                    if (len == MAX_ONEPAGE_SIZE) {
                        isHasMoreDataDhf = true;
                    } else {
                        isHasMoreDataDhf = false;
                    }
                    targetJSON = resJSONdhf;
                    pageIndex = pageIndexDhf;
                } else if (currState == LXFG_YHF) {
                    if (len == MAX_ONEPAGE_SIZE) {
                        isHasMoreDataYhf = true;
                    } else {
                        isHasMoreDataYhf = false;
                    }
                    targetJSON = resJSONyhf;
                    pageIndex = pageIndexYhf;
                }
                
                if (len == 0 && pageIndex > 1) {
                    Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
                }
                
                if (pageIndex == 1) {
                    if (currState == LXFG_ALL) {
                        resJSONall = br.getResJson().getJSONArray("lxfgList");
                        if (resJSONall.length() == 0) {
                            resJSONall.put(JSONObject.NULL);
                        }
                    } else if (currState == LXFG_DHF) {
                        resJSONdhf = br.getResJson().getJSONArray("lxfgList");
                        if (resJSONdhf.length() == 0) {
                            resJSONdhf.put(JSONObject.NULL);
                        }
                    } else if (currState == LXFG_YHF) {
                        resJSONyhf = br.getResJson().getJSONArray("lxfgList");
                        if (resJSONyhf.length() == 0) {
                            resJSONyhf.put(JSONObject.NULL);
                        }
                    }
                } else {
                    JSONArray dataList = br.getResJson().getJSONArray("lxfgList");
                    int lenDataList = dataList.length();
                    for (int i=0; i<lenDataList; i++) {
                        targetJSON.put(targetJSON.length(), dataList.getJSONObject(i));
                    }
                }
                aLxfgAdapter.notifyDataSetChanged();
                preState = currState;
            } catch (JSONException e) {
                Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    class LxfgAdapter extends BaseAdapter {
        JSONArray resJSON;
        boolean isHasMoreData;
        @Override
        public int getCount() {
            if (currState == LXFG_ALL) {
                resJSON = resJSONall;
                isHasMoreData = isHasMoreDataAll;
            } else if (currState == LXFG_DHF) {
                resJSON = resJSONdhf;
                isHasMoreData = isHasMoreDataDhf;
            } else if (currState == LXFG_YHF) {
                resJSON = resJSONyhf;
                isHasMoreData = isHasMoreDataYhf;
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
                        if (currState == LXFG_ALL) {
                            pageIndexAll++;
                        } else if (currState == LXFG_DHF) {
                            pageIndexDhf++;
                        } else if (currState == LXFG_YHF) {
                            pageIndexYhf++;
                        }
                        isRefsh = true;
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
                convertView = laytInf.inflate(R.layout.lxfg_list_item, null);
            }
            try {
                JSONObject aJSONObj = resJSON.getJSONObject(position);
                TextView tvCCbfg = (TextView)convertView.findViewById(R.id.tv_CCbfg);
                String aCCbfg = "";
                if (!aJSONObj.isNull("CCbfg")) {
                    aCCbfg = aJSONObj.getString("CCbfg");
                    if (aCCbfg.length() > 6) {
                        aCCbfg = aCCbfg.substring(0, 6)+"...";
                    }
                }
                if (aCCbfg == null || aCCbfg.equals("")) {
                    tvCCbfg.setText("");
                    //resJSON.remove(position);
//                    int len = resJSON.length();
//                    JSONArray resJSON_ = new JSONArray();
//                    for (int i=0; i<len; i++) {
//                        if (i == position) {
//                            continue;
//                        }
//                        resJSON_.put(resJSON_.length(), resJSON.get(i));
//                    }
//                    if (currState == LXFG_ALL) {
//                        resJSONall = resJSON_;
//                    } else if (currState == LXFG_DHF) {
//                        resJSONdhf = resJSON_;
//                    } else if (currState == LXFG_YHF) {
//                        resJSONyhf = resJSON_;
//                    }
//                    lxfgListView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            notifyDataSetChanged();
//                        }
//                    });
                } else {
                    tvCCbfg.setText("["+aCCbfg+"]");
                }
                
                TextView tvDZxhfrq = (TextView) convertView.findViewById(R.id.tv_DZxhfrq);
                
                String aNStatus = aJSONObj.getString("NStatus");
                if (aNStatus.equals(Constants.STATUS_LXFG_DHF+"")) {
                    //待审查
                    tvDZxhfrq.setText("待回复...");
                    tvDZxhfrq.setTextColor(Color.parseColor("#ff6601"));
                } else if (aNStatus.equals(Constants.STATUS_LXFG_YHF+"")) {
                    //审查合格
                    String aDZxhfrq = "";
                    if (!aJSONObj.isNull("DZxhfrq")) {
                        aDZxhfrq = aJSONObj.getString("DZxhfrq");
                    }
                    tvDZxhfrq.setText("回复："+aDZxhfrq);
                    tvDZxhfrq.setTextColor(Color.parseColor("#666666"));
                } 
                
                TextView tvCTitle = (TextView)convertView.findViewById(R.id.tv_CTitle);
                String aCTitle = "";
                if (!aJSONObj.isNull("CTitle")) {
                    aCTitle = aJSONObj.getString("CTitle");
                }
                tvCTitle.setText(aCTitle);
                
                TextView tvCAjAh = (TextView)convertView.findViewById(R.id.tv_CAjAh);
                String aCAjAh = "";
                if (!aJSONObj.isNull("CAjAh")) {
                    aCAjAh = aJSONObj.getString("CAjAh");
                }
                tvCAjAh.setText(aCAjAh);
                
                TextView tvDLyrq = (TextView)convertView.findViewById(R.id.tv_DLyrq);
                String aDLyrq = "";
                if (!aJSONObj.isNull("DLyrq")) {
                    aDLyrq = aJSONObj.getString("DLyrq");
                }
                tvDLyrq.setText("留言:"+aDLyrq);
                convertView.setTag(aJSONObj.getString("CBh"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }
}
