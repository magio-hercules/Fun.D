package com.fund.iam;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.StringRes;

public class SharedPreferenceUtils {

    public static boolean isExistSharedPreference(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void PUT_INT(Context c, @StringRes int key, int value) {
        SharedPreferences sharedPreferences = getSharedPreference(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(c.getString(key), value);
        editor.apply();
    }

    public static void PUT_LONG(Context c, @StringRes int key, long value) {
        SharedPreferences sharedPreferences = getSharedPreference(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(c.getString(key), value);
        editor.apply();
    }

    public static void PUT_STRING(Context c, @StringRes int key, String value) {
        SharedPreferences sharedPreferences = getSharedPreference(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(c.getString(key), value);
        editor.apply();
    }

    public static void PUT_BOOLEAN(Context c, @StringRes int key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreference(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(c.getString(key), value);
        editor.apply();
    }

    public static void PUT_FLOAT(Context c, @StringRes int key, float value) {
        SharedPreferences sharedPreferences = getSharedPreference(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(c.getString(key), value);
        editor.apply();
    }

    public static final long GET_LONG(Context context, @StringRes int key) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getLong(context.getString(key), -1);
    }

    public static final long GET_LONG(Context context, @StringRes int key, long defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getLong(context.getString(key), defaultValue);
    }

    public static final int GET_INT(Context context, @StringRes int key) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getInt(context.getString(key), -1);
    }

    public static final int GET_INT(Context context, @StringRes int key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getInt(context.getString(key), defaultValue);
    }

    public static final String GET_STRING(Context context, @StringRes int key) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getString(context.getString(key), null);
    }

    public static final String GET_STRING(Context context, @StringRes int key, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getString(context.getString(key), defaultValue);
    }

    public static final float GET_FLOAT(Context context, @StringRes int key) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getFloat(context.getString(key), -1);
    }

    public static final float GET_FLOAT(Context context, @StringRes int key, float defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getFloat(context.getString(key), defaultValue);
    }

    public static final boolean GET_BOOLEAN(Context context, @StringRes int key) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getBoolean(context.getString(key), false);
    }

    public static final boolean GET_BOOLEAN(Context context, @StringRes int key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getBoolean(context.getString(key), defaultValue);
    }

    public static final void REMOVE_KEY(Context context, @StringRes int key) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if(sharedPreferences.contains(context.getString(key)))
        sharedPreferences.edit().remove(context.getString(key)).apply();
    }
}
