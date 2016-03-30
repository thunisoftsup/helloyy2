package com.thunisoft.sswy.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.pojo.TCourt;
import com.thunisoft.sswy.mobile.view.CheckableListItemLayout;

public class CourtListAdapter extends ArrayAdapter<TCourt> {
    List<TCourt> objects;
    LayoutInflater inflater;
    int resource;
    //Integer selectedPosition;
    String cid;
    
    public CourtListAdapter(Context context, int resource, List<TCourt> objects) {
        super(context, resource, objects);
        this.objects = objects;
        inflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(resource, null);
        } else {
            view = convertView;
        }

        TextView tv = (TextView) view.findViewById(R.id.tv_court_name);
        if (objects.get(position) == null) {
            tv.setText("");
            view.findViewById(R.id.listdividor).setVisibility(View.GONE);
            view.setBackgroundColor(Color.parseColor("#f3f3f3"));
            return view;
        } else {
        	view.findViewById(R.id.listdividor).setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.drawable.court_list_item_bg);
            tv.setText(objects.get(position).getCName());
        }
        
        CheckableListItemLayout layout = (CheckableListItemLayout) view;
        layout.setParentViewGroup(parent);
        if (cid != null && cid.equals(objects.get(position).getCId())) {
        	layout.setChecked(true);
        } else {
        	layout.setChecked(false);
        }
        return view;
    }

}
