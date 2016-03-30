package com.thunisoft.sswy.mobile.activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;

public class WaittingDialog extends Dialog {
	ImageView animWaitImg;
	TextView waitInfo;
	private AnimationDrawable animationDrawable;
	public interface AbordTaskLsn {
		public void onTaskAbord();
	}
	private AbordTaskLsn listenner;
	boolean isCanclable;
	
	public WaittingDialog(Context context, String txt) {
		super(context);
		init(context, txt);
	}

	public WaittingDialog(Context context, int theme, String txt) {
		super(context, theme);
		init(context, txt);
	}
	
	private void init(final Context context, String txt) {
		isCanclable = true;
		setContentView(R.layout.dialog_waitting);
		waitInfo = (TextView) findViewById(R.id.waiting_info);
		animWaitImg = (ImageView) findViewById(R.id.wait_anim_img);
		waitInfo.setText(txt);
		animWaitImg.setImageResource(R.drawable.anim_sys_waitting);
		animationDrawable = (AnimationDrawable) animWaitImg.getDrawable(); 
		animationDrawable.start();  
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		animationDrawable.stop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isCanclable) return super.onTouchEvent(event);
		dismiss();
		if(listenner != null) {
		    listenner.onTaskAbord();
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		if (!isCanclable) return;
		super.onBackPressed();
		if(listenner != null) {
		    listenner.onTaskAbord();
		}
	}
	
	public void setAbordTaskLsn(AbordTaskLsn listenner) {
		this.listenner = listenner;
	}
	
	public void setWaittingTxt(String txt) {
		waitInfo.setText(txt);
	}
	
	public void setIsCanclable(boolean isCanclable) {
		this.isCanclable = isCanclable;
	}
}
