package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

public class ChannelUser {

    public int id;

    @SerializedName("channel_id")
    public int channelId;

    @SerializedName("user_id")
    public int userId;

}
