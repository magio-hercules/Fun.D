package com.fund.iam.ui.main.more.setting.update;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class UpdateFragmentProvider {

    @ContributesAndroidInjector
    abstract UpdateFragment provideUpdateFragmentFactory();
}
