package com.progscrape.data;

import java.util.List;

public class Feed {
    private List<String> topTags;
    private List<Story> stories;

    public List<String> getTopTags() {
        return topTags;
    }

    public void setTopTags(List<String> topTags) {
        this.topTags = topTags;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
