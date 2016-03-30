package com.thunisoft.sswy.mobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.court.CourtListActivity;
import com.thunisoft.sswy.mobile.activity.court.CourtListActivity_;
import com.thunisoft.sswy.mobile.activity.dialog.ConfirmDialogActivity_;
import com.thunisoft.sswy.mobile.activity.nrc.NetRegisterCaseActivity_;
import com.thunisoft.sswy.mobile.activity.nrc.NetRegisterCaseReviewActivity;
import com.thunisoft.sswy.mobile.activity.nrc.NetRegisterCaseReviewActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.datasource.NrcBasicDao;
import com.thunisoft.sswy.mobile.logic.DeleteLogic;
import com.thunisoft.sswy.mobile.logic.DeleteLogic.DelLayyResponseListener;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr;
import com.thunisoft.sswy.mobile.logic.net.ResponseUtilExtr.BaseResponseExtr;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.pojo.TLayy;
import com.thunisoft.sswy.mobile.util.StringUtils;
import com.thunisoft.sswy.mobile.util.nrc.NrcConstants;
import com.thunisoft.sswy.mobile.util.nrc.NrcEditData;
import com.thunisoft.sswy.mobile.util.nrc.NrcUploadTask;
import com.thunisoft.sswy.mobile.util.nrc.NrcUtils;
import com.thunisoft.sswy.mobile.view.PopMenu;

@EActivity(R.layout.activity_wslacx)
public class WslacxActy extends BaseActivity implements DelLayyResponseListener {

	@Bean
	ResponseUtilExtr responseUtil;

	/**
	 * 立案预约 列表 ListView
	 */
	@ViewById(R.id.wslacx_list)
	PullToRefreshListView wslaListView;

	/**
	 * 网上立案 状态
	 */
	@ViewById(R.id.wslacx_header)
	LinearLayout wslaStatusLinLayout;

	/**
	 * 网上立案 状态名称
	 */
	@ViewById(R.id.wslacx_status_name)
	TextView wslaStatusNameTV;

	/** 网上立案_状态弹出框 */
	private PopMenu popMenu;

	/**
	 * 要显示的立案预约 List
	 */
	List<TLayy> layyList = new ArrayList<TLayy>();

	/**
	 * 立案预约 列表 Adatper
	 */
	WslaAdapter aWslaAdapter;

	/** 网上立案 状态 ：默认为全部 */
	private int status = NrcUtils.NRC_STATUS_ALL;

	private static final int MAX_ONEPAGE_SIZE = 10;

	private int currPage = 1;

	boolean isHasMoreData = false;

	/** 立案预约基本信息 */
	@Bean
	NrcBasicDao nrcBasicDao;

	/** 登录信息 */
	@Bean
	LoginCache loginCache;

	/**
	 * 上传服务器 任务
	 */
	@Bean
	NrcUploadTask nrcUploadTask;

	@Bean
	DeleteLogic deleteLogic;

	private WaittingDialog deleteDialog;

	private int currentPosition;

