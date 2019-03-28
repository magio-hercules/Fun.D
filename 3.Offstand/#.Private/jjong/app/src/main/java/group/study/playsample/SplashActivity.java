package group.study.playsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import pl.droidsonroids.gif.GifImageView;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
//        gifImageView.setGifImageResource(R.drawable.splash_gif);
        gifImageView.setImageResource(R.drawable.splash_gif);


        Log.d("[Splash]", "onCreate");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 6000);

        Log.d("[Splash]", "end onCreate");
    }

}
