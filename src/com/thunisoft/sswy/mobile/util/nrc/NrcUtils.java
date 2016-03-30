package com.thunisoft.sswy.mobile.util.nrc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 网上立案 工具类
 * 
 * @author gewx
 *
 */
@SuppressLint("UseSparseArrays")
public class NrcUtils {

	/**
	 * code Key
	 */
	public static final String KEY_CODE = "code";

	/**
	 * 名称 Key
	 */
	public static final String KEY_NAME = "name";

	/**
	 * 获取根据证件类型名称，证件类型code
	 * 
	 * @return
	 */
	public static int getLitigantStateCodeByName(String name) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("原告", NrcConstants.LITIGANT_TYPE_PLAINTIFF);
		map.put("被告", NrcConstants.LITIGANT_TYPE_DEFENDANT);
		return map.get(name);
	}

	/**
	 * 案件类型_民事
	 */
	public static final int CASE_TYPE_MS = 2;
	
	/**
	 * 获取案件类型List
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getCaseTypeList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(KEY_CODE, String.valueOf(CASE_TYPE_MS));
		map1.put(KEY_NAME, "民事");
		list.add(map1);
		return list;
	}

	/**
	 * 根据案件类型code，获取案件类型名称
	 * 
	 * @return
	 */
	public static String getCaseTypeNameByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(CASE_TYPE_MS, "民事");
		return map.get(code);
	}

	/**
	 * 审判程序_起诉
	 */
	public static final int JUDGE_PROGRAM_QS = 1;
	
	/**
	 * 获取审判程序List
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getJudgeProgramList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(KEY_CODE, String.valueOf(JUDGE_PROGRAM_QS));
		map1.put(KEY_NAME, "起诉");
		list.add(map1);
		return list;
	}

	/**
	 * 根据审判程序code，获取审判程序名称
	 * 
	 * @return
	 */
	public static String getJudgeProgramNameByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(JUDGE_PROGRAM_QS, "起诉");
		return map.get(code);
	}

	/**
	 * 申请人身份_为本人申请
	 */
	public static final int APPLICANT_FOR_ME = 1;
	
	/**
	 * 申请人身份_为他人申请
	 */
	public static final int APPLICANT_FOR_OTHER = 2;
	
	/**
	 * 获取申请人类型List
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getApplicantTypeList(int loginType) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = null;
		if (LoginCache.LOGIN_TYPE_LS_VERIFID != loginType) {
			map1 = new HashMap<String, String>();
			map1.put(KEY_CODE, String.valueOf(APPLICANT_FOR_ME));
			map1.put(KEY_NAME, "为本人申请");
		}

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(KEY_CODE, String.valueOf(APPLICANT_FOR_OTHER));
		map2.put(KEY_NAME, "为他人申请");

		if (null != map1) {
			list.add(map1);
		}
		list.add(map2);
		return list;
	}

	/**
	 * 根据申请人身份code，获取申请人身份名称
	 * 
	 * @return
	 */
	public static String getApplicantNameByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(APPLICANT_FOR_ME, "为本人申请");
		map.put(APPLICANT_FOR_OTHER, "为他人申请");
		return map.get(code);
	}

	/**
	 * 获取代理人类型List
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getAgentTypeList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(KEY_CODE, "1");
		map1.put(KEY_NAME, "律师");

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(KEY_CODE, "2");
		map2.put(KEY_NAME, "承担法律援助的律师");

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put(KEY_CODE, "3");
		map3.put(KEY_NAME, "监护人");

		Map<String, String> map4 = new HashMap<String, String>();
		map4.put(KEY_CODE, "4");
		map4.put(KEY_NAME, "亲友");

		Map<String, String> map5 = new HashMap<String, String>();
		map5.put(KEY_CODE, "5");
		map5.put(KEY_NAME, "人民团体推荐的人");

		Map<String, String> map6 = new HashMap<String, String>();
		map6.put(KEY_CODE, "6");
		map6.put(KEY_NAME, "所在单位推荐的人");

		Map<String, String> map7 = new HashMap<String, String>();
		map7.put(KEY_CODE, "7");
		map7.put(KEY_NAME, "法院许可的其他公民");

		Map<String, String> map8 = new HashMap<String, String>();
		map8.put(KEY_CODE, "8");
		map8.put(KEY_NAME, "法律工作者");

		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		list.add(map6);
		list.add(map7);
		list.add(map8);
		return list;
	}

	/**
	 * 根据代理人类型code，获取 代理人类型名称
	 * 
	 * @return
	 */
	public static String getAgentNameByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "律师");
		map.put(2, "承担法律援助的律师");
		map.put(3, "监护人");
		map.put(4, "亲友");
		map.put(5, "人民团体推荐的人");
		map.put(6, "所在单位推荐的人");
		map.put(7, "法院许可的其他公民");
		map.put(8, "法律工作者");
		return map.get(code);
	}

	/**
	 * 获取性别类型List
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getGenderTypeList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(KEY_CODE, "1");
		map1.put(KEY_NAME, "男");

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(KEY_CODE, "2");
		map2.put(KEY_NAME, "女");

		list.add(map1);
		list.add(map2);
		return list;
	}

	/**
	 * 获取根据性别类型code，性别类型名称
	 * 
	 * @return
	 */
	public static String getGenderNameByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "男");
		map.put(2, "女");
		return map.get(code);
	}

	/**
	 * 获取证件类型List
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getCertificateTypeList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(KEY_CODE, "1");
		map1.put(KEY_NAME, "身份证");

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(KEY_CODE, "2");
		map2.put(KEY_NAME, "军官证");

		Map<String, String> map7 = new HashMap<String, String>();
		map7.put(KEY_CODE, "7");
		map7.put(KEY_NAME, "护    照");

		list.add(map1);
		list.add(map2);
		list.add(map7);
		return list;
	}

	/**
	 * 获取根据证件类型code，证件类型名称
	 * 
	 * @return
	 */
	public static String getCertificateNameByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "身份证");
		map.put(2, "军官证");
		map.put(7, "护照");
		return map.get(code);
	}

	/**
	 * 法人 单位：营业执照复印件
	 */
	public static final String ZJ_YYZZ_FYJ = "营业执照复印件";

	/**
	 * 法人 代表：身份证明书
	 */
	public static final String ZJ_FDDBR_SF_ZMS = "法定代表人身份证明书";

	/**
	 * 非法人：组织机构代码证复印件
	 */
	public static final String ZJ_ZZJG_DMZ_FYJ = "组织机构代码证复印件";

	/**
	 * 非法人 主要负责人：身份证明书
	 */
	public static final String ZJ_FZR_ID_ZMS = "主要负责人身份证明书";

	/**
	 * 正面复印件后缀
	 */
	public static final String ZJ_SUFFIX_ZM = "复印件";

	/**
	 * 背面复印件后缀
	 */
	public static final String ZJ_SUFFIX_BM = "背面复印件";
	
	 /**
     * 代理人：律师、承担法律责任的律师
     */
    public static final String AGENT_CERT_TIP = "代理人身份证明（执业证件）";
    
    /**
     * 代理人：非律师、承担法律责任的律师  <br>
     * 监护人,亲友,人民团体推荐的人,所在单位推荐的人,法院许可的其他公民,法律工作者
     */
    public static final String AGENT_TELLER_CERT_TIP = "代为告诉人身份证明（原告身份证复印件）";
    
    /**
     * 代理人：非律师、承担法律责任的律师  <br>
     * 监护人,亲友,人民团体推荐的人,所在单位推荐的人,法院许可的其他公民,法律工作者
     */
    public static final String AGENT_IDENTFY_TIP = "代理人身份证明";
    
    /**
	 * 授权委托书
	 */
	public static final String LICENSE_BOOK = "授权委托书";

	public static Map<String, String> getCertNameMap(List<TLayyDsr> dsrList) {
		Map<String, String> certNameMap = new HashMap<String, String>();
		certNameMap.put(ZJ_YYZZ_FYJ, ZJ_YYZZ_FYJ);
		certNameMap.put(ZJ_FDDBR_SF_ZMS, ZJ_FDDBR_SF_ZMS);
		certNameMap.put(ZJ_ZZJG_DMZ_FYJ, ZJ_ZZJG_DMZ_FYJ);
		certNameMap.put(ZJ_FZR_ID_ZMS, ZJ_FZR_ID_ZMS);
		certNameMap.put(AGENT_CERT_TIP, AGENT_CERT_TIP);
		certNameMap.put(AGENT_TELLER_CERT_TIP, AGENT_TELLER_CERT_TIP);
		certNameMap.put(AGENT_IDENTFY_TIP, AGENT_IDENTFY_TIP);
		certNameMap.put("身份证复印件", "身份证复印件");
		certNameMap.put("身份证背面复印件", "身份证背面复印件");
		certNameMap.put("军官证复印件", "军官证复印件");
		certNameMap.put("军官证背面复印件", "军官证背面复印件");
		certNameMap.put("护照复印件", "护照复印件");
		certNameMap.put("护照背面复印件", "护照背面复印件");
		if (null != dsrList && dsrList.size() > 0) {
			for (TLayyDsr layyDsr : dsrList) {
				certNameMap.put(layyDsr.getCName() + LICENSE_BOOK, layyDsr.getCName() + LICENSE_BOOK);
			}
		}
		return certNameMap;
	}
	
	/**
	 * 获取根据证件类型名称，证件类型code
	 * 
	 * @return
	 */
	public static int getCertificateCodeByName(String name) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("身份证", 1);
		map.put("军官证", 2);
		map.put("护照", 7);
		return map.get(name);
	}

	/**
	 * 获取当事人类型List
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getLitigantTypeList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(KEY_CODE, "1");
		map1.put(KEY_NAME, "自然人");

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(KEY_CODE, "2");
		map2.put(KEY_NAME, "法人");

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put(KEY_CODE, "3");
		map3.put(KEY_NAME, "非法人组织");

		list.add(map1);
		list.add(map2);
		list.add(map3);
		return list;
	}

	/**
	 * @return
	 */
	public static String getLitigantTypeNameByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "自然人");
		map.put(2, "法人");
		map.put(3, "非法人组织");
		return map.get(code);
	}

	/**
	 * 网上提交状态：全部
	 */
	public static final int NRC_STATUS_ALL = -1;

	/**
	 * 网上提交状态：待提交
	 */
	public static final int NRC_STATUS_DTJ = 100;

	/**
	 * 网上提交状态：申请失效
	 */
	public static final int NRC_STATUS_SQSB = 101;

	/**
	 * 网上提交状态：待审查
	 */
	public static final int NRC_STATUS_DSC = 1;

	/**
	 * 网上提交状态：审查通过
	 */
	public static final int NRC_STATUS_SCTG = 2;

	/**
	 * 网上提交状态：审查不通过
	 */
	public static final int NRC_STATUS_SCBTG = 3;

	/**
	 * 网上提交状态：已立案
	 */
	public static final int NRC_STATUS_YLA = 4;
	
	/**
	 * 允许再次提交
	 */
	public static final int NRC_RE_SUBMIT = 1;

	/**
	 * 案件申请状态对照表
	 */
	public static String getNrcStatusNameByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(NRC_STATUS_ALL, "全部");
		map.put(NRC_STATUS_DTJ, "待提交");
		map.put(NRC_STATUS_SQSB, "申请失效");
		map.put(NRC_STATUS_DSC, "待审查");
		map.put(NRC_STATUS_SCTG, "审核通过");
		map.put(NRC_STATUS_SCBTG, "审核不通过");
		map.put(NRC_STATUS_YLA, "已立案");
		return map.get(code);
	}

	/**
	 * 获取 网上立案 状态名称和颜色值
	 * 
	 * @return
	 */
	public static HashMap<Integer, HashMap<String, String>> getNrcStatusMap() {
		HashMap<Integer, HashMap<String, String>> map = new HashMap<Integer, HashMap<String, String>>();

		HashMap<String, String> dtjMap = new HashMap<String, String>();
		dtjMap.put(KEY_NAME, "待提交");
		dtjMap.put("bgColor", "#ff9800");
		map.put(NRC_STATUS_DTJ, dtjMap);

		HashMap<String, String> sqsbMap = new HashMap<String, String>();
		sqsbMap.put(KEY_NAME, "申请失效");
		sqsbMap.put("bgColor", "#db3838");
		map.put(NRC_STATUS_SQSB, sqsbMap);

		HashMap<String, String> dscMap = new HashMap<String, String>();
		dscMap.put(KEY_NAME, "待审查");
		dscMap.put("bgColor", "#66bed1");
		map.put(NRC_STATUS_DSC, dscMap);

		HashMap<String, String> sctgMap = new HashMap<String, String>();
		sctgMap.put(KEY_NAME, "审核通过");
		sctgMap.put("bgColor", "#8CC34B");
		map.put(NRC_STATUS_SCTG, sctgMap);

		HashMap<String, String> scbtgMap = new HashMap<String, String>();
		scbtgMap.put(KEY_NAME, "审核不通过");
		scbtgMap.put("bgColor", "#db3838");
		map.put(NRC_STATUS_SCBTG, scbtgMap);

		HashMap<String, String> ylaMap = new HashMap<String, String>();
		ylaMap.put(KEY_NAME, "已立案");
		ylaMap.put("bgColor", "#8CC34B");
		map.put(NRC_STATUS_YLA, ylaMap);

		return map;
	}

	/**
	 * 获取 网上立案 状态
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getNrcStatusList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> map0 = new HashMap<String, String>();
		map0.put(KEY_CODE, String.valueOf(NRC_STATUS_ALL));
		map0.put(KEY_NAME, "全部");

		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put(KEY_CODE, String.valueOf(NRC_STATUS_DTJ));
		map1.put(KEY_NAME, "待提交");

		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put(KEY_CODE, String.valueOf(NRC_STATUS_SQSB));
		map2.put(KEY_NAME, "申请失效");

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put(KEY_CODE, String.valueOf(NRC_STATUS_DSC));
		map3.put(KEY_NAME, "待审查");

		Map<String, String> map4 = new HashMap<String, String>();
		map4.put(KEY_CODE, String.valueOf(NRC_STATUS_SCTG));
		map4.put(KEY_NAME, "审核通过");

		Map<String, String> map5 = new HashMap<String, String>();
		map5.put(KEY_CODE, String.valueOf(NRC_STATUS_SCBTG));
		map5.put(KEY_NAME, "审核不通过");

		Map<String, String> map6 = new HashMap<String, String>();
		map6.put(KEY_CODE, String.valueOf(NRC_STATUS_YLA));
		map6.put(KEY_NAME, "已立案");

		list.add(map0);
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		list.add(map6);
		return list;
	}

	/**
	 * 获取审核结果
	 * 
	 * @return
	 */
	public static String getCheckResultByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "通过");
		map.put(2, "不通过");
		map.put(3, "按撤诉处理");
		return map.get(code);
	}

	/**
	 * 获取 审核状态 背景色
	 * 
	 * @return
	 */
	public static Map<Integer, String> getCheckStatusMap() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "#66bed1");
		map.put(2, "#8CC34B");
		map.put(3, "#db3838");
		return map;
	}
	
	/**
	 * 审核结果未通过
	 */
	public static final int CHECK_RESULT_NO = 2;
	
	/**
	 * 获取申请类型
	 * 
	 * @return
	 */
	public static String getApplicationTypeByCode(int code) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "起诉");
		map.put(2, "上诉");
		return map.get(code);
	}

	/**
	 * 获取认证方式
	 * 
	 * @return
	 */
	public static List<Map<String, String>> getRzfsList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put(KEY_CODE, "1");
		map1.put(KEY_NAME, "实名认证");
		list.add(map1);

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put(KEY_CODE, "1");
		map2.put(KEY_NAME, "律师认证");
		list.add(map2);
		return list;
	}

	public static String getFormatDate(String date) {
		String newDate = "";
		if (StringUtils.isNotBlank(date)) {
			newDate = date.substring(0, date.indexOf(" "));
		}
		return newDate;
	}

	public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	@SuppressLint("SimpleDateFormat")
	public static final String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static final Date string2Date(String str, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			date = new Date();
		}
		return date;
	}

	public static final long getTimeByDateStr(String str, String format) {
		long time = 0;
		if (StringUtils.isNotBlank(str)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = null;
			try {
				date = sdf.parse(str);
				time = date.getTime();
			} catch (ParseException e) {
				time = 0;
			}
		}
		return time;
	}
	
	public static final String getBirthDay() {
		Calendar cal = Calendar.getInstance();
		int currYear = cal.get(Calendar.YEAR);
		cal.set(Calendar.YEAR, currYear - 30);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String birthdayStr = NrcUtils.formatDate(cal.getTime(), NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);
		return birthdayStr;
	}

	/**
	 * 获取证据编号
	 * 
	 * @param evidenceList
	 * @return
	 */
	public static final String getZjbh(List<TZj> evidenceList) {
		int zjbhTemp = 1;
		if (null != evidenceList) {
			zjbhTemp = evidenceList.size() + 1;
		}
		String zjbh = String.valueOf(zjbhTemp);
		if (zjbh.length() < 4) { // 证据编号默认最小长度为4 例如：0001
			for (int i = 0; i < 4 - zjbh.length(); i++) {
				zjbh = "0" + zjbh;
			}
		}
		return zjbh;
	}

	public static final String getAddress(String addressInfo, String addressId) {
		String[] addressIdArray = addressId.split(NrcConstants.ADDRESS_SPLIT);
		String[] addressArray = addressInfo.split(NrcConstants.ADDRESS_SPLIT);
		int addressIdLength = addressIdArray.length;
		// 因为存在 北京市-北京市第二中级人民法院-北京市东城区这样的地址
		// 但是在显示的时候又去掉了 “北京市第二中级人民法院”，保存数据库的时候也不保存，但是在保存addressId的时候还要加上
		// “北京市第二中级人民法院”的id
		if (isMunicipality(addressIdArray[0])) {
			addressIdLength = addressIdLength - 1;
		}
		StringBuffer addressSb = new StringBuffer("");
		for (int i = 0; i < addressIdLength; i++) {
			addressSb.append(addressArray[0]);
			
			if (i < addressIdLength - 1) {
				addressSb.append("-");
			}
		}
		return addressSb.toString();
	}

	public static final String getAddressDetail(String addressInfo, String addressId) {
		String[] addressIdArray = addressId.split(NrcConstants.ADDRESS_SPLIT);
		String[] addressArray = addressInfo.split(NrcConstants.ADDRESS_SPLIT);
		int addressIdLength = addressIdArray.length;
		// 因为存在 北京市-北京市第二中级人民法院-北京市东城区这样的地址
		// 但是在显示的时候又去掉了 “北京市第二中级人民法院”，保存数据库的时候也不保存，但是在保存addressId的时候还要加上
		// “北京市第二中级人民法院”的id
		if (isMunicipality(addressIdArray[0])) {
			addressIdLength = addressIdLength - 1;
		}
		StringBuffer addressSb = new StringBuffer("");
		for (int i = 0; i < addressIdLength; i++) {
			addressSb.append(addressArray[i<addressArray.length ? i:(addressArray.length-1)]);
			if (i < addressIdLength - 1) {
				
				addressSb.append("-");
			}
		}
		String addressDetail = addressInfo.replaceAll(addressSb.toString(), "");
		if (addressDetail.startsWith(NrcConstants.ADDRESS_SPLIT)) {
			addressDetail = addressDetail.substring(1);
		}
		return addressDetail;
	}
	
	private static final String municipalityIdds[] = { "1", "51", "1100", "2950" };
	public static boolean isMunicipality(String addressId) {
		for (int i=0; i<municipalityIdds.length; i++) {
			if (addressId.equals(municipalityIdds[i])) {
				return true;
			}
		}
	    return false;
	}
	
	public static Integer stringToInt(String str) {
		if (StringUtils.isNotBlank(str)) {
			try {
				int i = Integer.parseInt(str);
				return i;
			} catch (Exception e) {
				
			}
		}
		return null;
	}
	
}
