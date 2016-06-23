package com.hcpt.fastfood.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcpt.fastfood.BaseFragment;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.activity.DashboardActivity;
import com.hcpt.fastfood.activity.MainActivity;
import com.hcpt.fastfood.activity.MyOrderActivity;
import com.hcpt.fastfood.activity.NewOrderActivity;
import com.hcpt.fastfood.activity.OrderActivityAdmin;
import com.hcpt.fastfood.activity.RegisterActivity;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.User;
import com.hcpt.fastfood.utility.StringUtility;

public class AccountFragment extends BaseFragment implements OnClickListener {

    private EditText txtName;
    private EditText txtPassword;
    private TextView txtForgotPassword;
    private TextView btnLogin;
    private TextView btnRegister;
    private TextView btnViewOrder;
    private TextView lbl_myProfile;
    private TextView lblNewOrder;
    private LinearLayout layoutMyProfile;
    private LinearLayout layoutChangePass;
    private LinearLayout layoutViewOrder;
    private LinearLayout layoutMyOrder;
    private LinearLayout layoutNoLoggedIn, layoutLoggedIn;
    private User currentUser = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        self = getActivity();
        initUI(view);
        initControl();
        initData();
        return view;
    }

    private void initControl() {
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        btnViewOrder.setOnClickListener(this);
        layoutMyProfile.setOnClickListener(this);
        layoutChangePass.setOnClickListener(this);
        layoutViewOrder.setOnClickListener(this);
        layoutMyOrder.setOnClickListener(this);
    }

    private void initUI(View view) {
        txtName = (EditText) view.findViewById(R.id.txtName);
        txtPassword = (EditText) view.findViewById(R.id.txtPassword);
        txtForgotPassword = (TextView) view.findViewById(R.id.txtForgotPassword);
        btnLogin = (TextView) view.findViewById(R.id.btnLogin);
        btnRegister = (TextView) view.findViewById(R.id.btnRegister);
        btnViewOrder = (TextView) view.findViewById(R.id.btnViewOrder);
        lbl_myProfile = (TextView) view.findViewById(R.id.lbl_myProfile);
        lblNewOrder = (TextView) view.findViewById(R.id.lblNewOrder);
        layoutMyProfile = (LinearLayout) view.findViewById(R.id.layoutMyProfile);
        layoutChangePass = (LinearLayout) view.findViewById(R.id.layoutChangePass);
        layoutViewOrder = (LinearLayout) view.findViewById(R.id.layoutViewOrder);
        layoutMyOrder = (LinearLayout) view.findViewById(R.id.layoutMyOrder);
        layoutLoggedIn = (LinearLayout) view.findViewById(R.id.layoutLoggedIn);
        layoutNoLoggedIn = (LinearLayout) view.findViewById(R.id.layoutNoLoggedIn);
        updateViewLogin();
    }

    public void initData() {
        currentUser = GlobalValue.getInstance().currentUser;
    }

    public void setHeaderTitle() {
        String title = getString(R.string.account);
        if (GlobalValue.getInstance().isRole(Constant.ROLE_ADMIN_USER))
            title = getString(R.string.lbl_Admin);
        else if (GlobalValue.getInstance().isRole(Constant.ROLE_CHEF_USER))
            title = getString(R.string.lbl_Chef);
        else if (GlobalValue.getInstance().isRole(Constant.ROLE_NORMAL_USER))
            title = getString(R.string.account);
        else if (GlobalValue.getInstance().isRole(Constant.ROLE_USER_DELIVERY))
            title = getString(R.string.lbl_Delivery);
        else if (GlobalValue.getInstance().isRole(Constant.ROLE_WAITER_USER))
            title = getString(R.string.lbl_Waiter);
        ((MainActivity) getActivity()).setHeaderTitle(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String userName = txtName.getText().toString();
                String userPass = txtPassword.getText().toString();
                if (userName.isEmpty() || userName == null || userPass.isEmpty() || userPass == null) {
                    showToastMessage(getResources().getString(
                            R.string.please_enter_user_name_and_password));
                } else {
                    login(userName, userPass);
                }
                break;
            case R.id.btnRegister:
                gotoActivity(RegisterActivity.class);
                break;
            case R.id.txtForgotPassword:
                showDialogForgotPassword();
                break;
            case R.id.btnViewOrder:
                gotoActivity(MyOrderActivity.class);
                break;
            case R.id.layoutMyProfile:
                if (currentUser.getRole().equals(Constant.ROLE_ADMIN_USER)) {
                    gotoActivity(DashboardActivity.class);
                } else {
                    gotoActivity(RegisterActivity.class);
                }
                break;
            case R.id.layoutChangePass:
                showDialogChangePassword();
                break;
            case R.id.layoutViewOrder:
                if (currentUser.getRole().equals(Constant.ROLE_ADMIN_USER)) {
                    gotoActivity(OrderActivityAdmin.class);
                } else {
                    gotoActivity(NewOrderActivity.class);
                }
                break;
            case R.id.layoutMyOrder:
                gotoActivity(MyOrderActivity.class);
                break;
        }
    }

    @Override
    public void onLogIn() {
        if (currentUser.getRole().equals(Constant.ROLE_ADMIN_USER)) {
            lblNewOrder.setText(R.string.lbl_all_order);
        } else {
            lblNewOrder.setText(R.string.lbl_view_order);
        }
        setHeaderTitle();
    }

    @Override
    public void onLogOut() {
        txtName.setText("");
        txtPassword.setText("");
        setHeaderTitle();
    }

    private void showDialogForgotPassword() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_forgot_password);
        dialog.setCancelable(false);
        final EditText txtEmail = (EditText) dialog.findViewById(R.id.txtEmail);
        TextView lblSend = (TextView) dialog.findViewById(R.id.lblSend);
        TextView lblCancel = (TextView) dialog.findViewById(R.id.lblCancel);

        lblCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lblSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                if (email.isEmpty() || email == null) {
                    showToastMessage(getResources().getString(
                            R.string.please_enter_your_email));
                } else {
                    if (StringUtility.isEmailValid(email)) {
                        forgotPassword(email);
                        dialog.dismiss();
                    } else {
                        showToastMessage(getResources().getString(
                                R.string.email_is_not_validated));
                    }
                }
            }
        });

        dialog.show();
    }

    private void showDialogChangePassword() {
        final Dialog dialog = new Dialog(self);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_change_password);
        dialog.setCancelable(false);
        TextView lblSubmit = (TextView) dialog.findViewById(R.id.lblSubmit);
        TextView lblCancel = (TextView) dialog.findViewById(R.id.lblCancel);
        final EditText txtcurrentPassword = (EditText) dialog.findViewById(R.id.txtcurrentPassword);
        final EditText txtNewPassword = (EditText) dialog.findViewById(R.id.txtNewPassword);
        final EditText txtConfirmPass = (EditText) dialog.findViewById(R.id.txtConfirmPass);

        lblCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lblSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userId = GlobalValue.getInstance().getCurrentUser()
                        .getId();
                String currentPassword = txtcurrentPassword.getText()
                        .toString();
                String newPassword = txtNewPassword.getText().toString();
                String confirmPass = txtConfirmPass.getText().toString();

                if (currentPassword.isEmpty() || newPassword.isEmpty()
                        || confirmPass.isEmpty()) {
                    showToastMessage(getResources().getString(
                            R.string.lbl_please_enter_full));
                } else {
                    if (!newPassword.equals(confirmPass)) {
                        showToastMessage(getResources().getString(
                                R.string.passwords_do_not_match));
                    } else {
                        changePassword(userId, currentPassword, newPassword);
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }

    private void login(String userName, String password) {
        String ime = GlobalValue.getInstance().getIME(getActivity());
        ModelManager.login(self, true, userName, password, ime,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        if (ParserUtility.isSuccess(json)) {
                            GlobalValue.getInstance().setCurrentUser(
                                    ParserUtility.parseUser(json));
                            currentUser = ParserUtility.parseUser(json);
                            GlobalValue.getInstance().setLogin(true);
                            updateViewLogin();
                            ((MainActivity) getActivity()).login(json);
                            getTable(ParserUtility.parseUser(json).getId());
                        } else {
                            showToastMessage(getResources().getString(
                                    R.string.login_fail));
                        }
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.have_some_error));
                    }
                });
    }

    private void getTable(String user_id) {
        ModelManager.getListTable(self, true, user_id,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        GlobalValue.getInstance().setArrTableObject(
                                ParserUtility.parseListTable(json));
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.have_some_error));
                    }
                });
    }

    private void forgotPassword(String email) {
        ModelManager.forgetPassword(self, true, email,
                new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        showToastMessage(ParserUtility.getMessage(json));
                    }

                    @Override
                    public void onError() {
                        showToastMessage(getResources().getString(
                                R.string.have_some_error));
                    }
                });
    }

    private void changePassword(String userId, String currentPassword,
                                String newPassword) {
        ModelManager.changePassword(self, true, userId, currentPassword,
                newPassword, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                    if (ParserUtility.isSuccess(json)) {
                        showToastMessage(getResources().getString(
                                R.string.change_pass));
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

        @Override
        public void onResume() {
            super.onResume();
            updateViewLogin();
        }

    public void updateViewLogin() {
        if (GlobalValue.getInstance().isLogin) {
            layoutLoggedIn.setVisibility(View.VISIBLE);
            layoutNoLoggedIn.setVisibility(View.GONE);
            currentUser = GlobalValue.getInstance().currentUser;
            if (currentUser.getRole().equals(Constant.ROLE_ADMIN_USER)) {
                layoutMyProfile.setVisibility(View.VISIBLE);
                layoutChangePass.setVisibility(View.VISIBLE);
                layoutViewOrder.setVisibility(View.VISIBLE);
                layoutMyOrder.setVisibility(View.GONE);
                lbl_myProfile.setText(getActivity().getResources().getString(R.string.lbl_dashboard));
                GlobalValue.getInstance().arrCartItems.clear();
            } else if (currentUser.getRole().equals(Constant.ROLE_CHEF_USER)) {
                layoutMyProfile.setVisibility(View.VISIBLE);
                layoutChangePass.setVisibility(View.VISIBLE);
                layoutViewOrder.setVisibility(View.VISIBLE);
                layoutMyOrder.setVisibility(View.VISIBLE);
                lbl_myProfile.setText(getActivity().getResources().getString(R.string.lbl_my_profile));
            } else if (currentUser.getRole().equals(Constant.ROLE_WAITER_USER)) {
                layoutMyProfile.setVisibility(View.VISIBLE);
                layoutChangePass.setVisibility(View.VISIBLE);
                layoutViewOrder.setVisibility(View.GONE);
                layoutMyOrder.setVisibility(View.VISIBLE);
                lbl_myProfile.setText(getActivity().getResources().getString(R.string.lbl_my_profile));
            } else if (currentUser.getRole().equals(Constant.ROLE_USER_DELIVERY)) {
                layoutMyProfile.setVisibility(View.VISIBLE);
                layoutChangePass.setVisibility(View.VISIBLE);
                layoutViewOrder.setVisibility(View.VISIBLE);
                layoutMyOrder.setVisibility(View.VISIBLE);
                lbl_myProfile.setText(getActivity().getResources().getString(R.string.lbl_my_profile));
            } else {
                layoutMyProfile.setVisibility(View.VISIBLE);
                layoutChangePass.setVisibility(View.VISIBLE);
                layoutViewOrder.setVisibility(View.GONE);
                layoutMyOrder.setVisibility(View.VISIBLE);
                lbl_myProfile.setText(getActivity().getResources().getString(R.string.lbl_my_profile));
            }
        } else {
            layoutLoggedIn.setVisibility(View.GONE);
            layoutNoLoggedIn.setVisibility(View.VISIBLE);
        }
    }
}
