package study.easycalendar.model;

public class Dday {

    private String title;
    private String day;
    private int color;
    private String uri;

    public Dday(String title, String day, int color, String uri) {

        this.title = title;
        this.day   = day;
        this.color = color;
        this.uri   = uri;
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


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
