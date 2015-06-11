package com.progscrape.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.progscrape.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoriesFragment extends Fragment {
    @InjectView(R.id.stories)
    StoriesView stories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stories, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this, getView());
        stories.setTag(getArguments().getString("tag"));
    }

    public static StoriesFragment create(String tag) {
        StoriesFragment fragment = new StoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        fragment.setArguments(bundle);
        return fragment;
    }
}
