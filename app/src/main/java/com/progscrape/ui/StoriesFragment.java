package com.progscrape.ui;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.progscrape.R;
import com.progscrape.app.data.Story;
import com.progscrape.data.Data;
import com.progscrape.event.ActivityEvent;
import com.progscrape.modules.Injector;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StoriesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String tag;
    private int seq = 0;

    @InjectView(R.id.stories)
    StoriesView stories;

    @InjectView(R.id.story_recycler)
    protected RecyclerView storyRecycler;

    @InjectView(R.id.swipe_refresh)
    protected SwipeRefreshLayout refresh;

    @Inject
    protected Data data;

    @Inject
    protected Bus bus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stories, container, false);
    }

    @OnClick(R.id.toolbar)
    protected void onClickToolbar() {
        bus.post(ActivityEvent.SCROLL_TO_TOP);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this, getView());

        setTag(getArguments().getString("tag"));
        refresh.setOnRefreshListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Injector.obtain(getActivity(), ActivityComponent.class).inject(this);
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        refresh.setRefreshing(false);
        Log.i("stories", "StoriesFragment stopped");
    }

    @Override
    public void onPause() {
        super.onPause();
        refresh.setRefreshing(false);
        Log.i("stories", "StoriesFragment paused");
    }

    @Subscribe
    public void onActivityEvent(ActivityEvent what) {
        switch (what) {
            case SCROLL_TO_TOP:
                stories.scrollToTop();
                break;
        }
    }

    public static StoriesFragment create(String tag) {
        StoriesFragment fragment = new StoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    private TrendingStoryAdapter getTrendingStoryAdapter() {
        return (TrendingStoryAdapter)storyRecycler.getAdapter();
    }

    public void setTag(String tag) {
        stories.setTag(tag);
        this.tag = tag;
        update();
    }

    @Override
    public void onRefresh() {
        update();
    }

    private void update() {
        refresh.setRefreshing(true);

        final int curr = ++seq;
        data.getStoryData(tag, new RequestListener<List<Story>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (curr != seq) {
                    Log.i("stories", "Out of date request");
                    return;
                }

                refresh.setRefreshing(false);
                Log.e("stories", "Failed to retrieve stories", spiceException);
                Toast toast = Toast.makeText(getActivity(), "Failed to refresh stories, please try again.", Toast.LENGTH_SHORT);
                toast.show();
                getTrendingStoryAdapter().setErrorState();
            }

            @Override
            public void onRequestSuccess(List<Story> res) {
                if (curr != seq) {
                    Log.i("stories", "Out of date request");
                    return;
                }

                Log.i("stories", "Completed request");
                refresh.setRefreshing(false);
                getTrendingStoryAdapter().setStories(res);
            }
        }, true);
    }
}
