package com.thunisoft.sswy.mobile.pojo;

public class TCourtAttach {
    private String id;
    private String ext;//扩展名
    private boolean deleted;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExt() {
        return ext;
    }
    public void setExt(String ext) {
        this.ext = ext;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public boolean isDeleted() {
        return deleted;
    }
    
}
