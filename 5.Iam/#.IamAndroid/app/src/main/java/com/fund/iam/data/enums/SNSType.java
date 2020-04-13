package com.fund.iam.data.enums;

public enum SNSType {

    GOOGLE("GOOGLE"), FACEBOOK("FACEBOOK"), KAKAO("KAKAO");

    private String snsType;

    SNSType(String snsType) {
        this.snsType = snsType;
    }

    public String getSnsType() {
        return snsType;
    }
}
