package com.thunisoft.sswy.mobile.adapter.nrc;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity_;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddAgentCertActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddOtherAgentCertActivity;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DeleteLogic.DelSsclFjResponseListener;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.pojo.TLayySscl;
import com.thunisoft.sswy.mobile.pojo.TLayySsclFj;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 诉讼材料附件 adapter
 * 
 * @author gewx
 *
 */
public class NrcSsclFjAdapter extends BaseAdapter implements DelSsclFjResponseListener {

	/*********** 需要初始化的参数 ******************/
	private Activity activity;

	/** 是否显示删除按钮 */
	private boolean isDel = false;

	/** 诉讼材料附件 List */
	private List<TLayySsclFj> ssclFjList;

	private TLayySscl sscl;

	private int selectType = NrcConstants.SELECT_TYPE_MULTI;

	/**
	 * 诉讼材料附件类型：1：证件类，2：起诉状
	 */
	private int ssclFjType = NrcConstants.SSCL_TYPE_INDICTMENT;

	/**
	 * 附件类型：多个附件，单个附件
	 */
	private int fjType;

	/**
	 * 如果是证件，所属人类型为：代理人，需要传入授权委托书的position
	 */
	private int licensePos = -1;

	/**
	 * activity for result request code，哪个页面调用的，需要返回到相应页面的onActivityResult
	 * 中的request
	 */
	private int requestCode;

	private DownloadLogic downloadLogic;

	private DeleteLogic deleteLogic;

	private Map<String, ArrayList<TLayySsclFj>> ssclFjListMap;
	/*********** 需要初始化的参数 ******************/

	/** 单个文件 */
	public static final int FJ_TYPE_SINGLE = 1;

	/** 多个文件 */
	public static final int FJ_TYPE_MULTIPLE = 2;

	/** 授权委托书 */
	public static final int FJ_TYPE_LICENSE = 3;

	Map<String, Boolean> imgSuffixMap = FileUtils.getImgSuffixMap();

	/**
	 * 删除提示框
	 */
	private WaittingDialog deleteDialog;

