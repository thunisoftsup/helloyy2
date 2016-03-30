package com.thunisoft.sswy.mobile.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.AddCaseActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.activity.dzsd.WritListActivity_;
import com.thunisoft.sswy.mobile.adapter.SdListAdapter;
import com.thunisoft.sswy.mobile.interfaces.ILoadMoreListener;
import com.thunisoft.sswy.mobile.interfaces.IOnCkxqListener;
import com.thunisoft.sswy.mobile.interfaces.IOnWsqsListener;
import com.thunisoft.sswy.mobile.interfaces.IWaitingDialogNotifier;
import com.thunisoft.sswy.mobile.logic.SdLogic;
import com.thunisoft.sswy.mobile.logic.response.SdResponse;
import com.thunisoft.sswy.mobile.pojo.TSd;
import com.thunisoft.sswy.mobile.pojo.TSdWrit;
import com.thunisoft.sswy.mobile.util.EncryptionUtils;
import com.thunisoft.sswy.mobile.util.FileUtils;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.ZipUtils;

@EFragment(R.layout.fragment_sd_list)
public class SdListFragment extends BaseFragment implements ILoadMoreListener, IOnWsqsListener,IOnCkxqListener {

    @ViewById(R.id.list_sd)
    PullToRefreshListView list_sd;

    SdListAdapter adapter;

    List<TSd> datas = new ArrayList<TSd>();

    @ViewById(R.id.btn_load_more)
    Button btn_load_more;

    EditText txtSearch;
    
    
    
    @ViewById(R.id.null_page)
    LinearLayout nullPage;

    @Bean
    SdLogic sdLogic;

    View search_commt;

    View search_text_del;
    
    LinearLayout add_case;
    
    View currentItemView;
    String currentSdId;
    
    Animation currentAnimation;

    int page = 1;
    int rows = Constants.DZSD_LIST_PAGE_SIZE;
    String scope = Constants.SCOPE_DZSD_WQS;
    
    @Bean
    ZipUtils zipUitls;

    @Bean
    EncryptionUtils encryptionUtils;
    
    IWaitingDialogNotifier waitingDialogNotifier;

    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public void setWaitingDialogNotifier(IWaitingDialogNotifier waitingDialogNotifier) {
        this.waitingDialogNotifier = waitingDialogNotifier;
    }


