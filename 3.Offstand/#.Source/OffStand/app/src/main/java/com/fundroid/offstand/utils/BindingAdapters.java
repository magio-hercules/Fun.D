package com.fundroid.offstand.utils;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("onTouch")
    public static void onTouch(View self, View.OnTouchListener onTouchListener) {
        self.setOnTouchListener(onTouchListener);
    }

}
