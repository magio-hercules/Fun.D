package com.fundroid.offstand.di.component;

import com.fundroid.offstand.core.OffStandApplication;
import com.fundroid.offstand.di.builder.ActivityBuilder;
import com.fundroid.offstand.di.builder.ReceiverBuilder;
import com.fundroid.offstand.di.builder.ServiceBuilder;
import com.fundroid.offstand.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivityBuilder.class, ServiceBuilder.class, ReceiverBuilder.class})
public interface AppComponent extends AndroidInjector<OffStandApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<OffStandApplication> {
    }

}
