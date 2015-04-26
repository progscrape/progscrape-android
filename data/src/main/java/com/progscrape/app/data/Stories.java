package com.progscrape.app.data;

import java.util.List;

public class Stories {
    private List<Story> stories;

    public Stories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getStories() {
        return stories;
    }
}
