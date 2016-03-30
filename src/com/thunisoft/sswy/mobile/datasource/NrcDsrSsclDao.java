package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;

/**
 * 网上立案_当事人_诉讼材料 dao（操作数据库）
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcDsrSsclDao {

    public static final String TABLE_NAME = "t_layy_dsr_sscl";

   /**
     * 根据立案预约_当事人Id，获取相关当事人_诉讼材料List
     * @param layyId
     * @return
     */
    public List<TLayyDsrSscl> getLayyDsrSsclListByDsrId(String dsrId) {
        List<TLayyDsrSscl> layyDsrSsclList = new ArrayList<TLayyDsrSscl>();
        StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_dsr_id, c_dsr_name, c_sscl_id,");
        sql.append(" c_sscl_name from ");
        sql.append(TABLE_NAME);
        sql.append(" where c_dsr_id = ?");
        Cursor cursor = null;
        try {
            cursor = DBHelper.query(sql.toString(), new String[] {dsrId});
            while (cursor.moveToNext()) {
            	TLayyDsrSscl layyDsrSscl = buildDsrSscl(cursor);
            	layyDsrSsclList.add(layyDsrSscl);
            }
        } finally {
            cursor.close();
        }
        return layyDsrSsclList;
    }
    
    /**
     * 根据主键获取 立案预约_当事人_诉讼材料
     * @param id
     * @return
     */
    public TLayyDsrSscl getLayyDsrSsclById(String id) {;
        StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_dsr_id, c_dsr_name, c_sscl_id,");
        sql.append(" c_sscl_name from ");
        sql.append(TABLE_NAME);
        sql.append(" where c_id = ?");
        
        TLayyDsrSscl layyDsrSscl = null;
        Cursor cursor = null;
        try {
            cursor = DBHelper.query(sql.toString(), new String[] { id });
            if (cursor.moveToNext()) {
            	layyDsrSscl = buildDsrSscl(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return layyDsrSscl;
    }

    /**
     * 根据 立案预约Id，立案预约_诉讼材料Id，获取相关当事人_诉讼材料List
     * @param layyId
     * @param ssclId
     * @return
     */
    public List<TLayyDsrSscl> getDsrSsclListByLayySsclId(String layyId, String ssclId) {
        List<TLayyDsrSscl> dsrSsclList = new ArrayList<TLayyDsrSscl>();
        StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_dsr_id, c_dsr_name, c_sscl_id,");
        sql.append(" c_sscl_name from ");
        sql.append(TABLE_NAME);
        sql.append(" where c_layy_id = ? and c_sscl_id = ?");
        Cursor cursor = null;
        try {
            cursor = DBHelper.query(sql.toString(), new String[] {layyId, ssclId});
            while (cursor.moveToNext()) {
            	TLayyDsrSscl layyDsrSscl = buildDsrSscl(cursor);
            	dsrSsclList.add(layyDsrSscl);
            }
        } finally {
            cursor.close();
        }
        return dsrSsclList;
    }
    
    /**
     * 保存 立案预约_当事人_诉讼材料
     * @param dsrSsclList
     */
    public void saveDsrSscl(List<TLayyDsrSscl> dsrSsclList) {
        DBHelper.beginTransaction();
        if (null != dsrSsclList) {
            for (TLayyDsrSscl dsrSscl : dsrSsclList) {
                DBHelper.insert(TABLE_NAME, convertDsrSscl(dsrSscl));
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }

    /**
     * 更新 立案预约_当事人_诉讼材料
     * @param dsrSsclList
     */
    public void updateDsrSscl(List<TLayyDsrSscl> dsrSsclList) {
        DBHelper.beginTransaction();
        if (null != dsrSsclList) {
        	String[] params = new String[1];
            for (TLayyDsrSscl dsrSscl : dsrSsclList) {
            	params[0] = dsrSscl.getCId();
                DBHelper.update(TABLE_NAME, convertDsrSscl(dsrSscl), "c_id=?", params);
            }
        }
        DBHelper.setTransactionSuccessful();
        DBHelper.endTransaction();
    }
    
    /**
     * 更新 或 插入 立案预约_当事人_诉讼材料
     * @param dsrSsclList
     */
    public void updateOrSaveDsrSscl(List<TLayyDsrSscl> dsrSsclList) {
    	DBHelper.beginTransaction();
    	if (null != dsrSsclList) {
    		String[] params = new String[1];
    		for (TLayyDsrSscl dsrSscl : dsrSsclList) {
    			params[0] = dsrSscl.getCId();
    			if (!DBHelper.update(TABLE_NAME, convertDsrSscl(dsrSscl), "c_id=?", params)) { //更新失败，插入
    				DBHelper.insert(TABLE_NAME, convertDsrSscl(dsrSscl));
    			}
    		}
    	}
    	DBHelper.setTransactionSuccessful();
    	DBHelper.endTransaction();
    }
    
    /**
   	 * 根据诉讼材料id，删除当事人诉讼材料关联
   	 * 
   	 * @param ssclIdList
   	 *            诉讼材料id
   	 */
    public void deleteDsrSsclBySsclIdList(List<String> ssclIdList) {
    	if (null != ssclIdList && ssclIdList.size() > 0) {
    		DBHelper.beginTransaction();
			StringBuffer sbSql = new StringBuffer("c_sscl_id in (");
			for (String ssclId : ssclIdList) {
				sbSql.append("'").append(ssclId).append("', ");
			}
			String temp = sbSql.toString();
	        String sqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";
			DBHelper.delete(TABLE_NAME, sqlStr, null);
			DBHelper.setTransactionSuccessful();
	    	DBHelper.endTransaction();
		}
    }
    
    /**
     * 根据立案预约_当事人_诉讼材料 主键，删除当事人_诉讼材料
     * @param 主键List
     */
    public void deleteDsrSsclById(List<String> dsrSsclIdList) {
    	DBHelper.beginTransaction();
        if (null != dsrSsclIdList) {
            String[] params = new String[1];
            for (String id : dsrSsclIdList) {
                params[0] = id;
                DBHelper.delete(TABLE_NAME, "c_id = ?", params);
            }
        }
    	DBHelper.setTransactionSuccessful();
    	DBHelper.endTransaction();
    }
    
    /**
     * 转换 立案预约_当事人_诉讼材料
     * @param layyDsrSscl
     * @return
     */
    private ContentValues convertDsrSscl(TLayyDsrSscl layyDsrSscl) {
    	
        ContentValues cv = new ContentValues();
        cv.put("c_id", layyDsrSscl.getCId());
        cv.put("c_layy_id", layyDsrSscl.getCLayyId());
        cv.put("c_dsr_id", layyDsrSscl.getCDsrId());
        cv.put("c_dsr_name", layyDsrSscl.getCDsrName());
        cv.put("c_sscl_id", layyDsrSscl.getCSsclId());
        
        cv.put("c_sscl_name", layyDsrSscl.getCSsclName());
       
        return cv;
    }
    
    /**
     * 构造 立案预约_当事人_诉讼材料
     * @param cursor
     * @return
     */
    private TLayyDsrSscl buildDsrSscl(Cursor cursor) {
    	
    	TLayyDsrSscl layyDsrSscl = new TLayyDsrSscl();
    	layyDsrSscl.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
    	layyDsrSscl.setCLayyId(cursor.getString(cursor.getColumnIndex("c_layy_id")));
    	layyDsrSscl.setCDsrId(cursor.getString(cursor.getColumnIndex("c_dsr_id")));
    	layyDsrSscl.setCDsrName(cursor.getString(cursor.getColumnIndex("c_dsr_name")));
    	layyDsrSscl.setCSsclId(cursor.getString(cursor.getColumnIndex("c_sscl_id")));
    	
    	layyDsrSscl.setCSsclName(cursor.getString(cursor.getColumnIndex("c_sscl_name")));
    	
    	
    	return layyDsrSscl;
    }
}
