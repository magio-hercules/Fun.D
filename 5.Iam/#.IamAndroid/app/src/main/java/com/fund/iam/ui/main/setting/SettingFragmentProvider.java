package com.fund.iam.ui.main.setting;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingFragmentProvider {

    @ContributesAndroidInjector
    abstract SettingFragment provideSettingFragmentFactory();
}
