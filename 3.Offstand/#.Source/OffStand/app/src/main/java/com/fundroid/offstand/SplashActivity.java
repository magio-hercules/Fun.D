package com.fundroid.offstand;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {
    static final String TAG = "[SPLASH]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        try {
            GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
//        gifImageView.setImageResource(R.drawable.splash_gif);
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.splash_gif);
            gifImageView.setImageDrawable(gifDrawable);

            gifDrawable.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                    Log.d(TAG, "onAnimationCompleted");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //0513 -> SettingActivity에 이름값이 "" 이면 Setting 이동 아니면 MainActivity로 이동

                            if ("" == SettingActivity.userNameCheck) {
                                Intent settingIntent = new Intent(SplashActivity.this, SettingActivity.class);
                                startActivity(settingIntent);
                                finish();
                            } else {
                                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    }, 1500);
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "end onCreate");
    }
}
