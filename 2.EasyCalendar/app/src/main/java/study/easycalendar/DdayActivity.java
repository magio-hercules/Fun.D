package study.easycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import study.easycalendar.adapter.DdayAdapter;
import study.easycalendar.list.ListActivity;
import study.easycalendar.model.Dday;

public class DdayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Dday> ddayList;

    private DdayAdapter adapter;

    private RecyclerView rvDday, rvCounting;

    ItemTouchHelper itemTouchHelper;

    DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.content_dday);
        setContentView(R.layout.activity_dday);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ddayList = new ArrayList<>();

        rvDday = findViewById(R.id.rv_dday);
        rvCounting = findViewById(R.id.rv_counting);

        rvDday.setLayoutManager(new LinearLayoutManager(this));
        rvCounting.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DdayAdapter(ddayList);
        rvDday.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback
                = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                ddayList.remove(position);
                adapter.notifyDataSetChanged();
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);




        ImageView newDday = (ImageView) findViewById(R.id.iv_new_dday);

        newDday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent(getApplicationContext(), DdayEditActivity.class);
                startActivity(intent);

            }
        });

        Button btnDday = (Button) findViewById(R.id.btn_dday);

        btnDday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rvDday.setVisibility(View.VISIBLE);
                rvCounting.setVisibility(View.GONE);

                itemTouchHelper.attachToRecyclerView(rvDday);

                ddayList.clear();


                testDday();
            }
        });

        Button btnCounting = (Button) findViewById(R.id.btn_counting);

        btnCounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rvDday.setVisibility(View.GONE);
                rvCounting.setVisibility(View.VISIBLE);

                itemTouchHelper.attachToRecyclerView(rvCounting);

                ddayList.clear();

                testCounting();
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void testDday()
    {

        ddayList.add(new Dday("고등어","20190101", -769226,null));
        ddayList.add(new Dday("미꾸라지","20190215", -26624, null));
        ddayList.add(new Dday("갈치","20190330",-3285959, null));

        rvDday.setAdapter(adapter);
    }

    private void testCounting()
    {

        ddayList.add(new Dday("오징어","20180101",-14575885, null));
        ddayList.add(new Dday("꼴뚜기","20180215",-769226,null));
        ddayList.add(new Dday("랍스터","20180330",-26624, null));

        rvCounting.setAdapter(adapter);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.nav_calendar:
                intent = new Intent(this, CalendarActivity.class);
                break;
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