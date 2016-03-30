package com.thunisoft.sswy.mobile.activity.dialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.BaseLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.logic.response.YzmResponse;
import com.thunisoft.sswy.mobile.util.FileUtils;

@EActivity(R.layout.dialog_yzm)
public class YzmDialogActivity extends BaseActivity {

    @ViewById(R.id.tv_yzm)
    TextView tv_yzm;
    
    @ViewById(R.id.img_yzm)
    ImageView img_yzm;
    
    @Bean
    BaseLogic baseLogic;
    
    @Bean
    AuthLogic authLogic;
    
    @Bean
    FileUtils fileUtils;

    @AfterViews
    public void initViews() {
        loadYzm();
    }
    
    @Click(R.id.img_yzm)
    public void imgYzmClick() {
        loadYzm();
    }

    @Click(R.id.btn_positive)
    public void positiveClick() {
        String yzm = tv_yzm.getText().toString();
        jcyzm(yzm);
    }
    
    @Background
    public void jcyzm(String yzm) {
        BaseResponse br = authLogic.jcyzm(yzm);
        if(br.isSuccess()) {
            Intent data = new Intent();
            data.putExtra("yzm", yzm);
            setResult(RESULT_OK, data);
            finish();
        } else {
            loadYzm();
        }
    }
    
    @Background
    public void loadYzm() {
        YzmResponse yr = baseLogic.getYzm();
        if(!yr.isSuccess()) {
            showToast(yr.getMessage());
        } else {
            Bitmap bm = BitmapFactory.decodeStream(yr.getInputStream());
            fileUtils.closeQuietly(yr.getInputStream());
            showYzm(bm);
        }
    }
    
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
//             return false; 
//        }else { 
//        } 
    	return super.onKeyDown(keyCode, event); 
           
    } 
    
    @UiThread
    public void showYzm(Bitmap bm) {
        img_yzm.setScaleType(ScaleType.FIT_XY);
        img_yzm.setImageBitmap(bm);
    }

}
