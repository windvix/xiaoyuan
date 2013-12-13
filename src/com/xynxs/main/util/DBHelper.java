package com.xynxs.main.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库助手
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "xy.data";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建SharePre表
		db.execSQL("CREATE TABLE IF NOT EXISTS SHARE_PRE(KEY VARCHAR PRIMARY KEY, VAL VARCHAR)");
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
	}

	/**
	 * 更新Key val
	 */
	public void updateKeyVal(String key, String val) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("REPLACE INTO SHARE_PRE(KEY, VAL) VALUES(?, ?)", new Object[] { key, val });
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
	
	/**
	 * 删除所有内容
	 */
	public void deleteAllKeyVal(){
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM SHARE_PRE");
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}
	

	/**
	 * 获取 val
	 */
	public String getVal(String key) {
		String val = "";
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery("SELECT VAL FROM SHARE_PRE WHERE KEY=?", new String[] { key });
		try{
		c.moveToNext();
		val = c.getString(c.getColumnIndex("VAL"));
		}catch(Exception e){
			
		}
		c.close();
		return val;
	}
}
