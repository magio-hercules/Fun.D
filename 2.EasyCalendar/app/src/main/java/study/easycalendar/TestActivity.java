package study.easycalendar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import study.easycalendar.model.Schedule;
import study.easycalendar.model.local.AppDatabase;
import study.easycalendar.model.local.DatabaseHandler;
import study.easycalendar.model.local.ScheduleDao;

/**
 * TestActivity
 * DB 연동 테스트 Activity 입니다. 추후에 지울 예정입니다.
 * DB 접근은 기본적으로 MainThread(UIThread)에서 사용하지 않습니다. (IO 처리가 오래 걸릴 수 있기 때문에 다른 스레드에서 접근합니다.)
 * 많은 방법들이 있지만 3가지만 소개합니다.
 * 1. Thread , 2. ExecutorServie, 3. AsyncTask
 * <p>
 * 위와 같은 방법으로 DB에 접근하여 데이터를 받아와 사용합니다.
 * DatabaseHandler.getInstance()
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private ExecutorService executorService;

    private AsyncTask asyncTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_test);
        findViewById(R.id.insert).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.select).setOnClickListener(this);
        findViewById(R.id.select2).setOnClickListener(this);
        findViewById(R.id.select3).setOnClickListener(this);
        ScheduleDao scheduleDao = AppDatabase.getInstance(getApplicationContext()).scheduleDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("lsc", "test " + scheduleDao.getSchedulesList().size());
            }
        }).start();

    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    private List<Schedule> scheduleListFromDB; //가져올 데이터..

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        DatabaseHandler.getInstance().insertSchedule(new Schedule(LocalDate.now(), LocalTime.now(), "테스트 제목", "테스트 내용"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TestActivity.this, "INSERT COMPLETE", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
                break;

            case R.id.delete:
                //나중에 필요한 경우가 생기면 만들게요.
                break;

            case R.id.update:
                //나중에 필요한 경우가 생기면 만들게요.
                break;

            case R.id.select:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread가 아닌 Thread에서 DB에 접근하여 리스트를 가져올 수 있습니다.
                        scheduleListFromDB = DatabaseHandler.getInstance().getSchedulesList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TestActivity.this, "SELECT " + scheduleListFromDB.size(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();

                break;

            case R.id.select2:
                executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    //ExecutorService 또한 다른 Thread에서 작업을 처리합니다.
                    scheduleListFromDB = DatabaseHandler.getInstance().getSchedulesList();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TestActivity.this, "SELECT2 " + scheduleListFromDB.size(), Toast.LENGTH_SHORT).show();
                        }
                    });

                });
                break;

            case R.id.select3:
                //AsyncTask : 백그라운드 작업전 , 백그라운드, 백그라운드 작업 후 등을 종합해서 사용할 수 있습니다.

                asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        scheduleListFromDB = DatabaseHandler.getInstance().getSchedulesList();
                        return scheduleListFromDB;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        Toast.makeText(TestActivity.this, "SELECT3 " + scheduleListFromDB.size(), Toast.LENGTH_SHORT).show();
                    }
                };

                asyncTask.execute();
                break;


        }
    }
}
