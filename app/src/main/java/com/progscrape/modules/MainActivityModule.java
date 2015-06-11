package com.progscrape.modules;

import com.progscrape.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule extends ActivityModule<MainActivity> {
    public MainActivityModule(MainActivity activity) {
        super(activity);
    }

    @Provides
    @ActivityScope
    MainActivity mainActivity() {
        return activity;
    }
}
