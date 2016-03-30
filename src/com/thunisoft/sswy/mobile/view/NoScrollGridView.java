package com.thunisoft.sswy.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 增加无滚动条的gridView
 * @author gewx
 *
 */
public class NoScrollGridView extends GridView{
	 public NoScrollGridView(Context context) {  
	        super(context);  
	    }  
	  
	    public NoScrollGridView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    }  
	  
	    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle);  
	    }  
	  
	    @Override  
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,        
	                MeasureSpec.AT_MOST);  
	        super.onMeasure(widthMeasureSpec, expandSpec);  
	    }  
	  
	    @Override  
	    public boolean dispatchTouchEvent(MotionEvent ev) {  
	        if(ev.getAction() == MotionEvent.ACTION_MOVE){     
	            return true;   
	        }   
	        return super.dispatchTouchEvent(ev);  
	    }  
}
