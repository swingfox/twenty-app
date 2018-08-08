package com.example.twenty.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlDbHelper extends SQLiteOpenHelper {
	 public static final String DATABASE_TABLE = "twentyApp";
	 public static final String KEY_ID = "id";
	 public static final String COLUMN1 = "feelings";
	 public static final String COLUMN2 = "reason";
	 public static final String COLUMN3 = "date";

	 
	 private static final String SCRIPT_CREATE_DATABASE =
			 "create table " + DATABASE_TABLE + " ("
			 + KEY_ID + " integer primary key autoincrement, "
			 + COLUMN1 + " VARCHAR, "
			 + COLUMN2 + " VARCHAR, " + COLUMN3 +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
		
	 public SqlDbHelper(Context context, String name, CursorFactory factory,int version) {
	  super(context, name, factory, version);
	 }
	 @Override
	 public void onCreate(SQLiteDatabase db) {
	  db.execSQL(SCRIPT_CREATE_DATABASE);
	 } 
	 @Override
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	  onCreate(db);
	 } 
}
