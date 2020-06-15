package com.fund.iam.ui.main.more.setting.update;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.databinding.ObservableField;

import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.fund.iam.utils.CommonUtils;

public class UpdateViewModel extends BaseViewModel<UpdateNavigator> {

    public ObservableField<String> currentVersion = new ObservableField();
    public ObservableField<String> message = new ObservableField();
    public Context mContext;

    public UpdateViewModel(Context context, DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        mContext = context;
        currentVersion.set("현재 버전 " + CommonUtils.getVersionName(context));
        message.set(CommonUtils.getVersionName(context).equals(getDataManager().getMarketVersion()) ? resourceProvider.getString(R.string.version_state_normal) : resourceProvider.getString(R.string.version_state_old));
    }

    public void onUpdate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id="+mContext.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
