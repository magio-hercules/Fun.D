/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.fundroid.offstand.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.fundroid.offstand.di.quailfier.PreferenceInfo;

import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper {

    public static final String PREF_KEY_USER_NAME = "userName";
    private static final String PREF_KEY_AVATAR = "character";
    private static final String PREF_KEY_TOTAL = "total";
    private static final String PREF_KEY_WIN = "win";
    private static final String PREF_KEY_ROOM_NAME = "roomName";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getUserName() {
        return mPrefs.getString(PREF_KEY_USER_NAME, null);
    }

    @Override
    public void setUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_USER_NAME, userName).apply();
    }

    @Override
    public int getUserAvatar() {
        return mPrefs.getInt(PREF_KEY_AVATAR, 1);
    }

    @Override
    public void setUserAvatar(int avatar) {
        mPrefs.edit().putInt(PREF_KEY_AVATAR, avatar).apply();
    }

    @Override
    public int getUserWin() {
        return mPrefs.getInt(PREF_KEY_WIN, 0);
    }

    @Override
    public void setUserWin(int win) {
        mPrefs.edit().putInt(PREF_KEY_WIN, win).apply();
    }

    @Override
    public int getUserTotal() {
        return mPrefs.getInt(PREF_KEY_TOTAL, 0);
    }

    @Override
    public void setUserTotal(int total) {
        mPrefs.edit().putInt(PREF_KEY_TOTAL, total).apply();
    }

    @Override
    public String getRoomName() {
        return mPrefs.getString(PREF_KEY_ROOM_NAME, null);
    }

    @Override
    public void setRoomName(String roomName) {
        mPrefs.edit().putString(PREF_KEY_ROOM_NAME, roomName).apply();
    }
}
