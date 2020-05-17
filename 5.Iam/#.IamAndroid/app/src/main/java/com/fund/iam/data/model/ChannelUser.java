package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

public class ChannelUser {

    private int id;

    @SerializedName("channel_id")
    private int channelId;

    @SerializedName("user_id")
    private int userId;

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

    public int gender;

    public String token;

    public ChannelUser(int id, int channelId, int userId, String email, String userName, String nickName, String phone, String jobList, String locationList, String imageUrl, String snsType, int age, int gender, String token) {
        this.id = id;
        this.channelId = channelId;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.nickName = nickName;
        this.phone = phone;
        this.jobList = jobList;
        this.locationList = locationList;
        this.imageUrl = imageUrl;
        this.snsType = snsType;
        this.age = age;
        this.gender = gender;
        this.token = token;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJobList() {
        return jobList;
    }

    public void setJobList(String jobList) {
        this.jobList = jobList;
    }

    public String getLocationList() {
        return locationList;
    }

    public void setLocationList(String locationList) {
        this.locationList = locationList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSnsType() {
        return snsType;
    }

    public void setSnsType(String snsType) {
        this.snsType = snsType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
