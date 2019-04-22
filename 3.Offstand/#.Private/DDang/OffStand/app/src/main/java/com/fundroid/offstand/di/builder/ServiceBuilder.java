package com.fundroid.offstand.di.builder;

import com.fundroid.offstand.service.WifiP2pService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract WifiP2pService bindNetworkService();

}
