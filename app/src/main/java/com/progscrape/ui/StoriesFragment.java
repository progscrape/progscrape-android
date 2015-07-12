package com.progscrape.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
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
import com.progscrape.modules.Injector;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stories, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this, getView());
        Injector.obtain(getActivity(), ActivityComponent.class).inject(this);

        setTag(getArguments().getString("tag"));
        refresh.setOnRefreshListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        refresh.setRefreshing(false);
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

                refresh.setRefreshing(false);
                getTrendingStoryAdapter().setStories(res);
            }
        }, true);
    }
}
