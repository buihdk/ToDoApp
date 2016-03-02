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
    private static final int DATABASE_VERSION = 512;
    // Table Names
    private static final String TABLE_ITEMS = "items";
    // Items Table Column(s)
    private static final String KEY_ITEM = "item";
    private static final String KEY_DATE = "date";


    public ToDoAppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Called when the database is created for the FIRST time.
       If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + "(" + KEY_ITEM + " TEXT, " + KEY_DATE + " TEXT)");
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

    public boolean insertItems(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(KEY_ITEM, name);
//        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_ITEM, item.item_name);
        contentValues.put(KEY_DATE, item.due_date);
        db.insert(TABLE_ITEMS, null, contentValues);
        db.close();
        return true;
    }

    public boolean updateItems(Item old_item, Item new_item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, new_item.item_name);
        contentValues.put(KEY_DATE, new_item.due_date);
        db.update(TABLE_ITEMS, contentValues, KEY_ITEM + " = ? AND " + KEY_DATE + " = ?", new String[]{old_item.item_name, old_item.due_date}); // for int var: Integer.toString(var)
        db.close();
        return true;
    }

    public boolean deleteItems(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ITEM + " = ? AND " + KEY_DATE + " = ?", new String[]{item.item_name, item.due_date});
        db.close();
        return true;
    }

    public ArrayList<Item> readItems()
    {
        ArrayList<Item> readItems = new ArrayList<Item>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur =  db.rawQuery("SELECT " + KEY_ITEM + ", " + KEY_DATE + " FROM " + TABLE_ITEMS, null);
        try {
            cur.moveToFirst();
            while(!cur.isAfterLast()) {
                readItems.add(new Item(cur.getString(cur.getColumnIndex(KEY_ITEM)),cur.getString((cur.getColumnIndex(KEY_DATE)))));
                cur.moveToNext();
            }
        } finally {
            cur.close();
        }
        db.close();
        return readItems;
    }
}