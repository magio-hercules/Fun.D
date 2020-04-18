package com.fund.iam.di.builder;

import com.fund.iam.ui.intro.IntroActivity;
import com.fund.iam.ui.letter.LetterActivity;
import com.fund.iam.ui.login.LoginActivity;
import com.fund.iam.ui.main.MainActivity;
import com.fund.iam.ui.main.bookmark.BookmarkFragmentProvider;
import com.fund.iam.ui.main.channel.ChannelFragmentProvider;
import com.fund.iam.ui.main.channel.ChannelUserListFragmentProvider;
import com.fund.iam.ui.main.channel.CreateChannelFragmentProvider;
import com.fund.iam.ui.main.home.HomeEditFragmentProvider;
import com.fund.iam.ui.main.home.HomeFragmentProvider;
import com.fund.iam.ui.main.letterbox.LetterBoxFragmentProvider;
import com.fund.iam.ui.main.more.notice.NoticeFragmentProvider;
import com.fund.iam.ui.main.more.setting.SettingFragmentProvider;
import com.fund.iam.ui.main.more.setting.contact_us.ContactUsFragmentProvider;
import com.fund.iam.ui.main.more.setting.privacy.PrivacyFragmentProvider;
import com.fund.iam.ui.main.more.setting.update.UpdateFragmentProvider;
import com.fund.iam.ui.main.search.SearchFragmentProvider;
import com.fund.iam.ui.main.more.MoreFragmentProvider;

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
            HomeEditFragmentProvider.class,
            BookmarkFragmentProvider.class,
            SearchFragmentProvider.class,
            MoreFragmentProvider.class,
            NoticeFragmentProvider.class,
            SettingFragmentProvider.class,
            UpdateFragmentProvider.class,
            ContactUsFragmentProvider.class,
            PrivacyFragmentProvider.class,
            LetterBoxFragmentProvider.class,
            CreateChannelFragmentProvider.class,
            ChannelFragmentProvider.class,
            ChannelUserListFragmentProvider.class
    })
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract LetterActivity bindLetterActivity();
}
