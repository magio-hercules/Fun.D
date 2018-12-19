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

    public LocalDate dDayDate;

    public Schedule(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String title, String memo, String category, String notification, String repeat, boolean isDday, LocalDate dDayDate) {
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
        this.dDayDate = dDayDate;
    }

    public int getId() {
        return id;
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public boolean isDday() {
        return isDday;
    }

    public void setDday(boolean dday) {
        isDday = dday;
    }

    public LocalDate getdDayDate() {
        return dDayDate;
    }

    public void setdDayDate(LocalDate dDayDate) {
        this.dDayDate = dDayDate;
    }
}
