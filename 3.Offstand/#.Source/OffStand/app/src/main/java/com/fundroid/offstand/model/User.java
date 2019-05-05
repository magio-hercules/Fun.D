package com.fundroid.offstand.model;

public class User {

    public enum EnumStatus {

        STANDBY(0), READY(1), INGAME(2), RESULT(3);

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
    private int seat; // 1~10
    private int avatar; // 1~10
    private String name;

    // 전적
    private int win;
    private int lose;
    private int draw;
    private int rate;

    // card
    private int card1;
    private int card2;

    // 족보
    private int level;
    private int level_score;


    public User() {
        new User(-1, false, 0, 0, "");
    }


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

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
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


