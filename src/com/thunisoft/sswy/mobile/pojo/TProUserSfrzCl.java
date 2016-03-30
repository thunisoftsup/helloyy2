package com.thunisoft.sswy.mobile.pojo;
public class TProUserSfrzCl implements java.io.Serializable {

	private static final long serialVersionUID = 8150863241857202920L;
    /**编号  */
	private String CBh;
    /**立案预约编号  */
	private String CLayyId;
    /**身份认证id  */
	private String CProUserSfrzId;
    /**用户编号  */
	private String CProUserId;
    /**材料名称  */
	private String CClName;
    /**材料存储路径  */
	private String CClPath;
    /**材料类型  */
	private Integer NClLx;
    /**创建时间  */
	private String DCreate;
	/**是否与服务器同步*/
    private Integer NSync;	
    /**
     * 返回编号 
     *
     * @return  编号
     */
	public String getCBh() {
		return CBh;
	}
    /**
     * 设置编号 
     *
     * @param cBh 编号
     */
	public void setCBh(String cBh) {
		CBh = cBh;
	}
    /**
     * 返回立案预约编号 
     *
     * @return  立案预约编号
     */
	public String getCLayyId() {
		return CLayyId;
	}
    /**
     * 设置立案预约编号 
     *
     * @param cLayyId 立案预约编号
     */
	public void setCLayyId(String cLayyId) {
		CLayyId = cLayyId;
	}
    /**
     * 返回身份认证id 
     *
     * @return  身份认证id
     */
	public String getCProUserSfrzId() {
		return CProUserSfrzId;
	}
    /**
     * 设置身份认证id 
     *
     * @param cProUserSfrzId 身份认证id
     */
	public void setCProUserSfrzId(String cProUserSfrzId) {
		CProUserSfrzId = cProUserSfrzId;
	}
    /**
     * 返回用户编号 
     *
     * @return  用户编号
     */
	public String getCProUserId() {
		return CProUserId;
	}
    /**
     * 设置用户编号 
     *
     * @param cProUserId 用户编号
     */
	public void setCProUserId(String cProUserId) {
		CProUserId = cProUserId;
	}
	
    /**
     * 返回创建时间 
     *
     * @return  创建时间
     */
	public String getDCreate() {
		return DCreate;
	}
    /**
     * 设置创建时间 
     *
     * @param dCreate 创建时间
     */
	public void setDCreate(String dCreate) {
		DCreate = dCreate;
	}
    /**
     * 返回材料名称 
     *
     * @return  材料名称
     */
	public String getCClName() {
		return CClName;
	}
    /**
     * 设置材料名称 
     *
     * @param cClName 材料名称
     */
	public void setCClName(String cClName) {
		CClName = cClName;
	}
    /**
     * 返回材料存储路径 
     *
     * @return  材料存储路径
     */
	public String getCClPath() {
		return CClPath;
	}
    /**
     * 设置材料存储路径 
     *
     * @param cClPath 材料存储路径
     */
	public void setCClPath(String cClPath) {
		CClPath = cClPath;
	}
    /**
     * 返回材料类型 
     *
     * @return  材料类型
     */
	public Integer getNClLx() {
		return NClLx;
	}
    /**
     * 设置材料类型 
     *
     * @param nClLx 材料类型
     */
	public void setNClLx(Integer nClLx) {
		NClLx = nClLx;
	}
	/**  
	 * 获取  是否与服务器同步  
	 * @return nSync  
	 */
	public Integer getNSync() {
		return NSync;
	}
	/**  
	 * 设置  是否与服务器同步  
	 * @param nSync
	 */
	public void setNSync(Integer nSync) {
		NSync = nSync;
	}
}
