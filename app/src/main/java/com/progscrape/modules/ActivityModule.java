package com.progscrape.modules;

import android.app.Activity;

import com.octo.android.robospice.SpiceManager;
import com.progscrape.BaseActivity;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ActivityModule<T extends BaseActivity> {
    protected T activity;

    public ActivityModule(T activity) {
        this.activity = activity;
    }

    @Provides @ActivityScope
    public SpiceManager spiceManager() {
        return activity.getSpiceManager();
    }

    @Provides @ActivityScope
    Activity activity() {
        return activity;
    }
}
