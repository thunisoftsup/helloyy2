package com.thunisoft.sswy.mobile.util.nrc;


public interface NrcConstants {

	/**
	 * 证件类型_身份证
	 */
	public int CERTIFICATE_TYPE_IDCARD = 1;
	
	/**
	 * 诉讼材料类型_证件
	 */
	public int SSCL_TYPE_CERTIFICATE = 1;
	
	/**
	 * 诉讼材料类型_起诉状
	 */
	public int SSCL_TYPE_INDICTMENT = 2;
	
	/**
	 * 证件所属人 类型：原告
	 */
	public int ZZSSR_TYPE_PLAINTIFF = 1;
	
	/**
	 * 证件所属人 类型：原告代理人
	 */
	public int ZZSSR_TYPE_PLAINTIFF_AGENT = 2;
	
	/**
	 * 人员类型：当事人
	 */
	public int PERSON_TYPE_LITIGANT = 1;
	
	/**
	 * 人员类型：代理人
	 */
	public int PERSON_TYPE_AGENT = 2;
	
	/**
	 * 选择类型：单选
	 */
	public int SELECT_TYPE_SINGLE = 1;
	
	/**
	 * 选择类型：多选
	 */
	public int SELECT_TYPE_MULTI = 2;
	
	/**
	 * 文件类型：所有
	 */
	public int FILE_TYPE_ALL = 0;
	
	/**
	 * 文件类型：图片
	 */
	public int FILE_TYPE_PIC = 1;
	
	/**
	 * 文件类型：音频
	 */
	public int FILE_TYPE_AUDIO = 2;
	
	/**
	 * 文件类型：视频
	 */
	public int FILE_TYPE_VIDEO = 3;
	
	/**
	 * 地址分隔符“-”
	 */
	public String ADDRESS_SPLIT = "-";
	
	/**
	 * 关联人 id、姓名 分隔符
	 */
	public String REL_NAME_SPLIT = ",";
	
	/**
	 * 起诉状标题  当事人 名称之间的分隔符 
	 */
	public String INDICTMENT_NAME_LITIGANT_SPLIT = "、";
	
	/**
	 * 起诉状关联 当事人的 名称分隔符
	 */
	public String INDICTMENT_REL_LITIGANT_SPLIT = " ";
	
	/**
	 * 同步服务器状态：全部
	 */
	public int SYNC_ALL = 0;
	
	/**
	 * 同步服务器状态：已同步
	 */
	public int SYNC_TRUE = 1;
	
	/**
	 * 同步服务器状态：未同步
	 */
	public int SYNC_FALSE = 2;
	
	/**
	 * 证件号码长度：身份证
	 */
	public int CERTIFICATE_LENGTH_ID_CARD = 18;
	
	/**
	 * 证件号码长度：其它（军官证、护照）
	 */
	public int CERTIFICATE_LENGTH_OTHER = 20;
	
    /** 人员类型：自然人 */
    public int DSR_TYPE_NORMAL = 1;
    
    /** 人员类型：法人 */
    public int DSR_TYPE_CORPORATION = 2;

    /** 人员类型：非法人组织 */
    public int DSR_TYPE_OTHER = 3;
    
    /** 网上立案查看界面：当事人类型-原告 */
	public int LITIGANT_TYPE_PLAINTIFF = 1;
	
	/** 网上立案查看界面：当事人类型-被告 */
	public int LITIGANT_TYPE_DEFENDANT = 2;
	
	/** 网上立案查看界面：证件类型-原告 */
	public int CERTIFICATES_TYPE_PLAINTIFF = 3;
	
	/** 网上立案查看界面：证件类型-被告 */
	public int CERTIFICATES_TYPE_DEFENDANT = 4;
	
	/** 网上立案查看界面：证件类型-代理人告 */
	public int CERTIFICATES_TYPE_AGENT = 5;
	
	/** 代理人关联原告：一个原告最多允许被关联上的人数 */
	public int AGENT_REL_PLAINTIFF_COUNT = 2;
	
	/**
	 * 申请人身份认证：手持身份证
	 */
	public int PRO_USER_SFRZ_SCSFZ = 1;
	
	/**
	 * 申请人身份认证：背面
	 */
	public int PRO_USER_SFRZ_ZM = 2;
	
	/**
	 * 申请人身份认证：背面
	 */
	public int PRO_USER_SFRZ_BM = 3;
	

	/** 取消的request code */
	public int REQ_CODE_CANCEL = 3;
	
	/**
     * 实名认证：手持证件
     */
    public int REQ_CODE_SFRZ_SCSFZ = 4;
    
    /**
     * 实名认证：身份证正面
     */
    public int REQ_CODE_SFRZ_ZM = 5;
    
    /**
     * 实名认证：身份证背面
     */
    public int REQ_CODE_SFRZ_BM = 6;
    
    /**
     * 选择法院
     */
    public int REQ_CODE_SELECT_COURT = 7;
    
    /**
     * 证件所属人类型：当事人
     */
    public int CERT_OWNER_TYPE_LITIGANT = 1;
    
    /**
     * 证件所属人类型：代理人
     */
    public int CERT_OWNER_TYPE_AGENT = 2;
    
    /**
     * 页面打开方式：新增
     */
    public int OPEN_TYPE_INSERT = 1;
    
    /**
     * 页面打开方式：编辑
     */
    public int OPEN_TYPE_EDIT = 2;
    
    /**
     * 页面打开方式：查看
     */
    public int OPEN_TYPE_DISPLAY = 3;
	
}
