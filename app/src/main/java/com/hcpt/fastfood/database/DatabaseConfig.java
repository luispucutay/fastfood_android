package com.hcpt.fastfood.database;

import android.annotation.SuppressLint;

import com.hcpt.fastfood.PacketUtility;

public final class DatabaseConfig {
	private final int DB_VERSION = 3;
	private final String DB_NAME = "dbTemplateAlbum.sqlite";

	private static DatabaseConfig instance = null;

	public static DatabaseConfig getInstance() {
		if (instance == null) {
			instance = new DatabaseConfig();
		}
		return instance;
	}

	/**
	 * Get database version
	 * 
	 * @return
	 */
	public int getDatabaseVersion() {
		return DB_VERSION;
	}

	/**
	 * Get database name
	 * 
	 * @return
	 */
	public String getDatabaseName() {
		return DB_NAME;
	}

	/**
	 * Get database path
	 * 
	 * @return
	 */
	@SuppressLint("SdCardPath")
	public String getDatabasePath() {
		return "/data/data/" + PacketUtility.getPacketName() + "/databases/";
	}

	/**
	 * Get database path
	 * 
	 * @return
	 */
	public String getDatabaseFullPath() {
		return getDatabasePath() + DB_NAME;
	}
}
