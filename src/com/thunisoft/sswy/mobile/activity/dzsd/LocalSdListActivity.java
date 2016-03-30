package com.thunisoft.sswy.mobile.activity.dzsd;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.adapter.SdListAdapter;
import com.thunisoft.sswy.mobile.interfaces.IOnCkxqListener;
import com.thunisoft.sswy.mobile.logic.SdLogic;
import com.thunisoft.sswy.mobile.pojo.TSd;
import com.thunisoft.sswy.mobile.pojo.TSdWrit;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 我的送达文书列表
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_local_sd_list)
public class LocalSdListActivity extends BaseActivity implements IOnCkxqListener{
    @ViewById(R.id.list_sd)
    ListView list_sd;

    SdListAdapter adapter;
    
    List<TSd> datas = new ArrayList<TSd>();
    List<TSd> allDatas = new ArrayList<TSd>();
    
    @ViewById(R.id.btn_load_more)
    Button btn_load_more;
    
    @ViewById(R.id.txtSearch)
    EditText txtSearch;

    View search_commt;

    View search_text_del;
    
    WaittingDialog waitDialog;
    
    @Bean
    SdLogic sdLogic;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    
    @AfterViews
    public void initViews() {
        setTitle("已签收文书");
        setBtnBack();
        search_commt = findViewById(R.id.search_commt);
        search_commt.setOnClickListener(this);
        search_text_del = findViewById(R.id.search_text_del);
        search_text_del.setOnClickListener(this);
        txtSearch = (EditText)findViewById(R.id.txtSearch);
        txtSearch.setHint(getText(R.string.sd_search_hint));
        adapter = new SdListAdapter(this, R.layout.standard_list_item_multiple, datas, Constants.SCOPE_DZSD_YQS);
        adapter.setOnCkxqListener(this);
        list_sd.setAdapter(adapter);
        waitDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "...");
        waitDialog.setIsCanclable(false);
        search();
    }
    
    public void search() {
        String text = txtSearch.getText().toString();
        loadDatas(text);
    }
    
    @Background
    public void loadDatas(String searchValue) {
        if(allDatas.isEmpty()) {
            allDatas = sdLogic.getSdListFromDb(0);
        }
        List<TSd> filterList = new ArrayList<TSd>();
        if(StringUtils.isNotBlank(searchValue)) {
            for(TSd sd : allDatas) {
                if(sd.getCAh() != null && sd.getCAh().indexOf(searchValue) > -1 || sd.getCWritName().indexOf(searchValue) > -1) {
                    filterList.add(sd);
                }
            }
        } else {
            filterList = allDatas;
        }
        notifyDataChanged(filterList);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.search_text_del:
            txtSearch.setText("");
            break;
        case R.id.search_commt:
            search();
            break;
        }
        super.onClick(v);
    }
    
    @UiThread
    public void notifyDataChanged(List<TSd> sdList) {
        datas.clear();
        datas.addAll(sdList);
        adapter.notifyDataSetChanged();
    }
    
    public boolean checkWsExists(String sdId) {
        List<TSdWrit> writList = sdLogic.getSdWritFromDb(sdId);
        if(writList == null || writList.isEmpty()) {
            return false;
        }
        for(TSdWrit writ : writList) {
            if(StringUtils.isBlank(writ.getCPath())) {
                return false;
            }
            File file = new File(writ.getCPath());
            if(!file.exists() || !file.isFile()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        waitDialog.dismiss();
    }

    @Override
    public void ckxq(String sdId, View view) {
        waitDialog.show();
        if(checkWsExists(sdId)) {
            TSd sd = sdLogic.getSdInfoFromDb(sdId);
            Intent intent = new Intent(this, WritListActivity_.class);
            intent.putExtra("CId", sd.getCId());
            intent.putExtra("CAh", sd.getCAh());
            intent.putExtra("CSdrMc", sd.getCSdrMc());
            intent.putExtra("CSdrBgdh", sd.getCSdrBgdh());
            intent.putExtra("DFssj", sdf.format(sd.getDFssj()));
            intent.putExtra("CSdrTs", sd.getCSdrTs());
            intent.putExtra("CSdrFy", sd.getCSdrFy());
            intent.putExtra("CWritName", sd.getCWritName());
            startActivityForResult(intent, Constants.REQUEST_CODE_SHOW_SD_WRIT_LIST);
        } else {
            showToast("您查看的文书已经被删除");
        }
        
    }

}
