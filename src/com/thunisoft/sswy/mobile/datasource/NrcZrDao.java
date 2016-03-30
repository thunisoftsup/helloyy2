package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.util.DzfyCryptanalysis;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_证人 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcZrDao {

	private static final String TABLE_NAME = "t_layy_zr";

	/**
	 * 获取前N条数据
	 */
	public static final int TOP_N = 3;

	/**
	 * 获取全部数据
	 */
	public static final int TOP_ALL = 0;

	/**
	 * 根据立案预约Id，获取相关证人List
	 * 
	 * @param layyId
	 * @return
	 */
	public List<TLayyZr> getZrListBylayyId(String layyId, int topN) {

		List<TLayyZr> layyZrList = new ArrayList<TLayyZr>();
		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_name, n_xb, n_idcard_type,");
		sql.append(" c_idcard, d_csrq, n_age, c_gzdw, c_sjhm,");
		sql.append(" n_ctzz, c_ylf_id, c_ylf_mc, c_address, c_address_id,");
		sql.append(" c_zsd, c_zsd_id from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_layy_id = ?");
		if (TOP_ALL != topN) {
			sql.append(" limit 0,").append(topN);
		}
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { layyId });
			while (cursor.moveToNext()) {
				TLayyZr layyZr = buildLayyZr(cursor);
				layyZrList.add(layyZr);
			}
		} finally {
			cursor.close();
		}
		return layyZrList;
	}

	/**
	 * 根据主键获取 立案预约_证人
	 * 
	 * @param id
	 * @return
	 */
	public TLayyZr getLayyZrById(String id) {

		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_name, n_xb, n_idcard_type,");
		sql.append(" c_idcard, d_csrq, n_age, c_gzdw, c_sjhm,");
		sql.append(" n_ctzz, c_ylf_id, c_ylf_mc, c_address, c_address_id,");
		sql.append(" c_zsd, c_zsd_id from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_id = ?");

		TLayyZr layyZr = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { id });
			if (cursor.moveToNext()) {
				layyZr = buildLayyZr(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return layyZr;
	}

	/**
	 * 保存 立案预约_证人
	 * 
	 * @param layyZrList
	 */
	public void saveLayyZr(List<TLayyZr> layyZrList) {
		DBHelper.beginTransaction();
		if (null != layyZrList) {
			for (TLayyZr layyzr : layyZrList) {
				DBHelper.insert(TABLE_NAME, convertLayyZr(layyzr));
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 立案预约_证人
	 * 
	 * @param layyZrList
	 */
	public void updateLayyZr(List<TLayyZr> layyZrList) {
		DBHelper.beginTransaction();
		if (null != layyZrList) {
			String[] params = new String[1];
			for (TLayyZr layyzr : layyZrList) {
				params[0] = layyzr.getCId();
				DBHelper.update(TABLE_NAME, convertLayyZr(layyzr), "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 或 保存 立案预约_证人
	 * 
	 * @param zrList
	 */
	public void updateOrSaveZr(List<TLayyZr> zrList) {
		DBHelper.beginTransaction();
		if (null != zrList) {
			String[] params = new String[1];
			for (TLayyZr layyzr : zrList) {
				params[0] = layyzr.getCId();
				if (!DBHelper.update(TABLE_NAME, convertLayyZr(layyzr), "c_id=?", params)) { // 更新失败，插入
					DBHelper.insert(TABLE_NAME, convertLayyZr(layyzr));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_证人 主键，删除证人
	 * 
	 * @param 主键List
	 */
	public void deleteLayyZr(List<String> zrIdList) {
		DBHelper.beginTransaction();
		if (null != zrIdList) {
			String[] params = new String[1];
			for (String id : zrIdList) {
				params[0] = id;
				DBHelper.delete(TABLE_NAME, "c_id = ?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_id，删除证人
	 * 
	 * @param layyId 立案预约_id
	 */
	public void deleteZrByLayyId(String layyId) {
		DBHelper.beginTransaction();
		String[] params = {layyId};
		DBHelper.delete(TABLE_NAME, "c_layy_id = ?", params);
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据 立案预约_id，删除证人
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteZrByLayyIdList(List<String> layyIdList) {
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
	 * 转换 立案预约_证人
	 * 
	 * @param layyZr
	 * @return
	 */
	private ContentValues convertLayyZr(TLayyZr layyZr) {

		ContentValues cv = new ContentValues();

		cv.put("c_id", layyZr.getCId());
		cv.put("c_layy_id", layyZr.getCLayyId());
		cv.put("c_name", layyZr.getCName());
		cv.put("n_xb", layyZr.getNXb());
		cv.put("n_idcard_type", layyZr.getNIdcardType());

		String idcard;
		try {
			idcard = DzfyCryptanalysis.encrypt(layyZr.getCIdcard());
		} catch (Exception e) {
			idcard = layyZr.getCIdcard();
		}
		cv.put("c_idcard", idcard);
		cv.put("d_csrq", layyZr.getDCsrq());
		cv.put("n_age", layyZr.getNAge());
		cv.put("c_gzdw", layyZr.getCGzdw());
		
		String sjhm;
		try {
			sjhm = DzfyCryptanalysis.encrypt(layyZr.getCSjhm());
		} catch (Exception e) {
			sjhm = layyZr.getCSjhm();
		}
		cv.put("c_sjhm", sjhm);

		cv.put("n_ctzz", layyZr.getNCtzz());
		cv.put("c_ylf_id", layyZr.getCYlfId());
		cv.put("c_ylf_mc", layyZr.getCYlfMc());
		
		String address;
		try {
			address = DzfyCryptanalysis.encrypt(layyZr.getCAddress());
		} catch (Exception e) {
			address = layyZr.getCAddress();
		}
		cv.put("c_address", address);
		cv.put("c_address_id", layyZr.getCAddressId());

		String zsd;
		try {
			zsd = DzfyCryptanalysis.encrypt(layyZr.getCZsd());
		} catch (Exception e) {
			zsd = layyZr.getCZsd();
		}
		cv.put("c_zsd", zsd);
		cv.put("c_zsd_id", layyZr.getCZsdId());

		return cv;
	}

	/**
	 * 构造 立案预约_证人
	 * 
	 * @param cursor
	 * @return
	 */
	private TLayyZr buildLayyZr(Cursor cursor) {

		TLayyZr layyZr = new TLayyZr();

		layyZr.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		layyZr.setCLayyId(cursor.getString(cursor.getColumnIndex("c_layy_id")));
		layyZr.setCName(cursor.getString(cursor.getColumnIndex("c_name")));
		String xb = cursor.getString(cursor.getColumnIndex("n_xb"));
		layyZr.setNXb(NrcUtils.stringToInt(xb));
		String idcardType = cursor.getString(cursor.getColumnIndex("n_idcard_type"));
		layyZr.setNIdcardType(NrcUtils.stringToInt(idcardType));

		String idcard;
		try {
			idcard = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_idcard")));
		} catch (Exception e) {
			idcard = cursor.getString(cursor.getColumnIndex("c_idcard"));
		}
		layyZr.setCIdcard(idcard);
		layyZr.setDCsrq(cursor.getString(cursor.getColumnIndex("d_csrq")));
		String age = cursor.getString(cursor.getColumnIndex("n_age"));
		layyZr.setNAge(NrcUtils.stringToInt(age));
		layyZr.setCGzdw(cursor.getString(cursor.getColumnIndex("c_gzdw")));
		
		String sjhm;
		try {
			sjhm = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_sjhm")));
		} catch (Exception e) {
			sjhm = cursor.getString(cursor.getColumnIndex("c_sjhm"));
		}
		layyZr.setCSjhm(sjhm);

		String ctzz = cursor.getString(cursor.getColumnIndex("n_ctzz"));
		layyZr.setNCtzz(NrcUtils.stringToInt(ctzz));
		layyZr.setCYlfId(cursor.getString(cursor.getColumnIndex("c_ylf_id")));
		layyZr.setCYlfMc(cursor.getString(cursor.getColumnIndex("c_ylf_mc")));
		
		String address;
		try {
			address = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_address")));
		} catch (Exception e) {
			address = cursor.getString(cursor.getColumnIndex("c_address"));
		}
		layyZr.setCAddress(address);
		layyZr.setCAddressId(cursor.getString(cursor.getColumnIndex("c_address_id")));

		String zsd;
		try {
			zsd = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_zsd")));
		} catch (Exception e) {
			zsd = cursor.getString(cursor.getColumnIndex("c_zsd"));
		}
		layyZr.setCZsd(zsd);
		layyZr.setCZsdId(cursor.getString(cursor.getColumnIndex("c_zsd_id")));

		return layyZr;
	}
}
