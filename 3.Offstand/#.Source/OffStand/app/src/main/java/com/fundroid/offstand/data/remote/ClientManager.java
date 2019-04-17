package com.fundroid.offstand.data.remote;

import com.fundroid.offstand.data.model.Attendee;

import io.reactivex.Completable;

public class ClientManager {
    public static Completable sendMyInfo(Attendee attendee) {
        ConnectionManager.sendMessageCompletable(attendee).subscribe();
        return Completable.complete();
    }
}
