package study.easycalendar.model;

public class Dday {

    private String title;
    private String day;

    public Dday(String title, String day) {

        this.title = title;
        this.day   = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
