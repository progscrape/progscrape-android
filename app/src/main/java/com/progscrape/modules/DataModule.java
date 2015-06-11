package com.progscrape.modules;

import com.octo.android.robospice.SpiceManager;
import com.progscrape.data.RequestExecutor;
import com.progscrape.data.SpiceRequestExecutor;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    @Provides
    @ActivityScope
    public RequestExecutor requestExecutor(SpiceManager mgr) {
        return new SpiceRequestExecutor(mgr);
    }
}
