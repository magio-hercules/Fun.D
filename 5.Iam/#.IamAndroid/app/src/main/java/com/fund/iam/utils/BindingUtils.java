package com.fund.iam.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.SignInButton;
import com.orhanobut.logger.Logger;

import java.util.List;

public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }

//    @BindingAdapter("android:onClick")
//    public static void bindSignInClick(SignInButton signInButton, View.OnClickListener onClickListener) {
//        signInButton.setOnClickListener(onClickListener);
//    }

    @BindingAdapter("imgUrl")
    public static void setImageResource(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(imageView);
    }

}
