package com.fundroid.offstand.di.builder;

import com.fundroid.offstand.receiver.WifiDirectReceiver;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ReceiverBuilder {

    @ContributesAndroidInjector
    abstract WifiDirectReceiver bindWifiDirectReceiver();

}
