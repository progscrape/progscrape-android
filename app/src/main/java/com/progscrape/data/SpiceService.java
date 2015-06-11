package com.progscrape.data;

import android.app.Application;

import com.octo.android.robospice.okhttp.OkHttpSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

public class SpiceService extends OkHttpSpiceService {
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        // TODO: This should be injected via dagger
        cacheManager.addPersister(new FileJsonCacheFactory(application).create());
        return cacheManager;
    }
}
