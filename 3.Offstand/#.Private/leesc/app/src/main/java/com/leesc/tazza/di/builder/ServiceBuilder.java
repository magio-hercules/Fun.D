package com.leesc.tazza.di.builder;

import com.leesc.tazza.service.WifiP2pService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract WifiP2pService bindNetworkService();

}
