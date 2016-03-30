package com.thunisoft.sswy.mobile.pojo;

/**
 * 立案预约_诉讼材料
 * TLayySscl entity. @author MyEclipse Persistence Tools
 */

public class TLayySscl implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1265514800753889620L;
	
	/**
	 * 材料id
	 */
	private String CId;
	
	/**
	 * 立案预约id
	 */
    private String CLayyId;
    
    /**
     * 材料类型
     */
    private Integer NType;
    
    /**
     * 材料名称
     */
    private String CName;
    
    /**
     * 所属人id
     */
    private String CSsryId;
    
    /**
     * 所属人名称
     */
    private String CSsryMc;
    
    /**
     * 原始文件名称
     */
    private String COriginName;
    
    /**
     * 存储路径
     */
    private String CPath;
    
    /**
     * 来源
     */
    private String CLy;
    
    /**
     * 显示顺序
     */
    private Integer NXssx;

    /** default constructor */
    public TLayySscl() {
    }

    /**
     * 获取 材料id
     * @return 材料id
     */
    public String getCId() {
        return CId;
    }

    /**
     * 设置 材料id
     * @param cId 材料id
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
     * 获取 材料类型
     * @return 材料类型
     */
    public Integer getNType() {
        return NType;
    }

    /**
     * 设置 材料类型
     * @param nType 材料类型
     */
    public void setNType(Integer nType) {
        NType = nType;
    }

    /**
     * 获取 材料名称
     * @return 材料名称
     */
    public String getCName() {
        return CName;
    }

    /**
     * 设置 材料名称
     * @param cName 材料名称
     */
    public void setCName(String cName) {
        CName = cName;
    }

    /**
     * 获取 所属人id
     * @return 所属人id
     */
    public String getCSsryId() {
        return CSsryId;
    }

    /**
     * 设置 所属人id
     * @param cSsryId 所属人id
     */
    public void setCSsryId(String cSsryId) {
        CSsryId = cSsryId;
    }

    /**
     * 获取 所属人名称
     * @return 所属人名称
     */
    public String getCSsryMc() {
        return CSsryMc;
    }

    /**
     * 设置 所属人名称
     * @param cSsryMc 所属人名称
     */
    public void setCSsryMc(String cSsryMc) {
        CSsryMc = cSsryMc;
    }

    /**
     * 获取 原始文件名称
     * @return 原始文件名称
     */
    public String getCOriginName() {
        return COriginName;
    }

    /**
     * 设置 原始文件名称
     * @param cOriginName 原始文件名称
     */
    public void setCOriginName(String cOriginName) {
        COriginName = cOriginName;
    }

    /**
     * 获取 存储路径
     * @return 存储路径
     */
    public String getCPath() {
        return CPath;
    }

    /**
     * 设置 存储路径
     * @param cPath 存储路径
     */
    public void setCPath(String cPath) {
        CPath = cPath;
    }

    /**
     * 获取 来源
     * @return 来源
     */
    public String getCLy() {
        return CLy;
    }

    /**
     * 设置 来源
     * @param cLy 来源
     */
    public void setCLy(String cLy) {
        CLy = cLy;
    }

    /**
     * 获取 显示顺序
     * @return 显示顺序
     */
    public Integer getNXssx() {
        return NXssx;
    }

    /**
     * 设置 显示顺序
     * @param nXssx 显示顺序
     */
    public void setNXssx(Integer nXssx) {
        NXssx = nXssx;
    }

    
}