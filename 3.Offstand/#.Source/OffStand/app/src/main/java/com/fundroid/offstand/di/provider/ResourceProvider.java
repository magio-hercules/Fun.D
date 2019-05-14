package com.fundroid.offstand.di.provider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import androidx.annotation.ArrayRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import com.fundroid.offstand.R;

public class ResourceProvider {
    private final Context context;
    public ResourceProvider(Context context) {
        this.context = context;
    }

    public Drawable getDrawable(@DrawableRes int resId) {
        return context.getDrawable(resId);
    }

    public String[] getArray(@ArrayRes int resId) {
        return context.getResources().getStringArray(resId);
    }

    public String getString(@StringRes int resId) {
        return context.getString(resId);
    }

    public String getString(@StringRes int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }

    public void testClick() {
//        MediaPlayer test = MediaPlayer.create(context, R.raw.Abstract_Click);
//        test.prepare();
//        test.setOnCompletionListener(mp -> test.s {
//        });
//        test.start();
    }


}