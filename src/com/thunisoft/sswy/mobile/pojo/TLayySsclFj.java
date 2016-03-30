package com.thunisoft.sswy.mobile.pojo;

/**
 * 立案预约_诉讼材料_附件
 * TLayySsclFj entity. @author MyEclipse Persistence Tools
 */

public class TLayySsclFj implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4103988509832056682L;
	
	/**
	 * 诉讼材料_附件id
	 */
	private String CId;
	
	/**
	 * 立案预约id
	 */
    private String CLayyId;
    
    /**
     * 诉讼材料id
     */
    private String CSsclId;
    
    /**
     * 原始文件名称
     */
    private String COriginName;
    
    /**
     * 存储路径
     */
    private String CPath;
    
    /**
     * 显示顺序
     */
    private Integer NXssx;
    
    /**
     * 是否已和服务器同步
     */
    private Integer NSync;

    /** default constructor */
    public TLayySsclFj() {
    	
    }

    /**
     * 获取 诉讼材料_附件id
     * @return 诉讼材料_附件id
     */
    public String getCId() {
        return CId;
    }

    /**
     * 设置 诉讼材料_附件id
     * @param cId 诉讼材料_附件id
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

	/**  
	 * 获取  是否已和服务器同步  
	 * @return nSync  
	 */
	public Integer getNSync() {
		return NSync;
	}

	/**  
	 * 设置  是否已和服务器同步  
	 * @param nSync
	 */
	public void setNSync(Integer nSync) {
		NSync = nSync;
	}
}