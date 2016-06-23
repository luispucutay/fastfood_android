package com.hcpt.fastfood.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private WebView cweBview;
	private Button btnTopBack;
	private TextView lblHeader, txtMinute;
	private LinearLayout layoutNotifiMe;
	private RelativeLayout layoutRefreshAfter;
	private ImageView imgCheck;
	private boolean notifime = false;
	private int time = 5;
	public static final String MY_PREFS_NAME = "LucasApp";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		getValue();
		initUI();
		initControl();
		getData();
	}

	private void getValue() {
		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
				MODE_PRIVATE);
		notifime = prefs.getBoolean("isNotifyMe", false);
		time = prefs.getInt("refreshAfter", 5);
	}

	private void initUI() {
		imgCheck = (ImageView) findViewById(R.id.imgCheck);
		if (notifime) {
			imgCheck.setImageResource(R.drawable.icon_checked);
		} else {
			imgCheck.setImageResource(R.drawable.icon_uncheck);
		}
		txtMinute = (TextView) findViewById(R.id.txtMinute);
		txtMinute.setText(time + " "
				+ getResources().getString(R.string.lbl_minutes));
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		lblHeader.setText(getString(R.string.lbl_setting));
		btnTopBack = (Button) findViewById(R.id.btnLeft);
		btnTopBack.setBackgroundResource(R.drawable.btn_back);
		btnTopBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		layoutNotifiMe = (LinearLayout) findViewById(R.id.layoutNotifiMe);
		layoutRefreshAfter = (RelativeLayout) findViewById(R.id.layoutRefreshAfter);
	}

	private void initControl() {
		layoutNotifiMe.setOnClickListener(this);
		layoutRefreshAfter.setOnClickListener(this);
	}

	private void getData() {
		String ime = GlobalValue.getInstance().getIME(this);
		
		ModelManager.checkDeviceStatus(this, true, ime, new ModelManagerListener() {
			@Override
			public void onSuccess(String json) {
				// TODO Auto-generated method stub
				
				if (ParserUtility.isSuccess(json)) {
					String isChecked = ParserUtility.getDataAsString(json);
					SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME,
							MODE_PRIVATE);
					Editor prefsEditor = mPrefs.edit();
						
					if (Integer.parseInt(isChecked) == 1)
					{		
						Log.e(TAG,"Cuong: isChecked: true");			
						notifime = true;
						prefsEditor.putBoolean("isNotifyMe", true);
						imgCheck.setImageResource(R.drawable.icon_checked);	
						
					}
					else{
						Log.e(TAG,"Cuong: isChecked: false");
						notifime = false;
						prefsEditor.putBoolean("isNotifyMe", false);
						imgCheck.setImageResource(R.drawable.icon_uncheck);
					}
					prefsEditor.commit();
				} 
			}

			@Override
			public void onError() {}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layoutNotifiMe:
			SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME,
					MODE_PRIVATE);
			Editor prefsEditor = mPrefs.edit();
			String ime = GlobalValue.getInstance().getIME(this);
			if (notifime) {
				setPushNotification(ime,"0");
				notifime = false;
				prefsEditor.putBoolean("isNotifyMe", false);
				imgCheck.setImageResource(R.drawable.icon_uncheck);
			} else {
				setPushNotification(ime,"1");
				notifime = true;
				prefsEditor.putBoolean("isNotifyMe", true);
				imgCheck.setImageResource(R.drawable.icon_checked);
			}
			prefsEditor.commit();
			break;
		case R.id.layoutRefreshAfter:
			showNumberPicker();
			break;
		}
	}

	private void showNumberPicker() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_scroll_number);
		dialog.setCancelable(false);
		final NumberPicker picker = (NumberPicker) dialog
				.findViewById(R.id.picker);
		picker.setValue(time);
		picker.setMinValue(1);
		picker.setMaxValue(100);
		TextView lblSend = (TextView) dialog.findViewById(R.id.lblSend);
		TextView lblCancel = (TextView) dialog.findViewById(R.id.lblCancel);

		// set value list category :
		lblCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		lblSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				time = picker.getValue();
				txtMinute.setText(time + " "
						+ getResources().getString(R.string.lbl_minutes));
				dialog.dismiss();
				SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME,
						MODE_PRIVATE);
				Editor prefsEditor = mPrefs.edit();
				prefsEditor.putInt("refreshAfter", time);
				prefsEditor.commit();
			}
		});

		dialog.show();
	}

	private void setPushNotification(String ime, String status) {
		ModelManager.setPushNotification(this, true, ime, status,
				new ModelManagerListener() {
					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						if (ParserUtility.isSuccess(json)) {
							showToastMessage(getResources().getString(
									R.string.change_status_successfully));
						} else {
							showToastMessage(ParserUtility.getMessage(json));
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
