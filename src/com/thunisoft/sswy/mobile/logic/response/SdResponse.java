package com.thunisoft.sswy.mobile.logic.response;

import java.util.List;

import com.thunisoft.sswy.mobile.pojo.TSd;

/**
 * 送达
 * 
 * @author lulg
 * 
 */
public class SdResponse extends BaseResponse {
    private List<TSd> sdList;
    private TSd writ;
    private String zip;

    public List<TSd> getSdList() {
        return sdList;
    }

    public void setSdList(List<TSd> sdList) {
        this.sdList = sdList;
    }

    public TSd getWrit() {
        return writ;
    }

    public void setWrit(TSd writ) {
        this.writ = writ;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

}