	/**
	 * 诉讼材料附件构造器
	 * 
	 * @param activity
	 *            当前页面activity
	 * @param ssclFjList
	 *            诉讼材料附件List
	 * @param ssclFjType
	 *            诉讼材料类型，如果类型为1，证件，需要传入证件所属人类型，不传默认为代理人
	 */
	public NrcSsclFjAdapter(Activity activity, TLayySscl sscl, List<TLayySsclFj> ssclFjList) {
		this.activity = activity;
		this.sscl = sscl;
		this.ssclFjList = ssclFjList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null == ssclFjList || ssclFjList.size() == 0) {
			count = 1;
		} else {
			switch (fjType) {
			case FJ_TYPE_MULTIPLE:
				count = ssclFjList.size() + 2;
				break;

			case FJ_TYPE_SINGLE:
				count = ssclFjList.size() + 1;
				break;

			case FJ_TYPE_LICENSE:
				count = ssclFjList.size() + 1;
				break;

			default:
				count = 1;
				break;
			}
		}
		return count;
	}

	@Override
	public TLayySsclFj getItem(int position) {
		return ssclFjList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_nrc_file_item, null);
			viewHolder.fileRL = (RelativeLayout) convertView.findViewById(R.id.nrc_file_item_file);
			viewHolder.suffixBgFL = (FrameLayout) convertView.findViewById(R.id.nrc_file_item_suffix_bg);
			viewHolder.suffixNameTV = (TextView) convertView.findViewById(R.id.nrc_file_item_suffix_name);
			viewHolder.fileNameTV = (TextView) convertView.findViewById(R.id.nrc_file_item_name);
			viewHolder.delFileBtn = (Button) convertView.findViewById(R.id.nrc_file_item_del);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (isAddBtn(position)) { // 显示添加按钮
			viewHolder.fileRL.setBackgroundResource(R.drawable.nrc_file_item_add);
			ItemAddFileOnClick addFileOnClick = new ItemAddFileOnClick();
			viewHolder.fileRL.setOnClickListener(addFileOnClick);
			operateItemHiddenView(viewHolder);
		} else if (isDelBtn(position)) { // 显示删除按钮
			ItemDelFileOnClick delFileOnClick = new ItemDelFileOnClick();
			viewHolder.fileRL.setBackgroundResource(R.drawable.nrc_file_item_del);
			viewHolder.fileRL.setOnClickListener(delFileOnClick);
			operateItemHiddenView(viewHolder);
		} else {
			TLayySsclFj ssclFj = ssclFjList.get(position);
			String suffix = FileUtils.getFileSuffix(ssclFj.getCOriginName());
			if (null != imgSuffixMap.get(suffix.toLowerCase())) {// 扩展名为符合格式的图片
				imgItemHiddenView(viewHolder);
				File imgFile = new File(ssclFj.getCPath());
				if (imgFile.exists()) {
					viewHolder.fileRL.setBackgroundDrawable(FileUtils.getThumbnailDrawable(activity, ssclFj.getCPath()));
				} else {
					viewHolder.fileRL.setBackgroundResource(R.drawable.file_item_bg);
					viewHolder.suffixBgFL.setVisibility(View.VISIBLE);
					viewHolder.fileNameTV.setVisibility(View.GONE);
					viewHolder.suffixNameTV.setText(suffix.toUpperCase());
					viewHolder.fileNameTV.setText(ssclFj.getCOriginName());
				}
			} else {
				viewHolder.fileRL.setBackgroundResource(R.drawable.file_item_bg);
				viewHolder.suffixBgFL.setVisibility(View.VISIBLE);
				viewHolder.fileNameTV.setVisibility(View.GONE);
				viewHolder.suffixNameTV.setText(suffix.toUpperCase());
				viewHolder.fileNameTV.setText(ssclFj.getCOriginName());
			}

			if (isDel) {
				DelIndictmentOnClick delIndictmentOnClick = new DelIndictmentOnClick();
				delIndictmentOnClick.position = position;
				viewHolder.delFileBtn.setVisibility(View.VISIBLE);
				viewHolder.delFileBtn.setOnClickListener(delIndictmentOnClick);
			} else {
				viewHolder.delFileBtn.setVisibility(View.GONE);
				viewHolder.delFileBtn.setOnClickListener(null);
			}
			ItemOnClickListener itemOnClickListener = new ItemOnClickListener();
			itemOnClickListener.position = position;
			viewHolder.fileRL.setOnClickListener(itemOnClickListener);
		}
		return convertView;
	}

	/**
	 * 当前item是显示添加按钮
	 * 
	 * @param position
	 * @return
	 */
	private boolean isAddBtn(int position) {
		boolean isAddBtn = false;
		if (FJ_TYPE_SINGLE == fjType || FJ_TYPE_LICENSE == fjType) {
			if (null == ssclFjList || ssclFjList.size() == 0) {
				isAddBtn = true;
			}
		} else {
			if (position == ssclFjList.size()) {
				isAddBtn = true;
			}
		}
		return isAddBtn;
	}

	/**
	 * 当前item是显示删除按钮
	 * 
	 * @param position
	 * @return
	 */
	private boolean isDelBtn(int position) {
		boolean isDelBtn = false;
		if (FJ_TYPE_SINGLE == fjType || FJ_TYPE_LICENSE == fjType) {
			if (position == ssclFjList.size()) {
				isDelBtn = true;
			}
		} else if (position == ssclFjList.size() + 1) {
			isDelBtn = true;
		}
		return isDelBtn;
	}

	/**
	 * 添加和修改按钮需要隐藏的控件
	 * 
	 * @param viewHolder
	 */
	private void operateItemHiddenView(ViewHolder viewHolder) {
		viewHolder.delFileBtn.setVisibility(View.GONE);
		viewHolder.delFileBtn.setOnClickListener(null);
		viewHolder.fileNameTV.setVisibility(View.GONE);
		viewHolder.suffixBgFL.setVisibility(View.GONE);
	}

	/**
	 * 图片文件需要隐藏的控件
	 * 
	 * @param viewHolder
	 */
	private void imgItemHiddenView(ViewHolder viewHolder) {
		viewHolder.fileNameTV.setVisibility(View.GONE);
		viewHolder.suffixBgFL.setVisibility(View.GONE);
	}

	/**
	 * 获取 是否显示删除按钮
	 * 
	 * @return isDel
	 */

	public boolean isDel() {
		return isDel;
	}

	/**
	 * 设置 是否显示删除按钮
	 * 
	 * @param isDel
	 */
	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}

	/**
	 * 列表中 添加起诉状 点击时事件
	 * 
	 * @author gewx
	 *
	 */
	private class ItemAddFileOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			updateAgentLiscensePos();
			Intent intent = new Intent();
			intent.setClass(activity, AddPhotoDialogActivity_.class);
			Map<String, Object> selectedPathMap = new LinkedHashMap<String, Object>(); // 使用有序的Map
			SerializableSOMap selectedPathSMap = new SerializableSOMap();
			selectedPathSMap.setMap(selectedPathMap);
			if (null != ssclFjList && ssclFjList.size() > 0) {
				for (TLayySsclFj ssclFj : ssclFjList) {
					selectedPathMap.put(ssclFj.getCPath(), ssclFj.getCOriginName());
				}
			}
			intent.putExtra(AddPhotoDialogActivity.K_SELECT_TYPE, selectType);
			intent.putExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP, selectedPathSMap);
			activity.startActivityForResult(intent, requestCode);
		}
	}

	/**
	 * 如果是代理人证件需要更新 当前编辑的是第几个授权委托书
	 */
	private void updateAgentLiscensePos() {
		if (NrcConstants.SSCL_TYPE_CERTIFICATE == ssclFjType) {
			if (FJ_TYPE_LICENSE == fjType) {
				if (activity instanceof NrcAddAgentCertActivity) {
					NrcAddAgentCertActivity addAgentCertActivity = (NrcAddAgentCertActivity) activity;
					addAgentCertActivity.setLicensePos(licensePos);
				} else if (activity instanceof NrcAddOtherAgentCertActivity) {
					NrcAddOtherAgentCertActivity addAgentOtherCertActivity = (NrcAddOtherAgentCertActivity) activity;
					addAgentOtherCertActivity.setLicensePos(licensePos);
				}
			}
		}
	}

	/**
	 * 列表中 删除起诉状 点击时事件
	 * 
	 * @author gewx
	 *
	 */
	private class ItemDelFileOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (isDel) {
				isDel = false;
			} else {
				isDel = true;
			}
			NrcSsclFjAdapter.this.notifyDataSetChanged();
		}
	}

	/**
	 * 删除起诉状 点击时事件
	 * 
	 * @author gewx
	 *
	 */
	private class DelIndictmentOnClick implements OnClickListener {

		public int position;

		@Override
		public void onClick(View v) { // 删除同修改操作不同，点击删除之后再点击页面左上角的取消，也取消不掉了
			NrcSsclFjAdapter.this.isDel = true;
			TLayySsclFj ssclFj = ssclFjList.get(position);
			if (null == ssclFj.getNSync() || NrcConstants.SYNC_TRUE == ssclFj.getNSync()) {
				deleteDialog = new WaittingDialog(activity, R.style.CustomDialogStyle, "正在删除...");
				deleteDialog.setIsCanclable(false);
				deleteDialog.show();
				deleteLogic.setDeleteResultListener(NrcSsclFjAdapter.this);
				deleteLogic.deleteSsclFj(sscl, ssclFj);
			} else {
				deleteLocalSsclFj(position);
			}
		}
	}

	@Override
	public void deleteResult(BaseResponse response, String ssclFjId) {
		if (null != deleteDialog) {
			deleteDialog.dismiss();
			deleteDialog = null;
		}
		if (response.isXtcw()) {
			if (response.isSuccess()) {
				int position = -1;
				for (int i = 0; i < ssclFjList.size(); i++) {
					TLayySsclFj ssclFj = ssclFjList.get(i);
					if (ssclFj.getCId().equals(ssclFjId)) {
						position = i;
						break;
					}
				}
				if (position >= 0) {
					deleteLocalSsclFj(position);
				}
			} else {
				Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void deleteLocalSsclFj(int position) {
		TLayySsclFj ssclFj = ssclFjList.get(position);
		ArrayList<TLayySsclFj> localSsclFjList = null;
		if (NrcConstants.SSCL_TYPE_CERTIFICATE == sscl.getNType()) {
			localSsclFjList = NrcEditData.getCertificateFjListMap().get(sscl.getCId());
		} else if (NrcConstants.SSCL_TYPE_INDICTMENT == sscl.getNType()) {
			localSsclFjList = NrcEditData.getIndictmentFjListMap().get(sscl.getCId());
		}
		if (null != localSsclFjList && localSsclFjList.size() > position) {
			localSsclFjList.remove(position);
		}
		if (null != ssclFjListMap) {
			ssclFjListMap.remove(ssclFj.getCSsclId());
		}
		ssclFjList.remove(position);
		if (ssclFjList.size() == 0) {
			NrcSsclFjAdapter.this.isDel = false;
		}
		NrcSsclFjAdapter.this.notifyDataSetChanged();
	}

	/**
	 * 点击item
	 * 
	 * @author gewx
	 *
	 */
	private class ItemOnClickListener implements OnClickListener {

		public int position;

		@Override
		public void onClick(View v) {
			TLayySsclFj ssclFj = ssclFjList.get(position);
			if (StringUtils.isNotBlank(ssclFj.getCPath())) {
				File organFile = new File(ssclFj.getCPath());
				if (organFile.exists()) {
					String backupPath = FileUtils.getBackupFilePath(activity, organFile.getAbsolutePath());
					File backupFile = new File(backupPath);
					if (backupFile.exists()) {
						openFile(backupPath);
					} else {
						boolean success = FileUtils.copyfile(organFile, backupFile);
						if (success) {
							openFile(backupPath);
						} else {
							organFile.delete();
							Toast.makeText(activity, "打开文件失败，请重试", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					downloadLogic.downloadSscl(ssclFj, ssclFjType);
				}
			} else {
				downloadLogic.downloadSscl(ssclFj, ssclFjType);
			}
		}
	}

	private void openFile(String path) {
		Intent intent = ResponseUtilExtr.openFileWithAllPath(path);
		try {
			activity.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(activity, "打开文件失败，请重试", Toast.LENGTH_SHORT).show();
		}
	}

	static class ViewHolder {

		/**
		 * 如果是图片文件，需要把图片设置为背景色，用于显示
		 */
		RelativeLayout fileRL;

		/**
		 * 文件扩展名 背景
		 */
		FrameLayout suffixBgFL;

		/**
		 * 文件扩展名
		 */
		TextView suffixNameTV;

		/**
		 * 删除文件 按钮
		 */
		Button delFileBtn;

		/**
		 * 文件名
		 */
		TextView fileNameTV;

	}

	/**
	 * 设置 授权委托书的position
	 * 
	 * @param licensePos
	 */
	public void setLicensePos(int licensePos) {
		this.licensePos = licensePos;
	}

	/**
	 * 设置 downloadLogic
	 * 
	 * @param downloadLogic
	 */
	public void setDownloadLogic(DownloadLogic downloadLogic) {
		this.downloadLogic = downloadLogic;
	}

	/**
	 * 设置 deleteLogic
	 * 
	 * @param deleteLogic
	 */
	public void setDeleteLogic(DeleteLogic deleteLogic) {
		this.deleteLogic = deleteLogic;
	}

	/**
	 * 设置 requestCode
	 * 
	 * @param requestCode
	 */
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	/**
	 * 设置 ssclFjType
	 * 
	 * @param ssclFjType
	 */
	public void setSsclFjType(int ssclFjType) {
		this.ssclFjType = ssclFjType;
	}

	/**
	 * 设置 fjType
	 * 
	 * @param fjType
	 */
	public void setFjType(int fjType) {
		this.fjType = fjType;
	}

	/**
	 * 设置 ssclFjListMap
	 * 
	 * @param ssclFjListMap
	 */
	public void setSsclFjListMap(Map<String, ArrayList<TLayySsclFj>> ssclFjListMap) {
		this.ssclFjListMap = ssclFjListMap;
	}

	/**
	 * 设置 selectType
	 * 
	 * @param selectType
	 */
	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}
}
