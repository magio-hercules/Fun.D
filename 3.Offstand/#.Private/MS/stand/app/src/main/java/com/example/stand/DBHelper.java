package com.example.stand;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.stand.SplashActivity.dbInfos;
import static com.example.stand.SplashActivity.tableMyScore;
import static com.example.stand.SplashActivity.tableName;

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        this.database = database;


        String sqlCreate = "create table IF NOT EXISTS " + tableName + "(_id integer PRIMARY KEY autoincrement, rank integer, userId text, win text, lose text)";
        try {
            database.execSQL(sqlCreate);
        } catch (Exception e) {

        }

//        String mysqlCreate = "create table IF NOT EXISTS " + tableMyScore + "(_id integer PRIMARY KEY autoincrement, myImage integer, myName text, myWin text, myLose text)";
//        try {
//            database.execSQL(mysqlCreate);
//        } catch (Exception e) {
//
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String sql, Object[] params) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.execSQL(sql, params);
        sqLiteDatabase.close();
    }

    public void update(String sql) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    public void delete(String sql) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }


}
