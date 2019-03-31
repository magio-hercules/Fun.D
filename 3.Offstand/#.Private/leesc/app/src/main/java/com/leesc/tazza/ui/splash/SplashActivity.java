package com.leesc.tazza.ui.splash;

import android.os.Bundle;
import android.util.Log;

import com.leesc.tazza.BR;
import com.leesc.tazza.R;
import com.leesc.tazza.databinding.ActivitySplashBinding;
import com.leesc.tazza.ui.base.BaseActivity;
import com.leesc.tazza.ui.lobby.LobbyActivity;
import com.leesc.tazza.utils.ViewModelProviderFactory;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.disposables.Disposable;
import pl.droidsonroids.gif.GifDrawable;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    SplashViewModel splashViewModel;

    private ActivitySplashBinding activitySplashBinding;

    private Disposable disposable;

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
        Log.d("lsc", "SplashActivity onCreate");
        splashViewModel.setNavigator(this);
        activitySplashBinding = getViewDataBinding();
        initViews();

        //Todo : 롤리팝에서 onCreate 보다 ViewModel이 먼저 불려 navigator 에러남..  activity에서 퍼미션 체크로 변경
        disposable = TedRx2Permission.with(this)
                .setPermissions(ACCESS_FINE_LOCATION)
                .setDeniedMessage(getString(R.string.splash_msg_denied))
                .setGotoSettingButton(true)
                .setGotoSettingButtonText(getString(R.string.splash_msg_goto_setting))
                .request()
                .subscribe(
                        permissionResult -> {
                            Log.d("lsc", "SplashViewModel permissionResult " + permissionResult.isGranted());
                            if (permissionResult.isGranted()) {
                                startSplashAnimation();
                            } else {
                                //Todo : DialogFragment 으로 바꿀 것...
                                Log.d("lsc", "SplashViewModel permission not granted");
                                finishLobbyActivity();
                            }
                        },
                        this::handleError
                );

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
        disposable.dispose();
    }

    @Override
    public void handleError(Throwable throwable) {

    }
}