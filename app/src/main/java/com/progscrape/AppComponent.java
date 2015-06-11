package com.progscrape;

import com.progscrape.modules.AppModule;
import com.progscrape.modules.MainActivityModule;
import com.progscrape.ui.ActivityComponent;

import javax.inject.Singleton;

import dagger.Subcomponent;

@Singleton @Subcomponent(modules = AppModule.class)
public interface AppComponent {
    ActivityComponent plus(MainActivityModule module);
}
