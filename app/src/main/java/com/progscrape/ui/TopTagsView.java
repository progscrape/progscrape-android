package com.progscrape.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.progscrape.R;
import com.progscrape.data.Data;
import com.progscrape.modules.Injector;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TopTagsView extends LinearLayout {
    @Inject
    protected Data data;

    public TopTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            Injector.obtain(context).inject(this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

        data.getTopTags(new RequestListener<List<String>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(List<String> tags) {
                for (String tag : tags) {
                    addItem(tag);
                }
            }
        });
    }

    private void addItem(String tag) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        TextView item = (TextView) layoutInflater.inflate(R.layout.tag_item, this, false);

        item.setText(tag);
//        item.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
//        item.setOnClickListener(listener);

        addView(item);
    }
}
