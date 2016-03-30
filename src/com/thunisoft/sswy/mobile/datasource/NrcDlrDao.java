package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.util.DzfyCryptanalysis;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_代理人 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcDlrDao {

	private static final String TABLE_NAME = "t_layy_dlr";

	/**
	 * 获取前N条数据
	 */
	public static final int TOP_N = 3;

	/**
	 * 获取全部数据
	 */
	public static final int TOP_ALL = 0;

	/**
	 * 根据立案预约基本信息Id，获取相关代理人List
	 * 
	 * @param layyId
	 * @return
	 */
	public List<TLayyDlr> getLayyDlrList(String layyId, int topN) {
		List<TLayyDlr> layyDlrList = new ArrayList<TLayyDlr>();
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, n_dlr_type, n_dlr_dlzl, c_name,");
		sql.append(" c_bdlr_id, c_bdlr_mc, n_idcard_type, c_idcard, c_zyzh,");
		sql.append(" c_szdw, c_sjhm, n_xh from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_layy_id = ?");
		sql.append(" order by n_xh desc");
		if (TOP_ALL != topN) {
			sql.append(" limit 0,").append(topN); // 多取一条，用于判断是否显示查看更多
		}
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { layyId });
			while (cursor.moveToNext()) {
				TLayyDlr layyDlr = buildLayyDlr(cursor);
				layyDlrList.add(layyDlr);
			}
		} finally {
			cursor.close();
		}
		return layyDlrList;
	}

	/**
	 * 根据主键获取 立案预约_代理人
	 * 
	 * @param id
	 * @return
	 */
	public TLayyDlr getLayyDlrById(String id) {
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, n_dlr_type, n_dlr_dlzl, c_name,");
		sql.append(" c_bdlr_id, c_bdlr_mc, n_idcard_type, c_idcard, c_zyzh,");
		sql.append(" c_szdw, c_sjhm from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_id = ?");

		TLayyDlr layyDlr = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { id });
			if (cursor.moveToNext()) {
				layyDlr = buildLayyDlr(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return layyDlr;
	}

	/**
	 * 保存 立案预约_代理人
	 * 
	 * @param dlrList
	 */
	public void saveLayyDlr(List<TLayyDlr> dlrList) {
		DBHelper.beginTransaction();
		if (null != dlrList) {
			TLayyDlr firstDlr = dlrList.get(0);
			List<TLayyDlr> existDlrList = getLayyDlrList(firstDlr.getCLayyId(), 1);
			int xh = 1;
			if (null != existDlrList && existDlrList.size() > 0) {
				TLayyDlr existDlr = existDlrList.get(0);
				xh = existDlr.getNXh() + 1;
			}
			for (TLayyDlr dlr : dlrList) {
				dlr.setNXh(xh);
				DBHelper.insert(TABLE_NAME, convertLayyDlr(dlr));
				xh++;
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 立案预约_代理人
	 * 
	 * @param dlrList
	 */
	public void updateLayyDlr(List<TLayyDlr> dlrList) throws SSWYException {
		DBHelper.beginTransaction();
		if (null != dlrList) {
			String[] params = new String[1];
			for (TLayyDlr dlr : dlrList) {
				params[0] = dlr.getCId();
				DBHelper.update(TABLE_NAME, convertLayyDlr(dlr), "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 或 保存 立案预约_代理人List
	 * 
	 * @param dlrList
	 */
	public void updateOrSaveDlr(List<TLayyDlr> dlrList) {
		DBHelper.beginTransaction();
		if (null != dlrList) {
			String[] params = new String[1];
			for (TLayyDlr dlr : dlrList) {
				params[0] = dlr.getCId();
				if (!DBHelper.update(TABLE_NAME, convertLayyDlr(dlr), "c_id=?", params)) { // 更新失败，插入
					List<TLayyDlr> existDlrList = getLayyDlrList(dlr.getCLayyId(), 1);
					int xh = 1;
					if (null != existDlrList && existDlrList.size() > 0) {
						TLayyDlr existDlr = existDlrList.get(0);
						xh = existDlr.getNXh() + 1;
					}
					dlr.setNXh(xh);
					DBHelper.insert(TABLE_NAME, convertLayyDlr(dlr));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_代理人 主键，删除代理人
	 * 
	 * @param 主键List
	 */
	public void deleteLayyDlr(List<String> dlrIdList) {
		DBHelper.beginTransaction();
		if (null != dlrIdList) {
			String[] params = new String[1];
			for (String id : dlrIdList) {
				params[0] = id;
				DBHelper.delete(TABLE_NAME, "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据 立案预约_id，删除代理人
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteDlrByLayyId(String layyId) {
		DBHelper.beginTransaction();
		String[] params = { layyId };
		DBHelper.delete(TABLE_NAME, "c_layy_id = ?", params);
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据 立案预约_id，删除代理人
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteDlrByLayyIdList(List<String> layyIdList) {
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
	 * 转换 立案预约_代理人
	 * 
	 * @param layyDlr
	 * @return
	 */
	private ContentValues convertLayyDlr(TLayyDlr layyDlr) {
		ContentValues cv = new ContentValues();
		cv.put("c_id", layyDlr.getCId());
		cv.put("c_layy_id", layyDlr.getCLayyId());
		cv.put("n_dlr_type", layyDlr.getNDlrType());
		cv.put("n_dlr_dlzl", layyDlr.getNDlrDlzl());
		
		cv.put("c_name", layyDlr.getCName());
		cv.put("c_bdlr_id", layyDlr.getCBdlrId());
		cv.put("c_bdlr_mc", layyDlr.getCBdlrMc());
		
		cv.put("n_idcard_type", layyDlr.getNIdcardType());
		
		String idcard;
		try {
			idcard = DzfyCryptanalysis.encrypt(layyDlr.getCIdcard());
		} catch (Exception e) {
			idcard = layyDlr.getCIdcard();
		}
		cv.put("c_idcard", idcard);
		cv.put("c_zyzh", layyDlr.getCZyzh());
		cv.put("c_szdw", layyDlr.getCSzdw());
		
		String sjhm;
		try {
			sjhm = DzfyCryptanalysis.encrypt(layyDlr.getCSjhm());
		} catch (Exception e) {
			sjhm = layyDlr.getCSjhm();
		}
		cv.put("c_sjhm", sjhm);
		cv.put("n_xh", layyDlr.getNXh());
		return cv;
	}

	/**
	 * 构造 立案预约_代理人
	 * 
	 * @param cursor
	 * @return
	 */
	private TLayyDlr buildLayyDlr(Cursor cursor) {
		TLayyDlr layyDlr = new TLayyDlr();
		layyDlr.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		layyDlr.setCLayyId(cursor.getString(cursor.getColumnIndex("c_layy_id")));
		String dltType = cursor.getString(cursor.getColumnIndex("n_dlr_type"));
		layyDlr.setNDlrType(NrcUtils.stringToInt(dltType));
		String dlrDlzl = cursor.getString(cursor.getColumnIndex("n_dlr_dlzl"));
		layyDlr.setNDlrDlzl(NrcUtils.stringToInt(dlrDlzl));
		layyDlr.setCName(cursor.getString(cursor.getColumnIndex("c_name")));

		layyDlr.setCBdlrId(cursor.getString(cursor.getColumnIndex("c_bdlr_id")));
		layyDlr.setCBdlrMc(cursor.getString(cursor.getColumnIndex("c_bdlr_mc")));
		String idcardType = cursor.getString(cursor.getColumnIndex("n_idcard_type"));
		layyDlr.setNIdcardType(NrcUtils.stringToInt(idcardType));
		
		String idcard;
		try {
			idcard = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_idcard")));
		} catch (Exception e) {
			idcard = cursor.getString(cursor.getColumnIndex("c_idcard"));
		}
		layyDlr.setCIdcard(idcard);
		layyDlr.setCZyzh(cursor.getString(cursor.getColumnIndex("c_zyzh")));

		layyDlr.setCSzdw(cursor.getString(cursor.getColumnIndex("c_szdw")));
		
		String sjhm;
		try {
			sjhm = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_sjhm")));
		} catch (Exception e) {
			sjhm = cursor.getString(cursor.getColumnIndex("c_sjhm"));
		}
		layyDlr.setCSjhm(sjhm);
		String xh = cursor.getString(cursor.getColumnIndex("n_xh"));
		layyDlr.setNXh(NrcUtils.stringToInt(xh));

		return layyDlr;
	}
}
