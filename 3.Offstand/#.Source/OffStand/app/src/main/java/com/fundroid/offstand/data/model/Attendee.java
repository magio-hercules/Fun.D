package com.fundroid.offstand.data.model;

import com.google.gson.Gson;

import androidx.annotation.NonNull;

public class Attendee {

    public enum EnumStatus {

        STANDBY("STANDBY"), READY("READY"), INGAME("INGAME");

        private String enumStatus;

        EnumStatus(String enumStatus) {
            this.enumStatus = enumStatus;
        }
    }

    public enum EnumAvatar {
        JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8);

        private int index;

        EnumAvatar(int index) {
            this.index = index;
        }
    }

    private Integer id;

    private String name;

    private EnumStatus status;

    private EnumAvatar avatar;

    private Integer seatNo;

    private Integer win;

    private Integer lose;

    private Double winningRate;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(Integer seatNo) {
        this.seatNo = seatNo;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    public EnumAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(EnumAvatar avatar) {
        this.avatar = avatar;
    }

    public int getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(Integer lose) {
        this.lose = lose;
    }

    public double getWinningRate() {
        return winningRate;
    }

    public void setWinningRate(double winningRate) {
        this.winningRate = winningRate;
    }

    public Attendee(String name, EnumAvatar avatar, Integer win, Integer lose) {
        this.name = name;
        this.avatar = avatar;
        this.win = win;
        this.lose = lose;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
