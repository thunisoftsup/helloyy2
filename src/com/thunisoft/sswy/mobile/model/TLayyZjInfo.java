package com.thunisoft.sswy.mobile.model;

import java.io.Serializable;
import java.util.List;

import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;

public class TLayyZjInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6602902944051127708L;

	/**
	 * 诉讼材料
	 */
	private TZj TZj;
	
	/**
	 * 诉讼材料附件
	 */
    private List<TZjcl> TZjclList;

	public TZj getTZj() {
		return TZj;
	}

	public void setTZj(TZj tZj) {
		TZj = tZj;
	}

	public List<TZjcl> getTZjclList() {
		return TZjclList;
	}

	public void setTZjclList(List<TZjcl> tZjclList) {
		TZjclList = tZjclList;
	}
}
