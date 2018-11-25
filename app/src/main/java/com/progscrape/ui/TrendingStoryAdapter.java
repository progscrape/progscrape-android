package com.progscrape.ui;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.progscrape.R;
import com.progscrape.app.data.Story;
import com.progscrape.event.StoryEvent;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

public class TrendingStoryAdapter extends RecyclerView.Adapter<TrendingStoryAdapter.ViewHolder> {
    private boolean loaded;
    private Context context;
    private Bus bus;
    private List<Story> stories = new ArrayList<>();
    private boolean errorState;

    public TrendingStoryAdapter(Context context, Bus bus) {
        this.context = context;
        this.bus = bus;
        loaded = false;
    }

    @Override
    public int getItemViewType(int position) {
        return loaded ? 1 : 0;
    }

    @Override
    public TrendingStoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StoryView view = (StoryView) LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrendingStoryAdapter.ViewHolder holder, int position) {
        if (loaded) {
            holder.bind(stories.get(position));
        } else {
            Story story = new Story();
            story.setTitle(errorState ? "Failed to load stories" : "Loading...");
            holder.bind(story);
        }
    }

    @Override
    public int getItemCount() {
        return loaded ? stories.size() : 1;
    }

    public void setStories(List<Story> stories) {
        loaded = true;
        this.stories = stories;
        notifyDataSetChanged();
    }

    public void setErrorState() {
        loaded = false;
        errorState = true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private StoryView itemView;
        private Story story;

        public ViewHolder(final StoryView itemView) {
            super(itemView);
            this.itemView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (story.getHref() != null)
                        bus.post(new StoryEvent(story, StoryEvent.What.ACTIVATE));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (story.getHref() != null)
                        bus.post(new StoryEvent(story, v, StoryEvent.What.MENU));
                    return true;
                }
            });
        }

        public void bind(Story story) {
            this.story = story;
            itemView.setHasHref(story.getHref() != null);
            itemView.setTags(story.getTags());
            itemView.setText(story.getTitle());
            itemView.setIconsVisible(story.getHackerNewsUrl() != null,
                    story.getRedditUrl() != null, story.getLobstersUrl() != null);
        }
    }
}
