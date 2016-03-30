package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.util.DzfyCryptanalysis;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 网上立案_基本信息 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class NrcBasicDao {

	private static final String TABLE_NAME = "t_layy";

	public static final int ONEPAGE_SIZE = 10;
	
	@Bean
	LoginCache loginCache;
	
	@Bean
	NrcDsrDao nrcDsrDao;
	
	@Bean
	NrcDlrDao nrcDlrDao;
	
	@Bean
	NrcZrDao nrcZrDao;
	
	@Bean
	NrcSsclDao nrcSsclDao;
	
	@Bean
	NrcZjDao nrcZjDao;
	
	@Bean
	NrcSfrzclDao nrcSfrzclDao;
	
	@Bean
	NrcShDao nrcShDao;

	/**
	 * 获取 所有的 网上立案_基本信息
	 * 
	 * @return List
	 */
	public List<TLayy> getLayyList(int limitEnd) {
		List<TLayy> layyList = new ArrayList<TLayy>();
		StringBuffer sql = new StringBuffer("select c_id, c_court_id, c_court_name,");
		sql.append(" c_ysfy_id, c_ysfy_name, n_ajlx, c_ajlx,");
		sql.append(" n_spcx, c_spcx, n_ajlx, c_ajlx, c_ysaj_bh,");
		sql.append(" c_ysah_nh, c_ysah_jcspzh, n_ajlx, c_ajlx, c_ysah_phh,");
		sql.append(" c_ysah_zphh, c_ssqq, c_aqsm, c_dsr, n_status,");
		sql.append(" n_zctj, d_create, d_update, c_sqbdje, c_sqbdw,");
		sql.append(" c_sqbdxw, n_zxyjlx, c_zxyjlx, c_zxyjwsbh, n_sqr_ly,");
		sql.append(" n_sqr_rzqk, c_sqr_rzqk, n_sqr_sf, c_sqr_sf, c_sqr_id,");
		sql.append(" c_pro_user_id, c_pro_user_name, n_idcard_type, c_idcard, c_sjhm,");
		sql.append(" c_dzyx, c_zyzh, c_lsmc, n_fs, n_fsjg,");
		sql.append(" c_fsjg, c_ywxt_jlid, c_ah, c_ajmc, n_ay,");
		sql.append(" c_ay, n_yjssfje, n_yjf, n_ysa, n_yla,");
		sql.append(" n_ys, c_ywxt_ys_aj_bh, n_xssx, c_flyj, n_sync,");
		sql.append(" c_ysah, c_sqr_name");
		sql.append(" from ").append(TABLE_NAME);
		sql.append(" order by d_create desc limit");
		int limitStart = limitEnd - 10;
		if (limitStart < 0) {
			limitStart = 0;
		}
		sql.append(" ").append(limitStart).append(",").append(limitEnd);
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] {});
			while (cursor.moveToNext()) {
				TLayy layy = buildLayy(cursor);
				layyList.add(layy);
			}
		} finally {
			cursor.close();
		}
		return layyList;
	}

	/**
	 * 获取 所有的 网上立案_基本信息
	 * 
	 * @return List
	 */
	public List<TLayy> getLocalLayyList(int status) {
		List<TLayy> layyList = new ArrayList<TLayy>();
		StringBuffer sql = new StringBuffer("select c_id, c_court_id, c_court_name,");
		sql.append(" c_ysfy_id, c_ysfy_name, n_ajlx, c_ajlx,");
		sql.append(" n_spcx, c_spcx, n_ajlx, c_ajlx, c_ysaj_bh,");
		sql.append(" c_ysah_nh, c_ysah_jcspzh, n_ajlx, c_ajlx, c_ysah_phh,");
		sql.append(" c_ysah_zphh, c_ssqq, c_aqsm, c_dsr, n_status,");
		sql.append(" n_zctj, d_create, d_update, c_sqbdje, c_sqbdw,");
		sql.append(" c_sqbdxw, n_zxyjlx, c_zxyjlx, c_zxyjwsbh, n_sqr_ly,");
		sql.append(" n_sqr_rzqk, c_sqr_rzqk, n_sqr_sf, c_sqr_sf, c_sqr_id,");
		sql.append(" c_pro_user_id, c_pro_user_name, n_idcard_type, c_idcard, c_sjhm,");
		sql.append(" c_dzyx, c_zyzh, c_lsmc, n_fs, n_fsjg,");
		sql.append(" c_fsjg, c_ywxt_jlid, c_ah, c_ajmc, n_ay,");
		sql.append(" c_ay, n_yjssfje, n_yjf, n_ysa, n_yla,");
		sql.append(" n_ys, c_ywxt_ys_aj_bh, n_xssx, c_flyj, n_sync,");
		sql.append(" c_ysah, c_sqr_name");
		sql.append(" from ").append(TABLE_NAME);
		sql.append(" where n_sync = ? and c_pro_user_id = ?");
		List<String> paramList = new ArrayList<String>();
		paramList.add(String.valueOf(NrcConstants.SYNC_FALSE));
		paramList.add(loginCache.getUserId());
		if (NrcUtils.NRC_STATUS_ALL != status) {
			sql.append(" and n_status = ?");
			paramList.add(String.valueOf(status));
		}
		sql.append(" order by d_update desc, d_create desc");
		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				TLayy layy = buildLayy(cursor);
				layyList.add(layy);
			}
		} finally {
			if(null!=cursor){
				cursor.close();
			}
		}
		return layyList;
	}

	/**
	 * 根据主键获取 网上立案_基本信息
	 * 
	 * @param 主键
	 * @return 网上立案_基本信息
	 */
	public TLayy getLayyById(String id) {
		StringBuffer sql = new StringBuffer("select c_id, c_court_id, c_court_name,");
		sql.append(" c_ysfy_id, c_ysfy_name, n_ajlx, c_ajlx,");
		sql.append(" n_spcx, c_spcx, n_ajlx, c_ajlx, c_ysaj_bh,");
		sql.append(" c_ysah_nh, c_ysah_jcspzh, n_ajlx, c_ajlx, c_ysah_phh,");
		sql.append(" c_ysah_zphh, c_ssqq, c_aqsm, c_dsr, n_status,");
		sql.append(" n_zctj, d_create, d_update, c_sqbdje, c_sqbdw,");
		sql.append(" c_sqbdxw, n_zxyjlx, c_zxyjlx, c_zxyjwsbh, n_sqr_ly,");
		sql.append(" n_sqr_rzqk, c_sqr_rzqk, n_sqr_sf, c_sqr_sf, c_sqr_id,");
		sql.append(" c_pro_user_id, c_pro_user_name, n_idcard_type, c_idcard, c_sjhm,");
		sql.append(" c_dzyx, c_zyzh, c_lsmc, n_fs, n_fsjg,");
		sql.append(" c_fsjg, c_ywxt_jlid, c_ah, c_ajmc, n_ay,");
		sql.append(" c_ay, n_yjssfje, n_yjf, n_ysa, n_yla,");
		sql.append(" n_ys, c_ywxt_ys_aj_bh, n_xssx, c_flyj,");
		sql.append(" c_ysah, c_sqr_name, n_sync, c_ysah,");
		sql.append(" c_sqr_name");
		sql.append(" from " + TABLE_NAME + " where c_id = ?");
		TLayy layy = null;
		Cursor cursor = null;
		try {
			cursor = DBHelper.query(sql.toString(), new String[] { id });
			if (cursor.moveToNext()) {
				layy = buildLayy(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return layy;
	}

	/**
	 * 根据当前登录人Id，获取之前创建的 立案预约（审查合格、已立案、申请失败），<br>
	 * 用于查询身份认证_材料的立案预约Id
	 * 
	 * @param 主键
	 * @return 网上立案_基本信息
	 */
	public TLayy getCurrUserLayy(String userId) {
		StringBuffer sql = new StringBuffer("select c_id, c_court_id, c_court_name,");
		sql.append(" c_ysfy_id, c_ysfy_name, n_ajlx, c_ajlx,");
		sql.append(" n_spcx, c_spcx, n_ajlx, c_ajlx, c_ysaj_bh,");
		sql.append(" c_ysah_nh, c_ysah_jcspzh, n_ajlx, c_ajlx, c_ysah_phh,");
		sql.append(" c_ysah_zphh, c_ssqq, c_aqsm, c_dsr, n_status,");
		sql.append(" n_zctj, d_create, d_update, c_sqbdje, c_sqbdw,");
		sql.append(" c_sqbdxw, n_zxyjlx, c_zxyjlx, c_zxyjwsbh, n_sqr_ly,");
		sql.append(" n_sqr_rzqk, c_sqr_rzqk, n_sqr_sf, c_sqr_sf, c_sqr_id,");
		sql.append(" c_pro_user_id, c_pro_user_name, n_idcard_type, c_idcard, c_sjhm,");
		sql.append(" c_dzyx, c_zyzh, c_lsmc, n_fs, n_fsjg,");
		sql.append(" c_fsjg, c_ywxt_jlid, c_ah, c_ajmc, n_ay,");
		sql.append(" c_ay, n_yjssfje, n_yjf, n_ysa, n_yla,");
		sql.append(" n_ys, c_ywxt_ys_aj_bh, n_xssx, c_flyj,");
		sql.append(" c_ysah, c_sqr_name, n_sync, c_ysah,");
		sql.append(" c_sqr_name");
		sql.append(" from " + TABLE_NAME + " where c_pro_user_id = ?");
		sql.append(" and n_status in(?, ?, ?)");
		sql.append(" order by d_create desc limit 0,1");
		TLayy layy = null;
		Cursor cursor = null;
		try {
			String[] params = { userId, String.valueOf(NrcUtils.NRC_STATUS_SCTG), String.valueOf(NrcUtils.NRC_STATUS_YLA), String.valueOf(NrcUtils.NRC_STATUS_SQSB) };
			cursor = DBHelper.query(sql.toString(), params);
			if (cursor.moveToNext()) {
				layy = buildLayy(cursor);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return layy;
	}

	/**
	 * 保存 立案预约
	 * 
	 * @param List
	 */
	public void saveLayy(List<TLayy> layyList) {
		DBHelper.beginTransaction();
		if (null != layyList) {
			for (TLayy layy : layyList) {
				DBHelper.insert(TABLE_NAME, convertLayy(layy));
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 立案预约_基本信息
	 * 
	 * @param List
	 */
	public void updateLayy(List<TLayy> layyList) {
		DBHelper.beginTransaction();
		if (null != layyList) {
			String[] params = new String[1];
			for (TLayy layy : layyList) {
				params[0] = layy.getCId();
				DBHelper.update(TABLE_NAME, convertLayy(layy), "c_id=?", params);
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 更新 或 插入 立案预约_基本信息
	 * 
	 * @param List
	 */
	public void updateOrSaveLayy(List<TLayy> layyList) {
		DBHelper.beginTransaction();
		if (null != layyList) {
			String[] params = new String[1];
			for (TLayy layy : layyList) {
				params[0] = layy.getCId();
				if (!DBHelper.update(TABLE_NAME, convertLayy(layy), "c_id=?", params)) {
					DBHelper.insert(TABLE_NAME, convertLayy(layy));
				}
			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}

	/**
	 * 根据立案预约主键，删除立案预约
	 * 
	 * @param 主键List
	 */
	public void deleteLayy(Context context, String layyId) {
		if (StringUtils.isNotBlank(layyId)) {
			DBHelper.beginTransaction();
			List<String> layyIdList = new ArrayList<String>();
			layyIdList.add(layyId);
			nrcDsrDao.deleteDsrByLayyIdList(layyIdList); //删除 当事人
			nrcDlrDao.deleteDlrByLayyIdList(layyIdList); //删除 代理人
			nrcZrDao.deleteZrByLayyIdList(layyIdList); //删除 证人
			nrcSsclDao.deleteSsclByLayyIdList(context, layyIdList); //删除 诉讼材料
			nrcZjDao.deleteZjByLayyIdList(context, layyIdList); //删除 证据
			nrcSfrzclDao.deleteSfrzClByLayyIdList(context, layyIdList); //删除 身份认证材料
			nrcShDao.deleteShByLayyIdList(layyIdList); // //删除 审核
			String[] params = {layyId};
			DBHelper.delete(TABLE_NAME, "c_id = ?", params);
			DBHelper.setTransactionSuccessful();
			DBHelper.endTransaction();
		}
	}

	/**
	 * 删除掉已和服务器同步过的所有数据
	 */
	public void deleteSyncTrueLayy(Context context) {
		List<String> layyIdList = getSyncTrueLayyId();
		if (null != layyIdList && layyIdList.size() > 0) {
			DBHelper.beginTransaction();
			nrcDsrDao.deleteDsrByLayyIdList(layyIdList); //删除 当事人
			nrcDlrDao.deleteDlrByLayyIdList(layyIdList); //删除 代理人
			nrcZrDao.deleteZrByLayyIdList(layyIdList); //删除 证人
			nrcSsclDao.deleteSsclByLayyIdList(context, layyIdList); //删除 诉讼材料
			nrcZjDao.deleteZjByLayyIdList(context, layyIdList); //删除 证据
			nrcSfrzclDao.deleteSfrzClByLayyIdList(context, layyIdList); //删除 身份认证材料
			nrcShDao.deleteShByLayyIdList(layyIdList); // //删除 审核
			String[] params = {String.valueOf(NrcConstants.SYNC_TRUE)};
			DBHelper.delete(TABLE_NAME, "n_sync = ?", params);
			DBHelper.setTransactionSuccessful();
			DBHelper.endTransaction();
		}
	}
	
	/**
	 * 获取所有已经和服务器同步过的数据
	 */
	public List<String> getSyncTrueLayyId() {
		List<String> idList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select c_id");
		sql.append(" from " + TABLE_NAME + " where n_sync = ?");
		Cursor cursor = null;
		try {
			String[] params = {String.valueOf(NrcConstants.SYNC_TRUE)};
			cursor = DBHelper.query(sql.toString(), params);
			if (cursor.moveToNext()) {
				String id = buildLayyId(cursor);
				idList.add(id);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return idList;
	}

	/**
	 * 转换 立案预约
	 * 
	 * @param 立案预约
	 * @return
	 */
	private ContentValues convertLayy(TLayy layy) {

		ContentValues cv = new ContentValues();
		cv.put("c_id", layy.getCId());
		cv.put("c_court_id", layy.getCCourtId());
		cv.put("c_court_name", layy.getCCourtName());
		cv.put("c_ysfy_id", layy.getCYsfyId());
		
		cv.put("c_ysfy_name", layy.getCYsfyName());
		cv.put("n_ajlx", layy.getNAjlx());
		cv.put("c_ajlx", layy.getCAjlx());
		cv.put("n_spcx", layy.getNSpcx());
		cv.put("c_spcx", layy.getCSpcx());

		cv.put("c_ysaj_bh", layy.getCYsajBh());
		cv.put("c_ysah", layy.getCYsah());
		cv.put("c_ysah_nh", layy.getCYsahNh());
		cv.put("c_ysah_jcspzh", layy.getCYsahJcspzh());
		cv.put("c_ysah_phh", layy.getCYsahPhh());
		cv.put("c_ysah_zphh", layy.getCYsahZphh());
		cv.put("c_ssqq", layy.getCSsqq());
		cv.put("c_aqsm", layy.getCAqsm());
		cv.put("c_dsr", layy.getCDsr());
		cv.put("n_status", layy.getNStatus());
		cv.put("n_zctj", layy.getNZctj());

		cv.put("d_create", layy.getDCreate());
		cv.put("d_update", layy.getDUpdate());
		cv.put("c_sqbdje", layy.getCSqbdje());
		cv.put("c_sqbdw", layy.getCSqbdw());
		cv.put("c_sqbdxw", layy.getCSqbdxw());
		cv.put("n_zxyjlx", layy.getNZxyjlx());
		cv.put("c_zxyjlx", layy.getCZxyjlx());
		cv.put("c_zxyjwsbh", layy.getCZxyjwsbh());
		cv.put("n_sqr_ly", layy.getNSqrLy());
		cv.put("n_sqr_rzqk", layy.getNSqrRzqk());
		cv.put("c_sqr_rzqk", layy.getCSqrRzqk());

		cv.put("n_sqr_sf", layy.getNSqrSf());
		cv.put("c_sqr_sf", layy.getCSqrSf());
		cv.put("c_sqr_id", layy.getCSqrId());
		cv.put("c_sqr_name", layy.getCSqrName());
		cv.put("c_pro_user_id", layy.getCProUserId());
		
		cv.put("c_pro_user_name", layy.getCProUserName());
		cv.put("n_idcard_type", layy.getNIdcardType());
		
		String idcardNum;
        try {
        	idcardNum = DzfyCryptanalysis.encrypt(layy.getCIdcard());
		} catch (Exception e) {
			idcardNum = layy.getCIdcard();
		}
		cv.put("c_idcard", idcardNum);
		
		String sjhm;
		try {
			sjhm = DzfyCryptanalysis.encrypt(layy.getCSjhm());
		} catch (Exception e) {
			sjhm = layy.getCSjhm();
		}
		cv.put("c_sjhm", sjhm);
		cv.put("c_dzyx", layy.getCDzyx());
		cv.put("c_zyzh", layy.getCZyzh());
		cv.put("c_lsmc", layy.getCLsmc());

		cv.put("n_fs", layy.getNFs());
		cv.put("n_fsjg", layy.getNFsjg());
		cv.put("c_fsjg", layy.getCFsjg());
		cv.put("c_ywxt_jlid", layy.getCYwxtJlid());
		
		cv.put("c_ah", layy.getCAh());
		cv.put("c_ajmc", layy.getCAjmc());
		cv.put("n_ay", layy.getNAy());
		cv.put("c_ay", layy.getCAy());
		cv.put("n_yjssfje", layy.getNYjssfje());
		cv.put("n_yjf", layy.getNYjf());
		cv.put("n_ysa", layy.getNYsa());

		cv.put("n_yla", layy.getNYla());
		cv.put("n_ys", layy.getNYs());
		cv.put("c_ywxt_ys_aj_bh", layy.getCYwxtYsAjBh());
		cv.put("n_xssx", layy.getNXxsx());
		cv.put("c_flyj", layy.getCFlyj());

		cv.put("n_sync", layy.getNSync());
		return cv;
	}

	private TLayy buildLayy(Cursor cursor) { //由于手机端 cursor.getInt如果是null的情况，会赋值0，所以使用getString，然后转换Int
		TLayy layy = new TLayy();
		layy.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		layy.setCCourtId(cursor.getString(cursor.getColumnIndex("c_court_id")));
		
		layy.setCCourtName(cursor.getString(cursor.getColumnIndex("c_court_name")));
		
		layy.setCYsfyId(cursor.getString(cursor.getColumnIndex("c_ysfy_id")));
		
		layy.setCYsfyName(cursor.getString(cursor.getColumnIndex("c_ysfy_name")));
		String ajlx = cursor.getString(cursor.getColumnIndex("n_ajlx"));
		layy.setNAjlx(stringToInt(ajlx));
		layy.setCAjlx(cursor.getString(cursor.getColumnIndex("c_ajlx")));
		String spcx = cursor.getString(cursor.getColumnIndex("n_spcx"));
		layy.setNSpcx(stringToInt(spcx));
		layy.setCSpcx(cursor.getString(cursor.getColumnIndex("c_spcx")));

		layy.setCYsajBh(cursor.getString(cursor.getColumnIndex("c_ysaj_bh")));
		layy.setCYsah(cursor.getString(cursor.getColumnIndex("c_ysah")));
		layy.setCYsahNh(cursor.getString(cursor.getColumnIndex("c_ysah_nh")));
		layy.setCYsahJcspzh(cursor.getString(cursor.getColumnIndex("c_ysah_jcspzh")));
		layy.setCYsahPhh(cursor.getString(cursor.getColumnIndex("c_ysah_phh")));
		layy.setCYsahZphh(cursor.getString(cursor.getColumnIndex("c_ysah_zphh")));
		layy.setCSsqq(cursor.getString(cursor.getColumnIndex("c_ssqq")));
		layy.setCAqsm(cursor.getString(cursor.getColumnIndex("c_aqsm")));
		layy.setCDsr(cursor.getString(cursor.getColumnIndex("c_dsr")));
		String status = cursor.getString(cursor.getColumnIndex("n_status"));
		layy.setNStatus(stringToInt(status));
		String zctj = cursor.getString(cursor.getColumnIndex("n_zctj"));
		layy.setNZctj(stringToInt(zctj));

		layy.setDCreate(cursor.getString(cursor.getColumnIndex("d_create")));
		layy.setDUpdate(cursor.getString(cursor.getColumnIndex("d_update")));
		layy.setCSqbdje(cursor.getString(cursor.getColumnIndex("c_sqbdje")));
		layy.setCSqbdw(cursor.getString(cursor.getColumnIndex("c_sqbdw")));
		layy.setCSqbdxw(cursor.getString(cursor.getColumnIndex("c_sqbdxw")));
		String zxyjlx = cursor.getString(cursor.getColumnIndex("n_zxyjlx"));
		layy.setNZxyjlx(stringToInt(zxyjlx));
		layy.setCZxyjlx(cursor.getString(cursor.getColumnIndex("c_zxyjlx")));
		layy.setCZxyjwsbh(cursor.getString(cursor.getColumnIndex("c_zxyjwsbh")));

		String sqrLy = cursor.getString(cursor.getColumnIndex("n_sqr_ly"));
		layy.setNSqrLy(stringToInt(sqrLy));
		String rzqk = cursor.getString(cursor.getColumnIndex("n_sqr_rzqk"));
		layy.setNSqrRzqk(stringToInt(rzqk));
		layy.setCSqrRzqk(cursor.getString(cursor.getColumnIndex("c_sqr_rzqk")));
		String sqrSf = cursor.getString(cursor.getColumnIndex("n_sqr_sf"));
		layy.setNSqrSf(stringToInt(sqrSf));
		layy.setCSqrSf(cursor.getString(cursor.getColumnIndex("c_sqr_sf")));
		layy.setCSqrId(cursor.getString(cursor.getColumnIndex("c_sqr_id")));
		layy.setCSqrName(cursor.getString(cursor.getColumnIndex("c_sqr_name")));

		layy.setCProUserId(cursor.getString(cursor.getColumnIndex("c_pro_user_id")));
		layy.setCProUserName(cursor.getString(cursor.getColumnIndex("c_pro_user_name")));
		String idcardType = cursor.getString(cursor.getColumnIndex("n_idcard_type"));
		layy.setNIdcardType(stringToInt(idcardType));
		
		String idcardNum;
        try {
        	idcardNum = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_idcard")));
		} catch (Exception e) {
			idcardNum = cursor.getString(cursor.getColumnIndex("c_idcard"));
		}
		layy.setCIdcard(idcardNum);
		
		String sjhm;
		try {
			sjhm = DzfyCryptanalysis.decrypt(cursor.getString(cursor.getColumnIndex("c_sjhm")));
		} catch (Exception e) {
			sjhm = cursor.getString(cursor.getColumnIndex("c_sjhm"));
		}
		layy.setCSjhm(sjhm);
		layy.setCDzyx(cursor.getString(cursor.getColumnIndex("c_dzyx")));
		layy.setCZyzh(cursor.getString(cursor.getColumnIndex("c_zyzh")));
		layy.setCLsmc(cursor.getString(cursor.getColumnIndex("c_lsmc")));

		String fs = cursor.getString(cursor.getColumnIndex("n_fs"));
		layy.setNFs(stringToInt(fs));
		String fsjg = cursor.getString(cursor.getColumnIndex("n_fsjg"));
		layy.setNFsjg(stringToInt(fsjg));
		layy.setCFsjg(cursor.getString(cursor.getColumnIndex("c_fsjg")));
		layy.setCYwxtJlid(cursor.getString(cursor.getColumnIndex("c_ywxt_jlid")));
		
		layy.setCAh(cursor.getString(cursor.getColumnIndex("c_ah")));
		layy.setCAjmc(cursor.getString(cursor.getColumnIndex("c_ajmc")));

		String ay = cursor.getString(cursor.getColumnIndex("n_ay"));
		layy.setNAy(stringToInt(ay));
		
		layy.setCAy(cursor.getString(cursor.getColumnIndex("c_ay")));
		
		layy.setNYjssfje(cursor.getString(cursor.getColumnIndex("n_yjssfje")));
		String yjf = cursor.getString(cursor.getColumnIndex("n_yjf"));
		layy.setNYjf(stringToInt(yjf));
		String ysa = cursor.getString(cursor.getColumnIndex("n_ysa"));
		layy.setNYsa(stringToInt(ysa));

		String yla = cursor.getString(cursor.getColumnIndex("n_yla"));
		layy.setNYla(stringToInt(yla));
		String ys = cursor.getString(cursor.getColumnIndex("n_ys"));
		layy.setNYs(stringToInt(ys));
		layy.setCYwxtYsAjBh(cursor.getString(cursor.getColumnIndex("c_ywxt_ys_aj_bh")));
		String xssx = cursor.getString(cursor.getColumnIndex("n_xssx"));
		layy.setNXxsx(stringToInt(xssx));
		layy.setCFlyj(cursor.getString(cursor.getColumnIndex("c_flyj")));

		layy.setNSync(cursor.getInt(cursor.getColumnIndex("n_sync")));

		return layy;

	}
	
	private String buildLayyId(Cursor cursor) {
		return cursor.getString(cursor.getColumnIndex("c_id"));
	}
	
	private Integer stringToInt(String str) {
		if (StringUtils.isNotBlank(str)) {
			try {
				int i = Integer.parseInt(str);
				return i;
			} catch (Exception e) {
				
			}
		}
		return null;
	}
}
