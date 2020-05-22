package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LetterBox {

    @SerializedName("friend_id")
    private int friendId;

    @SerializedName("user_name")
    private String name;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("job_list")
    private String job;

    private String email;

    private String token;

    private String jobTitle;

    private String jobColor;

    private User user;

    private boolean badge;

    public LetterBox(User user) {
        this.user = user;
    }

    public LetterBox(int friendId, String name, String imageUrl, String token) {
        this.friendId = friendId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.token = token;
    }
}