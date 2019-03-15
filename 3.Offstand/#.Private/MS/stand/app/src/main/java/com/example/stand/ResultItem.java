package com.example.stand;

public class ResultItem {

    int rank;
    String userId;
    String win;
    String lose;

    public ResultItem(int rank, String userId, String win, String lose) {
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
        return "ResultItem{" +
                "rank=" + rank +
                ", userId='" + userId + '\'' +
                ", win='" + win + '\'' +
                ", lose='" + lose + '\'' +
                '}';
    }
}
