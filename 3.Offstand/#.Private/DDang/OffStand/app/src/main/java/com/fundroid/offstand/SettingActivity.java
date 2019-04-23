package com.fundroid.offstand;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fundroid.offstand.data.remote.ConnectionManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
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
    }

    @OnClick(R.id.button_save)
    public void Save() {
        EditText Text_Name = (EditText) findViewById(R.id.Text_Name);
        EditText Text_Avatar = (EditText) findViewById(R.id.Text_Avatar);

        Log.d(TAG, "DB Selecting");
        Cursor selectDB = db.select();
        Log.d(TAG, "DB Selecting Succes");

        if (selectDB.getCount() == 0){
            db.add(Text_Name.getText().toString(), Integer.parseInt(Text_Avatar.getText().toString()));
        }
        else {
//            db.update(new User(Text_Name.toString()));
            Log.d(TAG, "기존 DB가 있다");
        }
    }

    @OnClick(R.id.button_show)
    public void Load() {
        EditText Text_Name = (EditText) findViewById(R.id.Text_Name);
        EditText Text_Avatar = (EditText) findViewById(R.id.Text_Avatar);

        Log.d(TAG, "DB Selecting");
        Cursor selectDB = db.select();
        Log.d(TAG, "DB Selecting Succes");

        selectDB.moveToFirst();

        Text_Name.setText(selectDB.getString(0));
        Text_Avatar.setText(selectDB.getString(1));
    }
}
