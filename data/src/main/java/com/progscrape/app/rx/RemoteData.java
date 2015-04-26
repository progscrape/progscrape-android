package com.progscrape.app.rx;

import rx.Observable;
import rx.Observer;

public class RemoteData<T> {
    private final Observable<T> data;
    private final Observable<RemoteStatus> network;

    public RemoteData(Observable<T> data, Observable<RemoteStatus> network) {
        this.data = data;
        this.network = network;
    }

    public Observable<T> getData() {
        return data;
    }

    public Observable<RemoteStatus> getNetwork() {
        return network;
    }
}
