package com.leesc.tazza.ui.splash;


import android.content.Context;
import android.util.Log;

import com.leesc.tazza.R;
import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.di.provider.ResourceProvider;
import com.leesc.tazza.ui.base.BaseViewModel;
import com.leesc.tazza.utils.rx.SchedulerProvider;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import pl.droidsonroids.gif.GifDrawable;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, Context context, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider);
        Log.d("lsc", "SplashViewModel constructor");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("lsc", "SplashViewModel onCleared");
    }
}
