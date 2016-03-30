package com.thunisoft.sswy.mobile;

public class Constants {
    public static final int REQUEST_CODE_LOGIN = 1; // 登录
    public static final int REQUEST_CODE_CONFIRM_ZX = 2; // 注销
    public static final int REQUEST_CODE_LSRZ = 3; // 律师认证
    public static final int REQUEST_CODE_CONFIRM_LSRZ_FGXX = 4; // 律师认证，覆盖信息确认
    public static final int REQUEST_CODE_SMRZ = 5; //实名认证
    public static final int REQUEST_CODE_CONFIRM_SMRZ_CXM_FGXX = 6; //查询码实名认证，覆盖信息
    public static final int REQUEST_CODE_CONFIRM_SMRZ_YZM_FGXX = 7; //短信验证码实名认证，覆盖信息
    public static final int REQUEST_CODE_ZC = 8; // 注册
    public static final int REQUEST_CODE_XZZJ= 9; // 选择证件
    public static final int REQUEST_CODE_FINISH_WIDTH＿NEXT= 10; // 下一页关闭后，关闭本页
    public static final int REQUEST_CODE_SCAN_CODE= 11; //扫描二维码
    public static final int REQUEST_CODE_SHOW_SD_WRIT_LIST= 12; //显示送达文书列表
    public static final int REQUEST_CODE_SHOW_SD_WRIT_LIST_AFTER_DOWNLOAD= 13; //下载完成，显示送达文书列表
    public static final int REQUEST_CODE_CONFIRM_UPDATE= 14;//
    public static final int REQUEST_CODE_YZM= 15;//图片验证码
    public static final int REQUEST_CODE_QSWS = 16;//签收文书
    public static final int REQUEST_CODE_LOGINSUCESS = 17;//签收文书
    public static final int REQUEST_CODE_ADDCASESUCESS = 18;//添加案件成功
    public static final int REQUEST_CODE_DOWNLOAD = 19;//添加案件成功
    public static final int REQUEST_CODE_DELETE = 20;//删除立案
    
    public static final int REQUEST_CODE_DELETE_LITIGANT = 21;//删除当事人
    public static final int REQUEST_CODE_DELETE_AGENT = 22;//删除代理人
    public static final int REQUEST_CODE_DELETE_WITNESS = 23;//删除证人
    public static final int REQUEST_CODE_DELETE_INDICTMENT = 24;//删除起诉状
    public static final int REQUEST_CODE_DELETE_EVIDENCE = 25;//删除证据
    
    public static final int REQUEST_CODE_PAY = 26;//支付宝页面
    

    public static final int ERROR_SHOW_CONFIRM = 1;
    public static final int ERROR_SHOW_ALERT = 2;
    public static final int ERROR_SHOW_YZM = 3;
    public static final int ERROR_REQUEST_SID= 4;
    
    public static final int DZSD_LIST_PAGE_SIZE = 20;
    
    public static final int JSFS_ZY = 1;
    public static final int JSFS_ZJ = 2;
    
    /**
     * 应用ID 案件查询
     */
    public static final String APP_ID_AJCX = "3";

    /**
     * 应用ID 网上立案
     */
    public static final String APP_ID_WSLA = "4";
    
    /**
     * 应用ID 电子送达
     */
    public static final String APP_ID_DZSD = "6";
    /**
     * 应用ID 网上申诉信访
     */
    public static final String APP_ID_SSXF = "11";

    /**
     * 应用ID 执行线索举报
     */
    public static final String APP_ID_ZXXSJB = "12";

    /**
     * 应用ID 网上阅卷
     */
    public static final String APP_ID_WSYJ = "13";
    /**
     * 应用ID 联系法官
     */
    public static final String APP_ID_LXFG = "18";
    
    /**
     * 应用ID 网上交费
     */
    public static final String APP_ID_WSJF = "23";
    
    /**
     * 状态：待回复
     */
    public static final int STATUS_LXFG_DHF = 2;

    /**
     * 状态：已回复
     */
    public static final int STATUS_LXFG_YHF = 3;
    
    /**
     * 电子送达查询范围 未签收
     */
    public static final String SCOPE_DZSD_WQS = "3";
    
    /**
     * 电子送达查询范围 已签收
     */
    public static final String SCOPE_DZSD_YQS = "4";

    public static final int AJ_LIST_SCOPE_CXM_ZB = 1;//案件查询取值范围，在办
    public static final int AJ_LIST_SCOPE_YZM_LS = 2;//案件查询取值范围，历史
    
    /**
     * 当事人诉讼地位 原告
     */
    public static final String LITIGANT_SSDW_PLAINTIFF = "原告";
    /**
     * 当事人诉讼地位 被告
     */
    public static final String LITIGANT_SSDW_DEFENDANT = "被告";
    
    /**
     * 当事人类型 自然人
     */
    public static final int LITIGANT_TYPE_NATURAL = 1;
    
    /**
     * 当事人类型 法人
     */
    public static final int LITIGANT_TYPE_LEGAL = 2;
    
    /**
     * 当事人类型 非法人组织
     */
    public static final int LITIGANT_TYPE_NON_LEGAL = 3;
    
    /**
     * 代理人类型 律师代理人
     */
    public static final int AGENT_TYPE_LAWYER = 1;
    /**
     * 代理人类型 承担法律援助的律师
     */
    public static final int AGENT_TYPE_ASSIST_LAWYER = 2;
    
    /**
	 * 代理人类型：监护人
	 */
	public static final int AGENT_TYPE_JHR = 3;
    
    /**
     * startActivityForResult  resultCode
     */
    public static final int RESULT_OK = 1;
    
    /**
     * 选择法院
     */
    public static final int REQUEST_CODE_SELECT_COURT = 20;
    
    /**
     * 选择代理人类型
     */
    public static final int REQUEST_CODE_SELECT_AGENT_TYPE = 21;
    
    /**
     * 选择性别类型
     */
    public static final int REQUEST_CODE_SELECT_GENDER_TYPE = 22;
    
//    /**
//     * 选择证件类型
//     */
//    public static final int REQUEST_CODE_SELECT_CERTIFICATE_TYPE = 23;
    
    /**
     * 选择日期
     */
    public static final int REQUEST_CODE_SELECT_DATE = 24;
    
    /**
     * 选择地址
     */
    public static final int REQUEST_CODE_SELECT_ADDRESS = 25;
    
    /**
     * 选择当事人类型
     */
    public static final int REQUEST_CODE_SELECT_LITIGANT_TYPE = 26;
    
    /**
     * 性别 男
     */
    public static final int GENDER_MAN = 1;
    
    /**
     * 性别 女
     */
    public static final int GENDER_WOMAN = 2;
    
    /**
     * 证件类型：身份证
     */
    public static final int CERTIFICATE_TYPE_ID_CARD = 1;
    
}
