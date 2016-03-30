package com.thunisoft.sswy.mobile.activity.dzsd;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FromHtml;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.adapter.SdListAdapter;
import com.thunisoft.sswy.mobile.logic.SdLogic;
import com.thunisoft.sswy.mobile.logic.response.SdResponse;
import com.thunisoft.sswy.mobile.pojo.TSd;
import com.thunisoft.sswy.mobile.pojo.TSdWrit;
import com.thunisoft.sswy.mobile.util.EncryptionUtils;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.ZipUtils;

/**
 * 签名码签收文书_未登录
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_wsqs_wdl)
public class QmmQswsWdlActivity extends BaseActivity implements OnItemClickListener {

    @FromHtml(R.string.text_sm_dzqmm)
    @ViewById(R.id.tv_sm_qmm)
    TextView tv_sm_qmm;

    @ViewById(R.id.tv_zjhm)
    EditText tv_zjhm;

    @ViewById(R.id.tv_qmm)
    EditText tv_qmm;

    @ViewById(R.id.tv_tips)
    TextView tv_tips;

    @ViewById(R.id.list_ws)
    ListView list_ws;

    List<TSd> datas = new ArrayList<TSd>();

    SdListAdapter adapter;

    @Bean
    SdLogic sdLogic;

    @Bean
    ZipUtils zipUitls;

    @Bean
    EncryptionUtils encryptionUtils;
    
    @ViewById(R.id.btn_qs)
    Button btn_qs;

    @ViewById(R.id.btn_qb)
    Button btn_qb;
    
    WaittingDialog waitDialog;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    
    

    @AfterViews
    public void initViews() {
        setBtnBack();
        btn_qs.setOnClickListener(this);
        findViewById(R.id.btn_qb).setOnClickListener(this);
        setTitle("文书签收");
        waitDialog = new WaittingDialog(this, R.style.CustomDialogStyle, "签收中...");
        waitDialog.setIsCanclable(false);
        adapter = new SdListAdapter(this, R.layout.standard_list_item_arrow, datas,null);
        list_ws.setAdapter(adapter);
        list_ws.setOnItemClickListener(this);
        loadDatas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_qs:
            confirmQs();
            break;
        case R.id.btn_qb:
            qb();
            break;
        }
        super.onClick(v);
    }
    
    public void qb() {
        Intent intent = new Intent(this, LocalSdListActivity_.class);
        startActivity(intent);
    }
    
    /**
     * 点击签收
     */
    public void confirmQs() {
        btn_qs.setClickable(false);
        tv_tips.setText("");
        String zjhm = tv_zjhm.getText().toString();
        String dzyzm = tv_qmm.getText().toString();
        if(StringUtils.isBlank(zjhm)){
            showTips("请输入证件号");
            btn_qs.setClickable(true);
            return;
        }
        if(StringUtils.isBlank(dzyzm)){
            showTips("请输入签名码");
            btn_qs.setClickable(true);
            return;
        }
        Intent intent = new Intent(this, ConfirmDialogActivity_.class);
        intent.putExtra("message", "请确认签收，您签收后，将视为文书送达成功，系统会自动给法院反馈电子送达回证。");
        startActivityForResult(intent, Constants.REQUEST_CODE_QSWS);
    }

    public void qs() {
        waitDialog.show();
        String sfzjhm = tv_zjhm.getText().toString();
        String dzqmm = tv_qmm.getText().toString();
        doQs(sfzjhm, dzqmm);
    }
    
    @UiThread
    public void dismissDialog() {
        waitDialog.dismiss();
        btn_qs.setClickable(true);
    }

    @Background
    public void doQs(String sfzjhm, String dzqmm) {
        SdResponse sr = sdLogic.downWritByQmm(sfzjhm, dzqmm, 0L);
        if (!sr.isSuccess()) {
            dismissDialog();
            if (sr.isXtcw()) {
                showToast(sr.getMessage());
            } else {
                showTips(sr.getMessage());
            }
        } else {
            TSd sd = sr.getWrit();
            String zipString = sr.getZip();
            String dir = FileUtils.BASE_DIR + "/dzsd/writ/" + sd.getCId();
            List<Map<String, String>> pathList = zipUitls.unzipFile(new File(Environment.getExternalStorageDirectory(),
                    dir), encryptionUtils.decryptBase64ToBytes(zipString));
            if (pathList == null || pathList.isEmpty()) {
                dismissDialog();
                showTips("解压文件失败");
            } else {
                Long qssj = sd.getDQssj();
                if (qssj != null) {
                    SdResponse srUpdate = sdLogic.downWritByQmm(sfzjhm, dzqmm, qssj.longValue());
                    if (!srUpdate.isSuccess()) {
                        dismissDialog();
                        if (sr.isXtcw()) {
                            showToast(sr.getMessage());
                        } else {
                            showTips(sr.getMessage());
                        }
                    } else {
                        List<TSdWrit> writList = new ArrayList<TSdWrit>();
                        int index = 0;
                        for (Map<String, String> map : pathList) {
                            TSdWrit writ = new TSdWrit();
                            writ.setCId(sd.getCId() + "_" + index++);
                            writ.setCSdId(sd.getCId());
                            writ.setCName(map.get("name"));
                            writ.setCPath(map.get("path"));
                            writList.add(writ);
                        }
                        sdLogic.saveSd(sd, writList);
                        dismissDialog();
                        onQsComplete(sd);
                    }
                }
            }
        }
    }

    @UiThread
    public void onQsComplete(TSd sd) {
        Intent intent = new Intent(this, WritListActivity_.class);
        intent.putExtra("CId", sd.getCId());
        intent.putExtra("CAh", sd.getCAh());
        intent.putExtra("CSdrMc", sd.getCSdrMc());
        intent.putExtra("CSdrBgdh", sd.getCSdrBgdh());
        intent.putExtra("DFssj", sdf.format(sd.getDFssj()));
        intent.putExtra("DQssj", sd.getDQssj());
        intent.putExtra("CSdrTs", sd.getCSdrTs());
        intent.putExtra("CSdrFy", sd.getCSdrFy());
        intent.putExtra("CWritName", sd.getCWritName());
        startActivityForResult(intent, Constants.REQUEST_CODE_SHOW_SD_WRIT_LIST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_SHOW_SD_WRIT_LIST && resultCode == RESULT_OK) {
            tv_zjhm.setText("");
            tv_qmm.setText("");
            btn_qs.setClickable(true);
            loadDatas();
        } else if(requestCode == Constants.REQUEST_CODE_QSWS) {
            if(resultCode == RESULT_OK) {
                qs();
            } else {
                btn_qs.setClickable(true);
            }
            
        }
    }

    @Background
    public void loadDatas() {
        List<TSd> sdList = sdLogic.getSdListFromDb(2);
        datas.clear();
        datas.addAll(sdList);
        notifyDataChanged();
    }

    @UiThread
    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
        if (datas.size() > 1) {
            btn_qb.setVisibility(View.VISIBLE);
        } else {
            btn_qb.setVisibility(View.GONE);
        }
    }

    @UiThread
    public void showTips(String message) {
        tv_tips.setText(message);
    }

    @UiThread
    public void showToast(String message) {
        tv_tips.setText("");
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TSd sd = datas.get(position);
        Intent intent = new Intent(this, WritListActivity_.class);
        intent.putExtra("CId", sd.getCId());
        intent.putExtra("CAh", sd.getCAh());
        intent.putExtra("CSdrMc", sd.getCSdrMc());
        intent.putExtra("CSdrBgdh", sd.getCSdrBgdh());
        intent.putExtra("DFssj", sdf.format(sd.getDFssj()));
        intent.putExtra("CSdrTs", sd.getCSdrTs());
        intent.putExtra("CSdrFy", sd.getCSdrFy());
        intent.putExtra("CWritName", sd.getCWritName());
        startActivityForResult(intent, Constants.REQUEST_CODE_SHOW_SD_WRIT_LIST);
        //startActivity(intent);
    }

}
