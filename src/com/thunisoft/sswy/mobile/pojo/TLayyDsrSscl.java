package com.thunisoft.sswy.mobile.pojo;

/**
 * 立案预约_当事人_诉讼材料
 * TLayyDsrSscl entity. @author MyEclipse Persistence Tools
 */

public class TLayyDsrSscl implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4885434921005395149L;
	
	/**
	 * 当事人_诉讼材料Id
	 */
	private String CId;
	
	/**
	 * 当事人id
	 */
    private String CDsrId;
    
    /**
     * 当事人名称
     */
    private String CDsrName;
    
    /**
     * 诉讼材料id
     */
    private String CSsclId;
    
    /**
     * 诉讼材料名称
     */
    private String CSsclName;
    
    /**
     * 立案预约id
     */
    private String CLayyId;

    /** default constructor */
    public TLayyDsrSscl() {
    }

    /**
     * 获取 当事人_诉讼材料Id
     * @return 当事人_诉讼材料Id
     */
    public String getCId() {
        return CId;
    }

    /**
     * 设置 当事人_诉讼材料Id
     * @param cId 当事人_诉讼材料Id
     */
    public void setCId(String cId) {
        CId = cId;
    }

    /**
     * 获取 当事人id
     * @return 当事人id
     */
    public String getCDsrId() {
        return CDsrId;
    }

    /**
     * 设置 当事人id
     * @param cDsrId 当事人id
     */
    public void setCDsrId(String cDsrId) {
        CDsrId = cDsrId;
    }

    /**
     * 获取 当事人名称
     * @return 当事人名称
     */
    public String getCDsrName() {
        return CDsrName;
    }

    /**
     * 设置 当事人名称
     * @param cDsrName 当事人名称
     */
    public void setCDsrName(String cDsrName) {
        CDsrName = cDsrName;
    }

    /**
     * 获取 诉讼材料id
     * @return 诉讼材料id
     */
    public String getCSsclId() {
        return CSsclId;
    }

    /**
     * 设置 诉讼材料id
     * @param cSsclId 诉讼材料id
     */
    public void setCSsclId(String cSsclId) {
        CSsclId = cSsclId;
    }

    /**
     * 获取 诉讼材料名称
     * @return 诉讼材料名称
     */
    public String getCSsclName() {
        return CSsclName;
    }

    /**
     * 设置 诉讼材料名称
     * @param cSsclName 诉讼材料名称
     */
    public void setCSsclName(String cSsclName) {
        CSsclName = cSsclName;
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

    
}