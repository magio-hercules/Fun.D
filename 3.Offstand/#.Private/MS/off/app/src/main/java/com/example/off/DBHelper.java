package com.example.off;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase database; // private 로 할 경우 어떻게 접근 하는지?
    private String tableName = "roundScore";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.database = db;

        String sqlCreate = "create table IF NOT EXISTS " + tableName + "(_id integer PRIMARY KEY autoincrement, rank integer, userId text, win text, lose text)";
        try {
            database.execSQL(sqlCreate);
        } catch (Exception e) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
