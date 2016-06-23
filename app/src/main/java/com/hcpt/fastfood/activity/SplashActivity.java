package com.hcpt.fastfood.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.GcmManager;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.config.MySharedPreferences;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.ItemCart;
import com.hcpt.fastfood.object.User;
import com.hcpt.fastfood.utility.NetworkUtil;
import com.splunk.mint.Mint;

import java.util.ArrayList;
import java.util.Locale;

import me.drakeet.materialdialog.MaterialDialog;

@SuppressLint("NewApi")
public class SplashActivity extends BaseActivity {

    private GcmManager gcm;
    public static final String MY_PREFS_NAME = "LucasApp";
    private String userId = "0";
    private MaterialDialog mMaterialDialog;
    private Locale myLocale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gcm = new GcmManager(this);
        Mint.initAndStartSession(SplashActivity.this, "20a9a7c1");
        setContentView(R.layout.activity_splash);
        if (GlobalValue.pref == null) {
            GlobalValue.pref = new MySharedPreferences(this);
        }
        checkFirstSettingApp();
        getLanguage();
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        NetworkUtil.enableStrictMode();
    }

    private void checkLogin() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        GlobalValue.getInstance().isLogin = prefs.getBoolean("isLogin", false);
        if (GlobalValue.getInstance().isLogin) {
            Gson gson = new Gson();
            String json = prefs.getString("MyObject", "");
            GlobalValue.getInstance().currentUser = gson.fromJson(json, User.class);
            userId = gson.fromJson(json, User.class).getId();
        }
    }

    public void showDialogEnableNetwork() {
         mMaterialDialog = new MaterialDialog(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.lbl_error_network))
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        finish();
                    }
                });
        mMaterialDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtil.checkNetworkAvailable(this)) {
            checkLogin();
            initAllCategory();
        } else {
            showDialogEnableNetwork();
        }
    }

    private void initAllCategory() {
        GlobalValue.getInstance().arrCartItems = new ArrayList<ItemCart>();
        ModelManager.getAllCategory(this, true, new ModelManagerListener() {
            @Override
            public void onSuccess(String json) {
                GlobalValue.getInstance().setArrCategories(ParserUtility.parseListCategories(json));// arrCategories
                gotoActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub
            }
        });
        registerDevice();
    }

    @SuppressLint("NewApi")
    public void registerDevice() {

        String ime = GlobalValue.getInstance().getIME(this);
        String gcmRegid = gcm.getRegid();
        Log.e("gcmRegid", gcmRegid);
        if (gcm != null && !gcmRegid.isEmpty()) {
            ModelManager.registerDevice(this, true, gcmRegid, ime,
                    userId, new ModelManagerListener() {
                        @Override
                        public void onSuccess(String json) {
                            if (ParserUtility.isSuccess(json)) {
                                Log.e("dangtin", "register device success");
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

    public void getLanguage() {
        if (GlobalValue.pref.getStringValue(GlobalValue.LANGUAGE).equals(GlobalValue.LANGUAGE_ENGLISH)) {
            myLocale = new Locale(GlobalValue.LOCALE_ENGLISH);
        } else {
            myLocale = new Locale(GlobalValue.LOCALE_VIETNAMESE);
        }
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void checkFirstSettingApp() {
        if (!GlobalValue.pref.getBooleanValue(GlobalValue.CHECK_FIRST_SETTING_APP)) {
            GlobalValue.pref.putStringValue(GlobalValue.LANGUAGE, GlobalValue.LANGUAGE_ENGLISH);
        }
        GlobalValue.pref.putBooleanValue(GlobalValue.CHECK_FIRST_SETTING_APP, true);
    }
}
