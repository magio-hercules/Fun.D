package study.easycalendar.model;

public class Dday {

    private int id;
    private String title;
    private String day;
    private int color;
    private String uri;

    public Dday(int id, String title, String day, int color, String uri) {

        this.id = id;
        this.title = title;
        this.day   = day;
        this.color = color;
        this.uri   = uri;
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
