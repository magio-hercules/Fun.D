package com.fund.iam.data.model;


import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Notification {

    private String title = "메시지가 도착하였습니다.";
    private String body;

    @SerializedName("click_action")
    private String clickAction = "OPEN_ACTIVITY";

}