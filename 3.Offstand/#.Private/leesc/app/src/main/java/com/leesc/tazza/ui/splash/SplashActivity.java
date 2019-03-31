package com.leesc.tazza.ui.splash;

import android.os.Bundle;
import android.util.Log;

import com.leesc.tazza.BR;
import com.leesc.tazza.R;
import com.leesc.tazza.databinding.ActivitySplashBinding;
import com.leesc.tazza.ui.base.BaseActivity;
import com.leesc.tazza.ui.lobby.LobbyActivity;
import com.leesc.tazza.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import pl.droidsonroids.gif.GifDrawable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    SplashViewModel splashViewModel;

    private ActivitySplashBinding activitySplashBinding;

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
        splashViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(SplashViewModel.class);
        return splashViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = getViewDataBinding();
        splashViewModel.setNavigator(this);
        Log.d("lsc", "SplashActivity onCreate");
        initViews();
    }

    private void initViews() {
        ((GifDrawable) activitySplashBinding.bgSplash.getDrawable()).stop();
        ((GifDrawable) activitySplashBinding.bgSplash.getDrawable()).addAnimationListener(loopNumber -> startLobbyActivity());
    }

    @Override
    public void startSplashAnimation() {
        ((GifDrawable) activitySplashBinding.bgSplash.getDrawable()).start();
    }

    @Override
    public void startLobbyActivity() {
        LobbyActivity.start(this);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        finish();
    }

    @Override
    public void finishLobbyActivity() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void handleError(Throwable throwable) {

    }
}