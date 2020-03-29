package com.fund.iam.ui.main.more.notice;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NoticeFragmentProvider {

    @ContributesAndroidInjector
    abstract NoticeFragment provideNoticeFragmentFactory();
}
