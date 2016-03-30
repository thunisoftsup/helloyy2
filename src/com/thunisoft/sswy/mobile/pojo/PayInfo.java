package com.thunisoft.sswy.mobile.pojo;

public class PayInfo {
	
	private String CId;
	private String Cjfje;
	private String Cjfzh;
	private String Csfzh;
	
	private String Cdzph;
	private String Cjffs;
	private int Csuccess;
	
	public PayInfo() {
		super();
	}
	public PayInfo(String cId, String cjfje, String cjfzh, String csfzh,
			String cdzph, String cjffs, int csuccess) {
		super();
		CId = cId;
		Cjfje = cjfje;
		Cjfzh = cjfzh;
		Csfzh = csfzh;
		Cdzph = cdzph;
		Cjffs = cjffs;
		Csuccess = csuccess;
	}
	public String getCId() {
		return CId;
	}
	public void setCId(String cId) {
		CId = cId;
	}
	public String getCjfje() {
		return Cjfje;
	}
	public void setCjfje(String cjfje) {
		Cjfje = cjfje;
	}
	public String getCjfzh() {
		return Cjfzh;
	}
	public void setCjfzh(String cjfzh) {
		Cjfzh = cjfzh;
	}
	public String getCsfzh() {
		return Csfzh;
	}
	public void setCsfzh(String csfzh) {
		Csfzh = csfzh;
	}
	public String getCdzph() {
		return Cdzph;
	}
	public void setCdzph(String cdzph) {
		Cdzph = cdzph;
	}
	public String getCjffs() {
		return Cjffs;
	}
	public void setCjffs(String cjffs) {
		Cjffs = cjffs;
	}
	public int getCsuccess() {
		return Csuccess;
	}
	public void setCsuccess(int csuccess) {
		Csuccess = csuccess;
	}
	
	
	
	
	


}
