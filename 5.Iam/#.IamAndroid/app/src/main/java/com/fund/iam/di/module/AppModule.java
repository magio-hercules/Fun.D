package com.fund.iam.di.module;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.fund.iam.core.AppConstants;
import com.fund.iam.data.AppDataManager;
import com.fund.iam.data.DataManager;

import com.fund.iam.data.local.prefs.AppPreferencesHelper;
import com.fund.iam.data.local.prefs.PreferencesHelper;
import com.fund.iam.di.provider.AppResourceProvider;
import com.fund.iam.di.provider.AppSchedulerProvider;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.di.qualifier.PreferenceInfo;
import com.fund.iam.ui.letter.LetterAdapter;
import com.fund.iam.ui.main.letterbox.LetterBoxAdapter;
import com.fund.iam.ui.main.more.MoreAdapter;
import com.fund.iam.ui.main.more.notice.NoticeAdapter;
import com.fund.iam.ui.main.more.setting.contact_us.ContactAdapter;
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
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    static SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(providePreferenceName(), Context.MODE_PRIVATE);
    }

    @Provides
    @PreferenceInfo
    static String providePreferenceName() {
        return AppConstants.PREF_NAME;
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

//    @Provides
//    @DatabaseInfo
//    String provideDatabaseName() {
//        return AppConstants.DB_NAME;
//    }
//
//    @Provides
//    @Singleton
//    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
//        return appDbHelper;
//    }

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
    ResourceProvider provideResourceProvider(Context context) {
        return new AppResourceProvider(context);
    }

    @Provides
    LetterBoxAdapter provideLetterBoxAdapter() {
        return new LetterBoxAdapter(new ArrayList<>());
    }

    @Provides
    ContactAdapter provideContactAdapter() {
        return new ContactAdapter(new ArrayList<>());
    }

    @Provides
    LetterAdapter provideLetterAdapter() {
        return new LetterAdapter(new ArrayList<>());
    }

    @Provides
    MoreAdapter provideMoreAdapter() {
        return new MoreAdapter(new ArrayList<>());
    }

    @Provides
    NoticeAdapter provideNoticeAdapter() {
        return new NoticeAdapter(new ArrayList<>());
    }

    @Provides
    NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

}
