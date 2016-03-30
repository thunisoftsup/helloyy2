package com.thunisoft.sswy.mobile.logic.net;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.thunisoft.sswy.mobile.exception.SSWYException;
import com.thunisoft.sswy.mobile.util.FileUtils;

@EBean(scope = Scope.Singleton)
public class ResponseUtilExtr {
    @Bean
    NetUtils netUtils;
    @Bean
    FileUtils fileUtils;
    
    public static final int LOAD_WSLACX = 0x001;
    public static final int LOAD_SSXFCX = 0x002;
    public static final int LOAD_WSYJCX = 0x003;
    public static final int LOAD_ZXXSJB = 0x004;
    public static final int LOAD_LXFG = 0x005;
    public static final int LOAD_LYAJ = 0x006;
    public static final int COMMIT_LY = 0x007;
    public static final int LOAD_LXFG_DETAIL = 0x008;
    public static final int LOAD_LXFG_LYALL = 0x009;
    public static final int LOAD_OPEN_MODEL_FLAG = 0x0010;
    public static final int LOAD_AjXX = 0x0011;
    public static final int LOAD_AjXX_DETAIL_PRO = 0x0012;
    public static final int LOAD_AjXX_SPF_DETAIL_PRO = 0x0013;
    public static final int LOAD_AJDT = 0x0014;
    public static final int LOAD_TEMP_SID = 0x0015;
    public static final int LOAD_CXM_AJ = 0x0016;
    public static final int LOAD_YZM_AJ = 0x0017;
    public static final int LOAD_SJYZM = 0x0018;
    public static final int LOAD_AjXX_DETAIL_PUB = 0x0019;
    public static final int LOAD_AjXX_SPF_DETAIL_PUB = 0x0020;
    public static final int LOAD_AjXX_COUNT = 0x0021;
    public static final int LOAD_Aj_LXFG = 0x0022;
    public static final int LOAD_SF_LIST = 0x0023;
    public static final int LOAD_Aj_LXFGPUB = 0x0024;
    public static final int LOAD_AJDTPUB = 0x0025;
    public static final int LOAD_ADD_CASE = 0x0026;
    
    public static final int LOAD_WSJFLIST = 0x0027;
    public static final int LOAD_WSJFHD = 0x0028;
    public static final int LOAD_GETAPLIPAY = 0x0029;
    public static final int LOAD_PAYNO = 0x0030;
    public static final int LOAD_SIGNPRAMAS = 0x0031;
    public static final int LOAD_GETPAYORNOT = 0x0032;

    public class BaseResponseExtr {
        JSONObject resJson;
        String messg;
        public void setMessage(String msg) {
            this.messg = msg;
        }
        
        public String getMsg() {
            return messg;
        }
        
        public JSONObject getResJson() {
            return resJson;
        }
    }
    
