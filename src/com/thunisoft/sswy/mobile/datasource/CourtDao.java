package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.logic.response.CourtResponse;
import com.thunisoft.sswy.mobile.pojo.TCourt;
import com.thunisoft.sswy.mobile.pojo.TProvince;

@EBean(scope = Scope.Singleton)
public class CourtDao {

    private static final String TABLE_COURT = "t_court";

    public List<TProvince> getProvinceList() {
        List<TProvince> provinceList = new ArrayList<TProvince>();
        String sql = "select c_id,c_name,c_fjm from t_province order by n_order";
        Cursor cursor = null;
        try {
            cursor = DBHelper.query(sql, new String[] {});
            while (cursor.moveToNext()) {
                TProvince province = new TProvince();
                province.setCId(cursor.getString(0));
                String name = cursor.getString(1);
                if(name.length() == 2) {
                  StringBuilder sb = new StringBuilder();
                  sb.append(name);
                  sb.insert(1, "　");
                  name = sb.toString();
                }
                province.setCName(name);
                province.setCFjm(cursor.getString(2));
                provinceList.add(province);
            }
        } finally {
            cursor.close();
        }
        provinceList.add(null);
        provinceList.add(null);
        return provinceList;
    }
    
    public TCourt getCourt(String courtId) {
        String sql = "select c_id,c_name,c_ssfw_url,c_wap_url,n_jsfs from t_court where c_id=?";
        TCourt court = new TCourt();
        Cursor cursor = null;
        try {
            cursor = DBHelper.query(sql, new String[] { courtId });
            while (cursor.moveToNext()) {
                court.setCId(cursor.getString(0));
                court.setCName(cursor.getString(1));
                court.setCSsfwUrl(cursor.getString(2));
                court.setCWapUrl(cursor.getString(3));
                court.setNJsfs(cursor.getInt(4));
            }
            return court;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<TCourt> getCourtList(String fjm) {
        List<TCourt> courtList = new ArrayList<TCourt>();
        String sql = "select c_id,c_name,c_ssfw_url,c_wap_url,n_jsfs from t_court where c_fjm like '?%' order by n_order";
        Cursor cursor = null;
        try {
            cursor = DBHelper.query(sql, new String[] { fjm });
            while (cursor.moveToNext()) {
                TCourt court = new TCourt();
                court.setCId(cursor.getString(0));
                court.setCName(cursor.getString(1));
                court.setCSsfwUrl(cursor.getString(2));
                court.setCWapUrl(cursor.getString(3));
                court.setNJsfs(cursor.getInt(4));
                courtList.add(court);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return courtList;
    }

    public void saveCourt(CourtResponse cr) throws SSWYException {
        DBHelper.beginTransaction();
        List<TCourt> courtList = cr.getData();
        if (courtList != null) {
            for (TCourt court : courtList) {
                if (!DBHelper.insert(TABLE_COURT, convertCourt(court))) {
                    throw new SSWYException("法院信息存储失败！" + court.getCId());
                }
            }
        }
        List<String> closedData = cr.getClosedData();
        if (closedData != null) {
            String[] params = new String[1];
            for (String id : closedData) {
                params[0] = id;
                if (!DBHelper.delete(TABLE_COURT, "c_id=?", params)) {
                    throw new SSWYException("删除法院信息失败！" + id);
                }
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }

    private ContentValues convertCourt(TCourt court) {
        ContentValues cv = new ContentValues();
        cv.put("c_id", court.getCId());
        cv.put("c_name", court.getCName());
        cv.put("c_jc", court.getCJc());
        cv.put("c_parent_id", court.getCParentId());
        cv.put("c_fjm", court.getCFjm());
        cv.put("c_jb", court.getCJb());
        cv.put("n_order", court.getNOrder());
        cv.put("c_ssfw_url", court.getCSsfwUrl());
        cv.put("c_wap_url", court.getCWapUrl());
        cv.put("n_jsfs", court.getNJsfs());
        return cv;
    }
}
