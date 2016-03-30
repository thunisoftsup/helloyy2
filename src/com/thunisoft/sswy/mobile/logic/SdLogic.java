package com.thunisoft.sswy.mobile.logic;

import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;

import com.thunisoft.sswy.mobile.datasource.SdDao;
import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.net.SdResponseUtil;
import com.thunisoft.sswy.mobile.logic.response.SdResponse;
import com.thunisoft.sswy.mobile.pojo.TSd;
import com.thunisoft.sswy.mobile.pojo.TSdWrit;

/**
 * 文书签收逻辑类
 * 
 * @author lulg
 * 
 */
@EBean(scope = Scope.Singleton)
public class SdLogic {
    private static final String CLIENT = "android";
    
    /** 签名码下载文书 **/
    private static final String URL_DOWN_WRIT_QMM = "/mobile/dzsd/downWritByQmm.htm";
    /** 认证用户下载文书 **/
    private static final String URL_DOWN_WRIT_USER = "/mobile/pro/dzsd/downWritByUser.htm";
    /** 加载文书信息 **/
    private static final String URL_WRIT_INFO = "/mobile/pro/dzsd/loadWritInfo.htm";
    /** 加载文书列表 **/
    private static final String URL_LOAD_WRIT_LIST = "/mobile/pro/dzsd/loadWritList.htm";

    @Bean
    NetUtils netUtils;
    
    @Bean
    SdDao sdDao;

    @Bean
    SdResponseUtil sdResponseUtil;


    /**
     * 通过电子签名码下载文书
     * 
     * @param sfzjhm
     * @param dzqmm
     * @return
     */
    public SdResponse downWritByQmm(String sfzjhm, String dzqmm, long t) {
        String url = netUtils.getServerAddress() + URL_DOWN_WRIT_QMM;
        sdResponseUtil.clearParams()
                .addSecretParam("sfzjhm", sfzjhm)
                .addSecretParam("dzqmm", dzqmm)
                .addSecretParam("c", CLIENT);
        if (t != 0L) {
            sdResponseUtil.addSecretParam("t", String.valueOf(t));
        }
        return sdResponseUtil.getResponse(url, sdResponseUtil.getParams());
    }

    /**
     * 通过实名认证用户直接下载文书
     * 
     * @param id
     * @param t
     * @return
     */
    public SdResponse downWritByUser(String id, long t) {
        String url = netUtils.getServerAddress() + URL_DOWN_WRIT_USER;
        sdResponseUtil.clearParams().addParam("id", id).addSecretParam("c", CLIENT);
        if (t != 0L) {
            sdResponseUtil.addSecretParam("t", String.valueOf(t));
        }
        return sdResponseUtil.getResponse(url, sdResponseUtil.getParams());
    }

    /**
     * 加载送达信息
     * 
     * @param id
     * @return
     */
    public SdResponse loadSdInfo(String id) {
        String url = netUtils.getServerAddress() + URL_WRIT_INFO;
        return sdResponseUtil.getResponse(url, sdResponseUtil.getParams("id", id));
    }

    /**
     * 加载送达列表（已签收，未签收）
     * 
     * @param page
     * @param rows
     * @param scope
     * @return
     */
    public SdResponse loadSdList(int page, int rows, String scope,String searchValue) {
        String url = netUtils.getServerAddress() + URL_LOAD_WRIT_LIST;
        List<NameValuePair> params = sdResponseUtil.clearParams().addParam("page", String.valueOf(page))
                .addParam("rows", String.valueOf(rows)).addParam("scope", scope).addParam("searchValue", searchValue).getParams()
                ;
        return sdResponseUtil.getResponse(url, params);
    }
    
    /**
     * 从DB中加载送达列表
     * @return
     */
    public List<TSd> getSdListFromDb(int limit) {
        return sdDao.getSdList(limit);
    }
    
    /**
     * 从DB中加载送达信息
     * @param sdId
     * @return
     */
    public TSd getSdInfoFromDb(String sdId) {
        return sdDao.getSdInfo(sdId);
    }
    
    /**
     * 从DB中加载送达文书
     * @param sdId
     * @return
     */
    public List<TSdWrit> getSdWritFromDb(String sdId) {
        return sdDao.getSdWrit(sdId);
    }
    
    
    
    /**
     * 保存送达信息到DB中
     * @param sd
     * @param writList
     * @return
     */
    public SdResponse saveSd(TSd sd, List<TSdWrit> writList) {
        SdResponse response = new SdResponse();
        try {
            sdDao.saveSd(sd, writList);
            response.setSuccess(true);
        } catch (SSWYException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    
    /**
     * 保存送达文书信息到DB中
     * @param sd
     * @param writList
     * @return
     */
    public SdResponse saveSdWrit(List<TSdWrit> writList) {
        SdResponse response = new SdResponse();
        try {
            sdDao.saveSdWrit(writList);
            response.setSuccess(true);
        } catch (SSWYException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}
