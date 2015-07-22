package com.progscrape;

import android.app.Application;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import com.progscrape.modules.AppModule;
import com.progscrape.modules.Injector;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public final class App extends Application {
    private AppComponent appComponent;

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

        GlobalComponent globalComponent = DaggerGlobalComponent.builder().build();
        appComponent = globalComponent.appComponent(new AppModule(this));
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if (Injector.matchesService(name))
            return appComponent;

        return super.getSystemService(name);
    }
}
