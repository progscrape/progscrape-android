package com.progscrape.modules;

import com.progscrape.MainActivity;
import com.progscrape.ui.StoriesView;
import com.progscrape.ui.TrendingTagsView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    addsTo = AppModule.class,
    injects = {
        TrendingTagsView.class,
        StoriesView.class
    },
    library = true
)
public class MainActivityModule {
    private MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    MainActivity provideMainActivity() {
        return activity;
    }
}
