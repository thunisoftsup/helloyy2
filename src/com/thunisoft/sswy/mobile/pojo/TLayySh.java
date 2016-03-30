package com.thunisoft.sswy.mobile.pojo;


/**
 * 立案预约_审核
 * TLayySh entity. @author MyEclipse Persistence Tools
 */

public class TLayySh implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8859002884880941923L;
	
	/**
	 * 审核id
	 */
	private String CId;
	
	/**
	 * 立案预约id
	 */
    private String CLayyId;
    
    /**
     * 审核人id
     */
    private String CShrId;
    
    /**
     * 审核人姓名
     */
    private String CShrName;
    
    /**
     * 审核时间
     */
    private String DShsj;
    
    /**
     * 审核意见
     */
    private String CShyj;
    
    /**
     * 审核结果
     */
    private Integer NShjg;
    
    /**
     * 再次提交
     */
    private Integer NZctj;
    
    /**
     * 审判程序 代码值
     */
    private Integer NSpcx;
    
    /**
     * 审判程序 名称
     */
    private String CSpcx;
    
    /**
     * 驳回原因代码
     */
    private Integer NBhyy;
    
    /**
     * 驳回原因名称
     */
    private String CBhyy;

    /** default constructor */
    public TLayySh() {
    	
    }

    /**
     * 获取 审核id
     * @return 审核id
     */
    public String getCId() {
        return CId;
    }

    /**
     * 设置 审核id
     * @param cId 审核id
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
     * 获取 审核人id
     * @return 审核人id
     */
    public String getCShrId() {
        return CShrId;
    }

    /**
     * 设置 审核人id
     * @param cShrId 审核人id
     */
    public void setCShrId(String cShrId) {
        CShrId = cShrId;
    }

    /**
     * 获取 审核人姓名
     * @return 审核人姓名
     */
    public String getCShrName() {
        return CShrName;
    }

    /**
     * 设置 审核人姓名
     * @param cShrName 审核人姓名
     */
    public void setCShrName(String cShrName) {
        CShrName = cShrName;
    }

    /**
     * 获取 审核时间
     * @return 审核时间
     */
    public String getDShsj() {
        return DShsj;
    }

    /**
     * 设置 审核时间
     * @param dShsj 审核时间
     */
    public void setDShsj(String dShsj) {
        DShsj = dShsj;
    }

    /**
     * 获取 审核意见
     * @return 审核意见
     */
    public String getCShyj() {
        return CShyj;
    }

    /**
     * 设置 审核意见
     * @param cShyj 审核意见
     */
    public void setCShyj(String cShyj) {
        CShyj = cShyj;
    }

    /**
     * 获取 审核结果
     * @return 审核结果
     */
    public Integer getNShjg() {
        return NShjg;
    }

    /**
     * 设置 审核结果
     * @param nShjg 审核结果
     */
    public void setNShjg(Integer nShjg) {
        NShjg = nShjg;
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
     * 获取 审判程序 名称
     * @return 审判程序 名称
     */
    public String getCSpcx() {
        return CSpcx;
    }

    /**
     * 设置 审判程序 名称
     * @param cSpcx 审判程序 名称
     */
    public void setCSpcx(String cSpcx) {
        CSpcx = cSpcx;
    }

    /**
     * 获取 驳回原因代码
     * @return 驳回原因代码
     */
    public Integer getNBhyy() {
        return NBhyy;
    }

    /**
     * 设置 驳回原因代码
     * @param nBhyy 驳回原因代码
     */
    public void setNBhyy(Integer nBhyy) {
        NBhyy = nBhyy;
    }

    /**
     * 获取 驳回原因名称
     * @return 驳回原因名称
     */
    public String getCBhyy() {
        return CBhyy;
    }

    /**
     * 设置 驳回原因名称
     * @param cBhyy 驳回原因名称
     */
    public void setCBhyy(String cBhyy) {
        CBhyy = cBhyy;
    }

    
}