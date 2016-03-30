package com.thunisoft.sswy.mobile.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;

//import com.nostra13.universalimageloader.utils.IoUtils;

@EBean(scope = Scope.Singleton)
public class FileUtils {
	private static final String TAG = "FileUtils";
	public static final String BASE_DIR = "susong51";

	/**
	 * 文件长度最大为50M
	 */
	public static final long FILE_MAX_LENGTH = 50*1024*1024;
	
	/**
	 * 保存文件
	 * 
	 * @param is
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static boolean saveFile(InputStream is, String path, String fileName) {
		if (is == null || path == null || !hasExternalStorage()) {
			return false;
		}
		FileOutputStream fos = null;
		try {
			File savePath = new File(Environment.getExternalStorageDirectory(), path);
			if (!savePath.exists()) {
				savePath.mkdirs();
			}
			File file = new File(savePath, fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			// IoUtils.copyStream(is, fos, null);
			return true;
		} catch (IOException e) {
			Log.e(TAG, "保存文件出错！", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					Log.e(TAG, "关闭流出错！", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.e(TAG, "关闭流出错！", e);
				}
			}
		}
		return false;
	}

	public static void saveFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && !dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				Log.e(TAG, "资源关闭出错", e);
			}
		}
	}

	/**
	 * 判断有无SD卡
	 * 
	 * @return
	 */
	public static boolean hasExternalStorage() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileSuffix(String path) {
		int splitIndex = path.lastIndexOf(".");
		String suffix = "";
		if (splitIndex >= 0) {
			suffix = path.substring(splitIndex + 1);
		}
		return suffix;
	}

