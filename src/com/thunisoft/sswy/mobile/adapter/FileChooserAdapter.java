package com.thunisoft.sswy.mobile.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.FileChooserActivity;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;


public class FileChooserAdapter extends BaseAdapter {
	
	private FileChooserActivity fileChooserActivity;

	/**
	 * 当前目录下的符合条件的文件
	 */
	private ArrayList<FileInfo> mFileLists;
	
	/**
	 * 之前已选中的文件路径Map
	 */
	private Map<String, Object> selectedPathMap;
	
	/**
	 * 选择类型：多选、单选
	 */
	private int selectType = NrcConstants.SELECT_TYPE_SINGLE;
	
	private LayoutInflater mLayoutInflater = null;

	public FileChooserAdapter(FileChooserActivity fileChooserActivity, ArrayList<FileInfo> fileLists) {
		super();
		this.fileChooserActivity = fileChooserActivity;
		mFileLists = fileLists;
		this.selectedPathMap = fileChooserActivity.getSelectedPathMap();
		mLayoutInflater = (LayoutInflater) fileChooserActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mFileLists.size();
	}

	@Override
	public FileInfo getItem(int position) {
		return mFileLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = mLayoutInflater.inflate(R.layout.activity_file_chooser_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		
		FileInfo fileInfo = getItem(position);
		
		holder.checkCB.setVisibility(View.VISIBLE);
		String fileName = (String)selectedPathMap.get(fileInfo.getFilePath());
		if (StringUtils.isNotBlank(fileName)) {
			holder.checkCB.setChecked(true);
		} else {
			holder.checkCB.setChecked(false);
		}
		CheckOnClickListener checkOnClickListener = new CheckOnClickListener();
		checkOnClickListener.position = position;
		holder.checkCB.setOnClickListener(checkOnClickListener);
		holder.fileNameTV.setText(fileInfo.getFileName());
		if(fileInfo.isDirectory()){      //文件夹
			holder.fileSuffixBgFL.setBackgroundDrawable(fileChooserActivity.getResources().getDrawable(R.drawable.file_chooser_item_folder));
			holder.fileSuffixTV.setText("");
			holder.checkCB.setVisibility(View.GONE);
		} else {
			String suffix = FileUtils.getFileSuffix(fileInfo.getFilePath());
			holder.fileSuffixTV.setText(suffix.toUpperCase());
			holder.fileSuffixBgFL.setBackgroundDrawable(fileChooserActivity.getResources().getDrawable(R.drawable.file_item_suffix_bg));
		}
		return view;
	}

	static class ViewHolder {
		FrameLayout fileSuffixBgFL;
		TextView fileSuffixTV;
		TextView fileNameTV;
		CheckBox checkCB;

		public ViewHolder(View view) {
			fileSuffixBgFL = (FrameLayout) view.findViewById(R.id.file_chooser_item_suffix_bg);
			fileSuffixTV = (TextView) view.findViewById(R.id.file_chooser_item_suffix);
			fileNameTV = (TextView) view.findViewById(R.id.file_chooser_item_name);
			checkCB = (CheckBox) view.findViewById(R.id.file_chooser_item_check);
		}
	}

	
	enum FileType {
		FILE , DIRECTORY;
	}

	// =========================
	// Model
	// =========================
	public static class FileInfo {
		private FileType fileType;
		private String fileName;
		private String filePath;

		public FileInfo(String filePath, String fileName, boolean isDirectory) {
			this.filePath = filePath;
			this.fileName = fileName;
			fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
		}

		public boolean isDirectory(){
			if(fileType == FileType.DIRECTORY)
				return true ;
			else
				return false ;
		}
		
		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

	}

	/**
	 * 选中点击事件
	 * @author gewx
	 *
	 */
	private class CheckOnClickListener implements OnClickListener {

		public int position;
		
		@Override
		public void onClick(View v) {
			FileInfo fileInfo = getItem(position);
			if (null != selectedPathMap.get(fileInfo.getFilePath())) {
				selectedPathMap.remove(fileInfo.getFilePath());
			} else {
				if (FileUtils.fileLengthIsOk(fileInfo.getFilePath())) {
					if (NrcConstants.SELECT_TYPE_SINGLE == selectType) {
						selectedPathMap.clear();
						selectedPathMap.put(fileInfo.getFilePath(), fileInfo.getFileName());
						FileChooserAdapter.this.notifyDataSetChanged();
					} else {
						selectedPathMap.put(fileInfo.getFilePath(), fileInfo.getFileName());
					}
				} else {
					long fileSize = FileUtils.FILE_MAX_LENGTH/1024/1024;
					Toast.makeText(fileChooserActivity, "文件大小不能超过" + fileSize + "M", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**  
	 * 获取  选择类型：多选、单选  
	 * @return selectType  
	 */
	
	public int getSelectType() {
		return selectType;
	}

	/**  
	 * 设置  选择类型：多选、单选  
	 * @param selectType
	 */
	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}
}
