package com.thunisoft.sswy.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.pojo.TProvince;

public class ProvinceAdapter extends ArrayAdapter<TProvince>{
    List<TProvince> objects;
    LayoutInflater inflater;
    int resource;
    String currFjm;
    View defaltCheckedVeiw;
    
    public ProvinceAdapter(Context context, int resource, List<TProvince> objects) {
        super(context, resource, objects);
        this.objects = objects;
        inflater = LayoutInflater.from(context);
        this.resource = resource;
    }
    
    public void setCurrFjm(String fym) {
        this.currFjm = fym;
    }
    
    public View getDefaltCheckedVeiw() {
        return defaltCheckedVeiw;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView == null) {
            view = inflater.inflate(resource, null);
        } else {
            view = convertView;
        }
        TextView tv = (TextView)view.findViewById(R.id.tv_province_name);
        if(objects.get(position) == null) {
            tv.setText("");
            return view;
        } else {
            String cName = objects.get(position).getCName();
            String Fjm = objects.get(position).getCFjm();
            tv.setText(cName);
            if (currFjm != null && currFjm.equals(Fjm)) {
                view.setBackgroundResource(R.color.province_item_sel);
                defaltCheckedVeiw = view;
            } else {
                view.setBackgroundResource(R.color.province_item_default);
            }
        }
        return view;
    }
    
    

}
