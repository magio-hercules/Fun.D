package com.fund.iam.di.provider;

import android.graphics.drawable.Drawable;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

public interface ResourceProvider {

    Drawable getDrawable(@DrawableRes int resId);

    String[] getArray(@ArrayRes int resId);

    String getString(@StringRes int resId);

    String getString(@StringRes int resId, Object... formatArgs);

    int getInteger(@IntegerRes int resId);

    int getColor(@ColorRes int resId);

}
