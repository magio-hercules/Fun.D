package com.fundroid.offstand.data.model;

import android.util.Log;

import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

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

    private Pair<Integer, Integer> cards;

    private int card1;

    private int card2;

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

    public int getCard1() {
        return card1;
    }

    public void setCard1(int card1) {
        this.card1 = card1;
    }

    public int getCard2() {
        return card2;
    }

    public void setCard2(int card2) {
        this.card2 = card2;
    }

    public Pair<Integer, Integer> getCards() {
        return cards;
    }

    public void setCards(Pair<Integer, Integer> cards) {
        this.cards = cards;
    }

    public Attendee(String name, int avatar, Integer win, Integer lose) {
        this.name = name;
        this.avatar = avatar;
        this.win = win;
        this.lose = lose;
    }

    @Override
    public String toString() {
        Log.d("lsc","Attendee toString");
        return new Gson().toJson(this);
    }
}
