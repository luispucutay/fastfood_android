package com.hcpt.fastfood.database;


public class DatabaseAction {

	private static String STRING_SQL_INSERT_INTO_RESTAURANT = "INSERT OR IGNORE INTO "
			+ DBKeyConfig.TABLE_RESTAURANT + " VALUES(?,?)";
	private static String STRING_SQL_INSERT_INTO_FAVORITE = "INSERT OR IGNORE INTO Favorite("
			+ DBKeyConfig.KEY_FAVORITE_ID
			+ ","
			+ DBKeyConfig.KEY_CATEGORY_ID
			+ ","
			+ DBKeyConfig.KEY_NAME
			+ ","
			+ DBKeyConfig.KEY_CATEGORY_NANE
			+ ","
			+ DBKeyConfig.KEY_IMAGE
			+ ","
			+ DBKeyConfig.KEY_COLOR
			+ ")VALUES(?,?,?,?,?,?)";


	// *****************************favorite******************************
	// add/insert favorite
//	public static boolean insertFavorite(Context context, Favorite Info) {
//		PrepareStatement statement = new PrepareStatement(context);
//		return statement.insert(STRING_SQL_INSERT_INTO_FAVORITE, Info,
//				new FavoriteBinder());
//	}
//
//	public static boolean deleteFavorite(Context context, int resId) {
//		PrepareStatement statement = new PrepareStatement(context);
//		return statement.query("Delete from " + DBKeyConfig.TABLE_FAVORITE
//				+ " where " + DBKeyConfig.KEY_CATEGORY_ID + "='" + resId + "'",
//				null);
//	}
//
//	public static boolean checkFavorite(Context context, int resId) {
//		ArrayList<Favorite> arr = null;
//		PrepareStatement statement = new PrepareStatement(context);
//		arr = statement.select(DBKeyConfig.TABLE_FAVORITE, "*",
//				DBKeyConfig.KEY_CATEGORY_ID + "='" + resId + "'",
//				new FavoriteMapper());
//
//		return arr.size() > 0;
//	}
//
//	public static ArrayList<Favorite> getListFavoriet(Context context) {
//		PrepareStatement statement = new PrepareStatement(context);
//		return statement.select(DBKeyConfig.TABLE_FAVORITE, "*", "",
//				new FavoriteMapper());
//	}
//
//	public static String getCategoryName(Context context, int resId) {
//		String Name = null;
//		ArrayList<Favorite> arr = null;
//		PrepareStatement statement = new PrepareStatement(context);
//		arr = statement.select(DBKeyConfig.TABLE_FAVORITE, "*",
//				DBKeyConfig.KEY_CATEGORY_ID + "='" + resId + "'",
//				new FavoriteMapper());
//		Name = arr.get(0).getCategory_name();
//		return Name;
//
//	}
}
