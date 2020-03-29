package com.fund.iam.ui.main.more;

import android.content.Context;

import androidx.core.util.Pair;
import androidx.databinding.ObservableField;

import com.fund.iam.R;
import com.fund.iam.data.bus.NavigateBus;
import com.fund.iam.ui.main.more.notice.NoticeFragment;
import com.orhanobut.logger.Logger;

public class ItemMoreViewModel {

    public ObservableField<String> title = new ObservableField<>();
    private Context mContext;

    public ItemMoreViewModel(Context context, String title) {
        this.title.set(title);
        mContext = context;
    }

    public void onItemClick(String title) {
        if (title.equals(mContext.getResources().getString(R.string.title_notice))) {
            NavigateBus.getInstance().sendDestination(R.id.navigation_notice);
        } else if (title.equals(mContext.getResources().getString(R.string.title_setting))) {
            NavigateBus.getInstance().sendDestination(R.id.navigation_setting);
        } else {
            Logger.e("not defined title");
        }

    }
}
