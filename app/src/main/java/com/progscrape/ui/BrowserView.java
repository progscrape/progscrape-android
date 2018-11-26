package com.progscrape.ui;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.progscrape.MainActivity;
import com.progscrape.R;
import com.progscrape.modules.Injector;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.BindView;

public class BrowserView extends LinearLayout implements ActivityPauseNotifier.ActivityPauseNotification {
    @BindView(R.id.browser)
    WebView browser;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.progress)
    ProgressBar progress;

    @Inject
    MainActivity activity;

    private String titleText;
    private String href;

    public BrowserView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode())
            return;

        Injector.obtain(context, ActivityComponent.class).inject(this);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putString("title", titleText);
        bundle.putString("href", href);
        bundle.putParcelable("super", super.onSaveInstanceState());
        Bundle web = new Bundle();
        bundle.putBundle("browser", web);
        browser.saveState(web);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        super.onRestoreInstanceState(bundle.getParcelable("super"));
        this.href = bundle.getString("href");
        this.title.setText(titleText = bundle.getString("title"));
        browser.restoreState(bundle.getBundle("browser"));
    }

    public void onPause() {
        browser.onPause();
    }

    public void onResume() {
        browser.onResume();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode())
            return;

        ButterKnife.bind(this);

        progress.setIndeterminate(true);

        setupBrowser();
    }

    private void setupBrowser() {
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
        browser.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Unable to view " + mimetype + ". Download the file instead?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                dm.enqueue(new DownloadManager.Request(Uri.parse(url)));
                                back();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        back();
                    }
                }).show();
            }
        });
    }

    private void back() {
        activity.getFragmentManager().popBackStack();
    }

    public void setPage(String href, String title) {
        this.titleText = title;
        if (this.href == null || !this.href.equals(href)) {
            this.href = href;
            this.browser.loadUrl(href, new HashMap<>(ImmutableMap.of("Referer", "http://www.progscrape.com/")));
        }
        this.title.setText(title);
    }
}
