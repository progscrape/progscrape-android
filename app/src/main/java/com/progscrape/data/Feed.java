package com.progscrape.data;

import com.progscrape.app.data.Story;

import java.util.ArrayList;
import java.util.List;

public class Feed {
    private ArrayList<String> topTags;

    private ArrayList<Story> stories;

    public List<String> getTopTags() {
        return topTags;
    }

    public void setTopTags(List<String> topTags) {
        this.topTags = new ArrayList<>(topTags);
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = new ArrayList<>(stories);
    }
}
