package com.fundroid.offstand;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.fundroid.offstand.ui.lobby.LobbyActivity;
import com.fundroid.offstand.utils.GlideApp;
import com.fundroid.offstand.utils.OffstandGlideApp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.fundroid.offstand.core.AppConstant.PREF_NAME;
import static com.fundroid.offstand.data.local.prefs.AppPreferencesHelper.PREF_KEY_USER_NAME;

public class SplashActivity extends AppCompatActivity {
    static final String TAG = "[SPLASH]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        try {
//            GifImageView gifImageView = findViewById(R.id.GifImageView);
//            GifDrawable gifDrawable = new GifDrawable(getResources(), R.raw.splash_gif);
//            gifImageView.setImageDrawable(gifDrawable);
//
//            gifDrawable.addAnimationListener(loopNumber -> {
//                Log.d(TAG, "onAnimationCompleted");
//
//                new Handler().postDelayed(() -> {
//                    //0513 -> SettingActivity에 이름값이 "" 이면 Setting 이동 아니면 MainActivity로 이동
//
//                    if ("" == getSharedPreferences("version", MODE_PRIVATE).getString("userName", "")) {
//                        Intent settingIntent = new Intent(SplashActivity.this, SettingActivity.class);
//                        startActivity(settingIntent);
//                        finish();
//                    } else {
//                        Intent LobbyIntent = new Intent(SplashActivity.this, LobbyActivity.class);
//                        startActivity(LobbyIntent);
//                        finish();
//                    }
//                }, 1500);
//            });
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        VideoView splash = findViewById(R.id.splash);
        String videoRootPath = "android.resource://" + getPackageName() + "/";
        splash.setVideoURI(Uri.parse(videoRootPath + R.raw.mp4_splash));
        splash.setZOrderOnTop(true);
        splash.start();
        splash.setOnCompletionListener(mp -> {
            new Handler().postDelayed(() -> {
                //0513 -> SettingActivity에 이름값이 "" 이면 Setting 이동 아니면 MainActivity로 이동

                if ("" == getSharedPreferences("version", MODE_PRIVATE).getString("userName", "")) {
                    Intent settingIntent = new Intent(SplashActivity.this, SettingActivity.class);
                    startActivity(settingIntent);
                    finish();
                } else {
                    Intent LobbyIntent = new Intent(SplashActivity.this, LobbyActivity.class);
                    startActivity(LobbyIntent);
                    finish();
                }
            }, 1500);
        });

        Log.d(TAG, "end onCreate");
    }
}
