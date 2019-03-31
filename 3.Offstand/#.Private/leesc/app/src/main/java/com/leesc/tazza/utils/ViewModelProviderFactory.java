package com.leesc.tazza.utils;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;

import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.di.provider.ResourceProvider;
import com.leesc.tazza.service.WifiP2pService;
import com.leesc.tazza.ui.lobby.LobbyViewModel;
import com.leesc.tazza.ui.roominfo.RoomInfoViewModel;
import com.leesc.tazza.ui.splash.SplashViewModel;
import com.leesc.tazza.utils.rx.SchedulerProvider;

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
        if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            return (T) new SplashViewModel(dataManager, schedulerProvider, context, resourceProvider);
        } else if (modelClass.isAssignableFrom(LobbyViewModel.class)) {
            return (T) new LobbyViewModel(dataManager, schedulerProvider, wifiP2pManager, channel, resourceProvider);
        } else if (modelClass.isAssignableFrom(RoomInfoViewModel.class)) {
            return (T) new RoomInfoViewModel(dataManager, schedulerProvider, wifiP2pManager, channel, resourceProvider);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
