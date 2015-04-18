package com.progscrape.modules;

import android.view.ViewGroup;

import com.progscrape.MainActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class
        },
        complete = false,
        library = true
)
public class UiModule {
    @Provides
    @Singleton
    public ViewGroup root() {
        return null;
    }
}
