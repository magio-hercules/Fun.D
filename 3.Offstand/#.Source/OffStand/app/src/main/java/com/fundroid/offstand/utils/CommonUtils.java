package com.fundroid.offstand.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.annimon.stream.Stream;
import com.fundroid.offstand.R;
import com.fundroid.offstand.ui.lobby.main.MainFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static String getRandomString(int length)
    {
        StringBuilder stringBuilder = new StringBuilder();


        for(int i = 0; i< length; i++) {
            Random rnd = new Random();
//            String randomStr = String.valueOf();
            stringBuilder.append(((char) ((rnd.nextInt(26)) + 65)));
        }

        return stringBuilder.toString();
    }

    public static String getVisibleFragmentTag(FragmentActivity context) {
        for (Fragment fragment : context.getSupportFragmentManager().getFragments())
            if (fragment.isVisible()) {
                return fragment.getTag();
            }

        return null;
    }

    public static String getVisibleFragmentTag(FragmentActivity context, String tagForExceptFragment) {
        for (Fragment fragment : context.getSupportFragmentManager().getFragments())
            if (!fragment.getTag().equals(tagForExceptFragment)) {
                if (fragment.isVisible()) {
                    return fragment.getTag();
                }
            }
        return tagForExceptFragment;
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
