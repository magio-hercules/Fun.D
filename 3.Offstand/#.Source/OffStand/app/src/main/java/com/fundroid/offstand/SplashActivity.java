package com.fundroid.offstand;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;


public class SplashActivity extends AppCompatActivity {
    static final String TAG = "[SPLASH]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setImageResource(R.drawable.splash_gif);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 4000);

        Log.d(TAG, "end onCreate");
    }
}
