package com.hcpt.fastfood.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.adapter.MyOrderAdapter;
import com.hcpt.fastfood.adapter.StatusAdapter;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.OrderHistory;
import com.hcpt.fastfood.object.User;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.hcpt.fastfood.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyOrderActivity extends BaseActivity {
    private ArrayList<String> arrCustomers;
    private Button btnTopBack, btnRefresh, btnCustomer, btnUpdateStatus;
    private TextView lblHeader;
    private String TAG = "MyOrderActivity";
    private ArrayList<OrderHistory> listOrderHistory;
    private ListView lsvActually;

    private MyOrderAdapter itemAdapter;
    private PullToRefreshListView lsvItems;

    private int mInterval = 2; // 5 seconds by default, can be changed later
    private Timer mTimer;
    public static final String MY_PREFS_NAME = "LucasApp";
    private User currentUser = new User();
    private int currentPage = 1, maxPage = 2;
    public String currentCustomer = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "Cuong: onCreate");
        setContentView(R.layout.activity_my_order);
        currentUser = GlobalValue.getInstance().currentUser;
        listOrderHistory = new ArrayList<OrderHistory>();
        initUI();
        initControl();
        getValue();
        autoRefreshEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void getValue() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,
                MODE_PRIVATE);
        mInterval = prefs.getInt("refreshAfter", 5);
    }

    private void initControl() {
        lblHeader.setText(getString(R.string.lbl_my_order));
        btnTopBack.setBackgroundResource(R.drawable.btn_back);

        btnRefresh.setBackgroundResource(R.drawable.ic_action_refresh);
        btnTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {
            btnCustomer.setVisibility(View.VISIBLE);
            btnCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCustomers();
                }
            });
        } else {
            btnCustomer.setVisibility(View.GONE);
            btnCustomer.setOnClickListener(null);
        }

        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusDialog();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        itemAdapter = new MyOrderAdapter(this, listOrderHistory);
        lsvItems.setAdapter(itemAdapter);
        lsvItems.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GlobalValue.getInstance().currentOrder = listOrderHistory
                        .get(position - 1);
                GlobalValue.getInstance().detailScreenType = Constant.SCREEN_TYPE_MY_ORDER;
                gotoActivity(OrderDetailNewActivity.class);
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
                refresh();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                getData();
            }
        });
    }

    private void showStatusDialog() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_order_status);
        dialog.setCancelable(false);

        ListView lsvStatus = (ListView) dialog.findViewById(R.id.lsvStatus);
        TextView lblSubmit = (TextView) dialog.findViewById(R.id.lblSubmit);
        TextView lblCancel = (TextView) dialog.findViewById(R.id.lblCancel);

        final StatusAdapter adapter = new StatusAdapter(this,
                currentUser.getRole());
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
                dialogSub.setNegativeButton(
                        getResources().getString(R.string.lbl_yes),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogSub,
                                                int which) {
                                dialogSub.dismiss();
                                dialog.dismiss();
                                // save status
                                updateOrderByWaiter(currentCustomer, adapter
                                        .getNumberStatusByStringName());
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

    private void updateOrderByWaiter(String customer, String status) {
        ModelManager.updateOrderByWaiter(this, true, customer, status,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParserUtility.isSuccess(json)) {
                            showToastMessage(ParserUtility.getMessage(json));
                            refresh();
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

    private void refresh() {
        currentPage = 1;
        maxPage = 2;
        listOrderHistory = new ArrayList<OrderHistory>();
        getData();
        if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {
            if (currentCustomer == "")
                btnUpdateStatus.setVisibility(View.GONE);
            else
                btnUpdateStatus.setVisibility(View.VISIBLE);
        }
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
        arrCustomers.add(0, GlobalValue.getInstance().getCurrentTable().getName());
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
                if (position == 0) // get orders by current table
                    currentCustomer = "";
                else
                    currentCustomer = arrCustomers.get(position);
                refresh();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void initUI() {
        lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
        btnTopBack = (Button) findViewById(R.id.btnLeft);
        btnUpdateStatus = (Button) findViewById(R.id.btnUpdateStatus);
        btnCustomer = (Button) findViewById(R.id.btnCustomer);
        lsvItems = (PullToRefreshListView) findViewById(R.id.lsvItems);
        btnRefresh = (Button) findViewById(R.id.btnRight);
        btnRefresh.setVisibility(View.VISIBLE);
        lblHeader.setText(getResources().getString(R.string.lbl_my_order));
    }

    private void getData() {
        // Get data from Globa
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
            String userId = "";
            String type = "";
            if (GlobalValue.getInstance().isLogin) {
                userId = currentUser.getId();
                type = "1";
            } else {
                TelephonyManager mngr = (TelephonyManager) self
                        .getSystemService(Context.TELEPHONY_SERVICE);
                userId = mngr.getDeviceId();
                type = "0";
            }
            ModelManager.showOrder(this, true, userId, userId, type,
                    currentPage + "", GlobalValue.getInstance().getCurrentTable().getId(), currentCustomer, new ModelManagerListener() {
                        @Override
                        public void onSuccess(String json) {
                            if (ParserUtility.isSuccess(json)) {
                                listOrderHistory.addAll(ParserUtility
                                        .parseListOrder(json));
                                itemAdapter
                                        .setListOrderHistory(listOrderHistory);
                                itemAdapter.notifyDataSetChanged();
                                lsvItems.onRefreshComplete();
                                currentPage++;
                                maxPage = ParserUtility.getMaxPage(json);
                                if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER)) {
                                    if (currentCustomer == "")
                                        lblHeader.setText(GlobalValue.getInstance().getCurrentTable().getName());
                                    else
                                        lblHeader.setText(currentCustomer);
                                }
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
                            showToastMessage(getResources().getString(R.string.have_some_error));
                        }
                    });
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
