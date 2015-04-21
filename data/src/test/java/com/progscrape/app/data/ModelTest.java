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

    @Test
    public void test2() throws Exception {
    }
}
