package com.thunisoft.sswy.mobile.adapter.nrc;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.ViewById;

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
import com.thunisoft.sswy.mobile.activity.nrc.NrcAddEvidenceActivity;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DeleteLogic.DelSsclFjResponseListener;
import com.thunisoft.sswy.mobile.logic.DownloadLogic;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.pojo.TZj;
import com.thunisoft.sswy.mobile.pojo.TZjcl;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 网上立案 证据_材料 adapter
 * 
 * @author gewx
 *
 */
public class NrcEvidenceFjAdapter extends BaseAdapter implements DelSsclFjResponseListener{

	private Activity activity;

	private List<TZjcl> zjclList;
	
	private TZj zj;

	/** 是否显示删除按钮 */
	private boolean isDel;
	
	Map<String, Boolean> imgSuffixMap = FileUtils.getImgSuffixMap();
	
	private DownloadLogic downloadLogic;
	
	private DeleteLogic deleteLogic;
	
	private WaittingDialog deleteDialog;

	public NrcEvidenceFjAdapter(Activity activity, List<TZjcl> zjclList) {
		this.activity = activity;
		this.zjclList = zjclList;
	}

	@Override
	public int getCount() {
		if (null == zjclList || zjclList.size() == 0) {
			return 1;
		} else {
			return zjclList.size() + 2;
		}
	}

	@Override
	public TZjcl getItem(int position) {
		return zjclList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

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
			TZjcl zjcl = getItem(position);
			String suffix = FileUtils.getFileSuffix(zjcl.getCOriginName());
			if (null != imgSuffixMap.get(suffix.toLowerCase())) {//扩展名为符合格式的图片
				File imgFile = new File(zjcl.getCPath());
				if (imgFile.exists()) {
					imgItemHiddenView(viewHolder);
					viewHolder.fileRL.setBackgroundDrawable(FileUtils.getThumbnailDrawable(activity, zjcl.getCPath()));
				} else {
					viewHolder.fileNameTV.setText(imgFile.getName());
					viewHolder.fileRL.setBackgroundResource(R.drawable.file_item_bg);
				}
				ItemOnClickListener itemOnClickListener = new ItemOnClickListener();
				itemOnClickListener.position = position;
				viewHolder.fileRL.setOnClickListener(itemOnClickListener);
			} else {
				viewHolder.fileRL.setBackgroundResource(R.drawable.file_item_bg);
				viewHolder.suffixBgFL.setVisibility(View.VISIBLE);
				viewHolder.fileNameTV.setVisibility(View.GONE);
				viewHolder.suffixNameTV.setText(suffix.toUpperCase());
				viewHolder.fileNameTV.setText(zjcl.getCOriginName());
				ItemOnClickListener itemOnClickListener = new ItemOnClickListener();
				itemOnClickListener.position = position;
				viewHolder.fileRL.setOnClickListener(itemOnClickListener);
			}
			viewHolder.fileNameTV.setVisibility(View.GONE);
			if (isDel) {
				DelIndictmentOnClick delIndictmentOnClick = new DelIndictmentOnClick();
				delIndictmentOnClick.position = position;
				viewHolder.delFileBtn.setVisibility(View.VISIBLE);
				viewHolder.delFileBtn.setOnClickListener(delIndictmentOnClick);
			} else {
				viewHolder.delFileBtn.setVisibility(View.GONE);
				viewHolder.delFileBtn.setOnClickListener(null);
			}
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
		if (position == zjclList.size()) {
			isAddBtn = true;
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
		if (position == zjclList.size() + 1) {
			isDelBtn = true;
		}
		return isDelBtn;
	}

	/**
	 * 添加和修改按钮需要隐藏的控件
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

	private static class ViewHolder {

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

	class ItemAddFileOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setClass(activity, AddPhotoDialogActivity_.class);
			Map<String, Object> selectedPathMap = new LinkedHashMap<String, Object>(); // 使用有序的Map
			SerializableSOMap selectedPathSMap = new SerializableSOMap();
			selectedPathSMap.setMap(selectedPathMap);
			for (TZjcl zjcl : zjclList) {
				selectedPathMap.put(zjcl.getCPath(), zjcl.getCOriginName());
			}
			intent.putExtra(AddPhotoDialogActivity.K_SELECT_TYPE, NrcConstants.SELECT_TYPE_MULTI);
			intent.putExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP, selectedPathSMap);
			activity.startActivityForResult(intent, NrcAddEvidenceActivity.REQ_CODE_SELECT_FILE);
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
			NrcEvidenceFjAdapter.this.notifyDataSetChanged();
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
			NrcEvidenceFjAdapter.this.isDel = true;
			TZjcl zjcl = zjclList.get(position);
			if (null == zjcl.getNSync() || NrcConstants.SYNC_TRUE == zjcl.getNSync()) {
				deleteDialog = new WaittingDialog(activity, R.style.CustomDialogStyle, "正在删除...");
				deleteDialog.setIsCanclable(false);
				deleteDialog.show();
				deleteLogic.setDeleteResultListener(NrcEvidenceFjAdapter.this);
				deleteLogic.deleteZj(zj, zjcl);
			} else {
				deleteLocalZjcl(position);
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
				int deletePos = -1;
				if (null != zjclList && zjclList.size() > 0) {
					for (int i=0; i<zjclList.size(); i++) {
						TZjcl zjcl = zjclList.get(i);
						if (zjcl.getCId().equals(ssclFjId)) {
							deletePos = i;
							break;
						}
					}
				}
				if (deletePos >= 0) {
					deleteLocalZjcl(deletePos);
				}
			} else {
				Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(activity, response.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void deleteLocalZjcl(int position) {
		TZjcl zjcl = zjclList.get(position);
		ArrayList<TZjcl> localZjclList = NrcEditData.getEvidenceMaterial().get(zjcl.getCZjBh());
		if (null != localZjclList && localZjclList.size() > position) {
			localZjclList.remove(position);
		}
		zjclList.remove(position);
		if (zjclList.size() == 0) {
			NrcEvidenceFjAdapter.this.isDel = false;
		}
		NrcEvidenceFjAdapter.this.notifyDataSetChanged();
	}

	private class ItemOnClickListener implements OnClickListener {

		public int position;

		@Override
		public void onClick(View v) {
			TZjcl zjcl = zjclList.get(position);
			if (StringUtils.isNotBlank(zjcl.getCPath())) {
				File organFile = new File(zjcl.getCPath());
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
					downloadLogic.downloadZj(zj, zjcl);
				}
			} else {
				downloadLogic.downloadZj(zj, zjcl);
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
	
	/**  
	 * 设置  downloadLogic  
	 * @param downloadLogic
	 */
	public void setDownloadLogic(DownloadLogic downloadLogic) {
		this.downloadLogic = downloadLogic;
	}

	
	/**  
	 * 设置  deleteLogic  
	 * @param deleteLogic
	 */
	public void setDeleteLogic(DeleteLogic deleteLogic) {
		this.deleteLogic = deleteLogic;
	}

	/**  
	 * 设置  zj  
	 * @param zj
	 */
	public void setZj(TZj zj) {
		this.zj = zj;
	}
}
