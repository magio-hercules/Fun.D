package com.fundroid.offstand;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class SettingActivity extends AppCompatActivity implements View.OnTouchListener {
    static final String TAG = "[SETTING]";

    static String userNameCheck = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText userName;

    ImageView setting_character;
    ImageView setting_rightButton;
    ImageView setting_leftButton;
    ImageView setting_stats;
    ImageView setting_save;

    // 통계화면
    FrameLayout state_back;
    ImageView stats_title;
    ImageView stats_total;
    ImageView stats_win;
    ImageView stats_per;
    ImageView stats_save;

    // 개인통계
    TextView setting_stats_total_text;
    TextView setting_stats_win_text;
    TextView setting_stats_per_text;

    // 음악플레이
    static MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userName = (EditText) findViewById(R.id.userName);

        setting_character = (ImageView) findViewById(R.id.setting_Character);
        setting_leftButton = (ImageView) findViewById(R.id.setting_LeftButton);
        setting_rightButton = (ImageView) findViewById(R.id.setting_RightButton);
        setting_stats = (ImageView) findViewById(R.id.setting_stats);
        setting_save = (ImageView) findViewById(R.id.setting_save);

        // 통계화면
        state_back = (FrameLayout) findViewById(R.id.fragment_container_stats);
        stats_title = (ImageView) findViewById(R.id.setting_stats_title);
        stats_total = (ImageView) findViewById(R.id.setting_stats_total);
        stats_win = (ImageView) findViewById(R.id.setting_stats_win);
        stats_per = (ImageView) findViewById(R.id.setting_stats_per);
        stats_save = (ImageView) findViewById(R.id.setting_stats_save);


        // 개인통계
        setting_stats_total_text = (TextView) findViewById(R.id.setting_stats_total_text);
        setting_stats_win_text = (TextView) findViewById(R.id.setting_stats_win_text);
        setting_stats_per_text = (TextView) findViewById(R.id.setting_stats_per_text);

        state_back.setVisibility(View.GONE);
        stats_title.setVisibility(View.GONE);
        stats_total.setVisibility(View.GONE);
        stats_win.setVisibility(View.GONE);
        stats_per.setVisibility(View.GONE);
        stats_save.setVisibility(View.GONE);

        setting_stats_total_text.setVisibility(View.GONE);
        setting_stats_win_text.setVisibility(View.GONE);
        setting_stats_per_text.setVisibility(View.GONE);

        int[] character = {R.drawable.me_character_1, R.drawable.me_character_2,
                R.drawable.me_character_5, R.drawable.me_character_8, R.drawable.me_character_9};


        sharedPreferences = getSharedPreferences("version", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mp = MediaPlayer.create(SettingActivity.this, R.raw.mp3_1);


        userName.setText(sharedPreferences.getString("userName", ""));
        setting_character.setImageResource(character[sharedPreferences.getInt("character", 0)]);

        userNameCheck = sharedPreferences.getString("userName","");


        sharedPreferences.getString("userName","");

        //총게임수, 승수수, 승률
        sharedPreferences.getInt("total",0);
        sharedPreferences.getInt("win",0);
        sharedPreferences.getFloat("per",0);

        editor.putInt("total",5);
        editor.commit();

        editor.putInt("win",3);
        editor.commit();

        editor.putFloat("per", (new Float(sharedPreferences.getInt("win",0)) / new Float(sharedPreferences.getInt("total",0)))*100);
        editor.commit();

        setting_stats_total_text.setText(""+sharedPreferences.getInt("total",0));
        setting_stats_win_text.setText(""+sharedPreferences.getInt("win",0));
        setting_stats_per_text.setText(""+(int)(sharedPreferences.getFloat("per",0)));


        // 커서를 끝에 위치시키기
        userName.setSelection(userName.length());


        userName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
                    userName.setFocusable(false);
                    userName.setFocusableInTouchMode(true);
                    return true;
                }
                return false;
            }
        });

        setting_leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = sharedPreferences.getInt("character", 0);
                if (i > 0) {
                    i--;
                } else {
                    i = character.length - 1;
                }
                editor.putInt("character", i);
                editor.commit();
                setting_character.setImageResource(character[sharedPreferences.getInt("character", 0)]);
            }
        });

        setting_rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = sharedPreferences.getInt("character", 0);
                if (i < character.length - 1) {
                    i++;
                } else {
                    i = 0;
                }
                editor.putInt("character", i);
                editor.commit();
                setting_character.setImageResource(character[sharedPreferences.getInt("character", 0)]);
            }
        });

        setting_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_LONG).show();

                String input = userName.getText().toString();

                userName.setFocusable(false);
                userName.setFocusableInTouchMode(true);
                editor.putString("userName", input);
                editor.commit();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        // 버터나이프 사용
        ButterKnife.bind(this);

    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        return false;
    }

    @OnClick(R.id.setting_stats)
    public void Stats_Click() {

        state_back.setVisibility(View.VISIBLE);
        stats_title.setVisibility(View.VISIBLE);
        stats_total.setVisibility(View.VISIBLE);
        stats_win.setVisibility(View.VISIBLE);
        stats_per.setVisibility(View.VISIBLE);
        stats_save.setVisibility(View.VISIBLE);

        setting_stats_total_text.setVisibility(View.VISIBLE);
        setting_stats_win_text.setVisibility(View.VISIBLE);
        setting_stats_per_text.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.setting_stats_save)
    public void Save_Click() {

        state_back.setVisibility(View.GONE);
        stats_title.setVisibility(View.GONE);
        stats_total.setVisibility(View.GONE);
        stats_win.setVisibility(View.GONE);
        stats_per.setVisibility(View.GONE);
        stats_save.setVisibility(View.GONE);

        setting_stats_total_text.setVisibility(View.GONE);
        setting_stats_win_text.setVisibility(View.GONE);
        setting_stats_per_text.setVisibility(View.GONE);

    }

    // 해당 id 뒤에 있는 객체에 대해서 이벤트 처리를 어떻게 할 것인가를 정하는 기능([return] true : 불가능 / false : 가능)
    @OnTouch(R.id.fragment_container_stats)
    public boolean Click_block() {
        return true;
    }
}
