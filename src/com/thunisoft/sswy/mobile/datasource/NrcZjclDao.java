package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_证据_材料 dao（操作数据库）
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcZjclDao {

    public static final String TABLE_NAME = "t_zjcl";

    /**
     * 获取前N条数据
     */
    public static final int TOP_N = 3;
    
    /**
     * 获取全部数据
     */
    public static final int TOP_ALL = 0;
    
      /**
     * 根据证据Id，获取相关证据_材料List
     * @param zjId
     * @return
     */
    public List<TZjcl> getZjclListByZjId(String zjId, int topN, int sync) {
        List<TZjcl> zjclList = new ArrayList<TZjcl>();
        List<String> paramList = new ArrayList<String>();
        StringBuffer sql = new StringBuffer("select c_id, c_zj_bh, c_sh_bh, n_sh_type, c_origin_name,");
        sql.append(" c_path, d_create, n_sync from ");
        sql.append(TABLE_NAME);
        sql.append(" where c_zj_bh = ?");
        paramList.add(zjId);
        if (NrcConstants.SYNC_ALL != sync) {
        	sql.append(" and n_sync = ?");
        	paramList.add(String.valueOf(sync));
        }
        if (TOP_ALL != topN) {
        	sql.append(" limit 0,").append(topN);
        }
        Cursor cursor = null;
        try {
        	String[] params = paramList.toArray(new String[paramList.size()]);
            cursor = DBHelper.query(sql.toString(), params);
            while (cursor.moveToNext()) {
            	TZjcl zjcl = buildZjcl(cursor);
            	zjclList.add(zjcl);
            }
        } finally {
            cursor.close();
        }
        return zjclList;
    }
    
    /**
     * 获取所有没有和服务器同步的数据
     * @return
     */
    public List<TZjcl> getLocalZjclList() {
        List<TZjcl> zjclList = new ArrayList<TZjcl>();
        StringBuffer sql = new StringBuffer("select c_id, c_zj_bh, c_sh_bh, n_sh_type, c_origin_name,");
        sql.append(" c_path, d_create, n_sync from ");
        sql.append(TABLE_NAME);
        sql.append(" where n_sync = ?");
        Cursor cursor = null;
        try {
            cursor = DBHelper.query(sql.toString(), new String[] {String.valueOf(NrcConstants.SYNC_FALSE)});
            while (cursor.moveToNext()) {
            	TZjcl zjcl = buildZjcl(cursor);
            	zjclList.add(zjcl);
            }
        } finally {
            cursor.close();
        }
        return zjclList;
    }
    
    /**
     * 根据主键获取 立案预约_证据_材料
     * @param id
     * @return
     */
    public TZjcl getZjclById(String id) {
    	
	    StringBuffer sql = new StringBuffer("select c_id, c_zj_bh, c_sh_bh, n_sh_type, c_origin_name,");
	    sql.append(" c_path, d_create from ");
        sql.append(TABLE_NAME);
        sql.append(" where c_id = ?");
        
        TZjcl zjcl = null;
        Cursor cursor = null;
        try {
            cursor = DBHelper.query(sql.toString(), new String[] { id });
            if (cursor.moveToNext()) {
            	zjcl = buildZjcl(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return zjcl;
    }

    /**
     * 保存 立案预约_证据_材料
     * @param zjclList
     */
    public void saveZjcl(List<TZjcl> zjclList) {
        DBHelper.beginTransaction();
        if (null != zjclList) {
            for (TZjcl zjcl : zjclList) {
                DBHelper.insert(TABLE_NAME, convertZjcl(zjcl));
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }

    /**
     * 更新 立案预约_证据_材料
     * @param zjclList
     */
    public void updateZjcl(List<TZjcl> zjclList) {
        DBHelper.beginTransaction();
        if (null != zjclList) {
        	String[] params = new String[1];
            for (TZjcl zjcl : zjclList) {
            	params[0] = zjcl.getCId();
                DBHelper.update(TABLE_NAME, convertZjcl(zjcl), "c_id=?", params);
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }
    
    /**
     * 更新 或 保存 证据材料List
     * @param zjclList
     */
    public void updateOrSaveZjcl(List<TZjcl> zjclList) {
    	DBHelper.beginTransaction();
        if (null != zjclList) {
        	String[] params = new String[1];
            for (TZjcl zjcl : zjclList) {
            	params[0] = zjcl.getCId();
                if (!DBHelper.update(TABLE_NAME, convertZjcl(zjcl), "c_id=?", params)) { //更新失败，插入
                	DBHelper.insert(TABLE_NAME, convertZjcl(zjcl));
                }
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }
    
    /**
     * 根据立案预约_证据_材料 主键，删除证据_材料
     * @param 主键List
     */
    public void deleteZjcl(List<String> zjIdList) {
    	DBHelper.beginTransaction();
        if (null != zjIdList) {
            String[] params = new String[1];
            for (String id : zjIdList) {
                params[0] = id;
                DBHelper.delete(TABLE_NAME, "c_id=?", params);
            }
        }
    	DBHelper.setTransactionSuccessful();
    	DBHelper.endTransaction();
    }
    
    /**
     * 根据立案预约_证据_id，删除证据_材料
     * @param zjIdList 证据_id List
     */
    public void deleteZjclByZjId(List<String> zjIdList) {
    	DBHelper.beginTransaction();
    	if (null != zjIdList && zjIdList.size() > 0) {
			StringBuffer sbSql = new StringBuffer("c_zj_bh in (");
			for (String ssclId : zjIdList) {
				sbSql.append("'").append(ssclId).append("', ");
			}
			String temp = sbSql.toString();
	        String sqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";
			DBHelper.delete(TABLE_NAME, sqlStr, null);
		}
    	DBHelper.setTransactionSuccessful();
    	DBHelper.endTransaction();
    }
    
    /**
     * 转换 立案预约_证据_材料
     * @param zjcl
     * @return
     */
    private ContentValues convertZjcl(TZjcl zjcl) {
    	
        ContentValues cv = new ContentValues();
        
        cv.put("c_id", zjcl.getCId());
        cv.put("c_zj_bh", zjcl.getCZjBh());
        cv.put("c_sh_bh", zjcl.getCShBh());
        cv.put("n_sh_type", zjcl.getNShType());
        cv.put("c_origin_name", zjcl.getCOriginName());
        
        cv.put("c_path", zjcl.getCPath());
        cv.put("d_create", zjcl.getDCreate());
        cv.put("n_sync", zjcl.getNSync());
       
        return cv;
    }
    
    /**
     * 构造 立案预约_证据_材料
     * @param cursor
     * @return
     */
    public TZjcl buildZjcl(Cursor cursor) {
    	
        TZjcl zjcl = new TZjcl();
        
        zjcl.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
        zjcl.setCZjBh(cursor.getString(cursor.getColumnIndex("c_zj_bh")));
        zjcl.setCShBh(cursor.getString(cursor.getColumnIndex("c_sh_bh")));
        String shType = cursor.getString(cursor.getColumnIndex("n_sh_type"));
        zjcl.setNShType(NrcUtils.stringToInt(shType));
        zjcl.setCOriginName(cursor.getString(cursor.getColumnIndex("c_origin_name")));
        
        zjcl.setCPath(cursor.getString(cursor.getColumnIndex("c_path")));
        zjcl.setDCreate(cursor.getString(cursor.getColumnIndex("d_create")));
        zjcl.setNSync(cursor.getInt(cursor.getColumnIndex("n_sync")));
        
    	return zjcl;
    }
}
