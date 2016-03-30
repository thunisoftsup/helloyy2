package com.thunisoft.sswy.mobile.activity.dialog.nrc;

import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

/**
 * 性别 列表页面
 * 
 * @author gewx
 * 
 */
@EActivity(R.layout.dialog_gender)
public class GenderDialogActivity extends Activity {

	@ViewById(R.id.dialog_gender_list)
	protected ListView genderListView;

	private GenderAdapter genderAdapter;

	/**
	 * 性别类型 intent key
	 */
	public static final String K_GENDER_TYPE = "genderType";

	/**
	 * 性别类型
	 */
	private int genderType;

	private ColorStateList selectedColor;

	private ColorStateList normalColor;

	@AfterViews
	public void onAfterViews() {
		Resources res = this.getResources();
    	selectedColor = (ColorStateList) res.getColorStateList(R.color.nrc_dialog_item_selected_color);
    	normalColor = (ColorStateList) res.getColorStateList(R.color.nrc_dialog_item_normal_color);
		setFinishOnTouchOutside(true);// 设置为true点击区域外消失
		Intent intent = getIntent();
		genderType = intent.getIntExtra(K_GENDER_TYPE, Constants.GENDER_MAN);
		if (null == genderAdapter) {
			genderAdapter = new GenderAdapter(this, NrcUtils.getGenderTypeList());
			genderListView.setAdapter(genderAdapter);
		} else {
			genderAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 性别 adapter
	 * 
	 * @author gewx
	 *
	 */
	private class GenderAdapter extends BaseAdapter {

		private Context context;

		private List<Map<String, String>> genderMapList;

		public GenderAdapter(Context context, List<Map<String, String>> genderMapList) {
			this.context = context;
			this.genderMapList = genderMapList;
		}

		@Override
		public int getCount() {
			return genderMapList.size();
		}

		@Override
		public Object getItem(int position) {
			return genderMapList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;
			if (null == convertView) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.dialog_agent_type_item, null);
				viewHolder.genderNameTV = (TextView) convertView.findViewById(R.id.dialog_agent_type_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Map<String, String> genderMap = genderMapList.get(position);
			String genderName = genderMap.get(NrcUtils.KEY_NAME);
			viewHolder.genderNameTV.setText(genderName);
			int currGenderType = Integer.parseInt(genderMap.get(NrcUtils.KEY_CODE));
			if (currGenderType == genderType) {
				viewHolder.genderNameTV.setTextColor(selectedColor);
			} else {
				viewHolder.genderNameTV.setTextColor(normalColor);
			}
			ItemOnClick itemOnClick = new ItemOnClick();
			itemOnClick.position = position;
			convertView.setOnClickListener(itemOnClick);
			return convertView;
		}

		class ItemOnClick implements OnClickListener {

			public int position;

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Map<String, String> genderMap = genderMapList.get(position);
				int currGenderType = Integer.parseInt(genderMap.get(NrcUtils.KEY_CODE));
				intent.putExtra(K_GENDER_TYPE, currGenderType);
				GenderDialogActivity.this.setResult(Constants.RESULT_OK, intent);
				GenderDialogActivity.this.finish();
			}
		}

		class ViewHolder {

			/**
			 * 性别 名称
			 */
			TextView genderNameTV;
		}
	}
}
