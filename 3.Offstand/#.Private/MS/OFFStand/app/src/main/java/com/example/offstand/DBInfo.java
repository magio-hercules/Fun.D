package com.example.offstand;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 * <p>
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "DBInfo_table")
public class DBInfo {

    //    @PrimaryKey
//    @NonNull

    @PrimaryKey
    @ColumnInfo(name = "rank")
    private Integer mRank;

    @ColumnInfo(name = "user_image")
    private Integer mUserImage;

    @ColumnInfo(name = "user_id")
    private String mUserId;

    @ColumnInfo(name = "win")
    private String mWin;

    @ColumnInfo(name = "lose")
    private String mLose;

    public DBInfo(@NonNull int rank, int userImage, String userId, String win, String lose) {
        this.mRank = rank;
        this.mUserImage = userImage;
        this.mUserId = userId;
        this.mWin = win;
        this.mLose = lose;
    }

    public Integer getRank() {
        return this.mRank;
    }

    public Integer getUserImage() {
        return this.mUserImage;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public String getWin() {
        return this.mWin;
    }

    public String getLose() {
        return this.mLose;
    }

}