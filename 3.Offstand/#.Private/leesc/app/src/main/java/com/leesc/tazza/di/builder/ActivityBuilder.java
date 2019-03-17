package com.leesc.tazza.di.builder;

import com.leesc.tazza.ui.lobby.LobbyActivity;
import com.leesc.tazza.ui.lobby.LobbyActivityModule;
import com.leesc.tazza.ui.main.MainActivity;
import com.leesc.tazza.ui.main.MainActivityModule;
import com.leesc.tazza.ui.roominfo.RoomInfoActivity;
import com.leesc.tazza.ui.roominfo.RoomInfoActivityModule;
import com.leesc.tazza.ui.splash.SplashActivity;
import com.leesc.tazza.ui.splash.SplashActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {
            RoomInfoActivityModule.class
    })
    abstract RoomInfoActivity bindRoomInfoActivity();

    @ContributesAndroidInjector(modules = {
            LobbyActivityModule.class
    })
    abstract LobbyActivity bindLobbyActivity();

    @ContributesAndroidInjector(modules = {
            MainActivityModule.class
    })
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = SplashActivityModule.class)
    abstract SplashActivity bindSplashActivity();

}
