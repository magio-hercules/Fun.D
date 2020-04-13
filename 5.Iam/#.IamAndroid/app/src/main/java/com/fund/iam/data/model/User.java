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

    @SerializedName("location_list")
    private int locationList;

    @SerializedName("portfolio_list")
    private int portfolioList;

    @SerializedName("image_url")
    private String imageUrl;

    private int age;

    private int gender;

    private String token;
}