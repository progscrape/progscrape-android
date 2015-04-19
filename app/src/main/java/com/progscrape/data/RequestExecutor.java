package com.progscrape.data;

import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;

public interface RequestExecutor {
    public <T> void execute(OkHttpSpiceRequest<T> req, RequestListener<T> listener);
}
