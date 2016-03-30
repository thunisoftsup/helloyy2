package com.thunisoft.sswy.mobile.activity.transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.HomeActivity_;
import com.thunisoft.sswy.mobile.cache.CourtCache;
import com.thunisoft.sswy.mobile.fragment.GuidPageAdapter;
import com.thunisoft.sswy.mobile.util.ConfigUtils_;
import com.thunisoft.sswy.mobile.util.FileUtils;

/**
 * 引导页
 * 
 * @author lulg
 * 
 */
@EActivity(R.layout.activity_guidpage)
public class GuidPageActivity extends Activity {
	private static final String TAG = "GuidPageActivity";
	@Pref
	ConfigUtils_ configUtils;

	@ViewById(R.id.pager)
	ViewPager viewPager;
	GuidPageAdapter pageAdapter;
	ViewGroup mIndicator;
	List<Map<String, Integer>> datas = new ArrayList<Map<String, Integer>>();

	@Extra("firstTime")
	boolean firstTime;

	String courtId;

	@Bean
	FileUtils fileUtils;

	@Bean
	CourtCache courtCache;

	boolean misScrolled;
	LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		courtId = courtCache.getCourtId();
		configUtils.getSharedPreferences().edit().putBoolean("ydtp_showed", true);
	}

	@AfterViews
	public void initViews() {
		pageAdapter = new GuidPageAdapter(GuidPageActivity.this, datas);
		viewPager.setAdapter(pageAdapter);
		mIndicator = (ViewGroup) findViewById(R.id.indicator);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					misScrolled = false;
					break;
				case ViewPager.SCROLL_STATE_SETTLING:
					misScrolled = true;
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					int whichPage = viewPager.getCurrentItem();
					if (viewPager.getCurrentItem() == (viewPager.getAdapter().getCount() - 1) && !misScrolled) {
						Intent intent = new Intent(GuidPageActivity.this, HomeActivity_.class);
						startActivity(intent);
						finish();
					}
					scrollIndexs[prePageCheck].setImageResource(R.drawable.scroll_indicator_normal);
					scrollIndexs[whichPage].setImageResource(R.drawable.scroll_indicator_checked);
					prePageCheck = whichPage;
				}

			}
		});
		loadBitMaps();
	}

	private ImageView[] scrollIndexs;
	int prePageCheck;

	private void getFaceElemntLayout() {
		int facePage = datas.size();
		scrollIndexs = new ImageView[facePage];
		for (int i = 0; i < facePage; i++) {
			scrollIndexs[i] = (ImageView) inflater.inflate(R.layout.cell_scrl_index, null);
			mIndicator.addView(scrollIndexs[i]);
			if (i == 0) {
				scrollIndexs[i].setImageResource(R.drawable.scroll_indicator_checked);
				prePageCheck = 0;
			}
		}
	}

	@Background
	public void loadBitMaps() {
		List<Map<String, Integer>> guidpageIdList = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> guidpaga1 = new HashMap<String, Integer>();
		guidpaga1.put(GuidPageAdapter.K_IMG_R_ID, R.drawable.guidpage1);
		guidpaga1.put(GuidPageAdapter.K_BG_R_ID, R.drawable.guidpage_bg1);
		guidpageIdList.add(guidpaga1);

		Map<String, Integer> guidpaga2 = new HashMap<String, Integer>();
		guidpaga2.put(GuidPageAdapter.K_IMG_R_ID, R.drawable.guidpage2);
		guidpaga2.put(GuidPageAdapter.K_BG_R_ID, R.drawable.guidpage_bg2);
		guidpageIdList.add(guidpaga2);

		Map<String, Integer> guidpaga3 = new HashMap<String, Integer>();
		guidpaga3.put(GuidPageAdapter.K_IMG_R_ID, R.drawable.guidpage3);
		guidpaga3.put(GuidPageAdapter.K_BG_R_ID, R.drawable.guidpage_bg3);
		guidpageIdList.add(guidpaga3);
		notifyDataChanged(guidpageIdList);
	}

	@UiThread
	public void notifyDataChanged(List<Map<String, Integer>> guidpageIdList) {
		datas.clear();
		datas.addAll(guidpageIdList);
		pageAdapter.notifyDataSetChanged();
		getFaceElemntLayout();
	}

	/*
	 * private void loadLocalBitmaps(File externalPath, List<Bitmap> bitList,
	 * Set<String> pathSet) { for (String path : pathSet) { File file = new
	 * File(externalPath, path); if (file.exists()) { FileInputStream is = null;
	 * try { is = new FileInputStream(file); Bitmap bit =
	 * BitmapFactory.decodeStream(is); // bit = scaleBitMap(bit);
	 * bitList.add(bit); } catch (FileNotFoundException e) { Log.e("TAG", "",
	 * e); } finally { fileUtils.closeQuietly(is); } } } datas.clear();
	 * datas.addAll(bitList); }
	 */
}
