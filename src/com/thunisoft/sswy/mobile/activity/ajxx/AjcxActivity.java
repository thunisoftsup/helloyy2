package com.thunisoft.sswy.mobile.activity.ajxx;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FromHtml;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.AjxxCxActy_;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.YzmDialogActivity_;
import com.thunisoft.sswy.mobile.cache.CourtCache_;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.AppSecretUtil;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 案件查询界面
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_ajcx_wdl)
public class AjcxActivity extends BaseActivity implements OnCheckedChangeListener {
    private static final String TAG = "查询案件";
    
    private static final int CXM_CX = 1;
    private static final int YZM_CX = 2;
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.tab_cxm)
    RadioButton tab_cxm;

    @ViewById(R.id.tab_dxyzm)
    RadioButton tab_dxyzm;

    @ViewById(R.id.layout_cxm_cx)
    View layout_cxm_cx;

    @ViewById(R.id.layout_yzm_cx)
    View layout_yzm_cx;

    @FromHtml(R.string.text_sm_cx_ajcxm)
    @ViewById(R.id.tv_sm_ajcxm)
    TextView tv_sm_ajcxm;

    @FromHtml(R.string.text_sm_cx_dxyzm)
    @ViewById(R.id.tv_sm_dxyzm)
    TextView tv_sm_dxyzm;

    @Bean
    AuthLogic authLogic;

    @ViewById(R.id.tv_tips)
    TextView tv_tips;
    
    
    @ViewById(R.id.tv_cxm_zjhm)
    EditText tv_cxm_zjhm;
    @ViewById(R.id.tv_cxm)
    EditText tv_cxm;
    
    @ViewById(R.id.tv_yzm_zjhm)
    EditText tv_yzm_zjhm;
    @ViewById(R.id.tv_sjhm)
    EditText tv_sjhm;
    @ViewById(R.id.tv_yzm)
    EditText tv_yzm;
    @ViewById(R.id.btn_send_yzm)
    Button fsyzm;
    
    String tempSid;
    private int flag;
    private static final int MAX_ONEPAGE_SIZE = 20;
    private int pageIndex = 1;
    String sfzjhmCxm;String sfzjhmYzm; String cxm; String yzm;String sjhm;
    String yzm_pic;
    
    @AfterViews
    public void initViews() {
        flag = CXM_CX;
        setBtnBack();
        tab_cxm.setOnCheckedChangeListener(this);
        tab_dxyzm.setOnCheckedChangeListener(this);
        setTitle(R.string.text_ajcx);
        fsyzm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sfzjhmYzm = tv_yzm_zjhm.getText().toString();
                if (sfzjhmYzm == null || sfzjhmYzm.equals("")) {
                    Toast.makeText(AjcxActivity.this, "请输入证件号码", Toast.LENGTH_LONG).show();
                    return;
                }
                
                sjhm = tv_sjhm.getText().toString();
                if (sjhm == null || sjhm.equals("")) {
                    Toast.makeText(AjcxActivity.this, "请输入手机号码", Toast.LENGTH_LONG).show();
                    return;
                }
                fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_sel);
                fsyzm.setClickable(false);
                getSjyzm();
            }
        });
    }

    @UiThread
    public void hideTips() {
        tv_tips.setText("");
    }

    @UiThread
    public void shoTips(String message) {
        tv_tips.setText(message);
    }

    public void switchRadio(View v) {
        switch (v.getId()) {
        case R.id.tab_cxm:
            tv_sm_ajcxm.setVisibility(View.VISIBLE);
            tv_sm_dxyzm.setVisibility(View.GONE);
            layout_cxm_cx.setVisibility(View.VISIBLE);
            layout_yzm_cx.setVisibility(View.GONE);
            break;
        case R.id.tab_dxyzm:
            tv_sm_ajcxm.setVisibility(View.GONE);
            tv_sm_dxyzm.setVisibility(View.VISIBLE);
            layout_cxm_cx.setVisibility(View.GONE);
            layout_yzm_cx.setVisibility(View.VISIBLE);
            break;
        }
        tv_tips.setText("");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            switchRadio(buttonView);
        }
    }
    
    @Click(R.id.btn_cxm_cx)
    public void cxmCx() {
        flag = CXM_CX;
        sfzjhmCxm = tv_cxm_zjhm.getText().toString();
        if (sfzjhmCxm == null || sfzjhmCxm.equals("")) {
            Toast.makeText(this, "请输入证件号码!", Toast.LENGTH_LONG).show();
            return;
        }
        cxm = tv_cxm.getText().toString();
        if (cxm == null || cxm.equals("")) {
            Toast.makeText(this, "请输入查询码", Toast.LENGTH_LONG).show();
            return;
        }
        getDatas();
    }
    
    @Click(R.id.btn_yzm_cx)
    public void yzmCx() {
        flag = YZM_CX;

        sfzjhmYzm = tv_yzm_zjhm.getText().toString();
        if (sfzjhmYzm == null || sfzjhmYzm.equals("")) {
            Toast.makeText(this, "请输入证件号码", Toast.LENGTH_LONG).show();
            return;
        }
        sjhm = tv_sjhm.getText().toString();
        if (sjhm == null || sjhm.equals("")) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
        yzm = tv_yzm.getText().toString();
        if (yzm == null || yzm.equals("")) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
            return;
        }
        getDatas();
    }
    
    @Background
    public void getSjyzm() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sfzjhm", sfzjhmYzm));
        params.add(new BasicNameValuePair("phone", sjhm));
        params.add(new BasicNameValuePair("courtId", CourtCache_.getInstance_(this).getCourtId("")));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_SJYZM, params);
        getSjyzmDone(br);
    }
    
    @UiThread 
    public void getSjyzmDone(BaseResponseExtr br) {
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
            fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_default);
            fsyzm.setClickable(true);
        } else {
            // 过三十秒才让重新输入
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                int flag = 60;
                @Override
                public void run() {
                    flag -- ;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            fsyzm.setText(flag+"s后重发送");
                            fsyzm.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_input_text_size_));
                        }
                    });
                    if (flag == 0) {
                        timer.cancel();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                fsyzm.setBackgroundResource(R.drawable.dialog_btn_blue_bg_default);
                                fsyzm.setClickable(true);
                                fsyzm.setText("获取验证码");
                                fsyzm.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.default_input_text_size));
                            }
                        });
                    }
                }
            }, 0, 1000);
        }
    }
    
    @UiThread
    public void showYzm() {
        Intent intent = new Intent(this, YzmDialogActivity_.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_YZM);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_CODE_YZM && resultCode == RESULT_OK) {
            yzm_pic = data.getStringExtra("yzm");
        }
    }
    
    @Background 
    public void getDatas() {
    	if(StringUtils.isBlank(this.tempSid)) {
    		Log.i(TAG, "点击查询");
    		getTempSidAndDoAjcx();
    	} else {
    		Log.i(TAG, "loginBackground会话存在，直接登录:"+tempSid);
    		if (flag == CXM_CX) {
    			doCxmCx();
    		} else {
    			doYzmCx();
    		}
    	}
    }

	private void doYzmCx() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sfzjhm", AppSecretUtil.encodeAppString(sfzjhmYzm)));
		params.add(new BasicNameValuePair("phone", AppSecretUtil.encodeAppString(sjhm)));
		params.add(new BasicNameValuePair("yzm", AppSecretUtil.encodeAppString(yzm)));
		params.add(new BasicNameValuePair("page", pageIndex+""));
		params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
		params.add(new BasicNameValuePair("tempSid", tempSid));
		BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_YZM_AJ, params);
		loadDataDone(br);
	}

    int cxTimes = 0;
    private void doCxmCx() {
        cxTimes++;
        Log.e("loginTimes", String.valueOf(cxTimes));
        if(cxTimes > 3) {
            cxTimes = 0;
            return;
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (yzm_pic != null) {
            params.add(new BasicNameValuePair("yzm", yzm_pic));
        }
        params.add(new BasicNameValuePair("sfzjhm", AppSecretUtil.encodeAppString(sfzjhmCxm)));
        params.add(new BasicNameValuePair("cxm", AppSecretUtil.encodeAppString(cxm)));
        params.add(new BasicNameValuePair("tempSid", tempSid));
        params.add(new BasicNameValuePair("page", pageIndex+""));
        params.add(new BasicNameValuePair("rows", MAX_ONEPAGE_SIZE+""));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_CXM_AJ, params);
        loadDataDone(br);
    }
    
    @Background
    public void getTempSidAndDoAjcx() {
        BaseResponse br = authLogic.getAjxxTempSid();
        if (br.isSuccess()) {
            Log.i(TAG, "重新获取会话成功，重新登录");
            tempSid = br.getTempSid();
            if (flag == CXM_CX) {
    			doCxmCx();
    		} else {
    			doYzmCx();
    		}
        } else {
            Log.i(TAG, "重新获取会话失败");
            showToast("建立会话失败");
        }
    }
    
    @UiThread
    public void loadDataDone(BaseResponseExtr br) {
        if (br.getMsg() != null) {
            if(StringUtils.isNotBlank(br.getMsg())) {
                Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
            }
            try {
                if (br.getResJson().has("errorShowType")) {
                    if(Constants.ERROR_SHOW_YZM == br.getResJson().getInt("errorShowType")) {
                        cxTimes = 0;
                        showYzm();
                    } else if(Constants.ERROR_REQUEST_SID == br.getResJson().getInt("errorShowType")) {
                        Log.e(TAG, "Tempsid不存在，重新请求。。。。");
                        getTempSidAndDoAjcx();
                    }
                }
            } catch (Exception e) {
                cxTimes = 0;
                showToast("查询案件出错!");
            }
        } else {
            cxTimes = 0;
            // 跳转
            try {
                JSONArray resJsonArray = br.getResJson().getJSONArray("ajList");
                Intent itt = new Intent();
                itt.putExtra("resJson", resJsonArray.toString());
                itt.putExtra("flag", flag);
                itt.putExtra("cxm", cxm);
                itt.putExtra("sfzjhmYzm", sfzjhmYzm);
                itt.putExtra("sfzjhmCxm", sfzjhmCxm);
                itt.putExtra("yzm", yzm);
                itt.putExtra("sjhm", sjhm);
                itt.putExtra("tmpSid", tempSid);
                itt.putExtra("cxfs", flag);
                itt.setClass(this, AjxxCxActy_.class);
                startActivity(itt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
        }
        
    }
}
