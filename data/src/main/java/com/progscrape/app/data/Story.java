package com.progscrape.app.data;

import java.io.Serializable;
import java.util.List;

/**
 * A progscrape story: title, href, tags and scrape location.
 */
public class Story {
    private String title;
    private String href;
    private List<String> tags;
    private String redditUrl;
    private String hackerNewsUrl;
    private String lobstersUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getRedditUrl() {
        return redditUrl;
    }

    public void setRedditUrl(String redditUrl) {
        this.redditUrl = redditUrl;
    }

    public String getHackerNewsUrl() {
        return hackerNewsUrl;
    }

    public void setHackerNewsUrl(String hackerNewsUrl) {
        this.hackerNewsUrl = hackerNewsUrl;
    }

    public String getLobstersUrl() {
        return lobstersUrl;
    }

    public void setLobstersUrl(String lobstersUrl) {
        this.lobstersUrl = lobstersUrl;
    }
}
