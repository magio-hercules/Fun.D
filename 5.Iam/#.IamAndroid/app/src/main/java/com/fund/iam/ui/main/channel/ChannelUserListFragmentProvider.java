package com.fund.iam.ui.main.channel;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ChannelUserListFragmentProvider {

    @ContributesAndroidInjector
    abstract ChannelUserListFragment provideChannelUserListFragmentFactory();
}
