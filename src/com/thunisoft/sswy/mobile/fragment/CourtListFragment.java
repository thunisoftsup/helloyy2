package com.thunisoft.sswy.mobile.fragment;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.adapter.CourtListAdapter;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.interfaces.IOnCourtSelectedListener;
import com.thunisoft.sswy.mobile.interfaces.IOnNeedLoadingListener;
import com.thunisoft.sswy.mobile.pojo.TCourt;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;

@EFragment(R.layout.fragment_court_list)
public class CourtListFragment extends Fragment{
    @Pref
    ConfigUtils_ configUtils;
    
    @Bean
    CourtCache courtCache;
    
    @ViewById(R.id.court_list)
    PullToRefreshListView courtListView;
    CourtListAdapter courtListAdapter;
    List<TCourt> courtList = new ArrayList<TCourt>();
    IOnNeedLoadingListener<TCourt> onNeedLoadingListener;
    IOnCourtSelectedListener onCourtSelectedListener;
    String cid, currCourtName;
    @AfterViews
    public void initViews() {
        courtListAdapter = new CourtListAdapter(getActivity(), R.layout.court_list_item, courtList);
        courtListAdapter.setNotifyOnChange(false);
        cid = courtCache.getCourtId();
        courtListAdapter.setCid(cid);
        courtListView.setAdapter(courtListAdapter);
        courtListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	TextView tv = (TextView) view.findViewById(R.id.tv_court_name);
            	if (tv.getText().equals("")) {
            		return;
            	}
                view.performClick();
                TCourt selectedCourt = (TCourt)parent.getItemAtPosition(position);
                if(selectedCourt != null) {
                    onCourtSelectedListener.select(selectedCourt);
                }
            }
        });
        courtListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadDatas();
            }
        });
        loadDatas();
    }
    
    @Background
    public void loadDatas() {
        List<TCourt> dataList = onNeedLoadingListener.load();
        loadCourtDone(dataList);
    }
    
    private void selectCourt(TCourt court, int index) {
        cid = court.getCId();
        currCourtName = court.getCName();
        courtListAdapter.setCid(cid);
        courtCache.setCourtId(cid);
        courtCache.setCourtName(currCourtName);
        courtListView.getRefreshableView().setSelection(index);
        onCourtSelectedListener.select(court);
    }
    
    @UiThread
    public void loadCourtDone(List<TCourt> dataList) {
        courtList.clear();
        courtList.addAll(dataList);
        courtList.add(null);
        courtList.add(null);
        courtList.add(null);
        courtListAdapter.notifyDataSetChanged();
//        if (cid == null && !dataList.isEmpty()) {
//            selectCourt(dataList.get(0), 0);
//        } else {
            for(int i = 0;i<dataList.size();i++) {
                TCourt court = dataList.get(i);
                if(court.getCId().equals(cid)) {
                    selectCourt(dataList.get(i), i);
                    break;
                }
            }
//        }
        courtListView.onRefreshComplete();
    }

    public void setOnNeedLoadingListener(IOnNeedLoadingListener<TCourt> onNeedLoadingListener) {
        this.onNeedLoadingListener = onNeedLoadingListener;
    }

    public void setOnCourtSelectedListener(IOnCourtSelectedListener onCourtSelectedListener) {
        this.onCourtSelectedListener = onCourtSelectedListener;
    }
    
    public String getCid() {
    	return cid;
    }
    
    public String getCurrCountName() {
    	return currCourtName;
    }
}
