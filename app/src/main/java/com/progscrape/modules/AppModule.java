package com.progscrape.modules;

import com.progscrape.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    App provideApplication() {
        return app;
    }
}
