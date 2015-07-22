package com.progscrape.event;

public class SearchEvent {
    private final String tag;
    private final boolean initial;

    public SearchEvent(String tag, boolean initial) {
        this.tag = tag;
        this.initial = initial;
    }

    public String getTag() {
        return tag;
    }

    public boolean isInitial() {
        return initial;
    }
}
