package com.example.bookmark.model;

public class BookmarkUserItem {
    private String userProfileUrl;
    private String userName;
    private String userJob;
    private String userEmail;
    private int receivedMailCount;

    public BookmarkUserItem(String userProfileUrl, String userName, String userJob, String userEmail, int receivedMailCount) {
        this.userProfileUrl = userProfileUrl;
        this.userName = userName;
        this.userJob = userJob;
        this.userEmail = userEmail;
        this.receivedMailCount = receivedMailCount;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserJob() {
        return userJob;
    }

    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getReceivedMailCount() {
        return receivedMailCount;
    }

    public void setReceivedMailCount(int receivedMailCount) {
        this.receivedMailCount = receivedMailCount;
    }
}
