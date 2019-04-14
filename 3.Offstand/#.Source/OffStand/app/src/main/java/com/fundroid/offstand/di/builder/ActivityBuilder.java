package com.fundroid.offstand.di.builder;

import com.fundroid.offstand.ui.lobby.LobbyActivity;
import com.fundroid.offstand.ui.lobby.main.MainFragmentProvider;
import com.fundroid.offstand.ui.lobby.roominfo.RoomInfoDialogProvider;
import com.fundroid.offstand.ui.roominfo.RoomInfoActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract RoomInfoActivity bindRoomInfoActivity();

    @ContributesAndroidInjector(modules = {
            MainFragmentProvider.class})
    abstract LobbyActivity bindLobbyActivity();

}
