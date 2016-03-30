package com.thunisoft.sswy.mobile.activity.nrc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.adapter.nrc.NrcRelLitigantAdapter;
import com.thunisoft.sswy.mobile.datasource.NrcDsrDao;
import com.thunisoft.sswy.mobile.datasource.NrcDsrSsclDao;
import com.thunisoft.sswy.mobile.pojo.TLayyDsr;
import com.thunisoft.sswy.mobile.pojo.TLayyDsrSscl;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.util.UUIDHelper;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 诉讼材料_关联当事人
 * 
 */

@EActivity(R.layout.activity_nrc_rel_litigant)
public class NrcRelLitigantActivity extends BaseActivity {

	/**
	 * 标题
	 */
	@ViewById(R.id.nrc_rel_litigant_title)
	protected TextView titleTV;

	/**
	 * 全选
	 */
	@ViewById(R.id.nrc_rel_litigant_select_all)
	protected TextView selectAllTV;

	/**
	 * 左上角 提示 ：原告、被告
	 */
	@ViewById(R.id.nrc_rel_litigant_tip)
	protected TextView litigantTipTV;

	/**
	 * 当事人列表
	 */
	@ViewById(R.id.nrc_rel_litigant_list)
	protected ListView litigantListView;

	/**
	 * 所有符合条件的当事人
	 */
	private List<TLayyDsr> dsrList = new ArrayList<TLayyDsr>();

	/**
	 * 关联当事人列表 adapter
	 */
	private NrcRelLitigantAdapter relLitigantAdapter;

	/**
	 * 已关联的当事人
	 */
	private Map<String, TLayyDsr> checkedMap = new HashMap<String, TLayyDsr>();

	/** 当事人 诉讼地位（原告、被告） intent Key */
	public static final String K_LITIGANT_SSDW = "litigantSsdw";

	/** 诉讼材料 intent Key */
	public static final String K_SSCL = "sscl";
	
	/** 当事人 起诉状 List */
	public static final String K_DSR_SSCL_LIST = "dsrSsclList";
	
	private ArrayList<TLayyDsrSscl> dsrSsclList;

	/** 诉讼材料 */
	private TLayySscl sscl;

	/** 当事人 诉讼地位（原告、被告） */
	private String litigantSsdw;

	@Bean
	NrcDsrDao dsrDao;

