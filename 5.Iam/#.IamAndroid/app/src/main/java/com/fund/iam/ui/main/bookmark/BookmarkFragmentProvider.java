package com.fund.iam.ui.main.bookmark;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BookmarkFragmentProvider {

    @ContributesAndroidInjector
    abstract BookmarkFragment provideSettingFragmentFactory();
}
