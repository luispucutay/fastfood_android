/*
 * Name: $RCSfile: ParameterFactory.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 2:45:36 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.hcpt.fastfood.modelmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.object.ItemCart;

/**
 * ParameterFactory class builds parameters for web service apis
 *
 */
public final class ParameterFactory {

//	public static List<NameValuePair> createSendFeedbackParams(String email,
//															   String content) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("email", email));
//		parameters.add(new BasicNameValuePair("content", content));
//
//		return parameters;
//	}
//
//	public static List<NameValuePair> createOrderParams(String name,
//														String phone, ArrayList<ItemCart> arr, String paymentMethod,
//														String time, String address, String userId, String type,int tableId, int seatNumber) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("address", address));
//		parameters.add(new BasicNameValuePair("payment_type", paymentMethod));
//		parameters.add(new BasicNameValuePair("time", time));
//		parameters.add(new BasicNameValuePair("name", name));
//		parameters.add(new BasicNameValuePair("tel", phone));
//		parameters.add(new BasicNameValuePair("userId", userId));
//		parameters.add(new BasicNameValuePair("deviceId", userId));
//		parameters.add(new BasicNameValuePair("type", type));
//		parameters.add(new BasicNameValuePair("seats_number", seatNumber + ""));
//		parameters.add(new BasicNameValuePair("table_id", tableId + ""));
//
//		for (int i = 0; i < arr.size(); i++) {
//			parameters.add(new BasicNameValuePair("products[" + i + "]",
//					ParserUtility.convertCartItemToJson(arr.get(i))));
//		}
//
//		return parameters;
//	}
//
//	// Login
//	public static List<NameValuePair> createLoginParams(String userName,
//														String password, String ime) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("username", userName));
//		parameters.add(new BasicNameValuePair("password", password));
//		parameters.add(new BasicNameValuePair("ime", ime));
//		return parameters;
//	}
//
//	// Register
//	public static List<NameValuePair> createRegisterParams(String userName,
//														   String password, String email, String full_nam, String phone,
//														   String address) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("username", userName));
//		parameters.add(new BasicNameValuePair("password", password));
//		parameters.add(new BasicNameValuePair("email", email));
//		parameters.add(new BasicNameValuePair("full_name", full_nam));
//		parameters.add(new BasicNameValuePair("phone", phone));
//		parameters.add(new BasicNameValuePair("address", address));
//
//		return parameters;
//	}
//
//	public static List<NameValuePair> createResetUserIdOnDeviceParams(String ime) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("action", "resetUserId"));
//		parameters.add(new BasicNameValuePair("ime", ime));
//		return parameters;
//	}
//
//	public static List<NameValuePair> createCheckDeviceStatusParams(String ime) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("action", "checkDeviceStatus"));
//		parameters.add(new BasicNameValuePair("ime", ime));
//		return parameters;
//	}
//
//	// Register Device
//	public static List<NameValuePair> createRegisterDeviceParams(String gcm_id,
//																 String ime, String userId) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("gcm_id", gcm_id));
//		parameters.add(new BasicNameValuePair("type", "1"));
//		parameters.add(new BasicNameValuePair("status", "-1")); // -1: because do not want to update status if device existing
//		parameters.add(new BasicNameValuePair("ime", ime));
//		parameters.add(new BasicNameValuePair("userId", userId));
//		return parameters;
//	}
//
//	// Forget Password
//	public static List<NameValuePair> createForgetPasswordParams(String email) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("email", email));
//		return parameters;
//	}
//
//	// Change Password
//	public static List<NameValuePair> createChangePasswordParams(String userId,
//																 String currentPass, String newPass) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("userId", userId));
//		parameters.add(new BasicNameValuePair("current_pass", currentPass));
//		parameters.add(new BasicNameValuePair("new_pass", newPass));
//		return parameters;
//	}
//
//	// Update profile
//	public static List<NameValuePair> updateProfileParams(String userId,
//														  String userName, String password, String email, String full_nam,
//														  String phone, String address) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("userId", userId));
//		parameters.add(new BasicNameValuePair("username", userName));
//		parameters.add(new BasicNameValuePair("password", password));
//		parameters.add(new BasicNameValuePair("email", email));
//		parameters.add(new BasicNameValuePair("full_name", full_nam));
//		parameters.add(new BasicNameValuePair("phone", phone));
//		parameters.add(new BasicNameValuePair("address", address));
//
//		return parameters;
//	}
//
//	// Show Order
//	public static List<NameValuePair> createShowOrderParams(String userId,
//															String deviceId, String type, String page, String tableId, String customer) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("page", page));
//		parameters.add(new BasicNameValuePair("userId", userId));
//		parameters.add(new BasicNameValuePair("deviceId", deviceId));
//		parameters.add(new BasicNameValuePair("type", type));
//		parameters.add(new BasicNameValuePair("table_id", tableId + ""));
//		parameters.add(new BasicNameValuePair("customer", customer));
//		return parameters;
//	}
//
//	// Show All Order
//	public static List<NameValuePair> createShowAllOrderParams(String role,
//															   String page) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("role", role));
//		parameters.add(new BasicNameValuePair("page", page));
//		return parameters;
//	}
//
//	// Show All Order
//	public static List<NameValuePair> createShowAllOrderParamsAdmin(
//			String role, String page, String status) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("role", role));
//		parameters.add(new BasicNameValuePair("page", page));
//		if (status != Constant.STATUS_ALL) {
//			parameters.add(new BasicNameValuePair("status", status));
//		}
//		return parameters;
//	}
//
//	// Update Order for chef
//	public static List<NameValuePair> updateOrderForChef(String orderId,
//														 String chefId) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("orderId", orderId));
//		parameters.add(new BasicNameValuePair("chefId", chefId));
//		return parameters;
//	}
//
//	// Update reject Order for chef
//	public static List<NameValuePair> updateRejectOrderForChef(String orderId,
//															   String chefId) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("orderId", orderId));
//		parameters.add(new BasicNameValuePair("chefId", chefId));
//		parameters.add(new BasicNameValuePair("status", "reject"));
//		return parameters;
//	}
//
//	// Update Order for deliveryId
//	public static List<NameValuePair> updateOrderForDelivery(String orderId,
//															 String deliveryId) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("orderId", orderId));
//		parameters.add(new BasicNameValuePair("deliveryId", deliveryId));
//		return parameters;
//	}
//
//	// Update Order
//	public static List<NameValuePair> createUpdateOrderParamsChef(
//			String orderId, String status, String chefId) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("orderId", orderId));
//		parameters.add(new BasicNameValuePair("status", status));
//		parameters.add(new BasicNameValuePair("chefId", chefId));
//		return parameters;
//	}
//
//	public static List<NameValuePair> createUpdateOrderParamsDelivery(
//			String orderId, String status, String deliveryId) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("orderId", orderId));
//		parameters.add(new BasicNameValuePair("status", status));
//		parameters.add(new BasicNameValuePair("deliveryId", deliveryId));
//		return parameters;
//	}
//
//	// Show Order
//	public static List<NameValuePair> createDashBoardOrders(String option,
//															String page) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("option", option));
//		parameters.add(new BasicNameValuePair("page", page));
//		return parameters;
//	}
//
//	// Show Order
//	public static List<NameValuePair> createDashBoardOrdersByDay(String option,
//																 String startDate, String endDate) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("option", option));
//		parameters.add(new BasicNameValuePair("startDate", startDate));
//		parameters.add(new BasicNameValuePair("endDate", endDate));
//		return parameters;
//	}
//
//	// Show Order
//	public static List<NameValuePair> createUpdateOrdersByAdmin(String orderId,
//																String status) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("orderId", orderId));
//		parameters.add(new BasicNameValuePair("status", status));
//		return parameters;
//	}
//
//	public static List<NameValuePair> createUpdateOrdersByWaiter(String customer,
//																 String status) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("guest", customer));
//		parameters.add(new BasicNameValuePair("status", status));
//		return parameters;
//	}
//
//	// Show Page
//	public static List<NameValuePair> createPage(String page) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("page", page));
//		return parameters;
//	}
//
//	// Show Order
//	public static List<NameValuePair> createPushNotificationParam(String ime,
//																  String status) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("ime", ime));
//		parameters.add(new BasicNameValuePair("status", status));
//		return parameters;
//	}
//
//	// Forget Password
//	public static List<NameValuePair> createGetListTableParams(String user_id) {
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("user_id", user_id));
//		return parameters;
//	}

    /*----------------------------------------------------*/
    public static Map<String, String> createSendFeedbackParams(String email,
                                                               String content) {
        Map<String, String> params = new HashMap<>();

        params.put("email", email);
        params.put("content", content);

        return params;
    }

    public static Map<String, String> createOrderParams(String name,
                                                        String phone, ArrayList<ItemCart> arr, String paymentMethod,
                                                        String time, String address, String userId, String type, int tableId, int seatNumber) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("address", address);
        parameters.put("payment_type", paymentMethod);
        parameters.put("time", time);
        parameters.put("name", name);
        parameters.put("tel", phone);
        parameters.put("userId", userId);
        parameters.put("deviceId", userId);
        parameters.put("type", type);
        parameters.put("seats_number", seatNumber + "");
        parameters.put("table_id", tableId + "");

        for (int i = 0; i < arr.size(); i++) {
            parameters.put("products[" + i + "]",
                    ParserUtility.convertCartItemToJson(arr.get(i)));
        }

        return parameters;
    }

    // Login
    public static Map<String, String> createLoginParams(String userName,
                                                        String password, String ime) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", userName);
        parameters.put("password", password);
        parameters.put("ime", ime);
        return parameters;
    }

    // Register
    public static Map<String, String> createRegisterParams(String userName,
                                                           String password, String email, String full_nam, String phone,
                                                           String address) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", userName);
        parameters.put("password", password);
        parameters.put("email", email);
        parameters.put("full_name", full_nam);
        parameters.put("phone", phone);
        parameters.put("address", address);

        return parameters;
    }

    public static Map<String, String> createResetUserIdOnDeviceParams(String ime) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("action", "resetUserId");
        parameters.put("ime", ime);
        return parameters;
    }

    public static Map<String, String> createCheckDeviceStatusParams(String ime) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("action", "checkDeviceStatus");
        parameters.put("ime", ime);
        return parameters;
    }

    // Register Device
    public static Map<String, String> createRegisterDeviceParams(String gcm_id,
                                                                 String ime, String userId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("gcm_id", gcm_id);
        parameters.put("type", "1");
        parameters.put("status", "-1"); // -1: because do not want to update status if device existing
        parameters.put("ime", ime);
        parameters.put("userId", userId);
        return parameters;
    }

    // Forget Password
    public static Map<String, String> createForgetPasswordParams(String email) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        return parameters;
    }

    // Change Password
    public static Map<String, String> createChangePasswordParams(String userId,
                                                                 String currentPass, String newPass) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("current_pass", currentPass);
        parameters.put("new_pass", newPass);
        return parameters;
    }

    // Update profile
    public static Map<String, String> updateProfileParams(String userId,
                                                          String userName, String password, String email, String full_nam,
                                                          String phone, String address) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("username", userName);
        parameters.put("password", password);
        parameters.put("email", email);
        parameters.put("full_name", full_nam);
        parameters.put("phone", phone);
        parameters.put("address", address);

        return parameters;
    }

    // Show Order
    public static Map<String, String> createShowOrderParams(String userId,
                                                            String deviceId, String type, String page, String tableId, String customer) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", page);
        parameters.put("userId", userId);
        parameters.put("deviceId", deviceId);
        parameters.put("type", type);
        parameters.put("table_id", tableId + "");
        parameters.put("customer", customer);
        return parameters;
    }

    // Show All Order
    public static Map<String, String> createShowAllOrderParams(String role,
                                                               String page) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("role", role);
        parameters.put("page", page);
        return parameters;
    }

    // Show All Order
    public static Map<String, String> createShowAllOrderParamsAdmin(
            String role, String page, String status) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("role", role);
        parameters.put("page", page);
        if (status != Constant.STATUS_ALL) {
            parameters.put("status", status);
        }
        return parameters;
    }

    // Update Order for chef
    public static Map<String, String> updateOrderForChef(String orderId,
                                                         String chefId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        parameters.put("chefId", chefId);
        return parameters;
    }

    // Update reject Order for chef
    public static Map<String, String> updateRejectOrderForChef(String orderId,
                                                               String chefId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        parameters.put("chefId", chefId);
        parameters.put("status", "reject");
        return parameters;
    }

    // Update Order for deliveryId
    public static Map<String, String> updateOrderForDelivery(String orderId,
                                                             String deliveryId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        parameters.put("deliveryId", deliveryId);
        return parameters;
    }

    // Update Order
    public static Map<String, String> createUpdateOrderParamsChef(
            String orderId, String status, String chefId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        parameters.put("status", status);
        parameters.put("chefId", chefId);
        return parameters;
    }

    public static Map<String, String> createUpdateOrderParamsDelivery(
            String orderId, String status, String deliveryId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        parameters.put("status", status);
        parameters.put("deliveryId", deliveryId);
        return parameters;
    }

    // Show Order
    public static Map<String, String> createDashBoardOrders(String option,
                                                            String page) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("page", page);
        return parameters;
    }

    // Show Order
    public static Map<String, String> createDashBoardOrdersByDay(String option,
                                                                 String startDate, String endDate) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("option", option);
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
        return parameters;
    }

    // Show Order
    public static Map<String, String> createUpdateOrdersByAdmin(String orderId,
                                                                String status) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        parameters.put("status", status);
        return parameters;
    }

    public static Map<String, String> createUpdateOrdersByWaiter(String customer,
                                                                 String status) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("guest", customer);
        parameters.put("status", status);
        return parameters;
    }

    // Show Page
    public static Map<String, String> createPage(String page) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", page);
        return parameters;
    }

    // Show Order
    public static Map<String, String> createPushNotificationParam(String ime,
                                                                  String status) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("ime", ime);
        parameters.put("status", status);
        return parameters;
    }

    // Forget Password
    public static Map<String, String> createGetListTableParams(String user_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user_id", user_id);
        return parameters;
    }
}