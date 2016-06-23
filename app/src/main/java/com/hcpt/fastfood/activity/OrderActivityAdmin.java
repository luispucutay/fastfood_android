package com.hcpt.fastfood.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.adapter.FilterAdapter;
import com.hcpt.fastfood.adapter.MyOrderAdapter;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.OrderHistory;
import com.hcpt.fastfood.widget.NoScrollListView;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshListView;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

public class OrderActivityAdmin extends BaseActivity {

	private Button btnTopBack, btnRefresh, btnFilter;
	private TextView lblHeader;
	private ArrayList<OrderHistory> listOrderHistory;

	private MyOrderAdapter itemAdapter;
	private PullToRefreshListView lsvItems;
	private ListView lsvActually;

	private int mInterval = 2; // 5 seconds by default, can be changed later
	private Timer mTimer;
	public static final String MY_PREFS_NAME = "LucasApp";
	private int currentPage = 1, maxPage = 2;
	private String currentStatus = Constant.STATUS_ALL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		listOrderHistory = new ArrayList<OrderHistory>();
		initUI();
		initControl();
		getValue();
		autoRefreshEvents();
	}

	private void getValue() {
		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
				MODE_PRIVATE);
		mInterval = prefs.getInt("refreshAfter", 5);
	}

	private void initControl() {
		lblHeader.setText(getString(R.string.lbl_list_order));
		btnTopBack.setBackgroundResource(R.drawable.btn_back);
		btnRefresh.setBackgroundResource(R.drawable.ic_action_refresh);
		btnTopBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentPage = 1;
				listOrderHistory = new ArrayList<OrderHistory>();
				getData();
			}
		});

		itemAdapter = new MyOrderAdapter(this, listOrderHistory);
		lsvItems = (PullToRefreshListView) findViewById(R.id.lsvItems);
		lsvActually = lsvItems.getRefreshableView();
		lsvActually.setAdapter(itemAdapter);
		itemAdapter.notifyDataSetChanged();
		lsvItems.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				currentPage = 1;
				maxPage = 2;
				listOrderHistory = new ArrayList<OrderHistory>();
				getData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getData();
			}
		});

		lsvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long l) {
				GlobalValue.getInstance().currentOrder = listOrderHistory
						.get(position - 1);
				GlobalValue.getInstance().detailScreenType = Constant.SCREEN_TYPE_ADMIN_ORDER;
				gotoActivity(OrderDetailAdminActivity.class);
			}
		});

		btnFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showUpdateAdapter();
			}
		});
	}

	private void initUI() {
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		btnTopBack = (Button) findViewById(R.id.btnLeft);
		lsvItems = (PullToRefreshListView) findViewById(R.id.lsvItems);
		btnRefresh = (Button) findViewById(R.id.btnRight);
		btnFilter = (Button) findViewById(R.id.btnFilter);
		btnFilter.setVisibility(View.VISIBLE);
		btnRefresh.setVisibility(View.VISIBLE);
	}

	private void getData() {
		if (currentPage == (maxPage + 1)) {
			showToastMessage(getResources().getString(
					R.string.message_have_no_more_data));
			lsvItems.post(new Runnable() {
				@Override
				public void run() {
					lsvItems.onRefreshComplete();
				}
			});
		} else {
			String role = GlobalValue.getInstance().currentUser.getRole();
			ModelManager.showAllOrderAdmin(this, true, role,
					currentPage + "",currentStatus, new ModelManagerListener() {
						@Override
						public void onSuccess(String json) {
							// TODO Auto-generated method stub
							if (ParserUtility.isSuccess(json)) {
								listOrderHistory.addAll(ParserUtility
										.parseListOrder(json));
								itemAdapter
										.setListOrderHistory(listOrderHistory);
								itemAdapter.notifyDataSetChanged();
								lsvItems.onRefreshComplete();
								maxPage = ParserUtility.getMaxPage(json);
								currentPage++;
							} else {
								showToastMessage(ParserUtility.getMessage(json));
								lsvItems.onRefreshComplete();
							}
						}

						@Override
						public void onError() {
							// TODO Auto-generated method stub
							showToastMessage(getResources().getString(R.string.have_some_error));
						}
					});
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		currentPage = 1;
		maxPage = 2;
		listOrderHistory = new ArrayList<OrderHistory>();
		getData();
	}

	@Override
	public void onStop() {
		super.onStop();

		// Stopping refresh events action.
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	private class RefreshEvents extends TimerTask {

		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getData();
				}
			});
		}

	}

	private void autoRefreshEvents() {
		if (mTimer == null) {
			mTimer = new Timer();
			RefreshEvents refresh = new RefreshEvents();
			try {
				mTimer.scheduleAtFixedRate(refresh, mInterval * 60 * 1000,
						mInterval * 60 * 1000);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void showUpdateAdapter() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_filter_order_status);
		dialog.setCancelable(false);

		NoScrollListView lsvStatus = (NoScrollListView) dialog.findViewById(R.id.lsvStatus);
		TextView lblSubmit = (TextView) dialog.findViewById(R.id.lblSubmit);
		TextView lblCancel = (TextView) dialog.findViewById(R.id.lblCancel);

		final FilterAdapter adapter = new FilterAdapter(this,
				Constant.ROLE_ADMIN_USER);
		adapter.setSelectedStringByNumber(currentStatus);
		lsvStatus.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		lsvStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String temp = adapter.getItem(position).toString();
				adapter.setSelectedStatus(temp);
				adapter.notifyDataSetChanged();
			}
		});

		lblCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		lblSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				currentStatus = adapter.getNumberStatusByStringName();
				currentPage = 1;
				maxPage = 2;
				listOrderHistory = new ArrayList<OrderHistory>();
				getData();
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}
