package com.example.offstand;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.offstand.SplashActivity.adapter;

public class MainActivity extends AppCompatActivity {

    ImageButton imagebtn;

    ImageView myImage;
    TextView myNameText;
    TextView winText;
    TextView loseText;

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;

    private DBInfoViewModel mDBInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("@@", "@@" + adapter.getItemCount());


        // ViewModelProvider에서 새 ViewModel 또는 기존 ViewModel을 가져오십시오.
        mDBInfoViewModel = ViewModelProviders.of(this).get(DBInfoViewModel.class);

        // getAlphatedWords에서 반환된 LiveData에 대한 관찰자 추가
        // 관찰된 데이터가 변경되고 활동이 수행되면 온Changed() 메서드가 실행됨
        // 전경에.
        mDBInfoViewModel.getmAllDBInfos().observe(this, new Observer<List<DBInfo>>() {
            @Override
            public void onChanged(@Nullable List<DBInfo> dbInfos) {
                // 어댑터에서 캐시 된 단어 사본을 업데이트하십시오.
                adapter.setDBInfos(dbInfos);
            }
        });

        //줄 사이에 수평선 넣기
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        Log.d("@@", "@@" + adapter.getItemCount());

        myImage = (ImageView) findViewById(R.id.myImage);
        myNameText = (TextView) findViewById(R.id.myName);
        winText = (TextView) findViewById(R.id.myWin);
        loseText = (TextView) findViewById(R.id.myLose);
        imagebtn = (ImageButton) findViewById(R.id.imagebtn);


        SharedPreferences sharedPreferences = getSharedPreferences("version", MODE_PRIVATE);

        myImage.setImageResource(sharedPreferences.getInt("myImage", 0));
        myNameText.setText(sharedPreferences.getString("myName", ""));
        winText.setText(Integer.toString(sharedPreferences.getInt("myWin", 0)));
        loseText.setText(Integer.toString(sharedPreferences.getInt("myLose", 0)));

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));

            }
        });

    }
}
