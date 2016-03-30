package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_证据 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcZjDao {

	private static final String TABLE_NAME = "t_zj";

	/**
	 * 获取前N条数据
	 */
	public static final int TOP_N = 3;

	/**
	 * 获取全部数据
	 */
	public static final int TOP_ALL = 0;

	@Bean
	NrcZjclDao nrcZjclDao;

	/**
	 * 根据立案预约Id，获取相关证据List
	 * 
	 * @param layyId
	 * @return
	 */
	public List<TZj> getZjListBylayyId(String layyId, int topN) {

		List<TZj> zjList = new ArrayList<TZj>();

		StringBuffer sql = new StringBuffer("select c_id, c_ywxt_zjbh, c_xh, c_app_id, c_yw_bh,");
		sql.append(" c_aj_bh, c_ywxt_ajbh, c_name, n_zjlx, c_ssry_id,");
		sql.append(" c_ssry_mc, n_ssry_ssdw, c_zjly, c_zmwt, n_stauts,");
		sql.append(" c_bhyy, n_fs, n_fsjg, d_create, d_update,");
		sql.append(" c_zj_id, c_zzjl_id, c_ywxt_zzjl_id, c_zzjl_shlx from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_yw_bh = ?");
		if (TOP_ALL != topN) {
			sql.append(" limit 0,").append(topN);
		}
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { layyId });
			while (cursor.moveToNext()) {
				TZj zj = buildZj(cursor);
				zjList.add(zj);
			}
		} finally {
			cursor.close();
		}
		return zjList;
	}

	/**
	 * 根据立案预约Id，获取相关证据List
	 * 
	 * @param layyId
	 * @return
	 */
	public List<String> getZjIdList(String layyId) {

		List<String> zjIdList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select c_id from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_yw_bh = ?");
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { layyId });
			while (cursor.moveToNext()) {
				String zjId = buildZjId(cursor);
				zjIdList.add(zjId);
			}
		} finally {
			cursor.close();
		}
		return zjIdList;
	}

	/**
	 * 根据主键获取 立案预约_证据
	 * 
	 * @param id
	 * @return
	 */
	public TZj getZjById(String id) {

		StringBuffer sql = new StringBuffer("select c_id, c_ywxt_zjbh, c_xh, c_app_id, c_yw_bh,");
		sql.append(" c_aj_bh, c_ywxt_ajbh, c_name, n_zjlx, c_ssry_id,");
		sql.append(" c_ssry_mc, n_ssry_ssdw, c_zjly, c_zmwt, n_stauts,");
		sql.append(" c_bhyy, n_fs, n_fsjg, d_create, d_update,");
		sql.append(" c_zj_id, c_zzjl_id, c_ywxt_zzjl_id, c_zzjl_shlx from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_id = ?");

		TZj zj = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { id });
			if (cursor.moveToNext()) {
				zj = buildZj(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return zj;
	}

	/**
	 * 保存 立案预约_证据
	 * 
	 * @param zjList
	 * @throws SSWYException
	 */
	public void saveZj(List<TZj> zjList) {
		DBHelper.beginTransaction();
		if (null != zjList) {
			for (TZj zj : zjList) {
				DBHelper.insert(TABLE_NAME, convertZj(zj));
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 立案预约_证据
	 * 
	 * @param zjList
	 * @throws SSWYException
	 */
	public void updateZj(List<TZj> zjList) {
		DBHelper.beginTransaction();
		if (null != zjList) {
			String[] params = new String[1];
			for (TZj zj : zjList) {
				params[0] = zj.getCId();
				DBHelper.update(TABLE_NAME, convertZj(zj), "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 或 插入 立案预约_证据
	 * 
	 * @param zjList
	 */
	public void updateOrSaveZj(List<TZj> zjList) {
		DBHelper.beginTransaction();
		if (null != zjList) {
			String[] params = new String[1];
			for (TZj zj : zjList) {
				params[0] = zj.getCId();
				if (!DBHelper.update(TABLE_NAME, convertZj(zj), "c_id=?", params)) {
					DBHelper.insert(TABLE_NAME, convertZj(zj));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_证据 主键，删除证据
	 * 
	 * @param 主键List
	 */
	public void deleteZj(List<String> zrIdList) {
		DBHelper.beginTransaction();
		if (null != zrIdList) {
			String[] params = new String[1];
			for (String id : zrIdList) {
				params[0] = id;
				DBHelper.delete(TABLE_NAME, "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_id，删除证据
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteZjByLayyId(String layyId) {
		DBHelper.beginTransaction();
		String[] params = { layyId };
		DBHelper.delete(TABLE_NAME, "c_yw_bh = ?", params);
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_id，删除证据
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteZjByLayyIdList(Context context, List<String> layyIdList) {
		if (null != layyIdList && layyIdList.size() > 0) {
			// 删除证据材料和磁盘文件
			List<String> zjIdList = getZjIdListByLayyIdList(layyIdList); // 获取将要删除证据的id
			List<TZjcl> zjclList = getZjclListByZjIdList(zjIdList, NrcConstants.SYNC_TRUE); // 获取将要删除证据，对应的证据材料
			if (null != zjclList && zjclList.size() > 0) {
				DBHelper.beginTransaction();
				String fileDir = context.getFilesDir().getAbsolutePath();
				Map<String, String> delZjIdMap = new HashMap<String, String>();
				StringBuffer clSql = new StringBuffer("");
				clSql.append("c_id in (");
				for (TZjcl zjcl : zjclList) {
					if (zjcl.getCPath().startsWith(fileDir)) { // 删除源文件、备份文件、图片缩略图
						FileUtils.deleteFile(context, zjcl.getCPath()); // 删除诉讼材料_附件
					} else {
						FileUtils.deleteBackupFile(context, zjcl.getCPath());// 删除备份文件
						FileUtils.deleteThumbFile(context, zjcl.getCPath());// 删除图片缩略图
					}
					clSql.append("'").append(zjcl.getCId()).append("', ");
					delZjIdMap.put(zjcl.getCZjBh(), zjcl.getCZjBh());
				}
				String clTemp = clSql.toString();
				String clSqlStr = clTemp.substring(0, clTemp.lastIndexOf(",")) + ")";
				DBHelper.delete(NrcZjclDao.TABLE_NAME, clSqlStr, null); // 删除所有的证据材料
				
				// 删除证据
				StringBuffer delZjSb = new StringBuffer("");
				delZjSb.append("c_yw_bh in (");
				for (Map.Entry<String, String> entry : delZjIdMap.entrySet()) {
					delZjSb.append("'").append(entry.getKey()).append("', ");
				}
				String tempDelZjSb = delZjSb.toString();
				String delZjSql = tempDelZjSb.substring(0, tempDelZjSb.lastIndexOf(",")) + ")";
				DBHelper.delete(TABLE_NAME, delZjSql, null);
				
				DBHelper.setTransactionSuccessful();
				DBHelper.endTransaction();
			}
		}
	}

	/**
	 * 根据立案预约Id，获取相关证据List
	 * 
	 * @param layyId
	 * @return
	 */
	private List<String> getZjIdListByLayyIdList(List<String> layyIdList) {
		List<String> zjIdList = new ArrayList<String>();
		if (null != layyIdList && layyIdList.size() > 0) {
			// 删除证据
			StringBuffer sql = new StringBuffer("");
			sql.append("select c_id");
			sql.append(" from ").append(TABLE_NAME);
			sql.append(" where c_yw_bh in (");
			for (String layyId : layyIdList) {
				sql.append("'").append(layyId).append("', ");
			}
			String temp = sql.toString();
			String sqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";
			Cursor cursor = null;
			try {
				cursor = DBHelper.query(sqlStr, null);
				if (cursor.moveToNext()) {
					zjIdList.add(buildZjId(cursor));
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return zjIdList;
	}

	public List<TZjcl> getZjclListByZjIdList(List<String> zjIdList, int sync) {
		List<TZjcl> zjclList = new ArrayList<TZjcl>();
		if (null != zjIdList && zjIdList.size() > 0) {
			List<String> paramList = new ArrayList<String>();
			StringBuffer clSql = new StringBuffer("select c_id, c_zj_bh, c_sh_bh, n_sh_type, c_origin_name,");
			clSql.append(" c_path, d_create, n_sync from ");
			clSql.append(NrcZjclDao.TABLE_NAME);
			clSql.append(" where 1=1");
			if (NrcConstants.SYNC_ALL != sync) {
				paramList.add(String.valueOf(sync));
				clSql.append(" and n_sync = ?");
			}
			clSql.append(" and c_zj_bh in (");
			for (String zjId : zjIdList) {
				clSql.append("'").append(zjId).append("', ");
			}
			String clTemp = clSql.toString();
			String clSqlStr = clTemp.substring(0, clTemp.lastIndexOf(",")) + ")";
			Cursor cursor = null;
			try {
				String[] params = paramList.toArray(new String[paramList.size()]);
				cursor = DBHelper.query(clSqlStr, params);
				if (cursor.moveToNext()) {
					zjclList.add(nrcZjclDao.buildZjcl(cursor));
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return zjclList;
	}

	/**
	 * 转换 立案预约_证据
	 * 
	 * @param zj
	 * @return
	 */
	private ContentValues convertZj(TZj zj) {

		ContentValues cv = new ContentValues();

		cv.put("c_id", zj.getCId());
		cv.put("c_ywxt_zjbh", zj.getCYwxtZjbh());
		cv.put("c_xh", zj.getCXh());
		cv.put("c_app_id", zj.getCAppId());
		cv.put("c_yw_bh", zj.getCYwBh());

		cv.put("c_aj_bh", zj.getCAjBh());
		cv.put("c_ywxt_ajbh", zj.getCYwxtAjbh());
		cv.put("c_name", zj.getCName());
		cv.put("n_zjlx", zj.getNZjlx());
		cv.put("c_ssry_id", zj.getCSsryId());

		cv.put("c_ssry_mc", zj.getCSsryMc());
		cv.put("n_ssry_ssdw", zj.getNSsrySsdw());
		cv.put("c_zjly", zj.getCZjly());
		cv.put("c_zmwt", zj.getCZmwt());
		cv.put("n_stauts", zj.getNStauts());

		cv.put("c_bhyy", zj.getCBhyy());
		cv.put("n_fs", zj.getNFs());
		cv.put("n_fsjg", zj.getNFsjg());
		cv.put("d_create", zj.getDCreate());
		cv.put("d_update", zj.getDUpdate());

		cv.put("c_zj_id", zj.getCZjId());
		cv.put("c_zzjl_id", zj.getCZzjlId());
		cv.put("c_ywxt_zzjl_id", zj.getCYwxtZzjlId());
		cv.put("c_zzjl_shlx", zj.getCZzjlShlx());

		return cv;
	}

	/**
	 * 构造 立案预约_证据
	 * 
	 * @param cursor
	 * @return
	 */
	private TZj buildZj(Cursor cursor) {

		TZj zj = new TZj();

		zj.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		zj.setCYwxtZjbh(cursor.getString(cursor.getColumnIndex("c_ywxt_zjbh")));
		zj.setCXh(cursor.getString(cursor.getColumnIndex("c_xh")));
		zj.setCAppId(cursor.getString(cursor.getColumnIndex("c_app_id")));
		zj.setCYwBh(cursor.getString(cursor.getColumnIndex("c_yw_bh")));

		zj.setCAjBh(cursor.getString(cursor.getColumnIndex("c_aj_bh")));
		zj.setCYwxtAjbh(cursor.getString(cursor.getColumnIndex("c_ywxt_ajbh")));
		zj.setCName(cursor.getString(cursor.getColumnIndex("c_name")));
		String zjlx = cursor.getString(cursor.getColumnIndex("n_zjlx"));
		zj.setNZjlx(NrcUtils.stringToInt(zjlx));
		zj.setCSsryId(cursor.getString(cursor.getColumnIndex("c_ssry_id")));

		zj.setCSsryMc(cursor.getString(cursor.getColumnIndex("c_ssry_mc")));
		String ssrySsdw = cursor.getString(cursor.getColumnIndex("n_ssry_ssdw"));
		zj.setNSsrySsdw(NrcUtils.stringToInt(ssrySsdw));
		zj.setCZjly(cursor.getString(cursor.getColumnIndex("c_zjly")));
		zj.setCZmwt(cursor.getString(cursor.getColumnIndex("c_zmwt")));
		String status = cursor.getString(cursor.getColumnIndex("n_stauts"));
		zj.setNStauts(NrcUtils.stringToInt(status));

		zj.setCBhyy(cursor.getString(cursor.getColumnIndex("c_bhyy")));
		String fs = cursor.getString(cursor.getColumnIndex("n_fs"));
		zj.setNFs(NrcUtils.stringToInt(fs));
		String fsjg = cursor.getString(cursor.getColumnIndex("n_fsjg"));
		zj.setNFsjg(NrcUtils.stringToInt(fsjg));
		zj.setDCreate(cursor.getString(cursor.getColumnIndex("d_create")));
		zj.setDUpdate(cursor.getString(cursor.getColumnIndex("d_update")));

		zj.setCZjId(cursor.getString(cursor.getColumnIndex("c_zj_id")));
		zj.setCZzjlId(cursor.getString(cursor.getColumnIndex("c_zzjl_id")));
		zj.setCYwxtZzjlId(cursor.getString(cursor.getColumnIndex("c_ywxt_zzjl_id")));
		zj.setCZzjlShlx(cursor.getString(cursor.getColumnIndex("c_zzjl_shlx")));

		return zj;
	}

	/**
	 * 构造 立案预约_证据
	 * 
	 * @param cursor
	 * @return
	 */
	private String buildZjId(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex("c_id"));
	}
}
