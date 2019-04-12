package com.leesc.tazza.data.remote;

import com.leesc.tazza.data.model.Attendee;

import io.reactivex.Completable;

public class ClientManager {
    public static Completable sendMyInfo(Attendee attendee) {
        ConnectionManager.sendMessageCompletable(attendee).subscribe();
        return Completable.complete();
    }
}
