package com.thunisoft.sswy.mobile.util.nrc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.pojo.TLayyDlr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.pojo.TLayyZr;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;

/**
 * 用于暂存网上立案所有数据
 * 
 * @author gewx
 *
 */
public class NrcEditData {

	/** 网上立案 基本信息 */
	private static TLayy layy;

	/** 当事人_原告 List */
	private static List<TLayyDsr> plaintiffList;

	/** 当事人_被告 List */
	private static List<TLayyDsr> defendantList;

	/** 代理人 List */
	private static List<TLayyDlr> agentList;

	/** 证人 List */
	private static List<TLayyZr> witnessList;

	/** 起诉状 List */
	private static List<TLayySscl> indictmentList;

	/** 起诉状附件 Map[起诉状id， List[附件]] */
	private static Map<String, ArrayList<TLayySsclFj>> indictmentFjListMap;

	/** 起诉状关联当事人 Map[起诉状id，List[当事人诉讼材料]] */
	private static Map<String, ArrayList<TLayyDsrSscl>> dsrIndictmentListMap;

	/** 证件 Map[人员Id，List[诉讼材料]] */
	private static Map<String, ArrayList<TLayySscl>> certificateListMap;

	/** 证件_附件 Map[证件id，List[附件]] */
	private static Map<String, ArrayList<TLayySsclFj>> certificateFjListMap;

	/** 证件_附件_所属人 Map[证件id，List[证件所属人]] */
	private static Map<String, ArrayList<TLayyDsrSscl>> dsrCertificateListMap;

	/** 证据 List */
	private static List<TZj> evidenceList;

	/** 证据 材料 Map[证据id， List[证据材料]] */
	private static Map<String, ArrayList<TZjcl>> evidenceMaterial;

	/** 申请人身份认证 Map[类型，身份认证材料] */
	private static Map<Integer, TProUserSfrzCl> proUserSfrzclMap;
	
	/**
	 * 清除数据，释放内存
	 */
	public static void clearData() {
		layy = null;
		plaintiffList = null;
		defendantList = null;
		agentList = null;
		witnessList = null;
		indictmentList = null;
		indictmentFjListMap = null;
		dsrIndictmentListMap = null;
		certificateListMap = null;
		certificateFjListMap = null;
		dsrCertificateListMap = null;
		evidenceList = null;
		evidenceMaterial = null;
		proUserSfrzclMap = null;
		System.gc();
	}


	/**
	 * 设置  网上立案 基本信息
	 * 
	 * @param layy
	 */
	public static void setLayy(TLayy newLayy) {
		layy = newLayy;
	}
	
	/**
	 * 获取  网上立案 基本信息
	 * 
	 * @return layy
	 */
	public static TLayy getLayy() {
		if (null == layy) {
			layy = new TLayy();
		}
		return layy;
	}

	/**
	 * 获取 当事人_原告 List
	 * 
	 * @return plaintiffList
	 */
	public static List<TLayyDsr> getPlaintiffList() {
		if (null == plaintiffList) {
			plaintiffList = new ArrayList<TLayyDsr>();
		}
		return plaintiffList;
	}

	/**
	 * 获取 当事人_被告 List
	 * 
	 * @return defendantList
	 */
	public static List<TLayyDsr> getDefendantList() {
		if (null == defendantList) {
			defendantList = new ArrayList<TLayyDsr>();
		}
		return defendantList;
	}

	/**
	 * 获取 代理人 List
	 * 
	 * @return agentList
	 */
	public static List<TLayyDlr> getAgentList() {
		if (null == agentList) {
			agentList = new ArrayList<TLayyDlr>();
		}
		return agentList;
	}

	/**
	 * 获取 证人 List
	 * 
	 * @return witnessList
	 */
	public static List<TLayyZr> getWitnessList() {
		if (null == witnessList) {
			witnessList = new ArrayList<TLayyZr>();
		}
		return witnessList;
	}

	/**
	 * 获取 起诉状 List
	 * 
	 * @return indictmentList
	 */
	public static List<TLayySscl> getIndictmentList() {
		if (null == indictmentList) {
			indictmentList = new ArrayList<TLayySscl>();
		}
		return indictmentList;
	}

	/**
	 * 获取 起诉状附件 Map[起诉状id， List[附件]]
	 * 
	 * @return indictmentFjListMap
	 */
	public static Map<String, ArrayList<TLayySsclFj>> getIndictmentFjListMap() {
		if (null == indictmentFjListMap) {
			indictmentFjListMap = new HashMap<String, ArrayList<TLayySsclFj>>();
		}
		return indictmentFjListMap;
	}

	/**
	 * 获取 起诉状关联当事人 Map[起诉状id，List[当事人诉讼材料]]
	 * 
	 * @return dsrIndictmentListMap
	 */
	public static Map<String, ArrayList<TLayyDsrSscl>> getDsrIndictmentListMap() {
		if (null == dsrIndictmentListMap) {
			dsrIndictmentListMap = new HashMap<String, ArrayList<TLayyDsrSscl>>();
		}
		return dsrIndictmentListMap;
	}

	/**
	 * 获取 证件 Map[人员Id，List[诉讼材料]]
	 * 
	 * @return certificateListMap
	 */
	public static Map<String, ArrayList<TLayySscl>> getCertificateListMap() {
		if (null == certificateListMap) {
			certificateListMap = new HashMap<String, ArrayList<TLayySscl>>();
		}
		return certificateListMap;
	}

	/**
	 * 获取 证件_附件 Map[证件id，List[附件]]
	 * 
	 * @return certificateFjListMap
	 */
	public static Map<String, ArrayList<TLayySsclFj>> getCertificateFjListMap() {
		if (null == certificateFjListMap) {
			certificateFjListMap = new HashMap<String, ArrayList<TLayySsclFj>>();
		}
		return certificateFjListMap;
	}

	/**
	 * 获取 证件_附件_所属人 Map[证件id，List[证件所属人]]
	 * 
	 * @return dsrCertificateListMap
	 */
	public static Map<String, ArrayList<TLayyDsrSscl>> getDsrCertificateListMap() {
		if (null == dsrCertificateListMap) {
			dsrCertificateListMap = new HashMap<String, ArrayList<TLayyDsrSscl>>();
		}
		return dsrCertificateListMap;
	}

	/**
	 * 获取 证据 List
	 * 
	 * @return evidenceList
	 */
	public static List<TZj> getEvidenceList() {
		if (null == evidenceList) {
			evidenceList = new ArrayList<TZj>();
		}
		return evidenceList;
	}

	/**
	 * 获取 证据 材料 Map[证据id， List[证据材料]]
	 * 
	 * @return evidenceMaterial
	 */
	public static Map<String, ArrayList<TZjcl>> getEvidenceMaterial() {
		if (null == evidenceMaterial) {
			evidenceMaterial = new HashMap<String, ArrayList<TZjcl>>();
		}
		return evidenceMaterial;
	}


	/**  
	 * 获取  申请人身份认证 Map[类型，身份认证材料]  
	 * @return proUserSfrzclMap  
	 */
	@SuppressLint("UseSparseArrays")
	public static Map<Integer, TProUserSfrzCl> getProUserSfrzclMap() {
		if (null == proUserSfrzclMap) {
			proUserSfrzclMap = new HashMap<Integer, TProUserSfrzCl>();
		}
		return proUserSfrzclMap;
	}
}
