package com.fund.iam.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fund.iam.R;
import com.orhanobut.logger.Logger;

public class RecyclerDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int startPadding, endPadding;

    public RecyclerDecoration(Drawable divider, int startPadding, int endPadding) {
        mDivider = divider;
        this.startPadding = startPadding;
        this.endPadding = endPadding;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int start = parent.getPaddingStart() + startPadding;
        int end = parent.getWidth() - parent.getPaddingEnd() - endPadding;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(start, top, end, bottom);
            mDivider.draw(c);
        }

    }
}
