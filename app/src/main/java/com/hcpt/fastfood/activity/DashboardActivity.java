package com.hcpt.fastfood.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.adapter.DashboarItemAdapter;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.DashboardRowItemObject;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshListView;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

@SuppressLint("NewApi")
public class DashboardActivity extends BaseActivity implements OnClickListener {

	private ArrayList<DashboardRowItemObject> listRowItem;
	private DashboarItemAdapter itemAdapter;

	private TextView lblHeader, btnDay, btnWeek, btnMonth, btnYear, lbl_type;
	private Button btnLeft, btnRight;

	// Var for date
	private DatePickerDialog datePickerDialogFrom;
	private DatePickerDialog datePickerDialogTo;
	private SimpleDateFormat dateFormatter;
	private Calendar fromTime;
	private Calendar toTime;

	private Context context;
	private String option = "0";
	private Boolean filtering = false;
	private int currentPage = 1;

	private PullToRefreshListView lsvSong;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		initUI();
		initControl();
		getData();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initUI() {
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setBackgroundResource(R.drawable.btn_back);
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setBackgroundResource(R.drawable.ic_calandar);
		btnRight.setVisibility(View.VISIBLE);

		btnDay = (TextView) findViewById(R.id.btnDay);
		btnWeek = (TextView) findViewById(R.id.btnWeek);
		btnMonth = (TextView) findViewById(R.id.btnMonth);
		btnYear = (TextView) findViewById(R.id.btnYear);
		lbl_type = (TextView) findViewById(R.id.lbl_type);

		listRowItem = new ArrayList<DashboardRowItemObject>();
		itemAdapter = new DashboarItemAdapter(this, listRowItem);
		lsvSong = (PullToRefreshListView) findViewById(R.id.lsvSong);
		// lsvActually = lsvSong.getRefreshableView();
		// lsvActually.setAdapter(itemAdapter);
		lsvSong.setAdapter(itemAdapter);
		itemAdapter.notifyDataSetChanged();
		lsvSong.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				listRowItem = new ArrayList<DashboardRowItemObject>();
				currentPage = 1;
				if (filtering) {
					demo();
				} else {
					getData();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (filtering) {
					demo();
				} else {
					getData();
				}
			}
		});
	}

	public void demo() {
		itemAdapter.notifyDataSetChanged();
		lsvSong.post(new Runnable() {
			@Override
			public void run() {
				lsvSong.onRefreshComplete();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnDay:
			filtering = false;
			option = "0";
			lbl_type.setText(getResources().getString(R.string.lbl_daily));
			listRowItem = new ArrayList<DashboardRowItemObject>();
			currentPage = 1;
			getData();
			break;
		case R.id.btnWeek:
			filtering = false;
			option = "1";
			listRowItem = new ArrayList<DashboardRowItemObject>();
			currentPage = 1;
			getData();
			lbl_type.setText(getResources().getString(R.string.lbl_weekly));
			break;
		case R.id.btnMonth:
			filtering = false;
			lbl_type.setText(getResources().getString(R.string.lbl_monthly));
			option = "2";
			listRowItem = new ArrayList<DashboardRowItemObject>();
			currentPage = 1;
			getData();
			break;
		case R.id.btnYear:
			filtering = false;
			lbl_type.setText(getResources().getString(R.string.lbl_yearly));
			option = "3";
			listRowItem = new ArrayList<DashboardRowItemObject>();
			currentPage = 1;
			getData();
			break;
		}
	}

	private void getData() {
		ModelManager.showDashBoard(this, true, option, currentPage + "",
				new ModelManagerListener() {
					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						if (ParserUtility.isSuccess(json)) {
							listRowItem.addAll(ParserUtility
									.parseListDashBoard(json));
							itemAdapter = new DashboarItemAdapter(context,
									listRowItem);
							itemAdapter.setArrItems(listRowItem);
							lsvSong.setAdapter(itemAdapter);
							itemAdapter.notifyDataSetChanged();
							lsvSong.onRefreshComplete();
							currentPage++;
						} else {
							showToastMessage(ParserUtility.getMessage(json));
							// listRowItem = new
							// ArrayList<DashboardRowItemObject>();
							// itemAdapter.setArrItems(listRowItem);
							// lsvSong.setAdapter(itemAdapter);
							// itemAdapter.notifyDataSetChanged();
							lsvSong.onRefreshComplete();
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

	private void getDataByDate(String startDate, String endDate) {
		ModelManager.showDashBoardByDate(this, true, option, startDate, endDate,
				new ModelManagerListener() {
					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						if (ParserUtility.isSuccess(json)) {
							listRowItem = new ArrayList<DashboardRowItemObject>();
							listRowItem = ParserUtility
									.parseListDashBoard(json);
							itemAdapter = new DashboarItemAdapter(context,
									listRowItem);
							itemAdapter.setArrItems(listRowItem);
							lsvSong.setAdapter(itemAdapter);
							itemAdapter.notifyDataSetChanged();
							//lbl_type.setText(getResources().getString(
							//		R.string.lbl_filter_by_date));
							lsvSong.onRefreshComplete();
						} else {
							showToastMessage(ParserUtility.getMessage(json));
							// listRowItem = new
							// ArrayList<DashboardRowItemObject>();
							// itemAdapter.setArrItems(listRowItem);
							// lsvSong.setAdapter(itemAdapter);
							// itemAdapter.notifyDataSetChanged();
							lsvSong.onRefreshComplete();
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

	private void initControl() {
		this.context = this;
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		fromTime = Calendar.getInstance();
		toTime = Calendar.getInstance();

		lblHeader.setText(getString(R.string.lbl_dashboard));
		btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		btnRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialogFilter();
			}
		});

		btnDay.setOnClickListener(this);
		btnWeek.setOnClickListener(this);
		btnMonth.setOnClickListener(this);
		btnYear.setOnClickListener(this);
	}

	private void showDialogFilter() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_date_filter);
		dialog.setCancelable(false);
		TextView lblSubmit = (TextView) dialog.findViewById(R.id.lblFilter);
		TextView lblCancel = (TextView) dialog.findViewById(R.id.lblCancel);
		final TextView txtFrom = (TextView) dialog.findViewById(R.id.txtFrom);
		final TextView txtTo = (TextView) dialog.findViewById(R.id.txtTo);

		txtFrom.setText(dateFormatter.format(fromTime.getTime()));
		txtTo.setText(dateFormatter.format(toTime.getTime()));

		txtFrom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar newCalendar = Calendar.getInstance();
				datePickerDialogFrom = new DatePickerDialog(context,
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								fromTime.set(year, monthOfYear, dayOfMonth);
								txtFrom.setText(dateFormatter.format(fromTime
										.getTime()));
							}
						}, newCalendar.get(Calendar.YEAR), newCalendar
								.get(Calendar.MONTH), newCalendar
								.get(Calendar.DAY_OF_MONTH));
				datePickerDialogFrom.show();
			}
		});

		txtTo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar newCalendar = Calendar.getInstance();
				datePickerDialogTo = new DatePickerDialog(context,
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								toTime.set(year, monthOfYear, dayOfMonth);
								txtTo.setText(dateFormatter.format(toTime
										.getTime()));
							}
						}, newCalendar.get(Calendar.YEAR), newCalendar
								.get(Calendar.MONTH), newCalendar
								.get(Calendar.DAY_OF_MONTH));
				datePickerDialogTo.show();
			}
		});

		// set value list category :
		lblCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		lblSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDataByDate(dateFormatter.format(fromTime.getTime()),
						dateFormatter.format(toTime.getTime()));
				filtering = true;
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}
