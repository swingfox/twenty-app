package com.example.twenty.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqlHandler {
	 
	 public static final String DATABASE_NAME = "FlowDB";
	 public static final int DATABASE_VERSION = 1;
	 private SQLiteDatabase sqlDatabase;
	 private SqlDbHelper dbHelper;
	 
	 public SqlHandler(Context context) {
		  dbHelper = new SqlDbHelper(context, DATABASE_NAME, null,DATABASE_VERSION);
		  sqlDatabase = dbHelper.getWritableDatabase();
	 }
	 
	 public void executeQuery(String query) {
		  try {
			   if (sqlDatabase.isOpen()) {
			    sqlDatabase.close();
			   }
			   sqlDatabase = dbHelper.getWritableDatabase();
			   sqlDatabase.execSQL(query);
		  } catch (Exception e) {
		   System.out.println("DATABASE ERROR " + e);
		  }
	 }
	 
	 public Cursor selectQuery(String query) {
	  Cursor c1 = null;
	  try {
		   if (sqlDatabase.isOpen()) {
		    sqlDatabase.close();
		   }
		   sqlDatabase = dbHelper.getWritableDatabase();
		   c1 = sqlDatabase.rawQuery(query, null);
	  } catch (Exception e) {
		   System.out.println("DATABASE ERROR " + e);
	  }
	  return c1;
	 
	 }
	 
	 public int getFeelingCount(String feel) {	 
		String countQuery = "SELECT * FROM flow WHERE feelings LIKE '%"+feel+"%'";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		    
		Cursor cursor = db.query("flow", null,"feelings LIKE ?", new String[] { "%"+feel+"%"}, null, null, null);
		     
		int cnt = cursor.getCount();
	    cursor.close();
	    return cnt;
	}
	 
	public long countAllFeelings() {	 
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		long cnt  = DatabaseUtils.queryNumEntries(db, "flow");
		return cnt;
	}
	 
	 
	 
	 public void deleteContact(int row) {
		 	SQLiteDatabase db = dbHelper.getWritableDatabase();
//			db.delete("flow", "id = ?",new String[] { String.valueOf(FLOW.getId()) });
			db.close();
		 	
//			db.delete(DATABASE_NAME, KEY_ID + "=" + row , null);

	      /*if you just have key_name to select a row,you can ignore passing rowid(here-row) and use:

	      db.delete(DATABASE_TABLE, KEY_NAME + "=" + key_name, null);
	      */  
		}
	public void deleteRow(String id){
		 SQLiteDatabase db = dbHelper.getWritableDatabase();
//		 db.delete(DATABASE_TABLE, KEY_ID+"="+id, null);
		 db.delete("flow", "id ="+id, null);
	}
	
}
