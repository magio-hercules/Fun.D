package com.fundroid.offstand.ui.lobby;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.ui.lobby.wifialert.WifiAlertDialog;
import com.fundroid.offstand.ui.setting.SettingActivity;
import com.fundroid.offstand.databinding.ActivityLobbyBinding;
import com.fundroid.offstand.ui.base.BaseActivity;
import com.fundroid.offstand.ui.lobby.main.MainFragment;
import com.fundroid.offstand.utils.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import static com.fundroid.offstand.utils.CommonUtils.getVisibleFragmentTag;

public class LobbyActivity extends BaseActivity<ActivityLobbyBinding, LobbyViewModel> implements LobbyNavigator, HasSupportFragmentInjector {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    long lastTimeBackPressed;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lobby;
    }

    @Override
    public LobbyViewModel getViewModel() {
        return ViewModelProviders.of(this, viewModelProviderFactory).get(LobbyViewModel.class);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, LobbyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        initViews();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.v("lsc", "LobbyActivity onCreate " + wifiManager.isWifiEnabled());
        if(wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        }
    }

    private void initViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.fragment_container, MainFragment.newInstance(), MainFragment.TAG)
                .commit();
    }

    @Override
    public void goToSettingActivity() {
        MediaPlayer.create(LobbyActivity.this, R.raw.mouth_interface_button).start();
        Intent intent = new Intent(LobbyActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Log.d("lsc", "LobbyActivity onBackPressed " + getVisibleFragmentTag(this, MainFragment.TAG));
        if (getVisibleFragmentTag(this, MainFragment.TAG).equals(MainFragment.TAG)) {

            if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
                finishAffinity();
                return;
            }

            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            lastTimeBackPressed = System.currentTimeMillis();
        } else {
            Log.d("lsc", "LobbyActivity onBackPressed 2");
            onFragmentDetached(getVisibleFragmentTag(this, MainFragment.TAG));
        }
    }

    @Override
    public void onFragmentDetached(String tag) {
        Log.d("lsc", "LobbyActivity onFragmentDetached tag " + tag);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .remove(fragment)
                    .commitNow();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleError(Throwable throwable) {
        Log.e("lsc", "LobbyActivity handleError " + throwable.getMessage());
    }

}