package com.bobamason.database1;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseAdapter {

	private DataBaseAdapter.SQLHelper helper;

	private Context ctx;

	private int sortByPosition = 0;
	private int orderPosition = 0;

	public DataBaseAdapter(Context context) {
		ctx = context;
		helper = new SQLHelper(ctx);
	}

	public void insert(Entry entry) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put(SQLHelper.COLUMN_NAME, entry.getName());
		vals.put(SQLHelper.COLUMN_CATEGORY, entry.getCategory());
		vals.put(SQLHelper.COLUMN_DESCRIPTION, entry.getDescription());
		vals.put(SQLHelper.COLUMN_COST, entry.getDBcost());
		vals.put(SQLHelper.COLUMN_VALUE, entry.getDBvalue());
		entry.setId(db.insert(SQLHelper.TABLE_NAME, null, vals));
		db.close();
	}

	public void deleteEntry(Entry entry) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(SQLHelper.TABLE_NAME,
				SQLHelper.UID + " = " + String.valueOf(entry.getId()), null);
		db.close();
	}

	public ArrayList<Entry> getSearchEntries(String str) {
		ArrayList<Entry> entries = new ArrayList<Entry>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null;
		try {
			String WHERE = SQLHelper.COLUMN_NAME + " LIKE '" + str + "%' OR "
					+ SQLHelper.COLUMN_CATEGORY + " LIKE '" + str + "%' OR "
					+ SQLHelper.COLUMN_DESCRIPTION + " LIKE '" + str + "%'";
			cursor = db.query(SQLHelper.TABLE_NAME, null, WHERE, null, null,
					null, getOrderByString(sortByPosition, orderPosition));
			Log.e("Cursor Get search", "cursor returned");
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e("Cursor Get search", e.toString());
		}
		if (cursor.moveToFirst()) {
			do {
				Entry entry = new Entry();
				entry.setId(cursor.getLong(cursor.getColumnIndex(SQLHelper.UID)));
				entry.setName(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_NAME)));
				entry.setCategory(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_CATEGORY)));
				entry.setDescription(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_DESCRIPTION)));
				entry.setCostfromDB(cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_COST)));
				entry.setValuefromDB(cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_VALUE)));
				entries.add(entry);
			} while (cursor.moveToNext());
		}

		return entries;
	}

	public void updateEntry(Entry entry) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put(SQLHelper.COLUMN_NAME, entry.getName());
		vals.put(SQLHelper.COLUMN_CATEGORY, entry.getCategory());
		vals.put(SQLHelper.COLUMN_DESCRIPTION, entry.getDescription());
		vals.put(SQLHelper.COLUMN_COST, entry.getDBcost());
		vals.put(SQLHelper.COLUMN_VALUE, entry.getDBvalue());
		db.update(SQLHelper.TABLE_NAME, vals,
				SQLHelper.UID + " = " + String.valueOf(entry.getId()), null);
		db.close();
	}

	public ArrayList<Entry> getAllEntries() {
		ArrayList<Entry> entries = new ArrayList<Entry>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(SQLHelper.TABLE_NAME, null, null, null, null,
					null, getOrderByString(sortByPosition, orderPosition));
			Log.e("Cursor Get All", "cursor returned");
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e("Cursor Get All", e.toString());
		}
		if (cursor.moveToFirst()) {
			do {
				Entry entry = new Entry();
				entry.setId(cursor.getLong(cursor.getColumnIndex(SQLHelper.UID)));
				entry.setName(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_NAME)));
				entry.setCategory(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_CATEGORY)));
				entry.setDescription(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_DESCRIPTION)));
				entry.setCostfromDB(cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_COST)));
				entry.setValuefromDB(cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_VALUE)));
				entries.add(entry);
			} while (cursor.moveToNext());
		}

		return entries;
	}

	public ArrayList<Entry> setSorting(int sortPos, int orderPos) {
		sortByPosition = sortPos;
		orderPosition = orderPos;
		ArrayList<Entry> entries = new ArrayList<Entry>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(SQLHelper.TABLE_NAME, null, null, null, null,
					null, getOrderByString(sortByPosition, orderPosition));
			Log.e("Cursor set sort", "cursor returned");
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e("Cursor set sort", e.toString());
		}
		if (cursor.moveToFirst()) {
			do {
				Entry entry = new Entry();
				entry.setId(cursor.getLong(cursor.getColumnIndex(SQLHelper.UID)));
				entry.setName(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_NAME)));
				entry.setCategory(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_CATEGORY)));
				entry.setDescription(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_DESCRIPTION)));
				entry.setCostfromDB(cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_COST)));
				entry.setValuefromDB(cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_VALUE)));
				entries.add(entry);
			} while (cursor.moveToNext());
		}

		return entries;
	}

	public ArrayList<Entry> setSorting(int sortPos, int orderPos, String str) {
		sortByPosition = sortPos;
		orderPosition = orderPos;
		ArrayList<Entry> entries = new ArrayList<Entry>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = null;
		try {
			String WHERE = SQLHelper.COLUMN_NAME + " LIKE '" + str + "%' OR "
					+ SQLHelper.COLUMN_CATEGORY + " LIKE '" + str + "%' OR "
					+ SQLHelper.COLUMN_DESCRIPTION + " LIKE '" + str + "%'";
			cursor = db.query(SQLHelper.TABLE_NAME, null, WHERE, null, null,
					null, getOrderByString(sortByPosition, orderPosition));
			Log.e("Cursor set sort search", "cursor returned");
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e("Cursor set sort search", e.toString());
		}
		if (cursor.moveToFirst()) {
			do {
				Entry entry = new Entry();
				entry.setId(cursor.getLong(cursor.getColumnIndex(SQLHelper.UID)));
				entry.setName(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_NAME)));
				entry.setCategory(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_CATEGORY)));
				entry.setDescription(cursor.getString(cursor
						.getColumnIndex(SQLHelper.COLUMN_DESCRIPTION)));
				entry.setCostfromDB(cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_COST)));
				entry.setValuefromDB(cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_VALUE)));
				entries.add(entry);
			} while (cursor.moveToNext());
		}

		return entries;
	}

	private String getOrderByString(int sortPos, int orderPos) {
		String s = "";
		if (sortPos == 0)
			s += SQLHelper.COLUMN_NAME;
		else if (sortPos == 1)
			s += SQLHelper.COLUMN_CATEGORY;
		else if (sortPos == 2)
			s += SQLHelper.COLUMN_DESCRIPTION;
		else if (sortPos == 3)
			s += SQLHelper.COLUMN_COST;
		else if (sortPos == 4)
			s += SQLHelper.COLUMN_VALUE;
		else
			return null;
		if (orderPos == 0)
			s += " ASC";
		else if (orderPos == 1)
			s += " DESC";
		else
			return null;

		return s;
	}

	public int getCount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = { SQLHelper.UID };
		Cursor cursor = null;
		try {
			cursor = db.query(SQLHelper.TABLE_NAME, columns, null, null, null,
					null, null);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e("Cursor", e.toString());
		}
		return cursor.getCount();
	}

	public float getTotalCost() {
		float total = 0f;
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = { SQLHelper.COLUMN_COST };
		Cursor cursor = null;
		try {
			cursor = db.query(SQLHelper.TABLE_NAME, columns, null, null, null,
					null, null);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e("Cursor", e.toString());
		}
		if (cursor.moveToFirst()) {
			do {
				total += cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_COST)) / 100;
			} while (cursor.moveToNext());
		}
		return total;
	}

	public float getTotalValue() {
		float total = 0f;
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] columns = { SQLHelper.COLUMN_VALUE };
		Cursor cursor = null;
		try {
			cursor = db.query(SQLHelper.TABLE_NAME, columns, null, null, null,
					null, null);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e("Cursor", e.toString());
		}
		if (cursor.moveToFirst()) {
			do {
				total += cursor.getInt(cursor
						.getColumnIndex(SQLHelper.COLUMN_VALUE)) / 100;
			} while (cursor.moveToNext());
		}
		return total;
	}

	static class SQLHelper extends SQLiteOpenHelper {

		private static final int DB_VERSION = 2;

		private static final String DB_NAME = "database1.db";

		private static final String TABLE_NAME = "TABLE1";

		private static final String UID = "_id";

		private static final String COLUMN_CATEGORY = "category";

		private static final String COLUMN_NAME = "name";

		private static final String COLUMN_COST = "cost";

		private static final String COLUMN_VALUE = "value";

		private static final String COLUMN_DESCRIPTION = "description";

		// private Context context;

		private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ UID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_NAME
				+ " VARCHAR(255), "
				+ COLUMN_CATEGORY
				+ " VARCHAR(255) ,"
				+ COLUMN_DESCRIPTION
				+ " VARCHAR(255), "
				+ COLUMN_COST
				+ " INTEGER, " + COLUMN_VALUE + " INTEGER);";

		public SQLHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			// this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				Log.e("Create Table", "created");
				db.execSQL(CREATE_TABLE);
				// Message.makeToast(context, this.toString() +
				// ".onCreate TABLE CREATED");

			} catch (SQLException e) {
				Log.e("Create Table", e.toString());
				e.printStackTrace();
				// Message.makeToast(context, e.toString());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
				// Message.makeToast(context, this.toString() +
				// ".onUpgrade TABLE UPGRADED");
				this.onCreate(db);
			} catch (SQLException e) {
				e.printStackTrace();
				// Message.makeToast(context, e.toString());
			}
		}
	}
}
