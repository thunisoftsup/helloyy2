package com.thunisoft.sswy.mobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.datasource.RegionDao;
import com.thunisoft.sswy.mobile.pojo.TRegion;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;
import com.thunisoft.sswy.mobile.view.HorizontalListView;

@EActivity(R.layout.activity_select_address)
public class SelectAddressActivity extends BaseActivity {

	/**
	 * 已选择地址
	 */
	@ViewById(R.id.select_address_selected)
	protected HorizontalListView selectedAddressLV;

	/**
	 * 地址ListView
	 */
	@ViewById(R.id.select_address_list)
	protected ListView AddressLV;

	/**
	 * 地址List adapter
	 */
	private AddressAdapter addressAdapter;

	/**
	 * 地址List
	 */
	private List<TRegion> addressList = new ArrayList<TRegion>();

	/**
	 * 已选择地址List adapter
	 */
	private SelectedAddressAdapter selectedAddressAdapter;

	/**
	 * 已选择地址List
	 */
	private List<TRegion> selectAddressList = new ArrayList<TRegion>();

	/**
	 * 已选择地址Id intent key
	 */
	public static final String K_SELECTED_ADDRESS_ID = "selectedAddressId";

	public static final String K_SELECTED_ADDRESS = "selectedAddress";

	/**
	 * 已选择的地址，key=级别，value=地区
	 */
	private HashMap<String, TRegion> selectRegionMap = new HashMap<String, TRegion>();

	@Bean
	RegionDao regionDao;

	/**
	 * 存放直辖市下的第二级地区<br>
	 * 服务器返回的数据 北京市-北京市第一中级人民法院-北京市东城区，<br>
	 * 需要去掉中间的 “北京市第一中级人民法院”
	 * 
	 */
	Map<String, TRegion> municipalityMap = new HashMap<String, TRegion>();

	@AfterViews
	public void onAfterViews() {
		List<TRegion> localAddressList = regionDao.getRegionList(RegionDao.VALID_TRUE, RegionDao.JB_PROVINCE);
		addressList.addAll(localAddressList);
		refreshAddressList();
		selectedAddressLV.setOnItemClickListener(new SelectedAddressOnItemClick());
	}

	/**
	 * 点击请选择地址提示
	 */
	@Click(R.id.select_address_tip)
	protected void clickSelectAddressTip() {
		addressList.clear();
		List<TRegion> localAddressList = regionDao.getRegionList(RegionDao.VALID_TRUE, RegionDao.JB_PROVINCE);
		addressList.addAll(localAddressList);
		refreshAddressList();
		selectAddressList.clear();
		selectRegionMap.clear();
		// 刷新已选地址
		refreshSelectedAddress();
	}
	
	@Click(R.id.select_address_save)
	protected void clickSure() {
		StringBuffer addressId = new StringBuffer();
		StringBuffer address = new StringBuffer();
		TRegion province = selectRegionMap.get(RegionDao.JB_PROVINCE);
		if (null == province) {
			Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
			return;
		} else {
			addressId.append(province.getCId()).append("-");
			address.append(province.getCName()).append("-");
		}
		if (NrcUtils.isMunicipality(province.getCId())) { // 说明是直辖市
			TRegion area = selectRegionMap.get(RegionDao.JB_AREA);
			if (null == area) {
				Toast.makeText(this, "地址选择不完整", Toast.LENGTH_SHORT).show();
				return;
			} else {
				addressId.append(area.getCId());
				address.append(area.getCName());
			}
		} else {
			TRegion city = selectRegionMap.get(RegionDao.JB_CITY);
			if (null == city) {
				Toast.makeText(this, "地址选择不完整", Toast.LENGTH_SHORT).show();
				return;
			} else {
				addressId.append(city.getCId()).append("-");
				address.append(city.getCName()).append("-");
			}
			TRegion area = selectRegionMap.get(RegionDao.JB_AREA);
			if (null == area) {
				Toast.makeText(this, "地址选择不完整", Toast.LENGTH_SHORT).show();
				return;
			} else {
				addressId.append(area.getCId());
				address.append(area.getCName());
			}
		}
		Intent intent = new Intent();
		intent.putExtra(K_SELECTED_ADDRESS_ID, addressId.toString());
		intent.putExtra(K_SELECTED_ADDRESS, address.toString());
		setResult(Constants.RESULT_OK, intent);
		this.finish();
	}
	
	@Click(R.id.select_address_back)
	protected void clickBack() {
		this.finish();
	}
	
