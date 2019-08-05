package com.fundroid.offstand.ui.lobby.wifialert;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class WifiAlertDialogProvider {

    @ContributesAndroidInjector
    abstract WifiAlertDialog provideWifiAlertDialogFactory();
}
