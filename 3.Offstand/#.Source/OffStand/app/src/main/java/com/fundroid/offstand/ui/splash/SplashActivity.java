package com.fundroid.offstand.ui.splash;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.ui.setting.SettingActivity;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.databinding.ActivitySplashBinding;
import com.fundroid.offstand.ui.base.BaseActivity;
import com.fundroid.offstand.ui.lobby.LobbyActivity;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DataManager dataManager;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        return ViewModelProviders.of(this, viewModelProviderFactory).get(SplashViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String videoRootPath = "android.resource://" + getPackageName() + "/";
        getViewDataBinding().splash.setVideoURI(Uri.parse(videoRootPath + R.raw.mp4_splash));
        getViewDataBinding().splash.setZOrderOnTop(true);
        getViewDataBinding().splash.start();
        getViewDataBinding().splash.setOnCompletionListener(mp -> goNext());
    }

    private void goNext() {
        if (dataManager.getUserName() == null) {
            SettingActivity.start(this);
        } else {
            LobbyActivity.start(this);
        }
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void handleError(Throwable throwable) {

    }
}
