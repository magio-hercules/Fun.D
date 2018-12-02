package study.easycalendar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

@Entity
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public LocalDate localDate;

    public LocalTime localTime;

    public String title;

    public String memo;

    public Schedule(LocalDate localDate, LocalTime localTime, String title, String memo) {
        this.localDate = localDate;
        this.localTime = localTime;
        this.title = title;
        this.memo = memo;
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

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }
}
