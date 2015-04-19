package com.progscrape.app.rx;

import rx.Observable;

public final class RefreshableObservable<T> extends Observable<T> {
    private Observable<T> o;
    private Observable<?> refresh;

    protected RefreshableObservable(OnSubscribe<T> f) {
        super(f);
    }

    public static <T> RefreshableObservable<T> on(Observable<T> o, Observable<?> refresh) {
        RefreshableObservable<T> r = new RefreshableObservable<T>(null);
        r.o = o;
        r.refresh = refresh;
        return r;
    }

    public void refresh() {

    }


}
