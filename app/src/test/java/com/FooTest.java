package com;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;
import com.progscrape.BuildConfig;
import com.progscrape.app.data.Story;
import com.progscrape.data.Data;
import com.progscrape.data.FeedParser;
import com.progscrape.data.RequestExecutor;
import com.squareup.okhttp.OkHttpClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21, manifest = Config.NONE)
public class FooTest {
    @Test
    public void testHi() throws InterruptedException {
        final OkHttpClient client = new OkHttpClient();
        RequestExecutor executor = new RequestExecutor() {
            @Override
            public <T> void execute(OkHttpSpiceRequest<T> req, RequestListener<T> listener, boolean force) {
                req.setOkHttpClient(client);
                try {
                    listener.onRequestSuccess(req.loadDataFromNetwork());
                } catch (Exception e) {
                    listener.onRequestFailure(new SpiceException(e));
                }
            }
        };
        Data data = new Data(executor, new FeedParser());
        final CountDownLatch latch = new CountDownLatch(1);

        data.getStoryData(null, new RequestListener<List<Story>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                System.out.println(spiceException);
                latch.countDown();
            }

            @Override
            public void onRequestSuccess(List<Story> stories) {
                System.out.println(stories);
                latch.countDown();
            }
        }, false);

        if (!latch.await(10, TimeUnit.SECONDS))
            fail("Timeout");
    }
}