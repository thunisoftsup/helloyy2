package com.thunisoft.sswy.mobile.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.interfaces.ILoadMoreListener;
import com.thunisoft.sswy.mobile.interfaces.IOnCkxqListener;
import com.thunisoft.sswy.mobile.interfaces.IOnWsqsListener;
import com.thunisoft.sswy.mobile.pojo.TSd;

/**
 * 律师认证记录Adapter
 * 
 * @author lulg
 * 
 */
public class SdListAdapter extends ArrayAdapter<TSd> {
    List<TSd> datas;
    LayoutInflater inflater;
    int resource;
    SimpleDateFormat df = null;
    ILoadMoreListener loadMoreListener;
    IOnWsqsListener onWsqsListener;
    IOnCkxqListener onCkxqListener;
    String scope;

    public SdListAdapter(Context context, int resource, List<TSd> datas,String scope) {
        super(context, resource);
        this.datas = datas;
        inflater = LayoutInflater.from(context);
        this.resource = resource;
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        this.scope = scope;
    }
    
    public void setLoadMoreListener(ILoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setOnWsqsListener(IOnWsqsListener onWsqsListener) {
        this.onWsqsListener = onWsqsListener;
    }

    public void setOnCkxqListener(IOnCkxqListener onCkxqListener) {
        this.onCkxqListener = onCkxqListener;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (datas.get(position) == null) {
            view = inflater.inflate(R.layout.loadmore_list_item, null);
            Button loadMoreButton = (Button) view.findViewById(R.id.btn_load_more);
            loadMoreButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreListener.loadMore();
                }
            });
        } else {
            view = inflater.inflate(resource, null);
        }
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_left = (TextView) view.findViewById(R.id.tv_left);
        TextView tv_right = (TextView) view.findViewById(R.id.tv_right);
        final TSd sd = datas.get(position);
        if (tv_title != null) {
            tv_title.setText(sd.getCAh());
            tv_left.setText(sd.getCWritName());

            if (sd.getNStatus() != null && sd.getNStatus().equals(Constants.SCOPE_DZSD_WQS) && sd.getDFssj() != null) {
                tv_right.setText(df.format(new Date(sd.getDFssj())));
            } else if (sd.getDQssj() != null) {
                tv_right.setText(df.format(new Date(sd.getDQssj())));
            }
        }
        
        View layout_btn_right = view.findViewById(R.id.layout_btn_right);
        TextView text_right = (TextView)view.findViewById(R.id.text_right);
        if(layout_btn_right != null) {
            if(Constants.SCOPE_DZSD_WQS.equals(scope)) {
                
                text_right.setText("签收");
                layout_btn_right.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onWsqsListener != null) {
                            onWsqsListener.qs(sd.getCId(), v);
                        }
                    }
                });
            } else {
                text_right.setText("查看");
                layout_btn_right.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(onCkxqListener != null) {
                           onCkxqListener.ckxq(sd.getCId(), v);
                       }
                    }
                });
            }
        }
        return view;
    }

}
