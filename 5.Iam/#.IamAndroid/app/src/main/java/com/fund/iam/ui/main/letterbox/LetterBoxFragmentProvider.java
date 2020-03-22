package com.fund.iam.ui.main.letterbox;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LetterBoxFragmentProvider {

    @ContributesAndroidInjector
    abstract LetterBoxFragment provideLetterBoxFragmentFactory();
}
