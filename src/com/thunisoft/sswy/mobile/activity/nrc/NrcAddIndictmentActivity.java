package com.thunisoft.sswy.mobile.activity.nrc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmOtherDialogActivity_;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcSsclFjAdapter;
import com.thunisoft.sswy.mobile.datasource.NrcDsrDao;
import com.thunisoft.sswy.mobile.datasource.NrcDsrSsclDao;
import com.thunisoft.sswy.mobile.datasource.NrcSsclDao;
import com.thunisoft.sswy.mobile.datasource.NrcSsclFjDao;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 添加起诉状
 * 
 */

@EActivity(R.layout.activity_nrc_add_indictment)
public class NrcAddIndictmentActivity extends BaseActivity {

	/** 起诉状 名称 */
	@ViewById(R.id.nrc_ai_name)
	protected EditText nameET;

	@ViewById(R.id.nrc_ai_plaintiff)
	protected LinearLayout plaintiffLL;
	
	/** 起诉状 原告 姓名拼串 */
	@ViewById(R.id.nrc_ai_plaintiff_names)
	protected TextView plaintiffNamesTV;

	@ViewById(R.id.nrc_ai_defendant)
	protected LinearLayout defendantLL;
	
	/** 起诉状 被告 姓名拼串 */
	@ViewById(R.id.nrc_ai_defendant_names)
	protected TextView defendantNamesTV;

	/** 起诉状 材料列表 */
	@ViewById(R.id.nrc_ai_material_list)
	protected GridView materialGridView;

	/** 起诉状 删除 按钮 */
	@ViewById(R.id.nrc_ai_delete)
	protected Button deleteBtn;

	/** 诉讼材料 intent key */
	public static final String K_SSCL = "sscl";

	/** 诉讼材料实体 bean */
	private TLayySscl sscl;

	/** 诉讼材料 附件 List */
	private List<TLayySsclFj> ssclFjList = new ArrayList<TLayySsclFj>();
	
	/** 当事人诉讼材料 List */
	private ArrayList<TLayyDsrSscl> dsrSsclList = new ArrayList<TLayyDsrSscl>();

	/** 诉讼材料 附件 adapter */
	private NrcSsclFjAdapter ssclFjAdapter;

	@Bean
	NrcSsclDao ssclDao;

	@Bean
	NrcSsclFjDao ssclFjDao;

	@Bean
	NrcDsrSsclDao dsrSsclDao;

	@Bean
	NrcDsrDao dsrDao;
	
	@Bean
	DownloadLogic downloadLogic;
	
	@Bean
	DeleteLogic deleteLogic;

	/** 关联原告 */
	private static final int REQ_REL_PLAINTIFF = 2;

	/** 关联被告*/
	private static final int REQ_REL_DEFENDANT = 4;
	
