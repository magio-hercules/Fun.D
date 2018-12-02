package study.easycalendar;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.threeten.bp.LocalDate;

import study.easycalendar.adapter.CalendarAdapter;
import study.easycalendar.databinding.ContentCalendarBinding;
import study.easycalendar.model.ScheduleViewModel;

public class CalendarActivity extends AppCompatActivity implements ScheduleViewModel.ScheduleNavigator {

    private ContentCalendarBinding binding;
    private CalendarAdapter calendarAdapter;
    private ScheduleViewModel scheduleViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.content_calendar);
        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        binding.setScheduleViewModel(scheduleViewModel);
        scheduleViewModel.setNavigator(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendarAdapter = new CalendarAdapter();
        binding.schedule.setLayoutManager(new LinearLayoutManager(this));
        binding.schedule.setAdapter(calendarAdapter);
        binding.calendar.setOnDateChangedListener(scheduleViewModel);



    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduleViewModel.getAllSchedules().observe(this,schedules -> {
            scheduleViewModel.setData(schedules);
            binding.calendar.addDecorator(scheduleViewModel);
        });
    }

    @Override
    public void onSelectedDayChange(LocalDate selectedDate) {
        scheduleViewModel.getSchedules(selectedDate).observe(this, newSchedules -> calendarAdapter.setData(newSchedules));
    }
}
