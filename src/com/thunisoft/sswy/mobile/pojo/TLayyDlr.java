package com.thunisoft.sswy.mobile.pojo;

/**
 * 立案预约_代理人
 * TLayyDlr entity. @author MyEclipse Persistence Tools
 */

public class TLayyDlr implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3014999891516199021L;
	
	/**
	 * 代理人id
	 */
	private String CId;
	
	/**
	 * 立案预约id
	 */
    private String CLayyId;
    
    /**
     * 代理人类型代码
     */
    private Integer NDlrType;
    
    /**
     * 姓名/单位名称
     */
    private String CName;
    
    /**
     * 被代理人id（1个或多个原告的id）
     */
    private String CBdlrId;
    
    /**
     * 被代理人名称（1个或多个原告的名称）
     */
    private String CBdlrMc;
    
    /**
     * 证件类型
     */
    private Integer NIdcardType;
    
    /**
     * 证件号码
     */
    private String CIdcard;
    
    /**
     * 执业证号
     */
    private String CZyzh;
    
    /**
     * 所在单位
     */
    private String CSzdw;
    
    /**
     * 手机号码
     */
    private String CSjhm;
    
    /**
     * 代理种类
     */
    private Integer NDlrDlzl;
    
    /**
     * 显示序号
     */
    private Integer NXh;

	/** default constructor */
    public TLayyDlr() {
    }

    /**
     * 获取 代理人id
     * @return 代理人id
     */
    public String getCId() {
        return CId;
    }

    /**
     * 设置 代理人id
     * @param cId 代理人id
     */
    public void setCId(String cId) {
        CId = cId;
    }

    /**
     * 获取 立案预约id
     * @return 立案预约id
     */
    public String getCLayyId() {
        return CLayyId;
    }

    /**
     * 设置 立案预约id
     * @param cLayyId 立案预约id
     */
    public void setCLayyId(String cLayyId) {
        CLayyId = cLayyId;
    }

    /**
     * 获取 代理人类型代码
     * @return 代理人类型代码
     */
    public Integer getNDlrType() {
        return NDlrType;
    }

    /**
     * 设置 代理人类型代码
     * @param nDlrType 代理人类型代码
     */
    public void setNDlrType(Integer nDlrType) {
        NDlrType = nDlrType;
    }

    /**
     * 获取 姓名单位名称
     * @return 姓名单位名称
     */
    public String getCName() {
        return CName;
    }

    /**
     * 设置 姓名单位名称
     * @param cName 姓名单位名称
     */
    public void setCName(String cName) {
        CName = cName;
    }

    /**
     * 获取 被代理人id（1个或多个原告的id）
     * @return 被代理人id（1个或多个原告的id）
     */
    public String getCBdlrId() {
        return CBdlrId;
    }

    /**
     * 设置 被代理人id（1个或多个原告的id）
     * @param cBdlrId 被代理人id（1个或多个原告的id）
     */
    public void setCBdlrId(String cBdlrId) {
        CBdlrId = cBdlrId;
    }

    /**
     * 获取 被代理人名称（1个或多个原告的名称）
     * @return 被代理人名称（1个或多个原告的名称）
     */
    public String getCBdlrMc() {
        return CBdlrMc;
    }

    /**
     * 设置 被代理人名称（1个或多个原告的名称）
     * @param cBdlrMc 被代理人名称（1个或多个原告的名称）
     */
    public void setCBdlrMc(String cBdlrMc) {
        CBdlrMc = cBdlrMc;
    }

    /**
     * 获取 证件类型
     * @return 证件类型
     */
    public Integer getNIdcardType() {
        return NIdcardType;
    }

    /**
     * 设置 证件类型
     * @param nIdcardType 证件类型
     */
    public void setNIdcardType(Integer nIdcardType) {
        NIdcardType = nIdcardType;
    }

    /**
     * 获取 证件号码
     * @return 证件号码
     */
    public String getCIdcard() {
        return CIdcard;
    }

    /**
     * 设置 证件号码
     * @param cIdcard 证件号码
     */
    public void setCIdcard(String cIdcard) {
        CIdcard = cIdcard;
    }

    /**
     * 获取 执业证号
     * @return 执业证号
     */
    public String getCZyzh() {
        return CZyzh;
    }

    /**
     * 设置 执业证号
     * @param cZyzh 执业证号
     */
    public void setCZyzh(String cZyzh) {
        CZyzh = cZyzh;
    }

    /**
     * 获取 所在单位
     * @return 所在单位
     */
    public String getCSzdw() {
        return CSzdw;
    }

    /**
     * 设置 所在单位
     * @param cSzdw 所在单位
     */
    public void setCSzdw(String cSzdw) {
        CSzdw = cSzdw;
    }

    /**
     * 获取 手机号码
     * @return 手机号码
     */
    public String getCSjhm() {
        return CSjhm;
    }

    /**
     * 设置 手机号码
     * @param cSjhm 手机号码
     */
    public void setCSjhm(String cSjhm) {
        CSjhm = cSjhm;
    }

    /**
     * 获取 代理种类
     * @return 代理种类
     */
    public Integer getNDlrDlzl() {
        return NDlrDlzl;
    }

    /**
     * 设置 代理种类
     * @param nDlrDlzl 代理种类
     */
    public void setNDlrDlzl(Integer nDlrDlzl) {
        NDlrDlzl = nDlrDlzl;
    }

	/**  
	 * 获取  显示序号  
	 * @return nXh  
	 */
	public Integer getNXh() {
		return NXh;
	}

	/**  
	 * 设置  显示序号  
	 * @param nXh
	 */
	public void setNXh(Integer nXh) {
		NXh = nXh;
	}

}