package com.progscrape.ui;

import android.content.Context;
import android.os.Parcelable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.progscrape.event.ActivityEvent;
import com.progscrape.R;
import com.progscrape.modules.Injector;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.BindView;
import icepick.Icepick;
import icepick.Icicle;

public class StoriesView extends LinearLayout {
    @BindView(R.id.story_recycler)
    protected RecyclerView stories;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    protected TextView title;

    @BindView(R.id.swipe_refresh)
    protected SwipeRefreshLayout refresh;

    @Inject
    protected TrendingStoryAdapterFactory storiesAdapterFactory;

    @Inject
    protected Bus bus;

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

        ButterKnife.bind(this);

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
        if (tag != null) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
            title.setText(tag);
            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    bus.post(ActivityEvent.POP_BACK);
                }
            });
        } else {
            DrawerArrowDrawable drawable = new DrawerArrowDrawable(getContext().getResources());

            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    bus.post(ActivityEvent.TOGGLE_DRAWER);
                }
            });
        }
    }

    private LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager)stories.getLayoutManager();
    }

    public void scrollToTop() {
        stories.smoothScrollToPosition(0);
    }
}
