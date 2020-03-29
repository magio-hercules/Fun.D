package com.fund.iam.di.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fund.iam.BuildConfig;
import com.fund.iam.data.remote.ApiHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {


    @Provides
    @Singleton
    @Named("aws")
    OkHttpClient provideAwsAuthOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides
    @Singleton
    @Named("firebase")
    OkHttpClient provideFirebaseAuthOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides
    @Singleton
    @Named("aws")
    Retrofit provideAwsService(@Named("aws") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.AWS_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("firebase")
    Retrofit provideFirebaseService(@Named("firebase") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.FCM_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("aws")
    ApiHelper provideAwsApiHelper(@Named("aws") Retrofit retrofit) {
        return retrofit.create(ApiHelper.class);
    }

    @Provides
    @Singleton
    @Named("firebase")
    ApiHelper provideFirebaseApiHelper(@Named("firebase") Retrofit retrofit) {
        return retrofit.create(ApiHelper.class);
    }
}
