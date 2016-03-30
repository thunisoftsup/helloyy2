package com.thunisoft.sswy.mobile.pojo;

/**
 * TZj entity. @author MyEclipse Persistence Tools
 */
public class TZj implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3373152292653517001L;
	
	/**证据ID  */
    private String CId;
    
    /**业务证据编号  */
    private String CYwxtZjbh;
    
    /**序号  */
    private String CXh;
    
    /**关联app模块id  */
    private String CAppId;
    
    /**关联业务编号  */
    private String CYwBh;
    
    /**关联案件编号  */
    private String CAjBh;
    
    /**业务系统案件编号  */
    private String CYwxtAjbh;
    
    /**证据名称  */
    private String CName;
    
    /**证据类型  */
    private Integer NZjlx;
    
    /**提出人编号（原所属人编号）  */
    private String CSsryId;
    
    /**提出人名称（原所属人名称）  */
    private String CSsryMc;
    
    /**提出人诉讼地位（原所属人诉讼地位）  */
    private Integer NSsrySsdw;
    
    /**证据来源  */
    private String CZjly;
    
    /**证明问题  */
    private String CZmwt;
    
    /**证据状态  */
    private Integer NStauts;
    
    /**驳回原因  */
    private String CBhyy;
    
    /**是否发送NP  */
    private Integer NFs;
    
    /**发送结果  */
    private Integer NFsjg;
    
    /**创建时间  */
    private String DCreate;
    
    /**更新时间  */
    private String DUpdate;
    
    /**证据id  */
    private String CZjId;
    
    /**质证记录id  */
    private String CZzjlId;
    
    /**质证记录审核类型  */
    private String CZzjlShlx;
    
    /**NP质证编号  */
    private String CYwxtZzjlId;

    /** default constructor */
    public TZj() {
    }

    /**
     * 返回证据ID 
     *
     * @return  证据ID
     */
    public String getCId() {
        return this.CId;
    }

    /**
     * 设置证据ID 
     *
     * @param CId 证据ID
     */
    public void setCId(String CId) {
        this.CId = CId;
    }

    /**
     * 返回业务证据编号 
     *
     * @return  业务证据编号
     */
    public String getCYwxtZjbh() {
        return this.CYwxtZjbh;
    }

    /**
     * 设置业务证据编号 
     *
     * @param CYwxtZjbh 业务证据编号
     */
    public void setCYwxtZjbh(String CYwxtZjbh) {
        this.CYwxtZjbh = CYwxtZjbh;
    }

    /**
     * 返回序号 
     *
     * @return  序号
     */
    public String getCXh() {
        return this.CXh;
    }

    /**
     * 设置序号 
     *
     * @param CXh 序号
     */
    public void setCXh(String CXh) {
        this.CXh = CXh;
    }

    /**
     * 返回关联app模块id 
     *
     * @return  关联app模块id
     */
    public String getCAppId() {
        return this.CAppId;
    }

    /**
     * 设置关联app模块id 
     *
     * @param CAppId 关联app模块id
     */
    public void setCAppId(String CAppId) {
        this.CAppId = CAppId;
    }

    /**
     * 返回关联业务编号 
     *
     * @return  关联业务编号
     */
    public String getCYwBh() {
        return this.CYwBh;
    }

    /**
     * 设置关联业务编号 
     *
     * @param CYwBh 关联业务编号
     */
    public void setCYwBh(String CYwBh) {
        this.CYwBh = CYwBh;
    }

    /**
     * 返回关联案件编号 
     *
     * @return  关联案件编号
     */
    public String getCAjBh() {
        return this.CAjBh;
    }

    /**
     * 设置关联案件编号 
     *
     * @param CAjBh 关联案件编号
     */
    public void setCAjBh(String CAjBh) {
        this.CAjBh = CAjBh;
    }

    /**
     * 返回业务系统案件编号 
     *
     * @return  业务系统案件编号
     */
    public String getCYwxtAjbh() {
        return this.CYwxtAjbh;
    }

    /**
     * 设置业务系统案件编号 
     *
     * @param CYwxtAjbh 业务系统案件编号
     */
    public void setCYwxtAjbh(String CYwxtAjbh) {
        this.CYwxtAjbh = CYwxtAjbh;
    }

    /**
     * 返回证据名称 
     *
     * @return  证据名称
     */
    public String getCName() {
        return this.CName;
    }

    /**
     * 设置证据名称 
     *
     * @param CName 证据名称
     */
    public void setCName(String CName) {
        this.CName = CName;
    }

    /**
     * 返回证据类型 
     *
     * @return  证据类型
     */
    public Integer getNZjlx() {
        return this.NZjlx;
    }

    /**
     * 设置证据类型 
     *
     * @param NZjlx 证据类型
     */
    public void setNZjlx(Integer NZjlx) {
        this.NZjlx = NZjlx;
    }

    /**
     * 返回提出人编号（原所属人编号） 
     *
     * @return  提出人编号（原所属人编号）
     */
    public String getCSsryId() {
        return this.CSsryId;
    }

    /**
     * 设置提出人编号（原所属人编号） 
     *
     * @param CSsryId 提出人编号（原所属人编号）
     */
    public void setCSsryId(String CSsryId) {
        this.CSsryId = CSsryId;
    }

    /**
     * 返回提出人名称（原所属人名称） 
     *
     * @return  提出人名称（原所属人名称）
     */
    public String getCSsryMc() {
        return this.CSsryMc;
    }

    /**
     * 设置提出人名称（原所属人名称） 
     *
     * @param CSsryMc 提出人名称（原所属人名称）
     */
    public void setCSsryMc(String CSsryMc) {
        this.CSsryMc = CSsryMc;
    }

    /**
     * 返回提出人诉讼地位（原所属人诉讼地位） 
     *
     * @return  提出人诉讼地位（原所属人诉讼地位）
     */
    public Integer getNSsrySsdw() {
        return this.NSsrySsdw;
    }

    /**
     * 设置提出人诉讼地位（原所属人诉讼地位） 
     *
     * @param NSsrySsdw 提出人诉讼地位（原所属人诉讼地位）
     */
    public void setNSsrySsdw(Integer NSsrySsdw) {
        this.NSsrySsdw = NSsrySsdw;
    }

    /**
     * 返回证据来源 
     *
     * @return  证据来源
     */
    public String getCZjly() {
        return this.CZjly;
    }

    /**
     * 设置证据来源 
     *
     * @param CZjly 证据来源
     */
    public void setCZjly(String CZjly) {
        this.CZjly = CZjly;
    }

    /**
     * 返回证明问题 
     *
     * @return  证明问题
     */
    public String getCZmwt() {
        return this.CZmwt;
    }

    /**
     * 设置证明问题 
     *
     * @param CZmwt 证明问题
     */
    public void setCZmwt(String CZmwt) {
        this.CZmwt = CZmwt;
    }

    /**
     * 返回证据状态 
     *
     * @return  证据状态
     */
    public Integer getNStauts() {
        return this.NStauts;
    }

    /**
     * 设置证据状态 
     *
     * @param NStauts 证据状态
     */
    public void setNStauts(Integer NStauts) {
        this.NStauts = NStauts;
    }

    /**
     * 返回驳回原因 
     *
     * @return  驳回原因
     */
    public String getCBhyy() {
        return this.CBhyy;
    }

    /**
     * 设置驳回原因 
     *
     * @param CBhyy 驳回原因
     */
    public void setCBhyy(String CBhyy) {
        this.CBhyy = CBhyy;
    }

    /**
     * 返回是否发送NP 
     *
     * @return  是否发送NP
     */
    public Integer getNFs() {
        return this.NFs;
    }

    /**
     * 设置是否发送NP 
     *
     * @param NFs 是否发送NP
     */
    public void setNFs(Integer NFs) {
        this.NFs = NFs;
    }

    /**
     * 返回发送结果 
     *
     * @return  发送结果
     */
    public Integer getNFsjg() {
        return this.NFsjg;
    }

    /**
     * 设置发送结果 
     *
     * @param NFsjg 发送结果
     */
    public void setNFsjg(Integer NFsjg) {
        this.NFsjg = NFsjg;
    }

    /**
     * 返回创建时间 
     *
     * @return  创建时间
     */
    public String getDCreate() {
        return this.DCreate;
    }

    /**
     * 设置创建时间 
     *
     * @param DCreate 创建时间
     */
    public void setDCreate(String DCreate) {
        this.DCreate = DCreate;
    }

    /**
     * 返回更新时间 
     *
     * @return  更新时间
     */
    public String getDUpdate() {
        return this.DUpdate;
    }

    /**
     * 设置更新时间 
     *
     * @param DUpdate 更新时间
     */
    public void setDUpdate(String DUpdate) {
        this.DUpdate = DUpdate;
    }

    /**
     * 返回证据id 
     *
     * @return  证据id
     */
    public String getCZjId() {
        return this.CZjId;
    }

    /**
     * 设置证据id 
     *
     * @param CZjId 证据id
     */
    public void setCZjId(String CZjId) {
        this.CZjId = CZjId;
    }

    /**
     * 返回质证记录id 
     *
     * @return  质证记录id
     */
    public String getCZzjlId() {
        return this.CZzjlId;
    }

    /**
     * 设置质证记录id 
     *
     * @param CZzjlId 质证记录id
     */
    public void setCZzjlId(String CZzjlId) {
        this.CZzjlId = CZzjlId;
    }

    /**
     * 返回质证记录审核类型 
     *
     * @return  质证记录审核类型
     */
    public String getCZzjlShlx() {
        return this.CZzjlShlx;
    }

    /**
     * 设置质证记录审核类型 
     *
     * @param CZzjlShlx 质证记录审核类型
     */
    public void setCZzjlShlx(String CZzjlShlx) {
        this.CZzjlShlx = CZzjlShlx;
    }

    /**
     * 返回NP质证编号 
     *
     * @return  NP质证编号
     */
    public String getCYwxtZzjlId() {
        return CYwxtZzjlId;
    }

    /**
     * 设置NP质证编号 
     *
     * @param cYwxtZzjlId NP质证编号
     */
    public void setCYwxtZzjlId(String cYwxtZzjlId) {
        CYwxtZzjlId = cYwxtZzjlId;
    }

}
