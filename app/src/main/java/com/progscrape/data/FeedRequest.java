package com.progscrape.data;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.octo.android.robospice.request.okhttp.OkHttpSpiceRequest;
import com.squareup.okhttp.OkUrlFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FeedRequest extends OkHttpSpiceRequest<Feed> {
    public FeedRequest() {
        super(Feed.class);
    }

    @Override
    public Feed loadDataFromNetwork() throws Exception {
        URL baseUrl = new URL("http://progscrape.com/feed.json");
        HttpURLConnection conn = getUrlFactory().open(baseUrl);

        try (InputStream in = conn.getInputStream()) {
            JsonObject rawFeed = JsonParser.object().from(in);
            if (rawFeed.getString("v").equals("1")) {
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
