package com.fundroid.offstand.di.builder;

import com.fundroid.offstand.ui.lobby.LobbyActivity;
import com.fundroid.offstand.ui.roominfo.RoomInfoActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

//import com.leesc.tazza.ui.roominfo.RoomInfoActivityModule;
//import com.leesc.tazza.ui.splash.SplashActivityModule;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract RoomInfoActivity bindRoomInfoActivity();

    @ContributesAndroidInjector
    abstract LobbyActivity bindLobbyActivity();

}
