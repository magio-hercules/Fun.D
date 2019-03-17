package com.leesc.tazza.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leesc.tazza.R;
import com.leesc.tazza.core.AppConstant;
import com.leesc.tazza.core.TazzaApplication;
import com.leesc.tazza.data.AppDataManager;
import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.data.local.prefs.AppPreferencesHelper;
import com.leesc.tazza.data.local.prefs.PreferencesHelper;
import com.leesc.tazza.di.provider.ResourceProvider;
import com.leesc.tazza.di.quailfier.PreferenceInfo;
import com.leesc.tazza.receiver.WifiDirectReceiver;
import com.leesc.tazza.service.WifiP2pService;
import com.leesc.tazza.utils.rx.AppSchedulerProvider;
import com.leesc.tazza.utils.rx.SchedulerProvider;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(TazzaApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    static SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.shared_preferences), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    static ResourceProvider provideResourceProvider(Context context) {
        return new ResourceProvider(context);
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstant.PREF_NAME;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    WifiP2pService provideNetworkService() {
        return new WifiP2pService();
    }

    @Provides
    WifiP2pManager provideWifiP2pManager(Context context) {
        return (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
    }

    @Provides
    WifiP2pManager.Channel provideWifiP2pManagerChannel(WifiP2pManager wifiP2pManager, Context context, WifiP2pService wifiP2pService) {
        return wifiP2pManager.initialize(context, context.getMainLooper(), wifiP2pService);
    }

    @Provides
    WifiDirectReceiver provideWifiDirectReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, WifiP2pService wifiP2pService) {
        return new WifiDirectReceiver(wifiP2pManager, channel, wifiP2pService);
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }
}
