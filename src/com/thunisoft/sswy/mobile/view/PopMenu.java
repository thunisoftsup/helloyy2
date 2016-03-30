package com.thunisoft.sswy.mobile.view;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.util.DensityUtil;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;

public class PopMenu {
	private List<Map<String, String>> itemList;
	private Context context;
	private PopupWindow popupWindow ;
	private ListView listView;
	//private OnItemClickListener listener;
	

	public PopMenu(Context context, List<Map<String, String>> itemList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.itemList = itemList;
		View view = LayoutInflater.from(context).inflate(R.layout.pop_menu_list, null);
        //设置 listview
        listView = (ListView)view.findViewById(R.id.pop_menu_list);
        listView.setAdapter(new PopAdapter());
        listView.setFocusableInTouchMode(true);
        listView.setFocusable(true);
        int width = context.getResources().getDimensionPixelSize(R.dimen.wslacx_status_width);
        popupWindow = new PopupWindow(view, width, LayoutParams.WRAP_CONTENT);
        
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	//设置菜单项点击监听器
	public void setOnItemClickListener(OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
	}

	//下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		//保证尺寸是根据屏幕像素密度来的
		popupWindow.showAsDropDown(parent, 0, -6);
		// 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //刷新状态
        popupWindow.update();
	}
	
	//隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	// 适配器
	private final class PopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.pop_menu_item, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
				holder.groupItem = (TextView) convertView.findViewById(R.id.pop_menu_item_name);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Map<String, String> itemMap = itemList.get(position);
			holder.groupItem.setText(itemMap.get(NrcUtils.KEY_NAME));
			return convertView;
		}

		private final class ViewHolder {
			TextView groupItem;
		}
	}
}
