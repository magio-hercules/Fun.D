/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.fundroid.offstand.ui.lobby.main;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fundroid.offstand.R;
import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

public class MainViewModel extends BaseViewModel<MainNavigator> {

    private final Context context;

    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, Context context) {
        super(dataManager, schedulerProvider);
        this.context = context;
    }

    public void makeRoom() {
        getNavigator().makeRoom();
    }

    public void findRoom() {
        getNavigator().findRoom();
    }

    public void guide() {
        getNavigator().guide();
    }

    @SuppressLint("ClickableViewAccessibility")
    public View.OnTouchListener onTouchListener() {
        return (v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d("lsc", "ACTION_DOWN " + v.getId());
                    v.setPressed(true);
                    break;

                case MotionEvent.ACTION_UP:
                    switch (v.getId()) {
                        case R.id.btn_make_room:
                            Log.d("lsc", "ACTION_UP btn_make_room");

                            MediaPlayer.create(context, R.raw.abstract_click).start();

                            makeRoom();
                            break;

                        case R.id.btn_find_room:
                            Log.d("lsc", "ACTION_UP btn_find_room");

                            MediaPlayer.create(context, R.raw.abstract_click).start();

                            findRoom();
                            break;

                        case R.id.btn_guide:
                            Log.d("lsc", "ACTION_UP btn_guide");

                            MediaPlayer.create(context, R.raw.abstract_click).start();

                            guide();
                            break;
                    }
                    break;
            }
            return false;
        };

    }

    public void onNavBackClick() {
        getNavigator().goBack();
    }
}
