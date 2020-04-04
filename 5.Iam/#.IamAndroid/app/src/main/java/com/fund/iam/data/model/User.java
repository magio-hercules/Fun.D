package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User {

    private int id;

    private String email;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("nick_name")
    private String nickName;

    private String phone;

    @SerializedName("job_list")
    private String jobList;

    private int age;

    private int gender;

    @SerializedName("firebase_token")
    private String firebaseToken;
}