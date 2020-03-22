package com.fund.iam.di.builder;

import com.fund.iam.ui.intro.IntroActivity;
import com.fund.iam.ui.letter.LetterActivity;
import com.fund.iam.ui.login.LoginActivity;
import com.fund.iam.ui.main.MainActivity;
import com.fund.iam.ui.main.bookmark.BookmarkFragmentProvider;
import com.fund.iam.ui.main.home.HomeFragmentProvider;
import com.fund.iam.ui.main.letterbox.LetterBoxFragmentProvider;
import com.fund.iam.ui.main.search.SearchFragmentProvider;
import com.fund.iam.ui.main.setting.SettingFragmentProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract IntroActivity bindIntroActivity();

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = {
            HomeFragmentProvider.class,
            BookmarkFragmentProvider.class,
            SearchFragmentProvider.class,
            SettingFragmentProvider.class,
            LetterBoxFragmentProvider.class
    })
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract LetterActivity bindLetterActivity();
}
