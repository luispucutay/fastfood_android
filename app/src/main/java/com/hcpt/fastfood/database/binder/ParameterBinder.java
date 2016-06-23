package com.hcpt.fastfood.database.binder;

import android.database.sqlite.SQLiteStatement;

public interface ParameterBinder {
	void bind(SQLiteStatement st, Object object);
}
