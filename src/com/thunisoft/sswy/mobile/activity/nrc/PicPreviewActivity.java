package com.thunisoft.sswy.mobile.activity.nrc;

import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.photoselector.util.AnimationUtil;
import com.photoselector.view.PhotoPreview;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity;
import com.thunisoft.sswy.mobile.activity.dialog.AddPhotoDialogActivity_;
import com.thunisoft.sswy.mobile.model.PicModel;
import com.thunisoft.sswy.mobile.model.SerializableSOMap;
import com.thunisoft.sswy.mobile.pojo.TProUserSfrzCl;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;

/**
 * 原图预览
 * 
 * @author gewx
 *
 */
@EActivity(R.layout.activity_pic_preview)
public class PicPreviewActivity extends BaseActivity implements OnPageChangeListener {

	private static final String TAG = PicPreviewActivity.class.getSimpleName();

	/** 头部控件 */
	@ViewById(R.id.organ_pic_preview_header)
	protected RelativeLayout layoutTop;
	
	/** 底部控件 */
	@ViewById(R.id.organ_pic_preview_footer)
	protected RelativeLayout layoutFooter;

	/** 显示原图 */
	@ViewById(R.id.organ_pic_preview_view)
	protected ViewPager mViewPager;

	/** 预览图片index/图片总数 */
	@ViewById(R.id.organ_pic_preview_percent)
	protected TextView tvPercent;

	/**
	 * 是否显示百分比  intent key
	 */
	public static final String IS_SHOW_PERCENT = "isShowPercent";
	
	private boolean isShowPercent = true;
	
	/** 图片List */
	protected List<PicModel> picList;

	/** 图片List intent key */
	public static final String K_PIC_IST = "picList";

	/** 当前图片position */
	protected int current;

	/** 当前图片position intent key */
	public static final String K_CURR_POSITION = "currPosition";

	private static final int REQ_SELECT_FILE = 5;
	
