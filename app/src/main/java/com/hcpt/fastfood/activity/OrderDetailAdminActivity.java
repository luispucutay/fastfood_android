package com.hcpt.fastfood.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.hcpt.fastfood.adapter.DetailOrderAdminAdapter;
import com.hcpt.fastfood.adapter.StatusAdapter;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.ItemHistory;
import com.hcpt.fastfood.object.OrderHistory;
import com.hcpt.fastfood.object.User;
import com.hcpt.fastfood.utility.DateUtil;
import com.hcpt.fastfood.utility.StringUtility;
import com.hcpt.fastfood.widget.NoScrollListView;

public class OrderDetailAdminActivity extends BaseActivity implements
		OnClickListener {

	private Button btnTopBack;
	private TextView lblHeader;
	private ArrayList<ItemHistory> listItemsHistory;

	private DetailOrderAdminAdapter itemAdapter;

	private TextView lblNo;
	private TextView lblTotal;
	private TextView lblDate;
	private TextView lblStatus;
	private NoScrollListView lsvItems;
	private TextView lblName;
	private TextView lblPhone;
	private TextView lblAddress;
	private TextView btnUpdate;
	private User currentUser;
	private OrderHistory currentOrder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart_order);
		listItemsHistory = new ArrayList<ItemHistory>();
		getDataFromGlobal();
		initUI();
		initControl();
		getData();

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnUpdate) {
			showUpdateAdapter();
		}
	}

	private void initControl() {
		lblHeader.setText(getString(R.string.lbl_order_detail));
		btnTopBack.setBackgroundResource(R.drawable.btn_back);
		btnTopBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		itemAdapter = new DetailOrderAdminAdapter(this, listItemsHistory);
		lsvItems.setAdapter(itemAdapter);
		btnUpdate.setOnClickListener(this);
		if (GlobalValue.getInstance().isLogin) {
			if (currentUser.getRole().equals(Constant.ROLE_NORMAL_USER)) {
				btnUpdate.setVisibility(View.GONE);
			}
		} else {
			btnUpdate.setVisibility(View.GONE);
		}
	}

	private void initUI() {
		lblNo = (TextView) findViewById(R.id.lblNo);
		lblTotal = (TextView) findViewById(R.id.lblTotal);
		lblDate = (TextView) findViewById(R.id.lblDate);
		lblStatus = (TextView) findViewById(R.id.lblStatus);
		lsvItems = (NoScrollListView) findViewById(R.id.lsvItems);
		lblName = (TextView) findViewById(R.id.lblName);
		lblPhone = (TextView) findViewById(R.id.lblPhone);
		lblAddress = (TextView) findViewById(R.id.lblAddress);
		btnUpdate = (TextView) findViewById(R.id.btnUpdate);
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		btnTopBack = (Button) findViewById(R.id.btnLeft);
	}

	private void getDataFromGlobal() {
		currentUser = GlobalValue.getInstance().getCurrentUser();
		currentOrder = GlobalValue.getInstance().currentOrder;
	}

	private void getData() {
		listItemsHistory = currentOrder.getListItem();
		itemAdapter.setArrItems(listItemsHistory);
		itemAdapter.notifyDataSetChanged();
		lblNo.setText(getResources().getString(R.string.lbl_no_item_colon) + " "
				+ currentOrder.getTotalItems());
		lblTotal.setText(getResources().getString(R.string.lbl_currency)
				+ currentOrder.getOrderPrice());
		lblDate.setText(DateUtil.convertTimeStampToDate(
				currentOrder.getCreated(), "yyyy-MM-dd kk:mm"));
		lblStatus.setText(StringUtility.getStatusByKey(currentOrder
				.getOrderStatus()));
		lblName.setText(getResources().getString(R.string.lbl_name_colon) + " "
				+ currentOrder.getOrderName());
		lblPhone.setText(getResources().getString(R.string.lbl_phone_colon) + " "
				+ currentOrder.getOrderTel());
		lblAddress.setText(getResources().getString(R.string.lbl_address_colon) + " "
				+ currentOrder.getOrderAddress());
	}

	private void showUpdateAdapter() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_update_order_status);
		dialog.setCancelable(false);

		ListView lsvStatus = (ListView) dialog.findViewById(R.id.lsvStatus);
		TextView lblSubmit = (TextView) dialog.findViewById(R.id.lblSubmit);
		TextView lblCancel = (TextView) dialog.findViewById(R.id.lblCancel);

		final StatusAdapter adapter = new StatusAdapter(this,
				currentUser.getRole());
		adapter.setSelectedStringByNumber(currentOrder.getOrderStatus());
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
				AlertDialog.Builder dialogSub = new AlertDialog.Builder(self,
						AlertDialog.THEME_HOLO_LIGHT);
				dialogSub
						.setTitle(
								getResources().getString(
										R.string.lbl_confirm_action))
						.setMessage(
								getResources()
										.getString(
												R.string.lbl_do_you_want_update_this_order));
				dialogSub.setNegativeButton(getResources().getString(R.string.lbl_yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialogSub,
									int which) {
								dialogSub.dismiss();
								dialog.dismiss();
								updateOrder(currentOrder.getId(),
										adapter.getNumberStatusByStringName());
							}
						});
				dialogSub.setNeutralButton(
						getResources().getString(R.string.lbl_no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialogSub,
									int which) {
								dialogSub.dismiss();
								dialog.dismiss();
							}
						});
				dialogSub.show();
			}
		});

		dialog.show();
	}

	private void updateOrder(String orderId, String status) {
		ModelManager.updateOrderByAdmin(this, true, orderId, status,
				new ModelManagerListener() {
					@Override
					public void onSuccess(String json) {
						if (ParserUtility.isSuccess(json)) {
							showToastMessage(ParserUtility.getMessage(json));
							onBackPressed();
						} else {
							showToastMessage(ParserUtility.getMessage(json));
						}
					}

					@Override
					public void onError() {
						showToastMessage(getResources().getString(
								R.string.have_some_error));
					}
				});
	}
}
