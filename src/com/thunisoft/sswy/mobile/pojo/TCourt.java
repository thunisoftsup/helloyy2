package com.thunisoft.sswy.mobile.pojo;

public class TCourt {
    private String CId;
    private String CName;
    private String CJc;
    private String CJp;
    private String CParentId;
    private String CFjm;
    private String CJb;
    private Integer NOrder;
    private String CSsfwUrl;
    private String CWapUrl;
    private Integer NJsfs;

    public String getCId() {
        return CId;
    }

    public void setCId(String cId) {
        CId = cId;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String cName) {
        CName = cName;
    }

    public String getCJc() {
        return CJc;
    }

    public void setCJc(String cJc) {
        CJc = cJc;
    }

    public String getCJp() {
        return CJp;
    }

    public void setCJp(String cJp) {
        CJp = cJp;
    }

    public String getCParentId() {
        return CParentId;
    }

    public void setCParentId(String cParentId) {
        CParentId = cParentId;
    }

    public String getCFjm() {
        return CFjm;
    }

    public void setCFjm(String cFjm) {
        CFjm = cFjm;
    }

    public String getCJb() {
        return CJb;
    }

    public void setCJb(String cJb) {
        CJb = cJb;
    }

    public Integer getNOrder() {
        return NOrder;
    }

    public void setNOrder(Integer nOrder) {
        NOrder = nOrder;
    }

    public String getCSsfwUrl() {
        return CSsfwUrl;
    }

    public void setCSsfwUrl(String cSsfwUrl) {
        CSsfwUrl = cSsfwUrl;
    }

    public String getCWapUrl() {
        return CWapUrl;
    }

    public void setCWapUrl(String cWapUrl) {
        CWapUrl = cWapUrl;
    }

    public Integer getNJsfs() {
        return NJsfs == null ? 0 : NJsfs;
    }

    public void setNJsfs(Integer nJsfs) {
        NJsfs = nJsfs;
    }
}
