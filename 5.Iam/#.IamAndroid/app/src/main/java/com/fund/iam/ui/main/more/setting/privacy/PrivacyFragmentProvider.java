package com.fund.iam.ui.main.more.setting.privacy;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PrivacyFragmentProvider {

    @ContributesAndroidInjector
    abstract PrivacyFragment providePrivacyFragmentFactory();
}