    public BaseResponseExtr getResponse(int urlflg, List<NameValuePair> params) {
        String url = null;
        if (urlflg == LOAD_WSLACX) {
//            url = netUtils.getServerAddress() + "/mobile/pro/layy/loadLayyList.htm";
            url = netUtils.getServerAddress() + "/api/wsla/loadWslaList";
        } else if (urlflg == LOAD_SSXFCX) {
            url = netUtils.getServerAddress() + "/mobile/pro/ssxf/loadSsxfList.htm";
        } else if (urlflg == LOAD_WSYJCX) {
            url = netUtils.getServerAddress() + "/mobile/pro/wsyj/loadWsyjList.htm";
        } else if (urlflg == LOAD_ZXXSJB) {
            url = netUtils.getServerAddress() + "/mobile/pro/zxxsjb/loadZxxsjbList.htm";
        } else if (urlflg == LOAD_LYAJ) {
            url = netUtils.getServerAddress() + "/mobile/pro/lxfg/loadAjList.htm";
        } else if (urlflg == LOAD_LXFG) {
            url = netUtils.getServerAddress() + "/mobile/pro/lxfg/loadLxfgList.htm";
        } else if (urlflg == COMMIT_LY) {
            url = netUtils.getServerAddress() + "/mobile/pro/lxfg/doCreateLxfg.htm";
        } else if (urlflg == LOAD_LXFG_DETAIL) {
            url = netUtils.getServerAddress() + "/mobile/pro/lxfg/detail.htm";
        } else if (urlflg == LOAD_LXFG_LYALL) {
            url = netUtils.getServerAddress() + "/mobile/pro/lxfg/loadOtherLyList.htm";
        } else if (urlflg == LOAD_OPEN_MODEL_FLAG) {
            url = netUtils.getServerAddress() + "/mobile/pro/checkModuleReachable.htm";
        } else if (urlflg == LOAD_AjXX) {
            url = netUtils.getServerAddress() + "/mobile/pro/ajxx/loadAjList.htm";
        } else if (urlflg == LOAD_AjXX_DETAIL_PRO) {
            url = netUtils.getServerAddress() + "/mobile/pro/ajxx/detail.htm";
        } else if (urlflg == LOAD_AjXX_SPF_DETAIL_PRO) {
            url = netUtils.getServerAddress() + "/mobile/pro/ajxx/specificDetail.htm";
        } else if (urlflg == LOAD_AJDT) {
            url = netUtils.getServerAddress() + "/mobile/pro/ajxx/loadAjdtList.htm";
        } else if (urlflg == LOAD_AJDTPUB) {
            url = netUtils.getServerAddress() + "/mobile/pub/ajxx/loadAjdtList.htm";
        } else if (urlflg == LOAD_CXM_AJ) {
            url = netUtils.getServerAddress() + "/mobile/cxmCxaj.htm";
        } else if (urlflg == LOAD_YZM_AJ) {
            url = netUtils.getServerAddress() + "/mobile/yzmCxaj.htm";
        } else if (urlflg == LOAD_TEMP_SID) {
            url = netUtils.getServerAddress() + "/mobile/ajxx/getTempSid.htm";
        } else if (urlflg == LOAD_SJYZM) {
            url = netUtils.getServerAddress() + "/mobile/sendSjyzm.htm";
        } else if (urlflg == LOAD_AjXX_DETAIL_PUB) {
            url = netUtils.getServerAddress() + "/mobile/pub/ajxx/detail.htm";
        } else if (urlflg == LOAD_AjXX_SPF_DETAIL_PUB) {
            url = netUtils.getServerAddress() + "/mobile/pub/ajxx/specificDetail.htm";
        } else if (urlflg == LOAD_AjXX_COUNT) {
            url = netUtils.getServerAddress() + "/mobile/pro/ajxx/loadUpdateCount.htm";
        } else if (urlflg == LOAD_Aj_LXFG) {
        	url = netUtils.getServerAddress() + "/mobile/pro/lxfg/ajLyDetail.htm";
        } else if (urlflg == LOAD_Aj_LXFGPUB) {
        	url = netUtils.getServerAddress() + "/mobile/pub/lxfg/ajLyDetail.htm";
        } else if (urlflg == LOAD_SF_LIST) {
        	url = netUtils.getMainAddress() + "/mobile/getSfList.htm";
        } else if (urlflg == LOAD_ADD_CASE) {
        	url = netUtils.getMainAddress() + "/mobile/pro/ajxx/addAj.htm";
        } else if (urlflg == LOAD_WSJFLIST) {
        	url = netUtils.getServerAddress() + "/api/wsjf/getUserRelatedWsjfList";
        }else if(urlflg == LOAD_WSJFHD){
        	url = netUtils.getServerAddress() + "/api/wsjf/updateJfjg";
        }else if(urlflg ==LOAD_GETAPLIPAY){
        	url = netUtils.getServerAddress() + "/api/wsjf/getAlipayByWsjfId";
        }else if(urlflg ==LOAD_PAYNO){
        	url = netUtils.getServerAddress() + "/api/wsjf/createOrderId";
        }else if(urlflg ==LOAD_SIGNPRAMAS){
        	url = netUtils.getServerAddress() + "/api/alipay/signRequestParams";
        }else if(urlflg ==LOAD_GETPAYORNOT){
        	url = netUtils.getServerAddress() + "/api/wsjf/getWsjfById";
        }  
        

        BaseResponseExtr br = new BaseResponseExtr();
        try {
            String result = netUtils.post(url, params);
            Log.i("susong51", "net res:"+result);
            if (result != null) {
               br.resJson = new JSONObject(result);
               if (!br.resJson.getBoolean("success")) {
                   br.setMessage(br.resJson.getString("message"));
               }
            }
        } catch (SSWYException e) {
            Log.e("", "",e);
            br.setMessage(e.getMessage());
        } catch (JSONException e) {
            Log.e("", "",e);
            br.setMessage("数据解析失败");
        } catch (Exception e) {
            Log.e("", "",e);
            br.setMessage(e.getMessage());
        }
        return br;
    }
    
