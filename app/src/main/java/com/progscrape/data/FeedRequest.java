package com.progscrape.data;

import android.util.Log;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;
import com.squareup.okhttp.OkUrlFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class FeedRequest extends OkHttpSpiceRequest<JsonObject> implements Request {
    private String query;

    public FeedRequest(String query) {
        super(JsonObject.class);
        this.query = query;
    }

    public String getCacheKey() {
        return query == null ? "feed" : "feed-" + query;
    }

    @Override
    public long getCacheDuration() {
        if (query == null)
            return TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES);

        // Shorter cache for tag queries
        return TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
    }

    @Override
    public JsonObject loadDataFromNetwork() throws Exception {
        URL baseUrl = new URL("http://www.progscrape.com/feed.json"
                + (query == null ? "" : "?search=" + URLEncoder.encode(query, "UTF-8")));
        Log.d("feed", "Making request to " + baseUrl);
        HttpURLConnection conn = getUrlFactory().open(baseUrl);

        try (InputStream in = conn.getInputStream()) {
            JsonObject rawFeed = JsonParser.object().from(in);
            Log.d("feed", "Successfully retrieved feed");
            return rawFeed;
        }
    }

    private OkUrlFactory getUrlFactory() {
        return new OkUrlFactory(getOkHttpClient());
    }
}
