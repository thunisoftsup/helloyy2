package com.thunisoft.sswy.mobile.logic.response;

import java.util.List;

import org.json.JSONArray;

import com.thunisoft.sswy.mobile.pojo.TCourt;

/**
 * 法院数据转换对象
 * 
 * @author lulg
 * 
 */
public class CourtResponse extends BaseResponse{
    private List<TCourt> data;
    private List<String> closedData;
    private TCourt court;
    private long DUpdate;
    private Object openModules;
    
    public List<TCourt> getData() {
        return data;
    }
    
    public Object getOpenModules() {
		return openModules;
	}
    
	public void setOpenModules(Object openModules) {
		this.openModules = openModules;
	}
	public void setData(List<TCourt> data) {
        this.data = data;
    }
    public List<String> getClosedData() {
        return closedData;
    }
    public void setClosedData(List<String> closedData) {
        this.closedData = closedData;
    }
    public long getDUpdate() {
        return DUpdate;
    }
    public void setDUpdate(long dUpdate) {
        DUpdate = dUpdate;
    }
    
    public TCourt getCourt() {
        return court;
    }
    public void setCourt(TCourt court) {
        this.court = court;
    }
    
    
}
