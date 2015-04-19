package com.progscrape.modules;

import android.app.Application;

import com.octo.android.robospice.SpiceManager;
import com.progscrape.App;
import com.progscrape.data.RequestExecutor;
import com.progscrape.data.SpiceRequestExecutor;
import com.progscrape.data.SpiceService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true, complete = false)
public class DataModule {
    @Provides
    @Singleton
    public SpiceManager spiceManager(Application app) {
        SpiceManager manager = new SpiceManager(SpiceService.class);
        manager.start(app);
        return manager;
    }

    @Provides
    @Singleton
    public RequestExecutor requestExecutor(SpiceManager mgr) {
        return new SpiceRequestExecutor(mgr);
    }
}
