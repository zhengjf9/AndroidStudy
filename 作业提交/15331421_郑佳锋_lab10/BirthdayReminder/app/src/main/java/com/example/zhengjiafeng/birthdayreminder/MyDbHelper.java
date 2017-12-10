package com.example.zhengjiafeng.birthdayreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhengjiafeng on 2017/12/7.
 */

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "Contacts";

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME +
                " (_id integer primary key , " + "name text , " +
                "birth text , " + "gift text);";
        db.execSQL(CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // To Do
        }
    public void insert(String name, String birth, String gift)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = { name };
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
    public boolean isExist(String name){
        SQLiteDatabase db = getWritableDatabase();
        boolean result = false;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null,null,null,null);
        for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
            if(name.equals(cursor.getString(1)))
                result = true;
        }
        cursor.close();
        db.close();
        return result;
    }
    public void update(String name, String birth, String gift) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = { name };
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }
}
