package com.progscrape.ui;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.progscrape.MainActivity;
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

    @InjectView(R.id.toolbar_title)
    protected TextView title;

    @Inject
    protected TrendingStoryAdapterFactory storiesAdapterFactory;

    @Inject
    protected MainActivity activity;

    @Icicle
    Parcelable scrollPos = null;

    private TrendingStoryAdapter adapter;

    public StoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            Injector.obtain(context, ActivityComponent.class).inject(this);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        scrollPos = getLayoutManager().onSaveInstanceState();
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
                    if (scrollPos != null)
                        getLayoutManager().onRestoreInstanceState(scrollPos);
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
        if (tag != null) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            title.setText(tag);
            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.getFragmentManager().popBackStack();
                }
            });
        } else {
            DrawerArrowDrawable drawable = new DrawerArrowDrawable(getContext()) {
                @Override
                boolean isLayoutRtl() {
                    return ViewCompat.getLayoutDirection(StoriesView.this)
                            == ViewCompat.LAYOUT_DIRECTION_RTL;
                }
            };

            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.openDrawer();
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        adapter.refresh(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        });
    }

    private LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager)stories.getLayoutManager();
    }
}
