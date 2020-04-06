package com.fund.iam.ui.main.channel;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CreateChannelFragmentProvider {

    @ContributesAndroidInjector
    abstract CreateChannelFragment provideCreateChannelFragmentFactory();
}
