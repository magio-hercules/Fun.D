package com.fund.iam.ui.main.home;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
//import android.widget.ImageView;

public class FixedCenterCrop extends androidx.appcompat.widget.AppCompatImageView
{
    int mWidth;
    int mHeight;

    public FixedCenterCrop(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
    {
//        Log.d(HomeFragment.class.getSimpleName(), "!!!!!!!!!!!!!!!!!!!!");
    
        final Drawable d = this.getDrawable();

        if(d != null) {
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int minW = width;
            int minH = height;

//            Log.d(HomeFragment.class.getSimpleName(), "onMeasure widthMeasureSpec: " + widthMeasureSpec + ", heightMeasureSpec: " + heightMeasureSpec);
//            Log.d(HomeFragment.class.getSimpleName(), "onMeasure width: " + width + ", height: " + height);

            if(width >= height) {
                height = (int) Math.ceil(width * (float) d.getIntrinsicHeight() / d.getIntrinsicWidth());
//                Log.d(HomeFragment.class.getSimpleName(), "onMeasure d.getIntrinsicHeight(): " + d.getIntrinsicHeight() + ", d.getIntrinsicWidth(): " + d.getIntrinsicWidth());
//                Log.d(HomeFragment.class.getSimpleName(), "onMeasure value : " + width * (float) d.getIntrinsicHeight() / d.getIntrinsicWidth());
            }
            else{
                width = (int) Math.ceil(height * (float) d.getIntrinsicWidth() / d.getIntrinsicHeight());
//                Log.d(HomeFragment.class.getSimpleName(), "onMeasure d.getIntrinsicWidth(): " + d.getIntrinsicWidth() + ", d.getIntrinsicHeight(): " + d.getIntrinsicHeight());
//                Log.d(HomeFragment.class.getSimpleName(), "onMeasure value : " + height * (float) d.getIntrinsicWidth() / d.getIntrinsicHeight());

            }
//            Log.e(HomeFragment.class.getSimpleName(), "onMeasure 111 width: " + width + ", height: " + height);

            if (width > minW) {
                float ratio = (float)minW / width;
//                Log.e(HomeFragment.class.getSimpleName(), "onMeasure 222 width: " + width + ", minW: " + minW);
//                Log.d(HomeFragment.class.getSimpleName(), "ratio : " + ratio);

                width = (int) Math.ceil(width * ratio);
                height = (int) Math.ceil(height * ratio);
            } else {
                float ratio = (float)minW / width;
//                Log.e(HomeFragment.class.getSimpleName(), "onMeasure 333 width: " + width + ", minW: " + minW);
//                Log.d(HomeFragment.class.getSimpleName(), "ratio : " + ratio);

                width = (int) Math.ceil(width * ratio);
                height = (int) Math.ceil(height * ratio);
            }

//            Log.e(HomeFragment.class.getSimpleName(), "onMeasure 555 width: " + width + ", height: " + height);

            mWidth = width;
            mHeight = height;
            this.setMeasuredDimension(width, height);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}