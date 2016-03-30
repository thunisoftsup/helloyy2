package com.thunisoft.sswy.mobile.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.auth.LsrzActivity;
import com.thunisoft.sswy.mobile.activity.auth.LsrzActivity_;
import com.thunisoft.sswy.mobile.pojo.LsrzJl;

/**
 * 律师认证记录Adapter
 * @author lulg
 *
 */
public class LsrzjlAdapter extends ArrayAdapter<LsrzJl> {
    List<LsrzJl> datas;
    LayoutInflater inflater;
    int resource;
    SimpleDateFormat df = null;
    Context context;
    
    public LsrzjlAdapter(Context context, int resource, List<LsrzJl> datas) {
        super(context, resource);
        this.datas = datas;
        inflater = LayoutInflater.from(context);
        this.resource = resource;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
        this.context = context;
    }

    
    
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        //if (convertView == null) {
        if (datas.get(position) == null && position == datas.size()-1) {
            view = inflater.inflate(R.layout.rzbg_list_item, null);
            view.findViewById(R.id.btn_rzbg).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itt = new Intent();
                    itt.setClass(context, LsrzActivity_.class);
                    itt.putExtra("change", "YES");
                    context.startActivity(itt);
                }
            });
            return view;
        }
        view = inflater.inflate(resource, null);
//        } else {
//            view = convertView;
//        }

        TextView tv_zyzh = (TextView) view.findViewById(R.id.tv_value_zyzh);
        TextView tv_rzsj = (TextView) view.findViewById(R.id.tv_value_rzsj);
        LsrzJl rzjl = datas.get(position);
        tv_zyzh.setText(rzjl.getZyzh());
        if (rzjl.getRzsj() != null) {
            if(rzjl.getRzsj() == 0L) {
                view.findViewById(R.id.layout_rzsj).setVisibility(View.GONE);
            } else {
                tv_rzsj.setText(df.format(new Date(rzjl.getRzsj())));
            }
            
        }

        return view;
    }

}
