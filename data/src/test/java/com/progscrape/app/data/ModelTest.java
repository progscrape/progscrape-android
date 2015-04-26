package com.progscrape.app.data;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;

public class ModelTest {
    @Test(timeout = 10000)
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        Model model = new Model(new RestSource() {
            @Override
            public Observable<Feed> defaultFeed(CacheMode cache) {
                return Observable.from(new Feed[]{new Feed()});
            }

            @Override
            public Observable<Feed> search(CacheMode cache, String query) {
                return Observable.from(new Feed[]{new Feed()});
            }
        }, Schedulers.immediate());

        model.stories().getNetwork().subscribe(s -> {
            assertNotNull(s);
            latch.countDown();
        });
        model.stories().getData().subscribe(s -> {
            assertNotNull(s);
            latch.countDown();
        });

        latch.await();
    }

    @Test
    public void test2() throws Exception {
    }
}
