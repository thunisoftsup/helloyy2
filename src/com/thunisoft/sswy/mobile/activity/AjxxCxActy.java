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
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.util.AppSecretUtil;

@EActivity(R.layout.activity_ajxx_cx)
public class AjxxCxActy extends BaseActivity{
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
    AjxxAdapter ajxxAdapter;
    JSONArray resJSONCxm, resJSONYzm;
    LayoutInflater laytInf;
    boolean isHasMoreDataCxm, isHasMoreDataYzm;
    private static final int MAX_ONEPAGE_SIZE = 20;
    private int pageIndexCxm, pageIndexYzm;
    String searchValueZb, searchValueLs;
    private int currScop;
    String sfzjhmCxm, sfzjhmYzm, cxm, yzm, tmpSid, sjhm;
    private int cxfs;
    
    @AfterViews
    public void initViews() {
        isHasMoreDataCxm = true;
        isHasMoreDataYzm = true;
        pageIndexCxm = 1;
        pageIndexYzm = 1;
        currScop = getIntent().getIntExtra("flag", -1);
        sfzjhmCxm = getIntent().getStringExtra("sfzjhmCxm");
        sfzjhmYzm = getIntent().getStringExtra("sfzjhmYzm");
        cxm = getIntent().getStringExtra("cxm");
        yzm = getIntent().getStringExtra("yzm");
        sjhm = getIntent().getStringExtra("sjhm");
        tmpSid = getIntent().getStringExtra("tmpSid");
        cxfs = getIntent().getIntExtra("cxfs", 1);
        String resJson = getIntent().getStringExtra("resJson");
        laytInf = LayoutInflater.from(this);
        ajxxListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                    pageIndexCxm = 1;
                } else {
                    pageIndexYzm = 1;
                }
                loadDatas();
            }
        });
        ajxxListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (view.getTag() == null) return;
                String CBh = view.getTag().toString();
                // 跳转到 创建联系法官页面。。。
                Intent itt = new Intent();
                itt.setClass(AjxxCxActy.this, AjxxDetailActy_.class);
                itt.putExtra("CBh", CBh);
                itt.putExtra("tempSid", tmpSid);
                String sfzjhm = "";
                if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                    sfzjhm = sfzjhmCxm;
                } else {
                    sfzjhm = sfzjhmYzm;
                }
                itt.putExtra("sfzjhm", sfzjhm);
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
        JSONArray resJsonArr = null;
        if (currScop == -1 || currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
            searchElm.setVisibility(View.GONE);
            currScop = Constants.AJ_LIST_SCOPE_CXM_ZB;
            try {
                resJSONCxm = new JSONArray(resJson);
                resJsonArr = resJSONCxm;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            searchElm.setVisibility(View.VISIBLE);
            currScop = Constants.AJ_LIST_SCOPE_YZM_LS;
            try {
                resJSONYzm = new JSONArray(resJson);
                resJsonArr = resJSONYzm;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (resJsonArr.length() == 1) {
        	Intent itt = new Intent();
        	itt.putExtra("tempSid", tmpSid);
            itt.setClass(AjxxCxActy.this, AjxxDetailActy_.class);
            String CBh = null;
			try {
				CBh = resJsonArr.getJSONObject(0).getString("CBh");
			} catch (JSONException e) {
			}
            itt.putExtra("CBh", CBh);
            String sfzjhm = "";
            if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                sfzjhm = sfzjhmCxm;
            } else {
                sfzjhm = sfzjhmYzm;
            }
            itt.putExtra("sfzjhm", sfzjhm);
            startActivity(itt);
            finish();
            return;
        }
        txtSearch.setHint("请输入案号、案由或案件名称搜索");
        loadDataDone(null);
    }
    
    @Click(R.id.search_commt)
    public void searchCommt() {
        String searchValue = "";
        if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
            pageIndexCxm = 1;
        } else {
            pageIndexYzm = 1;
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
    
    
    @Background
    public void loadDatas() {
        String searchValue = "";
        int pageIndex = 1;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        int currNetdr;
        if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
            currNetdr = ResponseUtilExtr.LOAD_CXM_AJ;
            pageIndex = pageIndexCxm;
            searchValue = searchValueZb;
            params.add(new BasicNameValuePair("sfzjhm", AppSecretUtil.encodeAppString(sfzjhmCxm)));
            params.add(new BasicNameValuePair("cxm", AppSecretUtil.encodeAppString(cxm)));
            params.add(new BasicNameValuePair("sid", tmpSid));
        } else {
            currNetdr = ResponseUtilExtr.LOAD_YZM_AJ;
            params.add(new BasicNameValuePair("sfzjhm", AppSecretUtil.encodeAppString(sfzjhmYzm)));
            params.add(new BasicNameValuePair("yzm", AppSecretUtil.encodeAppString(yzm)));
            params.add(new BasicNameValuePair("sid", tmpSid));
            params.add(new BasicNameValuePair("phone", AppSecretUtil.encodeAppString(sjhm)));
            pageIndex = pageIndexYzm;
            searchValue = searchValueLs;
        }
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        if (searchValue != null && !searchValue.equals("")) {
            params.add(new BasicNameValuePair("searchValue", searchValue));
        }
        BaseResponseExtr br = responseUtil.getResponse(currNetdr, params);
        loadDataDone(br);
    }
    
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        ajxxListView.onRefreshComplete();
        if (br != null && br.getMsg() != null) {
            //Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        	Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
        	if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                isHasMoreDataCxm = false;
            } else {
                isHasMoreDataYzm = false;
            }
        	ajxxAdapter.notifyDataSetChanged();
        } else {
            try {
                JSONArray resJSON;
                if (br == null) {
                    if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                        resJSON = resJSONCxm;
                    } else {
                        resJSON = resJSONYzm;
                    }
                } else {
                    resJSON = br.getResJson().getJSONArray("ajList");
                }
                int pageIndex = 0;
                int len = resJSON.length();
                if (len == 0) {
                    Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_LONG).show();
                }
                if (len == MAX_ONEPAGE_SIZE) {
                    if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                        isHasMoreDataCxm = true;
                    } else {
                        isHasMoreDataYzm = true;
                    }
                } else {
                    if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                        isHasMoreDataCxm = false;
                    } else {
                        isHasMoreDataYzm = false;
                    }
                }
                if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                    pageIndex = pageIndexCxm;
                } else {
                    pageIndex = pageIndexYzm;
                }
                if (pageIndex == 1) {
                    if (resJSON.length() == 0) {
                        resJSON.put(JSONObject.NULL);
                    }
                    if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                        resJSONCxm = resJSON;
                    } else {
                        resJSONYzm = resJSON;
                    }
                } else {
                    JSONArray dataList = resJSON;
                    int lenDataList = dataList.length();
                    for (int i=0; i<lenDataList; i++) {
                        if (currScop == Constants.AJ_LIST_SCOPE_CXM_ZB) {
                            resJSONCxm.put(resJSONCxm.length(), dataList.getJSONObject(i));
                        } else {
                            resJSONYzm.put(resJSONYzm.length(), dataList.getJSONObject(i));
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
                resJSON = resJSONCxm;
                isHasMoreData = isHasMoreDataCxm;
            } else {
                resJSON = resJSONYzm;
                isHasMoreData = isHasMoreDataYzm;
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
                            pageIndexCxm++;
                        } else {
                            pageIndexYzm++;
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
                convertView.setTag(aJSONObj.getString("CBh"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return convertView;
        }
        
    }
}
