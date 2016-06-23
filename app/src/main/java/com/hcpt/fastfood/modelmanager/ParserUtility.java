package com.hcpt.fastfood.modelmanager;

import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.object.Category;
import com.hcpt.fastfood.object.CookMethod;
import com.hcpt.fastfood.object.DashboardRowItemObject;
import com.hcpt.fastfood.object.Item;
import com.hcpt.fastfood.object.ItemCart;
import com.hcpt.fastfood.object.ItemHistory;
import com.hcpt.fastfood.object.OrderHistory;
import com.hcpt.fastfood.object.PromotionObject;
import com.hcpt.fastfood.object.Relish;
import com.hcpt.fastfood.object.RelishOption;
import com.hcpt.fastfood.object.Stream;
import com.hcpt.fastfood.object.TableObject;
import com.hcpt.fastfood.object.User;
import com.hcpt.fastfood.utility.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserUtility {

    private final static String KEY_DATA = "data";

    // demo
    public static ArrayList<Category> parseListCategories(String json) {
        ArrayList<Category> arr = new ArrayList<Category>();
        Category category = null;
        ArrayList<Relish> arrRelishs;
        ArrayList<RelishOption> arrRelishOptions;
        ArrayList<CookMethod> arrCookMethod, arrSubCookMethod;
        Relish relishItem;
        RelishOption relishOption;
        CookMethod cookmethodItem, subCookmethodItem;
        try {
            JSONObject object = new JSONObject(json);
            JSONObject item, relishJson, relishOptionJson, cookMethodJson, subCookMethodJson;
            JSONArray arrRelishJsons, arrCookMethodsJson, arrRelishOptionsJson, arrSubCookMethodsJson;

            JSONObject containerJson = object.getJSONObject(KEY_DATA);

            JSONArray items = containerJson.getJSONArray("menus");
            for (int i = 0; i < items.length(); i++) {
                item = items.getJSONObject(i);
                category = new Category();
                category.setId(item.getString("id"));
                category.setName(item.getString("name"));
                category.setPanini(item.getBoolean("panini"));

                // add relish list :
                arrRelishs = new ArrayList<Relish>();
                arrRelishJsons = item.getJSONArray("relishes");
                for (int j = 0; j < arrRelishJsons.length(); j++) {
                    relishJson = arrRelishJsons.getJSONObject(j);
                    relishItem = new Relish();
                    relishItem.setId(relishJson.getString("relish_id"));
                    relishItem.setName(relishJson.getString("relish_name"));
                    relishItem.setPrice(relishJson.getDouble("relish_price"));
                    // /get list relish option
                    arrRelishOptions = new ArrayList<RelishOption>();
                    arrRelishOptionsJson = relishJson.getJSONArray("relish_options");
                    RelishOption relishHard = new RelishOption();
                    relishHard.setId("0");
                    relishHard.setName(Constant.NONE);
                    arrRelishOptions.add(relishHard);
                    if (arrRelishOptionsJson.length() > 0) {
                        for (int k = 0; k < arrRelishOptionsJson.length(); k++) {
                            relishOption = new RelishOption();
                            relishOptionJson = arrRelishOptionsJson
                                    .getJSONObject(k);
                            relishOption.setId(relishOptionJson
                                    .getString("method_id"));
                            relishOption.setName(relishOptionJson
                                    .getString("method_name"));

                            arrRelishOptions.add(relishOption);
                        }
                    }
                    relishItem.setArrOptions(arrRelishOptions);
                    arrRelishs.add(relishItem);
                }
                category.setArrRelishs(arrRelishs);
                // ===
                // add relish list :
                arrCookMethod = new ArrayList<CookMethod>();
                arrCookMethodsJson = item.getJSONArray("cooking_methods");
                for (int j = 0; j < arrCookMethodsJson.length(); j++) {
                    cookMethodJson = arrCookMethodsJson.getJSONObject(j);
                    cookmethodItem = new CookMethod();
                    cookmethodItem.setId(cookMethodJson.getString("method_id"));
                    cookmethodItem.setName(cookMethodJson
                            .getString("method_name"));
                    // /get list relish option
                    arrSubCookMethod = new ArrayList<CookMethod>();
                    arrSubCookMethodsJson = cookMethodJson
                            .getJSONArray("method_sub");
                    for (int k = 0; k < arrSubCookMethodsJson.length(); k++) {
                        subCookmethodItem = new CookMethod();
                        subCookMethodJson = arrSubCookMethodsJson
                                .getJSONObject(k);
                        subCookmethodItem.setId(subCookMethodJson
                                .getString("method_id"));
                        subCookmethodItem.setName(subCookMethodJson
                                .getString("method_name"));

                        arrSubCookMethod.add(subCookmethodItem);
                    }
                    cookmethodItem.setArrSubMethods(arrSubCookMethod);
                    arrCookMethod.add(cookmethodItem);
                }
                category.setArrCookMethods(arrCookMethod);

                arr.add(category);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arr;
    }

    public static ArrayList<Item> parseListItem(String json) {

        ArrayList<Item> arr = new ArrayList<Item>();

        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONObject containerJson = jsonobj.getJSONObject(KEY_DATA);
            JSONArray items = containerJson.getJSONArray("foods");
            JSONObject itemJson;
            Item item;
            for (int i = 0; i < items.length(); i++) {
                item = new Item();
                itemJson = items.getJSONObject(i);
                item.setId(itemJson.getString("id"));
                item.setName(itemJson.getString("name"));
                item.setDesc(itemJson.getString("desc"));
                item.setThumb(itemJson.getString("thumb"));
                item.setMenuId(itemJson.getString("menu"));
                item.setPrice(itemJson.getDouble("price"));
                arr.add(item);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arr;
    }

    public static ArrayList<Stream> parseListStream(String json) {
        ArrayList<Stream> arr = new ArrayList<>();
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONArray items = jsonobj.getJSONArray(KEY_DATA);
            JSONObject itemJson;
            Stream stream;
            for (int i = 0; i < items.length(); i++) {
                stream = new Stream();
                itemJson = items.getJSONObject(i);
                stream.setId(itemJson.getString("id"));
                stream.setTitle(itemJson.getString("title"));
                stream.setDescription(itemJson.getString("description"));
                stream.setThumbnail(itemJson.getString("thumbnail"));
                stream.setChannelId(itemJson.getString("channelId"));
                stream.setLink(itemJson.getString("link"));
                stream.setPublishedAt(itemJson.getString("publishedAt"));
                arr.add(stream);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static String convertCartItemToJson(ItemCart item) {

        JSONObject json = new JSONObject();
        try {
            json.put("id", item.getItem().getId());
            json.put("sl", item.getQuantities());

            JSONArray arrJsonTopping = new JSONArray();
            JSONObject jsonTopping = null;
            for (Relish relish : item.getArrRelish()) {
                if (relish.getSelectedIndex() != 0) {
                    jsonTopping = new JSONObject();
                    jsonTopping.put(relish.getId(),
                            relish.getSelectedToppingOption());
                    arrJsonTopping.put(jsonTopping);
                }
            }
            json.put("toppings", arrJsonTopping);
            json.put("instruction", item.getInsTructions());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json.toString();
    }

    // Get json status
    public static boolean isSuccess(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            if (jsonobj.getString("status").equals("SUCCESS")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // Get json message
    public static String getMessage(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getString("message");
        } catch (Exception e) {
            return "";
        }
    }

    // Get user information
    public static User parseUser(String json) {
        User user = new User();
        try {
            JSONObject jsonobj = new JSONObject(json).getJSONObject("data");
            user.setId(jsonobj.getString("id"));
            user.setUser_name(jsonobj.getString("user_name"));
            user.setEmail(jsonobj.getString("email"));
            user.setFull_name(jsonobj.getString("full_name"));
            user.setPhone(jsonobj.getString("phone"));
            user.setAddress(jsonobj.getString("address"));
            user.setRole(jsonobj.getString("role"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    // Get data
    public static String getDataAsString(String json) {

        try {
            return new JSONObject(json).getString("data");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    // Get list order
    public static ArrayList<OrderHistory> parseListOrder(String json) {
        ArrayList<OrderHistory> arr = new ArrayList<OrderHistory>();
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONArray jsonArray = jsonobj.getJSONArray(KEY_DATA);
            JSONObject itemJson;
            OrderHistory item;
            for (int i = 0; i < jsonArray.length(); i++) {
                item = new OrderHistory();
                itemJson = jsonArray.getJSONObject(i);
                item.setId(itemJson.getString("id"));
                item.setOrderName(itemJson.getString("orderName"));
                item.setOrderPrice(itemJson.getString("orderPrice"));
                item.setCreated(itemJson.getString("orderTime"));
                item.setOrderAddress(itemJson.getString("orderAddress"));
                item.setOrderStatus(itemJson.getString("orderStatus"));
                item.setTotalItems(itemJson.getString("totalItems"));
                item.setOrderTel(itemJson.getString("orderTel"));

                JSONArray jsonArrayItem = itemJson.getJSONArray("orderFoods");
                JSONObject itemHistoryJson;
                for (int ii = 0; ii < jsonArrayItem.length(); ii++) {
                    ItemHistory itemHistory = new ItemHistory();
                    itemHistoryJson = jsonArrayItem.getJSONObject(ii);
                    itemHistory.setDishName(itemHistoryJson
                            .getString("dishName"));
                    itemHistory.setDishPrice(itemHistoryJson
                            .getString("dishPrice"));
                    itemHistory.setSl(itemHistoryJson.getString("sl"));
                    itemHistory.setTotal(itemHistoryJson.getString("total"));
                    itemHistory.setThumb(itemHistoryJson.getString("thumb"));
                    itemHistory.setCategory(itemHistoryJson
                            .getString("category"));
                    item.getListItem().add(itemHistory);
                }
                arr.add(item);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arr;
    }

    // Get list Dash Board Orders
    public static ArrayList<DashboardRowItemObject> parseListDashBoard(
            String json) {
        ArrayList<DashboardRowItemObject> arr = new ArrayList<DashboardRowItemObject>();
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONArray jsonArray = jsonobj.getJSONArray(KEY_DATA);
            JSONObject itemJson;
            DashboardRowItemObject item;
            for (int i = 0; i < jsonArray.length(); i++) {
                item = new DashboardRowItemObject();
                itemJson = jsonArray.getJSONObject(i);
                item.setTitle_left(itemJson.getString("day"));
                item.setTitle_new(itemJson.getString("numberNewOrder"));
                item.setTitle_void(itemJson.getString("numberVoidOrder"));
                item.setTitle_delivered(itemJson
                        .getString("numberDeliveredOrder"));
                item.setTitle_pending(itemJson.getString("numberPendingOrder"));
                arr.add(item);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arr;
    }

    // Get max page
    public static int getMaxPage(String json) {
        try {
            JSONObject jsonobj = new JSONObject(json);
            return jsonobj.getInt("numpage");
        } catch (Exception e) {
            return 0;
        }
    }

    // Get list Dash Board Orders
    public static ArrayList<PromotionObject> parseListPromotion(String json) {
        ArrayList<PromotionObject> arr = new ArrayList<PromotionObject>();
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONArray jsonArray = jsonobj.getJSONArray(KEY_DATA);
            JSONObject itemJson;
            PromotionObject item;
            for (int i = 0; i < jsonArray.length(); i++) {
                item = new PromotionObject();
                itemJson = jsonArray.getJSONObject(i);
                item.setId(itemJson.getString("id"));
                item.setImage(itemJson.getString("image"));
                item.setCategoryId(itemJson.getString("categoryName"));
                item.setTitle(itemJson.getString("title"));
                item.setDescription(itemJson.getString("description"));
                item.setCategoryName(itemJson.getString("categoryName"));
                item.setStartDate(DateUtil.getTimeStampOfDate(itemJson.getString("startDate")));
                item.setEndDate(DateUtil.getTimeStampOfDate(itemJson.getString("endDate")));
                item.setStatus(itemJson.getString("status"));
                item.setDateCreated(DateUtil.getTimeStampOfDate(itemJson.getString("dateCreated")));
                arr.add(item);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arr;
    }

    // Get list table
    public static ArrayList<TableObject> parseListTable(String json) {
        ArrayList<TableObject> arr = new ArrayList<TableObject>();
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONArray jsonArray = jsonobj.getJSONArray(KEY_DATA);
            JSONObject itemJson;
            TableObject item;
            for (int i = 0; i < jsonArray.length(); i++) {
                item = new TableObject();
                itemJson = jsonArray.getJSONObject(i);
                item.setId(itemJson.getString("id"));
                item.setName(itemJson.getString("title"));
                item.setNum_of_seat(itemJson.getString("capacity"));
                item.setCurrent_seat(itemJson.getString("occupied"));
                arr.add(item);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arr;
    }

    // Get list table
    public static ArrayList<String> parseListCustomersForWaiter(String json) {
        ArrayList<String> arr = new ArrayList<String>();
        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONArray jsonArray = jsonobj.getJSONArray("name");
            for (int i = 0; i < jsonArray.length(); i++) {
                arr.add(jsonArray.get(i).toString());
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arr;
    }
}
