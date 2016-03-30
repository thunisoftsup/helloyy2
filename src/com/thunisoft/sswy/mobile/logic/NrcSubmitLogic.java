package com.thunisoft.sswy.mobile.logic;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import com.alibaba.fastjson.JSON;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.sswy.mobile.datasource.NrcDlrDao;
import com.thunisoft.sswy.mobile.datasource.NrcDsrDao;
import com.thunisoft.sswy.mobile.datasource.NrcSsclDao;
import com.thunisoft.sswy.mobile.datasource.NrcZjDao;
import com.thunisoft.sswy.mobile.datasource.NrcZrDao;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.net.NrcSubmitResponseUtil;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.pojo.TZj;

/**
 * 网上立案 逻辑类
 * 
 * @author gewx
 * 
 */
@EBean(scope = Scope.Singleton)
public class NrcSubmitLogic {

	private static final String TAG = NrcSubmitLogic.class.getSimpleName();

	@Bean
	NetUtils netUtils;

	@Bean
	NrcSubmitResponseUtil responseUtil;

	@Bean
	NrcDsrDao nrcDsrDao;
	
	@Bean
	NrcDlrDao nrcDlrDao;
	
	@Bean
	NrcSsclDao nrcSsclDao;
	
	@Bean
	NrcZjDao nrcZjDao;
	
	@Bean
	NrcZrDao nrcZrDao;
	
	/**
	 * 提交_网上立案
	 * 
	 * @return
	 */
	public BaseResponse submitNetRegisterCase(TLayy layy, String tempSid) {

		List<TLayyDsr> dsrList = new ArrayList<TLayyDsr>();
		List<TLayyDsr> plaintiffList = nrcDsrDao.getLayyDsrList(layy.getCId(), Constants.LITIGANT_SSDW_PLAINTIFF, NrcDsrDao.TOP_ALL);
		dsrList.addAll(plaintiffList);
		List<TLayyDsr> defendantList = nrcDsrDao.getLayyDsrList(layy.getCId(), Constants.LITIGANT_SSDW_DEFENDANT, NrcDsrDao.TOP_ALL);
		dsrList.addAll(defendantList);

		List<TLayyDlr> dlrList = nrcDlrDao.getLayyDlrList(layy.getCId(), NrcDlrDao.TOP_ALL);
		List<TLayySscl> ssclList = nrcSsclDao.getSsclListBylayyId(layy.getCId(), NrcSsclDao.TYPE_ALL, NrcSsclDao.TOP_ALL);
		
		List<TZj> zjList = nrcZjDao.getZjListBylayyId(layy.getCId(), NrcZjDao.TOP_ALL);
		
		List<TLayyZr> zrList = nrcZrDao.getZrListBylayyId(layy.getCId(), NrcZrDao.TOP_ALL);

		responseUtil.setLayy(layy);
		String url = netUtils.getMainAddress() + "/api/wsla/saveOrUpdateLayy";
		responseUtil.clearParams();
		responseUtil.addParam("sid", tempSid);
		responseUtil.addParam("layy", JSON.toJSONString(layy));
		responseUtil.addParam("dlrList", JSON.toJSONString(dlrList));
		responseUtil.addParam("dsrList", JSON.toJSONString(dsrList));
		responseUtil.addParam("ssclList", JSON.toJSONString(ssclList));
		responseUtil.addParam("zjList", JSON.toJSONString(zjList));
		responseUtil.addParam("zrList", JSON.toJSONString(zrList));
		return responseUtil.getResponse(url, responseUtil.getParams());
	}
}
