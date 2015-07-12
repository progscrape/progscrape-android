package com.progscrape.ui;

import com.progscrape.MainActivity;
import com.progscrape.modules.ActivityScope;
import com.progscrape.modules.DataModule;
import com.progscrape.modules.MainActivityModule;

import dagger.Subcomponent;

@Subcomponent(modules = { DataModule.class, MainActivityModule.class }) @ActivityScope
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(StoriesFragment storiesFragment);

    void inject(BrowserView browserView);

    void inject(TopTagsView topTagsView);

    void inject(StoriesView storiesView);
}
