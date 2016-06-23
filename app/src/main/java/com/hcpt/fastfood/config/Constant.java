package com.hcpt.fastfood.config;

//This file is used to save all keys such as admod key, facebook key, twitter key, etc...
public class Constant {
	// admob banner key
	public static final String BANNER_ADMOD_KEY = "";
	public static final String INTERSTITIAL_ADMOD_KEY = "";
	public static final String GOOGLE_ANALYTIC_KEY = "";
	//
	public static final double MY_COMPANY_LONG = 105.802513;
	public static final double MY_COMPANY_LAT = 21.042875;

	/*
	 * // Status // CREATED public static final String STATUS_0 = "CRIADO"; //
	 * REJECT public static final String STATUS_1 = "REJEITADO"; // IN PROCESS
	 * public static final String STATUS_2 = "EM PROCESSO"; // READY public
	 * static final String STATUS_3 = "PRONTO"; // DELIVERED public static final
	 * String STATUS_4 = "ENTREGUE"; // FAIL public static final String STATUS_5
	 * = "FALHOU"; // ON THE WAY public static final String STATUS_6 =
	 * "A CAMINHO"; // ALL STATUS public static final String STATUS_7 =
	 * "TODOS STATUS";
	 */

	public static final String NONE = "None";
	// Status
	// CREATED
	public static final String STATUS_0 = "NEW";
	// REJECT
	public static final String STATUS_1 = "REJECTED";
	// IN PROCESS
	public static final String STATUS_2 = "IN PROCESS";
	// READY
	public static final String STATUS_3 = "READY";
	// DELIVERED
	public static final String STATUS_4 = "DELIVERED";
	// FAIL
	public static final String STATUS_5 = "FAILED";
	// ON THE WAY
	public static final String STATUS_6 = "ON THE WAY";
	// ALL STATUS
	public static final String STATUS_7 = "ALL STATUS";

	public static final String STATUS_CREATED = "0";
	public static final String STATUS_REJECT = "1";
	public static final String STATUS_IN_PROCESS = "2";
	public static final String STATUS_READY = "3";
	public static final String STATUS_DELIVERED = "4";
	public static final String STATUS_FAIL = "5";
	public static final String STATUS_ON_THE_WAY = "6";
	public static final String STATUS_ALL = "7";

	// ROLE
	public static final String ROLE_NORMAL_USER = "0";
	public static final String ROLE_USER_DELIVERY = "1";
	public static final String ROLE_CHEF_USER = "2";
	public static final String ROLE_ADMIN_USER = "3";
	public static final String ROLE_WAITER_USER = "4";

	// SCREEN TYPE
	public static final int SCREEN_TYPE_MY_ORDER = 0;
	public static final int SCREEN_TYPE_NEW_ORDER = 1;
	public static final int SCREEN_TYPE_ADMIN_ORDER = 2;

	// ACTION
	public static final String NEW_ACTION_DEFAULT = "-1";
	public static final String NEW_ACTION_REJECT = "0";
	public static final String NEW_ACTION_ASSIGN_TO_ME = "1";

}
