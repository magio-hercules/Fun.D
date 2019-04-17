package com.fundroid.offstand.utils;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.service.WifiP2pService;
import com.fundroid.offstand.ui.lobby.LobbyViewModel;
import com.fundroid.offstand.ui.lobby.findroom.FindRoomViewModel;
import com.fundroid.offstand.ui.lobby.main.MainViewModel;
import com.fundroid.offstand.ui.lobby.makeroom.MakeRoomViewModel;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;
    private final DataManager dataManager;
    private final SchedulerProvider schedulerProvider;
    private final WifiP2pService wifiP2pService;
    private final WifiP2pManager wifiP2pManager;
    private final WifiP2pManager.Channel channel;
    private final ResourceProvider resourceProvider;

    @Inject
    public ViewModelProviderFactory(Context context, DataManager dataManager, SchedulerProvider schedulerProvider, WifiP2pService wifiP2pService, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, ResourceProvider resourceProvider) {
        this.context = context;
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.wifiP2pService = wifiP2pService;
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.resourceProvider = resourceProvider;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LobbyViewModel.class)) {
            return (T) new LobbyViewModel(dataManager, schedulerProvider, wifiP2pManager, channel, resourceProvider);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(FindRoomViewModel.class)) {
            return (T) new FindRoomViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(MakeRoomViewModel.class)) {
            return (T) new MakeRoomViewModel(dataManager, schedulerProvider);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
