package com.fundroid.offstand.utils;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.ui.lobby.LobbyViewModel;
import com.fundroid.offstand.ui.lobby.findroom.FindRoomViewModel;
import com.fundroid.offstand.ui.lobby.guide.GuideViewModel;
import com.fundroid.offstand.ui.lobby.main.MainViewModel;
import com.fundroid.offstand.ui.lobby.makeroom.MakeRoomViewModel;
import com.fundroid.offstand.ui.lobby.wifialert.WifiAlertViewModel;
import com.fundroid.offstand.ui.splash.SplashViewModel;
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
    private final ResourceProvider resourceProvider;

    @Inject
    public ViewModelProviderFactory(Context context, DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        this.context = context;
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.resourceProvider = resourceProvider;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LobbyViewModel.class)) {
            return (T) new LobbyViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(dataManager, schedulerProvider, context);
        } else if (modelClass.isAssignableFrom(FindRoomViewModel.class)) {
            return (T) new FindRoomViewModel(context, dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(MakeRoomViewModel.class)) {
            return (T) new MakeRoomViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(GuideViewModel.class)) {
            return (T) new GuideViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            return (T) new SplashViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(WifiAlertViewModel.class)) {
            return (T) new WifiAlertViewModel(dataManager, schedulerProvider);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
