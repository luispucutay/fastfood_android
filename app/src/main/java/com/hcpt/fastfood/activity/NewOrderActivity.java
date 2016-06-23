package com.hcpt.fastfood.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.adapter.MyOrderAdapter;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.OrderHistory;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshListView;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

public class NewOrderActivity extends BaseActivity {

	private Button btnTopBack, btnRefresh;
	private TextView lblHeader;
	private ArrayList<OrderHistory> listOrderHistory;

	private MyOrderAdapter itemAdapter;
	private PullToRefreshListView lsvItems;
	private ListView lsvActually;

	private int mInterval = 2; // 5 seconds by default, can be changed later
	private Timer mTimer;
	public static final String MY_PREFS_NAME = "LucasApp";
	private int currentPage = 1, maxPage = 2;

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
		lblHeader.setText(getString(R.string.lbl_new_order));
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
				GlobalValue.getInstance().detailScreenType = Constant.SCREEN_TYPE_NEW_ORDER;
				gotoActivity(OrderDetailNewActivity.class);
			}
		});
	}

	private void initUI() {
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		btnTopBack = (Button) findViewById(R.id.btnLeft);
		lsvItems = (PullToRefreshListView) findViewById(R.id.lsvItems);
		btnRefresh = (Button) findViewById(R.id.btnRight);
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
			ModelManager.showAllOrder(this, true, role, currentPage + "",
					new ModelManagerListener() {
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
								listOrderHistory = new ArrayList<OrderHistory>();
								itemAdapter
										.setListOrderHistory(listOrderHistory);
								itemAdapter.notifyDataSetChanged();
								lsvItems.onRefreshComplete();
							}
						}

						@Override
						public void onError() {
							// TODO Auto-generated method stub
							showToastMessage(getResources().getString(
									R.string.have_some_error));
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
}
