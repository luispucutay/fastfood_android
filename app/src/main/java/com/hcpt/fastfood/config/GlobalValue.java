package com.hcpt.fastfood.config;

import java.util.ArrayList;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.hcpt.fastfood.object.Category;
import com.hcpt.fastfood.object.ItemCart;
import com.hcpt.fastfood.object.OrderHistory;
import com.hcpt.fastfood.object.PromotionObject;
import com.hcpt.fastfood.object.TableObject;
import com.hcpt.fastfood.object.User;

public class GlobalValue {

	public static GlobalValue mGlobalValue;

	public static GlobalValue getInstance() {
		if (mGlobalValue == null) {
			synchronized (GlobalValue.class) {
				if (mGlobalValue == null) {
					mGlobalValue = new GlobalValue();
				}
			}
		}
		return mGlobalValue;
	}

	public ArrayList<Category> arrCategories;	
	public ArrayList<ItemCart> arrCartItems;
	public ArrayList<TableObject> arrTableObject = new ArrayList<TableObject>();

	public ArrayList<TableObject> getArrTableObject() {
		return arrTableObject;
	}

	public void setArrTableObject(ArrayList<TableObject> arrTableObject) {
		this.arrTableObject = arrTableObject;
	}

	public boolean isLogin = false;
	public User currentUser = new User();
	private TableObject currentTable = new TableObject();

	public TableObject getCurrentTable() {
		return currentTable;
	}

	public void setCurrentTable(TableObject currentTable) {
		this.currentTable = currentTable;
	}

	public int detailScreenType;

	public int getDetailScreenType() {
		return detailScreenType;
	}

	public void setDetailScreenType(int detailScreenType) {
		this.detailScreenType = detailScreenType;
	}

	public OrderHistory currentOrder = new OrderHistory();
	public PromotionObject currentPromotion = new PromotionObject();

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isLogin() {
		return isLogin;
	}
	
	public boolean isRole(String role){
		if (currentUser == null || currentUser.getRole() == null)
			return false;
		return currentUser.getRole().equals(role);		
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public static GlobalValue getmGlobalValue() {
		return mGlobalValue;
	}

	public static void setmGlobalValue(GlobalValue mGlobalValue) {
		GlobalValue.mGlobalValue = mGlobalValue;
	}

	public ArrayList<Category> getArrCategories() {
		return arrCategories;
	}

	public void setArrCategories(ArrayList<Category> arrCategories) {
		this.arrCategories = arrCategories;
	}	

	public ArrayList<ItemCart> getArrCartItems() {
		return arrCartItems;
	}

	public void setArrCartItems(ArrayList<ItemCart> arrCartItems) {
		this.arrCartItems = arrCartItems;
	}
	
	public String getIME(Context context){
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String ime = telephonyManager.getDeviceId();//	"359149055000014";
		return ime;
	}

	public static final String FRUITY_DROID_PREFERENCES = "FRUITY_DROID_PREFERENCES";
	public static MySharedPreferences pref;
	public static final String CHECK_FIRST_SETTING_APP = "first_setting";

	public static final String LANGUAGE = "Language";
	public static final String LANGUAGE_ENGLISH = "English";
	public static final String LANGUAGE_VIETNAMESE = "Vietnamese";

	public static final String LOCALE_ENGLISH = "en";
	public static final String LOCALE_VIETNAMESE = "vi";
}
