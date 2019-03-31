package com.leesc.tazza.ui.lobby;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.leesc.tazza.BR;
import com.leesc.tazza.R;
import com.leesc.tazza.data.model.Room;
import com.leesc.tazza.databinding.ActivityLobbyBinding;
import com.leesc.tazza.receiver.WifiDirectReceiver;
import com.leesc.tazza.ui.base.BaseActivity;
import com.leesc.tazza.ui.roominfo.RoomInfoActivity;
import com.leesc.tazza.utils.ViewModelProviderFactory;

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
import pl.droidsonroids.gif.GifDrawable;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lsc", "LobbyActivity onCreate "/* + (wifiP2pService == null)*/);
        activityLobbyBinding = getViewDataBinding();
        lobbyViewModel.setNavigator(this);
        initViews();
        setupRecyclerView(activityLobbyBinding.rvRoom, new RoomAdapter());
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
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

    private void initViews() {
        ((GifDrawable) activityLobbyBinding.bgLobby.getDrawable()).setLoopCount(0);
    }

    private void setupRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void goToRoomInfoActivity() {
        RoomInfoActivity.start(this);
    }

    @Override
    public void goToSettingActivity() {
    }

    @Override
    public void onRepositoriesChanged(List<Room> rooms) {
        RoomAdapter adapter = (RoomAdapter) activityLobbyBinding.rvRoom.getAdapter();
        adapter.setDatas(rooms);
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