package com.thunisoft.sswy.mobile.activity.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.photoselector.activity.PhotoSelectorActivity;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.FileChooserActivity;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.util.PhoneStateUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;

/**
 * 添加图片
 * @author gewx
 *
 */
@EActivity(R.layout.dialog_add_photo)
public class AddPhotoDialogActivity extends Activity {

	/**
	 * 拍照的文件路径
	 */
	private String filePath;
	
	/**
	 * 已选择文件路径Map intent key
	 */
	public static final String K_SELECTED_PATH_MAP = "selectedPathMap";
	
	/**
	 * 已选中的文件路径Map
	 */
	private Map<String, Object> selectedPathMap;
	
	/**
	 * 选择类型：单选、多选   intent key
	 */
	public static final String K_SELECT_TYPE = "selectType";
	
	/**
	 * 选择类型：单选、多选
	 */
	private int selectType = NrcConstants.SELECT_TYPE_SINGLE;
	
	/**
	 * 文件类型：全部、图片
	 */
	public static final String K_FILE_TYPE = "fileType";
	
	/**
	 * 可选择的文件类型，默认为全部
	 */
	private int fileType = NrcConstants.FILE_TYPE_ALL;
	
	/**
     * 选择文件
     */
    public static final int REQ_CODE_SELECT_FILE = 1;
    
    /**
     * 相册
     */
    public static final int REQ_CODE_ALBUM = 2;
    
    /**
     * 拍照
     */
    public static final int REQ_CODE_TAKE_PIC = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFinishOnTouchOutside(true);// 设置为true点击区域外消失   
		Intent intent = getIntent();
		selectType = intent.getIntExtra(K_SELECT_TYPE, NrcConstants.SELECT_TYPE_SINGLE);
		fileType = intent.getIntExtra(K_FILE_TYPE, NrcConstants.FILE_TYPE_ALL);
		SerializableSOMap soMap = (SerializableSOMap)intent.getSerializableExtra(K_SELECTED_PATH_MAP);
		if (null != soMap) {
			selectedPathMap = soMap.getMap();
		}
		if (null == selectedPathMap) {
			selectedPathMap = new LinkedHashMap<String, Object>();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (RESULT_OK != resultCode) {
			return;
		}
		SerializableSOMap soMap = new SerializableSOMap();
		switch (requestCode) {
		case REQ_CODE_SELECT_FILE:
			soMap = (SerializableSOMap)data.getSerializableExtra(FileChooserActivity.K_SELECTED_PATH_MAP);
			break;
			
		case REQ_CODE_ALBUM:
			@SuppressWarnings("unchecked")
			ArrayList<String> selectedPathList = (ArrayList<String>)data.getSerializableExtra(PhotoSelectorActivity.K_SELECTED_PATH_LIST);
			selectedPathMap.clear();
			if (null != selectedPathList && selectedPathList.size() > 0) {
				for (String path : selectedPathList) {
					File imgFile = new File(path);
					selectedPathMap.put(path, imgFile.getName());
				}
				soMap.setMap(selectedPathMap);
			}
			break;
        
		case REQ_CODE_TAKE_PIC:
			File imgFile = new File(filePath);
			selectedPathMap.put(filePath, imgFile.getName());
			soMap.setMap(selectedPathMap);
			break;
			
		default:
			break;
		}
		
		Intent intent = new Intent();
		intent.putExtra(K_SELECTED_PATH_MAP, soMap);
		setResult(Constants.RESULT_OK, intent);
		this.finish();
	}
	
	/**
	 * 点击本地文件
	 */
	@Click(R.id.dialog_add_photo_local_file)
	protected void clickLocalFile() {
		Intent intent = new Intent(this ,FileChooserActivity.class);
		intent.putExtra(FileChooserActivity.K_SELECT_TYPE, selectType);
		intent.putExtra(FileChooserActivity.K_FILE_TYPE, fileType);
		SerializableSOMap soMap = new SerializableSOMap();
		soMap.setMap(selectedPathMap);
		intent.putExtra(FileChooserActivity.K_SELECTED_PATH_MAP, soMap);
		startActivityForResult(intent , REQ_CODE_SELECT_FILE);
	}
	
	/**
	 * 点击相册
	 */
	@Click(R.id.dialog_add_photo_album)
	protected void clickAlbum() {
		// 添加新图片
        Intent intent = new Intent(this, PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ArrayList<String> selectedPathList = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : selectedPathMap.entrySet()) {
			selectedPathList.add(entry.getKey());
		}
		intent.putExtra(PhotoSelectorActivity.K_SELECTED_PATH_LIST, selectedPathList);
		intent.putExtra(PhotoSelectorActivity.K_SELECT_TYPE, selectType);
        startActivityForResult(intent, REQ_CODE_ALBUM);
	}
	
	/**
	 * 点击拍照
	 */
	@Click(R.id.dialog_add_photo_shot)
	protected void clickShot() {
		File photoFile = PhoneStateUtils.initPhotoFile();
		filePath = photoFile.getAbsolutePath();
		Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(photoFile);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(imageCaptureIntent, REQ_CODE_TAKE_PIC);
	}
}
