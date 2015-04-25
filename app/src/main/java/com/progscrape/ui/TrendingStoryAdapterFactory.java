package com.progscrape.ui;

import android.content.Context;

import com.progscrape.MainActivity;
import com.progscrape.data.Data;

import javax.inject.Inject;

public class TrendingStoryAdapterFactory {
    private Data data;
    private MainActivity activity;

    @Inject
    public TrendingStoryAdapterFactory(Data data, MainActivity activity) {
        this.data = data;
        this.activity = activity;
    }

    public TrendingStoryAdapter create(Context context) {
        return new TrendingStoryAdapter(context, data, activity);
    }
}
