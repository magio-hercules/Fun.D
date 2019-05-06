package com.fundroid.offstand.data.model;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import static com.fundroid.offstand.data.remote.ApiDefine.API_MOVE;
import static com.fundroid.offstand.data.remote.ApiDefine.API_SHUFFLE;

public class ApiBody {

    private Integer no;

    @Expose
    private Attendee attendee;

    @Expose
    private ArrayList<Attendee> attendees;

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
    private Boolean result;

    public int getNo() {
        return no;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    public ApiBody(Integer no, Attendee attendee) {
        this.no = no;
        this.attendee = attendee;
    }

    public ApiBody(Integer no, ArrayList<Attendee> attendees) {
        this.no = no;
        this.attendees = attendees;
    }

    public ApiBody(Integer no, Integer seatNo) {
        this.no = no;
        this.seatNo = seatNo;
    }

    public ApiBody(Integer no, Integer seatNo1OrCardNo1, Integer seatNo2OrCardNo2) {
        this.no = no;

        if (no == API_MOVE) {
            seatNo = seatNo1OrCardNo1;
            seatNo2 = seatNo2OrCardNo2;
        } else if (no == API_SHUFFLE) {
            cardNo1 = seatNo1OrCardNo1;
            cardNo2 = seatNo2OrCardNo2;
        } else {
            //error
        }

        this.seatNo = seatNo;

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
