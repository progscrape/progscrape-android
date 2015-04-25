package com.progscrape.modules;

import android.app.Activity;

import com.progscrape.MainActivity;
import com.progscrape.ui.BrowserView;
import com.progscrape.ui.StoriesView;
import com.progscrape.ui.TopTagsView;
import com.progscrape.ui.TrendingTagsView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        addsTo = AppModule.class,
        injects = {
                TrendingTagsView.class,
                StoriesView.class,
                BrowserView.class,
                TopTagsView.class
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
    MainActivity mainActivity() {
        return activity;
    }

    @Provides
    @Singleton
    Activity activity() {
        return activity;
    }
}
