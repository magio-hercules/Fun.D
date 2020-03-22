package com.fund.iam.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.fund.iam.R;
import com.fund.iam.di.qualifier.PreferenceInfo;

import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper {

    private final SharedPreferences mPrefs;
    private final Context context;


    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        this.context = context;
    }

    @Override
    public void setPushToken(String value) {
        mPrefs.edit().putString(context.getString(R.string.key_push_token), value).apply();
    }

    @Override
    public String getPushToken() {
        return mPrefs.getString(context.getString(R.string.key_push_token), null);
    }
}
