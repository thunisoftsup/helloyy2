package com.thunisoft.sswy.mobile.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.util.Log;

/**
 * @author qiw
 * @version 1.0, 2013-3-21
 */
@EBean(scope = Scope.Singleton)
public class ZipUtils {
    private static final String TAG = ZipUtils.class.getSimpleName();

    /**
     * 解压缩zip文件
     * 
     * @param dir
     *            目录
     * @param zipData
     *            zip文件内容
     * @return 解压后子文件（不包括目录）的存储路径
     */
    public List<Map<String,String>> unzipFile(File dir, byte[] zipData) {
        if (dir == null || zipData == null) {
            Log.e(TAG, "the File or byte[] parameter is null.");
            return null;
        }
        List<Map<String,String>> pathList = new ArrayList<Map<String,String>>();
        ZipInputStream zis = null;
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            zis = new ZipInputStream(new ByteArrayInputStream(zipData));
            ZipEntry zipEntry = null;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                   
                    File writFile = new File(dir, zipEntry.getName());
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(writFile));
                    int b = -1;
                    while ((b = zis.read()) != -1) {
                        bos.write(b);
                    }
                    bos.close();
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("name", zipEntry.getName());
                    map.put("path", writFile.getAbsolutePath());
                    pathList.add(map);
                }
            }
        } catch (IOException e) {
            pathList = null;
            Log.e(TAG, "unzip file error:", e);
        } finally {
            try {
                if (zis != null) {
                    zis.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "close ZipInputStream error:", e);
            }
        }
        return pathList;
    }

}
