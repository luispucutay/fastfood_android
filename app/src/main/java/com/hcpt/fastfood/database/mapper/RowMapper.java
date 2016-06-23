package com.hcpt.fastfood.database.mapper;

import android.database.Cursor;

public interface RowMapper<E> {
	E mapRow(Cursor row, int rowNum);
}
