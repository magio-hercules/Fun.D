package com.example.stand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import static com.example.stand.MainActivity.dbInfos;

public class SplashActivity extends AppCompatActivity {

    public static ResultAdapter adapter;
    Button button;
    static final ArrayList<DBInfo> dbInfos = new ArrayList<>();
    SQLiteDatabase database;
    DBHelper dbHelper;
    public static final String tableName = "roundScore";
    public static final String tableMyScore = "myScore";



    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //SharedPreferences
        sharedPreferences = getSharedPreferences("version", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("myImage",R.drawable.image2);
        editor.putString("myName", "민석");
        editor.putInt("myWin", 100);
        editor.putInt("myLose", 104);
        editor.commit();

        getSharedpref();
        //SharedPreferences

        dbHelper = new DBHelper(this, "db.db", null, 1);

        database = dbHelper.getWritableDatabase();

        dbInfos.clear();

        dbInfos.add(new DBInfo(R.drawable.score01, "쫑미니", "4", "3"));
        dbInfos.add(new DBInfo(R.drawable.score02, "지수오","3", "3"));
        dbInfos.add(new DBInfo(R.drawable.score03, "문","2", "3"));
        dbInfos.add(new DBInfo(R.drawable.score04, "민석","1", "4"));



        adapter = new ResultAdapter();

        if (database != null) {
            String sqlInsert = "insert into " + tableName + "(rank, userId, win, lose) values(?,?,?,?)";

            for (int i = 0; i < dbInfos.size(); i++) {
                Object[] parmas = {dbInfos.get(i).rank, dbInfos.get(i).userId, dbInfos.get(i).win, dbInfos.get(i).lose};
                database.execSQL(sqlInsert, parmas);
                Log.d("@@", "@@");
            }
        }

        if (database != null) {
            String sqlSelect = "select rank, userId, win, lose from " + tableName;
            Cursor cursor = database.rawQuery(sqlSelect, null);

            for (int i = cursor.getCount(); i > cursor.getCount() - dbInfos.size(); i--) { // cusor.getCount() - userNumber //// 여기서 3명이라 가성
                cursor.moveToNext();
                int rank = cursor.getInt(0);
                String userId = cursor.getString(1);
                String win = cursor.getString(2);
                String lose = cursor.getString(3);

                adapter.addItem(new ResultItem(rank ,userId, win, lose));
                adapter.notifyDataSetChanged(); // 리스트뷰 갱신
            }
            cursor.close(); // 자원 해제
        }

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void getSharedpref() {

        String Lo = null;

        Lo = sharedPreferences.getString("myName","");

        Log.d("**","**"+Lo);
        //        sharedPreferences.getString("version", "");
    }
}
