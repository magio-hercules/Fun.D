package com.fundroid.offstand.core;

import android.app.Service;
import android.content.BroadcastReceiver;

import com.fundroid.offstand.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerApplication;

public class OffStandApplication extends DaggerApplication {

    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Inject
    DispatchingAndroidInjector<BroadcastReceiver> dispatchingBroadcastReceiverInjector;

    @Override
    public DispatchingAndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

    @Override
    public DispatchingAndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return dispatchingBroadcastReceiverInjector;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}