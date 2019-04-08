package com.leesc.tazza.data.model;

public class Attendee extends JsonBody {

    public enum EnumStatus {

        STANDBY("STANDBY"), READY("READY"), INGAME("INGAME");

        private String enumStatus;

        EnumStatus(String enumStatus) {
            this.enumStatus = enumStatus;
        }
    }

    public enum EnumCharacter {
        JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8);

        private int index;

        EnumCharacter(int index) {
            this.index = index;
        }
    }

    private int id;

    private String name;

    private EnumStatus status;

    private int win;

    private int lose;

    private double winningRate;

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

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
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

    public double getWinningRate() {
        return winningRate;
    }

    public void setWinningRate(double winningRate) {
        this.winningRate = winningRate;
    }

}
