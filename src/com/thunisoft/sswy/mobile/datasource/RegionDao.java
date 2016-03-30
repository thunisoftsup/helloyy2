package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TRegion;

/**
 * 中国行政地区 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class RegionDao {

	public static final String TABLE_NAME = "t_regions";

	/**
	 * 全部
	 */
	public static final int VALID_ALL = -1;

	/**
	 * 有效
	 */
	public static final int VALID_FALSE = 2;

	/**
	 * 无效
	 */
	public static final int VALID_TRUE = 1;
	
	/**
	 * 行政级别 全部
	 */
	public static final String JB_ALL = "1";
	
	/**
	 * 行政级别 省
	 */
	public static final String JB_PROVINCE = "2";

	/**
	 * 行政级别 市
	 */
	public static final String JB_CITY = "3";

	/**
	 * 行政级别 区
	 */
	public static final String JB_AREA = "4";
	
	
	/**
	 * 根据是否有效、级别  获取 行政地区
	 * 
	 * @param valid
	 *            全部、有效、无效
	 * @param cJb 级别
	 * @return List[TRegion]
	 */
	public List<TRegion> getRegionList(int valid, String cJb) {
		List<TRegion> regionList = new ArrayList<TRegion>();
		StringBuffer sql = new StringBuffer("select c_id, c_parent_id, c_fjm, c_jb, c_name,");
		sql.append(" n_order, n_valid from ");
		sql.append(TABLE_NAME);
		sql.append(" where 1=1");
		List<String> paramList = new ArrayList<String>();
		if (VALID_ALL != valid) {
			sql.append(" and n_valid = ?");
			paramList.add(String.valueOf(valid));
		}
		if (!JB_ALL.equals(cJb)) {
			sql.append(" and c_jb = ?");
			paramList.add(String.valueOf(cJb));
		}
		if (!JB_ALL.equals(cJb)) {
			sql.append(" and c_jb = ?");
			paramList.add(String.valueOf(cJb));
		}
		sql.append(" order by c_jb asc, n_order asc");
		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TRegion region = buildRegion(cursor);
				regionList.add(region);
			}
		} finally {
			cursor.close();
		}
		return regionList;
	}
	
	/**
	 * 根据是否有效、级别  、父节点 获取 行政地区
	 * 
	 * @param valid
	 *            全部、有效、无效
	 * @param cJb 级别
	 * @return List[TRegion]
	 */
	public List<TRegion> getRegionListByPId(int valid, List<String> pidList) {
		List<TRegion> regionList = new ArrayList<TRegion>();
		StringBuffer sql = new StringBuffer("select c_id, c_parent_id, c_fjm, c_jb, c_name,");
		sql.append(" n_order, n_valid from ");
		sql.append(TABLE_NAME);
		sql.append(" where 1=1");
		List<String> paramList = new ArrayList<String>();
		if (VALID_ALL != valid) {
			sql.append(" and n_valid = ?");
			paramList.add(String.valueOf(valid));
		}
		if (null != pidList && pidList.size() > 0) {
			StringBuffer inSql = new StringBuffer("");
			inSql.append(" and c_parent_id in (");
			for (String pid : pidList) {
				inSql.append("'").append(pid).append("', ");
			}
			String temp = inSql.toString();
			String inSqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";
			sql.append(inSqlStr);
		}
		sql.append(" order by c_jb asc, n_order asc");
		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TRegion region = buildRegion(cursor);
				regionList.add(region);
			}
		} finally {
			cursor.close();
		}
		return regionList;
	}

	/**
	 * 根据是否有效获取 行政地区
	 * 
	 * @param valid
	 *            全部、有效、无效
	 * @return List[TRegion]
	 */
	public List<TRegion> getRegionList(int valid) {
		List<TRegion> regionList = new ArrayList<TRegion>();
		StringBuffer sql = new StringBuffer("select c_id, c_parent_id, c_fjm, c_jb, c_name,");
		sql.append(" n_order, n_valid from ");
		sql.append(TABLE_NAME);
		sql.append(" where 1=1");
		List<String> paramList = new ArrayList<String>();
		if (VALID_ALL != valid) {
			sql.append(" and n_valid = ?");
			paramList.add(String.valueOf(valid));
		}
		sql.append(" order by c_jb asc, n_order asc");
		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TRegion region = buildRegion(cursor);
				regionList.add(region);
			}
		} finally {
			cursor.close();
		}
		return regionList;
	}
	
	/**
	 * 根据主键获取行政地区
	 * 
	 * @param id 主键
	 * @return TRegion
	 */
	public TRegion getRegionById(String id) {
		StringBuffer sql = new StringBuffer("select c_id, c_parent_id, c_fjm, c_jb, c_name,");
		sql.append(" n_order, n_valid from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_id = ?");
		TRegion region = null;
		Cursor cursor = null;
		try {
			String[] params = {id};
			cursor = DBHelper.query(sql.toString(), params);
			if (cursor.moveToNext()) {
				region = buildRegion(cursor);
			}
		} finally {
			cursor.close();
		}
		return region;
	}
	
	/**
	 * 保存 行政地区
	 * 
	 * @param List[TRegion]
	 */
	public void saveSsclFj(List<TRegion> regionList) {
		if (null != regionList && regionList.size() > 0) {
			DBHelper.beginTransaction();
			for (TRegion region : regionList) {
				DBHelper.insert(TABLE_NAME, convertRegion(region));
			}
			DBHelper.setTransactionSuccessful();
			DBHelper.endTransaction();
		}
	}

	/**
	 * 清空表
	 * 
	 */
	public void clearTable() {
		DBHelper.beginTransaction();
		DBHelper.clearTable(TABLE_NAME);
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 转换 行政地区
	 * 
	 * @param region
	 * @return
	 */
	private ContentValues convertRegion(TRegion region) {

		ContentValues cv = new ContentValues();
		cv.put("c_id", region.getCId());
		cv.put("c_parent_id", region.getCParentId());
		cv.put("c_fjm", region.getCFjm());
		cv.put("c_jb", region.getCJb());
		cv.put("c_name", region.getCName());

		cv.put("n_order", region.getNOrder());
		cv.put("n_valid", region.getNValid());

		return cv;
	}

	/**
	 * 构造 行政地区
	 * 
	 * @param cursor
	 * @return TRegion
	 */
	public TRegion buildRegion(Cursor cursor) {
		TRegion region = new TRegion();
		region.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		region.setCParentId(cursor.getString(cursor.getColumnIndex("c_parent_id")));
		region.setCFjm(cursor.getString(cursor.getColumnIndex("c_fjm")));
		region.setCJb(cursor.getString(cursor.getColumnIndex("c_jb")));
		region.setCName(cursor.getString(cursor.getColumnIndex("c_name")));
		
		region.setNOrder(cursor.getInt(cursor.getColumnIndex("n_order")));
		region.setNValid(cursor.getInt(cursor.getColumnIndex("n_valid")));
		return region;
	}
}
