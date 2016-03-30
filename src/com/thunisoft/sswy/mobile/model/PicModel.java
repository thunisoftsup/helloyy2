package com.thunisoft.sswy.mobile.model;

import java.io.Serializable;

/**
 * 图片 model类，用于在证件列表等，点击查看大图时传数据
 * @author gewx
 *
 */
public class PicModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**诉讼材料_附件_证件*/
	public static final int TYPE_SSCL_FJ_ZJ = 1;

	/**诉讼材料_附件_授权委托书*/
	public static final int TYPE_SSCL_FJ_SQWTS = 2;
	
	/**证据材料*/
	public static final int TYPE_ZJCL = 3;
	
	/**申请人身份认证_材料*/
	public static final int TYPE_SFRZ_CL = 4;

	/**
	 * 相关业务Id
	 */
	private String relId;
	
	/**
	 * 相关业务 父Id（比如证据材料，需要传入证据id）
	 */
	private String relPid;
	
	/**
	 * 图片路径
	 */
	private String path;
	
	/**
	 * 类型：1-诉讼材料附件，2-证据材料， 3-申请人身份认证_材料
	 */
	private int type;
	
	/**
	 * 图片名称
	 */
	private String name;

	public PicModel() {
		
	}

	/**  
	 * 获取  相关业务Id
	 * @return relId  
	 */
	public String getRelId() {
		return relId;
	}

	/**  
	 * 设置 相关业务Id
	 * @param relId
	 */
	public void setRelId(String relId) {
		this.relId = relId;
	}

	/**  
	 * 获取  图片路径
	 * @return path  
	 */
	public String getPath() {
		return path;
	}

	/**  
	 * 设置  图片路径
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**  
	 * 获取  类型：1-诉讼材料附件，2-证据材料， 3-申请人身份认证_材料
	 * @return type  
	 */
	public int getType() {
		return type;
	}

	/**  
	 * 设置  类型：1-诉讼材料附件，2-证据材料， 3-申请人身份认证_材料
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**  
	 * 获取  图片名称
	 * @return name  
	 */
	public String getName() {
		return name;
	}

	/**  
	 * 设置  图片名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**  
	 * 获取  relPid  
	 * @return relPid  
	 */
	public String getRelPid() {
		return relPid;
	}

	/**  
	 * 设置  relPid  
	 * @param relPid
	 */
	public void setRelPid(String relPid) {
		this.relPid = relPid;
	}
}
