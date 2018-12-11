package study.easycalendar.model.local;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

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

    private DatabaseHandler() {
        AppDatabase database = AppDatabase.getInstance(getApplicationInstance());
        scheduleDao = database.scheduleDao();
    }

    public LiveData<List<Schedule>> getSchedules() {
        return scheduleDao.getSchedules();
    }


    public List<Schedule> getSchedulesList() {
        return scheduleDao.getSchedulesList();
    }

    public void insertSchedule(Schedule schedule) {
        new InsertScheduleAsync(scheduleDao).execute(schedule);
    }

    public void insertScheduleList(List<Schedule> scheduleList) {
        new InsertScheduleListAsync(scheduleDao).execute(scheduleList);
    }

    private static class InsertScheduleAsync extends AsyncTask<Schedule, Void, Void> {

        private ScheduleDao managerDaoAsync;

        InsertScheduleAsync(ScheduleDao managerDao) {
            managerDaoAsync = managerDao;
        }

        @Override
        protected Void doInBackground(Schedule... managerResponses) {
            managerDaoAsync.insertSchedule(managerResponses[0]);
            return null;
        }
    }

    private static class InsertScheduleListAsync extends AsyncTask<List<Schedule>, Void, Void> {

        private ScheduleDao managerDaoAsync;

        InsertScheduleListAsync(ScheduleDao managerDao) {
            managerDaoAsync = managerDao;
        }

        @Override
        protected Void doInBackground(List<Schedule>... managerResponses) {
            managerDaoAsync.insertTestSchedules(managerResponses[0]);
            return null;
        }
    }

}

