package study.easycalendar.list;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import study.easycalendar.CalendarActivity;
import study.easycalendar.DdayActivity;
import study.easycalendar.R;
import study.easycalendar.adapter.ListAdapter;
import study.easycalendar.model.Schedule;
import study.easycalendar.model.local.AppDatabase;
import study.easycalendar.model.local.DatabaseHandler;
import study.easycalendar.model.local.ScheduleDao;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    private RecyclerView recyclerView;
    private ScheduleDao dao;
    private ArrayList<Schedule> arrayList;
    private ListAdapter adapter;
    private List<Schedule> scheduleListFromDB = new ArrayList<Schedule>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.schedule_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MyItemDecoration());

        dao = AppDatabase.getInstance(this).scheduleDao();

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
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    private void loadNotes() {
        arrayList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                scheduleListFromDB = DatabaseHandler.getInstance().getSchedulesList();
                arrayList.addAll(scheduleListFromDB);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();

        adapter = new ListAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        //항목하나하나당 한번씩 호출되서, 항목 하나하나를 다양하게 구현
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.nav_schedule:
                intent = new Intent(this, ListActivity.class);
                break;
            case R.id.nav_dday:
                intent = new Intent(this, DdayActivity.class);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "공유하기 기능 추가하기", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        return true;
    }
}
