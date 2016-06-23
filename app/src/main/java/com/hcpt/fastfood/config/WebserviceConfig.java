package com.hcpt.fastfood.config;

import android.content.Context;

import com.hcpt.fastfood.R;

public class WebserviceConfig {

    public static String GET_MENU_LIST(Context ct) {
        return ct.getString(R.string.server_backend_link) + "dish/menu/list";
    }

    public static String GET_CONTACT(Context ct) {
        return ct.getString(R.string.server_backend_link) + "restaurant/contact";
    }

    public static String GET_ORDER(Context ct) {
        return ct.getString(R.string.server_backend_link) + "order";
    }

    public static String GET_LOGIN(Context ct) {
        return ct.getString(R.string.server_backend_link) + "login";
    }

    public static String GET_REGISTER(Context ct) {
        return ct.getString(R.string.server_backend_link) + "register";
    }

    public static String GET_DEVICE_REGISTER(Context ct) {
        return ct.getString(R.string.server_backend_link) + "deviceRegister";
    }

    public static String GET_FORGET_PASSWORD(Context ct) {
        return ct.getString(R.string.server_backend_link) + "forgetPassword";
    }

    public static String GET_CHANGE_PASSWORD(Context ct) {
        return ct.getString(R.string.server_backend_link) + "changePassword";
    }

    public static String GET_UPDATE_PROFILE(Context ct) {
        return ct.getString(R.string.server_backend_link) + "updateProfile";
    }
    public static String GET_ORDER_HISTORY(Context ct) {
        return ct.getString(R.string.server_backend_link) + "orderHistory";
    }

    public static String GET_UPDATE_ORDER_STATUS_BY_DELIVERY(Context ct) {
        return ct.getString(R.string.server_backend_link) + "updateOrderStatusByDelivery";
    }
    public static String GET_UPDATE_ORDER(Context ct) {
        return ct.getString(R.string.server_backend_link) + "updateOrder";
    }

    public static String GET_UPDATE_ORDER_STATUS_BY_CHEF(Context ct) {
        return ct.getString(R.string.server_backend_link) + "updateOrderStatusByChef";
    }

    public static String GET_VIEW_ORDER_BY_ROLE(Context ct) {
        return ct.getString(R.string.server_backend_link) + "viewOrdersByRole";
    }

    public static String GET_DASH_BOARD_ORDER(Context ct) {
        return ct.getString(R.string.server_backend_link) + "dashBoardOrders";
    }

    public static String GET_UPDATE_ORDER_STATUS_BY_WAITER(Context ct) {
        return ct.getString(R.string.server_backend_link) + "updateOrderStatusByWaiter";
    }

    public static String GET_UPDATE_ORDER_STATUS_BY_ADMIN(Context ct) {
        return ct.getString(R.string.server_backend_link) + "updateOrderStatusByAdmin";
    }

    public static String GET_PROMOTION(Context ct) {
        return ct.getString(R.string.server_backend_link) + "promotion";
    }

    public static String GET_SHOW_TABLE_LIST(Context ct) {
        return ct.getString(R.string.server_backend_link) + "showTableList";
    }

    public static String GET_SHOW_GUEST_TABLE(Context ct) {
        return ct.getString(R.string.server_backend_link) + "showGuestTable";
    }

    public static String GET_SHOW_MENU(Context ct) {
        return ct.getString(R.string.server_backend_link) + "showMenu";
    }

    public static String GET_LIST_STREAM(Context ct) {
        return ct.getString(R.string.server_backend_link) + "stream";
    }

    public static String GET_SHOW_FOOD_BY_MENU(Context ct) {
        return ct.getString(R.string.server_backend_link) + "showFoodByMenu";
    }
}
