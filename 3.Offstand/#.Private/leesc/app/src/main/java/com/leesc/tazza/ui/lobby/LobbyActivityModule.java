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

package com.leesc.tazza.ui.lobby;


import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;

import com.leesc.tazza.data.DataManager;
import com.leesc.tazza.di.provider.ResourceProvider;
import com.leesc.tazza.receiver.WifiDirectReceiver;
import com.leesc.tazza.service.WifiP2pService;
import com.leesc.tazza.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class LobbyActivityModule {

    @Provides
    LobbyViewModel provideLobbyViewModel(DataManager dataManager,
                                         SchedulerProvider schedulerProvider,
                                         Context context,
                                         WifiP2pManager wifiP2pManager,
                                         WifiP2pManager.Channel channel,
                                         WifiP2pService wifiP2pService,
                                         WifiDirectReceiver wifiDirectReceiver,
                                         ResourceProvider resourceProvider
    ) {
        return new LobbyViewModel(dataManager, schedulerProvider, context, wifiP2pManager, channel, wifiP2pService, wifiDirectReceiver, resourceProvider);
    }
}
