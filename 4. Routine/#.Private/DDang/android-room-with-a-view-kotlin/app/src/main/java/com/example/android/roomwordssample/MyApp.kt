package com.example.android.roomwordssample

import android.app.Application
import com.facebook.stetho.Stetho

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}