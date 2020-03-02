package com.fund.iam;

import android.content.Context;

public class PrefAuth {

    public static String getFcmToken(Context context) {
        return SharedPreferenceUtils.GET_STRING(context, R.string.key_fcm_token);
    }

    public static void setFcmToken(Context context, String value) {
        SharedPreferenceUtils.PUT_STRING(context, R.string.key_fcm_token, value);
    }

}
