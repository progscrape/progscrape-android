package com.progscrape.modules;

import android.app.Application;

import com.progscrape.App;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        includes = {
                UiModule.class,
                DataModule.class
        },
        injects = {
                App.class
        },
        library = true
)
public final class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }
}
