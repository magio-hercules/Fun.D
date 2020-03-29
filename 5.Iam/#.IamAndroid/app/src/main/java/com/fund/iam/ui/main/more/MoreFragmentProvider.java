package com.fund.iam.ui.main.more;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MoreFragmentProvider {

    @ContributesAndroidInjector
    abstract MoreFragment provideMoreFragmentFactory();
}
