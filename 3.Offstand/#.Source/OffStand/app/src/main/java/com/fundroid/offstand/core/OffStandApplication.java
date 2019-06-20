package com.fundroid.offstand.core;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.fundroid.offstand.data.remote.ConnectionManager;
import com.fundroid.offstand.di.component.DaggerAppComponent;
import com.fundroid.offstand.utils.rx.ServerPublishSubjectBus;
import com.google.firebase.FirebaseApp;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerApplication;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

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

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(e -> Log.e("lsc","rx Handle error " + e.getMessage()));
        Stetho.initializeWithDefaults(this);
        FirebaseApp.initializeApp(this);
        ServerPublishSubjectBus.getInstance().getEvents(String.class)
                .flatMap(json -> ConnectionManager.serverProcessor((String) json))
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
//                    Log.d("lsc", "/**/OffStandApplication result " + result);
                }, onError -> {
                    Log.d("lsc", "OffStandApplication onError " + onError);
                }, () -> Log.d("lsc", "OffStandApplication onCompleted"));

    }


}