    public BaseResponseExtr getResponseDldFile(String CBh, String fjlx) {
        BaseResponseExtr br = new BaseResponseExtr();
        String path = FileUtils.BASE_DIR + "/wsyj/";
        String filePath = path+fjlx;
        File file = new File(Environment.getExternalStorageDirectory(), filePath);
        if (file.exists()) {
            br.setMessage("hasdownload;"+filePath);
            return br;
        }
        String url = netUtils.getServerAddress() + "/mobile/pro/downLoadJz.htm";
        url += "?id="+CBh;
        try {
            InputStream is = netUtils.getStreamWithsid(url);
            if (is == null) {
                br.setMessage("下载文件失败");
                File fileTemp = new File(filePath);
                if (fileTemp.exists()) {
                	fileTemp.delete();
                }
            } else {
                String fileName = fjlx;
                boolean success = fileUtils.saveFile(is, path, fileName);
                if (!success) {
                    br.setMessage("下载文件失败");
                } else {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("fileName", path+fileName);
                    br.resJson = jsonObj;
                }
            }
        } catch (JSONException e) {
            br.setMessage("数据解析失败");
        } catch (Exception e) {
            br.setMessage(e.getMessage());
        }
        return br;
    }
    
    public static Intent openFile(String filePath){  
        File file = new File(Environment.getExternalStorageDirectory(), filePath);  
        filePath = file.getPath();
        if(!file.exists()) return null;  
        /* 取得扩展名 */  
        String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();   
        /* 依扩展名的类型决定MimeType */  
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||  
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            return getAudioFileIntent(filePath);  
        }else if(end.equals("3gp")||end.equals("mp4")){  
            return getAudioFileIntent(filePath);  
        }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||  
                end.equals("jpeg")||end.equals("bmp")){  
            return getImageFileIntent(filePath);  
        }else if(end.equals("apk")){  
            return getApkFileIntent(filePath);  
        }else if(end.equals("ppt")){  
            return getPptFileIntent(filePath);  
        }else if(end.equals("xls")){  
            return getExcelFileIntent(filePath);  
        }else if(end.equals("doc")){  
            return getWordFileIntent(filePath);  
        }else if(end.equals("pdf")){  
            return getPdfFileIntent(filePath);  
        }else if(end.equals("chm")){  
            return getChmFileIntent(filePath);  
        }else if(end.equals("txt")){  
            return getTextFileIntent(filePath,false);  
        }else{  
            return getAllIntent(filePath);  
        }  
    }  
    
    public static Intent openFileWithAllPath(String filePath){  
        File file = new File(filePath);  
        filePath = file.getPath();
        if(!file.exists()) return null;  
        /* 取得扩展名 */  
        String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();   
        /* 依扩展名的类型决定MimeType */  
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||  
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            return getAudioFileIntent(filePath);  
        }else if(end.equals("3gp")||end.equals("mp4")){  
            return getAudioFileIntent(filePath);  
        }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||  
                end.equals("jpeg")||end.equals("bmp")){  
            return getImageFileIntent(filePath);  
        }else if(end.equals("apk")){  
            return getApkFileIntent(filePath);  
        }else if(end.equals("ppt")){  
            return getPptFileIntent(filePath);  
        }else if(end.equals("xls")){  
            return getExcelFileIntent(filePath);  
        }else if(end.equals("doc")){  
            return getWordFileIntent(filePath);  
        }else if(end.equals("pdf")){  
            return getPdfFileIntent(filePath);  
        }else if(end.equals("chm")){  
            return getChmFileIntent(filePath);  
        }else if(end.equals("txt")){  
            return getTextFileIntent(filePath,false);  
        }else{  
            return getAllIntent(filePath);  
        }  
    }  
      
    //Android获取一个用于打开APK文件的intent  
    public static Intent getAllIntent( String param ) {  
  
        Intent intent = new Intent();    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        intent.setAction(android.content.Intent.ACTION_VIEW);    
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri,"*/*");   
        return intent;  
    }  
    //Android获取一个用于打开APK文件的intent  
    public static Intent getApkFileIntent( String param ) {  
  
        Intent intent = new Intent();    
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
        intent.setAction(android.content.Intent.ACTION_VIEW);    
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri,"application/vnd.android.package-archive");   
        return intent;  
    }  
  
    //Android获取一个用于打开VIDEO文件的intent  
    public static Intent getVideoFileIntent( String param ) {  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        intent.putExtra("oneshot", 0);  
        intent.putExtra("configchange", 0);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "video/*");  
        return intent;  
    }  
  
    //Android获取一个用于打开AUDIO文件的intent  
    public static Intent getAudioFileIntent( String param ){  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        intent.putExtra("oneshot", 0);  
        intent.putExtra("configchange", 0);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "audio/*");  
        return intent;  
    }  
  
    //Android获取一个用于打开Html文件的intent     
    public static Intent getHtmlFileIntent( String param ){  
  
        Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.setDataAndType(uri, "text/html");  
        return intent;  
    }  
  
    //Android获取一个用于打开图片文件的intent  
    public static Intent getImageFileIntent( String param ) {  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "image/*");  
        return intent;  
    }  
  
    //Android获取一个用于打开PPT文件的intent     
    public static Intent getPptFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");     
        return intent;     
    }     
  
    //Android获取一个用于打开Excel文件的intent     
    public static Intent getExcelFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/vnd.ms-excel");     
        return intent;     
    }     
  
    //Android获取一个用于打开Word文件的intent     
    public static Intent getWordFileIntent( String param ){    
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/msword");     
        return intent;     
    }     
  
    //Android获取一个用于打开CHM文件的intent     
    public static Intent getChmFileIntent( String param ){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/x-chm");     
        return intent;     
    }     
  
    //Android获取一个用于打开文本文件的intent     
    public static Intent getTextFileIntent( String param, boolean paramBoolean){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        if (paramBoolean){     
            Uri uri1 = Uri.parse(param );     
            intent.setDataAndType(uri1, "text/plain");     
        }else{     
            Uri uri2 = Uri.fromFile(new File(param ));     
            intent.setDataAndType(uri2, "text/plain");     
        }     
        return intent;     
    }    
    //Android获取一个用于打开PDF文件的intent     
    public static Intent getPdfFileIntent( String param ){     
  
        Intent intent = new Intent("android.intent.action.VIEW");     
        intent.addCategory("android.intent.category.DEFAULT");     
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
        Uri uri = Uri.fromFile(new File(param ));     
        intent.setDataAndType(uri, "application/pdf");     
        return intent;     
    }  
}

