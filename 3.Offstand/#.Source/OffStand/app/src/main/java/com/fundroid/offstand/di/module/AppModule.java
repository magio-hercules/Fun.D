package com.fundroid.offstand.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;

import com.fundroid.offstand.R;
import com.fundroid.offstand.core.AppConstant;
import com.fundroid.offstand.core.OffStandApplication;
import com.fundroid.offstand.data.AppDataManager;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.local.prefs.AppPreferencesHelper;
import com.fundroid.offstand.data.local.prefs.PreferencesHelper;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.di.quailfier.PreferenceInfo;
import com.fundroid.offstand.receiver.WifiDirectReceiver;
import com.fundroid.offstand.service.WifiP2pService;
import com.fundroid.offstand.utils.rx.AppSchedulerProvider;
import com.fundroid.offstand.utils.rx.SchedulerProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(OffStandApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    static SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(providePreferenceName(), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    static ResourceProvider provideResourceProvider(Context context) {
        return new ResourceProvider(context);
    }

    @Provides
    @PreferenceInfo
    static String providePreferenceName() {
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
