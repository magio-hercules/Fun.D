package com.example.eom.offstand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "[MAIN]";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버터나이프 사용
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_room)
    public void goRoom() {
        Log.d(TAG, "goRoom");

        Intent intent = new Intent(MainActivity.this, RoomActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_room2)
    public void goRoom2() {
        Log.d(TAG, "goRoom2");

        Intent intent = new Intent(MainActivity.this, Room2Activity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_play)
    public void goPlay() {
        Log.d(TAG, "goPlay");

        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        startActivity(intent);
    }
}
