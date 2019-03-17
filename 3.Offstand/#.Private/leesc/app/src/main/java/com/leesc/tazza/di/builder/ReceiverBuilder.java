package com.leesc.tazza.di.builder;

import com.leesc.tazza.receiver.WifiDirectReceiver;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ReceiverBuilder {

    @ContributesAndroidInjector
    abstract WifiDirectReceiver bindWifiDirectReceiver();

}
