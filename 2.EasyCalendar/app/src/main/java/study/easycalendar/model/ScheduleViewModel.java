package study.easycalendar.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
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
import org.threeten.bp.LocalTime;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import study.easycalendar.DetailActivity;
import study.easycalendar.model.local.AppDatabase;
import study.easycalendar.model.local.ScheduleDao;

public class ScheduleViewModel extends AndroidViewModel {

    private Schedule schedule;
    private ScheduleDao scheduleDao;
    private ExecutorService executorService;
    private Context context;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        scheduleDao = AppDatabase.getInstance(application).scheduleDao();
        executorService = Executors.newSingleThreadExecutor();
        context = application;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }


    public void onItemClick() {
//        executorService.execute(() -> scheduleDao.deleteSchedule(schedule));
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", schedule.id);
        context.startActivity(intent);
    }

    public boolean onItemLongClick() {
        schedule.setTitle("수정된 메모 제목");
        schedule.setMemo("메모메모");
        executorService.execute(() -> scheduleDao.updateSchedule(schedule));
        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
