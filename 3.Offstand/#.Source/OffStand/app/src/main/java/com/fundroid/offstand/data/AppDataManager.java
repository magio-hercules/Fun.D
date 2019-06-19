
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

    @Override
    public String getUserName() {
        return mPreferencesHelper.getUserName();
    }

    @Override
    public void setUserName(String userName) {
        mPreferencesHelper.setUserName(userName);
    }

    @Override
    public int getUserAvatar() {
        return mPreferencesHelper.getUserAvatar();
    }

    @Override
    public void setUserAvatar(int avatar) {
        mPreferencesHelper.setUserAvatar(avatar);
    }

    @Override
    public int getUserWin() {
        return mPreferencesHelper.getUserWin();
    }

    @Override
    public void setUserWin(int win) {
        mPreferencesHelper.setUserWin(win);
    }

    @Override
    public int getUserTotal() {
        return mPreferencesHelper.getUserTotal();
    }

    @Override
    public void setUserTotal(int total) {
        mPreferencesHelper.setUserTotal(total);
    }

    @Override
    public String getRoomName() {
        return mPreferencesHelper.getRoomName();
    }

    @Override
    public void setRoomName(String roomName) {
        mPreferencesHelper.setRoomName(roomName);
    }
}
