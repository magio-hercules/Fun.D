
package com.leesc.tazza.data;

import android.content.Context;

import com.google.gson.Gson;
import com.leesc.tazza.data.local.prefs.PreferencesHelper;


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
