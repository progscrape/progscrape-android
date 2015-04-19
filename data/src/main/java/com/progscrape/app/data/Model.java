package com.progscrape.app.data;

import com.progscrape.app.rx.RefreshableObservable;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.subjects.PublishSubject;

public class Model {
    private RestSource rest;
    private Scheduler scheduler;
    private RefreshableObservable<Tags> tags;
    private RefreshableObservable<Stories> stories;
    private Observable<Object> feed;
    private Observable<Void> refresh;

    @Inject
    public Model(RestSource rest, Scheduler scheduler) {
        this.rest = rest;
        this.scheduler = scheduler;

        refresh = PublishSubject.create();
        refresh.observeOn(scheduler).forEach($ -> refresh());
    }

    private void refresh() {

    }

    public RefreshableObservable<Tags> topTags() {
        return tags;
    }

    public RefreshableObservable<Stories> stories() {
        if (stories == null) {
//            stories = RefreshableObservable.on(defaultFeed().map(feed -> new Stories(feed.getStories())));
        }
        return stories;
    }

    public RefreshableObservable<Stories> search(String query) {
        return null;
    }

    private Observable<Feed> defaultFeed() {
        if (feed == null) {
            feed = Observable.create(f -> {
                f.onNext(null);
            });

        }

        return null;//feed;
    }
}
