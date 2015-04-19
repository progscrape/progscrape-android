package com.progscrape.app.data;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.functions.Action1;

import static org.junit.Assert.assertNotNull;

public class ModelTest {
    @Test(timeout = 10000)
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Model model = new Model(null, null);
        model.stories().subscribe(s -> {
            assertNotNull(s);
            latch.countDown();
        });

        latch.await();
    }
}
