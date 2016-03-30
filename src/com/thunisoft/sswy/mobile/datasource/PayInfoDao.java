package com.thunisoft.sswy.mobile.datasource;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.ContentValues;
import android.database.Cursor;

import com.thunisoft.sswy.mobile.pojo.PayInfo;


/**
 * 中国行政地区 dao（操作数据库）
 * 
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class PayInfoDao {

	public static final String TABLE_NAME = "t_province_new";
	
	
	/**
	 * 
	 * 
	 */
	public List<PayInfo> getPayList(int valid) {
		
		List<PayInfo> regionList = new ArrayList<PayInfo>();
		StringBuffer sql = new StringBuffer(" select c_id, c_jfje, c_jfzh, c_sfzh, c_dzph,c_jffs,n_success ");
			
		sql.append("  from ");
		sql.append(TABLE_NAME);
		sql.append(" where 1=1");
		List<String> paramList = new ArrayList<String>();

		sql.append(" and n_success = ?");
		paramList.add(String.valueOf(valid));

		Cursor cursor = null;
		try {
			String[] params = paramList.toArray(new String[paramList.size()]);
			cursor = DBHelper.query(sql.toString(), params);
			while (cursor.moveToNext()) {
				PayInfo region = buildRegion(cursor);
				regionList.add(region);
			}
		} finally {
			cursor.close();
		}
		return regionList;
	}
	
	/**
	 * 根据立c_id，删除
	 * 
	 * @param layyId 立案预约_id
	 */
	public void deletePayInfo(String cId) {
		DBHelper.beginTransaction();
		String[] params = {cId};
		DBHelper.delete(TABLE_NAME, "c_id = ?", params);
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}
	
	
	/**
	 * 更新 或 保存 
	 * 
	 * @param dsrList
	 */
	public void SavePayInfo(List<PayInfo> payList) {
		DBHelper.beginTransaction();
		if (null != payList) {
			for (PayInfo payInfo : payList) {
				DBHelper.insert(TABLE_NAME, convertLayyDsr(payInfo));

			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}
	
	public void UpdatePayInfo(List<PayInfo> dsrList){
		DBHelper.beginTransaction();
		if (null != dsrList) {
			String[] params = new String[1];
			for (PayInfo payInfo : dsrList) {
				DBHelper.update(TABLE_NAME, convertLayyDsr(payInfo), "c_id=?", params);

			}
		}
		DBHelper.setTransactionSuccessful();
		DBHelper.endTransaction();
	}
	
	
	/**
	 * 转换 立案预约_当事人
	 * 
	 * @param layyDsr
	 * @return
	 */
	private ContentValues convertLayyDsr(PayInfo payInfo) {

		ContentValues cv = new ContentValues();
		cv.put("c_id", payInfo.getCId());
		cv.put("c_jfje", payInfo.getCjfje());
		cv.put("c_jfzh", payInfo.getCjfzh());
		cv.put("c_sfzh", payInfo.getCsfzh());
		cv.put("c_dzph", payInfo.getCdzph());
		cv.put("c_jffs", payInfo.getCjffs());
		cv.put("n_success", payInfo.getCsuccess());
		return cv;
	}
	

	/**
	 * 
	 * 
	 * @param cursor
	 * @return TRegion
	 */
	public PayInfo buildRegion(Cursor cursor) {
		PayInfo payInfo = new PayInfo();
		payInfo.setCId(cursor.getString(cursor.getColumnIndex("c_id")));
		payInfo.setCjfje(cursor.getString(cursor.getColumnIndex("c_jfje")));
		payInfo.setCjfzh(cursor.getString(cursor.getColumnIndex("c_jfzh")));
		payInfo.setCsfzh(cursor.getString(cursor.getColumnIndex("c_sfzh")));
		payInfo.setCdzph(cursor.getString(cursor.getColumnIndex("c_dzph")));
		payInfo.setCjffs(cursor.getString(cursor.getColumnIndex("c_jffs")));
		payInfo.setCsuccess(cursor.getInt(cursor.getColumnIndex("n_success")));
		return payInfo;		
	}
}
