package com.progscrape.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.progscrape.MainActivity;
import com.progscrape.R;
import com.progscrape.data.Data;
import com.progscrape.event.SearchEvent;
import com.progscrape.modules.Injector;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class TopTagsView extends LinearLayout {
    @Inject
    protected Data data;

    @Inject
    protected Bus bus;

    public TopTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            Injector.obtain(context, ActivityComponent.class).inject(this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            addItem("tag 1");
            addItem("tag 2");
            addItem("tag 3");
            return;
        }

        ButterKnife.inject(this);

        data.getTopTags(new RequestListener<List<String>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e("tags", "Failed to retrieve tags", spiceException);
            }

            @Override
            public void onRequestSuccess(List<String> tags) {
                for (String tag : tags) {
                    addItem(tag);
                }
            }
        }, false);
    }

    private void addItem(final String tag) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        RelativeLayout item = (RelativeLayout) layoutInflater.inflate(R.layout.tag_item, this, false);

        ((TextView)item.getChildAt(0)).setText(tag);
//        item.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(new SearchEvent(tag, false));
            }
        });

        addView(item);
    }
}
