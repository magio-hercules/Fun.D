package com.fundroid.offstand;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundroid.offstand.data.remote.ConnectionManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import io.reactivex.schedulers.Schedulers;

import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;

public class SettingActivity extends AppCompatActivity {
    static final String TAG = "[SETTING]";
    DBConnect db = new DBConnect(this);

//    EditText Text_Name = (EditText) findViewById(R.id.Text_Name);
//    EditText Text_Avatar = (EditText) findViewById(R.id.Text_Avatar);
//    Button button_save = (Button) findViewById(R.id.button_save);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 버터나이프 사용
        ButterKnife.bind(this);

        FrameLayout state_black = (FrameLayout) findViewById(R.id.fragment_container_stats);
        state_black.setVisibility(View.GONE);

        ImageView stats_save = (ImageView) findViewById(R.id.setting_stats_save);
        stats_save.setVisibility(View.GONE);
    }

    @OnClick(R.id.setting_stats)
    public void Stats_Click(){
        FrameLayout state_black = (FrameLayout) findViewById(R.id.fragment_container_stats);
        state_black.setVisibility(View.VISIBLE);

        ImageView stats_save = (ImageView) findViewById(R.id.setting_stats_save);
        stats_save.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.setting_stats_save)
    public void Save_Click(){
        FrameLayout state_black = (FrameLayout) findViewById(R.id.fragment_container_stats);
        state_black.setVisibility(View.GONE);

        ImageView stats_save = (ImageView) findViewById(R.id.setting_stats_save);
        stats_save.setVisibility(View.GONE);
    }

    // 해당 id 뒤에 있는 객체에 대해서 이벤트 처리를 어떻게 할 것인가를 정하는 기능([return] true : 불가능 / false : 가능)
    @OnTouch(R.id.fragment_container_stats)
    public boolean Click_block(){
        return true;
    }


//    @OnClick(R.id.button_save)
//    public void Save() {
//        EditText Text_Name = (EditText) findViewById(R.id.Text_Name);
//        EditText Text_Avatar = (EditText) findViewById(R.id.Text_Avatar);
//
//        Log.d(TAG, "DB Selecting");
//        Cursor selectDB = db.select();
//        Log.d(TAG, "DB Selecting Succes");
//
//        if (selectDB.getCount() == 0){
//            db.add(Text_Name.getText().toString(), Integer.parseInt(Text_Avatar.getText().toString()));
//        }
//        else {
//            db.update(Text_Name.getText().toString(), Integer.parseInt(Text_Avatar.getText().toString()));
//        }
//    }
//
//    @OnClick(R.id.button_show)
//    public void Load() {
//        EditText Text_Name = (EditText) findViewById(R.id.Text_Name);
//        EditText Text_Avatar = (EditText) findViewById(R.id.Text_Avatar);
//
//        Log.d(TAG, "DB Selecting");
//        Cursor selectDB = db.select();
//        Log.d(TAG, "DB Selecting Succes");
//
//        selectDB.moveToFirst();
//
//        Text_Name.setText(selectDB.getString(0));
//        Text_Avatar.setText(selectDB.getString(1));
//    }
}
