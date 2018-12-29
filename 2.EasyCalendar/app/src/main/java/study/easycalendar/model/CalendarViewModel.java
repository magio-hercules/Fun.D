package study.easycalendar.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.annimon.stream.Stream;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import study.easycalendar.model.local.AppDatabase;
import study.easycalendar.model.local.ScheduleDao;

public class CalendarViewModel extends AndroidViewModel implements OnDateSelectedListener, DayViewDecorator {

    private ScheduleDao scheduleDao;
    private ExecutorService executorService;
    private CalendarNavigator navigator;
    private List<LocalDate> schedules;

    public interface CalendarNavigator {
        void onSelectedDayChange(LocalDate selectedDate);
    }

    public void setNavigator(CalendarNavigator navigator) {
        this.navigator = navigator;
    }

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        scheduleDao = AppDatabase.getInstance(application).scheduleDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void setData(List<Schedule> newSchedules) {
        schedules = Stream.of(newSchedules)
                .map(Schedule::getStartDate)
                .toList();
    }

    public LiveData<List<Schedule>> getAllSchedules() {
        return scheduleDao.getSchedules();
    }

    public LiveData<List<Schedule>> getSchedules(LocalDate selectedDate) {
        return scheduleDao.getSchedulesOnSelectedDate(selectedDate);
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
