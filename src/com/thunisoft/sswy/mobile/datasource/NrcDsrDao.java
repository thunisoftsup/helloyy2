package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.util.DzfyCryptanalysis;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_当事人 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcDsrDao {

	private static final String TABLE_NAME = "t_layy_dsr";

	/**
	 * 获取前N条数据
	 */
	public static final int TOP_N = 3;

	/**
	 * 获取全部数据
	 */
	public static final int TOP_ALL = 0;

	/**
	 * 根据立案预约基本信息Id，获取相关当事人List前N条数据，<br>
	 * 传入0为获取所有数据
	 * 
	 * @param layyId
	 * @return
	 */
	public List<TLayyDsr> getLayyDsrList(String layyId, String ssdw, int topN) {

		List<TLayyDsr> layyDsrList = new ArrayList<TLayyDsr>();

		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_ywxt_dsrbh, c_ys_rybh, n_xh,");
		sql.append(" c_ssdw, n_type, c_name, n_idcard_type, c_idcard,");
		sql.append(" n_xb, d_csrq, n_age, n_mz, n_zy,");
		sql.append(" c_gzdw, c_sjhm, c_lxdh, c_address, c_address_id,");
		sql.append(" c_zsd, c_zsd_id, n_dwxz, c_zzjgdm, c_dwdz,");
		sql.append(" c_dwdz_id, c_fddbr, c_fddbr_zw, c_fddbr_sjhm, c_fddbr_lxdh");
		sql.append(" from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_layy_id = ? and c_ssdw = ?");
		sql.append(" order by n_xh asc");
		if (TOP_ALL != topN) {
			sql.append(" limit 0,").append(topN + 1); // 多取一条，用于判断是否显示查看更多
		}
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { layyId, ssdw });
			while (cursor.moveToNext()) {
				TLayyDsr layyDsr = buildLayyDsr(cursor);
				layyDsrList.add(layyDsr);
			}
		} finally {
			cursor.close();
		}
		return layyDsrList;
	}

	/**
	 * 根据主键获取 立案预约_当事人
	 * 
	 * @param id
	 * @return
	 */
	public TLayyDsr getLayyDsrById(String id) {

		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_ywxt_dsrbh, c_ys_rybh, n_xh,");
		sql.append(" c_ssdw, n_type, c_name, n_idcard_type, c_idcard,");
		sql.append(" n_xb, d_csrq, n_age, n_mz, n_zy,");
		sql.append(" c_gzdw, c_sjhm, c_lxdh, c_address, c_address_id,");
		sql.append(" c_zsd, c_zsd_id, n_dwxz, c_zzjgdm, c_dwdz,");
		sql.append(" c_dwdz_id, c_fddbr, c_fddbr_zw, c_fddbr_sjhm, c_fddbr_lxdh");
		sql.append(" from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_id = ?");

		TLayyDsr layyDsr = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { id });
			if (cursor.moveToNext()) {
				layyDsr = buildLayyDsr(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return layyDsr;
	}

	/**
	 * 根据当事人主键集合，获取 立案预约_当事人List
	 * 
	 * @param ids
	 * @return
	 */
	public List<TLayyDsr> getDsrListByIdList(String ssdw, List<String> ids) {

		List<TLayyDsr> layyDsrList = new ArrayList<TLayyDsr>();

		if (null == ids || ids.size() == 0) {
			return layyDsrList;
		}

		StringBuffer sql = new StringBuffer("select c_id, c_layy_id, c_ywxt_dsrbh, c_ys_rybh, n_xh,");
		sql.append(" c_ssdw, n_type, c_name, n_idcard_type, c_idcard,");
		sql.append(" n_xb, d_csrq, n_age, n_mz, n_zy,");
		sql.append(" c_gzdw, c_sjhm, c_lxdh, c_address, c_address_id,");
		sql.append(" c_zsd, c_zsd_id, n_dwxz, c_zzjgdm, c_dwdz,");
		sql.append(" c_dwdz_id, c_fddbr, c_fddbr_zw, c_fddbr_sjhm, c_fddbr_lxdh");
		sql.append(" from ");
		sql.append(TABLE_NAME);
		sql.append(" where c_ssdw = ? and c_id in (");

		for (String id : ids) {
			sql.append("'").append(id).append("', ");
		}

		String temp = sql.toString();
		String sqlStr = temp.substring(0, temp.lastIndexOf(",")) + ")";

		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sqlStr, new String[] { ssdw });
			while (cursor.moveToNext()) {
				TLayyDsr layyDsr = buildLayyDsr(cursor);
				layyDsrList.add(layyDsr);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return layyDsrList;
	}

	/**
	 * 保存 立案预约_当事人
	 * 
	 * @param dsrList
	 */
	public void saveLayyDsr(List<TLayyDsr> dsrList) {
		DBHelper.beginTransaction();
		if (null != dsrList && dsrList.size() > 0) {
			TLayyDsr firstDsr = dsrList.get(0);

			List<TLayyDsr> existDsrList = getLayyDsrList(firstDsr.getCLayyId(), firstDsr.getCSsdw(), 1);
			int xh = 1;
			if (null != existDsrList && existDsrList.size() > 0) {
				TLayyDsr existDsr = existDsrList.get(0);
				xh = existDsr.getNXh() + 1;
			}

			for (TLayyDsr dsr : dsrList) {
				dsr.setNXh(xh);
				DBHelper.insert(TABLE_NAME, convertLayyDsr(dsr));
				xh++;
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 立案预约_当事人
	 * 
	 * @param dsrList
	 */
	public void updateLayyDsr(List<TLayyDsr> dsrList) {
		DBHelper.beginTransaction();
		if (null != dsrList) {
			String[] params = new String[1];
			for (TLayyDsr dsr : dsrList) {
				params[0] = dsr.getCId();
				DBHelper.update(TABLE_NAME, convertLayyDsr(dsr), "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 或 保存 当事人List
	 * 
	 * @param dsrList
	 */
	public void updateOrSaveDsr(List<TLayyDsr> dsrList) {
		DBHelper.beginTransaction();
		if (null != dsrList) {
			String[] params = new String[1];
			for (TLayyDsr dsr : dsrList) {
				params[0] = dsr.getCId();
				if (!DBHelper.update(TABLE_NAME, convertLayyDsr(dsr), "c_id=?", params)) { // 更新失败了，插入
					List<TLayyDsr> existDsrList = getLayyDsrList(dsr.getCLayyId(), dsr.getCSsdw(), 1);
					int xh = 1;
					if (null != existDsrList && existDsrList.size() > 0) {
						TLayyDsr existDsr = existDsrList.get(0);
						xh = existDsr.getNXh() + 1;
					}
					dsr.setNXh(xh);
					DBHelper.insert(TABLE_NAME, convertLayyDsr(dsr));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_当事人 主键，删除当事人
	 * 
	 * @param 主键List
	 */
	public void deleteLayyDsr(List<String> dsrIdList) {
		DBHelper.beginTransaction();
		if (null != dsrIdList) {
			String[] params = new String[1];
			for (String id : dsrIdList) {
				params[0] = id;
				DBHelper.delete(TABLE_NAME, "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约_id，删除当事人
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteDsrByLayyId(String layyId) {
		DBHelper.beginTransaction();
		String[] params = { layyId };
		DBHelper.delete(TABLE_NAME, "c_layy_id = ?", params);
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}
	
	/**
	 * 根据立案预约_id，删除当事人
	 * 
	 * @param layyId
	 *            立案预约_id
	 */
	public void deleteDsrByLayyIdList(List<String> layyIdList) {
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
	 * 转换 立案预约_当事人
	 * 
	 * @param layyDsr
	 * @return
	 */
	private ContentValues convertLayyDsr(TLayyDsr layyDsr) {

		ContentValues cv = new ContentValues();
		cv.put("c_id", layyDsr.getCId());
		cv.put("c_layy_id", layyDsr.getCLayyId());
		cv.put("c_ywxt_dsrbh", layyDsr.getCYwxtDsrbh());
		cv.put("c_ys_rybh", layyDsr.getCYsRybh());
		cv.put("n_xh", layyDsr.getNXh());

		cv.put("c_ssdw", layyDsr.getCSsdw());
		cv.put("n_type", layyDsr.getNType());
		cv.put("c_name", layyDsr.getCName());
		cv.put("n_idcard_type", layyDsr.getNIdcardType());
		
		String idcard;
		try {
			idcard = DzfyCryptanalysis.encrypt(layyDsr.getCIdcard());
		} catch (Exception e) {
			idcard = layyDsr.getCIdcard();
		}
		cv.put("c_idcard", idcard);

		cv.put("n_xb", layyDsr.getNXb());
		cv.put("d_csrq", layyDsr.getDCsrq());
		cv.put("n_age", layyDsr.getNAge());
		cv.put("n_mz", layyDsr.getNMz());
		cv.put("n_zy", layyDsr.getNZy());

		cv.put("c_gzdw", layyDsr.getCGzdw());
		
		String sjhm;
		try {
			sjhm = DzfyCryptanalysis.encrypt(layyDsr.getCSjhm());
		} catch (Exception e) {
			sjhm = layyDsr.getCSjhm();
		}
		cv.put("c_sjhm", sjhm);
		cv.put("c_lxdh", layyDsr.getCLxdh());
		
		String address;
		try {
			address = DzfyCryptanalysis.encrypt(layyDsr.getCAddress());
		} catch (Exception e) {
			address = layyDsr.getCAddress();
		}
		cv.put("c_address", address);
		cv.put("c_address_id", layyDsr.getCAddressId());

		String zsd;
		try {
			zsd = DzfyCryptanalysis.encrypt(layyDsr.getCZsd());
		} catch (Exception e) {
			zsd = layyDsr.getCZsd();
		}
		cv.put("c_zsd", zsd);
		cv.put("c_zsd_id", layyDsr.getCZsdId());
		cv.put("n_dwxz", layyDsr.getNDwxz());
		cv.put("c_zzjgdm", layyDsr.getCZzjgdm());
		cv.put("c_dwdz", layyDsr.getCDwdz());

		cv.put("c_dwdz_id", layyDsr.getCDwdzId());
		cv.put("c_fddbr", layyDsr.getCFddbr());
		cv.put("c_fddbr_zw", layyDsr.getCFddbrZw());
		String fddbrSjhm;
		try {
			fddbrSjhm = DzfyCryptanalysis.encrypt(layyDsr.getCFddbrSjhm());
		} catch (Exception e) {
			fddbrSjhm = layyDsr.getCFddbrSjhm();
		}
		cv.put("c_fddbr_sjhm", fddbrSjhm);
		cv.put("c_fddbr_lxdh", layyDsr.getCFddbrLxdh());

		return cv;
	}

	/**
	 * 构造 立案预约_当事人
	 * 
	 * @param cursor
	 * @return
	 */
	private TLayyDsr buildLayyDsr(Cursor cursor) {
		TLayyDsr layyDsr = new TLayyDsr();

		layyDsr.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		layyDsr.setCLayyId(cursor.getString(cursor.getColumnIndex("c_layy_id")));
		layyDsr.setCYwxtDsrbh(cursor.getString(cursor.getColumnIndex("c_ywxt_dsrbh")));
		layyDsr.setCYsRybh(cursor.getString(cursor.getColumnIndex("c_ys_rybh")));
		String xh = cursor.getString(cursor.getColumnIndex("n_xh"));
		layyDsr.setNXh(NrcUtils.stringToInt(xh));

		layyDsr.setCSsdw(cursor.getString(cursor.getColumnIndex("c_ssdw")));
		String type = cursor.getString(cursor.getColumnIndex("n_type"));
		layyDsr.setNType(NrcUtils.stringToInt(type));
		layyDsr.setCName(cursor.getString(cursor.getColumnIndex("c_name")));
		String idcartType = cursor.getString(cursor.getColumnIndex("n_idcard_type"));
		layyDsr.setNIdcardType(NrcUtils.stringToInt(idcartType));
		
		String idcard;
		try {
			idcard = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_idcard")));
		} catch (Exception e) {
			idcard = cursor.getString(cursor.getColumnIndex("c_idcard"));
		}
		layyDsr.setCIdcard(idcard);

		String xb = cursor.getString(cursor.getColumnIndex("n_xb"));
		layyDsr.setNXb(NrcUtils.stringToInt(xb));
		layyDsr.setDCsrq(cursor.getString(cursor.getColumnIndex("d_csrq")));
		String age = cursor.getString(cursor.getColumnIndex("n_age"));
		layyDsr.setNAge(NrcUtils.stringToInt(age));
		String mz = cursor.getString(cursor.getColumnIndex("n_mz"));
		layyDsr.setNMz(NrcUtils.stringToInt(mz));
		String zy = cursor.getString(cursor.getColumnIndex("n_zy"));
		layyDsr.setNZy(NrcUtils.stringToInt(zy));

		layyDsr.setCGzdw(cursor.getString(cursor.getColumnIndex("c_gzdw")));
		
		String sjhm;
		try {
			sjhm = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_sjhm")));
		} catch (Exception e) {
			sjhm = cursor.getString(cursor.getColumnIndex("c_sjhm"));
		}
		layyDsr.setCSjhm(sjhm);
		layyDsr.setCLxdh(cursor.getString(cursor.getColumnIndex("c_lxdh")));
		
		String address;
		try {
			address = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_address")));
		} catch (Exception e) {
			address = cursor.getString(cursor.getColumnIndex("c_address"));
		}
		layyDsr.setCAddress(address);
		layyDsr.setCAddressId(cursor.getString(cursor.getColumnIndex("c_address_id")));

		String zsd;
		try {
			zsd = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_zsd")));
		} catch (Exception e) {
			zsd = cursor.getString(cursor.getColumnIndex("c_zsd"));
		}
		layyDsr.setCZsd(zsd);
		layyDsr.setCZsdId(cursor.getString(cursor.getColumnIndex("c_zsd_id")));
		String dwxz = cursor.getString(cursor.getColumnIndex("n_dwxz"));
		layyDsr.setNDwxz(NrcUtils.stringToInt(dwxz));
		layyDsr.setCZzjgdm(cursor.getString(cursor.getColumnIndex("c_zzjgdm")));
		layyDsr.setCDwdz(cursor.getString(cursor.getColumnIndex("c_dwdz")));

		layyDsr.setCDwdzId(cursor.getString(cursor.getColumnIndex("c_dwdz_id")));
		layyDsr.setCFddbr(cursor.getString(cursor.getColumnIndex("c_fddbr")));
		layyDsr.setCFddbrZw(cursor.getString(cursor.getColumnIndex("c_fddbr_zw")));
		
		String fddbrSjhm;
		try {
			fddbrSjhm = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_fddbr_sjhm")));
		} catch (Exception e) {
			fddbrSjhm = cursor.getString(cursor.getColumnIndex("c_fddbr_sjhm"));
		}
		layyDsr.setCFddbrSjhm(fddbrSjhm);
		layyDsr.setCFddbrLxdh(cursor.getString(cursor.getColumnIndex("c_fddbr_lxdh")));

		return layyDsr;
	}
}
