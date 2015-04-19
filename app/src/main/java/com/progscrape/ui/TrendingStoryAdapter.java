package com.progscrape.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                // TODO
            }

            @Override
            public void onRequestSuccess(List<Story> res) {
                stories = res;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public TrendingStoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.story_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrendingStoryAdapter.ViewHolder holder, int position) {
        holder.itemView.setText(stories.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
