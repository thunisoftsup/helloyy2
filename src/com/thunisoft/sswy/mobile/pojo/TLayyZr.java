package com.thunisoft.sswy.mobile.pojo;


/**
 * 立案预约_证人
 * TLayyZr entity. @author MyEclipse Persistence Tools
 */

public class TLayyZr implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -960796567847768237L;
	
	/**
	 * 证人Id
	 */
	private String CId;
	
	/**
	 * 立案预约id
	 */
    private String CLayyId;
    
    /**
     * 姓名
     */
    private String CName;
    
    /**
     * 性别
     */
    private Integer NXb;
    
    /**
     * 证件类型
     */
    private Integer NIdcardType;
    
    /**
     * 证件号码
     */
    private String CIdcard;
    
    /**
     * 出生日期
     */
    private String DCsrq;
    
    /**
     * 年龄
     */
    private Integer NAge;
    
    /**
     * 工作单位
     */
    private String CGzdw;
    
    /**
     * 手机号码
     */
    private String CSjhm;
    
    /**
     * 出庭作证
     */
    private Integer NCtzz;
    
    /**
     * 有利方Id
     */
    private String CYlfId;
    
    /**
     * 有利方名称
     */
    private String CYlfMc;
    
    /**
     * 经常居住地/联系地址
     */
    private String CAddress;
    
    /**
     * 经常居住地/联系地址编号
     */
    private String CAddressId;
    
    /**
     * 住所地
     */
    private String CZsd;
    
    /**
     * 住所地编号
     */
    private String CZsdId;
    
    /** default constructor */
    public TLayyZr() {
    	
    }

    /**
     * 获取 证人Id
     * @return 证人Id
     */
    public String getCId() {
        return CId;
    }

    /**
     * 设置 证人Id
     * @param cId 证人Id
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
     * 获取 姓名
     * @return 姓名
     */
    public String getCName() {
        return CName;
    }

    /**
     * 设置 姓名
     * @param cName 姓名
     */
    public void setCName(String cName) {
        CName = cName;
    }

    /**
     * 获取 性别
     * @return 性别
     */
    public Integer getNXb() {
        return NXb;
    }

    /**
     * 设置 性别
     * @param nXb 性别
     */
    public void setNXb(Integer nXb) {
        NXb = nXb;
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
     * 获取 出生日期
     * @return 出生日期
     */
    public String getDCsrq() {
        return DCsrq;
    }

    /**
     * 设置 出生日期
     * @param dCsrq 出生日期
     */
    public void setDCsrq(String dCsrq) {
        DCsrq = dCsrq;
    }

    /**
     * 获取 年龄
     * @return 年龄
     */
    public Integer getNAge() {
        return NAge;
    }

    /**
     * 设置 年龄
     * @param nAge 年龄
     */
    public void setNAge(Integer nAge) {
        NAge = nAge;
    }

    /**
     * 获取 工作单位
     * @return 工作单位
     */
    public String getCGzdw() {
        return CGzdw;
    }

    /**
     * 设置 工作单位
     * @param cGzdw 工作单位
     */
    public void setCGzdw(String cGzdw) {
        CGzdw = cGzdw;
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
     * 获取 出庭作证
     * @return 出庭作证
     */
    public Integer getNCtzz() {
        return NCtzz;
    }

    /**
     * 设置 出庭作证
     * @param nCtzz 出庭作证
     */
    public void setNCtzz(Integer nCtzz) {
        NCtzz = nCtzz;
    }

    /**
     * 获取 有利方Id
     * @return 有利方Id
     */
    public String getCYlfId() {
        return CYlfId;
    }

    /**
     * 设置 有利方Id
     * @param cYlfId 有利方Id
     */
    public void setCYlfId(String cYlfId) {
        CYlfId = cYlfId;
    }

    /**
     * 获取 有利方名称
     * @return 有利方名称
     */
    public String getCYlfMc() {
        return CYlfMc;
    }

    /**
     * 设置 有利方名称
     * @param cYlfMc 有利方名称
     */
    public void setCYlfMc(String cYlfMc) {
        CYlfMc = cYlfMc;
    }

    /**
     * 获取 经常居住地联系地址
     * @return 经常居住地联系地址
     */
    public String getCAddress() {
        return CAddress;
    }

    /**
     * 设置 经常居住地联系地址
     * @param cAddress 经常居住地联系地址
     */
    public void setCAddress(String cAddress) {
        CAddress = cAddress;
    }

    /**
     * 获取 经常居住地联系地址编号
     * @return 经常居住地联系地址编号
     */
    public String getCAddressId() {
        return CAddressId;
    }

    /**
     * 设置 经常居住地联系地址编号
     * @param cAddressId 经常居住地联系地址编号
     */
    public void setCAddressId(String cAddressId) {
        CAddressId = cAddressId;
    }

    /**
     * 获取 住所地
     * @return 住所地
     */
    public String getCZsd() {
        return CZsd;
    }

    /**
     * 设置 住所地
     * @param cZsd 住所地
     */
    public void setCZsd(String cZsd) {
        CZsd = cZsd;
    }

    /**
     * 获取 住所地编号
     * @return 住所地编号
     */
    public String getCZsdId() {
        return CZsdId;
    }

    /**
     * 设置 住所地编号
     * @param cZsdId 住所地编号
     */
    public void setCZsdId(String cZsdId) {
        CZsdId = cZsdId;
    }

    
}