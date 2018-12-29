package study.easycalendar.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import study.easycalendar.model.local.AppDatabase;
import study.easycalendar.model.local.ScheduleDao;

public class ScheduleViewModel extends AndroidViewModel {

    private Schedule schedule;
    private ScheduleDao scheduleDao;
    private ExecutorService executorService;
    private ScheduleNavigator navigator;

    public interface ScheduleNavigator {

        void startDetailActivity();

        void showDeleteDialog();
    }

    public void setNavigator(ScheduleNavigator navigator) {
        this.navigator = navigator;
    }

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        scheduleDao = AppDatabase.getInstance(application).scheduleDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void onItemClick() {
        navigator.startDetailActivity();
    }

    public boolean onItemLongClick() {
        navigator.showDeleteDialog();
        return true;
    }

    public void deleteSchedule() {
        executorService.execute(() -> scheduleDao.deleteSchedule(schedule));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
