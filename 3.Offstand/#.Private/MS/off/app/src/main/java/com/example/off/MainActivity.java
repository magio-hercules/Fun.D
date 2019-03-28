package com.example.off;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.off.SplashActivity.adapter;

public class MainActivity extends AppCompatActivity {

    ImageButton imagebtn;

    ImageView myImage;
    TextView myNameText;
    TextView winText;
    TextView loseText;

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);




        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Log.d("@@", "@@" + adapter.getItem(0));
        Log.d("@@", "@@" + adapter.getItem(1));
        Log.d("@@", "@@" + adapter.getItem(2));
        Log.d("@@", "@@" + adapter.getItem(3));

        myImage = (ImageView) findViewById(R.id.myImage);
        myNameText = (TextView) findViewById(R.id.myName);
        winText = (TextView) findViewById(R.id.myWin);
        loseText = (TextView) findViewById(R.id.myLose);
        imagebtn = (ImageButton) findViewById(R.id.imagebtn);


        SharedPreferences sharedPreferences = getSharedPreferences("version", MODE_PRIVATE);

        myImage.setImageResource(sharedPreferences.getInt("myImage", 0));
        myNameText.setText(sharedPreferences.getString("myName", ""));
        winText.setText(Integer.toString(sharedPreferences.getInt("myWin", 0)));
        loseText.setText(Integer.toString(sharedPreferences.getInt("myLose", 0)));

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));

            }
        });

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemAdapter.ViewHolder holder, View view, int i) {
                Item item = adapter.getItem(i);

                Toast.makeText(getApplicationContext(), "UserId 선택됨 : " + item.getUserId(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
