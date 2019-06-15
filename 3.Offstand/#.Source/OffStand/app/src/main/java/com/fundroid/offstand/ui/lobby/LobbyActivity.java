package com.fundroid.offstand.ui.lobby;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fundroid.offstand.BR;
import com.fundroid.offstand.R;
import com.fundroid.offstand.SettingActivity;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.databinding.ActivityLobbyBinding;
import com.fundroid.offstand.ui.base.BaseActivity;
import com.fundroid.offstand.ui.lobby.main.MainFragment;
import com.fundroid.offstand.utils.ViewModelProviderFactory;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.Disposable;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.fundroid.offstand.utils.CommonUtils.getVisibleFragmentTag;

public class LobbyActivity extends BaseActivity<ActivityLobbyBinding, LobbyViewModel> implements LobbyNavigator, HasSupportFragmentInjector {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    LobbyViewModel lobbyViewModel;

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
        lobbyViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(LobbyViewModel.class);
        return lobbyViewModel;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        Log.d("lsc", "LobbyActivity supportFragmentInjector " + fragmentDispatchingAndroidInjector);
        return fragmentDispatchingAndroidInjector;
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, LobbyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("lsc", "LobbyActivity onCreate ");
        lobbyViewModel.setNavigator(this);
        initViews();
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
            Log.d("lsc", "LobbyActivity onBackPressed 1");
            super.onBackPressed();
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