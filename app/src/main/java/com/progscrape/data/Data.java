package com.progscrape.data;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import javax.inject.Inject;

public class Data {
    private RequestExecutor spiceManager;

    @Inject
    public Data(RequestExecutor spiceManager) {
        this.spiceManager = spiceManager;
    }

    public void getTopTags(final RequestListener<List<String>> listener) {
        spiceManager.execute(new FeedRequest(), new RequestListener<Feed>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(Feed feed) {
                listener.onRequestSuccess(feed.getTopTags());
            }
        });
    }

    public void getStoryData(final RequestListener<List<Story>> listener) {
        spiceManager.execute(new FeedRequest(), new RequestListener<Feed>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(Feed feed) {
                listener.onRequestSuccess(feed.getStories());
            }
        });
    }
}