	/**
	 * 打开方式
	 */
	public static final String K_OPEN_TYPE = "openType";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		overridePendingTransition(R.anim.activity_alpha_action_in, 0); // 渐入效果
	}

	@SuppressWarnings("unchecked")
	@AfterViews
	protected void onAfterViews() {
		mViewPager.setOnPageChangeListener(this);
		Intent intent = getIntent();
		picList = (List<PicModel>) intent.getSerializableExtra(K_PIC_IST);
		current = intent.getIntExtra(K_CURR_POSITION, 0);
		isShowPercent = intent.getBooleanExtra(IS_SHOW_PERCENT, true);
		int openType = intent.getIntExtra(K_OPEN_TYPE, NrcConstants.OPEN_TYPE_EDIT);
		if (NrcConstants.OPEN_TYPE_DISPLAY == openType) {
			layoutFooter.setVisibility(View.GONE);
		} else {
			layoutFooter.setVisibility(View.VISIBLE);
		}
		updatePercent();
		bindData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Constants.RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case REQ_SELECT_FILE:
			SerializableSOMap zmSoMap = (SerializableSOMap) data.getSerializableExtra(AddPhotoDialogActivity.K_SELECTED_PATH_MAP);
			Map<String, Object> zmMap = zmSoMap.getMap();
			if (null != zmMap && zmMap.size() > 0) {
				for (Map.Entry<String, Object> pathEntry : zmMap.entrySet()) {
					String path = pathEntry.getKey();
					PicModel currPicModel = picList.get(current);
					if (PicModel.TYPE_SFRZ_CL == currPicModel.getType()) {
						Map<Integer, TProUserSfrzCl> sfrzclMap = NrcEditData.getProUserSfrzclMap();
						if (null != sfrzclMap && sfrzclMap.size() > 0) {
							for (Map.Entry<Integer, TProUserSfrzCl> entry : sfrzclMap.entrySet()) {
								TProUserSfrzCl sfrzcl = entry.getValue();
								if (sfrzcl.getCBh().equals(currPicModel.getRelId())) {
									sfrzcl.setCClPath(path);
									sfrzcl.setNSync(NrcConstants.SYNC_FALSE);
									break;
								}
							}
						}
					} else if (PicModel.TYPE_SSCL_FJ_ZJ == currPicModel.getType()) {
						
					} else if (PicModel.TYPE_ZJCL == currPicModel.getType()) { // 证据没有重新上传，所以暂时不作处理

					}
					break;
				}
			}
			this.finish();
			break;

		default:
			break;
		}
	}

	/** 绑定数据，更新界面 */
	protected void bindData() {
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(current);
	}

	private PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public int getCount() {
			if (picList == null) {
				return 0;
			} else {
				return picList.size();
			}
		}

		@Override
		public View instantiateItem(final ViewGroup container, final int position) {
			PhotoPreview photoPreview = new PhotoPreview(getApplicationContext());
			PicModel picModel = picList.get(position);
			((ViewPager) container).addView(photoPreview);
			Bitmap bmp = photoPreview.getBitmapByPath(PicPreviewActivity.this, picModel.getPath());
			photoPreview.getIvContent().setImageBitmap(bmp);
			photoPreview.getPbLoading().setVisibility(View.INVISIBLE);
			photoPreview.setOnClickListener(photoItemClickListener);
			return photoPreview;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	};
	protected boolean isUp;

	@Click(R.id.organ_pic_preview_back)
	public void onClick(View v) {
		this.finish();
	}

	/**
	 * 点击重新上传
	 */
	@Click(R.id.organ_pic_preview_reupload)
	protected void clickReupload() {
		Intent intent = new Intent();
		intent.setClass(this, AddPhotoDialogActivity_.class);
		intent.putExtra(AddPhotoDialogActivity.K_SELECT_TYPE, NrcConstants.SELECT_TYPE_SINGLE);
		intent.putExtra(AddPhotoDialogActivity.K_FILE_TYPE, NrcConstants.FILE_TYPE_PIC);
		startActivityForResult(intent, REQ_SELECT_FILE);
	}

	/**
	 * 点击删除
	 */
	@Click(R.id.organ_pic_preview_delete)
	protected void clickDelete() {
		PicModel currPicModel = picList.get(current);
		if (PicModel.TYPE_SFRZ_CL == currPicModel.getType()) {
			int type = -1;
			Map<Integer, TProUserSfrzCl> sfrzclMap = NrcEditData.getProUserSfrzclMap();
			if (null != sfrzclMap && sfrzclMap.size() > 0) {
				for (Map.Entry<Integer, TProUserSfrzCl> entry : sfrzclMap.entrySet()) {
					TProUserSfrzCl sfrzcl = entry.getValue();
					if (sfrzcl.getCBh().equals(currPicModel.getRelId())) {
						type = entry.getKey();
						break;
					}
				}
				sfrzclMap.remove(type);
			}
		} else if (PicModel.TYPE_SSCL_FJ_ZJ == currPicModel.getType()) {
			
		} else if (PicModel.TYPE_ZJCL == currPicModel.getType()) { // 证据没有重新上传，所以暂时不作处理

		}
		this.finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		current = arg0;
		updatePercent();
	}

	protected void updatePercent() {
		if (isShowPercent) {
			tvPercent.setVisibility(View.VISIBLE);
			tvPercent.setText((current + 1) + "/" + picList.size());
		} else {
			tvPercent.setVisibility(View.GONE);
		}
	}

	/** 图片点击事件回调 */
	private OnClickListener photoItemClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isUp) {
				new AnimationUtil(getApplicationContext(), R.anim.translate_up).setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(layoutTop);
				if (View.VISIBLE == layoutFooter.getVisibility()) {
					new AnimationUtil(getApplicationContext(), R.anim.translate_down).setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(layoutFooter);
				}
				isUp = true;
			} else {
				new AnimationUtil(getApplicationContext(), R.anim.translate_down_current).setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(layoutTop);
				if (View.VISIBLE == layoutFooter.getVisibility()) { 
					new AnimationUtil(getApplicationContext(), R.anim.translate_up_current).setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(layoutFooter);
				}
				isUp = false;
			}
		}
	};
}
