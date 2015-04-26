package com.progscrape.data;

import com.grack.nanojson.JsonObject;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.progscrape.app.data.Story;

import java.util.List;

import javax.inject.Inject;

public class Data {
    private RequestExecutor executor;
    private FeedParser feedParser;

    @Inject
    public Data(RequestExecutor executor, FeedParser feedParser) {
        this.executor = executor;
        this.feedParser = feedParser;
    }

    public void getTopTags(final RequestListener<List<String>> listener, boolean force) {
        executor.execute(new FeedRequest(null), new RequestListener<JsonObject>() {
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

    public void getStoryData(String query, final RequestListener<List<Story>> listener, boolean force) {
        executor.execute(new FeedRequest(query), new RequestListener<JsonObject>() {
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
