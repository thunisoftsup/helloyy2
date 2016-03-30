package com.thunisoft.sswy.mobile.pojo;

/**
 * TZjcl entity. @author MyEclipse Persistence Tools
 */
public class TZjcl implements java.io.Serializable {

	private static final long serialVersionUID = 242257925746271530L;

    /**证据材料ID  */
	private String CId;
	
    /**关联证据编号  */
	private String CZjBh;
	
    /**关联审核编号  */
	private String CShBh;
	
    /**原始文件名称  */
	private String COriginName;
	
    /**存储路径  */
	private String CPath;
	
    /**创建时间  */
	private String DCreate;
	
    /**审核类型 1-真实性审核，2-证明问题性审核  */
	private Integer NShType;

	/** 是否同步服务器 */
	private Integer NSync; 
	
	/** default constructor */
	public TZjcl() {
		
	}

    /**
     * 返回证据材料ID 
     *
     * @return  证据材料ID
     */
	public String getCId() {
		return this.CId;
	}

    /**
     * 设置证据材料ID 
     *
     * @param CId 证据材料ID
     */
	public void setCId(String CId) {
		this.CId = CId;
	}

    /**
     * 返回关联证据编号 
     *
     * @return  关联证据编号
     */
	public String getCZjBh() {
		return this.CZjBh;
	}

    /**
     * 设置关联证据编号 
     *
     * @param CZjBh 关联证据编号
     */
	public void setCZjBh(String CZjBh) {
		this.CZjBh = CZjBh;
	}

    /**
     * 返回关联审核编号 
     *
     * @return  关联审核编号
     */
	public String getCShBh() {
		return CShBh;
	}

    /**
     * 设置关联审核编号 
     *
     * @param cShBh 关联审核编号
     */
	public void setCShBh(String cShBh) {
		CShBh = cShBh;
	}

    /**
     * 返回原始文件名称 
     *
     * @return  原始文件名称
     */
	public String getCOriginName() {
		return this.COriginName;
	}

    /**
     * 设置原始文件名称 
     *
     * @param COriginName 原始文件名称
     */
	public void setCOriginName(String COriginName) {
		this.COriginName = COriginName;
	}

    /**
     * 返回存储路径 
     *
     * @return  存储路径
     */
	public String getCPath() {
		return this.CPath;
	}

    /**
     * 设置存储路径 
     *
     * @param CPath 存储路径
     */
	public void setCPath(String CPath) {
		this.CPath = CPath;
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
     * 返回审核类型 1-真实性审核，2-证明问题性审核 
     *
     * @return  审核类型
     */
    public Integer getNShType() {
        return NShType;
    }

    /**
     * 设置审核类型 1-真实性审核，2-证明问题性审核 
     *
     * @param nShType 审核类型
     */
    public void setNShType(Integer nShType) {
        NShType = nShType;
    }

	/**  
	 * 获取  是否同步服务器  
	 * @return nSync  
	 */
	public Integer getNSync() {
		return NSync;
	}

	/**  
	 * 设置  是否同步服务器  
	 * @param nSync
	 */
	public void setNSync(Integer nSync) {
		NSync = nSync;
	}
}
