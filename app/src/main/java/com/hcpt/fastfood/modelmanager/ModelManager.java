package com.hcpt.fastfood.modelmanager;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.WebserviceConfig;
import com.hcpt.fastfood.object.ItemCart;
import com.hcpt.fastfood.volley.HttpError;
import com.hcpt.fastfood.volley.HttpGet;
import com.hcpt.fastfood.volley.HttpListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private static String TAG = "ModelManager";

    /*----------------------------------------------------------------*/
    public static void getAlldata(Context context, boolean isProgess,
                                  final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, WebserviceConfig.GET_MENU_LIST(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void sendFeedBack(Context context, String email,
                                    String content, boolean isProgess,
                                    final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createSendFeedbackParams(email, content);
        new HttpGet(context, WebserviceConfig.GET_CONTACT(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void sendProductOrder(Context context, String name,
                                        String phone, ArrayList<ItemCart> arrCart, String paymentMethod,
                                        String time, String address, String userId, String type, int tableId, int seatNumber,
                                        boolean isProgess, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createOrderParams(name, phone, arrCart, paymentMethod, time,
                address, userId, type, tableId, seatNumber);
        new HttpGet(context, WebserviceConfig.GET_ORDER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    JSONObject jsonObj;
                    try {
                        jsonObj = new JSONObject((String) response);
                        if (jsonObj.getString("status")
                                .equalsIgnoreCase("SUCCESS")) {
                            listener.onSuccess((String) response);
                        } else {
                            listener.onError();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        listener.onError();
                    }
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Login
    public static void login(Context context, boolean isProgess,
                             String userName, String password, String ime,
                             final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createLoginParams(userName, password, ime);
        new HttpGet(context, WebserviceConfig.GET_LOGIN(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Register
    public static void register(Context context, boolean isProgess,
                                String userName, String password, String email, String full_name,
                                String phone, String address, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createRegisterParams(userName, password, email, full_name,
                phone, address);
        new HttpGet(context, WebserviceConfig.GET_REGISTER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Register Device
    public static void registerDevice(Context context, boolean isProgess,
                                      String gcm_id, String ime, String userId,
                                      final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createRegisterDeviceParams(gcm_id, ime, userId);
        new HttpGet(context, WebserviceConfig.GET_DEVICE_REGISTER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // reset UserId on Device
    public static void resetUserIdOnDevice(Context context, boolean isProgess, String ime,
                                           final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createResetUserIdOnDeviceParams(ime);
        new HttpGet(context, WebserviceConfig.GET_DEVICE_REGISTER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // check device status
    public static void checkDeviceStatus(Context context, boolean isProgess, String ime,
                                         final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createCheckDeviceStatusParams(ime);
        new HttpGet(context, WebserviceConfig.GET_DEVICE_REGISTER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Forgot password
    public static void forgetPassword(Context context, boolean isProgess,
                                      String email, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createForgetPasswordParams(email);
        new HttpGet(context, WebserviceConfig.GET_FORGET_PASSWORD(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Change password
    public static void changePassword(Context context, boolean isProgess,
                                      String userId, String currentPass, String newPass,
                                      final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createChangePasswordParams(userId, currentPass, newPass);
        new HttpGet(context, WebserviceConfig.GET_CHANGE_PASSWORD(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Update profile
    public static void updateProfile(Context context, boolean isProgess,
                                     String userId, String userName, String password, String email,
                                     String full_name, String phone, String address,
                                     final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.updateProfileParams(userId, userName, password, email,
                full_name, phone, address);
        new HttpGet(context, WebserviceConfig.GET_UPDATE_PROFILE(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void showOrder(Context context, boolean isProgess,
                                 String userId, String deviceId, String type, String page, String tableId, String customer,
                                 final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createShowOrderParams(userId, deviceId, type, page, tableId, customer);
        new HttpGet(context, WebserviceConfig.GET_ORDER_HISTORY(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void updateOrderDelivery(Context context, boolean isProgess,
                                           String orderId, String status, String deliveryId,
                                           final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createUpdateOrderParamsDelivery(orderId, status, deliveryId);
        new HttpGet(context, WebserviceConfig.GET_UPDATE_ORDER_STATUS_BY_DELIVERY(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void updateNewOrderDelivery(Context context,
                                              boolean isProgess, String orderId, String status,
                                              String deliveryId, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createUpdateOrderParamsDelivery(orderId, status, deliveryId);
        new HttpGet(context, WebserviceConfig.GET_UPDATE_ORDER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void updateOrderChef(Context context, boolean isProgess,
                                       String orderId, String status, String chefId,
                                       final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createUpdateOrderParamsChef(orderId, status, chefId);
        new HttpGet(context, WebserviceConfig.GET_UPDATE_ORDER_STATUS_BY_CHEF(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void updateNewOrderChef(Context context, boolean isProgess,
                                          String orderId, String status, String deliveryId,
                                          final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createUpdateOrderParamsChef(orderId, status, deliveryId);
        new HttpGet(context, WebserviceConfig.GET_UPDATE_ORDER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show all new order for chef and delivery
    public static void showAllOrder(Context context, boolean isProgess,
                                    String role, String page, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createShowAllOrderParams(role, page);
        new HttpGet(context, WebserviceConfig.GET_VIEW_ORDER_BY_ROLE(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show all order
    public static void showAllOrderAdmin(Context context, boolean isProgess,
                                         String role, String page, String status,
                                         final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createShowAllOrderParamsAdmin(role, page, status);
        new HttpGet(context, WebserviceConfig.GET_VIEW_ORDER_BY_ROLE(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show all order
    public static void updateOrder(Context context, boolean isProgess,
                                   Boolean isReject, String orderId, String chefId, String deliveryId,
                                   final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        if (isReject) {
            params = ParameterFactory.updateRejectOrderForChef(orderId, chefId);
        } else {
            if (!chefId.equals("-1")) {
                params = ParameterFactory.updateOrderForChef(orderId, chefId);
            } else {
                params = ParameterFactory.updateOrderForDelivery(orderId, deliveryId);
            }
        }

        new HttpGet(context, WebserviceConfig.GET_UPDATE_ORDER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void showDashBoard(Context context, boolean isProgess,
                                     String option, String page, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createDashBoardOrders(option, page);
        new HttpGet(context, WebserviceConfig.GET_DASH_BOARD_ORDER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void showDashBoardByDate(Context context, boolean isProgess, String option,
                                           String startDate, String endDate,
                                           final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createDashBoardOrdersByDay(option, startDate, endDate);
        new HttpGet(context, WebserviceConfig.GET_DASH_BOARD_ORDER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void updateOrderByWaiter(Context context, boolean isProgess,
                                           String customer, String status, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createUpdateOrdersByWaiter(customer, status);
        new HttpGet(context, WebserviceConfig.GET_UPDATE_ORDER_STATUS_BY_WAITER(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });

    }

    public static void updateOrderByAdmin(Context context, boolean isProgess,
                                          String orderId, String status, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createUpdateOrdersByAdmin(orderId, status);
        new HttpGet(context, WebserviceConfig.GET_UPDATE_ORDER_STATUS_BY_ADMIN(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void getPromotion(Context context, boolean isProgess,
                                    String page, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createPage(page);
        new HttpGet(context, WebserviceConfig.GET_PROMOTION(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    // Show order
    public static void setPushNotification(Context context, boolean isProgess,
                                           String ime, String status, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createPushNotificationParam(ime, status);
        new HttpGet(context, WebserviceConfig.GET_PROMOTION(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getListTable(Context context, boolean isProgess,
                                    String user_id, final ModelManagerListener listener) {
        Map<String, String> params = ParameterFactory.createGetListTableParams(user_id);
        new HttpGet(context, WebserviceConfig.GET_SHOW_TABLE_LIST(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getListActiveCustomersForWaiter(Context context,
                                                       boolean isProgess, final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, WebserviceConfig.GET_SHOW_GUEST_TABLE(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getAllCategory(Context context, boolean isProgess,
                                      final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, WebserviceConfig.GET_SHOW_MENU(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getListStream(Context context, boolean isProgess,
                                      final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        new HttpGet(context, WebserviceConfig.GET_LIST_STREAM(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }

    public static void getFoodByCategory(String keyword, String menu_id,
                                         String page, Context context, boolean isProgess,
                                         final ModelManagerListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("menu_id", menu_id);
        params.put("page", page);
        new HttpGet(context, WebserviceConfig.GET_SHOW_FOOD_BY_MENU(context), params, isProgess, new HttpListener() {
            @Override
            public void onHttpRespones(Object response) {
                if (response != null) {
                    listener.onSuccess(response.toString());
                } else {
                    listener.onError();
                }
            }
        }, new HttpError() {
            @Override
            public void onHttpError(VolleyError volleyError) {
                listener.onError();
            }
        });
    }
}