package com.fundroid.offstand;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.utils.rx.AppSchedulerProvider;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.fundroid.offstand.core.AppConstant.ROOM_PORT;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "[MAIN]";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버터나이프 사용
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_room)
    public void goRoom() {
        Log.d(TAG, "goRoom");

        Intent intent = new Intent(MainActivity.this, RoomActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_room2)
    public void goRoom2() {
        Log.d(TAG, "goRoom2");

        Intent intent = new Intent(MainActivity.this, Room2Activity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_play)
    public void goPlay() {
        Log.d(TAG, "goPlay");

        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_server)
    public void createSocket() {
        ConnectionManager.serverThreadObservable(ROOM_PORT, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(message -> {
                    Log.d(TAG, "createSocket message " + message);
                }, onError -> {
                    Log.d(TAG, "createSocket onError " + onError.getMessage());
                }, () -> {
                    Log.d(TAG, "createSocket terminated");
                });
    }

    @OnClick(R.id.button_client)
    public void connectSocket() {

        new Thread(() -> {
            try {
                ConnectionManager.clientThreadObservable(InetAddress.getLocalHost(), ROOM_PORT)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(onNext -> {
                            Log.d(TAG, "enterRoom thread " + Thread.currentThread().getName());
                            Log.d(TAG, "enterRoom onNext " + onNext);
                            //                            Toast.makeText(context, onNext, Toast.LENGTH_SHORT).show();
                        }, onError -> {
                            Log.d(TAG, "enterRoom onError " + onError.getMessage());
                        }, () -> {
                            Log.d(TAG, "enterRoom terminated");
                        });
            } catch (UnknownHostException e) {
                Log.e("lsc","e " + e.getMessage());
            }
        }).start();



    }
}
