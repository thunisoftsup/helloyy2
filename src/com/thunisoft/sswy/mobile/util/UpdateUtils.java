package com.thunisoft.sswy.mobile.util;

import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.widget.Toast;

import com.thunisoft.dzfy.mobile.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

@EBean
public class UpdateUtils {
    public void checkUpdate(final Context context) {
        // 友盟检测
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                case UpdateStatus.Yes:
                    UmengUpdateAgent.showUpdateDialog(context, updateInfo);
                    break;
                case UpdateStatus.No:
                    Toast.makeText(context, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.NoneWifi:
                    Toast.makeText(context, "没有wifi网络", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.Timeout:
                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setAppkey(context.getResources().getString(R.string.umeng_appkey));
        UmengUpdateAgent.setChannel(context.getResources().getString(R.string.umeng_channel));
        UmengUpdateAgent.forceUpdate(context);
    }
    
    public void checkUpdateAuto(final Context context) {
        // 友盟检测
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                case UpdateStatus.Yes:
                    UmengUpdateAgent.showUpdateDialog(context, updateInfo);
                    break;
//                case UpdateStatus.No:
//                    Toast.makeText(context, "已经是最新版本", Toast.LENGTH_SHORT).show();
//                    break;
//                case UpdateStatus.NoneWifi:
//                    Toast.makeText(context, "没有wifi网络", Toast.LENGTH_SHORT).show();
//                    break;
//                case UpdateStatus.Timeout:
//                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
//                    break;
                }
            }
        });
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setAppkey(context.getResources().getString(R.string.umeng_appkey));
        UmengUpdateAgent.setChannel(context.getResources().getString(R.string.umeng_channel));
        UmengUpdateAgent.forceUpdate(context);
    }
}
