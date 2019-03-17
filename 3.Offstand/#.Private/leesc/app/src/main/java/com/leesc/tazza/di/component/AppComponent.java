package com.leesc.tazza.di.component;

import com.leesc.tazza.core.TazzaApplication;
import com.leesc.tazza.di.builder.ActivityBuilder;

import com.leesc.tazza.di.builder.ReceiverBuilder;
import com.leesc.tazza.di.builder.ServiceBuilder;
import com.leesc.tazza.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivityBuilder.class, ServiceBuilder.class, ReceiverBuilder.class})
public interface AppComponent extends AndroidInjector<TazzaApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<TazzaApplication> {
    }

}
