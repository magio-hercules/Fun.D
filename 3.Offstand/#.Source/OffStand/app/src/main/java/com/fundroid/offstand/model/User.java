package com.fundroid.offstand.model;

import androidx.core.util.Pair;

public class User {

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
    private int status; // 0(Standby), 1(ready), 2(game), 3(result)
    private Integer seat; // 1~10
    private int avatar; // 1~10
    private String name;

    // 전적
    private int win;
    private int lose;
    private int draw;
    private int rate;

    // card
    private Pair<Integer, Integer> cards;

    // 족보
    private int level;
    private int level_score;


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

    public User(boolean host, String name, int avatar, int win, int lose) {
        this.host = host;
        this.name = name;
        this.avatar = avatar;
        this.win = win;
        this.lose = lose;
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

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel_score() {
        return level_score;
    }

    public void setLevel_score(int level_score) {
        this.level_score = level_score;
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

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Pair<Integer, Integer> getCards() {
        return cards;
    }

    public void setCards(Pair<Integer, Integer> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "User{" +
                "host=" + host +
                ", id=" + id +
                ", seat=" + seat +
                ", avatar=" + avatar +
                ", name='" + name + '\'' +
                '}';
    }
}


