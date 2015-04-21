package com.progscrape.data;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;
import com.squareup.okhttp.OkUrlFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FeedRequest extends OkHttpSpiceRequest<Feed> implements Request {
    public FeedRequest() {
        super(Feed.class);
    }

    public String getCacheKey() {
        return "feed";
    }

    @Override
    public long getCacheDuration() {
        return TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES);
    }

    @Override
    public Feed loadDataFromNetwork() throws Exception {
        URL baseUrl = new URL("http://www.progscrape.com/feed.json");
        HttpURLConnection conn = getUrlFactory().open(baseUrl);

        try (InputStream in = conn.getInputStream()) {
            JsonObject rawFeed = JsonParser.object().from(in);
            if (rawFeed.getInt("v") == 1) {
                JsonArray rawTags = rawFeed.getArray("tags");
                JsonArray rawStories = rawFeed.getArray("stories");

                List<Story> stories = new ArrayList<>();
                for (Object obj : rawStories) {
                    JsonObject rawStory = (JsonObject) obj;
                    Story story = new Story();
                    story.setTitle(rawStory.getString("title"));
                    story.setHref(rawStory.getString("href"));
                    story.setTags(toStringList(rawStory.getArray("tags")));
                    story.setHackerNewsUrl(rawStory.getString("hnews"));
                    story.setRedditUrl(rawStory.getString("reddit"));

                    stories.add(story);
                }

                Feed feed = new Feed();
                feed.setStories(stories);
                feed.setTopTags(toStringList(rawTags));
                return feed;
            } else {
                throw new IllegalArgumentException("Unexpected feed version");
            }
        }
    }

    private List<String> toStringList(JsonArray tags) {
        List<String> list = new ArrayList<>();
        for (Object tag : tags)
            list.add((String) tag);
        return list;
    }

    private OkUrlFactory getUrlFactory() {
        return new OkUrlFactory(getOkHttpClient());
    }
}
