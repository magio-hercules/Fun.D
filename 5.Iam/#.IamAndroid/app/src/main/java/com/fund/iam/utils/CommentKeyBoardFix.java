package com.fund.iam.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;

public class CommentKeyBoardFix
{
    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private Rect contentAreaOfWindowBounds = new Rect();

    public CommentKeyBoardFix(Activity activity)
    {
        FrameLayout content = activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(this::possiblyResizeChildOfContent);
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent()
    {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious)
        {
            int heightDifference = 0;
            if (heightDifference > (usableHeightNow /4))
            {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightNow - heightDifference;
            }
            else
            {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightNow;
            }
            mChildOfContent.layout(contentAreaOfWindowBounds.left, contentAreaOfWindowBounds.top, contentAreaOfWindowBounds.right, contentAreaOfWindowBounds.bottom);
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight()
    {
        mChildOfContent.getWindowVisibleDisplayFrame(contentAreaOfWindowBounds);
        return contentAreaOfWindowBounds.height();
    }
}