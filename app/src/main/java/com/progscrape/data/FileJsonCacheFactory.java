package com.progscrape.data;

import android.app.Application;

import com.grack.nanojson.JsonObject;
import com.octo.android.robospice.persistence.Persister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;
import com.octo.android.robospice.persistence.file.InFileObjectPersisterFactory;

import java.io.File;

import javax.inject.Inject;

public class FileJsonCacheFactory {
    private Application application;

    @Inject
    public FileJsonCacheFactory(Application application) {
        this.application = application;
    }

    public FileJsonCache create() throws CacheCreationException {
        return new FileJsonCache(application, JsonObject.class, new File(application.getCacheDir(), "json"));
    }
}
