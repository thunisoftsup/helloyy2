package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_诉讼材料_附件 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcSsclFjDao {

	public static final String TABLE_NAME = "t_layy_sscl_fj";

	/**
	 * 根据立案预约_诉讼材料Id，获取相关诉讼材料_附件List
	 * 
	 * @param ssclId
	 * @return
	 */
	public List<TLayySsclFj> getSsclFjListBySsclId(String ssclId, int sync) {
		List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
		List<String> paramList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_sscl_id, c_origin_name, c_path,");
		sql.append(" n_xssx, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_sscl_id = ?");
		paramList.add(ssclId);
		if (NrcConstants.SYNC_ALL != sync) {
			sql.append(" and n_sync = ?");
			paramList.add(String.valueOf(sync));
		}
		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TLayySsclFj ssclFj = buildSsclFj(cursor);
				ssclFjList.add(ssclFj);
			}
		} finally {
			cursor.close();
		}
		return ssclFjList;
	}

	/**
	 * 根据主键获取 立案预约_诉讼材料_附件
	 * 
	 * @param id
	 * @return
	 */
	public TLayySsclFj getSsclFjById(String id) {
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_sscl_id, c_origin_name, c_path,");
		sql.append(" n_xssx, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_id = ?");

		TLayySsclFj ssclFj = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { id });
			if (cursor.moveToNext()) {
				ssclFj = buildSsclFj(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return ssclFj;
	}

	/**
	 * 获取所有本地 诉讼材料_附件 数据
	 * 
	 * @return
	 */
	public List<TLayySsclFj> getLocalSsclFjList() {
		List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_sscl_id, c_origin_name, c_path,");
		sql.append(" n_xssx, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" where n_sync = ?");
		Cursor cursor = null;
		try {
			String[] params = { String.valueOf(NrcConstants.SYNC_FALSE) };
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TLayySsclFj ssclFj = buildSsclFj(cursor);
				ssclFjList.add(ssclFj);
			}
		} finally {
			cursor.close();
		}
		return ssclFjList;
	}

	/**
	 * 根据立案预约_诉讼材料Id，获取相关诉讼材料_附件List
	 * 
	 * @param ssclId
	 * @return
	 */
	public List<TLayySsclFj> getSsclFjOneList() {
		List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_sscl_id, c_origin_name, c_path,");
		sql.append(" n_xssx, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" limit 0,1");
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), null);
			while (cursor.moveToNext()) {
				TLayySsclFj ssclFj = buildSsclFj(cursor);
				ssclFjList.add(ssclFj);
			}
		} finally {
			cursor.close();
		}
		return ssclFjList;
	}

	/**
	 * 保存 立案预约_诉讼材料_附件
	 * 
	 * @param ssclFjList
	 */
	public void saveSsclFj(List<TLayySsclFj> ssclFjList) {
		DBHelper.beginTransaction();
		if (null != ssclFjList) {
			for (TLayySsclFj ssclFj : ssclFjList) {
				DBHelper.insert(TABLE_NAME, convertSsclFj(ssclFj));
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 立案预约_诉讼材料_附件
	 * 
	 * @param ssclFjList
	 */
	public void updateSsclFj(List<TLayySsclFj> ssclFjList) {
		DBHelper.beginTransaction();
		if (null != ssclFjList) {
			String[] params = new String[1];
			for (TLayySsclFj ssclFj : ssclFjList) {
				params[0] = ssclFj.getCId();
				DBHelper.update(TABLE_NAME, convertSsclFj(ssclFj), "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 或 插入 立案预约_诉讼材料_附件
	 * 
	 * @param ssclFjList
	 */
	public void updateOrSaveSsclFj(List<TLayySsclFj> ssclFjList) {
		DBHelper.beginTransaction();
		if (null != ssclFjList) {
			String[] params = new String[1];
			for (TLayySsclFj ssclFj : ssclFjList) {
				params[0] = ssclFj.getCId();
				if (!DBHelper.update(TABLE_NAME, convertSsclFj(ssclFj), "c_id=?", params)) { // 更新失败，插入
					DBHelper.insert(TABLE_NAME, convertSsclFj(ssclFj));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_诉讼材料_id，删除诉讼材料_附件
	 * 
	 * @param 诉讼材料_id
	 *            List
	 */
	public void deleteSsclFjBySsclId(List<String> ssclIdList) {
		DBHelper.beginTransaction();
		if (null != ssclIdList && ssclIdList.size() > 0) {
			StringBuffer sbSql = new StringBuffer("c_sscl_id in (");
			for (String ssclId : ssclIdList) {
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
	 * 根据立案预约_诉讼材料_附件 主键，删除诉讼材料_附件
	 * 
	 * @param 主键
	 */
	public void deleteSsclFjById(String id) {
		DBHelper.beginTransaction();
		String[] params = {id};
		DBHelper.delete(TABLE_NAME, "c_id=?", params);
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 转换 立案预约_诉讼材料_附件
	 * 
	 * @param ssclFj
	 * @return
	 */
	private ContentValues convertSsclFj(TLayySsclFj ssclFj) {

		ContentValues cv = new ContentValues();
		cv.put("c_id", ssclFj.getCId());
		cv.put("c_layy_id", ssclFj.getCLayyId());
		cv.put("c_sscl_id", ssclFj.getCSsclId());
		cv.put("c_origin_name", ssclFj.getCOriginName());
		cv.put("c_path", ssclFj.getCPath());

		cv.put("n_xssx", ssclFj.getNXssx());
		cv.put("n_sync", ssclFj.getNSync());

		return cv;
	}

	/**
	 * 构造 立案预约_诉讼材料_附件
	 * 
	 * @param cursor
	 * @return
	 */
	public TLayySsclFj buildSsclFj(Cursor cursor) {

		TLayySsclFj ssclFj = new TLayySsclFj();
		ssclFj.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		ssclFj.setCLayyId(cursor.getString(cursor.getColumnIndex("c_layy_id")));
		ssclFj.setCSsclId(cursor.getString(cursor.getColumnIndex("c_sscl_id")));
		ssclFj.setCOriginName(cursor.getString(cursor.getColumnIndex("c_origin_name")));
		ssclFj.setCPath(cursor.getString(cursor.getColumnIndex("c_path")));

		String xssx = cursor.getString(cursor.getColumnIndex("n_xssx"));
		ssclFj.setNXssx(NrcUtils.stringToInt(xssx));
		ssclFj.setNSync(cursor.getInt(cursor.getColumnIndex("n_sync")));

		return ssclFj;
	}
}
