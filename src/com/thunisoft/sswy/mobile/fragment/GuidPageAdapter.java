package com.thunisoft.sswy.mobile.fragment;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.util.FileUtils;

public class GuidPageAdapter extends PagerAdapter{
    
    List<Map<String, Integer>> datas;
    LayoutInflater inflater;
    
    /**
     * 要显示的中间图片
     */
    public static final String K_IMG_R_ID = "imgRId";
    
    /**
     * 图片背景
     */
    public static final String K_BG_R_ID = "bgRId";
    
    private Resources res;
    
    public GuidPageAdapter (Context context, List<Map<String, Integer>> datas) {
       this.datas = datas;
       inflater = LayoutInflater.from(context);
       res = context.getResources();
    }
    
    @Override
    public int getCount() {
        return datas.size();
    }
    
    public void addDatas(List<Bitmap> newDatas) {
        
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.guidpage_item, container, false);
        FrameLayout bgFL = (FrameLayout)view.findViewById(R.id.guidpage_bg);
        ImageView imageView = (ImageView)view.findViewById(R.id.guidpage_image_item);
        Map<String, Integer> map = datas.get(position);
        bgFL.setBackgroundDrawable(res.getDrawable(map.get(K_BG_R_ID)));
        Drawable imgDrw = res.getDrawable(map.get(K_IMG_R_ID));
        Bitmap imgBmp = FileUtils.drawableToBitamp(imgDrw);
        imageView.setImageBitmap(imgBmp);
        container.addView(view);
        return view;
    }
    
    

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg1 == arg0;
    }

}
