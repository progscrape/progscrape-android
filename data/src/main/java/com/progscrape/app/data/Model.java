package com.progscrape.app.data;

import com.progscrape.app.rx.RefreshableRemoteData;
import com.progscrape.app.rx.RemoteStatus;

import javax.inject.Inject;

import rx.Scheduler;
import rx.functions.Action1;
import rx.subjects.AsyncSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class Model {
    private RestSource rest;
    private Scheduler scheduler;
    private RefreshableRemoteData<Feed> feed;
    private RefreshableRemoteData<Tags> tags;
    private RefreshableRemoteData<Stories> stories;

    @Inject
    public Model(RestSource rest, Scheduler scheduler) {
        this.rest = rest;
        this.scheduler = scheduler;

        feed = getFeed(null);
        tags = feed.map($ -> new Tags($.getTopTags()));
        stories = feed.map($ -> new Stories($.getStories()));
    }

    private RefreshableRemoteData<Feed> getFeed(String tag) {
        ReplaySubject<RemoteStatus> statusSubject = ReplaySubject.createWithSize(1);
        ReplaySubject<Feed> feedSubject = ReplaySubject.createWithSize(1);
        PublishSubject<Void> refresh = PublishSubject.create();
        refresh.observeOn(scheduler).forEach($ -> {
            CacheMode mode = CacheMode.DISABLE_CACHE;
            fetch(tag, feedSubject, statusSubject, mode);
        });
        fetch(tag, feedSubject, statusSubject, CacheMode.ENABLE_CACHE);
        return new RefreshableRemoteData<>(feedSubject, statusSubject, refresh);
    }

    private void fetch(String tag, ReplaySubject<Feed> feedSubject, ReplaySubject<RemoteStatus> statusSubject, CacheMode mode) {
        statusSubject.onNext(RemoteStatus.LOADING);
        Action1<Feed> action = (x) -> {
            statusSubject.onNext(RemoteStatus.NOT_LOADING);
            feedSubject.onNext(x);
        };
        if (tag == null) {
            rest.defaultFeed(mode).subscribe(action);
        } else {
            rest.search(mode, tag).subscribe(action);
        }
    }

    public RefreshableRemoteData<Tags> topTags() {
        return tags;
    }

    public RefreshableRemoteData<Stories> stories() {
        return stories;
    }

    public RefreshableRemoteData<Stories> search(String tag) {
        return getFeed(tag).map($ -> new Stories($.getStories()));
    }

    private RefreshableRemoteData<Feed> defaultFeed() {
        return feed;
    }
}
