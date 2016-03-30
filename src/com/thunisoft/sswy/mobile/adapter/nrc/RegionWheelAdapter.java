package com.thunisoft.sswy.mobile.adapter.nrc;

import java.util.List;

import android.content.Context;

import com.thunisoft.sswy.mobile.pojo.TRegion;
import com.widget.wheel.adapter.AbstractWheelTextAdapter;

/**
 * 行政地区
 */
public class RegionWheelAdapter extends AbstractWheelTextAdapter {

	private List<TRegion> regionList;
	
	public RegionWheelAdapter(Context context, List<TRegion> regionList) {
		super(context);
		this.regionList = regionList;
	}

	@Override
	public int getItemsCount() {
		return regionList.size();
	}

	@Override
	protected CharSequence getItemText(int index) {
		return regionList.get(index).getCName();
	}

}
