package com.thunisoft.sswy.mobile.logic.response;

import java.util.List;

import com.thunisoft.sswy.mobile.pojo.LsrzJl;

/**
 * 
 * @author lulg
 * 
 */
public class GywResponse extends BaseResponse {
    private String xm;
    private String zjhm;
    private String sjhm;
    private String zjlx;
    private List<LsrzJl> rzjlList;

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getZjhm() {
        return zjhm;
    }

    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public List<LsrzJl> getRzjlList() {
        return rzjlList;
    }

    public void setRzjlList(List<LsrzJl> rzjlList) {
        this.rzjlList = rzjlList;
    }

}
