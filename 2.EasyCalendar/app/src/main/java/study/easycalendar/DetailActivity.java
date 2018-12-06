package study.easycalendar;

import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import study.easycalendar.list.ListActivity;

public class DetailActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "Detail";

    Spinner spinner_category;
    Spinner spinner_repeat;
    Spinner spinner_notification;

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
                Snackbar.make(view, "저장 기능 추가하기", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                Toast.makeText(parent.getContext(),
                        parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(parent.getContext(),
                               parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(parent.getContext(),
                        parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
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
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int min = calendar.get(calendar.MINUTE);
        int sec = calendar.get(calendar.SECOND);

        Log.d(TAG, "현재 시간 (" + hour + "시 " + min + "분 " + sec + "초)");
    }
}
