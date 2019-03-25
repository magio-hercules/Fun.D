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

    @OnClick(R.id.button_play)
    public void goPlay() {
        Log.d(TAG, "goPlay");

        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        startActivity(intent);
    }
}
