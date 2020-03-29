package com.fund.iam.ui.main.search;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchFragmentProvider {

    @ContributesAndroidInjector
    abstract SearchFragment provideSettingFragmentFactory();
}
