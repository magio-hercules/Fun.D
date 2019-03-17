package com.leesc.tazza.data.model;

public class Attendee {

    public enum EnumStatus {

        STANDBY("STANDBY"), READY("READY");

        private String enumStatus;

        EnumStatus(String enumStatus) {
            this.enumStatus = enumStatus;
        }
    }

    private int id;

    private String name;

    private EnumStatus status;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }
}
