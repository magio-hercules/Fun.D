package com.example.eom.offstand.model;

public class User {


    // 방장
    private boolean boss;

    private int status; // 0(Standby), 1(ready), 2(game)
    private int seat; // 1~10
    private int avatar; // 1~10
    private String name;

    // 전적
    private int win;
    private int lose;
    private int rate;

    // card
    private int card1;
    private int card2;

    public User() {

    }

    public User(boolean boss, int seat, int avatar, String name) {
        boss = boss;
        status = 0;
        seat = seat;
        avatar = avatar;
        name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
