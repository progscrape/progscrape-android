package com.progscrape.data;

import com.grack.nanojson.JsonObject;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import javax.inject.Inject;

public class Data {
    private RequestExecutor spiceManager;
    private FeedParser feedParser;

    @Inject
    public Data(RequestExecutor spiceManager, FeedParser feedParser) {
        this.spiceManager = spiceManager;
        this.feedParser = feedParser;
    }

    public void getTopTags(final RequestListener<List<String>> listener, boolean force) {
        spiceManager.execute(new FeedRequest(), new RequestListener<JsonObject>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(JsonObject feed) {
                listener.onRequestSuccess(feedParser.parse(feed).getTopTags());
            }
        }, force);
    }

    public void getStoryData(final RequestListener<List<Story>> listener, boolean force) {
        spiceManager.execute(new FeedRequest(), new RequestListener<JsonObject>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(JsonObject feed) {
                listener.onRequestSuccess(feedParser.parse(feed).getStories());
            }
        }, force);
    }
}
