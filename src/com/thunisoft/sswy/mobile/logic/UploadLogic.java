package com.thunisoft.sswy.mobile.logic;

import java.io.File;
import java.nio.charset.Charset;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.util.Log;

import com.thunisoft.sswy.mobile.datasource.NrcSsclDao;
import com.thunisoft.sswy.mobile.logic.net.UploadNetUtils;
import com.thunisoft.sswy.mobile.logic.net.UploadResponseUtil;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.SSWYConstants;

/**
 * 网上立案 上传文件
 * 
 * @author gewx
 * 
 */	
@EBean(scope = Scope.Singleton)
public class UploadLogic {

	private static final String TAG = UploadLogic.class.getSimpleName();

	@Bean
	UploadNetUtils uploadNetUtils;

	@Bean
	UploadResponseUtil responseUtil;

	@Bean
	NrcSsclDao nrcSsclDao;
	
	/**
	 * 上传诉讼材料附件
	 * 
	 * @return
	 */
	public BaseResponse uploadSscl(TLayySscl sscl, TLayySsclFj ssclFj) {
		try {
			String url = uploadNetUtils.getMainAddress() + "/api/wsla/uploadSscl";
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
			entity.addPart("layyId", new StringBody(sscl.getCLayyId(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclId", new StringBody(sscl.getCId(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclMc", new StringBody(sscl.getCName(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclSsrId", new StringBody(sscl.getCSsryId(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclSsrMc", new StringBody(sscl.getCSsryMc(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("type", new StringBody(sscl.getNType().toString(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclFjId", new StringBody(ssclFj.getCId(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("clName", new StringBody(ssclFj.getCOriginName(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			File file = new File(ssclFj.getCPath());
			FileBody fileBody = new FileBody(file);
			entity.addPart("file", fileBody);
			responseUtil.setSsclFj(ssclFj);
			responseUtil.setType(UploadResponseUtil.TYPE_SSCL);
			return responseUtil.getResponse(url, entity);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			BaseResponse response = new BaseResponse();
			response.setXtcw(false);
			response.setMessage(SSWYConstants.ERROR_NETWORK);
			return response;
		}
	}
	
	/**
	 * 上传证据材料
	 * 
	 * @return
	 */
	public BaseResponse uploadZj(TZj zj, TZjcl zjcl) {
		try {
			String url = uploadNetUtils.getMainAddress() + "/api/wsla/uploadSscl";
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
			entity.addPart("layyId", new StringBody(zj.getCYwBh(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclId", new StringBody(zj.getCId(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclMc", new StringBody(zj.getCName(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclSsrId", new StringBody(zj.getCSsryId(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclSsrMc", new StringBody(zj.getCSsryMc(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("type", new StringBody(String.valueOf(3), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("ssclFjId", new StringBody(zjcl.getCId(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("clName", new StringBody(zjcl.getCOriginName(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			File file = new File(zjcl.getCPath());
			FileBody fileBody = new FileBody(file);
			entity.addPart("file", fileBody);
			responseUtil.setZjcl(zjcl);
			responseUtil.setType(UploadResponseUtil.TYPE_ZJCL);
			return responseUtil.getResponse(url, entity);
		} catch (Exception e) {
			BaseResponse response = new BaseResponse();
			response.setMessage(SSWYConstants.ERROR_NETWORK);
			response.setXtcw(false);
			return response;
		}
	}
	
	/**
	 * 上传身份认证材料
	 * 
	 * @return
	 */
	public BaseResponse uploadSfrzcl(TProUserSfrzCl sfrzcl) {
		try {
			String url = uploadNetUtils.getMainAddress() + "/api/wsla/uploadSmrzcl";
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
			entity.addPart("layyId", new StringBody(sfrzcl.getCLayyId(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("type", new StringBody(sfrzcl.getNClLx().toString(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("clId", new StringBody(sfrzcl.getCBh(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			File file = new File(sfrzcl.getCClPath());
			FileBody fileBody = new FileBody(file);
			entity.addPart("clName", new StringBody(file.getName(), Charset.forName(UploadNetUtils.CHARSET_DEFAULT)));
			entity.addPart("file", fileBody);
			responseUtil.setSfrzcl(sfrzcl);
			responseUtil.setType(UploadResponseUtil.TYPE_SFRZCL);
			return responseUtil.getResponse(url, entity);
		} catch (Exception e) {
			BaseResponse response = new BaseResponse();
			response.setMessage(SSWYConstants.ERROR_NETWORK);
			response.setXtcw(false);
			return response;
		}
	}
}
