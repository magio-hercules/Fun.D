package com.project.study.studyproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemoAdd extends AppCompatActivity {

    EditText et1;
    String gubun;
    int key;
    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);

        et1 = (EditText) findViewById(R.id.contents);

        //get data from intent
        key = getIntent().getIntExtra("key", 0);
        gubun = getIntent().getStringExtra("check");
        String contents = getIntent().getStringExtra("contents");
        et1.setText(contents);
    }

    @Override
    public void onBackPressed() {

        EditText editContents = (EditText)findViewById(R.id.contents);
        String strContents = editContents.getText().toString();

        DbHandler dbHandler = new DbHandler(getApplicationContext());

        if (gubun.equals("add")){
            mNow = System.currentTimeMillis();
            mDate = new Date(mNow);
            String strDate = mFormat.format(mDate);
//            Toast.makeText(getApplicationContext(), strDate, Toast.LENGTH_SHORT).show();

            Dictionary dictionary = new Dictionary(strContents, strDate);
            dbHandler.addNotes(dictionary);
            dbHandler.close();
            Toast.makeText(getApplicationContext(), "메모 입력성공!!", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (gubun.equals("edit")){
            mNow = System.currentTimeMillis();
            mDate = new Date(mNow);
            String strDate = mFormat.format(mDate);

            Dictionary dictionary = new Dictionary(et1.getText().toString(), strDate);
            dbHandler.editRow(dictionary, key);
            dbHandler.close();
            Toast.makeText(getApplicationContext(), "메모 수정성공!!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
