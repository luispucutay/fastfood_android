package com.hcpt.fastfood.utility;

import android.app.Activity;
import android.view.WindowManager;

public class SmallUtility {

	public static void hiddenKeyboard(Activity act) {
		act.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}
