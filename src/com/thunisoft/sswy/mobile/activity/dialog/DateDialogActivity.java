package com.thunisoft.sswy.mobile.activity.dialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.adapter.nrc.StrericWheelAdapter;
import com.widget.wheel.listener.OnWheelChangedListener;
import com.widget.wheel.view.WheelView;

/**
 * 日期选择器
 * 
 * @author gewx
 *
 */
@EActivity(R.layout.dialog_wheel_date)
public class DateDialogActivity extends Activity {

	/**
	 * 标题
	 */
	@ViewById(R.id.dialog_wheel_date_title)
	protected TextView titleTV;
	
	/**
	 * 年份
	 */
	@ViewById(R.id.dialog_wheel_date_year)
	protected WheelView yearView;

	/**
	 * 月份
	 */
	@ViewById(R.id.dialog_wheel_date_month)
	protected WheelView monthView;

	/**
	 * 日
	 */
	@ViewById(R.id.dialog_wheel_date_day)
	protected WheelView dayView;
	
	public static final String K_DATE = "date";
	
	private static final int MIN_YEAR = 1900; // 最小年份
	
	@SuppressLint("UseSparseArrays")
	private Map<Integer, Integer> monthDays = new HashMap<Integer, Integer>();

	private List<String> years = new ArrayList<String>();

	private List<String> months = new ArrayList<String>();

	private List<String> days = new ArrayList<String>();

	private Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		calendar = (Calendar)intent.getSerializableExtra(K_DATE);
		monthDays.put(1, 31);
		monthDays.put(2, 28);
		monthDays.put(3, 31);
		monthDays.put(4, 30);
		monthDays.put(5, 31);
		monthDays.put(6, 30);
		monthDays.put(7, 31);
		monthDays.put(8, 31);
		monthDays.put(9, 30);
		monthDays.put(10, 31);
		monthDays.put(11, 30);
		monthDays.put(12, 31);
		
	}
	
	@AfterViews
	protected void onAfterViews() {
		initViewsData();
	}
	
	/**
	 * 选择日期，确认
	 */
	@Click(R.id.dialog_wheel_date_sure)
	protected void clickSureBtn() {
		String year = years.get(yearView.getCurrentItem());
		calendar.set(Calendar.YEAR,Integer.parseInt(year));
		String month = months.get(monthView.getCurrentItem());
		calendar.set(Calendar.MONTH,Integer.parseInt(month)-1);
		String day = days.get(dayView.getCurrentItem());
		calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day));
		
		Intent intent = new Intent();
		intent.putExtra(K_DATE, calendar);
		setResult(Constants.RESULT_OK, intent);
		this.finish();
	}
	
	/**
	 * 选择日期，取消
	 */
	@Click(R.id.dialog_wheel_date_cancel)
	protected void clickCancelBtn() {
		this.finish();
	}
	
	private void initViewsData() {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		initYear(year);
		initMonth(month);
		initDay(year, month, day);
		titleTV.setText(getSelectDateTimeStr());
	}

	private void initYear(int year) {
		years.clear();
		Calendar c = Calendar.getInstance();
		int nowYear = c.get(Calendar.YEAR);
		// zhiyu 2015-06-19 年上限上提10年
		for (int i = MIN_YEAR; i <= (nowYear + 10); i++) {
			years.add(String.valueOf(i));
		}

		yearView.setViewAdapter(new StrericWheelAdapter(this, years));
		yearView.setCurrentItem(year - MIN_YEAR);
		yearView.setCyclic(true);
		yearView.setInterpolator(new AnticipateOvershootInterpolator());
		yearView.addChangingListener(new YearChangedListener());

	}

	private void initMonth(int month) {
		months.clear();
		for (int i = 1; i <= 12; i++) {
			months.add(String.valueOf(i));
		}

		monthView.setViewAdapter(new StrericWheelAdapter(this, months));
		monthView.setCurrentItem(month - 1);
		monthView.setCyclic(true);
		monthView.setInterpolator(new AnticipateOvershootInterpolator());
		monthView.addChangingListener(new MonthChangedListener());
	}

	private void initDay(int year, int month, int day) {
		int endDay = monthDays.get(month);
		if (2 == month) {
			Calendar calendar = Calendar.getInstance();
			boolean isleapYear = ((GregorianCalendar) calendar)
					.isLeapYear(year);
			if (isleapYear) {
				endDay = 29;
			}
		}

		days.clear();
		for (int i = 1; i <= endDay; i++) {
			days.add(String.valueOf(i));
		}

		if (day > days.size()) {
			day = days.size();
		}

		dayView.setViewAdapter(new StrericWheelAdapter(this, days));
		dayView.setCurrentItem(day - 1);
		dayView.setCyclic(true);
		dayView.setInterpolator(new AnticipateOvershootInterpolator());
		dayView.addChangingListener(new DayChangedListener());
	}

	/**
	 * 年份改变时监听
	 * 
	 * @author gewx
	 *
	 */
	class YearChangedListener implements OnWheelChangedListener {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (oldValue != newValue) {
				String monthStr = months.get(monthView.getCurrentItem());
				int month = Integer.parseInt(monthStr);
				String dayStr = days.get(dayView.getCurrentItem());
				int day = Integer.parseInt(dayStr);
				initDay(MIN_YEAR + newValue, month, day);
			}
			titleTV.setText(getSelectDateTimeStr());
		}

	}

	/**
	 * 月份改变时监听
	 * 
	 * @author gewx
	 *
	 */
	class MonthChangedListener implements OnWheelChangedListener {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (oldValue != newValue) {
				String yearStr = years.get(yearView.getCurrentItem());
				int year = Integer.parseInt(yearStr);
				String dayStr = days.get(dayView.getCurrentItem());
				int day = Integer.parseInt(dayStr);
				initDay(year, newValue + 1, day);
			}
			titleTV.setText(getSelectDateTimeStr());
		}

	}

	/**
	 * 日期改变时监听
	 * 
	 * @author gewx
	 *
	 */
	class DayChangedListener implements OnWheelChangedListener {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (oldValue != newValue) {
				titleTV.setText(getSelectDateTimeStr());
			}
		}

	}

	/**
	 * 获取选择的时间
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public String getSelectDateTimeStr() {
		Calendar calendar = Calendar.getInstance();
		
		String yearStr = years.get(yearView.getCurrentItem());
		int year = Integer.parseInt(yearStr);
		String monthStr = months.get(monthView.getCurrentItem());
		int month = Integer.parseInt(monthStr);
		String dayStr = days.get(dayView.getCurrentItem());
		int day = Integer.parseInt(dayStr);
		
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(date);
	}

}