	@Bean
	NrcDsrSsclDao dsrSsclDao;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		sscl = (TLayySscl) intent.getSerializableExtra(K_SSCL);
		litigantSsdw = intent.getStringExtra(K_LITIGANT_SSDW);
		dsrSsclList = (ArrayList<TLayyDsrSscl>)intent.getSerializableExtra(K_DSR_SSCL_LIST);
	}

	@AfterViews
	protected void onAfterView() {
		// 原告
		titleTV.setText("关联" + litigantSsdw);
		litigantTipTV.setText(litigantSsdw);
		refreshLitigantList();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 点击全选或取消全选
	 */
	@Click(R.id.nrc_rel_litigant_select_all)
	protected void clickSelectAll() {
		String selectAllType = selectAllTV.getText().toString();
		String selectAll = getResources().getString(R.string.nrc_select_all);
		String selectAllCancel = getResources().getString(R.string.nrc_select_all_cancel);
		if (selectAll.equals(selectAllType)) { // 全选
			selectAllTV.setText(selectAllCancel);
			for (TLayyDsr dsr : dsrList) {
				checkedMap.put(dsr.getCId(), dsr);
				relLitigantAdapter.notifyDataSetChanged();
			}
		} else { // 取消全选
			selectAllTV.setText(selectAll);
			checkedMap.clear();
			relLitigantAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 点击 关联当事人 返回
	 */
	@Click(R.id.nrc_rel_litigant_back)
	protected void clickBack() {
		NrcRelLitigantActivity.this.finish();
	}

	/**
	 * 点击 关联当事人 确定
	 */
	@Click(R.id.nrc_rel_litigant_sure)
	protected void clickSure() {
		// 保存当事人诉讼材料表，
		ArrayList<TLayyDsrSscl> dstSsclListTemp = new ArrayList<TLayyDsrSscl>();
		Map<String, TLayyDsrSscl> dsrSsclMap = new HashMap<String, TLayyDsrSscl>();
		if (null != dsrSsclList && dsrSsclList.size() > 0) {
			for (TLayyDsrSscl dsrSscl : dsrSsclList) {
				dsrSsclMap.put(dsrSscl.getCDsrId(), dsrSscl);
			}
		}
		// 不是原告，为被告
		if (!Constants.LITIGANT_SSDW_PLAINTIFF.equals(litigantSsdw)) { // 如果是被告，不能删除原告的数据
			List<TLayyDsr> plaintiffList = NrcEditData.getPlaintiffList();
			for (TLayyDsr plaintiff : plaintiffList) {
				TLayyDsrSscl dsrSscl = dsrSsclMap.get(plaintiff.getCId());
				if (null != dsrSscl) {
					dstSsclListTemp.add(dsrSscl);
				}
			}
		} else { // 如果是原告，不能删除被告的数据
			List<TLayyDsr> defendantList = NrcEditData.getDefendantList();
			for (TLayyDsr defendant : defendantList) {
				TLayyDsrSscl dsrSscl = dsrSsclMap.get(defendant.getCId());
				if (null != dsrSscl) {
					dstSsclListTemp.add(dsrSscl);
				}
			}
		}
		if (checkedMap.size() > 0) {
			for (Map.Entry<String, TLayyDsr> entry : checkedMap.entrySet()) {
				TLayyDsr dsr = entry.getValue();
				TLayyDsrSscl dsrSscl = new TLayyDsrSscl();
				dsrSscl.setCId(UUIDHelper.getUuid());
				dsrSscl.setCDsrId(dsr.getCId());
				dsrSscl.setCDsrName(dsr.getCName());
				dsrSscl.setCLayyId(sscl.getCLayyId());
				dsrSscl.setCSsclId(sscl.getCId());
				dsrSscl.setCSsclName(sscl.getCName());
				dstSsclListTemp.add(dsrSscl);
			}
		}
		Intent intent = new Intent();
		intent.putExtra(K_DSR_SSCL_LIST, dstSsclListTemp);
		setResult(Constants.RESULT_OK, intent);
		this.finish();
	}

	/**
	 * 刷新当事人列表
	 */
	private void refreshLitigantList() {
		dsrList.clear();
		checkedMap.clear();
		Map<String, TLayyDsrSscl> dsrSsclMap = new HashMap<String, TLayyDsrSscl>();
		if (null != dsrSsclList && dsrSsclList.size() > 0) {
			for (TLayyDsrSscl dsrSscl : dsrSsclList) {
				dsrSsclMap.put(dsrSscl.getCDsrId(), dsrSscl);
			}
		}

		if (Constants.LITIGANT_SSDW_PLAINTIFF.equals(litigantSsdw)) {
			dsrList.addAll(NrcEditData.getPlaintiffList());
			for (TLayyDsr plaintiff : NrcEditData.getPlaintiffList()) {
				TLayyDsrSscl dsrSscl = dsrSsclMap.get(plaintiff.getCId());
				if (null != dsrSscl) {
					checkedMap.put(plaintiff.getCId(), plaintiff);
				}
			}

		} else {
			dsrList.addAll(NrcEditData.getDefendantList());
			for (TLayyDsr defendant : NrcEditData.getDefendantList()) {
				TLayyDsrSscl dsrSscl = dsrSsclMap.get(defendant.getCId());
				if (null != dsrSscl) {
					checkedMap.put(defendant.getCId(), defendant);
				}
			}
		}

		if (null == relLitigantAdapter) {
			relLitigantAdapter = new NrcRelLitigantAdapter(this, dsrList, checkedMap);
			relLitigantAdapter.setSelectAllTV(selectAllTV);
			litigantListView.setAdapter(relLitigantAdapter);
		} else {
			relLitigantAdapter.notifyDataSetChanged();
		}

		if (checkedMap.size() < dsrList.size()) {
			String selectAll = getResources().getString(R.string.nrc_select_all);
			selectAllTV.setText(selectAll);
		} else {
			String selectAllCancel = getResources().getString(R.string.nrc_select_all_cancel);
			selectAllTV.setText(selectAllCancel);
		}
	}
}
