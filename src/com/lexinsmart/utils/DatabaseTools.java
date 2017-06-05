package com.lexinsmart.utils;

import static com.lexinsmart.MyApplication.gDb;
import static com.lexinsmart.utils.Constants.DB_COLUMN_LEN;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseTools {

	/**
	 * 为本地数据库的表格插入一条数据
	 * <p>
	 * 使用override指定是否覆盖原有数据
	 * 
	 * @param tableName
	 * @param tableColumns
	 * @param values
	 */
	public static void updateTable(Context context, String tableName, String[] tableColumns, String[] values,
			boolean override) {
		// 打开数据库，如果不存在则创建并添加表格DB_TABLE_USER
		gDb = SQLiteDatabase.openDatabase(context.getFilesDir().getParent() + "/mydb.db", null,
				SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
		String sql = "CREATE TABLE if not exists " + tableName + "(";
		for (int i = 0; i < tableColumns.length; i++) {
			sql += tableColumns[i] + " CHAR(" + DB_COLUMN_LEN + "), ";
		}
		sql = sql.substring(0, sql.length() - 2) + ")";
		gDb.execSQL(sql);

		// 清除表格DB_TABLE_USER中原有数据
		if (override) {
			gDb.delete(tableName, null, null);
		}

		// 向表格DB_TABLE_USER插入数据
		ContentValues cv = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			cv.put(tableColumns[i], values[i]);
		}
		if (cv.size() > 0) {
			gDb.insert(tableName, null, cv);
		}

		// 关闭数据库
		gDb.close();
	}

	/**
	 * 为本地数据库表格的某条数据更新值
	 * <p>
	 * XXX: 暂时是将所有行的某列全部赋值，等待修改
	 * 
	 * @param tableName
	 * @param columnNames
	 * @param values
	 */
	public static void updateTableColumns(Context context, String tableName, String[] columnNames, String[] values) {
		// 打开数据库
		gDb = SQLiteDatabase.openDatabase(context.getFilesDir().getParent() + "/mydb.db", null,
				SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);

		// 给表格DB_TABLE_USER中ID列赋予新值
		ContentValues cv = new ContentValues();
		for (int i = 0; i < values.length; i++) {
			cv.put(columnNames[i], values[i]);
		}
		gDb.update(tableName, cv, null, null);

		// 关闭数据库
		gDb.close();
	}

	/**
	 * 
	 * @param context
	 * @param tableName
	 * @param columnsToQuery
	 * @return
	 */
	public static String[] queryUnAndPw(Context context, String tableName, String[] columnsToQuery) {

		try {
			gDb = SQLiteDatabase.openDatabase(context.getFilesDir().getParent() + "/mydb.db", null,
					SQLiteDatabase.OPEN_READWRITE);
			Cursor cursor = gDb.query(tableName, columnsToQuery, null, null, null, null, null);
			String username = "", password = "";
			while (cursor.moveToNext()) {
				username = cursor.getString(cursor.getColumnIndex(columnsToQuery[0]));
				password = cursor.getString(cursor.getColumnIndex(columnsToQuery[1]));
			}
			gDb.close();
			return new String[] { username, password };
		} catch (SQLiteException e) {
			return null;
		}

	}

	/**
	 * XXX: 有严重问题，删除表格失败！
	 * 
	 * @param context
	 * @param tableName
	 */
	public static void deleteLocalDBTable(Context context, String tableName) {
		// 打开数据库，如果不存在则创建并添加表格DB_TABLE_USER
		gDb = SQLiteDatabase.openDatabase(context.getFilesDir().getParent() + "/mydb.db", null,
				SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
		gDb.delete(tableName, null, null);

		// 关闭数据库
		gDb.close();
	}

}
