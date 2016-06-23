package com.hcpt.fastfood.fragment;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.hcpt.fastfood.BaseFragment;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.activity.MainActivity;
import com.hcpt.fastfood.adapter.CartItemAdapter;
import com.hcpt.fastfood.adapter.SubCookMethodAdapter;
import com.hcpt.fastfood.adapter.ToppingAdapter;
import com.hcpt.fastfood.adapter.CartItemAdapter.CartItemAdapterListener;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.object.CookMethod;
import com.hcpt.fastfood.object.ItemCart;

public class CartOrderFragment extends BaseFragment implements OnClickListener {
	public final static String PAYPAL_METHOD = "paypal";
	public final static String BANK_TRANSFER_METHOD = "bank transfer";
	public final static String DELIVERY_METHOD = "pay on delivery";
	private static final String TAG = "CartOrderFragment";
	private TextView lblNumberItem, lblTotalPrice, btnOrder, txtTime,
			txtAddress, txtDate;
	private ListView lsvItems;
	private ArrayList<ItemCart> arrCartItems;
	private CartItemAdapter itemAdapter;
	private Activity self;
	private Dialog mDialog;
	private ToppingAdapter mToppingAdapter;
	private TextView btnCancel, btnSave;
	private EditText edtInsTruction;
	private LinearLayout layoutCartOrder, layoutSubmitOrder;
	public boolean isSubmitOrder = false;
	double totalPrice = 0;
	int totalItems = 0;
	// layout submitOrder
	private TextView lblNumberItemSM, lblTotalPriceSM, btnOrderSM, lblPhone;
	private EditText txtName, txtPhone;
	private TextView lblPaymentMethod;
	private String selected_payment_method = "";
	private DatePickerDialog datePickerDialog;
	private TimePickerDialog timePickerDialog;
	private Calendar timeOrder;
	private SimpleDateFormat dateFormatter, timeFormatter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cart_order, container,
				false);
		self = getActivity();
		initUI(view);
		initControl();
		return view;
	}

	@Override
	public void onLogOut() {
		txtName.setText("");
		txtPhone.setText("");
		lblPhone.setText(R.string.lbl_phone_number);
		txtDate.setText("");
		txtTime.setText("");
		txtAddress.setText("");
		lblPaymentMethod.setText(R.string.payment_method);

		txtDate.setOnClickListener(this);
		txtTime.setOnClickListener(this);
		lblPaymentMethod.setOnClickListener(this);

		txtName.setFocusable(true);
		txtName.setFocusableInTouchMode(true);

		txtPhone.setFocusable(true);
		txtPhone.setFocusableInTouchMode(true);

		txtDate.setFocusable(true);
		txtDate.setFocusableInTouchMode(true);

		txtTime.setFocusable(true);
		txtTime.setFocusableInTouchMode(true);

		txtAddress.setFocusable(true);
		txtAddress.setFocusableInTouchMode(true);

	}

	public void onLogIn() {
		if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {
			onSelectCustomer(getString(R.string.new_customer));
		} else {
			txtName.setText(GlobalValue.getInstance().currentUser
					.getFull_name());
			txtPhone.setText(GlobalValue.getInstance().currentUser.getPhone());
			txtAddress.setText(GlobalValue.getInstance().currentUser
					.getAddress());
			lblPaymentMethod.setFocusable(true);
			if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {
				selected_payment_method = DELIVERY_METHOD;
				lblPaymentMethod.setText(R.string.lbl_cash_on_delivery);
			} else {
				selected_payment_method = "";
				lblPaymentMethod.setText(R.string.payment_method_label);

			}
			txtDate.setOnClickListener(this);
			txtTime.setOnClickListener(this);
			lblPaymentMethod.setOnClickListener(this);
		}
	}

	private void initControl() {
		// TODO Auto-generated method stub
		btnOrder.setOnClickListener(this);
		lblPaymentMethod.setOnClickListener(this);
		btnOrderSM.setOnClickListener(this);
		txtDate.setOnClickListener(this);
		txtTime.setOnClickListener(this);
		initItemlist();
		selected_payment_method = "";
		lblPaymentMethod.setText(getString(R.string.payment_method_label));

		if (GlobalValue.getInstance().isLogin) {
			onLogIn();
		} else {
			txtName.setText("");
			txtPhone.setText("");
			txtAddress.setText("");
		}

	}

	private void initUI(View view) {
		// layout Cart Order
		layoutCartOrder = (LinearLayout) view.findViewById(R.id.layoutOrder);
		btnOrder = (TextView) view.findViewById(R.id.btnOrder);
		lblNumberItem = (TextView) view.findViewById(R.id.lblNumberItem);
		lblTotalPrice = (TextView) view.findViewById(R.id.lblTotalPrice);
		lsvItems = (ListView) view.findViewById(R.id.lsvItems);
		// layout Submit Order :
		layoutSubmitOrder = (LinearLayout) view
				.findViewById(R.id.layoutSubmitOrder);
		btnOrderSM = (TextView) view.findViewById(R.id.btnSubmitOrder);
		lblNumberItemSM = (TextView) view.findViewById(R.id.lblItemNumber);
		lblTotalPriceSM = (TextView) view.findViewById(R.id.lblFinalPrice);
		lblPaymentMethod = (TextView) view.findViewById(R.id.lblPaymentMethod);
		lblPhone = (TextView) view.findViewById(R.id.lblPhone);
		txtName = (EditText) view.findViewById(R.id.txtName);
		txtPhone = (EditText) view.findViewById(R.id.txtPhone);
		txtTime = (TextView) view.findViewById(R.id.txtTime);
		txtAddress = (EditText) view.findViewById(R.id.txtAddress);
		txtDate = (TextView) view.findViewById(R.id.txtDate);

		// Init Dialog
		timeOrder = Calendar.getInstance();
		dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
		timeFormatter = new SimpleDateFormat("K:mm a", Locale.US);
		Calendar newCalendar = Calendar.getInstance();
		datePickerDialog = new DatePickerDialog(self, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				timeOrder.set(year, monthOfYear, dayOfMonth);
				txtDate.setText(dateFormatter.format(timeOrder.getTime()));
			}
		}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
				newCalendar.get(Calendar.DAY_OF_MONTH));

		timePickerDialog = new TimePickerDialog(self, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				timeOrder.set(Calendar.HOUR_OF_DAY, hourOfDay);
				timeOrder.set(Calendar.MINUTE, minute);
				txtTime.setText(timeFormatter.format(timeOrder.getTime()));
			}
		}, newCalendar.get(Calendar.HOUR_OF_DAY),
				newCalendar.get(Calendar.MINUTE), false);
	}

	// Init information
	public void onHiddenChanged(boolean hidden) {
		/**/
	}

	public void updateContent() {
		totalItems = 0;
		totalPrice = 0;
		if (GlobalValue.getInstance().arrCartItems != null
				& !arrCartItems.equals(GlobalValue.getInstance().arrCartItems)) {
			arrCartItems.clear();
			arrCartItems.addAll(GlobalValue.getInstance().arrCartItems);
		}
		// update price

		for (ItemCart item : arrCartItems) {
			// caculate a item
			totalPrice += item.getTotalPrice();
			totalItems += item.getQuantities();
		}
		// layout cart
		lblNumberItem.setText(totalItems + "");
		lblTotalPrice.setText(String.format("$ %.2f", totalPrice) );
		// layout submit
		lblNumberItemSM.setText(totalItems + "");
		lblTotalPriceSM.setText(String.format("$ %.2f", totalPrice) );

		itemAdapter.notifyDataSetChanged();

		updateUI(isSubmitOrder);

	}

	public void onClickBacktoCart() {
		Log.e(TAG, "Cuong: onClickBacktoCart");
		isSubmitOrder = false;
		updateUI(isSubmitOrder);
	}

	private void initItemlist() {
		arrCartItems = new ArrayList<ItemCart>();
		itemAdapter = new CartItemAdapter(self, arrCartItems,
				new CartItemAdapterListener() {

					@Override
					public void clickDisplaylistSubCookMethod(int itemIndex,
							ItemCart item) {
						// TODO Auto-generated method stub
						showDialogSubCookMethod(itemIndex, item);
					}

					@Override
					public void clickDelete(int itemIndex) {
						// TODO Auto-generated method stub
						GlobalValue.getInstance().arrCartItems
								.remove(itemIndex);
						updateContent();
					}

					@Override
					public void clickAddTopping(int itemIndex, ItemCart itemCart) {
						// TODO Auto-generated method stub
						if (itemCart.getArrRelish().size() > 0) {
							showToppingsDialog(itemIndex, itemCart);
						} else {
							Toast.makeText(self,
									self.getString(R.string.error_no_topping),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void clickAddQuantities(int itemIndex, ItemCart item) {
						// TODO Auto-generated method stub
						showListQuantities(itemIndex);
					}

					@Override
					public void clickAddIntroduction(int itemIndex,
							ItemCart itemCart) {
						// TODO Auto-generated method stub
						showInstructionDialog(itemIndex, itemCart);

					}
				});
		lsvItems.setAdapter(itemAdapter);

	}

	@Override
	public void onClick(View v) {
		if (v == btnOrder) {
			if (GlobalValue.getInstance().arrCartItems.size() > 0) {
				isSubmitOrder = true;
				updateUI(isSubmitOrder);
			} else {
				Toast.makeText(
						self,
						getResources().getString(
								R.string.please_add_your_item_to_cart),
						Toast.LENGTH_SHORT).show();
			}
			return;
		}
		if (v == btnOrderSM) {
			String name = txtName.getText().toString();
			String phone = txtPhone.getText().toString();
			String time = txtTime.getText().toString();
			String date = txtDate.getText().toString();
			String address = txtPhone.getText().toString();
			boolean isValid = false;
			if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER))
				isValid = !name.isEmpty() && !phone.isEmpty();// check only name
																// & seats for
																// waiter
			else
				isValid = !name.isEmpty() && !phone.isEmpty()
						&& !time.isEmpty() && !date.isEmpty()
						&& !address.isEmpty()
						&& !selected_payment_method.isEmpty();

			if (isValid) {
				showOrderConfirmDialog();
			} else {
				Toast.makeText(self,
						self.getString(R.string.error_input_receipted_info),
						Toast.LENGTH_SHORT).show();
			}
			return;
		}

		if (v == lblPaymentMethod) {
			showTypePaymentDialog();
			return;
		}

		if (v == txtTime) {
			timePickerDialog.show();
			return;
		}

		if (v == txtDate) {
			datePickerDialog.show();
			return;
		}
	}

	private void updateUI(boolean isSubmitScreen) {
		if (isSubmitScreen) {
			layoutCartOrder.setVisibility(View.GONE);
			layoutSubmitOrder.setVisibility(View.VISIBLE);
			((MainActivity) self).showOrHideRightMenu(false);

			((MainActivity) self).showSubmitOrder();
			if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {
				((MainActivity) self).showOrHideCustomerMenu(true);
				((MainActivity) self).showOrHideTableMenu(false);
			} else {
				((MainActivity) self).showOrHideTableMenu(true);
				((MainActivity) self).showOrHideCustomerMenu(false);
			}

		} else {
			layoutCartOrder.setVisibility(View.VISIBLE);
			layoutSubmitOrder.setVisibility(View.GONE);
			((MainActivity) self).hideSubmitOrder();
			((MainActivity) self).showOrHideRightMenu(true);
			if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {
				((MainActivity) self).showOrHideCustomerMenu(false);
				((MainActivity) self).showOrHideTableMenu(true);
			} else {
				((MainActivity) self).showOrHideTableMenu(false);
				((MainActivity) self).showOrHideCustomerMenu(false);
			}
		}
	}

	private void showOrderConfirmDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(self,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle(self.getString(R.string.app_name))
				.setMessage(self.getString(R.string.confirm_order))
				.setNegativeButton(self.getString(R.string.btn_OK),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (selected_payment_method
										.equals(PAYPAL_METHOD)) {
									requestPaypalPayment(totalPrice,
											"Payment test", "USD");
								} else {
									sendListOrder(selected_payment_method);
								}
								dialog.dismiss();
							}
						})
				.setNeutralButton(self.getString(R.string.btn_Cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
		dialog.show();
	}

	private void showDialogSubCookMethod(final int itemIndex,
			final ItemCart item) {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_select_cook_method);
		dialog.setCancelable(false);

		ListView lsvCook = (ListView) dialog.findViewById(R.id.lsvCookMethod);
		SubCookMethodAdapter adapter = new SubCookMethodAdapter(self, item);
		lsvCook.setAdapter(adapter);

		lsvCook.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CookMethod subcookmethod = (CookMethod) parent
						.getItemAtPosition(position);
				GlobalValue.getInstance().arrCartItems.get(itemIndex)
						.setSelectedSubCookMethod(subcookmethod);
				updateContent();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void showListQuantities(final int itemIndex) {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_select_cook_method);
		dialog.setCancelable(false);

		TextView lblDialogTitle = (TextView) dialog
				.findViewById(R.id.lblDialogTitle);
		lblDialogTitle.setText(getResources().getString(
				R.string.add_item_quantity));
		ListView lsvCook = (ListView) dialog.findViewById(R.id.lsvCookMethod);
		Integer[] numberlist = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(self,
				android.R.layout.simple_list_item_single_choice, numberlist);
		lsvCook.setAdapter(adapter);
		lsvCook.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GlobalValue.getInstance().arrCartItems.get(itemIndex)
						.setQuantities(
								(Integer) parent.getItemAtPosition(position));
				updateContent();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void showInstructionDialog(final int itemIndex,
			final ItemCart mItemCart) {
		// Create view
		mDialog = new Dialog(self, R.style.AppTheme_OrderDetailsDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_instruction);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = 4 * (displaymetrics.widthPixels / 5);
		// lp.height = 3 * (displaymetrics.heightPixels / 5);
		mDialog.getWindow().setAttributes(lp);
		mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
		mDialog.setCancelable(false);

		// Get view
		edtInsTruction = (EditText) mDialog.findViewById(R.id.edtInsTructions);
		if (null != GlobalValue.getInstance().arrCartItems.get(itemIndex)
				.getInsTructions()
				&& GlobalValue.getInstance().arrCartItems.get(itemIndex)
						.getInsTructions().length() > 0) {
			edtInsTruction.setText(GlobalValue.getInstance().arrCartItems.get(
					itemIndex).getInsTructions());
		}
		btnCancel = (TextView) mDialog.findViewById(R.id.btnCancel);
		btnSave = (TextView) mDialog.findViewById(R.id.btnSave);
		if (mToppingAdapter != null) {
			mToppingAdapter = null;
		}
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edtInsTruction.getText().toString().length() > 0) {
					GlobalValue.getInstance().arrCartItems.get(itemIndex)
							.setInsTructions(
									edtInsTruction.getText().toString());
					updateContent();
				}
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	private void showToppingsDialog(final int itemIndex,
			final ItemCart mItemCart) {
		// Create view
		mDialog = new Dialog(self, R.style.AppTheme_OrderDetailsDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_toppings);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = 19 * (displaymetrics.widthPixels / 20);
		lp.height = 3 * (displaymetrics.heightPixels / 5);
		mDialog.getWindow().setAttributes(lp);
		mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
		mDialog.setCancelable(false);

		// Get view
		ListView lvToppings = (ListView) mDialog.findViewById(R.id.lvToppings);
		btnCancel = (TextView) mDialog.findViewById(R.id.btnCancel);
		btnSave = (TextView) mDialog.findViewById(R.id.btnSave);

		// Set data
		if (mItemCart != null) {
			// reset topping adapter
			if (mToppingAdapter != null) {
				mToppingAdapter = null;
			}
			// creat adapter again
			mToppingAdapter = new ToppingAdapter(self, mItemCart);
			lvToppings.setAdapter(mToppingAdapter);
		}

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GlobalValue.getInstance().arrCartItems.set(itemIndex,
						mToppingAdapter.getmItemCart());
				updateContent();
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	private void showTypePaymentDialog() {
		// parse data

		AlertDialog levelDialog;
		// Strings to Show In Dialog with Radio Buttons
		final CharSequence[] items = {
				getResources().getString(R.string.lbl_paypal_payment),
				getResources().getString(R.string.lbl_banking_deposite),
				getResources().getString(R.string.lbl_cash_on_delivery) };

		// Creating and Building the Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(self,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setTitle(getResources()
				.getString(R.string.payment_method_label));
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						switch (item) {
						case 0:
							selected_payment_method = PAYPAL_METHOD;
							break;
						case 1:
							selected_payment_method = BANK_TRANSFER_METHOD;
							break;
						case 2:
							selected_payment_method = DELIVERY_METHOD;
							break;

						}
						lblPaymentMethod.setText(items[item]);
						dialog.dismiss();
					}
				});
		levelDialog = builder.create();
		levelDialog.show();
	}

	private void showBankInfo(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(self,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setTitle(getResources().getString(R.string.banking_information));
		builder.setMessage(Html.fromHtml(message));
		builder.setNegativeButton(getResources().getString(R.string.lbl_yes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						GlobalValue.getInstance().arrCartItems.clear();
						isSubmitOrder = false;
						updateContent();

					}
				});
		builder.create().show();
	}

	public void sendListOrder(String paymentMethod) {
		long timeIntervals;
		if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {// if
																			// waiter,
																			// time
																			// is
																			// now
			Time time = new Time();
			time.setToNow();
			timeIntervals = time.toMillis(false) / 1000;
		} else
			// if not waiter, time is delivery time
			timeIntervals = timeOrder.getTimeInMillis() / 1000;
		processOrder(txtName.getText().toString(), txtPhone.getText()
				.toString(), GlobalValue.getInstance().arrCartItems,
				paymentMethod, timeIntervals + "", txtAddress.getText()
						.toString());
	}

	private void processOrder(String name, String phone,
			ArrayList<ItemCart> arr, String paymentMethod, String time,
			String address) {
		String id = "";
		String type = "";
		int tableId = 0;
		int seats = 0;

		if (GlobalValue.getInstance().isLogin) {
			id = GlobalValue.getInstance().currentUser.getId();
			type = "1";
		} else {
			TelephonyManager mngr = (TelephonyManager) self
					.getSystemService(self.TELEPHONY_SERVICE);
			id = mngr.getDeviceId();
			type = "0";
		}
		if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {
			seats = Integer.parseInt(phone);
			phone = "";
			tableId = Integer.parseInt(GlobalValue.getInstance()
					.getCurrentTable().getId());
		}

		ModelManager.sendProductOrder(self, name, phone, arr, paymentMethod,
				time, address, id, type, tableId, seats, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						Log.e("Cart order", "json : " + json);
						if (selected_payment_method
								.equals(BANK_TRANSFER_METHOD)) {
							JSONObject jsonobj;
							try {
								jsonobj = new JSONObject(json);
								showBankInfo(jsonobj.getString("bankInfo"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								GlobalValue.getInstance().arrCartItems.clear();
								isSubmitOrder = false;
								updateContent();
							}

						} else {
							Toast.makeText(
									self,
									getResources().getString(
											R.string.your_order_is_successfull),
									Toast.LENGTH_SHORT).show();
							GlobalValue.getInstance().arrCartItems.clear();
							isSubmitOrder = false;
							updateContent();
						}

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				});
	}

	// ==================================================
	// paypal
	private void requestPaypalPayment(double value, String content,
			String currency) {

		PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(value),
				currency, content);

		Intent intent = new Intent(self, PaymentActivity.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
				PaymentActivity.ENVIRONMENT_NO_NETWORK);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID,
				getActivity().getString(R.string.PAYPAL_CLIENT_APP_ID));
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL,
				getActivity().getString(R.string.PAYPAL_RECEIVE_EMAIL_ID));
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, 0);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		startActivityForResult(intent, 0);
	}

	public void onSelectCustomer(String customerName) {
		if (customerName == getString(R.string.new_customer)) {
			txtName.setText("");
			txtName.setFocusable(true);
			txtName.setFocusableInTouchMode(true);
			txtName.invalidate();

		} else {
			txtName.setText(customerName);
			txtName.setFocusable(false);
		}
		lblPhone.setText(R.string.lbl_no_of_seats);
		txtPhone.setText("1");
		txtAddress.setText("");
		selected_payment_method = DELIVERY_METHOD;
		lblPaymentMethod.setText(R.string.lbl_cash_on_delivery);

		txtDate.setText("");
		txtTime.setText("");
		txtAddress.setText("");
		//

		txtDate.setFocusable(false);
		txtTime.setFocusable(false);
		txtAddress.setFocusable(false);
		//
		lblPaymentMethod.setOnClickListener(null);
		txtDate.setOnClickListener(null);
		txtTime.setOnClickListener(null);
	}
}
