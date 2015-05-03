package com.progscrape.data;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.progscrape.app.data.Story;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FeedParser {
    @Inject
    public FeedParser() {
    }

    public Feed parse(JsonObject json) {
        if (json.getInt("v") == 1) {
            JsonArray rawTags = json.getArray("tags");
            JsonArray rawStories = json.getArray("stories");

            List<Story> stories = new ArrayList<>();
            for (Object obj : rawStories) {
                JsonObject rawStory = (JsonObject) obj;
                Story story = new Story();
                story.setTitle(rawStory.getString("title"));
                story.setHref(rawStory.getString("href"));
                story.setTags(toStringList(rawStory.getArray("tags")));
                story.setHackerNewsUrl(rawStory.getString("hnews"));
                story.setRedditUrl(rawStory.getString("reddit"));
                story.setLobstersUrl(rawStory.getString("lobsters"));

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

    private List<String> toStringList(JsonArray tags) {
        List<String> list = new ArrayList<>();
        for (Object tag : tags)
            list.add((String) tag);
        return list;
    }
}
