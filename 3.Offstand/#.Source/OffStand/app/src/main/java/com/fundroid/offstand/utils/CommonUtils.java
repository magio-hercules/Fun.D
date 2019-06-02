package com.fundroid.offstand.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.fundroid.offstand.R;
import com.fundroid.offstand.ui.lobby.main.MainFragment;

public final class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static String getVisibleFragmentTag(FragmentActivity context) {
        for (Fragment fragment : context.getSupportFragmentManager().getFragments())
            if (!fragment.getTag().equals(MainFragment.TAG)) {
                if (fragment.isVisible()) {
                    return fragment.getTag();
                }
            }
        return MainFragment.TAG;
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
