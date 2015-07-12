package com.progscrape.ui;

import android.content.Context;

import com.progscrape.MainActivity;
import com.progscrape.data.Data;

import javax.inject.Inject;

public class TrendingStoryAdapterFactory {
    private MainActivity activity;

    @Inject
    public TrendingStoryAdapterFactory(MainActivity activity) {
        this.activity = activity;
    }

    public TrendingStoryAdapter create(Context context) {
        return new TrendingStoryAdapter(context, activity);
    }
}
