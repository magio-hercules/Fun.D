package com.project.study.studyproject;

public class Dictionary {

    private int key;
    private String contents;
    private String dates;

    public Dictionary() {
    }

    public Dictionary(String contents, String dates) {
        this.contents = contents;
        this.dates = dates;
    }

    public Dictionary(int key, String contents, String dates) {
        this.key = key;
        this.contents = contents;
        this.dates = dates;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return dates;
    }

    public void setDate(String date) {
        this.dates = date;
    }
}


