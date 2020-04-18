package com.fund.iam.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

public class ViewUtils {

    public static int PixelToDp(Context context, int pixel) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = pixel / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    public static int DpToPixel(Context context, int DP) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources().getDisplayMetrics());
        return (int) px;
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }



}
