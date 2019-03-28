package com.example.off;

public class Item {

    int rank;
    String userId;
    String win;
    String lose;

    public Item(int rank, String userId, String win, String lose) {
        this.rank = rank;
        this.userId = userId;
        this.win = win;
        this.lose = lose;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }

    @Override
    public String toString() {
        return "Item{" +
                "rank=" + rank +
                ", userId='" + userId + '\'' +
                ", win='" + win + '\'' +
                ", lose='" + lose + '\'' +
                '}';
    }
}
