package com.progscrape;

import android.app.Application;
import android.support.annotation.NonNull;

import com.progscrape.modules.AppModule;
import com.progscrape.modules.Injector;

import dagger.ObjectGraph;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public final class App extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            // TODO Crashlytics.start(this);
            // TODO Timber.plant(new CrashlyticsTree());
        }

        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if (Injector.matchesService(name))
            return objectGraph;

        return super.getSystemService(name);
    }
}
