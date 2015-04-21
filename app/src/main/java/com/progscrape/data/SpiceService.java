package com.progscrape.data;

import android.app.Application;
import android.content.Intent;

import com.octo.android.robospice.okhttp.OkHttpSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SpiceService extends OkHttpSpiceService {
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        // TODO: This should be injected via dagger
        cacheManager.addPersister(new FileJsonCacheFactory(application).create());
        return cacheManager;
    }
}
