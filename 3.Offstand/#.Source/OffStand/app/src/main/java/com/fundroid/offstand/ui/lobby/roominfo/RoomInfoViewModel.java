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

package com.fundroid.offstand.ui.lobby.roominfo;

import android.net.wifi.p2p.WifiP2pManager;

import com.fundroid.offstand.data.DataManager;
import com.fundroid.offstand.di.provider.ResourceProvider;
import com.fundroid.offstand.ui.base.BaseViewModel;
import com.fundroid.offstand.utils.rx.SchedulerProvider;

public class RoomInfoViewModel extends BaseViewModel<RoomInfoNavigator> {

    public RoomInfoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, ResourceProvider resourceProvider) {
        super(dataManager, schedulerProvider);
    }

    public void onLaterClick() {
        getNavigator().dismissDialog();
    }

    public void onSubmitClick() {
        getNavigator().dismissDialog();
    }
}
