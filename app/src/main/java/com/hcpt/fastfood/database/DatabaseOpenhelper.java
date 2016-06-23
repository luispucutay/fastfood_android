package com.hcpt.fastfood.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hcpt.fastfood.utility.Logger;




public class DatabaseOpenhelper extends SQLiteOpenHelper {

	private static final String TAG = "DatabaseOpenhelper";
	private Context context = null;
	private DatabaseConfig databaseConfig = null;
	
	public DatabaseOpenhelper(Context context, DatabaseConfig config) {

		super(context, config.getDatabaseName(), null, config.getDatabaseVersion());
		this.context = context;
		this.databaseConfig = config;

		if (!isDatabaseExist()) {

			// Create blank file
			getReadableDatabase();
			close();
			try {
				copyDatabase();
			} catch (IOException e) {
				Log.d(TAG, "Error to init database");
			}
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * Copy database to application directory on SD card
	 * 
	 * @throws IOException
	 * 
	 * @throws IOException
	 */
	private void copyDatabase() throws IOException {
		Logger.e(TAG, "Copy database into application directory");
		Logger.e(TAG, databaseConfig.getDatabaseFullPath());
		InputStream is = context.getAssets().open(databaseConfig.getDatabaseName());
		OutputStream os = new FileOutputStream(databaseConfig.getDatabaseFullPath());
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		os.flush();
		os.close();
		is.close();
	}

	/**
	 * Check database is exist
	 * 
	 * @return
	 */
	private boolean isDatabaseExist() {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(databaseConfig.getDatabaseFullPath(), null,
					SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			Logger.e(TAG, "Database is not exist! " + databaseConfig.getDatabaseFullPath()
					+ " ======================");
			e.printStackTrace();
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return (checkDB != null) ? true : false;
	}

}
