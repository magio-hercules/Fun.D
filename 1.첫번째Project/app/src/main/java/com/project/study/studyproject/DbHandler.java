package com.project.study.studyproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "CSY";//name of the database
    private static final String TABLE_NAME = "NOTES";//name for the table

    private static final String KEY_ID = "key";
    private static final String CONTENTS = "contents";
    private static final String CREATED_AT = "datetimes";

//    Context context;

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Query to create table in databse
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                           "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENTS + " TEXT," + CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    //Executes once a database change is occurred
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // table 에 insert
    public void addNotes(Dictionary dictionary) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTENTS, dictionary.getContents());
        values.put(CREATED_AT, dictionary.getDate());

        if (dictionary.getContents().equals("")){   //공란일 경우 pass

        }else
           db.insert(TABLE_NAME, null, values);
    }

    public void editRow(Dictionary dictionary, int key) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTENTS, dictionary.getContents());
        values.put(CREATED_AT, dictionary.getDate());

        db.update(TABLE_NAME, values, KEY_ID + " = '" + key + "'", new String[]{});

    }

    //method to list all details from table
    public ArrayList<Dictionary> getAllDatas() {

        ArrayList<Dictionary> arrayList = new ArrayList<Dictionary>();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Dictionary dictionary = new Dictionary();
                dictionary.setKey(cursor.getInt(0));
                dictionary.setContents(cursor.getString(1));
                dictionary.setDate(cursor.getString(2));

                arrayList.add(dictionary);
            } while (cursor.moveToNext());
        }

        return arrayList;
    }

    public void deleteRow(int key) {    //code to delete a row from the table
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = '" + key + "'", new String[]{});
        db.close();

    }
}