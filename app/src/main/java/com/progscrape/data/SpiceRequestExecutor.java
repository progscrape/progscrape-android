package com.progscrape.data;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpiceRequestExecutor implements RequestExecutor {
    private SpiceManager spiceManager;

    @Inject
    public SpiceRequestExecutor(SpiceManager spiceManager) {
        this.spiceManager = spiceManager;
    }

    @Override
    public <T> void execute(OkHttpSpiceRequest<T> req, RequestListener<T> listener) {
        spiceManager.execute(req, listener);
    }
}
