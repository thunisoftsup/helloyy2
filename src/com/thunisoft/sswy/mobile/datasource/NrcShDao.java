package com.thunisoft.sswy.mobile.datasource;

import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TLayySh;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_审核 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcShDao {

	private static final String TABLE_NAME = "t_layy_sh";

	/**
	 * 根据立案预约Id，获取相关审核List
	 * 
	 * @param layyId
	 * @return
	 */
	public TLayySh getShListBylayyId(String layyId) {
		TLayySh layySh = null;
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_shr_id, c_shr_name, d_shsj,");
		sql.append(" c_shyj, n_shjg, n_zctj, n_spcx, c_spcx,");
		sql.append(" n_bhyy, c_bhyy from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_layy_id = ? limit 0,1");
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { layyId });
			if (cursor.moveToNext()) {
				layySh = buildLayySh(cursor);
			}
		} finally {
			cursor.close();
		}
		return layySh;
	}

	/**
	 * 根据主键获取 立案预约_审核
	 * 
	 * @param id
	 * @return
	 */
	public TLayySh getLayyShById(String id) {
		;
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_shr_id, c_shr_name, d_shsj,");
		sql.append(" c_shyj, n_shjg, n_zctj, n_spcx, c_spcx,");
		sql.append(" n_bhyy, c_bhyy from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_id = ?");

		TLayySh layySh = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { id });
			if (cursor.moveToNext()) {
				layySh = buildLayySh(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return layySh;
	}

	/**
	 * 保存 立案预约_审核
	 * 
	 * @param shList
	 */
	public void saveLayySh(List<TLayySh> shList) {
		DBHelper.beginTransaction();
		if (null != shList) {
			for (TLayySh layySh : shList) {
				DBHelper.insert(TABLE_NAME, convertLayySh(layySh));
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 立案预约_审核
	 * 
	 * @param shList
	 */
	public void updateLayySh(List<TLayySh> shList) {
		DBHelper.beginTransaction();
		if (null != shList) {
			String[] params = new String[1];
			for (TLayySh layySh : shList) {
				params[0] = layySh.getCId();
				DBHelper.update(TABLE_NAME, convertLayySh(layySh), "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 或 插入 立案预约_审核
	 * 
	 * @param shList
	 */
	public void updateOrSaveSh(List<TLayySh> shList) {
		DBHelper.beginTransaction();
		if (null != shList && shList.size() > 0) {
			String[] params = new String[1];
			for (TLayySh layySh : shList) {
				params[0] = layySh.getCId();
				if (!DBHelper.update(TABLE_NAME, convertLayySh(layySh), "c_id=?", params)) { // 更新失败，插入新数据
					DBHelper.insert(TABLE_NAME, convertLayySh(layySh));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_审核 主键，删除审核
	 * 
	 * @param 主键List
	 */
	public void deleteLayySh(List<String> layyShIdList) {
		DBHelper.beginTransaction();
		if (null != layyShIdList) {
			String[] params = new String[1];
			for (String id : layyShIdList) {
				params[0] = id;
				DBHelper.delete(TABLE_NAME, "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_id，删除审核
	 * 
	 * @param 主键List
	 */
	public void deleteShByLayyIdList(List<String> layyIdList) {
		if (null != layyIdList && layyIdList.size() > 0) {
			DBHelper.beginTransaction();
			StringBuffer sql = new StringBuffer("");
			sql.append("c_id in (");
			for (String layyId : layyIdList) {
				sql.append("'").append(layyId).append("', ");
			}
			String temp = sql.toString();
			String sqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";
			DBHelper.delete(TABLE_NAME, sqlStr, null);
			DBHelper.setTransactionSuccessful();
			DBHelper.endTransaction();
		}
	}

	/**
	 * 转换 立案预约_审核
	 * 
	 * @param layySh
	 * @return
	 */
	private ContentValues convertLayySh(TLayySh layySh) {

		ContentValues cv = new ContentValues();
		cv.put("c_id", layySh.getCId());
		cv.put("c_layy_id", layySh.getCLayyId());
		cv.put("c_shr_id", layySh.getCShrId());
		cv.put("c_shr_name", layySh.getCShrName());
		cv.put("d_shsj", layySh.getDShsj());

		cv.put("c_shyj", layySh.getCShyj());
		cv.put("n_shjg", layySh.getNShjg());
		cv.put("n_zctj", layySh.getNZctj());
		cv.put("n_spcx", layySh.getNSpcx());
		cv.put("c_spcx", layySh.getCSpcx());

		cv.put("n_bhyy", layySh.getNBhyy());
		cv.put("c_bhyy", layySh.getCBhyy());

		return cv;
	}

	/**
	 * 构造 立案预约_审核
	 * 
	 * @param cursor
	 * @return
	 */
	private TLayySh buildLayySh(Cursor cursor) {

		TLayySh layySh = new TLayySh();
		layySh.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		layySh.setCLayyId(cursor.getString(cursor.getColumnIndex("c_layy_id")));
		layySh.setCShrId(cursor.getString(cursor.getColumnIndex("c_shr_id")));
		layySh.setCShrName(cursor.getString(cursor.getColumnIndex("c_shr_name")));
		layySh.setDShsj(cursor.getString(cursor.getColumnIndex("d_shsj")));

		layySh.setCShyj(cursor.getString(cursor.getColumnIndex("c_shyj")));
		String shjg = cursor.getString(cursor.getColumnIndex("n_shjg"));
		layySh.setNShjg(NrcUtils.stringToInt(shjg));
		String zctj = cursor.getString(cursor.getColumnIndex("n_zctj"));
		layySh.setNZctj(NrcUtils.stringToInt(zctj));
		String spcx = cursor.getString(cursor.getColumnIndex("n_spcx"));
		layySh.setNSpcx(NrcUtils.stringToInt(spcx));
		layySh.setCSpcx(cursor.getString(cursor.getColumnIndex("c_spcx")));

		String bhyy = cursor.getString(cursor.getColumnIndex("n_bhyy"));
		layySh.setNBhyy(NrcUtils.stringToInt(bhyy));
		layySh.setCBhyy(cursor.getString(cursor.getColumnIndex("c_bhyy")));

		return layySh;
	}
}
