package com.progscrape.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.progscrape.R;
import com.progscrape.app.data.Story;
import com.progscrape.event.StoryEvent;
import com.progscrape.modules.Injector;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class WebViewFragment extends Fragment {
    @BindView(R.id.browserview)
    BrowserView browser;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.menu)
    View menu;

    @Inject
    protected Bus bus;

    private String href, title;
    private Story story;

    public static WebViewFragment newInstance(Story story) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("href", story.getHref());
        args.putString("title", story.getTitle());
        args.putSerializable("story", story);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        href = (String) args.get("href");
        title = (String) args.get("title");
        story = (Story) args.getSerializable("story");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("href", href);
        outState.putString("title", title);
        outState.putSerializable("story", story);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            href = savedInstanceState.getString("href");
            title = savedInstanceState.getString("title");
            story = (Story) savedInstanceState.getSerializable("story");
        }

        browser.setPage(href, title);
    }

    @OnClick(R.id.back)
    public void onClickBack() {
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.menu)
    public void onClickMenu() {
        // Be defensive if we don't have a story here for some reason
        if (story != null) {
            bus.post(new StoryEvent(story, menu, StoryEvent.What.STORY_MENU));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        browser.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        browser.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.webview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        Injector.obtain(getActivity(), ActivityComponent.class).inject(this);
    }
}