	/**
	 * word文档
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isWordFile(String path) {
		String suffix = getFileSuffix(path);
		return "doc".equals(suffix) || "docx".equals(suffix) || "wps".equals(suffix);
	}

	/**
	 * excel表格
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExcelFile(String path) {
		String suffix = getFileSuffix(path);
		return "xls".equals(suffix) || "xlsx".equals(suffix);
	}

	/**
	 * PDF文档
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isPdfFile(String path) {
		String suffix = getFileSuffix(path);
		return "pdf".equals(suffix);
	}

	/**
	 * 过滤文件，获取符合条件的文件
	 * 
	 * @param files
	 * @return 获取符合条件的文件
	 */
	public static File[] filterFiles(File[] files, int fileType) {
		List<File> fileList = new ArrayList<File>();
		if (null != files && files.length > 0) {
			Map<String, Boolean> suffixMap;
			if (NrcConstants.FILE_TYPE_ALL == fileType) {
				suffixMap = FileUtils.getAllSuffixMap();
			} else if (NrcConstants.FILE_TYPE_PIC == fileType) {
				suffixMap = FileUtils.getImgSuffixMap();
			} else {
				suffixMap = FileUtils.getAllSuffixMap();
			}
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					fileList.add(file);
				} else {
					String suffix = getFileSuffix(file.getAbsolutePath());
					Boolean success = suffixMap.get(suffix.toLowerCase());
					if (null != success) {
						fileList.add(file);
					}
				}
			}
		}
		return fileList.toArray(new File[fileList.size()]);
	}

	/**
	 * 封装所有的文件 扩展名
	 * 
	 * @return
	 */
	public static Map<String, Boolean> getAllSuffixMap() {
		Map<String, Boolean> allSuffixMap = new HashMap<String, Boolean>();

		allSuffixMap.put("txt", true);
		allSuffixMap.put("doc", true);
		allSuffixMap.put("docx", true);
		allSuffixMap.put("xls", true);
		allSuffixMap.put("xlsx", true);
		allSuffixMap.put("pdf", true);

		allSuffixMap.put("jpg", true);
		allSuffixMap.put("jpeg", true);
		allSuffixMap.put("gif", true);
		allSuffixMap.put("png", true);
		allSuffixMap.put("bmp", true);

		allSuffixMap.put("mp3", true);
		allSuffixMap.put("wav", true);

		allSuffixMap.put("avi", true);
		allSuffixMap.put("rm", true);
		allSuffixMap.put("rmvb", true);
		allSuffixMap.put("mp4", true);

		return allSuffixMap;
	}

	/**
	 * 封装图片文件 扩展名
	 * 
	 * @return
	 */
	public static Map<String, Boolean> getImgSuffixMap() {
		Map<String, Boolean> imgSuffixMap = new HashMap<String, Boolean>();
		imgSuffixMap.put("jpg", true);
		imgSuffixMap.put("jpeg", true);
		imgSuffixMap.put("gif", true);
		imgSuffixMap.put("png", true);
		imgSuffixMap.put("bmp", true);
		return imgSuffixMap;
	}

	/**
	 * 缩略图 宽
	 */
	private static final int THUMB_WIDTH = 200;

	/**
	 * 缩略图 高
	 */
	private static final int THUMB_HEIGHT = 200;

	/**
	 * 根据文件路径，生成缩略图
	 * 
	 * @param filePath
	 * @return
	 */
	public static void generateThumb(Context context, String filePath) {
		String thumbPath = getThumbPath(context, filePath);
		File thumbFile = new File(thumbPath);
		if (!thumbFile.exists()) {
			Bitmap bmpTemp = getLoacalBitmap(filePath, THUMB_WIDTH, THUMB_HEIGHT); // 原图
			int rotation = getImageRotation(filePath);
			Bitmap bmp = rotateBitmap(bmpTemp, rotation); // 旋转之后的图
			compressBitmapQualityAndSave(bmp, 100, thumbPath);
		}
	}

	/**
	 * 根据文件路径，生成缩略图
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getThumbnailBmg(Context context, String path) {
		String thumbPath = getThumbPath(context, path);
		File thumbFile = new File(thumbPath);
		if (!thumbFile.exists()) {
			generateThumb(context, path);
		}
		Bitmap thumbBmp = BitmapFactory.decodeFile(thumbPath);
		return thumbBmp;
	}

	/**
	 * 根据文件路径，获取缩略图drawable
	 * 
	 * @param filePath
	 * @return
	 */
	public static Drawable getThumbnailDrawable(Context context, String path) {
		String thumbPath = getThumbPath(context, path);
		File thumbFile = new File(thumbPath);
		if (!thumbFile.exists()) {
			generateThumb(context, path);
		}
		Bitmap thumbBmp = BitmapFactory.decodeFile(thumbPath);
		return new BitmapDrawable(thumbBmp);
	}

	/**
	 * 根据文件路径获取缩略图路径
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static String getThumbPath(Context context, String path) {
		String thumbPath = "";
		if (StringUtils.isNotBlank(path)) {
			File file = new File(path);
			String parentPath = context.getFilesDir().getAbsolutePath() + "/thumbnail";
			thumbPath = parentPath + "/" + file.getName();
		}
		return thumbPath;
	}

	/**
	 * 获取本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String path, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bitMap = BitmapFactory.decodeFile(path, options);
		if (bitMap == null) {
			return null;
		}
		return bitMap;
	}

	/**
	 * 读取图片的Exif信息
	 * 
	 * @param path
	 * @return
	 */
	private static int getImageRotation(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
		}
		return degree;
	}

	/**
	 * 旋转图片角度
	 * 
	 * @param bitmap
	 * @param rotate
	 * @return
	 */
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotation) {
		if (bitmap == null)
			return null;

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix mtx = new Matrix();
		mtx.postRotate(rotation);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	/**
	 * 计算图片的缩小比率
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			int heightRatio = Math.round((float) height / (float) reqHeight);
			int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}
		return inSampleSize;
	}

	/**
	 * 按照质量压缩图片并保存,
	 * 
	 * @param bitmap
	 * @param quality
	 * @param toPath
	 * @return
	 */
	private static boolean compressBitmapQualityAndSave(Bitmap bitmap, int quality, String toPath) {
		if (null == bitmap) {
			return false;
		}
		try {
			File file = new File(toPath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * 获取图片文件名称
	 * @param context
	 * @param fileName 文件名
	 * @return
	 */
	public static String getImageFilePath(Context context, String fileName) {
		String organPath = context.getFilesDir().getAbsolutePath() + "/image";
		organPath = organPath + "/" + new Date().getTime() + ".";
		String suffix = FileUtils.getFileSuffix(fileName);
		if (StringUtils.isBlank(suffix)) {
			suffix = "jpeg";
		}
		organPath = organPath + suffix;
		return organPath;
	}
	
	/**
	 * 获取备份文件名称
	 * @param context
	 * @param organPath 源文件路径
	 * @return
	 */
	public static String getBackupFilePath(Context context, String organPath) {
		File organFile = new File(organPath);
		String backupPath = Environment.getExternalStorageDirectory() + "/thunisoft/android/dzfy/image";
		backupPath = backupPath + "/" + organFile.getName();
		return backupPath;
	}

	/**
	 * inputStream流 保存到 文件
	 * 
	 * @param ins
	 * @param file
	 */
	public static void inputStreamToFile(InputStream ins, String path) {
		OutputStream os = null;
		try {
			File file = new File(path);
			if (!file.getParentFile().exists()) {
				file.mkdirs();
			}
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
				os.write(buffer, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * copy文件
	 * @param fromFile
	 * @param toFile
	 * @param rewrite
	 * @return
	 */
	public static boolean copyfile(File fromFile, File toFile){
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists()) {
			toFile.delete();
		}
		try {
			FileInputStream fosfrom = new FileInputStream(fromFile);
			FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); //将内容写到新文件当中
			}
			fosfrom.close();
			fosto.close();
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 删除文件，备份文件、缩略图（如果是图片文件）、源文件
	 * @param context
	 * @param organPath
	 */
	public static void deleteFile(Context context, String organPath) {
		deleteBackupFile(context, organPath);
		deleteThumbFile(context, organPath);
		File organFile = new File(organPath);
		if (organFile.exists()) {
			organFile.delete();
		}
	}
	
	/**
	 * 删除备份文件
	 * @param context
	 * @param organPath
	 */
	public static void deleteBackupFile(Context context, String organPath) {
		String backupPath = FileUtils.getBackupFilePath(context, organPath);
		File backupFile = new File(backupPath);
		if (backupFile.exists()) {
			backupFile.delete();
		}
	}
	
	/**
	 * 删除缩略图
	 * @param context
	 * @param organPath
	 */
	public static void deleteThumbFile(Context context, String organPath) {
		String thumbPath = FileUtils.getThumbPath(context, organPath);
		File thumbFile = new File(thumbPath);
		if (thumbFile.exists()) {
			thumbFile.delete();
		}
	}
	
	/**
	 * 获取文件大小 单位B
	 * @param path
	 * @return
	 */
	public static long getFileLengthB(String path){
		File f= new File(path);  
	    if (f.exists() && f.isFile()){  
	        return f.length();  
	    }else{  
	        return 0; 
	    }  
	}
	
	public static boolean fileLengthIsOk(String path) {
		if (FILE_MAX_LENGTH < getFileLengthB(path)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 复制文件到制定目录
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean copyFileTo(String source, String target){
		return writeFileTo(getBytes(source), target);
	}
	
	/**
	 * 写文件到制定目录
	 * @param buffer
	 * @param filePath
	 * @return
	 */
	public static boolean writeFileTo(byte[] buffer, String filePath){
		FileOutputStream out;
		try {
			out = new FileOutputStream(filePath);
			out.write(buffer);
		    out.flush();
		    out.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            return buffer;
        } catch (IOException e) {  
            return buffer;  
        }  
        return buffer;  
    }
    
    public static Bitmap drawableToBitamp(Drawable drawable) {
    	BitmapDrawable bd = (BitmapDrawable) drawable;
    	return bd.getBitmap();
    }
}
