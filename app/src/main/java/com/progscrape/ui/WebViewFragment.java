package com.progscrape.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.progscrape.R;
import com.progscrape.app.data.Story;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WebViewFragment extends Fragment {
    private String href;
    private String titleText;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        href = (String) args.get("href");
        titleText = (String) args.get("title");
    }

    @InjectView(R.id.browser)
    WebView browser;

    @InjectView(R.id.title)
    TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.webview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this, getView());
        WebSettings settings = browser.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        browser.setWebChromeClient(new WebChromeClient());
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl(href);

        title.setText(titleText);
    }

    public static WebViewFragment newInstance(Story story) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("href", story.getHref());
        args.putString("title", story.getTitle());
        fragment.setArguments(args);
        return fragment;
    }
}
