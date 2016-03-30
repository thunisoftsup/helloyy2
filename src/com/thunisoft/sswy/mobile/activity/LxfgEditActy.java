package com.thunisoft.sswy.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;

@EActivity(R.layout.activity_lxfgedit)
public class LxfgEditActy extends Activity  {
    @Bean
    ResponseUtilExtr responseUtil;
    @ViewById(R.id.ly_title)
    EditText lyTitle;
    @ViewById(R.id.ly_content)
    EditText lyContent;
    @ViewById(R.id.btn_commit)
    TextView commitTextView;
    public static boolean isCreatSucess = false;
    @Click(R.id.btn_back)
    public void back() {
        finish();
    }
    
    String lyTitleStr, lyContentStr, CBh;
    
    @Click(R.id.btn_commit)
    public void commit() {
        lyTitleStr = lyTitle.getText().toString();
        lyContentStr = lyContent.getText().toString();
        if (lyTitleStr == null || lyTitleStr.equals("")) {
            Toast.makeText(this, "留言标题不能为空...", Toast.LENGTH_LONG).show();
            return;
        } 
        
//        if (containsEmoji(lyTitleStr)) {
//        	 Toast.makeText(this, "暂不支持表情输入...", Toast.LENGTH_LONG).show();
//             return;
//        }
        
        if (lyContentStr == null || lyContentStr.equals("")) {
            Toast.makeText(this, "留言内容不能为空...", Toast.LENGTH_LONG).show();
            return;
        } 
        
//        if (containsEmoji(lyContentStr)) {
//       	 	Toast.makeText(this, "暂不支持表情输入...", Toast.LENGTH_LONG).show();
//            return;
//        }
        
        CBh = getIntent().getStringExtra("CBh");
        if (CBh == null || CBh.equals("")) {
            Toast.makeText(this, "未找到留言案件", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, ConfirmDialogActivity_.class);
        intent.putExtra("message", "是否确认提交");
        startActivityForResult(intent, Constants.REQUEST_CODE_CONFIRM_ZX);
    }
    
    private boolean containsEmoji(String source) {
    	source = source+" ";
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    private boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    
    @Background
    public void commitLy(String CBh, String lyTitleStr, String lyContentStr) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ajbh", CBh));
        params.add(new BasicNameValuePair("title", lyTitleStr));
        params.add(new BasicNameValuePair("content", lyContentStr));
        BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.COMMIT_LY, params);
        commitSucess(br);
    }
    
    @UiThread
    public void commitSucess(BaseResponseExtr br) {
        commitTextView.setTextColor(Color.parseColor("#ffffff"));
        commitTextView.setClickable(true);
        if (br.getMsg() != null) {
            Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
        } else {
            isCreatSucess = true;
            Toast.makeText(this, "创建留言成功!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            commitTextView.setTextColor(Color.parseColor("#cccccc"));
            commitTextView.setClickable(false);
            commitLy(CBh, lyTitleStr, lyContentStr);
        } 
    }
}
