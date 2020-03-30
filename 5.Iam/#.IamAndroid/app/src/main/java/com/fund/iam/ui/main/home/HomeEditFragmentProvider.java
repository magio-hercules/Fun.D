package com.fund.iam.ui.main.home;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeEditFragmentProvider {

    @ContributesAndroidInjector
    abstract HomeEditFragment provideHomeFragmentFactory();
}
