package com.hcpt.fastfood.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.hcpt.fastfood.adapter.CategoryAdapter;
import com.hcpt.fastfood.adapter.LanguageAdapter;
import com.hcpt.fastfood.fragment.StreamsFragment;
import com.hcpt.fastfood.object.Language;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.BaseFragment;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.adapter.TableAdapter;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.fragment.AccountFragment;
import com.hcpt.fastfood.fragment.CartOrderFragment;
import com.hcpt.fastfood.fragment.MenuFragment;
import com.hcpt.fastfood.fragment.SearchFragment;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.TabItem;
import com.hcpt.fastfood.object.User;
import com.hcpt.fastfood.widget.PagerSlidingTabStrip;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends BaseActivity implements OnClickListener {

    public static final String MY_PREFS_NAME = "LucasApp";
    private PagerSlidingTabStrip tabs;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private TableAdapter tableAdapter;
    private ArrayList<TabItem> arrTabs;
    private ArrayList<String> arrCustomers;
    private List<Fragment> listFragments;
    public static final int TAB_MENU = 0;
    public static final int TAB_SEARCH = 1;
    public static final int TAB_CART = 2;
    public static final int TAB_STREAMS = 3;
    public static final int TAB_ACCOUNT = 4;

    public int CURRENT_FRAGMENT = 0;
    private Button btnLeft, btnRight, btnPromotion, btnPromotionTemp, btnCustomer;
    private TextView lblHeader, lblTable;

    private MaterialDialog mMaterialDialog;
    private LanguageAdapter languageAdapter;
    private ArrayList<Language> arrLanguage;
    private Language currentLanguage;
    private Locale myLocale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        initUI();
        initControl();
        checkLogin();
        initListLanguage();
        updateLanguage();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initVariables() {
        arrCustomers = new ArrayList<String>();
    }

    private void checkLogin() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
                MODE_PRIVATE);
        GlobalValue.getInstance().isLogin = prefs.getBoolean("isLogin", false);
        if (GlobalValue.getInstance().isLogin) {
            Gson gson = new Gson();
            String json = prefs.getString("MyObject", "");
            GlobalValue.getInstance().currentUser = gson.fromJson(json,
                    User.class);
        }
        if (GlobalValue.getInstance().isLogin()) {
            if (GlobalValue.getInstance().currentUser.getRole().equals(
                    Constant.ROLE_WAITER_USER)
                    && GlobalValue.getInstance().getCurrentTable().getId() == null) {
                getTable(GlobalValue.getInstance().getCurrentUser().getId());
            }
            if (GlobalValue.getInstance().currentUser.getRole().equals(
                    Constant.ROLE_WAITER_USER)) {
                lblTable.setVisibility(View.VISIBLE);
            } else {
                lblTable.setVisibility(View.GONE);
            }
        } else {
            lblTable.setVisibility(View.GONE);
        }
    }

    public void login(String json) {
        SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME,
                MODE_PRIVATE);
        Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String jsonTemp = gson.toJson(ParserUtility.parseUser(json));
        prefsEditor.putString("MyObject", jsonTemp);
        prefsEditor.putBoolean("isLogin", true);
        prefsEditor.commit();
        for (Fragment ifragment : listFragments) {
            ((BaseFragment) ifragment).onLogIn();
        }
        updateTableSelectionVisibility();
    }

    public void logout() {
        SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String jsonTemp = gson.toJson(new User());
        prefsEditor.putString("MyObject", jsonTemp);
        prefsEditor.putBoolean("isLogin", false);
        prefsEditor.commit();
        for (Fragment ifragment : listFragments) {
            ((BaseFragment) ifragment).onLogOut();
        }
        updateTableSelectionVisibility();
    }

    private void initControl() {
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnPromotion.setOnClickListener(this);
        lblTable.setOnClickListener(this);
        btnCustomer.setOnClickListener(this);
    }

    public void showSubmitOrder() {
        lblTable.setVisibility(View.GONE);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setBackgroundResource(R.drawable.btn_back);
        lblHeader.setText(getString(R.string.submit_order));
    }

    public void hideSubmitOrder() {
        lblTable.setVisibility(View.GONE);
        btnLeft.setVisibility(View.GONE);
        lblHeader.setText(getString(R.string.order_now));
    }

    private void initUI() {
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnPromotion = (Button) findViewById(R.id.btnPromotion);
        btnPromotionTemp = (Button) findViewById(R.id.btnPromotionTemp);
        btnCustomer = (Button) findViewById(R.id.btnCustomer);
        btnLeft.setVisibility(View.GONE);
        btnRight.setVisibility(View.VISIBLE);
        btnPromotion.setVisibility(View.VISIBLE);
        btnPromotionTemp.setVisibility(View.INVISIBLE);
        btnCustomer.setVisibility(View.GONE);
        btnRight.setBackgroundResource(R.drawable.ic_action_overflow);
        lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
        lblTable = (TextView) findViewById(R.id.lblTable);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        initTabs();
        currentLanguage = new Language();
    }

    private void initTabs() {
        arrTabs = new ArrayList<TabItem>();
        listFragments = new ArrayList<Fragment>();
        arrTabs.add(new TabItem(getResources().getString(R.string.lbl_menu),
                R.drawable.ic_menu_select, R.drawable.ic_menu_non_select));
        arrTabs.add(new TabItem(getResources().getString(R.string.search),
                R.drawable.ic_search_select, R.drawable.ic_search_non_select));
        arrTabs.add(new TabItem(getResources().getString(R.string.lbl_cart),
                R.drawable.ic_cart_select, R.drawable.ic_cart_non_select));
        arrTabs.add(new TabItem(getResources().getString(R.string.streams), R.drawable.ic_streams,
                R.drawable.ic_streams_non_select));
        arrTabs.add(new TabItem(getResources().getString(R.string.lbl_account),
                R.drawable.ic_user_select, R.drawable.ic_user_non_select));
        // add tab
        // add fragment
        listFragments.add(new MenuFragment());
        listFragments.add(new SearchFragment());
        listFragments.add(new CartOrderFragment());
        listFragments.add(new StreamsFragment());
        listFragments.add(new AccountFragment());
        initUITabs();
    }

    private void initUITabs() {
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                onTabChange(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        tabs.setArrTabs(arrTabs);
        tabs.setViewPager(viewPager);
        int valueInPixels = (int) getResources().getDimension(R.dimen.size_text_tab_menu);
        tabs.setTextSize(valueInPixels);
        tabs.setTextColor(Color.WHITE);
        tabs.setIndicatorColorResource(R.color.green);
        tabs.setSelectedTextColor(getResources().getColor(R.color.orange_setting));
        tabs.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                onTabChange(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewPager.setCurrentItem(TAB_MENU);
        lblHeader.setText(getString(R.string.menu));
    }

    public void setHeaderTitle(String title) {
        Log.e(TAG, "CUONG: header " + title);
        lblHeader.setText(title);
    }

    private void onTabChange(int index) {
        CURRENT_FRAGMENT = index;
        switch (index) {
            case TAB_MENU:
                lblHeader.setText(getString(R.string.menu));
                btnLeft.setVisibility(View.GONE);
                btnRight.setVisibility(View.VISIBLE);
                btnCustomer.setVisibility(View.GONE);
                btnPromotion.setVisibility(View.VISIBLE);
//                MenuFragment fragmentMenu = (MenuFragment) ((MyPagerAdapter) viewPager
//                        .getAdapter()).getActiveFragment(viewPager, TAB_MENU);
//                fragmentMenu.loadItembyCategory();
                updateTableSelectionVisibility();
                break;
            case TAB_SEARCH:
                lblHeader.setText(getString(R.string.search));
                btnLeft.setVisibility(View.GONE);
                btnRight.setVisibility(View.VISIBLE);
                btnPromotion.setVisibility(View.VISIBLE);
                btnCustomer.setVisibility(View.GONE);
                updateTableSelectionVisibility();
                break;
            case TAB_CART:
                lblHeader.setText(getString(R.string.order));
                btnLeft.setVisibility(View.INVISIBLE);
                btnRight.setVisibility(View.VISIBLE);
                btnPromotion.setVisibility(View.GONE);
                btnCustomer.setVisibility(View.GONE);
                CartOrderFragment fragmentCart = (CartOrderFragment) ((MyPagerAdapter) viewPager
                        .getAdapter()).getActiveFragment(viewPager, TAB_CART);
                fragmentCart.updateContent();
                updateTableSelectionVisibility();
                break;

            case TAB_STREAMS:
                btnLeft.setVisibility(View.GONE);
                btnPromotion.setVisibility(View.GONE);
                btnRight.setVisibility(View.VISIBLE);
                lblHeader.setText(getString(R.string.streams));
                updateTableSelectionVisibility();
                break;
            case TAB_ACCOUNT:
                lblHeader.setText(getString(R.string.account));
                btnLeft.setVisibility(View.GONE);
                btnPromotion.setVisibility(View.GONE);
                btnRight.setVisibility(View.VISIBLE);
                btnCustomer.setVisibility(View.GONE);
                AccountFragment accountFragment = (AccountFragment) ((MyPagerAdapter) viewPager.getAdapter()).getActiveFragment(viewPager, TAB_ACCOUNT);
                accountFragment.onResume();
                accountFragment.setHeaderTitle();
                updateTableSelectionVisibility();
                break;
            default:
                break;
        }
    }

    public void showOrHideRightMenu(boolean isShow) {
        if (isShow)
            btnRight.setVisibility(View.VISIBLE);
        else
            btnRight.setVisibility(View.GONE);
    }

    public void showOrHideTableMenu(boolean isShow) {
        if (isShow)
            lblTable.setVisibility(View.VISIBLE);
        else
            lblTable.setVisibility(View.GONE);
    }

    public void showOrHideCustomerMenu(boolean isShow) {
        if (isShow)
            btnCustomer.setVisibility(View.VISIBLE);
        else
            btnCustomer.setVisibility(View.GONE);
    }

    private void updateTableSelectionVisibility() {
        if (GlobalValue.getInstance().isLogin()) {
            if (GlobalValue.getInstance().currentUser.getRole().equals(
                    Constant.ROLE_WAITER_USER)
                    && GlobalValue.getInstance().getCurrentTable().getId() == null) {
                getTable(GlobalValue.getInstance().getCurrentUser().getId());
            }
            if (GlobalValue.getInstance().currentUser.getRole().equals(
                    Constant.ROLE_WAITER_USER)) {
                lblTable.setVisibility(View.VISIBLE);
            } else {
                lblTable.setVisibility(View.GONE);
            }
        } else {
            lblTable.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "Cuong: CURRENT_FRAGMENT " + CURRENT_FRAGMENT);
        if (CURRENT_FRAGMENT == TAB_CART) {
            CartOrderFragment fragmentCart = (CartOrderFragment) ((MyPagerAdapter) viewPager
                    .getAdapter()).getActiveFragment(viewPager, TAB_CART);
            if (fragmentCart.isSubmitOrder) {
                fragmentCart.onClickBacktoCart();
            } else {
                showDialogExitApp();
            }
        } else {
            showDialogExitApp();
        }
    }

    public void showDialogExitApp() {
        mMaterialDialog = new MaterialDialog(this)
                .setTitle(getResources().getString(R.string.exit_app))
                .setMessage(getResources().getString(R.string.do_you_want_exit_app))
                .setPositiveButton(getResources().getString(R.string.lbl_yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        GlobalValue.mGlobalValue = null;
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.lbl_no), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblTable:
                getTable(GlobalValue.getInstance().getCurrentUser().getId());
                break;
            case R.id.btnPromotion:
                Intent intent = new Intent(self, PromotionActivity.class);
                self.startActivity(intent);
                self.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.push_left_out);
                break;
            case R.id.btnLeft:
                if (CURRENT_FRAGMENT == TAB_CART) {
                    CartOrderFragment fragmentCart = (CartOrderFragment) ((MyPagerAdapter) viewPager
                            .getAdapter()).getActiveFragment(viewPager, TAB_CART);
                    fragmentCart.onClickBacktoCart();
                }
                break;
            case R.id.btnRight:
                if (GlobalValue.getInstance().isLogin) {
                    showMenuWhenLogin(v);
                } else {
                    showMenuNoLogin(v);
                }
                break;
            case R.id.btnCustomer:
                getCustomers();
                break;
        }

    }

    private void showMenuWhenLogin(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.getMenuInflater().inflate(R.menu.poupup_menu_login, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals(getString(R.string.lbl_setting))) {
                    showDialogLanguage();
                } else if (item.getTitle().equals(getString(R.string.lbl_setting))) {
                    gotoActivity(SettingActivity.class);
                } else if (item.getTitle().equals(getString(R.string.lbl_about))) {
                    gotoActivity(AboutActivity.class);
                } else {
                    resetUserIdOnDevice();
                    GlobalValue.getInstance().setLogin(false);
                    GlobalValue.getInstance().currentUser
                            .setRole("0");
                    logout();
                    AccountFragment accountFragment = (AccountFragment) ((MyPagerAdapter) viewPager
                            .getAdapter()).getActiveFragment(
                            viewPager, TAB_ACCOUNT);
                    accountFragment.onResume();
                }
                return false;
            }
        });
        menu.show();
    }

    private void showMenuNoLogin(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.getMenuInflater().inflate(R.menu.poupup_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if ((menuItem.getTitle().equals(getString(R.string.lbl_language)))) {
                    showDialogLanguage();
                } else if (menuItem.getTitle().equals(getString(R.string.lbl_setting))) {
                    gotoActivity(SettingActivity.class);
                } else {
                    gotoActivity(AboutActivity.class);
                }
                return false;
            }
        });
        menu.show();
    }

    private void showDialogLanguage() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_select_language);
        dialog.setCancelable(false);
        ListView lsvLanguage = (ListView) dialog.findViewById(R.id.lsvLanguage);
        TextView lblClose = (TextView) dialog.findViewById(R.id.lblClose);
        TextView lblCannel = (TextView) dialog.findViewById(R.id.lbl_cannel);

        languageAdapter.notifyDataSetChanged();
        lsvLanguage.setAdapter(languageAdapter);

        lblClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalValue.pref.putStringValue(GlobalValue.LANGUAGE, currentLanguage.getName());
                updateLanguage();
                reloadActivity();
                dialog.dismiss();
            }
        });

        lblCannel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lsvLanguage.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refreshListLanguage(arrLanguage.get(position).getName());
            }
        });

        dialog.show();
    }

    private void initListLanguage() {
        arrLanguage = new ArrayList<>();
        arrLanguage.add(new Language("1", GlobalValue.LANGUAGE_ENGLISH, false));
        arrLanguage.add(new Language("2", GlobalValue.LANGUAGE_VIETNAMESE, false));
        languageAdapter = new LanguageAdapter(self, arrLanguage);
        refreshListLanguage(GlobalValue.pref.getStringValue(GlobalValue.LANGUAGE));
    }

    private void refreshListLanguage(String name) {
        for (int i = 0; i < arrLanguage.size(); i++) {
            if (arrLanguage.get(i).getName().equals(name)) {
                arrLanguage.get(i).setIsSelected(true);
                currentLanguage = arrLanguage.get(i);
            } else {
                arrLanguage.get(i).setIsSelected(false);
            }
        }
        languageAdapter.notifyDataSetChanged();
    }

    public void updateLanguage() {
        if (GlobalValue.pref.getStringValue(GlobalValue.LANGUAGE).equals(GlobalValue.LANGUAGE_ENGLISH)) {
            myLocale = new Locale(GlobalValue.LOCALE_ENGLISH);
        } else if (GlobalValue.pref.getStringValue(GlobalValue.LANGUAGE).equals(GlobalValue.LANGUAGE_VIETNAMESE)) {
            myLocale = new Locale(GlobalValue.LOCALE_VIETNAMESE);
        }
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }



    private void resetUserIdOnDevice() {
        String ime = GlobalValue.getInstance().getIME(this);
        ModelManager.resetUserIdOnDevice(this, true, ime, new ModelManagerListener() {
            @Override
            public void onSuccess(String json) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub
            }
        });

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arrTabs.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return arrTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

        public Fragment getActiveFragment(ViewPager container, int position) {
            String name = makeFragmentName(container.getId(), position);
            return getSupportFragmentManager().findFragmentByTag(name);
        }

        private String makeFragmentName(int viewId, int index) {
            return "android:switcher:" + viewId + ":" + index;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data
                    .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", "Payment OK. Response :"
                            + confirm.toJSONObject().toString(4));
                    Toast toast = Toast.makeText(self, self.getResources().getString(R.string.payment_successfully),
                            Toast.LENGTH_LONG);
                    toast.setGravity(
                            Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    if (CURRENT_FRAGMENT == TAB_CART) {
                        CartOrderFragment fragmentCart = (CartOrderFragment) ((MyPagerAdapter) viewPager
                                .getAdapter()).getActiveFragment(viewPager,
                                TAB_CART);
                        fragmentCart
                                .sendListOrder(CartOrderFragment.PAYPAL_METHOD);
                    }

                } catch (JSONException e) {
                    Log.e("paymentExample",
                            "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i("paymentExample",
                    "An invalid payment was submitted. Please see the docs.");
        }
    }

    private void getTable(String user_id) {
        ModelManager.getListTable(self, true, user_id,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        GlobalValue.getInstance().setArrTableObject(
                                ParserUtility.parseListTable(json));
                        showDialogTable();
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.have_some_error));
                    }
                });
    }

    private void getCustomers() {
        ModelManager.getListActiveCustomersForWaiter(self, true,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParserUtility.isSuccess(json)) {
                            arrCustomers = ParserUtility.parseListCustomersForWaiter(json);
                        }
                        showDialogCustomers();
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.have_some_error));
                    }
                });
    }

    public void showDialogCustomers() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_select_table);
        dialog.setCancelable(false);
        ListView lsvTable = (ListView) dialog.findViewById(R.id.lsvTable);
        TextView lblClose = (TextView) dialog.findViewById(R.id.lblCancel);
        TextView txtHeader = (TextView) dialog.findViewById(R.id.txtHeader);
        txtHeader.setText(self.getResources().getString(R.string.select_customer));
        arrCustomers.add(0, getString(R.string.new_customer));
        // set value list category :
        ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, this.arrCustomers);
        lsvTable.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();

        lblClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lsvTable.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CartOrderFragment fragmentCart = (CartOrderFragment) ((MyPagerAdapter) viewPager
                        .getAdapter()).getActiveFragment(viewPager, TAB_CART);
                fragmentCart.onSelectCustomer(arrCustomers.get(position));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showDialogTable() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_select_table);
        dialog.setCancelable(false);
        ListView lsvTable = (ListView) dialog.findViewById(R.id.lsvTable);
        TextView lblClose = (TextView) dialog.findViewById(R.id.lblCancel);

        // set value list category :
        tableAdapter = new TableAdapter(this, GlobalValue.getInstance()
                .getArrTableObject());
        lsvTable.setAdapter(tableAdapter);
        tableAdapter.notifyDataSetChanged();

        lblClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lsvTable.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GlobalValue.getInstance().setCurrentTable(
                        GlobalValue.getInstance().getArrTableObject()
                                .get(position));
                lblTable.setText(GlobalValue.getInstance().getCurrentTable()
                        .getName());
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