	/** 添加 起诉状附件*/
	private static final int REQ_CODE_ADD_QSZ_FJ = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void onAfterView() {
		downloadLogic.activity = this;
		deleteLogic.activity = this;
		Intent intent = getIntent();
		sscl = (TLayySscl) intent.getSerializableExtra(K_SSCL);
		if (StringUtils.isBlank(sscl.getCId())) {
			sscl.setCId(UUIDHelper.getUuid());
			sscl.setNType(NrcConstants.SSCL_TYPE_INDICTMENT);
		}
		List<TLayySsclFj> ssclFjListTemp = NrcEditData.getIndictmentFjListMap().get(sscl.getCId());
		if (null != ssclFjListTemp && ssclFjListTemp.size() > 0) {
			ssclFjList.addAll(ssclFjListTemp);
		}
		nameET.setFocusable(false);
		List<TLayyDsrSscl> localDsrSsclList = NrcEditData.getDsrIndictmentListMap().get(sscl.getCId());
		if (null != localDsrSsclList && localDsrSsclList.size() > 0) {
			dsrSsclList.addAll(localDsrSsclList);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		nameET.setText(sscl.getCName());
		refreshSscjFj();
		refreshRelLitigant();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Constants.RESULT_OK != resultCode && Activity.RESULT_OK != resultCode) {
			return;
		}

		switch (requestCode) {
		case NrcConstants.REQ_CODE_CANCEL:
			this.finish();
			break;
		case REQ_REL_PLAINTIFF:
			ArrayList<TLayyDsrSscl> plaintiffSsclList = (ArrayList<TLayyDsrSscl>)data.getSerializableExtra(NrcRelLitigantActivity.K_DSR_SSCL_LIST);
			dsrSsclList.clear();
			dsrSsclList.addAll(plaintiffSsclList);
			break;
		case REQ_REL_DEFENDANT:
			ArrayList<TLayyDsrSscl> defendantSsclList = (ArrayList<TLayyDsrSscl>)data.getSerializableExtra(NrcRelLitigantActivity.K_DSR_SSCL_LIST);
			dsrSsclList.clear();
			dsrSsclList.addAll(defendantSsclList);
			break;
		case REQ_CODE_ADD_QSZ_FJ:
			// 获取路径名
			SerializableSOMap soMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);

			Map<String, TLayySsclFj> localSsclFjMap = new LinkedHashMap<String, TLayySsclFj>();
			if (null != ssclFjList && ssclFjList.size() > 0) {
				for (int i = 0; i < ssclFjList.size(); i++) {
					TLayySsclFj ssclFj = ssclFjList.get(i);
					localSsclFjMap.put(ssclFj.getCPath(), ssclFj);
				}
			}
			Map<String, TLayySsclFj> localSsclFjMapTemp = new LinkedHashMap<String, TLayySsclFj>();
			localSsclFjMapTemp.putAll(localSsclFjMap);

			Map<String, Object> fjPathMap = soMap.getMap();
			if (null != fjPathMap && fjPathMap.size() > 0) {
				int xssx = 0;
				if (ssclFjList.size() > 0) {
					if (null != ssclFjList.get(ssclFjList.size() - 1).getNXssx()) {
						xssx = ssclFjList.get(ssclFjList.size() - 1).getNXssx();
					}
				}
				for (Map.Entry<String, Object> entry : fjPathMap.entrySet()) {
					String path = entry.getKey();
					if (null == localSsclFjMap.get(path)) { // 增加本次在本地新选的图片
						xssx++;
						TLayySsclFj ssclFj = new TLayySsclFj();
						ssclFj.setCId(UUIDHelper.getUuid());
						ssclFj.setCLayyId(sscl.getCLayyId());
						ssclFj.setCPath(entry.getKey());
						ssclFj.setNSync(NrcConstants.SYNC_FALSE);
						ssclFj.setNXssx(xssx);
						ssclFj.setCSsclId(sscl.getCId());
						String fileName = (String) entry.getValue();
						ssclFj.setCOriginName(fileName);
						localSsclFjMapTemp.put(path, ssclFj);
					}
				}
			}

			if (localSsclFjMap.size() > 0) { // 去除本次本地没选中的图片
				for (Map.Entry<String, TLayySsclFj> entry : localSsclFjMap.entrySet()) {
					String path = entry.getKey();
					if (null == fjPathMap.get(path)) {
						localSsclFjMapTemp.remove(path);
					}
				}
			}
			ssclFjList.clear();
			for (Map.Entry<String, TLayySsclFj> entry : localSsclFjMapTemp.entrySet()) {
				ssclFjList.add(entry.getValue());
			}

			break;
			
		case Constants.REQUEST_CODE_DELETE_INDICTMENT:
			deleteBtnSure();
			break;
		default:
			break;
		}
	}

	/**
	 * 点击 起诉状 返回
	 */
	@Click(R.id.nrc_ai_back)
	protected void clickBack() {
		Intent intent = new Intent(NrcAddIndictmentActivity.this, ConfirmDialogActivity_.class);
		intent.putExtra("message", "数据尚未保存，确定取消？");
		startActivityForResult(intent, NrcConstants.REQ_CODE_CANCEL);
	}

	/**
	 * 点击 起诉状 确定
	 */
	@Click(R.id.nrc_ai_save)
	protected void clickSure() {
		if (checkData()) {
			List<TLayySscl> indictmentList = NrcEditData.getIndictmentList();
			int delPosition = getDelPosition(indictmentList);
			if (delPosition >= 0) {
				indictmentList.remove(delPosition);
				indictmentList.add(delPosition, sscl); // 先保存到静态变量中，在立案预约中，点击暂存或者提交，统一保存
			} else {
				indictmentList.add(sscl); // 先保存到静态变量中，在立案预约中，点击暂存或者提交，统一保存
			}
			saveIndictmentFj();
			if (null != dsrSsclList && dsrSsclList.size() > 0) {
				for (TLayyDsrSscl dsrSscl : dsrSsclList) {
					dsrSscl.setCSsclName(sscl.getCName()); // 更新当事人诉讼材料 表的 名称字段
				}
			}
			ArrayList<TLayyDsrSscl> localDsrSsclList = NrcEditData.getDsrIndictmentListMap().get(sscl.getCId());
			if (null == localDsrSsclList) {
				localDsrSsclList = new ArrayList<TLayyDsrSscl>();
				NrcEditData.getDsrIndictmentListMap().put(sscl.getCId(), localDsrSsclList);
			} else {
				localDsrSsclList.clear();
			}
			localDsrSsclList.addAll(dsrSsclList);
			NrcAddIndictmentActivity.this.finish();
		}
	}

	/**
	 * 点击 关联原告
	 */
	@Click(R.id.nrc_ai_rel_plaintiff)
	protected void clickRelPlaintiff() {
		Intent intent = new Intent();
		intent.setClass(this, NrcRelLitigantActivity_.class);
		intent.putExtra(NrcRelLitigantActivity.K_SSCL, sscl);
		intent.putExtra(NrcRelLitigantActivity.K_LITIGANT_SSDW, Constants.LITIGANT_SSDW_PLAINTIFF);
		intent.putExtra(NrcRelLitigantActivity.K_DSR_SSCL_LIST, dsrSsclList);
		startActivityForResult(intent, REQ_REL_PLAINTIFF);
	}

	/**
	 * 点击 关联被告
	 */
	@Click(R.id.nrc_ai_rel_defendant)
	protected void clickRelDefendant() {
		Intent intent = new Intent();
		intent.setClass(this, NrcRelLitigantActivity_.class);
		intent.putExtra(NrcRelLitigantActivity.K_SSCL, sscl);
		intent.putExtra(NrcRelLitigantActivity.K_LITIGANT_SSDW, Constants.LITIGANT_SSDW_DEFENDANT);
		intent.putExtra(NrcRelLitigantActivity.K_DSR_SSCL_LIST, dsrSsclList);
		startActivityForResult(intent, REQ_REL_DEFENDANT);
	}

	/**
	 * 点击 起诉状 删除
	 */
	@Click(R.id.nrc_ai_delete)
	protected void clickDeleteBtn() {
		Intent intent = new Intent(NrcAddIndictmentActivity.this, ConfirmOtherDialogActivity_.class);
        intent.putExtra("message", getResources().getString(R.string.text_delete));
        NrcAddIndictmentActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_DELETE_INDICTMENT);
	}
	
	public void deleteBtnSure(){
		List<TLayySscl> indictmentList = NrcEditData.getIndictmentList();
		int delPosition = getDelPosition(indictmentList);
		if (delPosition >= 0) {
			indictmentList.remove(delPosition);
		}
		NrcAddIndictmentActivity.this.finish();
	}

	/**
	 * 刷新诉讼材料附件列表
	 */
	private void refreshSscjFj() {
		if (null == ssclFjAdapter) {
			ssclFjAdapter = new NrcSsclFjAdapter(this, sscl, ssclFjList);
			ssclFjAdapter.setRequestCode(REQ_CODE_ADD_QSZ_FJ);
			ssclFjAdapter.setDownloadLogic(downloadLogic);
			ssclFjAdapter.setDeleteLogic(deleteLogic);
			ssclFjAdapter.setSsclFjType(NrcConstants.SSCL_TYPE_INDICTMENT);
			ssclFjAdapter.setFjType(NrcSsclFjAdapter.FJ_TYPE_MULTIPLE);
			materialGridView.setAdapter(ssclFjAdapter);
		} else {
			ssclFjAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 刷新 当前关联当事人
	 */
	private void refreshRelLitigant() {

		StringBuffer pIndictmentNames = new StringBuffer(""); // 已关联的原告姓名 拼串
																// 用于起诉状名称
		StringBuffer dIndictmentNames = new StringBuffer(""); // 已关联的被告姓名 拼串
																// 用于起诉状名称

		StringBuffer plaintiffNames = new StringBuffer(""); // 已关联的原告姓名 拼串
		StringBuffer defendantNames = new StringBuffer(""); // 已关联的被告姓名 拼串

		StringBuffer ssclSsrId = new StringBuffer("");
		StringBuffer ssclSsrMc = new StringBuffer("");

		Map<String, TLayyDsrSscl> dsrSsclMap = new HashMap<String, TLayyDsrSscl>();
		
		if (null != dsrSsclList && dsrSsclList.size() > 0) {
			for (TLayyDsrSscl dsrSscl : dsrSsclList) {
				dsrSsclMap.put(dsrSscl.getCDsrId(), dsrSscl);
			}
		}

		List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
		for (TLayyDsr plaintiff : plaintiffList) {
			if (null != dsrSsclMap.get(plaintiff.getCId())) {
				plaintiffNames.append(plaintiff.getCName()).append(NrcConstants.INDICTMENT_REL_LITIGANT_SPLIT);
				pIndictmentNames.append(plaintiff.getCName()).append(NrcConstants.INDICTMENT_NAME_LITIGANT_SPLIT);
				ssclSsrId.append(plaintiff.getCId()).append(NrcConstants.REL_NAME_SPLIT);
				ssclSsrMc.append(plaintiff.getCName()).append(NrcConstants.REL_NAME_SPLIT);
			}
		}
		List<TLayyDsr> defendantList = NrcEditData.getDefendantList();
		for (TLayyDsr defendant : defendantList) {
			if (null != dsrSsclMap.get(defendant.getCId())) {
				defendantNames.append(defendant.getCName()).append(NrcConstants.INDICTMENT_REL_LITIGANT_SPLIT);
				dIndictmentNames.append(defendant.getCName()).append(NrcConstants.INDICTMENT_NAME_LITIGANT_SPLIT);
				ssclSsrId.append(defendant.getCId()).append(NrcConstants.REL_NAME_SPLIT);
				ssclSsrMc.append(defendant.getCName()).append(NrcConstants.REL_NAME_SPLIT);
			}
		}
		sscl.setCSsryId("");
		sscl.setCSsryMc("");
		if (StringUtils.isNotBlank(ssclSsrId.toString())) {
			String ssclSsrIdTemp = ssclSsrId.toString();
			ssclSsrIdTemp = ssclSsrIdTemp.substring(0, ssclSsrIdTemp.length() - 1);
			String ssclSsrMcTemp = ssclSsrMc.toString();
			ssclSsrMcTemp = ssclSsrMcTemp.substring(0, ssclSsrMcTemp.length() - 1);
			sscl.setCSsryId(ssclSsrIdTemp);
			sscl.setCSsryMc(ssclSsrMcTemp);
		}
		if (StringUtils.isNotBlank(plaintiffNames.toString())) {
			plaintiffLL.setVisibility(View.VISIBLE);
			plaintiffNamesTV.setText(plaintiffNames.toString());
		} else {
			plaintiffLL.setVisibility(View.GONE);
		}
		if (StringUtils.isNotBlank(defendantNames.toString())) {
			defendantLL.setVisibility(View.VISIBLE);
			defendantNamesTV.setText(defendantNames.toString());
		} else {
			defendantLL.setVisibility(View.GONE);
		}
		String ssclName = "";
		if (StringUtils.isNotBlank(pIndictmentNames.toString())) {
			StringBuffer ssclNameSb = new StringBuffer("名称：");
			String pIndictmentTemp = pIndictmentNames.toString();
			pIndictmentTemp = pIndictmentTemp.substring(0, pIndictmentTemp.length() - 1);
			ssclNameSb.append(pIndictmentTemp).append("起诉");

			if (StringUtils.isNotBlank(dIndictmentNames.toString())) {
				String dIndictmentTemp = dIndictmentNames.toString();
				dIndictmentTemp = dIndictmentTemp.substring(0, dIndictmentTemp.length() - 1);
				ssclNameSb.append(dIndictmentTemp);
				ssclName = ssclNameSb.toString();
			}
		}
		if (StringUtils.isNotBlank(ssclName)) {
			sscl.setCName(ssclName.toString());
			nameET.setText(sscl.getCName());
		}
	}

	/**
	 * 检查起诉状是否关联了原告、被告、上传了附件
	 * 
	 * @return
	 */
	private boolean checkData() {
		String relPlaintiffNames = plaintiffNamesTV.getText().toString();
		if (StringUtils.isBlank(relPlaintiffNames)) {
			Toast.makeText(this, "请关联原告", Toast.LENGTH_SHORT).show();
			return false;
		}

		String relDefendantNames = defendantNamesTV.getText().toString();
		if (StringUtils.isBlank(relDefendantNames)) {
			Toast.makeText(this, "请关联被告", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (ssclFjList.size() == 0) {
			Toast.makeText(this, "请添加附件", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 获取当前添加的诉讼材料在 诉讼材料list中的位置
	 * 
	 * @param indictmentList
	 * @return
	 */
	private int getDelPosition(List<TLayySscl> indictmentList) {
		int delPosition = -1;
		if (null != indictmentList && indictmentList.size() > 0) {
			for (int i = 0; i < indictmentList.size(); i++) {
				TLayySscl ssclTemp = indictmentList.get(i);
				if (ssclTemp.getCId().equals(sscl.getCId())) {
					delPosition = i;
					break;
				}
			}
		}
		return delPosition;
	}

	/**
	 * 保存当前诉讼材料_附件，到静态变量中
	 */
	private void saveIndictmentFj() {
		ArrayList<TLayySsclFj> localSsclFjList = NrcEditData.getIndictmentFjListMap().get(sscl.getCId());
		if (null == localSsclFjList) {
			localSsclFjList = new ArrayList<TLayySsclFj>();
			NrcEditData.getIndictmentFjListMap().put(sscl.getCId(), localSsclFjList);
		} else {
			localSsclFjList.clear();
		}
		localSsclFjList.addAll(ssclFjList);
	}
}
