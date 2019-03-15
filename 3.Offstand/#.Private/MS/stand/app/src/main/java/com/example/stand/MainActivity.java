package com.example.stand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import static com.example.stand.SplashActivity.adapter;

public class MainActivity extends AppCompatActivity {

    ImageButton imagebtn;

    ImageView myImage;
    TextView myNameText;
    TextView winText;
    TextView loseText;

    static final ArrayList<DBInfo> dbInfos = new ArrayList<>();

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("version", MODE_PRIVATE);
        sharedPreferences.getInt("myWin",0);

        myImage = (ImageView) findViewById(R.id.myImage);
        myImage.setImageResource(sharedPreferences.getInt("myImage",0));


        myNameText = (TextView) findViewById(R.id.myName);
        myNameText.setText(sharedPreferences.getString("myName",""));


        winText = (TextView) findViewById(R.id.myWin);
        winText.setText(Integer.toString(sharedPreferences.getInt("myWin",0)));

        loseText = (TextView) findViewById(R.id.myLose);
        loseText.setText(Integer.toString(sharedPreferences.getInt("myLose",0)));


        imagebtn = (ImageButton) findViewById(R.id.imagebtn);

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));

            }
        });

        //클릭시 메소드
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResultItem item = (ResultItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택: " + item.getUserId(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
