
package com.fundroid.offstand.data;

import android.content.Context;

import com.fundroid.offstand.data.local.prefs.PreferencesHelper;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppDataManager implements DataManager {

    private final Context mContext;

//    private final DbHelper mDbHelper;

    private final Gson mGson;

    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public AppDataManager(Context context, PreferencesHelper preferencesHelper, Gson gson) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mGson = gson;
    }

}
