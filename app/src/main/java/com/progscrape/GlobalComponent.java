package com.progscrape;

import com.progscrape.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * This is the top-level component in the application, containing global singletons.
 */
@Component
public interface GlobalComponent {
    AppComponent appComponent(AppModule appModule);
}