	/**
	 * 刷新已选择地址
	 */
	private void refreshSelectedAddress() {
		if (null == selectedAddressAdapter) {
			selectedAddressAdapter = new SelectedAddressAdapter();
			selectedAddressLV.setAdapter(selectedAddressAdapter);
		} else {
			selectedAddressAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 刷新地址列表
	 */
	private void refreshAddressList() {
		if (null == addressAdapter) {
			addressAdapter = new AddressAdapter();
			AddressLV.setAdapter(addressAdapter);
		} else {
			addressAdapter.notifyDataSetChanged();
		}
	}

	private class AddressAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return addressList.size();
		}

		@Override
		public TRegion getItem(int position) {
			return addressList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			AddressViewHolder viewHolder = null;
			TRegion tRegion = getItem(position);
			if (null == convertView) {
				viewHolder = new AddressViewHolder();
				LayoutInflater inflater = LayoutInflater.from(SelectAddressActivity.this);
				convertView = inflater.inflate(R.layout.activity_select_address_item, null);
				viewHolder.addressCheckRB = (RadioButton) convertView.findViewById(R.id.select_address_item_radio);
				viewHolder.addressTV = (TextView) convertView.findViewById(R.id.select_address_item_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (AddressViewHolder) convertView.getTag();
			}

			viewHolder.addressTV.setText(tRegion.getCName());
			TRegion tempRegion = selectRegionMap.get(tRegion.getCJb());
			if (null != tempRegion && tempRegion.getCId().equals(tRegion.getCId())) {
				viewHolder.addressCheckRB.setChecked(true);
			} else {
				viewHolder.addressCheckRB.setChecked(false);
			}

			AddressOnClickListener addressOnClickListener = new AddressOnClickListener();
			addressOnClickListener.tRegion = tRegion;
			convertView.setOnClickListener(addressOnClickListener);
			return convertView;
		}
	}

	private static class AddressViewHolder {

		/**
		 * 地址名称
		 */
		TextView addressTV;

		/**
		 * 地址 选中
		 */
		RadioButton addressCheckRB;
	}

	private class SelectedAddressAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return selectAddressList.size();
		}

		@Override
		public TRegion getItem(int position) {
			return selectAddressList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AddressYetViewHolder viewHolder = null;
			final TRegion tRegion = getItem(position);
			if (null == convertView) {
				viewHolder = new AddressYetViewHolder();
				LayoutInflater inflater = LayoutInflater.from(SelectAddressActivity.this);
				convertView = inflater.inflate(R.layout.activity_select_address_yet_item, null);
				viewHolder.addressYetTV = (TextView) convertView.findViewById(R.id.select_address_item_name);
				viewHolder.arrowView = (View) convertView.findViewById(R.id.select_address_item_arrow);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (AddressYetViewHolder) convertView.getTag();
			}
            
			if (RegionDao.JB_AREA.equals(tRegion.getCJb())) {
				viewHolder.arrowView.setVisibility(View.GONE);
			} else {
				viewHolder.arrowView.setVisibility(View.VISIBLE);
			}
			viewHolder.addressYetTV.setText(tRegion.getCName());
			return convertView;
		}
	}

	private static class AddressYetViewHolder {

		/**
		 * 地址名称
		 */
		TextView addressYetTV;
		
		/**
		 * 箭头
		 */
		View arrowView;
	}

	private class SelectedAddressOnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			List<TRegion> localAddressNewList = new ArrayList<TRegion>();
			localAddressNewList.addAll(selectAddressList);
			selectAddressList.clear();
			selectRegionMap.clear();
			selectAddressList.addAll(localAddressNewList.subList(0, arg2 + 1));
			for (TRegion tRegion2 : selectAddressList) {
				selectRegionMap.put(tRegion2.getCJb(), tRegion2);
			}
			// 刷新已选地址
			refreshSelectedAddress();
			addressList.clear();
			String parentId = selectAddressList.get(selectAddressList.size() - 1).getCId();
			List<TRegion> localAddressList = getSubAddressById(parentId);
			addressList.addAll(localAddressList);
			refreshAddressList();
		}
	}

	private class AddressOnClickListener implements OnClickListener {

		public TRegion tRegion;

		@Override
		public void onClick(View v) {
			List<TRegion> localAddressList = getSubAddressById(tRegion.getCId());
			if (null != localAddressList && localAddressList.size() > 0) {
				addressList.clear();
				// 查询下一级别
				addressList.addAll(localAddressList);
				refreshAddressList();
			} else {
				refreshAddressList();
			}
			if (null == selectRegionMap.get(tRegion.getCJb())) {
				selectRegionMap.put(tRegion.getCJb(), tRegion);
				selectAddressList.add(tRegion);
			} else {
				selectRegionMap.put(tRegion.getCJb(), tRegion);
				selectAddressList.remove(selectAddressList.size() - 1);
				selectAddressList.add(tRegion);
			}
			refreshSelectedAddress();
		}
	}
	
	/**
	 * 获取所有子节点地址
	 */
	private List<TRegion> getSubAddressById(String id) {
		List<String> pIdList = new ArrayList<String>();
		pIdList.add(id);
		List<TRegion> localAddressList = regionDao.getRegionListByPId(RegionDao.VALID_TRUE, pIdList);
		if (NrcUtils.isMunicipality(id)) { // 说明是直辖市
			List<String> tPIdList = new ArrayList<String>();
			for (TRegion region : localAddressList) {
				tPIdList.add(region.getCId());
			}
			localAddressList = regionDao.getRegionListByPId(RegionDao.VALID_TRUE, tPIdList);
			for (TRegion region : localAddressList) {
				region.setCId(region.getCParentId() + "-" + region.getCId());
			}
		}
		return localAddressList;
	}
}
