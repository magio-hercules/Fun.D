
package study.easycalendar.model.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import study.easycalendar.model.Schedule;

@Database(entities = Schedule.class, version = 1)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public abstract ScheduleDao scheduleDao();

    private final static List<Schedule> TEST_SCHEDULES = Arrays.asList(
            new Schedule(LocalDate.of(2018, 12, 1), LocalTime.now(), "12월 1일", "메모입니다."),
            new Schedule(LocalDate.of(2018, 12, 1), LocalTime.now(), "12월 1일", "두번째 메모."),
            new Schedule(LocalDate.of(2018, 12, 3), LocalTime.now(), "12월 3일", "가나다라마바사."),
            new Schedule(LocalDate.of(2018, 12, 3), LocalTime.now(), "12월 3일", "아자차카타파하."),
            new Schedule(LocalDate.of(2018, 12, 5), LocalTime.now(), "12월 5일", "ABCDEFG"),
            new Schedule(LocalDate.of(2018, 12, 5), LocalTime.now(), "12월 5일", "HIJKLMN"),
            new Schedule(LocalDate.of(2018, 12, 5), LocalTime.now(), "12월 5일", "OPQRSTU"),
            new Schedule(LocalDate.of(2018, 12, 5), LocalTime.now(), "12월 5일", "VWXYZ"),
            new Schedule(LocalDate.of(2018, 12, 7), LocalTime.now(), "12월 7일", "아야어여오요."),
            new Schedule(LocalDate.of(2018, 12, 7), LocalTime.now(), "12월 7일", "으이.")
    );

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "AppDatabase")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(
                                            () -> getInstance(context).scheduleDao().insertTestSchedules(TEST_SCHEDULES)
                                    );
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }

}

