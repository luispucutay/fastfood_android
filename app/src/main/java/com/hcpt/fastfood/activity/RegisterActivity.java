package com.hcpt.fastfood.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.User;
import com.hcpt.fastfood.utility.StringUtility;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private WebView cweBview;
	private String fromLocation = "";
	private String destinationLocation = "";
	private Button btnTopBack;
	private TextView lblHeader;

	private EditText txtName;
	private EditText txtPassword;
	private EditText txtPasswordConfirm;
	private EditText txtEmail;
	private EditText txtPhone;
	private EditText txtFullName;
	private EditText txtAddress;
	private TextView btnRegister, lblConfirmPass, lblPassword;
	private boolean isLogin;
	private User user;
	public static final String MY_PREFS_NAME = "LucasApp";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		user = new User();
		isLogin = GlobalValue.getInstance().isLogin;
		if (isLogin) {
			user = GlobalValue.getInstance().currentUser;
		}
		initUI();
		initControl();
		getData();
	}

	private void initUI() {
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		btnTopBack = (Button) findViewById(R.id.btnLeft);
		btnTopBack.setBackgroundResource(R.drawable.btn_back);
		btnTopBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		txtName = (EditText) findViewById(R.id.txtName);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPasswordConfirm = (EditText) findViewById(R.id.txtPasswordConfirm);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtPhone = (EditText) findViewById(R.id.txtPhone);
		txtFullName = (EditText) findViewById(R.id.txtFullName);
		txtAddress = (EditText) findViewById(R.id.txtAddress);
		btnRegister = (TextView) findViewById(R.id.btnRegister);
		lblConfirmPass = (TextView) findViewById(R.id.lblConfirmPass);
		lblPassword = (TextView) findViewById(R.id.lblPassword);

		if (isLogin) {
			lblHeader.setText(getString(R.string.lbl_my_profile));
			txtName.setText(user.getUser_name());
			txtEmail.setText(user.getEmail());
			txtPhone.setText(user.getPhone());
			txtFullName.setText(user.getFull_name());
			txtAddress.setText(user.getAddress());
			txtPasswordConfirm.setVisibility(View.GONE);
			lblConfirmPass.setVisibility(View.GONE);
			txtPassword.setVisibility(View.GONE);
			lblPassword.setVisibility(View.GONE);
			
			txtName.setEnabled(false);
			txtEmail.setEnabled(false);
			lblHeader.setText(getString(R.string.lbl_update_profile));
			btnRegister.setText(getString(R.string.lbl_update_profile));
		} else {
			lblHeader.setText(getString(R.string.lbl_register));
			btnRegister.setText(getString(R.string.lbl_register));
		}
	}

	private void initControl() {
		btnRegister.setOnClickListener(this);
	}

	private void getData() {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnRegister:
			if (isLogin) {
				updateProfile();
			} else {
				register();
			}
			break;
		}
	}

	public void register() {
		// Get value from view
		user.setUser_name(txtName.getText().toString());
		user.setPassword(txtPassword.getText().toString());
		user.setEmail(txtEmail.getText().toString());
		user.setPhone(txtPhone.getText().toString());
		user.setFull_name(txtFullName.getText().toString());
		user.setAddress(txtAddress.getText().toString());
		String passWordConfirm = txtPasswordConfirm.getText().toString();

		if (user.getUser_name().isEmpty() || user.getPassword().isEmpty()
				|| user.getEmail().isEmpty() || user.getPhone().isEmpty()
				|| user.getFull_name().isEmpty() || user.getAddress().isEmpty()
				|| passWordConfirm.isEmpty()) {
			showToastMessage(getResources().getString(
					R.string.lbl_please_enter_full));
			return;
		}

		if (!StringUtility.isEmailValid(user.getEmail())) {
			showToastMessage(getResources().getString(
					R.string.email_is_not_validated));
			return;
		}

		if (!user.getPassword().equals(passWordConfirm)) {
			showToastMessage(getResources().getString(
					R.string.passwords_do_not_match));
			return;
		}

		ModelManager.register(this, true, user.getUser_name(),
				user.getPassword(), user.getEmail(), user.getFull_name(),
				user.getPhone(), user.getAddress(), new ModelManagerListener() {
					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						if (ParserUtility.isSuccess(json)) {
							user = ParserUtility.parseUser(json);
							GlobalValue.getInstance().setCurrentUser(
									ParserUtility.parseUser(json));
							GlobalValue.getInstance().setLogin(true);
							showToastMessage(getResources().getString(
									R.string.register_success));

							SharedPreferences mPrefs = getSharedPreferences(
									MY_PREFS_NAME, MODE_PRIVATE);
							Editor prefsEditor = mPrefs.edit();
							Gson gson = new Gson();
							String jsonTemp = gson.toJson(ParserUtility
									.parseUser(json));
							prefsEditor.putString("MyObject", jsonTemp);
							prefsEditor.putBoolean("isLogin", true);
							prefsEditor.commit();
							onBackPressed();
						} else {
							showToastMessage(getResources().getString(
									R.string.register_failed)
									+ ParserUtility.getMessage(json));
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

	public void updateProfile() {
		// Get value from view
		user.setUser_name(txtName.getText().toString());
		user.setPassword(getResources().getString(R.string.no_effect));
		user.setEmail(txtEmail.getText().toString());
		user.setPhone(txtPhone.getText().toString());
		user.setFull_name(txtFullName.getText().toString());
		user.setAddress(txtAddress.getText().toString());

		if (user.getUser_name().isEmpty() || user.getEmail().isEmpty() || user.getPhone().isEmpty()
				|| user.getFull_name().isEmpty() || user.getAddress().isEmpty()) {
			showToastMessage(getResources().getString(
					R.string.lbl_please_enter_full));
			return;
		}

		if (!StringUtility.isEmailValid(user.getEmail())) {
			showToastMessage(getResources().getString(
					R.string.email_is_not_validated));
			return;
		}

		ModelManager.updateProfile(this, true, user.getId(),
				user.getUser_name(), user.getPassword(), user.getEmail(),
				user.getFull_name(), user.getPhone(), user.getAddress(),
				new ModelManagerListener() {
					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						if (ParserUtility.isSuccess(json)) {
							GlobalValue.getInstance().setCurrentUser(user);
							GlobalValue.getInstance().setLogin(true);
							getResources().getString(
									R.string.update_profile_success);

							SharedPreferences mPrefs = getSharedPreferences(
									MY_PREFS_NAME, MODE_PRIVATE);
							Editor prefsEditor = mPrefs.edit();
							Gson gson = new Gson();
							String jsonTemp = gson.toJson(user);
							prefsEditor.putString("MyObject", jsonTemp);
							prefsEditor.putBoolean("isLogin", true);
							prefsEditor.commit();

							onBackPressed();
						} else {
							showToastMessage(getResources().getString(
									R.string.update_profile_failed)
									+ ParserUtility.getMessage(json));
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
