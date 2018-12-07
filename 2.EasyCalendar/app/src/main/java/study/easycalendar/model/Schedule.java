package study.easycalendar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

@Entity
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public LocalDate startDate;

    public LocalTime startTime;

    public LocalDate endDate;

    public LocalTime endTime;

    public String title;

    public String memo;

    public String category;

    public String notification;

    public String repeat;

    public boolean isDday;

    @Ignore
    public Schedule(LocalDate startDate, LocalTime startTime, String title, String memo) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.title = title;
        this.memo = memo;
    }

    public Schedule(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String title, String memo, String category, String notification, String repeat, boolean isDday) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.title = title;
        this.memo = memo;
        this.category = category;
        this.notification = notification;
        this.repeat = repeat;
        this.isDday = isDday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public boolean isDday() {
        return isDday;
    }

    public void setDday(boolean dday) {
        isDday = dday;
    }
}
