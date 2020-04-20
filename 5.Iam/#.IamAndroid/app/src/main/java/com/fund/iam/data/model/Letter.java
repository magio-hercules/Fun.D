package com.fund.iam.data.model;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Letter {

    @SerializedName("friend_id")
    private int id;

    private int type;

    private String message;

    private String imageUrl;

    public Letter(int type, String message) {
        this.type = type;
        this.message = message;
    }
}