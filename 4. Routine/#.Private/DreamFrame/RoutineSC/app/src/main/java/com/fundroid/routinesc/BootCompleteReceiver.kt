package com.fundroid.routinesc;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import java.util.concurrent.TimeUnit

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("lsc","부트 onReceive ${intent?.action}")
        }
    }
}
