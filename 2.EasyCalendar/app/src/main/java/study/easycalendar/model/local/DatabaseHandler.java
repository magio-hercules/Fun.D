package study.easycalendar.model.local;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import org.threeten.bp.LocalDate;

import java.util.List;

import study.easycalendar.model.Schedule;

import static study.easycalendar.App.getApplicationInstance;

public class DatabaseHandler {

    private static volatile DatabaseHandler instance;

    private ScheduleDao scheduleDao;

    private LiveData<List<Schedule>> schedules;

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            synchronized (DatabaseHandler.class) {
                if (instance == null) {
                    instance = new DatabaseHandler();
                }
            }
        }
        return instance;
    }

    public DatabaseHandler() {
        AppDatabase database = AppDatabase.getInstance(getApplicationInstance());
        scheduleDao = database.scheduleDao();
    }

    public LiveData<List<Schedule>> getSchedules() {
        return scheduleDao.getSchedules();
    }


    public List<Schedule> getSchedulesList() {
        return scheduleDao.getSchedulesList();
    }

    public List<Schedule> getDDayList() {
        return scheduleDao.getDDayList(LocalDate.now());
    }

    public List<Schedule> getCountingList() {
        return scheduleDao.getCountingList(LocalDate.now());
    }

    public void insertSchedule(Schedule schedule) {
        new InsertScheduleAsync(scheduleDao).execute(schedule);
    }

    public void insertScheduleList(List<Schedule> scheduleList) {
        new InsertScheduleListAsync(scheduleDao).execute(scheduleList);
    }

    public void updateSchedule(Schedule schedule) {
        new UpdateScheduleAsync(scheduleDao).execute(schedule);
    }

    public Integer deleteSchedule(Schedule schedule) {
        new DeleteScheduleAsync(scheduleDao).execute(schedule);
        return null;
    }

    private static class InsertScheduleAsync extends AsyncTask<Schedule, Void, Void> {

        private ScheduleDao scheduleDaoAsync;

        InsertScheduleAsync(ScheduleDao scheduleDao) {
            scheduleDaoAsync = scheduleDao;
        }

        @Override
        protected Void doInBackground(Schedule... schedules) {
            scheduleDaoAsync.insertSchedule(schedules[0]);
            return null;
        }
    }

    private static class InsertScheduleListAsync extends AsyncTask<List<Schedule>, Void, Void> {

        private ScheduleDao scheduleDaoAsync;

        InsertScheduleListAsync(ScheduleDao scheduleDao) {
            scheduleDaoAsync = scheduleDao;
        }

        @Override
        protected Void doInBackground(List<Schedule>... schedules) {
            scheduleDaoAsync.insertTestSchedules(schedules[0]);
            return null;
        }
    }

    private static class UpdateScheduleAsync extends AsyncTask<Schedule, Void, Void> {

        private ScheduleDao scheduleDaoAsync;

        UpdateScheduleAsync(ScheduleDao scheduleDao) {
            scheduleDaoAsync = scheduleDao;
        }

        @Override
        protected Void doInBackground(Schedule... schedules) {
            scheduleDaoAsync.updateSchedule(schedules[0]);
            return null;
        }
    }
    
    private static class DeleteScheduleAsync extends AsyncTask<Schedule, Void, Void> {
        
        private ScheduleDao scheduleDaoAsync;

        DeleteScheduleAsync(ScheduleDao scheduleDao) {
            scheduleDaoAsync = scheduleDao;
        }

        @Override
        protected Void doInBackground(Schedule... schedules) {
            scheduleDaoAsync.deleteSchedule(schedules[0]);
            return null;
        }
    }

}