	@AfterViews
	public void initViews() {
		nrcUploadTask.activity = this;
		// 初始化弹出菜单
		popMenu = new PopMenu(this, NrcUtils.getNrcStatusList());
		// 菜单项点击监听器
		popMenu.setOnItemClickListener(popmenuItemClickListener);

		wslaListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				nrcUploadTask.startUploadTask(WslacxActy.this);
				resetWslaList();
			}
		});
	}

	@Override
	protected void onResume() {
		nrcUploadTask.startUploadTask(this);
		resetWslaList();
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case Constants.REQUEST_CODE_DELETE:
			if (Activity.RESULT_OK == resultCode) {
				deleteDialog = new WaittingDialog(WslacxActy.this, R.style.CustomDialogStyle, "正在删除...");
				deleteDialog.setIsCanclable(false);
				deleteDialog.show();
				deleteLogic.activity = WslacxActy.this;
				deleteLogic.setDelLayyResponseListener(WslacxActy.this);
				deleteLogic.deleteLayy(layyList.get(currentPosition));
			}

			break;

		default:
			break;
		}
	}

	@Background
	public void loadDatas(int type, int page, int rows) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", type + ""));
		params.add(new BasicNameValuePair("page", page + ""));
		params.add(new BasicNameValuePair("rows", rows + ""));
		params.add(new BasicNameValuePair("sortField", "d_update"));
		params.add(new BasicNameValuePair("order", "desc"));
		BaseResponseExtr br = responseUtil.getResponse(ResponseUtilExtr.LOAD_WSLACX, params);
		loadDataDone(br);
	}

	@UiThread
	public void loadDataDone(BaseResponseExtr br) {
		if (currPage == 1) {
			List<TLayy> localLayyList = nrcBasicDao.getLocalLayyList(status);
			layyList.addAll(localLayyList);
		}
		if (StringUtils.isNotBlank(br.getMsg())) {
			if (layyList.size() == 0) {
				Toast.makeText(this, "暂无数据，请添加", Toast.LENGTH_SHORT).show();
			}
			refreshWslaList();
			Toast.makeText(this, br.getMsg(), Toast.LENGTH_LONG).show();
		} else {
			try {
				JSONObject dataObj = br.getResJson().getJSONObject("data");
				JSONArray dataArray = dataObj.getJSONArray("dataList");
				if (null != dataArray && dataArray.length() > 0) {
					// 服务器上的数据
					List<TLayy> serverLayyList = com.alibaba.fastjson.JSONObject.parseArray(dataArray.toString(), TLayy.class);
					// 手机本地数据
					List<TLayy> localLayyList = nrcBasicDao.getLocalLayyList(status);
					Map<String, TLayy> localLayyMap = new LinkedHashMap<String, TLayy>(); // 使用有序的map，用于后面去重
					for (TLayy layy : localLayyList) {
						localLayyMap.put(layy.getCId(), layy);
					}

					// 遍历服务器上的数据和本地的比较，如果发现服务器上的数据比本地的数据的更新时间，直接替换本地数据
					for (int i = 0; i < serverLayyList.size(); i++) {
						TLayy serverLayy = serverLayyList.get(i);
						serverLayy.setNSync(NrcConstants.SYNC_TRUE);
						TLayy localLayy = localLayyMap.get(serverLayy.getCId());
						if (null != localLayy) {
							String localTimeStr = localLayy.getDUpdate();
							String serverTimeStr = serverLayy.getDUpdate();
							long serverTime = NrcUtils.getTimeByDateStr(serverTimeStr, NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);
							long localTime = NrcUtils.getTimeByDateStr(localTimeStr, NrcUtils.YYYY_MM_DD_HH_MM_SS_SSS);
							if (serverTime > localTime) { // 如果服务器端的日期大于本地的，需要替换本地的数据
								int position = -1;
								for (int j = 0; j < layyList.size(); j++) {
									TLayy tempLocalLayy = layyList.get(i);
									if (tempLocalLayy.getCId().equals(serverLayy.getCId())) {
										position = j;
									}
								}
								if (position >= 0) {
									layyList.remove(position);
									layyList.add(position, serverLayy);
								}
							}
						} else {
							layyList.add(serverLayy);
						}
					}

					if (layyList.size() == 0) {
						Toast.makeText(this, "暂无数据，请添加", Toast.LENGTH_SHORT).show();
					}
					refreshWslaList();

					int totals = dataObj.getInt("total");
					JSONObject pageInfo = dataObj.getJSONObject("pageInfo");
					int page = pageInfo.getInt("page");
					int rows = pageInfo.getInt("rows");
					if (totals > page * rows) { // 如果总条数，大于已返回的数据，需要再次请求服务器
						isHasMoreData = true;
					} else {
						isHasMoreData = false;
					}
					nrcBasicDao.deleteSyncTrueLayy(this); // 清空本地已和服务器同步过的所有
															// 网上立案
				} else {
					if (layyList.size() == 0) {
						Toast.makeText(this, "暂无数据，请添加", Toast.LENGTH_SHORT).show();
					}
					refreshWslaList();
				}
			} catch (JSONException e) {
				if (layyList.size() == 0) {
					Toast.makeText(this, "暂无数据，请添加", Toast.LENGTH_SHORT).show();
				}
				refreshWslaList();
				Toast.makeText(this, "数据解析失败...", Toast.LENGTH_LONG).show();
			}
		}
		wslaListView.onRefreshComplete();
	}

	/**
	 * 网上立案列表 点击返回
	 */
	@Click(R.id.wslacx_back)
	protected void clickBack() {
		this.finish();
	}

	/**
	 * 网上立案列表 点击添加
	 */
	@Click(R.id.wslacx_add)
	protected void clickAdd() {
		Intent intent = new Intent();
		intent.putExtra(CourtListActivity.K_IS_WSLACX_ADD, true);
		intent.setClass(this, CourtListActivity_.class);
		startActivity(intent);
	}

	/**
	 * 网上立案状态
	 */
	@Click(R.id.wslacx_header)
	protected void clickStatus() {
		popMenu.showAsDropDown(wslaStatusLinLayout);
	}

	// 弹出菜单监听器
	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Map<String, String> itemMap = NrcUtils.getNrcStatusList().get(position);
			status = Integer.parseInt(itemMap.get(NrcUtils.KEY_CODE));
			resetWslaList();
			popMenu.dismiss();
		}
	};

	/**
	 * 重置网上立案列表数据
	 */
	public void resetWslaList() {
		wslaStatusNameTV.setText(NrcUtils.getNrcStatusNameByCode(status));
		layyList.clear();
		refreshWslaList();// 每次进来先清空页面
		isHasMoreData = false;
		currPage = 1;
		loadDatas(status, currPage, MAX_ONEPAGE_SIZE);
	}

	/**
	 * 刷新网上立案列表数据
	 */
	@UiThread
	protected void refreshWslaList() {
		if (null == aWslaAdapter) {
			aWslaAdapter = new WslaAdapter();
			wslaListView.setAdapter(aWslaAdapter);
		} else {
			aWslaAdapter.notifyDataSetChanged();
		}
	}

	class WslaAdapter extends BaseAdapter {

		private HashMap<Integer, HashMap<String, String>> statusMap;

		public WslaAdapter() {
			statusMap = NrcUtils.getNrcStatusMap();
		}

		/**
		 * 如果需要显示加载更多，为count+1，最后一个item显示加载更多
		 */
		@Override
		public int getCount() {
			int count = layyList.size();
			if (isHasMoreData) {
				count++;
			}
			return count;
		}

		@Override
		public TLayy getItem(int position) {
			return layyList.get(position);
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
				LayoutInflater inflater = LayoutInflater.from(WslacxActy.this);
				convertView = inflater.inflate(R.layout.wslacx_list_item, null);
				viewHolder.caseDetailLinLayout = (LinearLayout) convertView.findViewById(R.id.case_detail);
				viewHolder.loadMoreBtn = (Button) convertView.findViewById(R.id.btn_load_more);
				viewHolder.courtNameTV = (TextView) convertView.findViewById(R.id.tv_CCourtName);
				viewHolder.statusTV = (TextView) convertView.findViewById(R.id.tv_NStatus);
				viewHolder.litigantNameTV = (TextView) convertView.findViewById(R.id.tv_CDsr);
				viewHolder.createTimeTV = (TextView) convertView.findViewById(R.id.tv_DCreate);
				viewHolder.updateTimeTV = (TextView) convertView.findViewById(R.id.tv_DUPdate);
				viewHolder.checkLinLayout = (LinearLayout) convertView.findViewById(R.id.shenhlyt);
				viewHolder.checkerNameTV = (TextView) convertView.findViewById(R.id.tv_CShrName);
				viewHolder.checkOpinionTV = (TextView) convertView.findViewById(R.id.tv_CShyj);

				viewHolder.CaseTypeNameTV = (TextView) convertView.findViewById(R.id.tv_case_type_name);
				viewHolder.LawTypeNameTV = (TextView) convertView.findViewById(R.id.tv_law_type_name);
				viewHolder.wslacxOperLL = (LinearLayout) convertView.findViewById(R.id.wslacx_operation_ll);
				viewHolder.deleteTV = (TextView) convertView.findViewById(R.id.tv_delete);
				viewHolder.editTV = (TextView) convertView.findViewById(R.id.tv_edit);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (position >= getCount() - 1 && isHasMoreData) { // 最后一个item，如果允许查看更多，需要显示加载更多
				viewHolder.caseDetailLinLayout.setVisibility(View.GONE);
				viewHolder.loadMoreBtn.setVisibility(View.VISIBLE);
				viewHolder.caseDetailLinLayout.setOnClickListener(null);
				LoadMoreOnClickListener loadMoreOnClickListener = new LoadMoreOnClickListener();
				viewHolder.loadMoreBtn.setOnClickListener(loadMoreOnClickListener);
			} else {
				viewHolder.loadMoreBtn.setVisibility(View.GONE);
				viewHolder.caseDetailLinLayout.setVisibility(View.VISIBLE);
				TLayy layy = getItem(position);
				viewHolder.courtNameTV.setText(layy.getCCourtName());
				int status = layy.getNStatus();

				HashMap<String, String> statusMapTemp = statusMap.get(status);
				String statusName = statusMapTemp.get("name");
				String statusBgColor = statusMapTemp.get("bgColor");
				viewHolder.CaseTypeNameTV.setText(layy.getCAjlx());
				viewHolder.LawTypeNameTV.setText(layy.getCSpcx());
				viewHolder.statusTV.setText(statusName);
				viewHolder.statusTV.setBackgroundColor(Color.parseColor(statusBgColor));
				viewHolder.litigantNameTV.setText(layy.getCDsr());
				String createDate = "";
				String updateDate = "";
				if (layy.getDCreate() != null) {
					createDate = NrcUtils.getFormatDate(layy.getDCreate());
				}
				if (layy.getDUpdate() != null) {
					updateDate = NrcUtils.getFormatDate(layy.getDUpdate());
				}
				viewHolder.createTimeTV.setText(createDate);
				viewHolder.updateTimeTV.setText("更新于" + updateDate);

				viewHolder.checkLinLayout.setVisibility(View.GONE);

				ItemOnClickListener itemOnClickListener = new ItemOnClickListener();
				itemOnClickListener.position = position;
				viewHolder.caseDetailLinLayout.setOnClickListener(itemOnClickListener);
				viewHolder.loadMoreBtn.setOnClickListener(null);
				if (Integer.valueOf(NrcUtils.NRC_STATUS_DTJ).equals(status)) {
					viewHolder.wslacxOperLL.setVisibility(View.VISIBLE);
					viewHolder.deleteTV.setVisibility(View.VISIBLE);
				} else if (Integer.valueOf(NrcUtils.NRC_STATUS_SCBTG).equals(status)
					     && Integer.valueOf(NrcUtils.NRC_RE_SUBMIT).equals(layy.getNZctj())) {
					viewHolder.wslacxOperLL.setVisibility(View.VISIBLE);
					viewHolder.deleteTV.setVisibility(View.GONE);
				} else {
					viewHolder.wslacxOperLL.setVisibility(View.GONE);
				}

				DelWslaOnClickListener delOnClickListener = new DelWslaOnClickListener();
				delOnClickListener.position = position;
				// 删除点击事件
				viewHolder.deleteTV.setOnClickListener(delOnClickListener);

				final int currPos = position;
				// 编辑点击事件
				viewHolder.editTV.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(currPos);
					}
				});
			}
			return convertView;
		}

		/**
		 * 删除按钮监听
		 * 
		 * @author gewx
		 *
		 */
		private class DelWslaOnClickListener implements OnClickListener {

			public int position;

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WslacxActy.this, ConfirmDialogActivity_.class);
				intent.putExtra("message", getResources().getString(R.string.text_delete));
				WslacxActy.this.startActivityForResult(intent, Constants.REQUEST_CODE_DELETE);
				currentPosition = position;
			}
		}

		private class ItemOnClickListener implements OnClickListener {

			public int position;

			@Override
			public void onClick(View v) {
				startActivity(position);
			}
		}

		/**
		 * 打开查看或者编辑的页面
		 * 
		 * @param position
		 */
		private void startActivity(int position) {
			TLayy layy = getItem(position);
			int status = layy.getNStatus();
			Intent intent = new Intent();
			if (Integer.valueOf(NrcUtils.NRC_STATUS_DTJ).equals(status)) {
				intent.setClass(WslacxActy.this, NetRegisterCaseActivity_.class);
				NrcEditData.setLayy(layy);
			} else if (Integer.valueOf(NrcUtils.NRC_STATUS_SCBTG).equals(status)
				     && Integer.valueOf(NrcUtils.NRC_RE_SUBMIT).equals(layy.getNZctj())) {
				intent.setClass(WslacxActy.this, NetRegisterCaseActivity_.class);
				NrcEditData.setLayy(layy);
			} else {
				intent.setClass(WslacxActy.this, NetRegisterCaseReviewActivity_.class);
				intent.putExtra(NetRegisterCaseReviewActivity.K_LAYY, layy);
			}
			WslacxActy.this.startActivity(intent);
		}

		private class LoadMoreOnClickListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				currPage++;
				loadDatas(status, currPage, MAX_ONEPAGE_SIZE);
			}
		}
	}

	/**
	 * 删除网上立案请求，回调
	 * 
	 * @param response
	 * @param layyId
	 */
	@Override
	public void deleteResult(BaseResponse response, String layyId) {
		if (null != deleteDialog) {
			deleteDialog.dismiss();
			deleteDialog = null;
		}
		if (response.isXtcw()) {
			if (response.isSuccess()) {
				nrcBasicDao.deleteLayy(this, layyId);
				resetWslaList();
			} else {
				Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	static class ViewHolder {

		LinearLayout caseDetailLinLayout;

		/** 法院名称 */
		TextView courtNameTV;

		/** 网上立案 状态 */
		TextView statusTV;

		/** 当事人名称 */
		TextView litigantNameTV;

		/** 创建时间 */
		TextView createTimeTV;

		/** 更新时间 */
		TextView updateTimeTV;

		/** 审核 父控件 */
		LinearLayout checkLinLayout;

		/** 审核人姓名 */
		TextView checkerNameTV;

		/** 审核意见 */
		TextView checkOpinionTV;

		/**
		 * 案件类型
		 */
		TextView CaseTypeNameTV;

		/**
		 * 诉讼类型
		 */
		TextView LawTypeNameTV;

		/**
		 * 操作栏
		 */
		LinearLayout wslacxOperLL;

		/**
		 * 删除
		 */
		TextView deleteTV;

		/**
		 * 编辑
		 */
		TextView editTV;

		/** 加载更多 */
		Button loadMoreBtn;
	}

	/**
	 * 获取 网上立案 状态
	 * 
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置 网上立案 状态
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
