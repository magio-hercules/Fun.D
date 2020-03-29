package com.fund.iam.data.enums;

public enum LetterType {

    LOCAL(0), REMOTE(1);

    private int letterType;

    LetterType(int letterType) {
        this.letterType = letterType;
    }

    public int getLetterType() {
        return letterType;
    }
}
