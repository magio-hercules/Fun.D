package com.fundroid.offstand.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

import static com.fundroid.offstand.data.remote.ApiDefine.API_MOVE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_MOVE_BR;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE_BR;

public class ApiBody {

    private Integer no;

    @Expose
    private User user;

    @Expose
    private ArrayList<User> users;

    @Expose
    private Integer seatNo;

    @Expose
    private Integer seatNo2;

    @Expose
    private Integer[] seatNos;

    @Expose
    private Integer cardNo1;

    @Expose
    private Integer cardNo2;

    @Expose
    private Boolean draw;

    public int getNo() {
        return no;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ApiBody(Integer no) {
        this.no = no;
    }

    public ApiBody(Integer no, User user) {
        this.no = no;
        this.user = user;
    }

    public ApiBody(Integer no, ArrayList<User> users) {
        this.no = no;
        this.users = users;
    }

    public ApiBody(Integer no, ArrayList<User> users, boolean draw) {
        this.no = no;
        this.users = users;
        this.draw = draw;
    }

    public ApiBody(Integer no, Integer seatNo) {
        this.no = no;
        this.seatNo = seatNo;
    }

    public ApiBody(Integer no, Integer[] seatNos) {
        this.no = no;
        this.seatNos = seatNos;
    }

    public ApiBody(Integer no, Integer seatNo1OrCardNo1, Integer seatNo2OrCardNo2) {
        this.no = no;

        if (no == API_MOVE || no == API_MOVE_BR) {
            seatNo = seatNo1OrCardNo1;
            seatNo2 = seatNo2OrCardNo2;
        } else if (no == API_SHUFFLE_BR) {
            cardNo1 = seatNo1OrCardNo1;
            cardNo2 = seatNo2OrCardNo2;
        } else {
            //error
        }
    }

    public Integer getSeatNo() {
        return seatNo;
    }

    public Integer getSeatNo2() {
        return seatNo2;
    }

    public Integer getCardNo1() {
        return cardNo1;
    }

    public Integer getCardNo2() {
        return cardNo2;
    }

    public Integer[] getSeatNos() {
        return seatNos;
    }

    public Boolean getDraw() { return draw; }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
