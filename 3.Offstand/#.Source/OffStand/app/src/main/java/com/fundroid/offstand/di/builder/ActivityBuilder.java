package com.fundroid.offstand.di.builder;

import com.fundroid.offstand.ui.lobby.wifialert.WifiAlertDialogProvider;
import com.fundroid.offstand.ui.play.PlayActivity;
import com.fundroid.offstand.ui.room.RoomActivity;
import com.fundroid.offstand.ui.setting.SettingActivity;
import com.fundroid.offstand.ui.lobby.LobbyActivity;
import com.fundroid.offstand.ui.lobby.findroom.FindRoomFragmentProvider;
import com.fundroid.offstand.ui.lobby.guide.GuideFragmentProvider;
import com.fundroid.offstand.ui.lobby.main.MainFragmentProvider;
import com.fundroid.offstand.ui.lobby.makeroom.MakeRoomFragmentProvider;
import com.fundroid.offstand.ui.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector(modules = {
            MakeRoomFragmentProvider.class,
            MainFragmentProvider.class,
            GuideFragmentProvider.class,
            FindRoomFragmentProvider.class,
            WifiAlertDialogProvider.class,})
    abstract LobbyActivity bindLobbyActivity();

    @ContributesAndroidInjector()
    abstract SettingActivity bindSettingActivity();

    @ContributesAndroidInjector()
    abstract RoomActivity bindRoomActivity();

    @ContributesAndroidInjector(modules = GuideFragmentProvider.class)
    abstract PlayActivity bindPlayActivity();

}
