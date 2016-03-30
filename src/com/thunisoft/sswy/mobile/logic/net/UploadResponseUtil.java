package com.thunisoft.sswy.mobile.logic.net;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import android.util.Log;

import com.thunisoft.sswy.mobile.datasource.NrcSfrzclDao;
import com.thunisoft.sswy.mobile.datasource.NrcSsclFjDao;
import com.thunisoft.sswy.mobile.datasource.NrcZjclDao;
import com.thunisoft.sswy.mobile.exception.SSWYNetworkException;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.SSWYConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;

/**
 * 网上立案 上传 文件
 * @author gewx
 *
 */
@EBean(scope = Scope.Singleton)
public class UploadResponseUtil {
	
    private static final String TAG = UploadResponseUtil.class.getSimpleName();
    
    public static final int TYPE_SSCL = 1;
    
    public static final int TYPE_ZJCL = 2;
    
    public static final int TYPE_SFRZCL = 3;
    
    @Bean
    UploadNetUtils uploadNetUtils;
    
    private int type;
    
    private TLayySsclFj ssclFj;
    
    private TZjcl zjcl;
    
    private TProUserSfrzCl sfrzcl;
    
    @Bean
    NrcSsclFjDao ssclFjDao;
    
    @Bean
    NrcZjclDao zjclDao;
    
    @Bean
    NrcSfrzclDao sfrzclDao;
    
    public BaseResponse getResponse(String url, MultipartEntity entity) {
    	BaseResponse cr = new BaseResponse();
        try {
            String result = uploadNetUtils.post(url, entity);
            if (result != null) {
            	cr.setXtcw(true);
            	JSONObject resJson = new JSONObject(result);
//            	String dataStr = resJson.getString("data");
            	boolean success = resJson.getBoolean("success");
            	cr.setSuccess(success);
            	String message = resJson.getString("message");
                if (success) {
                	switch (type) {
					case TYPE_SSCL:
						ssclFj.setNSync(NrcConstants.SYNC_TRUE);
						List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
						ssclFjList.add(ssclFj);
						ssclFjDao.updateOrSaveSsclFj(ssclFjList);
						break;
					case TYPE_ZJCL:
						zjcl.setNSync(NrcConstants.SYNC_TRUE);
						List<TZjcl> zjclList = new ArrayList<TZjcl>();
						zjclList.add(zjcl);
						zjclDao.updateOrSaveZjcl(zjclList);
						break;
					case TYPE_SFRZCL:
						sfrzcl.setNSync(NrcConstants.SYNC_TRUE);
						List<TProUserSfrzCl> sfrzclList = new ArrayList<TProUserSfrzCl>();
						sfrzclList.add(sfrzcl);
						sfrzclDao.updateOrSaveSfrzcl(sfrzclList);
						break;

					default:
						break;
					}
                } else {
                	cr.setMessage(message);
                }
            } else {
            	cr.setMessage(SSWYConstants.ERROR_NETWORK);
            	cr.setXtcw(false);
            }
        } catch (SSWYNetworkException e) {
            Log.e(TAG, e.getMessage(), e);
            cr.setXtcw(false);
            cr.setSuccess(false);
            cr.setMessage(SSWYConstants.ERROR_NETWORK);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            cr.setXtcw(false);
            cr.setSuccess(false);
            cr.setMessage(SSWYConstants.ERROR_NETWORK);
        }
        return cr;
    }

	/**  
	 * 设置  ssclFj  
	 * @param ssclFj
	 */
	public void setSsclFj(TLayySsclFj ssclFj) {
		this.ssclFj = ssclFj;
	}

	/**  
	 * 设置  zjcl  
	 * @param zjcl
	 */
	public void setZjcl(TZjcl zjcl) {
		this.zjcl = zjcl;
	}

	/**  
	 * 设置  sfrzcl  
	 * @param sfrzcl
	 */
	public void setSfrzcl(TProUserSfrzCl sfrzcl) {
		this.sfrzcl = sfrzcl;
	}

	/**  
	 * 设置  type  
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
	
}
