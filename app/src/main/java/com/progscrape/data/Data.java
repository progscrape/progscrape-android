package com.progscrape.data;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.okhttp.OkHttpSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import javax.inject.Inject;

public class Data {
    private SpiceManager spiceManager;

    @Inject
    public Data(SpiceManager spiceManager) {
        this.spiceManager = spiceManager;
    }

    public void getTopTags(RequestListener<List<String>> listener) {

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

    public static void main(String[] args) {
        SpiceManager spiceManager = new SpiceManager(OkHttpSpiceService.class);
        Data data = new Data(spiceManager);
        data.getStoryData(new RequestListener<List<Story>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(List<Story> stories) {
                System.out.println(stories);
            }
        });
    }
}
