package com.fundroid.offstand.data.model;

import android.util.Log;

import androidx.core.util.Pair;

import com.google.gson.annotations.Expose;

public class User implements Comparable<User> {

    public enum EnumStatus {

        STANDBY(0), READY(1), INGAME(2), CARDOPEN(3), DIE(4), RESULT(5);

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

    // 방장
    private boolean host;

    private int id;

    @Expose
    private int status; // 0(Standby), 1(ready), 2(game), 3(result)

    @Expose
    private Integer seat; // 1~10

    private int avatar; // 1~10

    private String name;

    // 전적
    private int total;
    private int win;

    // card
    @Expose
    private Pair<Integer, Integer> cards;

    // 족보
    @Expose
    private int cardLevel;

    @Expose
    private Integer cardSum = 0;   // 남은 유저가 카드 오픈을 하지 않아도 AUTO_RESULT를 받을 수 있도록

    public User() {
        new User(-1, false, 0, 0, "");
    }

    //deprecated
    public User(int id, boolean host, int seat, int avatar, String name) {
        this.id = id;
        this.host = host;

        // TODO : 테스트 코드,
        if (host) {
            this.status = 1;
        } else {
            this.status = 0;
        }
        this.seat = seat;
        this.avatar = avatar;
        this.name = name;
    }

    public User(int seat, boolean host, String name, int avatar, int total, int win) {
        this.seat = seat;
        this.host = host;
        if (host) {
            this.status = 1;
        } else {
            this.status = 0;
        }
        this.name = name;
        this.avatar = avatar;
        this.total = total;
        this.win = win;
    }

    public void doBan() {
        this.id = -1;
        this.host = false;
        this.status = 0;
//        this.seat = 0; // seat는 유지
        this.avatar = 0;
        this.name = "";
    }

    public void doReady(boolean bFlag) {
        if (bFlag) {
            this.status = 1;
        } else {
            this.status = 0;
        }
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public int getId() {
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

    public int getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getCardSum() {
        return cardSum;
    }

    public void setCardSum(Integer cardSum) {
        this.cardSum = cardSum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
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

    public void setWin(int win) {
        this.win = win;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Pair<Integer, Integer> getCards() {
        return cards;
    }

    public void setCards(Pair<Integer, Integer> cards) {
//        Log.d("lsc","User setCards cards " + cards.first + ", " + cards.second);
        this.cards = cards;
    }

    @Override
    public int compareTo(User user) {
        Log.d("lsc","User compareTo cardSum " + cardSum);
        Log.d("lsc","User compareTo user " + (user == null));
        Log.d("lsc","User compareTo user.getCardSum() " + user.getCardSum());
        return cardSum.compareTo(user.getCardSum());
    }

    @Override
    public String toString() {
        return "User{" +
                "host=" + host +
                ", status=" + status +
                ", seat=" + seat +
                ", avatar=" + avatar +
                ", name='" + name + '\'' +
                ", cards=" + cards +
                ", cardLevel=" + cardLevel +
                ", cardSum=" + cardSum +
                '}';
    }
}


