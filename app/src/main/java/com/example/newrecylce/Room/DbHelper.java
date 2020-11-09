package com.example.newrecylce.Room;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";
    public static final String DATABASE_NAME = "MY_DATABASE";
    public static final String TABLE_NAME1 = "tables";
    public static String TABLE_NAME2;
    public static final String id0 = "id";
    public static final String subjectName1 = "subjectName";
    public static final String present2 = "present";
    public static final String absent3 = "absent";
    public static final String total4 = "total";
    public static final String percentage5 = "percentage";
    public static final String date6 = "date";
    public static final String status7 = "status";
    Context context;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table if not exists " + TABLE_NAME1 + "(" + id0 + " integer primary key autoincrement," + subjectName1 + " TEXT, " + total4 + " int,"
                + present2 + " int, " + absent3 + " int, " + percentage5 + " int);");
        Toast.makeText(context, "Created: " + TABLE_NAME1, Toast.LENGTH_SHORT).show();
    }

    public void createTable(String tableName) {
        TABLE_NAME2 = tableName;
        final SQLiteDatabase db = getWritableDatabase();
        String CREATE_TABLE_NEW_USER = "CREATE TABLE if not exists " + TABLE_NAME2 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + date6 + " text, " + status7 + " int)";
        db.execSQL(CREATE_TABLE_NEW_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addDateData(String date, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(date6, date);
        values.put(status7, status);
        long result = db.insert(TABLE_NAME2, null, values);
        if (result == -1)
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Succeed", Toast.LENGTH_SHORT).show();
    }

    public void insertData(String sub, long total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(subjectName1, sub);
        values.put(total4, total);
        long result = db.insert(TABLE_NAME1, null, values);
        if (result == -1)
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Succeed", Toast.LENGTH_SHORT).show();
    }

    public void deleteData(String newTable, String a) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table if exists " + newTable);
        db.execSQL("delete  from " + TABLE_NAME1 + " where " + subjectName1 + " like \"" + a + "\"");
    }

    public void deleteItem(String tableName, String a) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + tableName + " where " + date6 + " like \"" + a + "\"");
    }

    public Cursor showData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_NAME1;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public int totalCount(String tableName) {
        long count;
        int counts;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            count = DatabaseUtils.queryNumEntries(db, tableName);
            counts = (int) count;
        } catch (Exception e) {
            counts = 0;
        }
        return counts;
    }

    public int getPresentCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + tableName + " where " + status7 + " = 1";
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public int getAbsentCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + tableName + " where " + status7 + " = 0";
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public Cursor display() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + TABLE_NAME2;
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "display: all dates");
        return cursor;
    }

    public void updateData(int total, String subName, int present, int absent, int percentage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(subjectName1, subName);
        values.put(present2, present);
        values.put(absent3, absent);
        values.put(total4, total);
        values.put(percentage5, percentage);
        db.update(TABLE_NAME1, values, subjectName1 + "=?", new String[]{subName});
    }
}