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
import com.hcpt.fastfood.adapter.PromotionAdapter;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.PromotionObject;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshListView;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

public class PromotionActivity extends BaseActivity {

	private Button btnTopBack, btnRefresh, btnPromotion;
	private TextView lblHeader;
	private ArrayList<PromotionObject> listPromotion;

	private PromotionAdapter itemAdapter;
	private PullToRefreshListView lsvItems;
	private ListView lsvActually;

	private int mInterval = 2; // 5 seconds by default, can be changed later
	private Timer mTimer;
	public static final String MY_PREFS_NAME = "LucasApp";
	private int currentPage = 1, maxPage = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotion);
		listPromotion = new ArrayList<PromotionObject>();
		initUI();
		initControl();
		getValue();
		getData();
		autoRefreshEvents();
	}

	private void getValue() {
		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
				MODE_PRIVATE);
		mInterval = prefs.getInt("refreshAfter", 5);
	}

	private void initControl() {
		lblHeader.setText(getString(R.string.lbl_promotion));
		btnTopBack.setBackgroundResource(R.drawable.btn_back);
		btnRefresh.setBackgroundResource(R.drawable.ic_action_refresh);
		btnTopBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentPage = 1;
				listPromotion = new ArrayList<PromotionObject>();
				getData();
			}
		});

		itemAdapter = new PromotionAdapter(this, listPromotion);
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
				listPromotion = new ArrayList<PromotionObject>();
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
				GlobalValue.getInstance().currentPromotion = listPromotion
						.get(position - 1);
				gotoActivity(PromotionDetailActivity.class);
			}
		});
	}

	private void initUI() {
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		btnTopBack = (Button) findViewById(R.id.btnLeft);
		lsvItems = (PullToRefreshListView) findViewById(R.id.lsvItems);
		btnRefresh = (Button) findViewById(R.id.btnRight);
		btnRefresh.setVisibility(View.VISIBLE);
		btnPromotion = (Button) findViewById(R.id.btnPromotion);
		btnPromotion.setVisibility(View.GONE);
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
			ModelManager.getPromotion(this, true, currentPage + "",
					new ModelManagerListener() {
						@Override
						public void onSuccess(String json) {
							if (ParserUtility.isSuccess(json)) {
								listPromotion.addAll(ParserUtility
										.parseListPromotion(json));
								itemAdapter.setListOrderHistory(listPromotion);
								itemAdapter.notifyDataSetChanged();
								lsvItems.onRefreshComplete();
								maxPage = ParserUtility.getMaxPage(json);
								currentPage++;
							} else {
								showToastMessage(ParserUtility.getMessage(json));
								listPromotion = new ArrayList<PromotionObject>();
								itemAdapter.setListOrderHistory(listPromotion);
								itemAdapter.notifyDataSetChanged();
								lsvItems.onRefreshComplete();
							}
						}

						@Override
						public void onError() {
							showToastMessage(getResources().getString(R.string.have_some_error));
						}
					});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
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
