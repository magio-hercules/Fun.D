package com.fund.iam.di.component;

import android.app.Application;

import com.fund.iam.core.IamApplication;
import com.fund.iam.di.builder.ActivityBuilder;
import com.fund.iam.di.builder.ReceiverBuilder;
import com.fund.iam.di.builder.ServiceBuilder;
import com.fund.iam.di.module.AppModule;
import com.fund.iam.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, NetworkModule.class, ActivityBuilder.class, ReceiverBuilder.class, ServiceBuilder.class})
public interface AppComponent {

    void inject(IamApplication app);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}

