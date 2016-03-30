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

import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_诉讼材料 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcSsclDao {

	private static final String TABLE_NAME = "t_layy_sscl";

	public static final int TYPE_ALL = -1;

	/**
	 * 获取前N条数据
	 */
	public static final int TOP_N = 3;

	/**
	 * 获取全部数据
	 */
	public static final int TOP_ALL = 0;

	@Bean
	NrcSsclFjDao nrcSsclFjDao;

	/**
	 * 根据立案预约Id，获取相关诉讼材料List
	 * 
	 * @param layyId
	 * @return
	 */
	public List<TLayySscl> getSsclListBylayyId(String layyId, int type, int topN) {
		List<TLayySscl> ssclList = new ArrayList<TLayySscl>();
		List<String> paramList = new ArrayList<String>();
		paramList.add(layyId);
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, n_type, c_name, c_ssry_id,");
		sql.append(" c_ssry_mc, c_ly, c_origin_name, c_path, n_xssx");
		sql.append(" from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_layy_id = ?");
		if (TYPE_ALL != type) {
			paramList.add(String.valueOf(type));
			sql.append(" and n_type = ?");
		}
		sql.append(" order by n_xssx desc");
		if (TOP_ALL != topN) {
			sql.append(" limit 0,").append(topN);
		}
		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TLayySscl layySscl = buildSscl(cursor);
				ssclList.add(layySscl);
			}
		} finally {
			cursor.close();
		}
		return ssclList;
	}

	/**
	 * 根据立案预约Id，获取相关诉讼材料主键List
	 * 
	 * @param layyId
	 * @return
	 */
	public List<String> getSsclIdList(String layyId, int type, int topN) {

		List<String> ssclIdList = new ArrayList<String>();

		StringBuffer sql = new StringBuffer("select c_id");
		sql.append(" from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_layy_id = ? and n_type = ?");
		sql.append(" order by n_xssx desc");
		if (TOP_ALL != topN) {
			sql.append(" limit 0,").append(topN);
		}
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { layyId, String.valueOf(type) });
			while (cursor.moveToNext()) {
				ssclIdList.add(buildSsclId(cursor));
			}
		} finally {
			cursor.close();
		}
		return ssclIdList;
	}

	/**
	 * 根据主键获取 立案预约_诉讼材料
	 * 
	 * @param id
	 * @return
	 */
	public TLayySscl getSsclById(String id) {

		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, n_type, c_name, c_ssry_id,");
		sql.append(" c_ssry_mc, c_ly, c_origin_name, c_path, n_xssx");
		sql.append(" from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_id = ?");

		TLayySscl layySscl = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { id });
			if (cursor.moveToNext()) {
				layySscl = buildSscl(cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return layySscl;
	}

	/**
	 * 保存 立案预约_诉讼材料
	 * 
	 * @param ssclList
	 */
	public void saveSscl(List<TLayySscl> ssclList) {
		DBHelper.beginTransaction();
		if (null != ssclList) {
			TLayySscl firstSscl = ssclList.get(0);
			List<TLayySscl> existSsclList = getSsclListBylayyId(firstSscl.getCLayyId(), firstSscl.getNType(), 1);
			int xxsx = 1;
			if (null != existSsclList && existSsclList.size() > 0) {
				TLayySscl existSscl = existSsclList.get(0);
				xxsx = existSscl.getNXssx() + 1;
			}
			for (TLayySscl layySscl : ssclList) {
				layySscl.setNXssx(xxsx);
				DBHelper.insert(TABLE_NAME, convertSscl(layySscl));
				xxsx++;
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 立案预约_诉讼材料
	 * 
	 * @param ssclList
	 */
	public void updateSscl(List<TLayySscl> ssclList) {
		DBHelper.beginTransaction();
		if (null != ssclList) {
			String[] params = new String[1];
			for (TLayySscl layySscl : ssclList) {
				params[0] = layySscl.getCId();
				DBHelper.update(TABLE_NAME, convertSscl(layySscl), "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 或 插入 立案预约_诉讼材料
	 * 
	 * @param ssclList
	 */
	public void updateOrSaveSscl(List<TLayySscl> ssclList) {
		DBHelper.beginTransaction();
		if (null != ssclList) {
			String[] params = new String[1];
			for (TLayySscl layySscl : ssclList) {
				params[0] = layySscl.getCId();
				boolean success = DBHelper.update(TABLE_NAME, convertSscl(layySscl), "c_id=?", params);
				if (!success) { // 没更新成功说明，不存在，重新插入
					List<TLayySscl> ssclListTemp = new ArrayList<TLayySscl>();
					ssclListTemp.add(layySscl);
					List<TLayySscl> existSsclList = getSsclListBylayyId(layySscl.getCLayyId(), layySscl.getNType(), 1);
					int xxsx = 1;
					if (null != existSsclList && existSsclList.size() > 0) {
						TLayySscl existSscl = existSsclList.get(0);
						xxsx = existSscl.getNXssx() + 1;
					}
					layySscl.setNXssx(xxsx);
					DBHelper.insert(TABLE_NAME, convertSscl(layySscl));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_id和 诉讼材料类型 删除 诉讼材料数据
	 * 
	 * @param layyId
	 *            网上立案_id
	 * @param type
	 *            诉讼材料类型
	 */
	public void deleteSsclByLayyId(String layyId, int type) {
		DBHelper.beginTransaction();
		String[] params = { layyId, String.valueOf(type) };
		DBHelper.delete(TABLE_NAME, "c_layy_id = ? and n_type = ?", params);
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_id，删除诉讼材料
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteSsclByLayyIdList(Context context, List<String> layyIdList) {
		if (null != layyIdList && layyIdList.size() > 0) {
			// 删除证据材料和磁盘文件
			List<String> ssclIdList = getSsclIdListByLayyIdList(layyIdList); // 获取将要删除诉讼材料的id
			List<TLayySsclFj> ssclFjList = getSsclFjListBySsclIdList(ssclIdList, NrcConstants.SYNC_TRUE); // 获取将要删除诉讼材料，对应的附件
			if (null != ssclFjList && ssclFjList.size() > 0) {
				DBHelper.beginTransaction();
				Map<String, String> delSsclIdMap = new HashMap<String, String>();
				String fileDir = context.getFilesDir().getAbsolutePath();
				StringBuffer fjSql = new StringBuffer("");
				fjSql.append("c_id in (");
				for (TLayySsclFj ssclFj : ssclFjList) {
					if (ssclFj.getCPath().startsWith(fileDir)) { // 删除源文件、备份文件、图片缩略图
						FileUtils.deleteFile(context, ssclFj.getCPath()); // 删除诉讼材料_附件
					} else {
						FileUtils.deleteBackupFile(context, ssclFj.getCPath());// 删除备份文件
						FileUtils.deleteThumbFile(context, ssclFj.getCPath());// 删除图片缩略图
					}
					fjSql.append("'").append(ssclFj.getCId()).append("', ");
					delSsclIdMap.put(ssclFj.getCSsclId(), ssclFj.getCSsclId());
				}
				String fjTemp = fjSql.toString();
				String fjSqlStr = fjTemp.substring(0, fjTemp.lastIndexOf(",")) + ")";
				DBHelper.delete(NrcSsclFjDao.TABLE_NAME, fjSqlStr, null); // 删除诉讼材料_附件

				// 删除当事人诉讼材料
				StringBuffer dsrSsclSql = new StringBuffer("c_sscl_id in (");
				for (Map.Entry<String, String> entry : delSsclIdMap.entrySet()) {
					dsrSsclSql.append("'").append(entry.getKey()).append("', ");
				}
				String dsrSscltemp = dsrSsclSql.toString();
				String dsrSsclSqlStr = dsrSscltemp.substring(0, dsrSscltemp.lastIndexOf(",")) + ")";
				DBHelper.delete(NrcDsrSsclDao.TABLE_NAME, dsrSsclSqlStr, null);

				// 删除诉讼材料
				StringBuffer delSsclSb = new StringBuffer("");
				delSsclSb.append("c_id in (");
				for (Map.Entry<String, String> entry : delSsclIdMap.entrySet()) {
					delSsclSb.append("'").append(entry.getKey()).append("', ");
				}
				String tempDelSscl = delSsclSb.toString();
				String delSsclSql = tempDelSscl.substring(0, tempDelSscl.lastIndexOf(",")) + ")";
				DBHelper.delete(TABLE_NAME, delSsclSql, null);
				
				DBHelper.setTransactionSuccessful();
				DBHelper.endTransaction();
			}
		}
	}

	/**
	 * 根据立案预约Id，获取诉讼材料IdList
	 * 
	 * @param layyId
	 * @return
	 */
	private List<String> getSsclIdListByLayyIdList(List<String> layyIdList) {
		List<String> ssclIdList = new ArrayList<String>();
		if (null != layyIdList && layyIdList.size() > 0) {
			// 删除证据
			StringBuffer sql = new StringBuffer("");
			sql.append("select c_id");
			sql.append(" from ").append(TABLE_NAME);
			sql.append(" where c_layy_id in (");
			for (String layyId : layyIdList) {
				sql.append("'").append(layyId).append("', ");
			}
			String temp = sql.toString();
			String sqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";
			Cursor cursor = null;
			try {
				cursor = DBHelper.query(sqlStr, null);
				if (cursor.moveToNext()) {
					ssclIdList.add(buildSsclId(cursor));
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return ssclIdList;
	}

	public List<TLayySsclFj> getSsclFjListBySsclIdList(List<String> ssclIdList, int sync) {
		List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
		if (null != ssclIdList && ssclIdList.size() > 0) {
			List<String> paramList = new ArrayList<String>();
			StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_sscl_id, c_origin_name, c_path,");
			sql.append(" n_xssx, n_sync from ");
			sql.append(NrcSsclFjDao.TABLE_NAME);
			sql.append(" where 1=1");
			if (NrcConstants.SYNC_ALL != sync) {
				paramList.add(String.valueOf(sync));
				sql.append(" and n_sync = ?");
			}
			sql.append(" and c_sscl_id in (");
			for (String zjId : ssclIdList) {
				sql.append("'").append(zjId).append("', ");
			}
			String temp = sql.toString();
			String sqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";
			Cursor cursor = null;
			try {
				String[] params = paramList.toArray(new String[paramList.size()]);
				cursor = DBHelper.query(sqlStr, params);
				if (cursor.moveToNext()) {
					ssclFjList.add(nrcSsclFjDao.buildSsclFj(cursor));
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return ssclFjList;
	}

	/**
	 * 根据立案预约_诉讼材料 主键，删除诉讼材料
	 * 
	 * @param 主键List
	 */
	public void deleteSscl(List<String> layySsclIdList) {
		DBHelper.beginTransaction();
		if (null != layySsclIdList) {
			String[] params = new String[1];
			for (String id : layySsclIdList) {
				params[0] = id;
				DBHelper.delete(TABLE_NAME, "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 转换 立案预约_诉讼材料
	 * 
	 * @param layySscl
	 * @return
	 */
	private ContentValues convertSscl(TLayySscl layySscl) {

		ContentValues cv = new ContentValues();
		cv.put("c_id", layySscl.getCId());
		cv.put("c_layy_id", layySscl.getCLayyId());
		cv.put("n_type", layySscl.getNType());
		cv.put("c_name", layySscl.getCName());
		cv.put("c_ssry_id", layySscl.getCSsryId());

		cv.put("c_ssry_mc", layySscl.getCSsryMc());
		cv.put("c_ly", layySscl.getCLy());
		cv.put("c_origin_name", layySscl.getCOriginName());
		cv.put("c_path", layySscl.getCPath());
		cv.put("n_xssx", layySscl.getNXssx());

		return cv;
	}

	/**
	 * 构造 立案预约_诉讼材料
	 * 
	 * @param cursor
	 * @return
	 */
	private TLayySscl buildSscl(Cursor cursor) {

		TLayySscl layySscl = new TLayySscl();
		layySscl.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		layySscl.setCLayyId(cursor.getString(cursor.getColumnIndex("c_layy_id")));
		String type = cursor.getString(cursor.getColumnIndex("n_type"));
		layySscl.setNType(NrcUtils.stringToInt(type));
		layySscl.setCName(cursor.getString(cursor.getColumnIndex("c_name")));
		layySscl.setCSsryId(cursor.getString(cursor.getColumnIndex("c_ssry_id")));

		layySscl.setCSsryMc(cursor.getString(cursor.getColumnIndex("c_ssry_mc")));
		layySscl.setCLy(cursor.getString(cursor.getColumnIndex("c_ly")));
		layySscl.setCOriginName(cursor.getString(cursor.getColumnIndex("c_origin_name")));
		layySscl.setCPath(cursor.getString(cursor.getColumnIndex("c_path")));
		String xssx = cursor.getString(cursor.getColumnIndex("n_xssx"));
		layySscl.setNXssx(NrcUtils.stringToInt(xssx));

		return layySscl;
	}

	/**
	 * 构造 立案预约_诉讼材料id
	 * 
	 * @param cursor
	 * @return
	 */
	private String buildSsclId(Cursor cursor) {
		String ssclId = cursor.getString(cursor.getColumnIndex("c_id"));
		return ssclId;
	}
}
