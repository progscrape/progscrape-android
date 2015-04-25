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

import com.progscrape.MainActivity;
import com.progscrape.R;
import com.progscrape.modules.Injector;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BrowserView extends LinearLayout implements ActivityPauseNotifier.ActivityPauseNotification {
    @InjectView(R.id.browser)
    WebView browser;

    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.progress)
    ProgressBar progress;

    @Inject
    MainActivity activity;

    private String titleText;

    public BrowserView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Injector.obtain(context).inject(this);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putString("title", titleText);
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

        ButterKnife.inject(this);

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
        this.browser.loadUrl(href);
        this.title.setText(title);
    }
}
