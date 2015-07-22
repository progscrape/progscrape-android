package com.progscrape.ui;

import android.content.Context;

import com.progscrape.MainActivity;
import com.progscrape.data.Data;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class TrendingStoryAdapterFactory {
    private Bus bus;

    @Inject
    public TrendingStoryAdapterFactory(Bus bus) {
        this.bus = bus;
    }

    public TrendingStoryAdapter create(Context context) {
        return new TrendingStoryAdapter(context, bus);
    }
}
