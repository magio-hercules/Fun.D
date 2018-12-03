package study.easycalendar.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.annimon.stream.Stream;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import study.easycalendar.TestActivity;
import study.easycalendar.model.local.AppDatabase;
import study.easycalendar.model.local.ScheduleDao;

import static study.easycalendar.App.getApplicationInstance;

public class ScheduleViewModel extends AndroidViewModel implements OnDateSelectedListener, DayViewDecorator {

    private ScheduleDao scheduleDao;
    private ExecutorService executorService;
    private ScheduleNavigator navigator;

    public interface ScheduleNavigator {
        void onSelectedDayChange(LocalDate selectedDate);
    }

    public void setNavigator(ScheduleNavigator navigator) {
        this.navigator = navigator;
    }

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        scheduleDao = AppDatabase.getInstance(application).scheduleDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    private List<LocalDate> schedules;

    public void setData(List<Schedule> newSchedules) {
        schedules = Stream.of(newSchedules)
                .map(Schedule::getDate)
                .toList();
    }

    public LiveData<List<Schedule>> getAllSchedules() {
        return scheduleDao.getSchedules();
    }

    public LiveData<List<Schedule>> getSchedules(LocalDate selectedDate) {
        return scheduleDao.getSchedulesOnSelectedDate(selectedDate);
    }

    public void testDataInsert(View v) {
        executorService.execute(() -> scheduleDao.insertSchedule(new Schedule(LocalDate.now(), LocalTime.now(), "test", "test")));
    }

    public void testGetData(View v) {
        executorService.execute(() -> {
            Log.d("lsc", "데이타 with No LiveData " + scheduleDao.getSchedulesList().size());
        });
    }

    public void goToTest(View v) {
        TestActivity.start(getApplicationInstance());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
        navigator.onSelectedDayChange(calendarDay.getDate());
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        return schedules.contains(calendarDay.getDate());
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.addSpan(new DotSpan(5, Color.RED));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
