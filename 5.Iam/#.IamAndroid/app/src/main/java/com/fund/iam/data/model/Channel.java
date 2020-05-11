package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

public class Channel {

    public int id;

    public String name;

    public String description;

    public String purpose;

    public String location;

    public String password;

    @SerializedName("now_user")
    public int nowUser;

    @SerializedName("max_user")
    public int maxUser;

    @SerializedName("owner_id")
    public int ownerId;

    @SerializedName("create_date")
    public String createDate;

}
