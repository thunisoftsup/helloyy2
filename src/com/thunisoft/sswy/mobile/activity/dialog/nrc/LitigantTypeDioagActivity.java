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
 * 当事人类型 列表页面
 * 
 * @author gewx
 * 
 */
@EActivity(R.layout.dialog_agent_type) //暂时和当事人类型共用一套xml
public class LitigantTypeDioagActivity extends Activity {

	/**当事人类型 列表*/
    @ViewById(R.id.dialog_agent_type_list)
    ListView litigantTypelistView;

    private LitigantTypeAdapter litigantTypeAdapter;
    
    /**当事人类型 code*/
    public static final String K_LITIGANT_TYPE_CODE = "code";
    
    private String litigantTypeCode;
    
    private ColorStateList selectedColor;
    
    private ColorStateList normalColor;
    
    @AfterViews
    public void onAfterViews() {
	    setFinishOnTouchOutside(true);// 设置为true点击区域外消失   
    	Intent intent = getIntent();
    	litigantTypeCode = intent.getStringExtra(K_LITIGANT_TYPE_CODE);
    	refreshLitigantType();
    	Resources res = this.getResources();
    	selectedColor = (ColorStateList) res.getColorStateList(R.color.nrc_dialog_item_selected_color);
    	normalColor = (ColorStateList) res.getColorStateList(R.color.nrc_dialog_item_normal_color);
    }
    
    /**
     * 刷新 当事人类型 列表
     */
    private void refreshLitigantType() {
    	if (null == litigantTypeAdapter) {
    		litigantTypeAdapter = new LitigantTypeAdapter(this, NrcUtils.getLitigantTypeList());
    		litigantTypelistView.setAdapter(litigantTypeAdapter);
    	} else {
    		litigantTypeAdapter.notifyDataSetChanged();
    	}
    }
    
    private class LitigantTypeAdapter extends BaseAdapter {

    	private Context context;
    	
    	private List<Map<String, String>> litigantTypeList;
    	
    	public LitigantTypeAdapter(Context context, List<Map<String, String>> litigantTypeList) {
    		
    		this.context = context;
    		this.litigantTypeList = litigantTypeList;
    	}
    	
		@Override
		public int getCount() {
			return litigantTypeList.size();
		}

		@Override
		public Object getItem(int position) {
			return litigantTypeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				LayoutInflater inflater = LayoutInflater.from(context);
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.dialog_agent_type_item, null); //暂时和代理人类型共用
				viewHolder.litigantTypeNameTV = (TextView)convertView.findViewById(R.id.dialog_agent_type_name); //暂时和代理人类型共用
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			Map<String, String> litigantTypeMap = litigantTypeList.get(position);
			viewHolder.litigantTypeNameTV.setText(litigantTypeMap.get(NrcUtils.KEY_NAME));
			if (litigantTypeMap.get(NrcUtils.KEY_CODE).equals(litigantTypeCode)) {
				viewHolder.litigantTypeNameTV.setTextColor(selectedColor);
			} else {
				viewHolder.litigantTypeNameTV.setTextColor(normalColor);
			}
			ItemOnClick itemOnClick = new ItemOnClick();
			itemOnClick.position = position;
			convertView.setOnClickListener(itemOnClick);
			return convertView;
		}
    	
		/**
		 * 当事人类型 点击选中
		 * @author gewx
		 *
		 */
		private class ItemOnClick implements OnClickListener {

	    	public int position;
	    	
			@Override
			public void onClick(View v) {
				Map<String, String> litigantTypeMap = litigantTypeList.get(position);
				Intent intent = new Intent();
				intent.putExtra(LitigantTypeDioagActivity.K_LITIGANT_TYPE_CODE, Integer.parseInt(litigantTypeMap.get(NrcUtils.KEY_CODE)));
				LitigantTypeDioagActivity.this.setResult(Constants.RESULT_OK, intent);
				LitigantTypeDioagActivity.this.finish();
			}
	    }
    }
    
    private static class ViewHolder {
		
		/**
		 * 当事人类型 名称
		 */
		TextView litigantTypeNameTV;
	}
}
