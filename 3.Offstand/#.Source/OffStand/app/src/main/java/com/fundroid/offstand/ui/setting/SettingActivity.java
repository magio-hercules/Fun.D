package com.fundroid.offstand.ui.setting;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fundroid.offstand.R;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.ui.lobby.LobbyActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import dagger.android.AndroidInjection;

public class SettingActivity extends AppCompatActivity implements View.OnTouchListener {
    static final String TAG = "[SETTING]";

    static SharedPreferences sharedPreferences;
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

    int characterState;

    @Inject
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        AndroidInjection.inject(this);

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


        final int[] character = {0, R.drawable.me_character_1, R.drawable.me_character_2,
                R.drawable.me_character_3, R.drawable.me_character_4,
                R.drawable.me_character_5, R.drawable.me_character_6,
                R.drawable.me_character_7, R.drawable.me_character_8,
                R.drawable.me_character_9, R.drawable.me_character_10};


//        sharedPreferences = getSharedPreferences("version", MODE_PRIVATE);
//        editor = sharedPreferences.edit();


//        userName.setText(sharedPreferences.getString("userName", ""));
        userName.setText(dataManager.getUserName());

//        setting_character.setImageResource(character[sharedPreferences.getInt("character", 1)]);
        setting_character.setImageResource(character[dataManager.getUserAvatar()]);

//        characterState = sharedPreferences.getInt("character", 1 );
        characterState = dataManager.getUserAvatar();

//        sharedPreferences.getString("userName", "");


        //총게임수, 승수수, 승률
//        sharedPreferences.getInt("total", 0);
//        sharedPreferences.getInt("win", 0);
//        sharedPreferences.getFloat("per", 0);


//        editor.putInt("total", 5);
//        editor.commit();
//        dataManager.setUserTotal(5);


//        editor.putInt("win", 3);
//        editor.commit();
//        dataManager.setUserWin(3);


//        editor.putFloat("per", (new Float(sharedPreferences.getInt("win", 0)) / new Float(sharedPreferences.getInt("total", 0))) * 100);
//        editor.commit();


//        setting_stats_total_text.setText("" + sharedPreferences.getInt("total", 0));
//        setting_stats_win_text.setText("" + sharedPreferences.getInt("win", 0));
//        setting_stats_per_text.setText("" + (int) (sharedPreferences.getFloat("per", 0)) + "%");

        setting_stats_total_text.setText("" + dataManager.getUserTotal());
        setting_stats_win_text.setText("" + dataManager.getUserWin());
        setting_stats_per_text.setText("" + ((int) (((float) dataManager.getUserWin() / (float) dataManager.getUserTotal()) * 100)) + "%");



        // 커서를 끝에 위치시키기
        userName.setSelection(userName.length());

        if (userName.getText().toString().length() == 0) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        }

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
                MediaPlayer.create(SettingActivity.this, R.raw.mouth_interface_button).start();

//                AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                        .build();
//
//                SoundPool sp = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
//                int soundID = sp.load(SettingActivity.this, R.raw.mouth_interface_button, 1);
//                //sp.play(soundID, 1f, 1f, 0, 0,1f);
//
//                sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                    @Override
//                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                        sp.play(soundID, 1f, 1f, 0, 0,1f);
//                    }
//                });

                if (characterState > 1) {
                    characterState--;
                } else {
                    characterState = character.length - 1;
                }

                setting_character.setImageResource(character[characterState]);
            }
        });

        setting_rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer.create(SettingActivity.this, R.raw.mouth_interface_button).start();



                if (characterState < character.length - 1) {
                    characterState++;
                } else {
                    characterState = 1;
                }

                setting_character.setImageResource(character[characterState]);
            }
        });

        setting_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer.create(SettingActivity.this, R.raw.mouth_interface_button).start();


                String input = userName.getText().toString();

                userName.setFocusable(false);
                userName.setFocusableInTouchMode(true);

                dataManager.setUserAvatar(characterState);
                dataManager.setUserName(input);
//                editor.putInt("character", characterState);
//                editor.putString("userName", input);
//                editor.commit();

                startActivity(new Intent(getApplicationContext(), LobbyActivity.class));
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
        MediaPlayer.create(SettingActivity.this, R.raw.mouth_interface_button).start();

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
        MediaPlayer.create(SettingActivity.this, R.raw.mouth_interface_button).start();

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

    public static void start(Context context) {
        final Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
