package com.fund.iam.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LoginBody {

    private String email;

    @SerializedName("user_name")
    private String userName;

    private String token;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("sns_type")
    private String snsType;

}