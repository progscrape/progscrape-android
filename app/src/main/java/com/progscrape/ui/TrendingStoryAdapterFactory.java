package com.progscrape.ui;

import android.content.Context;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.progscrape.data.Data;
import com.progscrape.data.Story;

import java.util.List;

import javax.inject.Inject;

public class TrendingStoryAdapterFactory {
    private Data data;

    @Inject
    public TrendingStoryAdapterFactory(Data data) {
        this.data = data;
    }

    public TrendingStoryAdapter create(Context context) {
        return new TrendingStoryAdapter(context, data);
    }
}
