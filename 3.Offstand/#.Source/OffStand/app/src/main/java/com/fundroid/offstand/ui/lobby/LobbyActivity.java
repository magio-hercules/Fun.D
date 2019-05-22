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
import com.fundroid.offstand.MainActivity;
import com.fundroid.offstand.R;
import com.fundroid.offstand.SettingActivity;
import com.fundroid.offstand.data.model.Room;
import com.fundroid.offstand.databinding.ActivityLobbyBinding;
import com.fundroid.offstand.receiver.WifiDirectReceiver;
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

import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.Disposable;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LobbyActivity extends BaseActivity<ActivityLobbyBinding, LobbyViewModel> implements LobbyNavigator, HasSupportFragmentInjector {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    LobbyViewModel lobbyViewModel;

    @Inject
    WifiDirectReceiver wifiDirectReceiver;

    private ActivityLobbyBinding activityLobbyBinding;
    private IntentFilter intentFilter;
    private Disposable disposable;

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
        return fragmentDispatchingAndroidInjector;
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, LobbyActivity.class);
        context.startActivity(intent);
    }

    // 배경음악 - 출처 : https://www.youtube.com/audiolibrary/music - Lone Wolf
    private  static MediaPlayer mp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lsc", "LobbyActivity onCreate ");
        lobbyViewModel.setNavigator(this);
        activityLobbyBinding = getViewDataBinding();
        initViews();
//        setupRecyclerView(activityLobbyBinding.rvRoom, new RoomAdapter());
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        disposable = TedRx2Permission.with(this)
                .setPermissions(ACCESS_FINE_LOCATION)
                .setDeniedMessage(getString(R.string.splash_msg_denied))
                .setGotoSettingButton(true)
                .setGotoSettingButtonText(getString(R.string.splash_msg_goto_setting))
                .request()
                .subscribe(
                        permissionResult -> {
                            Log.d("lsc", "LobbyActivity permissionResult " + permissionResult.isGranted());
                            if (permissionResult.isGranted()) {

                            } else {
//                                Todo : DialogFragment 으로 바꿀 것...
                                Log.d("lsc", "LobbyActivity permission not granted");
                                finish();
                            }
                        },
                        this::handleError
                );

        // 배경음악
//        mp = MediaPlayer.create(this, R.raw.lone_wolf);
//        mp.setLooping(true);
//        mp.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lsc", "LobbyActivity onStart");
        registerReceiver(wifiDirectReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lsc", "LobbyActivity onStop");
        unregisterReceiver(wifiDirectReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private void initViews() {
//        ((GifDrawable) activityLobbyBinding.bgLobby.getDrawable()).setLoopCount(0);
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .add(R.id.fragment_container, MainFragment.newInstance(), MainFragment.TAG)
                .commit();
    }

    private void setupRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void goToSettingActivity() {
        MediaPlayer.create(LobbyActivity.this, R.raw.mouth_interface_button).start();

        Intent intent = new Intent(LobbyActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRepositoriesChanged(List<Room> rooms) {
//        RoomAdapter adapter = (RoomAdapter) activityLobbyBinding.rvRoom.getAdapter();
//        adapter.setDatas(rooms);
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