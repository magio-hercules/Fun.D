package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Portfolio {

    private int id;

    @SerializedName("user_id")
    private int userId;

    private int type;

    private String text;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("create_date")
    private String createDate;

    @SerializedName("modify_date")
    private String modifyDate;

    public Portfolio(int userId, int type, String text, String imageUrl) {
        new Portfolio(-1, userId, type, text, imageUrl, "", "");
    }
}