    @AfterViews
    public void initViews() {
        search_commt = getActivity().findViewById(R.id.search_commt);
        search_commt.setOnClickListener(this);
        search_text_del = getActivity().findViewById(R.id.search_text_del);
        search_text_del.setOnClickListener(this);
        add_case = (LinearLayout) getActivity().findViewById(R.id.add_case);
        txtSearch = (EditText) getActivity().findViewById(R.id.txtSearch);
        
        
        
        adapter = new SdListAdapter(getActivity(), R.layout.standard_list_item_multiple, datas, scope);
        adapter.setLoadMoreListener(this);
        adapter.setOnWsqsListener(this);
        adapter.setOnCkxqListener(this);
        list_sd.setAdapter(adapter);
        list_sd.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                search();
            }
        });
        search();
    }
    
    
    @Click(R.id.add_case)
    public void addBtnClick(){
    	Intent intent = new Intent(getActivity(),AddCaseActivity_.class);
    	getActivity().startActivity(intent);
    }
    
    @UiThread
    public void showDialog(String text) {
        waitingDialogNotifier.showDialog(text);
    }
    
    @UiThread
    public void dismissDialog() {
        waitingDialogNotifier.dismissDialog();
    }

    public void search() {
        String text = txtSearch.getText().toString();
        waitingDialogNotifier.showDialog("正在加载...");
        loadDatas(text);
    }

    @Background
    public void doQs(String sdId) {
        SdResponse sr = sdLogic.downWritByUser(sdId, 0L);
        if (!sr.isSuccess()) {
            showToast(sr.getMessage());
            stopQs();
        } else {
            TSd sd = sr.getWrit();
            String zipString = sr.getZip();
            String dir = FileUtils.BASE_DIR + "/dzsd/writ/" + sd.getCId();
            List<Map<String, String>> pathList = zipUitls.unzipFile(new File(Environment.getExternalStorageDirectory(),
                    dir), encryptionUtils.decryptBase64ToBytes(zipString));
            if (pathList == null || pathList.isEmpty()) {
                stopQs();
                showToast("解压文件失败");
            } else {
                Long qssj = sd.getDQssj();
                if (qssj != null) {
                    SdResponse srUpdate = sdLogic.downWritByUser(sdId, qssj.longValue());
                    if (!srUpdate.isSuccess()) {
                        stopQs();
                        showToast(sr.getMessage());
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
                        sdLogic.saveSdWrit(writList);
                        onQsComplete(sd);
                    }
                }
            }
        }
    }

    @UiThread
    public void onQsComplete(TSd sd) {
        waitingDialogNotifier.dismissDialog();
        goXq(sd.getCId());
    }
    
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == View.VISIBLE){
				list_sd.setVisibility(View.GONE);
				nullPage.setVisibility(View.VISIBLE);
				
			}else{
				nullPage.setVisibility(View.GONE); 	
				list_sd.setVisibility(View.VISIBLE);
				
			}
		}
	};
    

    @Background
    public void loadDatas(String searchValue) {
        SdResponse sr = sdLogic.loadSdList(page, rows, scope, searchValue);
        if (!sr.isSuccess()) {
            stopRefresh();
            dismissDialog();
            showToast(sr.getMessage());
            Message msg = new Message();
            msg.what = View.GONE;
            handler.sendMessage(msg);
//            getActivity().findViewById(R.id.null_page).setVisibility(View.VISIBLE);  
        } else {
            List<TSd> sdList = sr.getSdList();
            Message msg = new Message();
            
            if(sdList.size()==0 && scope == Constants.SCOPE_DZSD_WQS){
            	msg.what = View.VISIBLE;           	
            }else{
            	msg.what = View.GONE;
            }
            handler.sendMessage(msg);
            notifyDataSetChanged(sdList);
        }
    }

    @UiThread
    public void stopRefresh() {
        list_sd.onRefreshComplete();
    }

    @UiThread
    public void notifyDataSetChanged(List<TSd> sdList) {
        waitingDialogNotifier.dismissDialog();
        if (sdList != null) {
            if (page == 1) {
                datas.clear();
            }
            datas.remove(null);
            datas.addAll(sdList);
            if (sdList.size() == rows) {
                page++;
                datas.add(null);
            }
            adapter.notifyDataSetChanged();
        }
        list_sd.onRefreshComplete();
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_CODE_SHOW_SD_WRIT_LIST_AFTER_DOWNLOAD && resultCode == Activity.RESULT_OK) {
            waitingDialogNotifier.dismissDialog();
            search();
        } else if(requestCode == Constants.REQUEST_CODE_QSWS && resultCode == Activity.RESULT_OK) {
            startLoadingAnim(currentItemView);
            waitingDialogNotifier.showDialog(scope == Constants.SCOPE_DZSD_WQS ? "签收中..." : "下载中...");
            doQs(currentSdId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.search_text_del:
            txtSearch.setText("");
            break;
        case R.id.search_commt:
            if (!this.isHidden()) {
                Log.e(getTag(), "searchCommt"+scope);
                page = 1;
                search();
            }
            break;
        case R.id.add_case:
        	Intent intent = new Intent(getActivity(),AddCaseActivity_.class);
        	getActivity().startActivity(intent);
        	break;
        }
        super.onClick(v);
    }

    @UiThread
    public void showNomoreBtn() {
        btn_load_more.setVisibility(View.VISIBLE);
    }

    @UiThread
    public void hideNomoreBtn() {
        btn_load_more.setVisibility(View.GONE);
    }

    @UiThread
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadMore() {
        search();
    }

    @Override
    public void qs(String sdId, View view) {
        currentSdId = sdId;
        currentItemView = view;
        Intent intent = new Intent(getActivity(), ConfirmDialogActivity_.class);
        intent.putExtra("message", "请确认签收，您签收后，将视为文书送达成功，系统会自动给法院反馈电子送达回证");
        startActivityForResult(intent, Constants.REQUEST_CODE_QSWS);
        /*startLoadingAnim(view);
        doQs(sdId);*/
    }
    
    @SuppressLint("NewApi")
    @UiThread
    public void stopQs() {
        currentAnimation.cancel();
        TextView textView = (TextView) currentItemView.findViewById(R.id.text_right);
        textView.setText(scope == Constants.SCOPE_DZSD_WQS ? "签收" : "查看");
        ImageView img = (ImageView) currentItemView.findViewById(R.id.img_right);
        img.setImageResource(R.drawable.icon_xq);
        waitingDialogNotifier.dismissDialog();
    }

    private void startLoadingAnim(View view) {
        
        TextView textView = (TextView) view.findViewById(R.id.text_right);
        textView.setText(scope == Constants.SCOPE_DZSD_WQS ? "签收中..." : "下载中...");
        ImageView img = (ImageView) view.findViewById(R.id.img_right);
        
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.loading);
        currentAnimation = animation;
        animation.setRepeatCount(200);
        img.setImageResource(R.drawable.loading);
        img.setAnimation(animation);
        animation.start();
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            search_commt.setOnClickListener(this);
            search_text_del.setOnClickListener(this);
        }
    }
    
    public boolean checkWsExists(String sdId) {
        List<TSdWrit> writList = sdLogic.getSdWritFromDb(sdId);
        if(writList == null || writList.isEmpty()) {
            return false;
        }
        for(TSdWrit writ : writList) {
            if(StringUtils.isBlank(writ.getCPath())) {
                return false;
            }
            File file = new File(writ.getCPath());
            if(!file.exists() || !file.isFile()) {
                return false;
            }
        }
        return true;
    }
    
    public void goXq(String sdId) {
        Intent intent = new Intent(getActivity(), WritListActivity_.class);
        intent.putExtra("CId", sdId);
        startActivityForResult(intent, Constants.REQUEST_CODE_SHOW_SD_WRIT_LIST_AFTER_DOWNLOAD);
    }

    /**
     * 点击查看按钮
     */
    @Override
    public void ckxq(String sdId , View view) {
        waitingDialogNotifier.showDialog(scope == Constants.SCOPE_DZSD_WQS ? "签收中..." : "下载中...");
        if(checkWsExists(sdId)) {
            goXq(sdId);
        } else {
            currentItemView = view;
            startLoadingAnim(view);
            // waitingDialogNotifier.showDialog(scope == Constants.SCOPE_DZSD_WQS ? "签收中..." : "下载中...");
            doQs(sdId);
        }
    }

}
