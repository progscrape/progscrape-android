package com.progscrape.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.progscrape.R;
import com.progscrape.modules.Injector;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoriesView extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.swipe_refresh)
    protected SwipeRefreshLayout refresh;

    @InjectView(R.id.story_recycler)
    protected RecyclerView stories;

    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;

    public StoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            Injector.obtain(context).inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        toolbar.setNavigationIcon(R.drawable.menu_icon);

        refresh.setOnRefreshListener(this);

        stories.setLayoutManager(new LinearLayoutManager(getContext()));
        stories.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(new TextView(getContext()));
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.itemView.setText("Position " + position);
            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            itemView.setText("NEW");
            this.itemView = itemView;
        }
    }
}
