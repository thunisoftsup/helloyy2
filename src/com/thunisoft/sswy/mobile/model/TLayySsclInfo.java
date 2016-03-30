package com.thunisoft.sswy.mobile.model;

import java.util.List;

import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;

public class TLayySsclInfo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -574171979664204601L;

	/**
	 * 诉讼材料
	 */
	private TLayySscl sscl;
	
	/**
	 * 诉讼材料附件
	 */
    private List<TLayySsclFj> ssclfjList;
    
    /**
	 * 当事人诉讼材料
	 */
    private List<TLayyDsrSscl> ssdsrClList;
    
    
    
	public List<TLayySsclFj> getSsclfjList() {
		return ssclfjList;
	}

	public void setSsclfjList(List<TLayySsclFj> ssclfjList) {
		this.ssclfjList = ssclfjList;
	}

	public List<TLayyDsrSscl> getSsdsrClList() {
		return ssdsrClList;
	}

	public void setSsdsrClList(List<TLayyDsrSscl> ssdsrClList) {
		this.ssdsrClList = ssdsrClList;
	}

	public TLayySscl getSscl() {
		return sscl;
	}

	public void setSscl(TLayySscl sscl) {
		this.sscl = sscl;
	}

}
