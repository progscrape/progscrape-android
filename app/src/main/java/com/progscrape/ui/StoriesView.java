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
import butterknife.OnItemClick;

public class StoriesView extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.swipe_refresh)
    protected SwipeRefreshLayout refresh;

    @InjectView(R.id.story_recycler)
    protected RecyclerView stories;

    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;

    @Inject
    protected TrendingStoryAdapterFactory storiesAdapterFactory;

    public StoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            Injector.obtain(context).inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode())
            return;

        ButterKnife.inject(this);
        toolbar.setNavigationIcon(R.drawable.menu_icon);

        refresh.setOnRefreshListener(this);

        stories.setLayoutManager(new LinearLayoutManager(getContext()));
        stories.setAdapter(storiesAdapterFactory.create(getContext()));

        stories.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                refresh.setEnabled(stories.getScrollY() == 0);
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
    }
}
