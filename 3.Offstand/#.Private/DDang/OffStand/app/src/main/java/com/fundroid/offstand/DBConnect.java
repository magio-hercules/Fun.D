package com.fundroid.offstand;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBConnect extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "OffStand";

    // MYINFO(개인정보)
    private static final String TABLE_MYINFO = "MYINFO";
    private static final String MYINFO_KEY_NAME = "name";
    private static final String MYINFO_KEY_AVATAR = "avatar";
    private static final String MYINFO_KEY_WIN = "win";
    private static final String MYINFO_KEY_LOSE = "lose";
    private static final String MYINFO_KEY_REGTIME = "regtime";

    public DBConnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_MYINFO + "(" +
                MYINFO_KEY_NAME + " TEXT PRIMARY KEY, " +
                MYINFO_KEY_AVATAR + " INTEGER, " +
                MYINFO_KEY_WIN + " INTEGER, " +
                MYINFO_KEY_LOSE + " INTEGER, " +
                MYINFO_KEY_REGTIME + " DATE " +
                ")";
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_DRINK =
                "DROP TABLE IF EXISTS " + TABLE_MYINFO;
        db.execSQL(DROP_TABLE_DRINK);

        onCreate(db);
    }

    public Cursor select(){
        SQLiteDatabase db = this.getWritableDatabase();

        String SELECT_TABLE_MYINFO =
//                "SELECT * FROM " + TABLE_MYINFO + " WHERE name = " + name + ";";
                "SELECT * FROM " + TABLE_MYINFO + ";";

        Cursor result = db.rawQuery(SELECT_TABLE_MYINFO, null);

        return result;
    }

    public void add(String name, Integer avatar) {
        SQLiteDatabase db = this.getWritableDatabase();

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date);

        ContentValues values = new ContentValues();
        values.put(MYINFO_KEY_NAME, name);
        values.put(MYINFO_KEY_AVATAR, avatar);
        values.put(MYINFO_KEY_WIN, 0);
        values.put(MYINFO_KEY_LOSE, 0);
        values.put(MYINFO_KEY_REGTIME, formatDate);

        db.insert(TABLE_MYINFO, null, values);

//        String INSERT_TABLE_USER =
//                "INSERT INTO " + TABLE_MYINFO +  " ( " + MYINFO_KEY_NAME + "," + MYINFO_KEY_AVATAR + "," + MYINFO_KEY_WIN + "," + MYINFO_KEY_LOSE + "," + MYINFO_KEY_REGTIME +  " ) "
//                        + "VALUES ( " + name + "," + avatar + "," + 0 + "," + 0 + "," + formatDate + " ) ";

//        db.execSQL(INSERT_TABLE_USER);

        db.close();
    }
}
