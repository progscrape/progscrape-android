package com.progscrape.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "feed")
public class Feed {
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<String> topTags;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Story> stories;

    @DatabaseField(id = true)
    private String query;

    public Feed() {
        this("");
    }

    public Feed(String query) {
        this.query = query;
    }

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
