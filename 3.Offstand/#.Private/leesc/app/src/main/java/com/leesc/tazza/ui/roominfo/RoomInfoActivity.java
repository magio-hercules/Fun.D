package com.leesc.tazza.ui.roominfo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;

import com.leesc.tazza.BR;
import com.leesc.tazza.R;
import com.leesc.tazza.databinding.ActivityRoomInfoBinding;
import com.leesc.tazza.receiver.WifiDirectReceiver;
import com.leesc.tazza.ui.base.BaseActivity;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RoomInfoActivity extends BaseActivity<ActivityRoomInfoBinding, RoomInfoViewModel> implements RoomInfoNavigator, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    RoomInfoViewModel roomInfoViewModel;

    @Inject
    WifiDirectReceiver wifiDirectReceiver;

    private ActivityRoomInfoBinding activityRoomInfoBinding;
    private IntentFilter intentFilter;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_room_info;
    }

    @Override
    public RoomInfoViewModel getViewModel() {
        return roomInfoViewModel;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    public static void start(Context context) {
        final Intent intent = new Intent(context, RoomInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRoomInfoBinding = getViewDataBinding();
        roomInfoViewModel.setNavigator(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lsc", "RoomInfoActivity onStart");
        registerReceiver(wifiDirectReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lsc", "RoomInfoActivity onStop");
        unregisterReceiver(wifiDirectReceiver);
    }

    @Override
    public void handleError(Throwable throwable) {
        Log.e("lsc", "RoomInfoActivity handleError " + throwable.getMessage());
    }

}