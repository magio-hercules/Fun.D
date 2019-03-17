package com.leesc.tazza.ui.splash;

import android.os.Bundle;
import android.util.Log;

import com.leesc.tazza.BR;
import com.leesc.tazza.R;
import com.leesc.tazza.databinding.ActivitySplashBinding;
import com.leesc.tazza.ui.base.BaseActivity;
import com.leesc.tazza.ui.lobby.LobbyActivity;
import com.leesc.tazza.ui.main.MainActivity;

import javax.inject.Inject;

import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    @Inject
    SplashViewModel splashViewModel;

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
        return splashViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel.setNavigator(this);
        Log.d("lsc","SplashActivity onCreate");
    }

    @Override
    public void startMainActivity() {
        MainActivity.start(this);
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
        finish();
    }

    @Override
    public void startLobbyActivity() {
        LobbyActivity.start(this);
        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void handleError(Throwable throwable) {

    }
}