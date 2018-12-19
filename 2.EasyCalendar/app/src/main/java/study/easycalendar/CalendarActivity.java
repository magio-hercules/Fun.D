package study.easycalendar;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import org.threeten.bp.LocalDate;

import study.easycalendar.adapter.CalendarAdapter;
import study.easycalendar.databinding.ActivityCalendarBinding;
import study.easycalendar.list.ListActivity;
import study.easycalendar.model.CalendarViewModel;

public class CalendarActivity extends AppCompatActivity implements CalendarViewModel.CalendarNavigator, NavigationView.OnNavigationItemSelectedListener {

    private ActivityCalendarBinding binding;
    private CalendarAdapter calendarAdapter;
    private CalendarViewModel calendarViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        binding.setCalendarViewModel(calendarViewModel);
        calendarViewModel.setNavigator(this);

        setSupportActionBar(binding.contentCalendar.toolbar);
        calendarAdapter = new CalendarAdapter();
        binding.contentCalendar.schedule.setLayoutManager(new LinearLayoutManager(this));
        binding.contentCalendar.schedule.setAdapter(calendarAdapter);
        binding.contentCalendar.calendar.setOnDateChangedListener(calendarViewModel);
        calendarViewModel.getSchedules(LocalDate.now()).observe(this, newSchedules -> calendarAdapter.setData(newSchedules));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.contentCalendar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        binding.contentCalendar.fab.setOnClickListener(v -> startActivity(new Intent(this, DetailActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        calendarViewModel.getAllSchedules().observe(this, schedules -> {
            calendarViewModel.setData(schedules);
            binding.contentCalendar.calendar.addDecorator(calendarViewModel);
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSelectedDayChange(LocalDate selectedDate) {
        calendarViewModel.getSchedules(selectedDate).observe(this, newSchedules -> calendarAdapter.setData(newSchedules));
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
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(intent);
        overridePendingTransition(0, 0);
//        finish();
        return true;
    }
}
