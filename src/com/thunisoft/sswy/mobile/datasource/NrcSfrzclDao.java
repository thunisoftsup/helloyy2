package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_申请人_身份认证_材料 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcSfrzclDao {

	private static final String TABLE_NAME = "t_pro_user_sfrz_cl";

	/**
	 * 获取当前的 申请人身份认证_材料
	 * 
	 * @param layyId
	 * @return
	 */
	public List<TProUserSfrzCl> getCurrSfrzClList(String userId, String layyId) {
		List<TProUserSfrzCl> sfrzclList = new ArrayList<TProUserSfrzCl>();
		StringBuffer sql = new StringBuffer("select c_bh, c_layy_id, c_pro_user_sfrz_id, c_pro_user_id, c_cl_name,");
		sql.append(" c_cl_path, n_cl_lx, d_create, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_pro_user_id = ?");
		sql.append(" and c_layy_id = ?");
		sql.append(" order by d_create");
		Cursor cursor = null;
		try {
			String[] params = {userId, layyId};
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TProUserSfrzCl sfrzcl = buildSfrzcl(cursor);
				sfrzclList.add(sfrzcl);
			}
		} finally {
			cursor.close();
		}
		return sfrzclList;
	}
	
	/**
	 * 获取本地所有的申请人身份认证材料
	 * 
	 * @return
	 */
	public List<TProUserSfrzCl> getUploadSfrzClList(String layyId, int sync) {
		List<String> paramList = new ArrayList<String>();
		List<TProUserSfrzCl> sfrzclList = new ArrayList<TProUserSfrzCl>();
		StringBuffer sql = new StringBuffer("select c_bh, c_layy_id, c_pro_user_sfrz_id, c_pro_user_id, c_cl_name,");
		sql.append(" c_cl_path, n_cl_lx, d_create, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_layy_id = ?");
		paramList.add(layyId);
		if (NrcConstants.SYNC_ALL != sync) {
			sql.append(" and n_sync = ?");
			paramList.add(String.valueOf(sync));
		}
		sql.append(" order by d_create");
		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TProUserSfrzCl sfrzcl = buildSfrzcl(cursor);
				sfrzclList.add(sfrzcl);
			}
		} finally {
			cursor.close();
		}
		return sfrzclList;
	}
	
	/**
	 * 获取本地所有的申请人身份认证材料
	 * 
	 * @return
	 */
	public List<TProUserSfrzCl> getLocalSfrzClList() {
		List<TProUserSfrzCl> sfrzclList = new ArrayList<TProUserSfrzCl>();
		StringBuffer sql = new StringBuffer("select c_bh, c_layy_id, c_pro_user_sfrz_id, c_pro_user_id, c_cl_name,");
		sql.append(" c_cl_path, n_cl_lx, d_create, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" where n_sync = ?");
		sql.append(" order by d_create desc");
		Cursor cursor = null;
		try {
			String[] params = {String.valueOf(NrcConstants.SYNC_FALSE)};
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TProUserSfrzCl sfrzcl = buildSfrzcl(cursor);
				sfrzclList.add(sfrzcl);
			}
		} finally {
			cursor.close();
		}
		return sfrzclList;
	}
	
	/**
	 * 获取当前的 申请人身份认证_材料
	 * 
	 * @param layyId
	 * @return
	 */
	public List<TProUserSfrzCl> getOneSfrzClList() {
		List<TProUserSfrzCl> sfrzclList = new ArrayList<TProUserSfrzCl>();
		StringBuffer sql = new StringBuffer("select c_bh, c_layy_id, c_pro_user_sfrz_id, c_pro_user_id, c_cl_name,");
		sql.append(" c_cl_path, n_cl_lx, d_create, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" limit 0,1");
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), null);
			while (cursor.moveToNext()) {
				TProUserSfrzCl sfrzcl = buildSfrzcl(cursor);
				sfrzclList.add(sfrzcl);
			}
		} finally {
			cursor.close();
		}
		return sfrzclList;
	}

	/**
	 * 更新 或 插入 申请人身份认证_材料List
	 * 
	 * @param sfrzclList
	 */
	public void updateOrSaveSfrzcl(List<TProUserSfrzCl> sfrzclList) {
		DBHelper.beginTransaction();
		if (null != sfrzclList) {
			String[] params = new String[1];
			for (TProUserSfrzCl sfrzcl : sfrzclList) {
				params[0] = sfrzcl.getCBh();
				if (!DBHelper.update(TABLE_NAME, convertSfrzcl(sfrzcl), "c_bh = ?", params)) {
					DBHelper.insert(TABLE_NAME, convertSfrzcl(sfrzcl));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_申请人身份认证_材料_主键，删除
	 * 
	 * @param 主键List
	 */
	public void deleteSfrzcl(List<String> sfrzclIdList) {
		DBHelper.beginTransaction();
		if (null != sfrzclIdList) {
			String[] params = new String[1];
			for (String id : sfrzclIdList) {
				params[0] = id;
				DBHelper.delete(TABLE_NAME, "c_bh = ?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据 立案预约_id，删除申请人认证材料
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteSfrzclByLayyIdList(List<String> layyIdList) {
		if (null != layyIdList && layyIdList.size() > 0) {
			
			
			
			DBHelper.beginTransaction();
			StringBuffer sql = new StringBuffer("");
			sql.append("c_layy_id in (");
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
	 * 根据立案预约Id，获取身份认证材料List
	 * 
	 * @return
	 */
	public List<TProUserSfrzCl> getSfrzClListByLayyIdList(List<String> layyIdList, int sync) {
		List<TProUserSfrzCl> sfrzclList = new ArrayList<TProUserSfrzCl>();
		List<String> paramList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select c_bh, c_layy_id, c_pro_user_sfrz_id, c_pro_user_id, c_cl_name,");
		sql.append(" c_cl_path, n_cl_lx, d_create, n_sync from ");
		sql.append(TABLE_NAME);
		sql.append(" where 1=1");
		if (NrcConstants.SYNC_ALL != sync) {
			paramList.add(String.valueOf(sync));
			sql.append(" and n_sync = ?");
		}
		sql.append(" and c_layy_id in (");
		for (String layyId : layyIdList) {
			sql.append("'").append(layyId).append("', ");
		}
		String temp = sql.toString();
		String sqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";
		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sqlStr, params);
			while (cursor.moveToNext()) {
				TProUserSfrzCl sfrzcl = buildSfrzcl(cursor);
				sfrzclList.add(sfrzcl);
			}
		} finally {
			cursor.close();
		}
		return sfrzclList;
	}
	
	/**
	 * 根据立案预约Id，删除 身份认证材料
	 * 
	 */
	public void deleteSfrzClByLayyIdList(Context context, List<String> layyIdList) {
		List<TProUserSfrzCl> sfrzclList = getSfrzClListByLayyIdList(layyIdList, NrcConstants.SYNC_TRUE);
		if (null != sfrzclList && sfrzclList.size() > 0) {
			StringBuffer clSql = new StringBuffer("");
			String fileDir = context.getFilesDir().getAbsolutePath();
			clSql.append("c_bh in (");
			for (TProUserSfrzCl sfrzcl : sfrzclList) {
				if (sfrzcl.getCClPath().startsWith(fileDir)) {
					FileUtils.deleteFile(context, sfrzcl.getCClPath());//删除磁盘文件
				} else {
					FileUtils.deleteBackupFile(context, sfrzcl.getCClPath());
					FileUtils.deleteThumbFile(context, sfrzcl.getCClPath());
				}
				clSql.append("'").append(sfrzcl.getCBh()).append("', ");
			}
			String clTemp = clSql.toString();
			String clSqlStr = clTemp.substring(0, clTemp.lastIndexOf(",")) + ")";
			DBHelper.delete(TABLE_NAME, clSqlStr, null); // 删除所有的证据材料
		}
	}
	
	/**
	 * 转换 立案预约_申请人身份认证_材料
	 * 
	 * @param sfrzcl
	 * @return
	 */
	private ContentValues convertSfrzcl(TProUserSfrzCl sfrzcl) {
		ContentValues cv = new ContentValues();
		cv.put("c_bh", sfrzcl.getCBh());
		cv.put("c_layy_id", sfrzcl.getCLayyId());
		cv.put("c_pro_user_sfrz_id", sfrzcl.getCProUserSfrzId());
		cv.put("c_pro_user_id", sfrzcl.getCProUserId());
		cv.put("c_cl_name", sfrzcl.getCClName());

		cv.put("c_cl_path", sfrzcl.getCClPath());
		cv.put("n_cl_lx", sfrzcl.getNClLx());
		cv.put("d_create", sfrzcl.getDCreate());
		cv.put("n_sync", sfrzcl.getNSync());

		return cv;
	}

	/**
	 * 构造 立案预约_申请人_身份认证_材料
	 * 
	 * @param cursor
	 * @return
	 */
	private TProUserSfrzCl buildSfrzcl(Cursor cursor) {
		TProUserSfrzCl sfrzcl = new TProUserSfrzCl();
		sfrzcl.setCBh(cursor.getString(cursor.getColumnIndex("c_bh")));
		sfrzcl.setCLayyId(cursor.getString(cursor.getColumnIndex("c_layy_id")));
		sfrzcl.setCProUserSfrzId(cursor.getString(cursor.getColumnIndex("c_pro_user_sfrz_id")));
		sfrzcl.setCProUserId(cursor.getString(cursor.getColumnIndex("c_pro_user_id")));
		sfrzcl.setCClName(cursor.getString(cursor.getColumnIndex("c_cl_name")));

		sfrzcl.setCClPath(cursor.getString(cursor.getColumnIndex("c_cl_path")));
		String clLx = cursor.getString(cursor.getColumnIndex("n_cl_lx"));
		sfrzcl.setNClLx(NrcUtils.stringToInt(clLx));
		sfrzcl.setDCreate(cursor.getString(cursor.getColumnIndex("d_create")));
		sfrzcl.setNSync(cursor.getInt(cursor.getColumnIndex("n_sync")));

		return sfrzcl;
	}
}
