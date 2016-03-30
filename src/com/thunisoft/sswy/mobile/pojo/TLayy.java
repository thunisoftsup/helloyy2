package com.thunisoft.sswy.mobile.pojo;


/**
 * 立案预约_基本信息
 * TLayy entity. @author MyEclipse Persistence Tools
 */

public class TLayy implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4326897564351072347L;
	
	/**
	 * 主键Id
	 */
	private String CId;
	
	/**
	 * 法院Id
	 */
    private String CCourtId;
    
    /**
     * 法院名称
     */
    private String CCourtName;
    
    /**
     * 原审法院id
     */
    private String CYsfyId;
    
    /**
     * 原审法院名称
     */
    private String CYsfyName;
    
    /**
     * 案件类型 代码值
     */
    private Integer NAjlx;
    
    /**
     * 案件类型 名称
     */
    private String CAjlx;
    
    /**
     * 审判程序 代码值
     */
    private Integer NSpcx;
    
    /**
     * 审判程序名称
     */
    private String CSpcx;
    
    /**
     * 原审案件编号
     */
    private String CYsajBh;
    
    /**
     * 原审案号
     */
    private String CYsah;
    
    /**
     * 原审案号_年号
     */
    private String CYsahNh;
    
    /**
     * （原审案号）简称和审判字号
     */
    private String CYsahJcspzh;
    
    /**
     * （原审案号）排行号
     */
    private String CYsahPhh;
    
    /**
     * （原审案号）子排行号
     */
    private String CYsahZphh;
    
    /**
     * 诉讼请求
     */
    private String CSsqq;
    
    /**
     * 案情说明
     */
    private String CAqsm;
    
    /**
     * 当事人
     */
    private String CDsr;
    
    /**
     * 状态
     */
    private Integer NStatus;
    
    /**
     * 再次提交
     */
    private Integer NZctj;
    
    /**
     * 创建时间
     */
    private String DCreate;
    
    /**
     * 更新时间
     */
    private String DUpdate;
    
    /**
     * 申请标的金额
     */
    private String CSqbdje;
    
    /**
     * 申请标的物
     */
    private String CSqbdw;
    
    /**
     * 申请标的行为
     */
    private String CSqbdxw;
    
    /**
     * 执行依据类型 代码值
     */
    private Integer NZxyjlx;
    
    /**
     * 执行依据类型 名称
     */
    private String CZxyjlx;
    
    /**
     * 执行依据类型 代码值
     */
    private String CZxyjwsbh;
    
    /**
     * 执行依据文书编号
     */
    private Integer NSqrLy;
    
    /**
     * 申请人来源 代码值
     */
    private Integer NSqrRzqk;
    
    /**
     * 申请人来源名称
     */
    private String CSqrRzqk;
    
    /**
     * 申请人身份代码
     */
    private Integer NSqrSf;
    
    /**
     * 申请人身份名称
     */
    private String CSqrSf;
    
    /**
     * 申请人id（如果申请人是当事人则是当事人id，如果是代理人是代理人id）
     */
    private String CSqrId;
    
    /**
    * 申请人 名称（如果申请人是当事人则是当事人 名称，如果是代理人是代理人 名称）
    */
    private String CSqrName;
    
    /**
     * 专业人员id（申请人）
     */
    private String CProUserId;
    
    /**
     * 专业人员名称（申请人）
     */
    private String CProUserName;
    
    /**
     * 证件类型
     */
    private Integer NIdcardType;
    
    /**
     * 证件号码
     */
    private String CIdcard;
    
    /**
     * 手机号码
     */
    private String CSjhm;
    
    /**
     * 电子邮箱
     */
    private String CDzyx;
    
    /**
     * 执业证号
     */
    private String CZyzh;
    
    /**
     * 律所名称
     */
    private String CLsmc;
    
    /**
     * 是否发送np系统
     */
    private Integer NFs;
    
    /**
     * 发送np系统处理结果 代码值
     */
    private Integer NFsjg;
    
    /**
     * 发送np系统处理结果名称
     */
    private String CFsjg;
    
    /**
     * 业务系统id(np案件主键)
     */
    private String CYwxtJlid;
    
    /**
     * 案号
     */
    private String CAh;
    
    /**
     * 案件名称
     */
    private String CAjmc;
    
    /**
     * 案由代码
     */
    private Integer NAy;
    
    /**
     * 案由名称
     */
    private String CAy;
    
    /**
     * 应交纳诉讼费用金额
     */
    private String NYjssfje;
    
    /**
     * 是否已缴费
     */
    private Integer NYjf;
    
    /**
     * 是否已收案
     */
    private Integer NYsa;
    
    /**
     * 是否已正式立案
     */
    private Integer NYla;
    
    /**
     * 是否移送
     */
    private Integer NYs;
    
    /**
     * 业务系统原审案件编号
     */
    private String CYwxtYsAjBh;
    
    /**
     * 显示顺序
     */
    private Integer NXxsx;
    
    /**
     * 法律依据
     */
    private String CFlyj;
    
    /**
     * 是否已同步服务器
     */
    private int NSync;
    
    /** default constructor */
    public TLayy() {
    	
    }

    /**
     * 获取 主键Id
     * @return 主键Id
     */
    public String getCId() {
        return CId;
    }

    /**
     * 设置 主键Id
     * @param cId 主键Id
     */
    public void setCId(String cId) {
        CId = cId;
    }

    /**
     * 获取 法院Id
     * @return 法院Id
     */
    public String getCCourtId() {
        return CCourtId;
    }

    /**
     * 设置 法院Id
     * @param cCourtId 法院Id
     */
    public void setCCourtId(String cCourtId) {
        CCourtId = cCourtId;
    }

    /**
     * 获取 法院名称
     * @return 法院名称
     */
    public String getCCourtName() {
        return CCourtName;
    }

    /**
     * 设置 法院名称
     * @param cCourtName 法院名称
     */
    public void setCCourtName(String cCourtName) {
        CCourtName = cCourtName;
    }

    /**
     * 获取 原审法院id
     * @return 原审法院id
     */
    public String getCYsfyId() {
        return CYsfyId;
    }

    /**
     * 设置 原审法院id
     * @param cYsfyId 原审法院id
     */
    public void setCYsfyId(String cYsfyId) {
        CYsfyId = cYsfyId;
    }

    /**
     * 获取 原审法院名称
     * @return 原审法院名称
     */
    public String getCYsfyName() {
        return CYsfyName;
    }

    /**
     * 设置 原审法院名称
     * @param cYsfyName 原审法院名称
     */
    public void setCYsfyName(String cYsfyName) {
        CYsfyName = cYsfyName;
    }

    /**
     * 获取 案件类型 代码值
     * @return 案件类型 代码值
     */
    public Integer getNAjlx() {
        return NAjlx;
    }

    /**
     * 设置 案件类型 代码值
     * @param nAjlx 案件类型 代码值
     */
    public void setNAjlx(Integer nAjlx) {
        NAjlx = nAjlx;
    }

    /**
     * 获取 案件类型 名称
     * @return 案件类型 名称
     */
    public String getCAjlx() {
        return CAjlx;
    }

    /**
     * 设置 案件类型 名称
     * @param cAjlx 案件类型 名称
     */
    public void setCAjlx(String cAjlx) {
        CAjlx = cAjlx;
    }

    /**
     * 获取 审判程序 代码值
     * @return 审判程序 代码值
     */
    public Integer getNSpcx() {
        return NSpcx;
    }

    /**
     * 设置 审判程序 代码值
     * @param nSpcx 审判程序 代码值
     */
    public void setNSpcx(Integer nSpcx) {
        NSpcx = nSpcx;
    }

    /**
     * 获取 审判程序名称
     * @return 审判程序名称
     */
    public String getCSpcx() {
        return CSpcx;
    }

    /**
     * 设置 审判程序名称
     * @param cSpcx 审判程序名称
     */
    public void setCSpcx(String cSpcx) {
        CSpcx = cSpcx;
    }

    /**
     * 获取 原审案件编号
     * @return 原审案件编号
     */
    public String getCYsajBh() {
        return CYsajBh;
    }

    /**
     * 设置 原审案件编号
     * @param cYsajBh 原审案件编号
     */
    public void setCYsajBh(String cYsajBh) {
        CYsajBh = cYsajBh;
    }

    /**
     * 获取 原审案号
     * @return 原审案号
     */
    public String getCYsah() {
        return CYsah;
    }

    /**
     * 设置 原审案号
     * @param cYsah 原审案号
     */
    public void setCYsah(String cYsah) {
        CYsah = cYsah;
    }

    /**
     * 获取 原审案号_年号
     * @return 原审案号_年号
     */
    public String getCYsahNh() {
        return CYsahNh;
    }

    /**
     * 设置 原审案号_年号
     * @param cYsahNh 原审案号_年号
     */
    public void setCYsahNh(String cYsahNh) {
        CYsahNh = cYsahNh;
    }

    /**
     * 获取 （原审案号）简称和审判字号
     * @return （原审案号）简称和审判字号
     */
    public String getCYsahJcspzh() {
        return CYsahJcspzh;
    }

    /**
     * 设置 （原审案号）简称和审判字号
     * @param cYsahJcspzh （原审案号）简称和审判字号
     */
    public void setCYsahJcspzh(String cYsahJcspzh) {
        CYsahJcspzh = cYsahJcspzh;
    }

    /**
     * 获取 （原审案号）排行号
     * @return （原审案号）排行号
     */
    public String getCYsahPhh() {
        return CYsahPhh;
    }

    /**
     * 设置 （原审案号）排行号
     * @param cYsahPhh （原审案号）排行号
     */
    public void setCYsahPhh(String cYsahPhh) {
        CYsahPhh = cYsahPhh;
    }

    /**
     * 获取 （原审案号）子排行号
     * @return （原审案号）子排行号
     */
    public String getCYsahZphh() {
        return CYsahZphh;
    }

    /**
     * 设置 （原审案号）子排行号
     * @param cYsahZphh （原审案号）子排行号
     */
    public void setCYsahZphh(String cYsahZphh) {
        CYsahZphh = cYsahZphh;
    }

    /**
     * 获取 诉讼请求
     * @return 诉讼请求
     */
    public String getCSsqq() {
        return CSsqq;
    }

    /**
     * 设置 诉讼请求
     * @param cSsqq 诉讼请求
     */
    public void setCSsqq(String cSsqq) {
        CSsqq = cSsqq;
    }

    /**
     * 获取 案情说明
     * @return 案情说明
     */
    public String getCAqsm() {
        return CAqsm;
    }

    /**
     * 设置 案情说明
     * @param cAqsm 案情说明
     */
    public void setCAqsm(String cAqsm) {
        CAqsm = cAqsm;
    }

    /**
     * 获取 当事人
     * @return 当事人
     */
    public String getCDsr() {
        return CDsr;
    }

    /**
     * 设置 当事人
     * @param cDsr 当事人
     */
    public void setCDsr(String cDsr) {
        CDsr = cDsr;
    }

    /**
     * 获取 状态
     * @return 状态
     */
    public Integer getNStatus() {
        return NStatus;
    }

    /**
     * 设置 状态
     * @param nStatus 状态
     */
    public void setNStatus(Integer nStatus) {
        NStatus = nStatus;
    }

    /**
     * 获取 再次提交
     * @return 再次提交
     */
    public Integer getNZctj() {
        return NZctj;
    }

    /**
     * 设置 再次提交
     * @param nZctj 再次提交
     */
    public void setNZctj(Integer nZctj) {
        NZctj = nZctj;
    }

    /**
     * 获取 创建时间
     * @return 创建时间
     */
    public String getDCreate() {
        return DCreate;
    }

    /**
     * 设置 创建时间
     * @param dCreate 创建时间
     */
    public void setDCreate(String dCreate) {
        DCreate = dCreate;
    }

    /**
     * 获取 更新时间
     * @return 更新时间
     */
    public String getDUpdate() {
        return DUpdate;
    }

    /**
     * 设置 更新时间
     * @param dUpdate 更新时间
     */
    public void setDUpdate(String dUpdate) {
        DUpdate = dUpdate;
    }

    /**
     * 获取 申请标的金额
     * @return 申请标的金额
     */
    public String getCSqbdje() {
        return CSqbdje;
    }

    /**
     * 设置 申请标的金额
     * @param cSqbdje 申请标的金额
     */
    public void setCSqbdje(String cSqbdje) {
        CSqbdje = cSqbdje;
    }

    /**
     * 获取 申请标的物
     * @return 申请标的物
     */
    public String getCSqbdw() {
        return CSqbdw;
    }

    /**
     * 设置 申请标的物
     * @param cSqbdw 申请标的物
     */
    public void setCSqbdw(String cSqbdw) {
        CSqbdw = cSqbdw;
    }

    /**
     * 获取 申请标的行为
     * @return 申请标的行为
     */
    public String getCSqbdxw() {
        return CSqbdxw;
    }

    /**
     * 设置 申请标的行为
     * @param cSqbdxw 申请标的行为
     */
    public void setCSqbdxw(String cSqbdxw) {
        CSqbdxw = cSqbdxw;
    }

    /**
     * 获取 执行依据类型 代码值
     * @return 执行依据类型 代码值
     */
    public Integer getNZxyjlx() {
        return NZxyjlx;
    }

    /**
     * 设置 执行依据类型 代码值
     * @param nZxyjlx 执行依据类型 代码值
     */
    public void setNZxyjlx(Integer nZxyjlx) {
        NZxyjlx = nZxyjlx;
    }

    /**
     * 获取 执行依据类型 名称
     * @return 执行依据类型 名称
     */
    public String getCZxyjlx() {
        return CZxyjlx;
    }

    /**
     * 设置 执行依据类型 名称
     * @param cZxyjlx 执行依据类型 名称
     */
    public void setCZxyjlx(String cZxyjlx) {
        CZxyjlx = cZxyjlx;
    }

    /**
     * 获取 执行依据类型 代码值
     * @return 执行依据类型 代码值
     */
    public String getCZxyjwsbh() {
        return CZxyjwsbh;
    }

    /**
     * 设置 执行依据类型 代码值
     * @param cZxyjwsbh 执行依据类型 代码值
     */
    public void setCZxyjwsbh(String cZxyjwsbh) {
        CZxyjwsbh = cZxyjwsbh;
    }

    /**
     * 获取 执行依据文书编号
     * @return 执行依据文书编号
     */
    public Integer getNSqrLy() {
        return NSqrLy;
    }

    /**
     * 设置 执行依据文书编号
     * @param nSqrLy 执行依据文书编号
     */
    public void setNSqrLy(Integer nSqrLy) {
        NSqrLy = nSqrLy;
    }

    /**
     * 获取 申请人来源 代码值
     * @return 申请人来源 代码值
     */
    public Integer getNSqrRzqk() {
        return NSqrRzqk;
    }

    /**
     * 设置 申请人来源 代码值
     * @param nSqrRzqk 申请人来源 代码值
     */
    public void setNSqrRzqk(Integer nSqrRzqk) {
        NSqrRzqk = nSqrRzqk;
    }

    /**
     * 获取 申请人来源名称
     * @return 申请人来源名称
     */
    public String getCSqrRzqk() {
        return CSqrRzqk;
    }

    /**
     * 设置 申请人来源名称
     * @param cSqrRzqk 申请人来源名称
     */
    public void setCSqrRzqk(String cSqrRzqk) {
        CSqrRzqk = cSqrRzqk;
    }

    /**
     * 获取 申请人身份代码
     * @return 申请人身份代码
     */
    public Integer getNSqrSf() {
        return NSqrSf;
    }

    /**
     * 设置 申请人身份代码
     * @param nSqrSf 申请人身份代码
     */
    public void setNSqrSf(Integer nSqrSf) {
        NSqrSf = nSqrSf;
    }

    /**
     * 获取 申请人身份名称
     * @return 申请人身份名称
     */
    public String getCSqrSf() {
        return CSqrSf;
    }

    /**
     * 设置 申请人身份名称
     * @param cSqrSf 申请人身份名称
     */
    public void setCSqrSf(String cSqrSf) {
        CSqrSf = cSqrSf;
    }

    /**
     * 获取 申请人id（如果申请人是当事人则是当事人id，如果是代理人是代理人id）
     * @return 申请人id（如果申请人是当事人则是当事人id，如果是代理人是代理人id）
     */
    public String getCSqrId() {
        return CSqrId;
    }

    /**
     * 设置 申请人id（如果申请人是当事人则是当事人id，如果是代理人是代理人id）
     * @param cSqrId 申请人id（如果申请人是当事人则是当事人id，如果是代理人是代理人id）
     */
    public void setCSqrId(String cSqrId) {
        CSqrId = cSqrId;
    }

    /**  
	 * 获取  申请人 名称（如果申请人是当事人则是当事人 名称，如果是代理人是代理人 名称）
	 * @return cSqrName  
	 */
	
	public String getCSqrName() {
		return CSqrName;
	}

	/**  
	 * 设置  申请人 名称（如果申请人是当事人则是当事人 名称，如果是代理人是代理人 名称）
	 * @param cSqrName
	 */
	public void setCSqrName(String cSqrName) {
		CSqrName = cSqrName;
	}

	/**
     * 获取 专业人员id（申请人）
     * @return 专业人员id（申请人）
     */
    public String getCProUserId() {
        return CProUserId;
    }

    /**
     * 设置 专业人员id（申请人）
     * @param cProUserId 专业人员id（申请人）
     */
    public void setCProUserId(String cProUserId) {
        CProUserId = cProUserId;
    }

    /**
     * 获取 专业人员名称（申请人）
     * @return 专业人员名称（申请人）
     */
    public String getCProUserName() {
        return CProUserName;
    }

    /**
     * 设置 专业人员名称（申请人）
     * @param cProUserName 专业人员名称（申请人）
     */
    public void setCProUserName(String cProUserName) {
        CProUserName = cProUserName;
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
     * 获取 电子邮箱
     * @return 电子邮箱
     */
    public String getCDzyx() {
        return CDzyx;
    }

    /**
     * 设置 电子邮箱
     * @param cDzyx 电子邮箱
     */
    public void setCDzyx(String cDzyx) {
        CDzyx = cDzyx;
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
     * 获取 律所名称
     * @return 律所名称
     */
    public String getCLsmc() {
        return CLsmc;
    }

    /**
     * 设置 律所名称
     * @param cLsmc 律所名称
     */
    public void setCLsmc(String cLsmc) {
        CLsmc = cLsmc;
    }

    /**
     * 获取 是否发送np系统
     * @return 是否发送np系统
     */
    public Integer getNFs() {
        return NFs;
    }

    /**
     * 设置 是否发送np系统
     * @param nFs 是否发送np系统
     */
    public void setNFs(Integer nFs) {
        NFs = nFs;
    }

    /**
     * 获取 发送np系统处理结果 代码值
     * @return 发送np系统处理结果 代码值
     */
    public Integer getNFsjg() {
        return NFsjg;
    }

    /**
     * 设置 发送np系统处理结果 代码值
     * @param nFsjg 发送np系统处理结果 代码值
     */
    public void setNFsjg(Integer nFsjg) {
        NFsjg = nFsjg;
    }

    /**
     * 获取 发送np系统处理结果名称
     * @return 发送np系统处理结果名称
     */
    public String getCFsjg() {
        return CFsjg;
    }

    /**
     * 设置 发送np系统处理结果名称
     * @param cFsjg 发送np系统处理结果名称
     */
    public void setCFsjg(String cFsjg) {
        CFsjg = cFsjg;
    }

    /**
     * 获取 业务系统id(np案件主键)
     * @return 业务系统id(np案件主键)
     */
    public String getCYwxtJlid() {
        return CYwxtJlid;
    }

    /**
     * 设置 业务系统id(np案件主键)
     * @param cYwxtJlid 业务系统id(np案件主键)
     */
    public void setCYwxtJlid(String cYwxtJlid) {
        CYwxtJlid = cYwxtJlid;
    }

    /**
     * 获取 案号
     * @return 案号
     */
    public String getCAh() {
        return CAh;
    }

    /**
     * 设置 案号
     * @param cAh 案号
     */
    public void setCAh(String cAh) {
        CAh = cAh;
    }

    /**
     * 获取 案件名称
     * @return 案件名称
     */
    public String getCAjmc() {
        return CAjmc;
    }

    /**
     * 设置 案件名称
     * @param cAjmc 案件名称
     */
    public void setCAjmc(String cAjmc) {
        CAjmc = cAjmc;
    }

    /**
     * 获取 案由代码
     * @return 案由代码
     */
    public Integer getNAy() {
        return NAy;
    }

    /**
     * 设置 案由代码
     * @param nAy 案由代码
     */
    public void setNAy(Integer nAy) {
        NAy = nAy;
    }

    /**
     * 获取 案由名称
     * @return 案由名称
     */
    public String getCAy() {
        return CAy;
    }

    /**
     * 设置 案由名称
     * @param cAy 案由名称
     */
    public void setCAy(String cAy) {
        CAy = cAy;
    }

    /**
     * 获取 应交纳诉讼费用金额
     * @return 应交纳诉讼费用金额
     */
    public String getNYjssfje() {
        return NYjssfje;
    }

    /**
     * 设置 应交纳诉讼费用金额
     * @param nYjssfje 应交纳诉讼费用金额
     */
    public void setNYjssfje(String nYjssfje) {
        NYjssfje = nYjssfje;
    }

    /**
     * 获取 是否已缴费
     * @return 是否已缴费
     */
    public Integer getNYjf() {
        return NYjf;
    }

    /**
     * 设置 是否已缴费
     * @param nYjf 是否已缴费
     */
    public void setNYjf(Integer nYjf) {
        NYjf = nYjf;
    }

    /**
     * 获取 是否已收案
     * @return 是否已收案
     */
    public Integer getNYsa() {
        return NYsa;
    }

    /**
     * 设置 是否已收案
     * @param nYsa 是否已收案
     */
    public void setNYsa(Integer nYsa) {
        NYsa = nYsa;
    }

    /**
     * 获取 是否已正式立案
     * @return 是否已正式立案
     */
    public Integer getNYla() {
        return NYla;
    }

    /**
     * 设置 是否已正式立案
     * @param nYla 是否已正式立案
     */
    public void setNYla(Integer nYla) {
        NYla = nYla;
    }

    /**
     * 获取 是否移送
     * @return 是否移送
     */
    public Integer getNYs() {
        return NYs;
    }

    /**
     * 设置 是否移送
     * @param nYs 是否移送
     */
    public void setNYs(Integer nYs) {
        NYs = nYs;
    }

    /**
     * 获取 业务系统原审案件编号
     * @return 业务系统原审案件编号
     */
    public String getCYwxtYsAjBh() {
        return CYwxtYsAjBh;
    }

    /**
     * 设置 业务系统原审案件编号
     * @param cYwxtYsAjBh 业务系统原审案件编号
     */
    public void setCYwxtYsAjBh(String cYwxtYsAjBh) {
        CYwxtYsAjBh = cYwxtYsAjBh;
    }

    /**
     * 获取 显示顺序
     * @return 显示顺序
     */
    public Integer getNXxsx() {
        return NXxsx;
    }

    /**
     * 设置 显示顺序
     * @param nXxsx 显示顺序
     */
    public void setNXxsx(Integer nXxsx) {
        NXxsx = nXxsx;
    }

    /**
     * 获取 法律依据
     * @return 法律依据
     */
    public String getCFlyj() {
        return CFlyj;
    }

    /**
     * 设置 法律依据
     * @param cFlyj 法律依据
     */
    public void setCFlyj(String cFlyj) {
        CFlyj = cFlyj;
    }

	/**  
	 * 获取  是否已同步服务器  
	 * @return nSync  
	 */
	public int getNSync() {
		return NSync;
	}

	/**  
	 * 设置  是否已同步服务器  
	 * @param nSync
	 */
	public void setNSync(int nSync) {
		NSync = nSync;
	}

}