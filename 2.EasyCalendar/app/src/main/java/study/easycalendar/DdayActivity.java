package study.easycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import study.easycalendar.adapter.DdayAdapter;
import study.easycalendar.model.Dday;
import study.easycalendar.model.Schedule;
import study.easycalendar.model.local.DatabaseHandler;

public class DdayActivity extends AppCompatActivity {

    private static final int EDIT_SCHEDULE = 1234;

    private List<Dday> ddayList;
    private List<Schedule> scheduleList;

    private DdayAdapter adapterDday, adapterCounting;

    private RecyclerView rvDday, rvCounting;

    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content_dday);
        setContentView(R.layout.activity_dday);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ddayList = new ArrayList<>();
        scheduleList = new ArrayList<>();

        rvDday = findViewById(R.id.rv_dday);
        rvCounting = findViewById(R.id.rv_counting);

        rvDday.setLayoutManager(new LinearLayoutManager(this));
        rvCounting.setLayoutManager(new LinearLayoutManager(this));

        adapterDday = new DdayAdapter(ddayList, this);
        rvDday.setAdapter(adapterDday);

        adapterCounting = new DdayAdapter(ddayList, this);
        rvCounting.setAdapter(adapterCounting);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback
                = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHandler.getInstance().deleteSchedule(scheduleList.get(position));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(DdayActivity.this, scheduleList.get(position).dDayDate + "디데이 삭제 완료", Toast.LENGTH_SHORT).show();

                                if (rvDday.getVisibility() == View.VISIBLE) {
                                    LoadDDays(true);

                                } else {

                                    LoadDDays(false);

                                }

                            }
                        });

                    }
                }).start();

            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);


        ImageView newDday = (ImageView) findViewById(R.id.iv_new_dday);

        newDday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent(getApplicationContext(), DdayEditActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, EDIT_SCHEDULE);

            }
        });


        Button btnDday = (Button) findViewById(R.id.btn_dday);

        btnDday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoadDDays(true);
            }
        });


        Button btnCounting = (Button) findViewById(R.id.btn_counting);

        btnCounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoadDDays(false);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getApplicationContext(), DdayEditActivity.class);
                startActivity(intent);
            }
        });

    }

    private void LoadDDays(final boolean isDDays) {

        ddayList.clear();
        scheduleList.clear();

        adapterDday.notifyDataSetChanged();
        adapterCounting.notifyDataSetChanged();

        if (isDDays) {

            rvDday.setVisibility(View.VISIBLE);
            rvCounting.setVisibility(View.GONE);


            itemTouchHelper.attachToRecyclerView(rvDday);

        } else {

            rvDday.setVisibility(View.GONE);
            rvCounting.setVisibility(View.VISIBLE);

            itemTouchHelper.attachToRecyclerView(rvCounting);

        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (isDDays) {
                    scheduleList = DatabaseHandler.getInstance().getDDayList();
                } else {
                    scheduleList = DatabaseHandler.getInstance().getCountingList();
                }

                for (int i = 0; i < scheduleList.size(); i++) {

                    LocalDate dDay = scheduleList.get(i).dDayDate;

                    ddayList.add(new Dday(scheduleList.get(i).id, scheduleList.get(i).title
                            , LocalDate.of(dDay.getYear(), dDay.getMonth(), dDay.getDayOfMonth()).format(DateTimeFormatter.BASIC_ISO_DATE)
                            , -769226
                            , null));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (isDDays) {

                            rvDday.setAdapter(adapterDday);
                        } else {

                            rvCounting.setAdapter(adapterCounting);
                        }
                    }
                });

            }
        }).start();


    }

    /**************************************************************************
     @Override public boolean onCreateOptionsMenu(Menu menu) {
     // Inflate the menu; this adds items to the action bar if it is present.
     getMenuInflater().inflate(R.menu.main, menu);
     return true;
     }
     **************************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        LoadDDays(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == EDIT_SCHEDULE) {

                //LoadDDays(true);
            }
        }

    }
}