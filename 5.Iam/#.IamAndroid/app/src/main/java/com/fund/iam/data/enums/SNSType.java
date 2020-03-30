package com.fund.iam.data.enums;

public enum SNSType {

    GOOGLE(0), FACEBOOK(1), KAKAO(2);

    private int snsType;

    SNSType(int snsType) {
        this.snsType = snsType;
    }

    public int getSnsType() {
        return snsType;
    }
}
