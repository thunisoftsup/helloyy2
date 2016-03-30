package com.thunisoft.sswy.mobile.logic.response;

/**
 * 返回结果基类
 * 
 * @author lulg
 * 
 */
public class BaseResponse {
    protected boolean success;
    protected String message;
    protected boolean xtcw = true;
    protected Integer errorShowType;
    /**
     * 临时sessionId
     */
    protected String tempSid;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isXtcw() {
        return xtcw;
    }

    public void setXtcw(boolean xtcw) {
        this.xtcw = xtcw;
    }

    public Integer getErrorShowType() {
        return errorShowType;
    }

    public void setErrorShowType(Integer errorShowType) {
        this.errorShowType = errorShowType;
    }

    public String getTempSid() {
        return tempSid;
    }

    public void setTempSid(String tempSid) {
        this.tempSid = tempSid;
    }
}
