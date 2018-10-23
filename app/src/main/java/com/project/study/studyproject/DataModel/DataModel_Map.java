package com.project.study.studyproject.DataModel;

public class DataModel_Map {
    private int id_marker;
    private String name;
    private String lat;
    private String lng;
    private String date;

    public DataModel_Map() {
    }

    public DataModel_Map(String name, String lat, String lng, String date) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    public int getIdMarker() {
        return id_marker;
    }

    public void setIdMarker(int id_marker) {
        this.id_marker = id_marker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
