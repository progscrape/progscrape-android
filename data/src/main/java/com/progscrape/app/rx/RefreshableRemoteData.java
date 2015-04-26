package com.progscrape.app.rx;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.subjects.Subject;

public class RefreshableRemoteData<T> extends RemoteData<T> {
    private Subject<Void, Void> refresh;

    public RefreshableRemoteData(Observable<T> data, Observable<RemoteStatus> network, Subject<Void, Void> refresh) {
        super(data, network);
        this.refresh = refresh;
    }

    public void refresh() {
        refresh.onNext(null);
    }

    public <X> RefreshableRemoteData<X> map(Func1<T, X> fn) {
        return new RefreshableRemoteData<>(getData().map(fn), getNetwork(), refresh);
    }
}
