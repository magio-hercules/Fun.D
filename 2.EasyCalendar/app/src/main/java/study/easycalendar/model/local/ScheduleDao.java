package study.easycalendar.model.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.threeten.bp.LocalDate;

import java.util.List;

import study.easycalendar.model.Schedule;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM Schedule")
    LiveData<List<Schedule>> getSchedules();

    @Query("SELECT * FROM Schedule WHERE id = :id")
    Schedule getSchedule(int id);

    @Query("SELECT * FROM Schedule")
    List<Schedule> getSchedulesList();

    @Query("SELECT * FROM Schedule WHERE startDate =:localDate")
    LiveData<List<Schedule>> getSchedulesOnSelectedDate(LocalDate localDate);

    @Query("SELECT * FROM Schedule WHERE startDate =:localDate")
    List<Schedule> getSchedulesOnSelectedDateList(LocalDate localDate);

    @Query("SELECT * FROM Schedule WHERE isDday = 1 AND dDayDate > :localDate")
    List<Schedule> getDDayList(LocalDate localDate);

    @Query("SELECT * FROM Schedule WHERE isDday = 1 AND dDayDate < :localDate")
    List<Schedule> getCountingList(LocalDate localDate);

    @Insert
    long insertSchedule(Schedule schedule);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTestSchedules(List<Schedule> schedules);

    @Update
    void updateSchedule(Schedule schedule);

    @Delete
    void deleteSchedule(Schedule schedule);

}
