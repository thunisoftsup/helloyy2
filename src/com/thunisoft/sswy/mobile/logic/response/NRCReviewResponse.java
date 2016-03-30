package com.thunisoft.sswy.mobile.logic.response;

import java.util.List;

import com.thunisoft.sswy.mobile.model.TLayySsclInfo;
import com.thunisoft.sswy.mobile.model.TLayyZjInfo;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayySh;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;

/**
 * 法院数据转换对象
 * 
 * @author lulg
 * 
 */
public class NRCReviewResponse extends BaseResponse{
	private TLayy layy;
	private List<TLayySh> shList;
	private List<TLayyDsr> dsrList;
    private List<TLayyDlr> dlrList;
    private List<TLayySsclInfo> ssclInfoList;
    private List<TLayyZjInfo> zjInfoList;
	private List<TLayyZr> zrList;
	private List<TProUserSfrzCl> sfrzClList;
    
	public List<TLayySh> getShList() {
		return shList;
	}
	
	public void setShList(List<TLayySh> shList) {
		this.shList = shList;
	}
    
    public TLayy getLayy() {
		return layy;
	}

	public void setLayy(TLayy layy) {
		this.layy = layy;
	}

	public List<TLayyDsr> getDsrList() {
		return dsrList;
	}

	public void setDsrList(List<TLayyDsr> dsrList) {
		this.dsrList = dsrList;
	}

	public List<TLayyDlr> getDlrList() {
		return dlrList;
	}

	public void setDlrList(List<TLayyDlr> dlrList) {
		this.dlrList = dlrList;
	}

	public List<TLayyZr> getZrList() {
		return zrList;
	}

	public void setZrList(List<TLayyZr> zrList) {
		this.zrList = zrList;
	}
	
    public List<TLayySsclInfo> getSsclInfoList() {
		return ssclInfoList;
	}

	public void setSsclInfoList(List<TLayySsclInfo> ssclInfoList) {
		this.ssclInfoList = ssclInfoList;
	}

	public List<TLayyZjInfo> getZjInfoList() {
		return zjInfoList;
	}

	public void setZjInfoList(List<TLayyZjInfo> zjInfoList) {
		this.zjInfoList = zjInfoList;
	}

	
	/**  
	 * 获取  sfrzClList  
	 * @return sfrzClList  
	 */
	public List<TProUserSfrzCl> getSfrzClList() {
		return sfrzClList;
	}

	/**  
	 * 设置  sfrzClList  
	 * @param sfrzClList
	 */
	public void setSfrzClList(List<TProUserSfrzCl> sfrzClList) {
		this.sfrzClList = sfrzClList;
	}

}
