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
    private static final int DATABASE_VERSION = 3;
    // Table Names
    private static final String TABLE_ITEMS = "items";
    // Items Table Column(s)
    private static final String KEY_POSITION = "position";
    private static final String KEY_ITEM = "item";

    public ToDoAppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Called when the database is created for the FIRST time.
       If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + "(" + KEY_POSITION + " INTEGER, " + KEY_ITEM + " TEXT" + ")");
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

    public Cursor readItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT " + KEY_ITEM + " FROM " + TABLE_ITEMS, null);
        return res;
    }

    public boolean insertItems(int position, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_POSITION, position);
        contentValues.put(KEY_ITEM, item);
        db.insert(TABLE_ITEMS, null, contentValues);
        return true;
    }

    public boolean updateItems(int position, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_POSITION, position);
        contentValues.put(KEY_ITEM, item);
        db.update(TABLE_ITEMS, contentValues, KEY_POSITION + " = ?", new String[]{Integer.toString(position)});
        return true;
    }

    public boolean deleteItems(int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS,KEY_POSITION + " = ?", new String[]{Integer.toString(position)});
        return true;
    }

    public ArrayList<String> todoItems()
    {
        ArrayList<String> todoItems = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT " + KEY_ITEM + " FROM " + TABLE_ITEMS, null );
        res.moveToFirst();

        while(res.moveToNext()){
            todoItems.add(res.getString(res.getColumnIndex(KEY_ITEM)));
            ;
        }
        return todoItems;
    }
}
