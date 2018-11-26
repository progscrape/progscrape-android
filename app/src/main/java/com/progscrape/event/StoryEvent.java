package com.progscrape.event;

import android.view.View;

import com.progscrape.app.data.Story;

public class StoryEvent {
    private final Story story;
    private View view;
    private final What what;

    public enum What {
        ACTIVATE,
        MENU,
        STORY_MENU,
    }

    public StoryEvent(Story story, What what) {
        this.story = story;
        this.what = what;
    }

    public StoryEvent(Story story, View view, What what) {
        this.story = story;
        this.view = view;
        this.what = what;
    }

    public View getView() {
        return view;
    }

    public Story getStory() {
        return story;
    }

    public What getWhat() {
        return what;
    }
}
