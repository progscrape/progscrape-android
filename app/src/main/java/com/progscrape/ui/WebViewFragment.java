package com.progscrape.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.progscrape.R;
import com.progscrape.app.data.Story;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WebViewFragment extends Fragment {
    @InjectView(R.id.browserview)
    BrowserView browser;

    private String href, title;

    public static WebViewFragment newInstance(Story story) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("href", story.getHref());
        args.putString("title", story.getTitle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        href = (String) args.get("href");
        title = (String) args.get("title");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("href", href);
        outState.putString("title", title);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            href = savedInstanceState.getString("href");
            title = savedInstanceState.getString("title");
        }

        browser.setPage(href, title);
    }

    @OnClick(R.id.back)
    public void onClickBack() {
        getFragmentManager().popBackStack();
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
        ButterKnife.inject(this, getView());
    }
}
