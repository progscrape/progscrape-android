package com.progscrape.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.progscrape.R;
import com.progscrape.app.data.Story;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WebViewFragment extends Fragment {
    private String href;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        href = (String) args.get("href");
    }

    @InjectView(R.id.browser)
    WebView browser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.webview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this, getView());
        browser.loadUrl(href);
    }

    public static WebViewFragment newInstance(Story story) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("href", story.getHref());
        fragment.setArguments(args);
        return fragment;
    }
}
