package com.thunisoft.sswy.mobile.activity.nrc;

import java.io.File;
import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmOtherDialogActivity_;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcEvidenceFjAdapter;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.ServiceUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.view.NoScrollGridView;

/**
 * 网上立案 添加证据
 * 
 */

@EActivity(R.layout.activity_nrc_add_evidence)
public class NrcAddEvidenceActivity extends BaseActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_ae_title)
	protected TextView titleTV;

	/** 证据 姓名 */
	@ViewById(R.id.nrc_ae_name)
	protected EditText nameET;

	/** 证据 所属人 名称 */
	@ViewById(R.id.nrc_ae_owner_name)
	protected TextView ownerNameTV;

	/** 证明 问题 */
	@ViewById(R.id.nrc_ae_prove_problem)
	protected EditText proveProblemET;

	/** 证据 来源 */
	@ViewById(R.id.nrc_ae_evidence_from)
	protected EditText evidenceFromET;

	/** 证据 文件列表 */
	@ViewById(R.id.nrc_ae_file_list)
	protected NoScrollGridView fileGridView;

	/** 证据 删除 按钮 */
	@ViewById(R.id.nrc_ae_delete)
	protected Button deleteBtn;

	/** 证件 intent key */
	public static final String K_ZJ = "zj";

	/** 证据 实体 bean */
	private TZj zj;

	/** 证据 材料 List */
	private List<TZjcl> zjclList = new ArrayList<TZjcl>();

	/** 证据 材料 adapter */
	private NrcEvidenceFjAdapter evidenceFjAdapter;

	private static final int REQ_CODE_EVIDENCE_REL_OWNER = 5;

	public static final int REQ_CODE_SELECT_FILE = 6;

	@Bean
	DownloadLogic downloadLogic;
	
	@Bean
	DeleteLogic deleteLogic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		zj = (TZj) intent.getSerializableExtra(K_ZJ);
	}

	@AfterViews
	protected void onAfterView() {
		downloadLogic.activity = this;
		deleteLogic.activity = this;
		if (StringUtils.isNotBlank(zj.getCId())) {
			nameET.setText(zj.getCName());
			proveProblemET.setText(zj.getCZmwt());
			evidenceFromET.setText(zj.getCZjly());
			List<TZjcl> localZjclList = NrcEditData.getEvidenceMaterial().get(zj.getCId());
			if (null != localZjclList && localZjclList.size() > 0) {
				zjclList.addAll(localZjclList);
			}
		} else {
			zj.setCId(UUIDHelper.getUuid());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ownerNameTV.setText(zj.getCSsryMc());
		refreshZjclList();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Constants.RESULT_OK != resultCode && Activity.RESULT_OK != resultCode) {
			return;
		}

		switch (requestCode) {
		case NrcConstants.REQ_CODE_CANCEL:
			this.finish();
			break;
		case REQ_CODE_EVIDENCE_REL_OWNER:
			zj = (TZj) data.getSerializableExtra(NrcEvidenceRelOwnerActivity.K_EVIDENCE);
			break;
		case REQ_CODE_SELECT_FILE:
			// 获取路径名
			SerializableSOMap soMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			
			Map<String, TZjcl> localZjclMap = new LinkedHashMap<String, TZjcl>();
			if (null != zjclList && zjclList.size() > 0) {
				for (int i = 0; i < zjclList.size(); i++) {
					TZjcl zjcl = zjclList.get(i);
					localZjclMap.put(zjcl.getCPath(), zjcl);
				}
			}
			Map<String, TZjcl> localZjclMapTemp = new LinkedHashMap<String, TZjcl>();
			localZjclMapTemp.putAll(localZjclMap);

			Map<String, Object> fjPathMap = soMap.getMap();
			if (null != fjPathMap && fjPathMap.size() > 0) {
				for (Map.Entry<String, Object> entry : fjPathMap.entrySet()) {
					String path = entry.getKey();
					if (null == localZjclMap.get(path)) { // 增加本次在本地新选的图片
						TZjcl zjcl = new TZjcl();
						zjcl.setCId(UUIDHelper.getUuid());
						File file = new File(path);
						zjcl.setCOriginName(file.getName());
						zjcl.setCPath(entry.getKey());
						zjcl.setCZjBh(zj.getCId());
						String nowStr = ServiceUtils.getServerTimeStr(this);
						zjcl.setDCreate(nowStr);
						zjcl.setNSync(NrcConstants.SYNC_FALSE);
						localZjclMapTemp.put(entry.getKey(), zjcl);
					}
				}
			}

			if (localZjclMap.size() > 0) { // 去除本次本地没选中的图片
				for (Map.Entry<String, TZjcl> entry : localZjclMap.entrySet()) {
					String path = entry.getKey();
					if (null == fjPathMap.get(path)) {
						localZjclMapTemp.remove(path);
					}
				}
			}
			zjclList.clear();
			for (Map.Entry<String, TZjcl> entry : localZjclMapTemp.entrySet()) {
				zjclList.add(entry.getValue());
			}
			break;
			
		case Constants.REQUEST_CODE_DELETE_EVIDENCE:
			deleteBtnSure();
			break;

		default:
			break;
		}
	}

	/**
	 * 点击 证据 返回
	 */
	@Click(R.id.nrc_ae_back)
	protected void clickBack() {
		Intent intent = new Intent(this, ConfirmDialogActivity_.class);
		intent.putExtra("message", "数据尚未保存，确定取消？");
		startActivityForResult(intent, NrcConstants.REQ_CODE_CANCEL);
	}

	/**
	 * 点击 证据 保存
	 */
	@Click(R.id.nrc_ae_save)
	protected void clickSure() {
		if (checkEvidenceData()) {
			List<TZj> zjList = NrcEditData.getEvidenceList();
			int delPosition = getDelPosition(zjList);
			if (delPosition >= 0) {
				zjList.remove(delPosition);
				zjList.add(delPosition, zj);
			} else {
				zjList.add(zj);
			}
			ArrayList<TZjcl> localzjclList = NrcEditData.getEvidenceMaterial().get(zj.getCId());
			if (null == localzjclList) {
				localzjclList = new ArrayList<TZjcl>();
				NrcEditData.getEvidenceMaterial().put(zj.getCId(), localzjclList);
			} else {
				localzjclList.clear();
			}
			localzjclList.addAll(zjclList);
			NrcAddEvidenceActivity.this.finish();
		}
	}

	/**
	 * 点击 提出人
	 */
	@Click(R.id.nrc_ae_owner)
	protected void clickOwner() {
		Intent intent = new Intent();
		intent.setClass(this, NrcEvidenceRelOwnerActivity_.class);
		intent.putExtra(NrcEvidenceRelOwnerActivity.K_EVIDENCE, zj);
		intent.putExtra(NrcEvidenceRelOwnerActivity.K_LITIGANT_SSDW, "原告");
		startActivityForResult(intent, REQ_CODE_EVIDENCE_REL_OWNER);
	}

	/**
	 * 点击 证据 删除
	 */
	@Click(R.id.nrc_ae_delete)
	protected void clickDeleteBtn() {
		Intent intent = new Intent(NrcAddEvidenceActivity.this, ConfirmOtherDialogActivity_.class);
        intent.putExtra("message", getResources().getString(R.string.text_delete));
        NrcAddEvidenceActivity.this.startActivityForResult(intent, Constants.REQUEST_CODE_DELETE_EVIDENCE);
	}
	
	public void deleteBtnSure(){
		List<TZj> zjList = NrcEditData.getEvidenceList();
		int delPosition = getDelPosition(zjList);
		if (delPosition >= 0) {
			zjList.remove(delPosition);
		}
		NrcAddEvidenceActivity.this.finish();
	}

	/**
	 * 刷新诉讼材料附件列表
	 */
	private void refreshZjclList() {
		if (null == evidenceFjAdapter) {
			evidenceFjAdapter = new NrcEvidenceFjAdapter(this, zjclList);
			evidenceFjAdapter.setDownloadLogic(downloadLogic);
			evidenceFjAdapter.setDeleteLogic(deleteLogic);
			evidenceFjAdapter.setZj(zj);
			fileGridView.setAdapter(evidenceFjAdapter);
		} else {
			evidenceFjAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 检查证据输入项
	 * 
	 * @return
	 */
	private boolean checkEvidenceData() {
		String name = nameET.getText().toString().trim();
		String evidenceProblem = proveProblemET.getText().toString().trim();
		String evidenceFrom = evidenceFromET.getText().toString().trim();
		zj.setCName(name);
		zj.setCZmwt(evidenceProblem);
		zj.setCZjly(evidenceFrom);
		if (StringUtils.isBlank(zj.getCName())) {
			Toast.makeText(this, "请输入证据名称", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtils.isBlank(zj.getCSsryId())) {
			Toast.makeText(this, "请关联提出人", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtils.isBlank(zj.getCZmwt())) {
			Toast.makeText(this, "请输入证明问题", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (StringUtils.isBlank(zj.getCZjly())) {
			Toast.makeText(this, "请输入证据来源", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (zjclList.size() == 0) {
			Toast.makeText(this, "请上传证据材料", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 查找该条数据是不是之前保存的证据， 是：更新，否：插入
	 * 
	 * @param layyZrList
	 * @return
	 */
	private int getDelPosition(List<TZj> zjList) {
		int delPosition = -1;
		if (null != zjList && zjList.size() > 0) {
			for (int i = 0; i < zjList.size(); i++) {
				TZj currZj = zjList.get(i);
				if (currZj.getCId().equals(zj.getCId())) {
					delPosition = i;
					break;
				}
			}
		}
		return delPosition;
	}
}
