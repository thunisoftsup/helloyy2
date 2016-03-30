package com.thunisoft.sswy.mobile.activity.dzsd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.SdLogic;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.response.SdResponse;
import com.thunisoft.sswy.mobile.pojo.TSd;
import com.thunisoft.sswy.mobile.pojo.TSdWrit;
import com.thunisoft.sswy.mobile.util.IntentHelper;
import com.thunisoft.sswy.mobile.util.WritIconUtils;

/**
 * 送达文书列表
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_writ_list)
public class WritListActivity extends BaseActivity {
    private static final String[] from = new String[] {"writ" };
    private static final int[] to = new int[] {R.id.tv_writ_name };

    @ViewById(R.id.list_writs)
    ListView lvWritList;

    @Bean
    LoginCache loginCache;

    @Bean
    SdLogic sdLogic;

    @ViewById(R.id.tv_ah)
    TextView tv_ah;// 案号
    @ViewById(R.id.tv_value_sdr)
    TextView tv_value_sdr;// 送达人
    @ViewById(R.id.tv_value_sdrq)
    TextView tv_value_sdrq;// 送达日期
    @ViewById(R.id.tv_value_bgdh)
    TextView tv_value_bgdh;// 办公电话
    @ViewById(R.id.tv_value_ts)
    TextView tv_value_ts;// 庭室
    @ViewById(R.id.tv_value_fy)
    TextView tv_value_fy;// 法院

    @Extra("CId")
    String CId;
    @Extra("CAh")
    String CAh;
    @Extra("CSdrMc")
    String CSdrMc;
    @Extra("CSdrBgdh")
    String CSdrBgdh;
    @Extra("DFssj")
    String DFssj;
    @Extra("CSdrTs")
    String CSdrTs;
    @Extra("CSdrFy")
    String CSdrFy;
    @Extra("CWritName")
    String CWritName;

    private SimpleAdapter listAdapter = null;
    private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    @Bean
    WritIconUtils writIconUtils;

    @AfterViews
    public void initViews() {
        setResult(RESULT_OK);
        setTitle("文书详情");
        listAdapter = new SimpleAdapter(WritListActivity.this, dataList, R.layout.sd_writ_list_item, from, to);
        lvWritList.setAdapter(listAdapter);
        setBtnBack();
        if (!loginCache.isLogined()) {
            tv_ah.setText("【" + CAh + "】");
            tv_value_sdr.setText(CSdrMc);
            tv_value_sdrq.setText(DFssj);
            tv_value_bgdh.setText(CSdrBgdh);
            tv_value_ts.setText(CSdrTs);
            tv_value_fy.setText(CSdrFy);
            setWritData();
        } else {
            loadSdInfo();
        }
    }

    @Background
    public void setWritData() {
        loadWritDataFromDb();
    }

    public void loadWritDataFromDb() {
        List<TSdWrit> sdWritList = sdLogic.getSdWritFromDb(CId);
        List<Map<String, Object>> newDataList = new ArrayList<Map<String, Object>>();
        for (TSdWrit writ : sdWritList) {
            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("icon", writIconUtils.getWritIconResId(writ.getCPath()));
            map.put("writ", "【文件名】"+writ.getCName());
            map.put("path", writ.getCPath());
            newDataList.add(map);
        }
        if(newDataList != null && !newDataList.isEmpty()) {
            notifyDataSetChanged(newDataList);
        } else {
            
        }
        
    }

    @UiThread
    public void notifyDataSetChanged(List<Map<String, Object>> newDataList) {
        dataList.clear();
        dataList.addAll(newDataList);
        listAdapter.notifyDataSetChanged();
    }

    @Background
    public void loadSdInfo() {
        SdResponse sr = sdLogic.loadSdInfo(CId);
        if (!sr.isSuccess()) {
            showToast(sr.getMessage());
        } else {
            loadWritDataFromDb();
            TSd sd = sr.getWrit();
            setSdInfo(sd);

        }
    }

    @UiThread
    public void setSdInfo(TSd sd) {
        tv_ah.setText("【" + sd.getCAh() + "】");
        tv_value_sdr.setText(sd.getCSdrMc());
        Long qssj = sd.getDQssj();
        if(qssj!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            tv_value_sdrq.setText(sdf.format(qssj));
        }
       
        tv_value_bgdh.setText(sd.getCSdrBgdh());
        tv_value_ts.setText(sd.getCSdrTs());
        tv_value_fy.setText(sd.getCSdrFy());
    }

    @ItemClick(R.id.list_writs)
    public void lvWritListItemClick(int position) {
        Intent intent = null;
        String path = dataList.get(position).get("path").toString();
        intent = ResponseUtilExtr.openFileWithAllPath(path);
//        int resId = 0;

//        if (path != null && (path.endsWith("pdf") || path.endsWith("PDF"))) {
//            intent = IntentHelper.getPdfFileIntent(path);
//            resId = R.string.no_pdf_reader;
//        } else {
//            intent = IntentHelper.getWordFileIntent(path);
//            resId = R.string.no_word_reader;
//        }
//        if (resId == 0) {
//            showToast(getResources().getString(resId));
//            return;
//        }
        try {
            WritListActivity.this.startActivity(intent);
        } catch (Exception e) {
            Log.e("", "打开文件出错", e);
            showToast("打开文件出错");
        }
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
