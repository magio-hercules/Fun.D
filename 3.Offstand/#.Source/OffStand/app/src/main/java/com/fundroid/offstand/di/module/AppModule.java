package com.fundroid.offstand.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fundroid.offstand.core.AppConstant;
import com.fundroid.offstand.core.OffStandApplication;
import com.fundroid.offstand.data.AppDataManager;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.data.local.prefs.AppPreferencesHelper;
import com.fundroid.offstand.data.local.prefs.PreferencesHelper;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.di.quailfier.PreferenceInfo;
import com.fundroid.offstand.ui.lobby.findroom.RoomAdapter;
import com.fundroid.offstand.utils.rx.AppSchedulerProvider;
import com.fundroid.offstand.utils.rx.SchedulerProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

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
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    RoomAdapter provideRoomAdapter() {
        return new RoomAdapter(new ArrayList<>());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }
}
