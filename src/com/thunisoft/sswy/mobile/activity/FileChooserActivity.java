package com.thunisoft.sswy.mobile.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.adapter.FileChooserAdapter;
import com.thunisoft.sswy.mobile.adapter.FileChooserAdapter.FileInfo;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;

/**
 * 选择文件activity
 * @author gewx
 *
 */
public class FileChooserActivity extends BaseActivity {
	
	/**文件列表*/
	private GridView fileGridView;
	
	/**返回上一级*/
	private Button historyBackBtn;
	
	/**当前目录*/
	private TextView pathTV;
	
	/**
	 * 返回图标
	 */
	private ImageView backIV;
	
	/**
	 * 确定
	 */
	private TextView sureTV;
	
	/**sdcard 根路径*/
	private String mSdcardRootPath;
	
	/**当前显示的路径*/
	private String mLastFilePath;
	
	/**当前目录下的所有文件*/
	private ArrayList<FileInfo> fileList;
	
	/**文件列表 adapter*/
	private FileChooserAdapter fileAdapter;
	
	/**
	 * 选择类型：多选、单选 intent key
	 */
    public static final String K_SELECT_TYPE = "selectType";

	/**
	 * 选择类型：多选、单选
	 */
	private int selectType = NrcConstants.SELECT_TYPE_SINGLE;
	
	/**
	 * 选中的文件路径Map intent key
	 */
	public static final String K_SELECTED_PATH_MAP = "selectedPathMap";
	
	/**选中的文件路径 Map*/
	private Map<String, Object> selectedPathMap;
	
	/**可以选择的文件类型  intent key*/
	public static final String K_FILE_TYPE = "fileType";
	
	private int fileType = NrcConstants.FILE_TYPE_ALL;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_file_chooser);
        
        Intent intent = getIntent();
        selectType = intent.getIntExtra(K_SELECT_TYPE, NrcConstants.SELECT_TYPE_SINGLE);
        fileType = intent.getIntExtra(K_FILE_TYPE, NrcConstants.FILE_TYPE_ALL);
        SerializableSOMap soMap = (SerializableSOMap)intent.getSerializableExtra(K_SELECTED_PATH_MAP);
        selectedPathMap = soMap.getMap();
		mSdcardRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

		historyBackBtn = (Button)findViewById(R.id.file_chooser_history_back);
		historyBackBtn.setOnClickListener(mClickListener);
		
		pathTV = (TextView)findViewById(R.id.file_chooser_path);
		
		fileGridView = (GridView)findViewById(R.id.file_chooser_list);
		fileGridView.setEmptyView(findViewById(R.id.file_chooser_empty_hint));
		fileGridView.setOnItemClickListener(mItemClickListener);
		setGridViewAdapter(mSdcardRootPath);
		
		backIV = (ImageView)findViewById(R.id.file_chooser_back);
		sureTV = (TextView)findViewById(R.id.file_chooser_sure);
		backIV.setOnClickListener(new BackOnClickListener());
		sureTV.setOnClickListener(new SelectOnClickListener());
	}
	
	/**
	 * 配置适配器
	 * @param filePath
	 */
	private void setGridViewAdapter(String filePath) {
		updateFileItems(filePath);
		fileAdapter = new FileChooserAdapter(this , fileList);
		fileAdapter.setSelectType(selectType);
		fileGridView.setAdapter(fileAdapter);
	}
	
	/**
	 * 根据路径更新数据，并且通知Adatper数据改变
	 * @param filePath
	 */
	private void updateFileItems(String filePath) {
		mLastFilePath = filePath ;
		pathTV.setText(mLastFilePath);
		
		if(fileList == null) {
			fileList = new ArrayList<FileInfo>() ;
		} else {
			fileList.clear() ;
		}
		
		File[] files = folderScan(filePath);
		if(files == null)  {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			if(files[i].isHidden()) {  // 不显示隐藏文件
				continue ;
			}
			
			String fileAbsolutePath = files[i].getAbsolutePath() ;
			String fileName = files[i].getName();
		    boolean isDirectory = false ;
			if (files[i].isDirectory()){
				isDirectory = true ;
			}
		    FileInfo fileInfo = new FileInfo(fileAbsolutePath , fileName , isDirectory) ;
			fileList.add(fileInfo);
		}
		//When first enter , the object of mAdatper don't initialized
		if(fileAdapter != null) {
			fileAdapter.notifyDataSetChanged();  //重新刷新
		}
	}
	
	/**
	 * 获得当前路径的所有文件
	 * @param path
	 * @return
	 */
	private File[] folderScan(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		return FileUtils.filterFiles(files, fileType);
	}
	
	private View.OnClickListener mClickListener = new  OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.file_chooser_history_back:
				backProcess();
				break;
			default :
			    	break ;
			}
		}
	};
	
	private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			FileInfo fileInfo = (FileInfo)(((FileChooserAdapter)adapterView.getAdapter()).getItem(position));
			if(fileInfo.isDirectory()) {   //点击项为文件夹, 显示该文件夹下所有文件
				updateFileItems(fileInfo.getFilePath());
			} else {   //其他文件.....
				toast(getText(R.string.open_file_error_format));
			}
		}
	};
    
	public boolean onKeyDown(int keyCode , KeyEvent event){
		if(event.getAction() == KeyEvent.ACTION_DOWN 
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			backProcess();   
			return true ;
		}
		return super.onKeyDown(keyCode, event);
	}
	//返回上一层目录的操作
	public void backProcess(){
		//判断当前路径是不是sdcard路径 ， 如果不是，则返回到上一层。
		if (!mLastFilePath.equals(mSdcardRootPath)) {  
			File thisFile = new File(mLastFilePath);
			String parentFilePath = thisFile.getParent();
			updateFileItems(parentFilePath);
		} else {   //是sdcard路径 ，直接结束
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	private void toast(CharSequence hint){
	    Toast.makeText(this, hint , Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 选定点击时事件
	 * @author gewx
	 *
	 */
	private class SelectOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (selectedPathMap.size() <= 0) {
				Toast.makeText(FileChooserActivity.this, "请选择文件" , Toast.LENGTH_SHORT).show();
				return;
			}
			SerializableSOMap soMap = new SerializableSOMap();
			soMap.setMap(selectedPathMap);
			Intent intent = new Intent();
			intent.putExtra(K_SELECTED_PATH_MAP, soMap);
			FileChooserActivity.this.setResult(Activity.RESULT_OK, intent);
			FileChooserActivity.this.finish();
		}
	}
	
	/**
	 * 返回点击时事件
	 * @author gewx
	 *
	 */
	private class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			FileChooserActivity.this.finish();
		}
	}

	/**  
	 * 获取  选中的文件路径 Map  
	 * @return selectedPathMap  
	 */
	public Map<String, Object> getSelectedPathMap() {
		return selectedPathMap;
	}

	/**  
	 * 设置  选中的文件路径 Map  
	 * @param selectedPathMap
	 */
	public void setSelectedPathMap(Map<String, Object> selectedPathMap) {
		this.selectedPathMap = selectedPathMap;
	}
}