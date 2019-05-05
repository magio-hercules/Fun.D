package com.fundroid.offstand.data.model;

import com.google.gson.Gson;

import androidx.annotation.NonNull;

public class Attendee {

    public enum EnumStatus {

        STANDBY(0), READY(1), INGAME(2), CARDOPEN(3), RESULT(4);

        private int enumStatus;

        EnumStatus(int enumStatus) {
            this.enumStatus = enumStatus;
        }

        public int getEnumStatus() {
            return enumStatus;
        }
    }

    public enum EnumAvatar {
        JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SEP(9), OCT(10);

        private int index;

        EnumAvatar(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private Integer id;

    private String name;

    private int status;

    private int avatar;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
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

    public Attendee(String name, int avatar, Integer win, Integer lose) {
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
