package com.fund.iam.di.builder;

import com.fund.iam.service.PushService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract PushService bindPushService();

}
