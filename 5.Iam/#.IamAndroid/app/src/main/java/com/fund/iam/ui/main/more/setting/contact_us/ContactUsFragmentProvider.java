package com.fund.iam.ui.main.more.setting.contact_us;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ContactUsFragmentProvider {

    @ContributesAndroidInjector
    abstract ContactUsFragment provideContactUsFragmentFactory();
}
