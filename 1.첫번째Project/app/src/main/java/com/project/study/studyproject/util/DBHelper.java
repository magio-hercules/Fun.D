package com.project.study.studyproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.study.studyproject.DataModel.DataModel_Map;
import com.project.study.studyproject.Dictionary;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
//    private static final String DATABASE_NAME = "CSY";//name of the database
//    private static final String TABLE_NAME = "NOTES";//name for the table

    private static final String KEY_ID = "id_marker";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_DATE = "date";

    private Context context;
    private SQLiteDatabase db;

    private String db_name = "";
    private String table_name = "";

    private String create_query = "";

//    public DbHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    public DBHelper(Context context, String db_name, String table_name, String query) {
        super(context, db_name, null, DATABASE_VERSION);

        this.context = context;
        this.db_name = db_name;
        this.table_name = table_name;
        this.create_query = query;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db = _db;
//        if (create_query != "") {
//            db.execSQL(create_query);
//        }
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + this.table_name + "(`id_marker` INTEGER	PRIMARY KEY AUTOINCREMENT, " +
//                                                "`id_marker_type`	BIGINT(11)	NOT NULL	DEFAULT AUTOINCREMENT, " +
//                                                "`id_detail_info`	BIGINT(11)	NOT NULL	DEFAULT AUTOINCREMENT, " +
                "`name`	VARCHAR(128)	NOT NULL, " +
                "`lat`	VARCHAR(64)	NOT NULL," +
                "`lng`	VARCHAR(64)	NOT NULL," +
                "`date`	DATETIME	NOT NULL)";
        db.execSQL(CREATE_TABLE);
    }

    //Executes once a database change is occurred
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + this.table_name);
        onCreate(db);
    }

    public void addNotes(String query) {
        if (query != "") {
            db.execSQL(query);
        }
    }

    //    //method to list all details from table
    public ArrayList<DataModel_Map> getAllDatas() {
        ArrayList<DataModel_Map> arrayList = new ArrayList<DataModel_Map>();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + this.table_name;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataModel_Map data = new DataModel_Map();
                data.setIdMarker(cursor.getInt(0));
                data.setName(cursor.getString(1));
                data.setLat(cursor.getString(2));
                data.setLng(cursor.getString(3));
                data.setDate(cursor.getString(4));
                arrayList.add(data);
            } while (cursor.moveToNext());
        }

        return arrayList;
    }

    public long addData(DataModel_Map data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, data.getName());
        values.put(KEY_LAT, data.getLat());
        values.put(KEY_LNG, data.getLng());
        values.put(KEY_DATE, data.getDate());

        if (data.getName().equals("")){   //공란일 경우 pass
            return -1;
        }else
            return db.insert(this.table_name, null, values);
    }

//    public void editRow(Dictionary dictionary, int key) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(CONTENTS, dictionary.getContents());
//        values.put(CREATED_AT, dictionary.getDate());
//
//        db.update(this.table_name, values, KEY_ID + " = '" + key + "'", new String[]{});
//
//    }
//


    public void deleteData(String id) {    //code to delete a row from the table
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(this.table_name, KEY_ID + " = '" + id + "'", new String[]{});
        db.close();
    }
}