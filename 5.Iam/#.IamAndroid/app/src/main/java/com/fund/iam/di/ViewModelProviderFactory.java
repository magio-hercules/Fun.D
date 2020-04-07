package com.fund.iam.di;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fund.iam.data.DataManager;
import com.fund.iam.di.provider.ResourceProvider;
import com.fund.iam.di.provider.SchedulerProvider;
import com.fund.iam.ui.intro.IntroViewModel;
import com.fund.iam.ui.letter.LetterViewModel;
import com.fund.iam.ui.login.LoginViewModel;
import com.fund.iam.ui.main.MainViewModel;
import com.fund.iam.ui.main.bookmark.BookmarkViewModel;
import com.fund.iam.ui.main.channel.CreateChannelViewModel;
import com.fund.iam.ui.main.home.HomeViewModel;
import com.fund.iam.ui.main.letterbox.LetterBoxViewModel;
import com.fund.iam.ui.main.more.MoreViewModel;
import com.fund.iam.ui.main.more.notice.NoticeViewModel;
import com.fund.iam.ui.main.more.setting.SettingViewModel;
import com.fund.iam.ui.main.search.SearchViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;
    private final DataManager dataManager;
    private final SchedulerProvider schedulerProvider;
    private final ResourceProvider resourceProvider;

    @Inject
    public ViewModelProviderFactory(Context context, DataManager dataManager, SchedulerProvider schedulerProvider, ResourceProvider resourceProvider) {
        this.context = context;
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.resourceProvider = resourceProvider;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(IntroViewModel.class)) {
            return (T) new IntroViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(context, dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(MoreViewModel.class)) {
            return (T) new MoreViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(BookmarkViewModel.class)) {
            return (T) new BookmarkViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(LetterBoxViewModel.class)) {
            return (T) new LetterBoxViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(LetterViewModel.class)) {
            return (T) new LetterViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(NoticeViewModel.class)) {
            return (T) new NoticeViewModel(dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(SettingViewModel.class)) {
            return (T) new SettingViewModel(context, dataManager, schedulerProvider, resourceProvider);
        } else if (modelClass.isAssignableFrom(CreateChannelViewModel.class)) {
            return (T) new CreateChannelViewModel(dataManager, schedulerProvider, resourceProvider);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
