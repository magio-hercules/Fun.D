package com.leesc.tazza.ui.base;


import android.util.Log;

import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.utils.rx.SchedulerProvider;

import java.lang.ref.WeakReference;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;


public abstract class BaseViewModel<N> extends ViewModel {

    private final DataManager mDataManager;

    private final ObservableBoolean mIsLoading = new ObservableBoolean(false);

    private final SchedulerProvider mSchedulerProvider;

    private CompositeDisposable mCompositeDisposable;

    private WeakReference<N> mNavigator;

    public BaseViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.set(isLoading);
    }

    public N getNavigator() {
        Log.d("lsc","getNavigator " + (mNavigator == null));
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        Log.d("lsc","setNavigator");
        this.mNavigator = new WeakReference<>(navigator);
        Log.d("lsc","setNavigator " + (mNavigator == null));
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }
}
