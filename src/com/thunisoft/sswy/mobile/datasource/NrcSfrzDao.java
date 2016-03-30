package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TProUserSfrz;

/**
 * 网上立案_申请人_身份认证 dao（操作数据库）<br>
 * 1.0 服务器端暂时不用，我们也不用
 * @author gewx
 *
 */
@Deprecated
@EBean(scope = Scope.Singleton)
public class NrcSfrzDao {

    private static final String TABLE_NAME = "t_pro_user_sfrz";

    /**
     * 根据 法院Id，申请人Id，获取相关身份认证
     * @param countId
     * @param userId
     * @return
     */
    public List<TProUserSfrz> getUserSfzrByUserId(String countId, String userId) {
        List<TProUserSfrz> sfrzList = new ArrayList<TProUserSfrz>();
        StringBuffer sql = new StringBuffer("select c_bh, c_pro_user_id, c_court_id, c_court_name, c_login,");
        sql.append(" c_name, c_zjlx, c_zjhm, c_phone, n_sczt ");
        sql.append(" c_sczt, c_scyj, d_create, d_update, d_scsj from ");
        sql.append(TABLE_NAME);
        sql.append(" where c_court_id = ? and c_pro_user_id = ?");
        Cursor cursor = null;
        try {
        	String[] params = {countId, userId};
            cursor = DBHelper.query(sql.toString(), params);
            while (cursor.moveToNext()) {
            	TProUserSfrz userSfrz = buildProUserSfrz(cursor);
            	sfrzList.add(userSfrz);
            }
        } finally {
            cursor.close();
        }
        return sfrzList;
    }
    
    /**
     * 更新 或 保存  申请人身份认证List
     * @param sfrzList
     */
    public void updateOrSaveProUserSfrz(List<TProUserSfrz> sfrzList) {
    	DBHelper.beginTransaction();
        if (null != sfrzList) {
        	String[] params = new String[1];
            for (TProUserSfrz sfrz : sfrzList) {
            	params[0] = sfrz.getCBh();
                if (!DBHelper.update(TABLE_NAME, convertProUserSfrz(sfrz), "c_id=?", params)) { //更新失败，插入
                	DBHelper.insert(TABLE_NAME, convertProUserSfrz(sfrz));
                }
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }
    
    /**
     * 根据立案预约_申请人身份认证 主键，删除
     * @param 主键List
     */
    public void deleteProUserSfrz(List<String> sfrzIdList) {
    	DBHelper.beginTransaction();
        if (null != sfrzIdList) {
            String[] params = new String[1];
            for (String id : sfrzIdList) {
                params[0] = id;
                DBHelper.delete(TABLE_NAME, "c_id=?", params);
            }
        }
    	DBHelper.setTransactionSuccessful();
    	DBHelper.endTransaction();
    }
    
    /**
     * 转换 立案预约_申请人身份认证
     * @param sfrz
     * @return
     */
    private ContentValues convertProUserSfrz(TProUserSfrz sfrz) {
        ContentValues cv = new ContentValues();
        cv.put("c_bh", sfrz.getCBh());
        cv.put("c_pro_user_id", sfrz.getCProUserId());
        cv.put("c_court_id", sfrz.getCCourtId());
        cv.put("c_court_name", sfrz.getCCourtName());
        cv.put("c_login", sfrz.getCLogin());
        
        cv.put("c_name", sfrz.getCName());
        cv.put("c_zjlx", sfrz.getCZjlx());
        cv.put("c_zjhm", sfrz.getCZjhm());
        cv.put("c_phone", sfrz.getCPhone());
        cv.put("n_sczt", sfrz.getNSczt());
        
        cv.put("c_sczt", sfrz.getCSczt());
        cv.put("c_scyj", sfrz.getCScyj());
        cv.put("d_create", sfrz.getDCreate());
        cv.put("d_update", sfrz.getDUpdate());
        cv.put("d_scsj", sfrz.getDScsj());
       
        return cv;
    }
    
    /**
     * 构造 立案预约_申请人身份认证
     * @param cursor
     * @return
     */
    private TProUserSfrz buildProUserSfrz(Cursor cursor) {
        TProUserSfrz sfrz = new TProUserSfrz();
        sfrz.setCBh(cursor.getString(cursor.getColumnIndex("c_bh")));
        sfrz.setCProUserId(cursor.getString(cursor.getColumnIndex("c_pro_user_id")));
        sfrz.setCCourtId(cursor.getString(cursor.getColumnIndex("c_court_id")));
        sfrz.setCCourtName(cursor.getString(cursor.getColumnIndex("c_court_name")));
        sfrz.setCLogin(cursor.getString(cursor.getColumnIndex("c_login")));
        
        sfrz.setCName(cursor.getString(cursor.getColumnIndex("c_name")));
        sfrz.setCZjlx(cursor.getString(cursor.getColumnIndex("c_zjlx")));
        sfrz.setCZjhm(cursor.getString(cursor.getColumnIndex("c_zjhm")));
        sfrz.setCPhone(cursor.getString(cursor.getColumnIndex("c_phone")));
        sfrz.setNSczt(cursor.getInt(cursor.getColumnIndex("n_sczt")));
        
        sfrz.setCSczt(cursor.getString(cursor.getColumnIndex("c_sczt")));
        sfrz.setCScyj(cursor.getString(cursor.getColumnIndex("c_scyj")));
        sfrz.setDCreate(cursor.getString(cursor.getColumnIndex("d_create")));
        sfrz.setDUpdate(cursor.getString(cursor.getColumnIndex("d_update")));
        sfrz.setDScsj(cursor.getString(cursor.getColumnIndex("d_scsj")));
        
    	return sfrz;
    }
}
