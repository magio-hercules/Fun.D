package study.easycalendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.Calendar;

import study.easycalendar.list.ListActivity;
import study.easycalendar.model.Schedule;
import study.easycalendar.model.local.DatabaseHandler;

public class DetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "[easy][Detail]";

    EditText edit_title;
    EditText edit_memo;

    Spinner spinner_category;
    Spinner spinner_repeat;
    Spinner spinner_notification;

    CheckBox checkBox_dday;

    DrawerLayout drawer;

    // Calendar
    Calendar calendar;
    DatePicker picker_start_date;
    DatePicker picker_end_date;
    TimePicker picker_start_time;
    TimePicker picker_end_time;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_title.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "일정 타이틀을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    InsertSchdule(view);
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_memo = (EditText) findViewById(R.id.edit_memo);

        checkBox_dday = (CheckBox) findViewById(R.id.checkBox_dday);

        initSpinner();
        initDateTime();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Setting 기능 추가하기", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_calendar) {
            intent = new Intent(DetailActivity.this, CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_schedule) {
            intent = new Intent(DetailActivity.this, ListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dday) {
            intent = new Intent(DetailActivity.this, DdayActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(DetailActivity.this, "공유하기 기능 추가하기", Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initSpinner() {
        spinner_category = (Spinner)findViewById(R.id.spinner_category);
        spinner_repeat = (Spinner)findViewById(R.id.spinner_repeat);
        spinner_notification = (Spinner)findViewById(R.id.spinner_notification);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        adapter = ArrayAdapter.createFromResource(
                this, R.array.repeat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_repeat.setAdapter(adapter);
        spinner_repeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        adapter = ArrayAdapter.createFromResource(
                this, R.array.notification, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_notification.setAdapter(adapter);
        spinner_notification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initDateTime() {
        picker_start_date = (DatePicker) findViewById(R.id.picker_start_date);
        picker_end_date = (DatePicker) findViewById(R.id.picker_end_date);
        picker_start_time = (TimePicker) findViewById(R.id.picker_start_time);
        picker_end_time = (TimePicker) findViewById(R.id.picker_end_time);

        calendar = Calendar.getInstance();
        int year = picker_start_date.getYear();
        int month = picker_start_date.getMonth() + 1;
        int day = picker_start_date.getDayOfMonth();
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int min = calendar.get(calendar.MINUTE);
        int sec = calendar.get(calendar.SECOND);

        Log.d(TAG, "현재 시간 (" + year + "년 " + month + "월 " + day + "일 " + hour + "시 " + min + "분 " + sec + "초)");
    }

    private void InsertSchdule(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int startHour, startMinute, endHour, endMinute;
                if(Build.VERSION.SDK_INT < 23){
                    startHour = picker_start_time.getCurrentHour();
                    startMinute = picker_start_time.getCurrentMinute();
                    endHour = picker_end_time.getCurrentHour();
                    endMinute = picker_end_time.getCurrentMinute();
                } else{
                    startHour = picker_start_time.getHour();
                    startMinute = picker_start_time.getMinute();
                    endHour = picker_end_time.getHour();
                    endMinute = picker_end_time.getMinute();
                }

                LocalDate startDate = LocalDate.of(picker_start_date.getYear(), picker_start_date.getMonth() + 1, picker_start_date.getDayOfMonth());
                LocalTime startTime = LocalTime.of(startHour, startMinute);
                LocalDate endDate = LocalDate.of(picker_end_date.getYear(), picker_end_date.getMonth() + 1, picker_end_date.getDayOfMonth());
                LocalTime endTime = LocalTime.of(endHour, endMinute);

                String title = edit_title.getText().toString();
                String memo = edit_memo.getText().toString();

                boolean bDday = checkBox_dday.isChecked();

                String category = spinner_category.getSelectedItem().toString();
                String notification = spinner_notification.getSelectedItem().toString();
                String repeat = spinner_repeat.getSelectedItem().toString();

//                Schedule s = new Schedule(LocalDate.now(), LocalTime.now(), title, memo);
                Schedule s = new Schedule(startDate,startTime,endDate,endTime,title,memo,category,notification,repeat,bDday);

                DatabaseHandler.getInstance().insertSchedule(s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(view, "저장 완료", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
                        Log.d(TAG, "InsertSchdule Complete : Data (" + startDate + ", " + startTime + ", " + endDate + ", " + endTime + ", " +
                                title + ", " + memo + ", " + category + ", " + notification + ", " + repeat + ", " + (bDday ? "D-day checked" : "D-day not Checked") + ")");

//                        Toast.makeText(DetailActivity.this, "INSERT COMPLETE", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
