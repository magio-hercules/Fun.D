package com.example.eom.offstand.model;

public class User {


    // 방장
    private boolean boss;

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
    private int level; // 족보 등급을 위한
    private int level_score;


    public User() {
        new User(0, false, 0, 0, "");
    }


    public User(int id, boolean boss, int seat, int avatar, String name) {
        this.id = id;
        this.boss = boss;
        this.status = 0;
        this.seat = seat;
        this.avatar = avatar;
        this.name = name;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
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
}
