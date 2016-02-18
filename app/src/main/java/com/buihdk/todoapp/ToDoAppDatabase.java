package com.buihdk.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class ToDoAppDatabase extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "ToDoList.db";
    private static final int DATABASE_VERSION = 1;
    // Table Names
    private static final String TABLE_ITEMS = "items";
    // Items Table Column(s)
    private static final String KEY_ITEM = "item";

    public ToDoAppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Called when the database is created for the FIRST time.
       If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + "(" + KEY_ITEM + " TEXT" + ")");
    }

    /* Called when the database needs to be upgraded.
       This method will only be called if a database already exists on disk with the same DATABASE_NAME,
       but the DATABASE_VERSION is different than the version of the database that exists on disk. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    public boolean insertItems(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, item);
        db.insert(TABLE_ITEMS, null, contentValues);
        return true;
    }

    public boolean updateItems(String old_item, String new_item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, new_item);
        db.update(TABLE_ITEMS, contentValues, KEY_ITEM + " = ?", new String[]{old_item}); // for int var: Integer.toString(var)
        return true;
    }

    public boolean deleteItems(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ITEM + " = ?", new String[]{item});
        return true;
    }

    public ArrayList<String> todoItems()
    {
        ArrayList<String> todoItems = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT " + KEY_ITEM + " FROM " + TABLE_ITEMS, null );
        res.moveToFirst();

        while(!res.isAfterLast()) {
            todoItems.add(res.getString(res.getColumnIndex(KEY_ITEM)));
            res.moveToNext();
        }

        return todoItems;
    }
}