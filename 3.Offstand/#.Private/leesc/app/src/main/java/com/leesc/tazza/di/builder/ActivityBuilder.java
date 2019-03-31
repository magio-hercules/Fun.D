package com.leesc.tazza.di.builder;

import com.leesc.tazza.ui.lobby.LobbyActivity;

import com.leesc.tazza.ui.roominfo.RoomInfoActivity;
//import com.leesc.tazza.ui.roominfo.RoomInfoActivityModule;
import com.leesc.tazza.ui.splash.SplashActivity;
//import com.leesc.tazza.ui.splash.SplashActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract RoomInfoActivity bindRoomInfoActivity();

    @ContributesAndroidInjector
    abstract LobbyActivity bindLobbyActivity();

    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

}
