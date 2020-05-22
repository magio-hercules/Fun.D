package com.fund.iam.data.model;


import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Letter {

    @SerializedName("user")
    private User user;

    @SerializedName("friend")
    private User friend;

    @SerializedName("message_owner")
    private int messageOwner;

    private int type;

    private String message;

    private String imageUrl;

    @SerializedName("user_token")
    private String userToken;

    public Letter(int type, String message) {
        this.type = type;
        this.message = message;
    }
}