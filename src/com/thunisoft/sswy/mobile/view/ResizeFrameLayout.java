package com.thunisoft.sswy.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 正方形的frameLayout
 * @author gewx
 *
 */
public class ResizeFrameLayout extends FrameLayout {

	public ResizeFrameLayout(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 
  
    public ResizeFrameLayout(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 
  
    public ResizeFrameLayout(Context context) { 
        super(context); 
    } 
  
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec)); 
  
        int childWidthSize = getMeasuredWidth(); 
        int childHeightSize = (int)(childWidthSize*0.8); 
        //高度和宽度一样 
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); 
    } 

}
