package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

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
    private String locationList;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("sns_type")
    private String snsType;

    private int age;

    private int gender;

    private String token;

    public User(int id, String snsType, String imageUrl, String userName, String nickName,
                String email, String phone, String location, String job, int gender, int age) {
        this.id = id;
        this.snsType = snsType;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.phone = phone;
        this.locationList = location;
        this.jobList = job;
        this.gender = gender;
        this.age = age;
    }

    public User(String email, String userName, String token, String imageUrl, String snsType) {
        this.email = email;
        this.userName = userName;
        this.token = token;
        this.imageUrl = imageUrl;
        this.snsType = snsType;
    }

    public User(String email, String token, String snsType) {
        this.email = email;
        this.token = token;
        this.snsType = snsType;
    }

    public User(int id, String userName, String token) {
        this.id = id;
        this.userName = userName;
        this.token = token;
    }


    public User(int userId, String userName, String imageUrl, String token) {
        this.id = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.token = token;
    }


    // contact us
    public User(String userName, String imageUrl, String email, String jobList) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.email = email;
        this.jobList = jobList;
    }
}