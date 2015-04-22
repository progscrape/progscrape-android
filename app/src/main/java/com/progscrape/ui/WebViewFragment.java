package com.progscrape.ui;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.progscrape.R;
import com.progscrape.app.data.Story;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WebViewFragment extends Fragment {
    @InjectView(R.id.browser)
    WebView browser;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.progress)
    ProgressBar progress;
    private String href;
    private String titleText;

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
        titleText = (String) args.get("title");
    }

    @OnClick(R.id.back)
    public void onClickBack() {
        getFragmentManager().popBackStack();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.webview, container, false);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this, getView());

        progress.setIndeterminate(true);

        WebSettings settings = browser.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 0) {
                    progress.setVisibility(View.VISIBLE);
                    progress.setIndeterminate(true);
                } else if (newProgress == 100) {
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.VISIBLE);
                    progress.setIndeterminate(false);
                    progress.setProgress(newProgress);
                }
            }
        });
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl(href);
        browser.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Unable to view " + mimetype + ". Download the file instead?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                                dm.enqueue(new DownloadManager.Request(Uri.parse(url)));
                                getFragmentManager().popBackStack();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getFragmentManager().popBackStack();
                    }
                }).show();
            }
        });

        title.setText(titleText);
    }
}
