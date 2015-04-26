package com.progscrape.app.data;

import rx.Observable;

/**
 * The raw REST source for the feed data.
 */
public interface RestSource {
    Observable<Feed> defaultFeed(CacheMode cache);

    Observable<Feed> search(CacheMode cache, String query);
}
