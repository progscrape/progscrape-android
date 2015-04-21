package com.progscrape.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.progscrape.R;
import com.progscrape.data.Data;
import com.progscrape.data.Story;

import java.util.ArrayList;
import java.util.List;

public class TrendingStoryAdapter extends RecyclerView.Adapter<TrendingStoryAdapter.ViewHolder> {
    private Context context;
    private Data data;
    private List<Story> stories = new ArrayList<>();

    public TrendingStoryAdapter(Context context, Data data) {
        this.context = context;
        this.data = data;

        data.getStoryData(new RequestListener<List<Story>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e("stories", "Failed to retrieve stories", spiceException);
            }

            @Override
            public void onRequestSuccess(List<Story> res) {
                stories = res;
                notifyDataSetChanged();
            }
        }, false);
    }

    @Override
    public TrendingStoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StoryView view = (StoryView) LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrendingStoryAdapter.ViewHolder holder, int position) {
        holder.itemView.setText(stories.get(position).getTitle());
        holder.itemView.setIconsVisible(stories.get(position).getHackerNewsUrl() != null,
                stories.get(position).getRedditUrl() != null);
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private StoryView itemView;

        public ViewHolder(StoryView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
