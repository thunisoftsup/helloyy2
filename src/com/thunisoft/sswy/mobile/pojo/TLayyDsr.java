package com.thunisoft.sswy.mobile.pojo;


/**
 * 立案预约_当事人
 * TLayyDsr entity. @author MyEclipse Persistence Tools
 */

public class TLayyDsr implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 832237985087371403L;
	
	/**
	 * 当事人id
	 */
	private String CId;
	
	/**
	 * 立案预约id
	 */
    private String CLayyId;
    
    /**
     * 业务系统当事人编号
     */
    private String CYwxtDsrbh;
    
    /**
     * 原审案件人员编号
     */
    private String CYsRybh;
    
    /**
     * 显示序号
     */
    private Integer NXh;
    
    /**
     * 诉讼地位
     */
    private String CSsdw;
    
    /**
     * 当事人类型
     */
    private Integer NType;
    
    /**
     * 自然人姓名/法人单位名称/非法人组织的单位名称
     */
    private String CName;
    
    /**
     * 自然人证件类型/法人的法定代表人证件类型/非法人组织的主要负责人证件类型
     */
    private Integer NIdcardType;
    
    /**
     * 自然人证件号码/法人的法定代表人证件号码/非法人组织的主要负责人证件号码
     */
    private String CIdcard;
    
    /**
     * 性别
     */
    private Integer NXb;
    
    /**
     * 出生日期
     */
    private String DCsrq;
    
    /**
     * 年龄
     */
    private Integer NAge;
    
    /**
     * 民族
     */
    private Integer NMz;
    
    /**
     * 职业
     */
    private Integer NZy;
    
    /**
     * 工作单位
     */
    private String CGzdw;
    
    /**
     * 手机号码
     */
    private String CSjhm;
    
    /**
     * 联系电话
     */
    private String CLxdh;
    
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
    
    /**
     * 单位性质
     */
    private Integer NDwxz;
    
    /**
     * 组织机构代码
     */
    private String CZzjgdm;
    
    /**
     * 单位地址
     */
    private String CDwdz;
    
    /**
     * 单位地址编号
     */
    private String CDwdzId;
    
    /**
     * 法定代表人
     */
    private String CFddbr;
    
    /**
     * 法定代表人职务
     */
    private String CFddbrZw;
    
    /**
     * 法定代表人手机号码
     */
    private String CFddbrSjhm;
    
    /**
     * 法定代表人联系电话(固话)
     */
    private String CFddbrLxdh;

    /** default constructor */
    public TLayyDsr() {
    }

    /**
     * 获取 当事人id
     * @return 当事人id
     */
    public String getCId() {
        return CId;
    }

    /**
     * 设置 当事人id
     * @param cId 当事人id
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
     * 获取 业务系统当事人编号
     * @return 业务系统当事人编号
     */
    public String getCYwxtDsrbh() {
        return CYwxtDsrbh;
    }

    /**
     * 设置 业务系统当事人编号
     * @param cYwxtDsrbh 业务系统当事人编号
     */
    public void setCYwxtDsrbh(String cYwxtDsrbh) {
        CYwxtDsrbh = cYwxtDsrbh;
    }

    /**
     * 获取 原审案件人员编号
     * @return 原审案件人员编号
     */
    public String getCYsRybh() {
        return CYsRybh;
    }

    /**
     * 设置 原审案件人员编号
     * @param cYsRybh 原审案件人员编号
     */
    public void setCYsRybh(String cYsRybh) {
        CYsRybh = cYsRybh;
    }

    /**
     * 获取 显示序号
     * @return 显示序号
     */
    public Integer getNXh() {
        return NXh;
    }

    /**
     * 设置 显示序号
     * @param nXh 显示序号
     */
    public void setNXh(Integer nXh) {
        NXh = nXh;
    }

    /**
     * 获取 诉讼地位
     * @return 诉讼地位
     */
    public String getCSsdw() {
        return CSsdw;
    }

    /**
     * 设置 诉讼地位
     * @param cSsdw 诉讼地位
     */
    public void setCSsdw(String cSsdw) {
        CSsdw = cSsdw;
    }

    /**
     * 获取 当事人类型
     * @return 当事人类型
     */
    public Integer getNType() {
        return NType;
    }

    /**
     * 设置 当事人类型
     * @param nType 当事人类型
     */
    public void setNType(Integer nType) {
        NType = nType;
    }

    /**
     * 获取 自然人姓名法人单位名称非法人组织的单位名称
     * @return 自然人姓名法人单位名称非法人组织的单位名称
     */
    public String getCName() {
        return CName;
    }

    /**
     * 设置 自然人姓名法人单位名称非法人组织的单位名称
     * @param cName 自然人姓名法人单位名称非法人组织的单位名称
     */
    public void setCName(String cName) {
        CName = cName;
    }

    /**
     * 获取 自然人证件类型法人的法定代表人证件类型非法人组织的主要负责人证件类型
     * @return 自然人证件类型法人的法定代表人证件类型非法人组织的主要负责人证件类型
     */
    public Integer getNIdcardType() {
        return NIdcardType;
    }

    /**
     * 设置 自然人证件类型法人的法定代表人证件类型非法人组织的主要负责人证件类型
     * @param nIdcardType 自然人证件类型法人的法定代表人证件类型非法人组织的主要负责人证件类型
     */
    public void setNIdcardType(Integer nIdcardType) {
        NIdcardType = nIdcardType;
    }

    /**
     * 获取 自然人证件号码法人的法定代表人证件号码非法人组织的主要负责人证件号码
     * @return 自然人证件号码法人的法定代表人证件号码非法人组织的主要负责人证件号码
     */
    public String getCIdcard() {
        return CIdcard;
    }

    /**
     * 设置 自然人证件号码法人的法定代表人证件号码非法人组织的主要负责人证件号码
     * @param cIdcard 自然人证件号码法人的法定代表人证件号码非法人组织的主要负责人证件号码
     */
    public void setCIdcard(String cIdcard) {
        CIdcard = cIdcard;
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
     * 获取 民族
     * @return 民族
     */
    public Integer getNMz() {
        return NMz;
    }

    /**
     * 设置 民族
     * @param nMz 民族
     */
    public void setNMz(Integer nMz) {
        NMz = nMz;
    }

    /**
     * 获取 职业
     * @return 职业
     */
    public Integer getNZy() {
        return NZy;
    }

    /**
     * 设置 职业
     * @param nZy 职业
     */
    public void setNZy(Integer nZy) {
        NZy = nZy;
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
     * 获取 联系电话
     * @return 联系电话
     */
    public String getCLxdh() {
        return CLxdh;
    }

    /**
     * 设置 联系电话
     * @param cLxdh 联系电话
     */
    public void setCLxdh(String cLxdh) {
        CLxdh = cLxdh;
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

    /**
     * 获取 单位性质
     * @return 单位性质
     */
    public Integer getNDwxz() {
        return NDwxz;
    }

    /**
     * 设置 单位性质
     * @param nDwxz 单位性质
     */
    public void setNDwxz(Integer nDwxz) {
        NDwxz = nDwxz;
    }

    /**
     * 获取 组织机构代码
     * @return 组织机构代码
     */
    public String getCZzjgdm() {
        return CZzjgdm;
    }

    /**
     * 设置 组织机构代码
     * @param cZzjgdm 组织机构代码
     */
    public void setCZzjgdm(String cZzjgdm) {
        CZzjgdm = cZzjgdm;
    }

    /**
     * 获取 单位地址
     * @return 单位地址
     */
    public String getCDwdz() {
        return CDwdz;
    }

    /**
     * 设置 单位地址
     * @param cDwdz 单位地址
     */
    public void setCDwdz(String cDwdz) {
        CDwdz = cDwdz;
    }

    /**
     * 获取 单位地址编号
     * @return 单位地址编号
     */
    public String getCDwdzId() {
        return CDwdzId;
    }

    /**
     * 设置 单位地址编号
     * @param cDwdzId 单位地址编号
     */
    public void setCDwdzId(String cDwdzId) {
        CDwdzId = cDwdzId;
    }

    /**
     * 获取 法定代表人
     * @return 法定代表人
     */
    public String getCFddbr() {
        return CFddbr;
    }

    /**
     * 设置 法定代表人
     * @param cFddbr 法定代表人
     */
    public void setCFddbr(String cFddbr) {
        CFddbr = cFddbr;
    }

    /**
     * 获取 法定代表人职务
     * @return 法定代表人职务
     */
    public String getCFddbrZw() {
        return CFddbrZw;
    }

    /**
     * 设置 法定代表人职务
     * @param cFddbrZw 法定代表人职务
     */
    public void setCFddbrZw(String cFddbrZw) {
        CFddbrZw = cFddbrZw;
    }

    /**
     * 获取 法定代表人手机号码
     * @return 法定代表人手机号码
     */
    public String getCFddbrSjhm() {
        return CFddbrSjhm;
    }

    /**
     * 设置 法定代表人手机号码
     * @param cFddbrSjhm 法定代表人手机号码
     */
    public void setCFddbrSjhm(String cFddbrSjhm) {
        CFddbrSjhm = cFddbrSjhm;
    }

    /**
     * 获取 法定代表人联系电话(固话)
     * @return 法定代表人联系电话(固话)
     */
    public String getCFddbrLxdh() {
        return CFddbrLxdh;
    }

    /**
     * 设置 法定代表人联系电话(固话)
     * @param cFddbrLxdh 法定代表人联系电话(固话)
     */
    public void setCFddbrLxdh(String cFddbrLxdh) {
        CFddbrLxdh = cFddbrLxdh;
    }

    
}