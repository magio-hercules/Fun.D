package com.fund.iam.ui.main.more.setting;


import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;

import com.fund.iam.R;
import com.fund.iam.data.DataManager;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.base.BaseViewModel;
import com.fund.iam.utils.CommonUtils;

public class SettingViewModel extends BaseViewModel<SettingNavigator> {

    public ObservableField<String> currentVersion = new ObservableField<>();
    public ObservableField<String> versionState = new ObservableField<>();
    public ObservableField<Integer> versionStateColor = new ObservableField<>();
    private Context mContext;

    public SettingViewModel(Context context, DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider, resourceProvider);
        mContext = context;
        initViewContents();
    }

    private void initViewContents() {
        currentVersion.set(String.format(getResourceProvider().getString(R.string.version_current), CommonUtils.getVersionName(mContext)));
        versionState.set(getResourceProvider().getString(R.string.version_state_normal));
        versionStateColor.set(getResourceProvider().getColor(R.color.red));
    }


}
