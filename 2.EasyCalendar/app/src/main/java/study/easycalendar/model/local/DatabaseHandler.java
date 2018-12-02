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


    public void insertSchedule(Schedule schedule) {
        new InsertScheduleAsync(scheduleDao).execute(schedule);
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

}

