package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.pojo.TSd;
import com.thunisoft.sswy.mobile.pojo.TSdWrit;

/**
 * 电子送达DAO
 * 
 * @author lulg
 * 
 */
@EBean(scope = Scope.Singleton)
public class SdDao {

    private static final String TABLE_SD = "t_sd";
    private static final String TABLE_SD_WRIT = "t_sd_writ";

    /**
     * 获得送达列表
     * 
     * @return
     */
    public List<TSd> getSdList(int limit) {
        String sql = "select c_id,c_ah,c_writname,c_ay,c_sdrmc,c_sdrts,c_sdrfy,c_sdrbgdh,c_qssj,c_fssj from t_sd order by c_qssj desc";
        if(limit != 0) {
            sql += " limit "+limit;
        }
        Cursor cursor = null;
        List<TSd> sdList = new ArrayList<TSd>();
        try {
            cursor = DBHelper.query(sql, new String[] {});
            while (cursor.moveToNext()) {
                TSd sd = new TSd();
                sd.setCId(cursor.getString(0));
                sd.setCAh(cursor.getString(1));
                sd.setCWritName(cursor.getString(2));
                sd.setCAy(cursor.getString(3));
                sd.setCSdrMc(cursor.getString(4));
                sd.setCSdrTs(cursor.getString(5));
                sd.setCSdrFy(cursor.getString(6));
                sd.setCSdrBgdh(cursor.getString(7));
                sd.setDQssj(cursor.getLong(8));
                sd.setDFssj(cursor.getLong(9));
                sdList.add(sd);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sdList;
    }

    /**
     * 获得送达信息
     * 
     * @param sdId
     * @return
     */
    public TSd getSdInfo(String sdId) {
        String sql = "select c_id,c_ah,c_writname,c_ay,c_sdrmc,c_sdrts,c_sdrfy,c_sdrbgdh,c_qssj,c_fssj from t_sd where c_id=?";
        Cursor cursor = null;
        TSd sd = new TSd();
        try {
            cursor = DBHelper.query(sql, new String[] { sdId });
            while (cursor.moveToNext()) {
                sd.setCId(cursor.getString(0));
                sd.setCAh(cursor.getString(1));
                sd.setCWritName(cursor.getString(2));
                sd.setCAy(cursor.getString(3));
                sd.setCSdrMc(cursor.getString(4));
                sd.setCSdrTs(cursor.getString(5));
                sd.setCSdrFy(cursor.getString(6));
                sd.setCSdrBgdh(cursor.getString(7));
                sd.setDQssj(cursor.getLong(8));
                sd.setDFssj(cursor.getLong(9));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return sd;
    }

    /**
     * 获得指定送达的文书
     * 
     * @param sdId
     * @return
     */
    public List<TSdWrit> getSdWrit(String sdId) {
        String sql = "select c_id,c_sd_id,c_name,c_path from t_sd_writ where c_sd_id=?";
        Cursor cursor = null;
        List<TSdWrit> writList = new ArrayList<TSdWrit>();
        try {
            cursor = DBHelper.query(sql, new String[] { sdId });
            while (cursor.moveToNext()) {
                TSdWrit writ = new TSdWrit();
                writ.setCId(cursor.getString(0));
                writ.setCSdId(cursor.getString(1));
                writ.setCName(cursor.getString(2));
                writ.setCPath(cursor.getString(3));
                writList.add(writ);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return writList;
    }

    /**
     * 保存送达信息
     * 
     * @param sd
     * @param writList
     * @throws SSWYException
     */
    public void saveSd(TSd sd, List<TSdWrit> writList) throws SSWYException {
        DBHelper.beginTransaction();
        if (!DBHelper.insert(TABLE_SD, convertSd(sd))) {
            throw new SSWYException("送达信息存储失败！" + sd.getCId());
        }
        if (writList != null) {
            for (TSdWrit writ : writList) {
                if (!DBHelper.insert(TABLE_SD_WRIT, convertSdWrit(writ))) {
                    throw new SSWYException("送达信息存储失败！" + sd.getCId());
                }
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }
    
    /**
     * 保存送达信息
     * 
     * @param sd
     * @param writList
     * @throws SSWYException
     */
    public void saveSdWrit(List<TSdWrit> writList) throws SSWYException {
        DBHelper.beginTransaction();
        if (writList != null) {
            for (TSdWrit writ : writList) {
                if (!DBHelper.insert(TABLE_SD_WRIT, convertSdWrit(writ))) {
                    throw new SSWYException("送达信息存储失败！"+writ.getCSdId());
                }
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }

    private ContentValues convertSd(TSd sd) {
        ContentValues cv = new ContentValues();
        cv.put("c_id", sd.getCId());
        cv.put("c_ah", sd.getCAh());
        cv.put("c_writname", sd.getCWritName());
        cv.put("c_ay", sd.getCAy());
        cv.put("c_sdrmc", sd.getCSdrMc());
        cv.put("c_sdrts", sd.getCSdrTs());
        cv.put("c_sdrfy", sd.getCSdrFy());
        cv.put("c_sdrbgdh", sd.getCSdrBgdh());
        cv.put("c_qssj", sd.getDQssj());
        cv.put("c_fssj", sd.getDFssj());
        return cv;
    }

    private ContentValues convertSdWrit(TSdWrit writ) {
        ContentValues cv = new ContentValues();
        cv.put("c_id", writ.getCId());
        cv.put("c_sd_id", writ.getCSdId());
        cv.put("c_name", writ.getCName());
        cv.put("c_path", writ.getCPath());
        return cv;
    }
}
