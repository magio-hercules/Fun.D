package com.example.off;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity { // 결과보기 창

    static final ArrayList<DBInfo> dbInfos = new ArrayList<DBInfo>();
    static ItemAdapter adapter;
    Button button;
    DBHelper dbHelper;
    private String tableName = "roundScore"; // 데이터 따로 뺄것/ DBHelper랑 겹침


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //SharedPreferences -> 개인 승무패 등등 저장
        sharedPreferences = getSharedPreferences("version", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("myImage", R.drawable.image2);
        editor.putString("myName", "민석");
        editor.putInt("myWin", 100);
        editor.putInt("myLose", 104);
        editor.commit();

        dbHelper = new DBHelper(this, "db.db", null, 1);
        dbHelper.database = dbHelper.getWritableDatabase();

        dbInfos.clear();

        dbInfos.add(new DBInfo(R.drawable.score01, "쫑미니", "4", "3"));
        dbInfos.add(new DBInfo(R.drawable.score02, "지수오", "3", "3"));
        dbInfos.add(new DBInfo(R.drawable.score03, "문", "2", "3"));
        dbInfos.add(new DBInfo(R.drawable.score04, "민석", "1", "4"));

        adapter = new ItemAdapter(this);

        if (dbHelper.database != null) {
            String sqlInsert = "insert into " + tableName + "(rank, userId, win, lose) values(?,?,?,?)";

            for (int i = 0; i < dbInfos.size(); i++) {
                Object[] parmas = {dbInfos.get(i).rank, dbInfos.get(i).userId, dbInfos.get(i).win, dbInfos.get(i).lose};
                dbHelper.database.execSQL(sqlInsert, parmas);
            }
        }

        if (dbHelper.database != null) {
            String sqlSelect = "select rank, userId, win, lose from " + tableName;
            Cursor cursor = dbHelper.database.rawQuery(sqlSelect, null);

            for (int i = cursor.getCount(); i > cursor.getCount() - dbInfos.size(); i--) { // cusor.getCount() - userNumber //// 여기서 3명이라 가정
                cursor.moveToNext();
                int rank = cursor.getInt(0);
                String userId = cursor.getString(1);
                String win = cursor.getString(2);
                String lose = cursor.getString(3);

                adapter.addItem(new Item(rank, userId, win, lose));
                adapter.notifyDataSetChanged();
            }
            cursor.close();
        }
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("@@", "@@" + adapter.getItem(0));

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void getSharedpref() { //

        String name = null;
        name = sharedPreferences.getString("myname", "");

    }
}

