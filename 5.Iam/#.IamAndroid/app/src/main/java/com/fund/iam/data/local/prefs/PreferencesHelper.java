package com.fund.iam.data.local.prefs;

public interface PreferencesHelper {

    void setPushToken(String value);

    String getPushToken();

    void setAuthEmail(String value);

    String getAuthEmail();

    void setAuthSnsType(String value);

    String getAuthSnsType();

}
