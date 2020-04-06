package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

public class Channel {

    public int id;

    public String name;

    public String description;

    @SerializedName("purppose")
    public String purpose;

    public String location;

    public String password;

    @SerializedName("max_user")
    public int maxUser;

    @SerializedName("owner_id")
    public int ownerId;

}
