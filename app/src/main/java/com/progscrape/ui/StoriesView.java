package com.progscrape.ui;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.progscrape.R;
import com.progscrape.modules.Injector;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icepick;
import icepick.Icicle;

public class StoriesView extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.swipe_refresh)
    protected SwipeRefreshLayout refresh;

    @InjectView(R.id.story_recycler)
    protected RecyclerView stories;

    @InjectView(R.id.toolbar)
    protected Toolbar toolbar;

    @Inject
    protected TrendingStoryAdapterFactory storiesAdapterFactory;

    @Icicle
    int scrollPos = -1;

    private TrendingStoryAdapter adapter;

    public StoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            Injector.obtain(context).inject(this);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        scrollPos = ((LinearLayoutManager)stories.getLayoutManager()).findFirstVisibleItemPosition();
        Log.i("stories", "Saving scroll position: " + scrollPos);
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        Log.i("stories", "Loading scroll position: " + scrollPos);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode())
            return;

        ButterKnife.inject(this);
        toolbar.setNavigationIcon(R.drawable.menu_icon);

        refresh.setOnRefreshListener(this);

        adapter = storiesAdapterFactory.create(getContext());
        stories.setAdapter(adapter);
        stories.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            boolean first = true;

            @Override
            public void onChanged() {
                Log.i("stories", "onChanged, scrollPos = " + scrollPos + ", first = " + first);
                if (first) {
                    first = false;
                    if (scrollPos != -1)
                        stories.scrollToPosition(scrollPos);
                }
            }
        });
        stories.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                refresh.setEnabled(stories.getScrollY() == 0);
            }
        });
    }

    public void setTag(String tag) {
        adapter.setTag(tag);
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);
    }
}
