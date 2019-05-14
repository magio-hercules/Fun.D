package com.fundroid.offstand.di.builder;

import com.fundroid.offstand.SettingActivity;
import com.fundroid.offstand.ui.lobby.LobbyActivity;
import com.fundroid.offstand.ui.lobby.findroom.FindRoomFragmentProvider;
import com.fundroid.offstand.ui.lobby.main.MainFragmentProvider;
import com.fundroid.offstand.ui.lobby.makeroom.MakeRoomFragmentProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {
            MakeRoomFragmentProvider.class,
            MainFragmentProvider.class,
            FindRoomFragmentProvider.class})
    abstract LobbyActivity bindLobbyActivity();

    @ContributesAndroidInjector()
    abstract SettingActivity bindSettingActivity();

}
