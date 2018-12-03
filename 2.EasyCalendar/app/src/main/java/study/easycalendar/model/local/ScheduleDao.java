package study.easycalendar.model.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.threeten.bp.LocalDate;

import java.util.List;

import study.easycalendar.model.Schedule;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM Schedule")
    LiveData<List<Schedule>> getSchedules();

    @Query("SELECT * FROM Schedule")
    List<Schedule> getSchedulesList();

    @Query("SELECT * FROM Schedule WHERE date =:localDate")
    LiveData<List<Schedule>> getSchedulesOnSelectedDate(LocalDate localDate);

    @Query("SELECT * FROM Schedule WHERE date =:localDate")
    List<Schedule> getSchedulesOnSelectedDateList(LocalDate localDate);

    @Insert
    void insertSchedule(Schedule schedule);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTestSchedules(List<Schedule> schedules);

}
