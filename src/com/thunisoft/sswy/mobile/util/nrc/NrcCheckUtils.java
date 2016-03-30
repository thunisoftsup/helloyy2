package com.thunisoft.sswy.mobile.util.nrc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.datasource.NrcDsrDao;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.util.StringUtils;

public class NrcCheckUtils {

	/**
	 * 暂存校验：添加原告和被告即可
	 * 
	 * @return
	 */
	public static boolean checkTempSaveData(Context context, TLayy layy) {
		if (NrcEditData.getPlaintiffList().size() == 0) {
			Toast.makeText(context, "请添加原告", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			for (TLayyDsr plaintiff : NrcEditData.getPlaintiffList()) {
				boolean result = checkLitigantData(layy.getCSqrId(), plaintiff);
				if (!result) {// 存在未填写全的，提示
					StringBuffer toastMsg = new StringBuffer("");
					toastMsg.append("原告：").append(plaintiff.getCName());
					toastMsg.append("，信息不全，请添加");
					Toast.makeText(context, toastMsg.toString(), Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}

		if (NrcEditData.getDefendantList().size() == 0) {
			Toast.makeText(context, "请添加被告", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 提交校验：原告、被告、起诉状、证件、申请人身份认证
	 * 
	 * @return
	 */
	public static boolean checkSubmitData(Context context, TLayy layy, NrcDsrDao nrcDsrDao, int loginType) {
		if (checkTempSaveData(context, layy)) {
			if (NrcEditData.getAgentList().size() > 0) {
				for (TLayyDlr agent : NrcEditData.getAgentList()) {
					boolean result = checkAgentData(layy.getCSqrId(), agent);
					if (!result) {// 存在未填写全的，提示
						StringBuffer toastMsg = new StringBuffer("");
						toastMsg.append("代理人：").append(agent.getCName());
						toastMsg.append("，信息不全，请添加");
						Toast.makeText(context, toastMsg.toString(), Toast.LENGTH_SHORT).show();
						return false;
					}
				}
			}
			if (NrcEditData.getIndictmentList().size() == 0) {
				Toast.makeText(context, "请添加起诉状", Toast.LENGTH_SHORT).show();
				return false;
			}

			if (NrcEditData.getCertificateListMap().size() == 0) {
				Toast.makeText(context, "请添加证件", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
				for (TLayyDsr plaintiff : plaintiffList) {
					if (Constants.LITIGANT_TYPE_NATURAL == plaintiff.getNType()) {
						// 获取当前原告的证件
						List<TLayySscl> certList = NrcEditData.getCertificateListMap().get(plaintiff.getCId());
						if (null != certList && certList.size() > 0) {
							String certName = NrcUtils.getCertificateNameByCode(plaintiff.getNIdcardType());
							String certNameZm = certName + NrcUtils.ZJ_SUFFIX_ZM;
							String certNameBm = certName + NrcUtils.ZJ_SUFFIX_BM;
							Map<String, String> tempMap = new HashMap<String, String>();
							for (TLayySscl cert : certList) {
								tempMap.put(cert.getCName(), cert.getCName());
							}
							if (null == tempMap.get(certNameZm)) {
								Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的" + certName + "正面照", Toast.LENGTH_SHORT).show();
								return false;
							}
							if (null == tempMap.get(certNameBm)) {
								Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的" + certName + "背面照", Toast.LENGTH_SHORT).show();
								return false;
							}
						} else {
							Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的证件", Toast.LENGTH_SHORT).show();
							return false;
						}
					} else if (Constants.LITIGANT_TYPE_LEGAL == plaintiff.getNType()) { // 法人
						// 获取当前原告的证件
						List<TLayySscl> certList = NrcEditData.getCertificateListMap().get(plaintiff.getCId());
						if (null != certList && certList.size() > 0) {
							Map<String, String> tempMap = new HashMap<String, String>();
							for (TLayySscl cert : certList) {
								tempMap.put(cert.getCName(), cert.getCName());
							}
							if (null == tempMap.get(NrcUtils.ZJ_YYZZ_FYJ)) {
								Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的" + NrcUtils.ZJ_YYZZ_FYJ, Toast.LENGTH_SHORT).show();
								return false;
							}
							if (null == tempMap.get(NrcUtils.ZJ_FDDBR_SF_ZMS)) {
								Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的" + NrcUtils.ZJ_FDDBR_SF_ZMS, Toast.LENGTH_SHORT).show();
								return false;
							}
						} else {
							Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的证件", Toast.LENGTH_SHORT).show();
							return false;
						}
					} else if (Constants.LITIGANT_TYPE_NON_LEGAL == plaintiff.getNType()) {
						// 获取当前原告的证件
						List<TLayySscl> certList = NrcEditData.getCertificateListMap().get(plaintiff.getCId());
						if (null != certList && certList.size() > 0) {
							Map<String, String> tempMap = new HashMap<String, String>();
							for (TLayySscl cert : certList) {
								tempMap.put(cert.getCName(), cert.getCName());
							}
							if (null == tempMap.get(NrcUtils.ZJ_ZZJG_DMZ_FYJ)) {
								Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的" + NrcUtils.ZJ_ZZJG_DMZ_FYJ, Toast.LENGTH_SHORT).show();
								return false;
							}
							if (null == tempMap.get(NrcUtils.ZJ_FZR_ID_ZMS)) {
								Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的" + NrcUtils.ZJ_FZR_ID_ZMS, Toast.LENGTH_SHORT).show();
								return false;
							}
						} else {
							Toast.makeText(context, "请上传原告：" + plaintiff.getCName() + "的证件", Toast.LENGTH_SHORT).show();
							return false;
						}
					}
				}
				List<TLayyDlr> agentList = NrcEditData.getAgentList();
				if (null != agentList && agentList.size() > 0) {
					for (TLayyDlr agent : agentList) {
						// 获取当前代理人的证件
						List<TLayySscl> certList = NrcEditData.getCertificateListMap().get(agent.getCId());
						if (null != certList && certList.size() > 0) {
							Map<String, TLayySscl> tempMap = new HashMap<String, TLayySscl>();
							for (TLayySscl cert : certList) {
								tempMap.put(cert.getCName(), cert);
							}
							
							//律师、承担法律援助的律师
							if (Constants.AGENT_TYPE_ASSIST_LAWYER == agent.getNDlrType()
									|| Constants.AGENT_TYPE_LAWYER == agent.getNDlrType()) {
								TLayySscl certTemp = tempMap.get(NrcUtils.AGENT_CERT_TIP);
								if (null == certTemp) {
									Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + NrcUtils.AGENT_CERT_TIP + "照片", Toast.LENGTH_SHORT).show();
									return false;
								} else {
									List<TLayySsclFj> certFjListTemp = NrcEditData.getCertificateFjListMap().get(certTemp.getCId());
									if (null == certFjListTemp || certFjListTemp.size() == 0) {
										Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + NrcUtils.AGENT_CERT_TIP + "照片", Toast.LENGTH_SHORT).show();
										return false;
									}
								}
								if (!checkAgentLicense(context, tempMap, agent, nrcDsrDao)) {
									return false;
								}
							} else {
								TLayySscl identfyCert = tempMap.get(NrcUtils.AGENT_IDENTFY_TIP);
								if (null == identfyCert) {
									Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + NrcUtils.AGENT_IDENTFY_TIP, Toast.LENGTH_SHORT).show();
									return false;
								} else {
									List<TLayySsclFj> identifyCertFj = NrcEditData.getCertificateFjListMap().get(identfyCert.getCId());
									if (null == identifyCertFj || identifyCertFj.size() == 0) {
										Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + NrcUtils.AGENT_IDENTFY_TIP, Toast.LENGTH_SHORT).show();
										return false;
									}
								}
								
								TLayySscl tellerCert = tempMap.get(NrcUtils.AGENT_TELLER_CERT_TIP); 
								if (null == tellerCert) {
									Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + NrcUtils.AGENT_TELLER_CERT_TIP, Toast.LENGTH_SHORT).show();
									return false;
								} else {
									List<TLayySsclFj> tellerCertFj = NrcEditData.getCertificateFjListMap().get(tellerCert.getCId());
									if (null == tellerCertFj || tellerCertFj.size() == 0) {
										Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + NrcUtils.AGENT_TELLER_CERT_TIP, Toast.LENGTH_SHORT).show();
										return false;
									}
								}
								
								if (Constants.AGENT_TYPE_JHR != agent.getNDlrType()) {
									if (!checkAgentLicense(context, tempMap, agent, nrcDsrDao)) {
										return false;
									}
								}
							}
							
						} else {
							Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + NrcUtils.AGENT_CERT_TIP + "照片", Toast.LENGTH_SHORT).show();
							return false;
						}
					}
				}
			}
			if (LoginCache.LOGIN_TYPE_LS_VERIFID != loginType) { // 律师认证的用户不需要身份认证
				Map<Integer, TProUserSfrzCl> sfrzclMap = NrcEditData.getProUserSfrzclMap();
				// 申请人身份认证
				if (sfrzclMap.size() == 0) {
					Toast.makeText(context, "请上传申请人身份认证照片", Toast.LENGTH_SHORT).show();
					return false;
				} else {
					if (null == sfrzclMap.get(NrcConstants.PRO_USER_SFRZ_SCSFZ)) {
						Toast.makeText(context, "请上传申请人手持证件照", Toast.LENGTH_SHORT).show();
						return false;
					}
					if (null == sfrzclMap.get(NrcConstants.PRO_USER_SFRZ_ZM)) {
						Toast.makeText(context, "请上传申请人身份证正面照", Toast.LENGTH_SHORT).show();
						return false;
					}
					if (null == sfrzclMap.get(NrcConstants.PRO_USER_SFRZ_BM)) {
						Toast.makeText(context, "请上传申请人身份证背面照", Toast.LENGTH_SHORT).show();
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private static boolean checkAgentLicense(Context context, Map<String, TLayySscl> ssclMap, TLayyDlr agent, NrcDsrDao nrcDsrDao) {
		if (StringUtils.isNotBlank(agent.getCBdlrId())) {
			String[] bdlrIdArray = agent.getCBdlrId().split(NrcConstants.REL_NAME_SPLIT);
			List<String> bdlrIdList = Arrays.asList(bdlrIdArray);
			List<TLayyDsr> dsrList = nrcDsrDao.getDsrListByIdList(Constants.LITIGANT_SSDW_PLAINTIFF, bdlrIdList);
			for (int i = 0; i < dsrList.size(); i++) {
				TLayyDsr dsr = dsrList.get(i);
				String licenseName = dsr.getCName() + NrcUtils.LICENSE_BOOK;
				TLayySscl licenseTemp = ssclMap.get(licenseName);
				if (null == licenseTemp) {
					Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + licenseName, Toast.LENGTH_SHORT).show();
					return false;
				} else {
					List<TLayySsclFj> licenseFjList = NrcEditData.getCertificateFjListMap().get(licenseTemp.getCId());
					if (null == licenseFjList || licenseFjList.size() == 0) {
						Toast.makeText(context, "请上传代理人：" + agent.getCName() + "的" + licenseName, Toast.LENGTH_SHORT).show();
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 检查当事人信息项填写是否完全
	 * 
	 * @param dsr
	 * @return
	 */
	public static boolean checkLitigantData(String layySqrId, TLayyDsr dsr) {
		// 因为原告可能是创建网上立案带过来的，信息项不全，所以在此增加检查
		if (StringUtils.isNotBlank(layySqrId) && layySqrId.equals(dsr.getCId())) {

			switch (dsr.getNType()) {
			case Constants.LITIGANT_TYPE_NATURAL:
				if (!checkNaturalData(dsr)) {
					return false;
				}
				break;
			case Constants.LITIGANT_TYPE_LEGAL: //检查法人的必填项
                if (!checkLegalData(dsr)) {
                	return false;
                }
				break;
			case Constants.LITIGANT_TYPE_NON_LEGAL: //检查非法人的必填项，与法人共用
				if (!checkLegalData(dsr)) {
                	return false;
                }
				break;

			default:
				break;
			}
		}
		return true;
	}

	/**
	 * 检查 自然人的必填项
	 * @param dsr
	 * @return
	 */
	private static boolean checkNaturalData(TLayyDsr dsr) {
		if (null == dsr.getNXb()) {
			return false;
		}
		if (StringUtils.isBlank(dsr.getCAddress())) {
			return false;
		}
		if (null == dsr.getNType()) {
			return false;
		}
		if (StringUtils.isBlank(dsr.getCName())) {
			return false;
		}
		if (null == dsr.getNIdcardType()) {
			return false;
		} else { // 身份证，出生日期为必填
			if (StringUtils.isBlank(dsr.getCIdcard())) { // 证件号
				return false;
			}
			if (NrcConstants.CERTIFICATE_TYPE_IDCARD == dsr.getNIdcardType()) {
				if (StringUtils.isBlank(dsr.getDCsrq())) {
					return false;
				}
			}
		}
		if (StringUtils.isBlank(dsr.getCSjhm())) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查 法人、非法人的必填项
	 * @param dsr
	 * @return
	 */
	private static boolean checkLegalData(TLayyDsr dsr) {
		if (StringUtils.isBlank(dsr.getCName())) {
			return false;
		}
//		if (StringUtils.isBlank(dsr.getCLxdh())) {
//			return false;
//		}
		if (StringUtils.isBlank(dsr.getCDwdz())) {
			return false;
		}
		if (StringUtils.isBlank(dsr.getCFddbr())) {
			return false;
		}
		if (StringUtils.isBlank(dsr.getCFddbrSjhm())) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查代理人信息项填写是否完全
	 * 
	 * @param dlr
	 * @return
	 */
	public static boolean checkAgentData(String layySqrId, TLayyDlr dlr) {
		// 因为代理人可能是创建网上立案带过来的，信息项不全，所以在此增加检查
		if (StringUtils.isNotBlank(layySqrId) && layySqrId.equals(dlr.getCId())) {
			if (StringUtils.isBlank(dlr.getCName())) {
				return false;
			}
			if (null == dlr.getNDlrType()) {
				return false;
			}
			if (null == dlr.getNIdcardType()) {
				return false;
			}
			if (StringUtils.isBlank(dlr.getCIdcard())) {
				return false;
			}
			if (StringUtils.isBlank(dlr.getCSjhm())) {
				return false;
			}
			if (Constants.AGENT_TYPE_LAWYER == dlr.getNDlrType() // 只有律师和
																	// 承担法律援助的律师才增加
																	// 执业证号和所在单位的检验
					|| Constants.AGENT_TYPE_ASSIST_LAWYER == dlr.getNDlrType()) {
				if (StringUtils.isBlank(dlr.getCZyzh())) {
					return false;
				}
				if (StringUtils.isBlank(dlr.getCSzdw())) {
					return false;
				}
			}
			if (StringUtils.isBlank(dlr.getCBdlrId())) {
				return false;
			}
		}
		return true;
	}

	public static final boolean isMobileNO(String str) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
