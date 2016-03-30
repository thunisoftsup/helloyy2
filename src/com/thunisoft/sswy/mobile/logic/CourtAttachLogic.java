package com.thunisoft.sswy.mobile.logic;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.thunisoft.sswy.mobile.logic.net.CourtAttachResponseUtil;
import com.thunisoft.sswy.mobile.logic.net.NetUtils;
import com.thunisoft.sswy.mobile.logic.response.CourtAttachResponse;
import com.thunisoft.sswy.mobile.logic.response.DownloadAttachResponse;
import com.thunisoft.sswy.mobile.pojo.TCourtAttach;
import com.thunisoft.sswy.mobile.util.FileUtils;

/**
 * 法院附件逻辑类
 * 
 * @author lulg
 * 
 */
@EBean(scope = Scope.Singleton)
public class CourtAttachLogic {
    private static final String TAG = "CourtAttachLogic";
    @Bean
    NetUtils netUtils;

    @Bean
    FileUtils fileUtils;
    
    @Bean
    CourtAttachResponseUtil responseUtil;

    public DownloadAttachResponse downloadAttachFile(TCourtAttach attach, String fjlx, String courtId) {
        String url = netUtils.getMainAddress() + "/mobile/downloadCourtAttach.htm?id=" + attach.getId();
        DownloadAttachResponse dar = new DownloadAttachResponse();
        try {
            InputStream is = netUtils.getStream(url);
            if (is == null) {
                dar.setSuccess(false);
                dar.setMessage("图片下载失败！");
                Log.e(TAG, "图片下载失败！");
            } else {
                String path = FileUtils.BASE_DIR + "/courtAttach/" + courtId + "/" + fjlx + "/";
                String fileName = attach.getId() + attach.getExt();
                boolean success = fileUtils.saveFile(is, path, fileName);
                if (!success) {
                    dar.setSuccess(false);
                    dar.setMessage("保存图片失败！");
                    Log.e(TAG, "保存图片失败！");
                } else {
                    dar.setSuccess(true);
                    dar.setPath(path);
                    dar.setFileName(fileName);
                }
            }
        } catch (Exception e) {
            dar.setSuccess(false);
            dar.setMessage("图片下载失败！");
            Log.e(TAG, "图片下载失败！");
        }
        return dar;
    }

    public CourtAttachResponse loadCourtAttach(String courtId, long time) {
        String url = netUtils.getMainAddress() + "/mobile/loadCourtAttachList.htm";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("courtId", courtId));
        if (time != 0L) {
            params.add(new BasicNameValuePair("t", String.valueOf(time)));
        }
        return responseUtil.getResponse(url, params);
    }

}
