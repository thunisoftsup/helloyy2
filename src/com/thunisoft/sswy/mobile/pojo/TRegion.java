package com.thunisoft.sswy.mobile.pojo;

import java.io.Serializable;

/**
 * 中国行政地区
 * 
 * @author gewx
 *
 */
public class TRegion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6102938310321257352L;

	/**
	 * id
	 */
	private String CId;

	/**
	 * 父节点id
	 */
	private String CParentId;

	/**
	 * 分级代码
	 */
	private String CFjm;

	/**
	 * 行政级别
	 */
	private String CJb;

	/**
	 * 地点名称
	 */
	private String CName;

	/**
	 * 序号
	 */
	private Integer NOrder;

	/**
	 * 是否有效
	 */
	private Integer NValid;

	/**  
	 * 获取  cId  
	 * @return cId  
	 */
	
	public String getCId() {
		return CId;
	}

	/**  
	 * 设置  cId  
	 * @param cId
	 */
	public void setCId(String cId) {
		CId = cId;
	}

	/**  
	 * 获取  父节点id
	 * @return cPId  
	 */
	
	public String getCParentId() {
		return CParentId;
	}

	/**  
	 * 设置  父节点id
	 * @param cPId
	 */
	public void setCParentId(String CParentId) {
		this.CParentId = CParentId;
	}

	/**  
	 * 获取  分级代码
	 * @return cFjm  
	 */
	
	public String getCFjm() {
		return CFjm;
	}

	/**  
	 * 设置  分级代码
	 * @param cFjm
	 */
	public void setCFjm(String cFjm) {
		CFjm = cFjm;
	}

	/**  
	 * 获取  行政级别
	 * @return cJb  
	 */
	
	public String getCJb() {
		return CJb;
	}

	/**  
	 * 设置  行政级别
	 * @param cJb
	 */
	public void setCJb(String cJb) {
		CJb = cJb;
	}

	/**  
	 * 获取  地点名称
	 * @return cName  
	 */
	
	public String getCName() {
		return CName;
	}

	/**  
	 * 设置  地点名称
	 * @param cName
	 */
	public void setCName(String cName) {
		CName = cName;
	}

	/**  
	 * 获取  序号
	 * @return nOrder  
	 */
	
	public Integer getNOrder() {
		return NOrder;
	}

	/**  
	 * 设置  序号
	 * @param nOrder
	 */
	public void setNOrder(Integer nOrder) {
		NOrder = nOrder;
	}

	/**  
	 * 获取  是否有效  
	 * @return nValid  
	 */
	
	public Integer getNValid() {
		return NValid;
	}

	/**  
	 * 设置  是否有效  
	 * @param nValid
	 */
	public void setNValid(Integer nValid) {
		NValid = nValid;
	}

	
}
