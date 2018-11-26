package com.progscrape.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.progscrape.R;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

public class StoryView extends LinearLayout {
    @BindView(R.id.title)
    TextView text;

    @BindView(R.id.icon_feed)
    ImageView feed;

    @BindView(R.id.icon_hn)
    ImageView hn;

    @BindView(R.id.icon_reddit)
    ImageView reddit;

    @BindView(R.id.icon_lobsters)
    ImageView lobsters;

    @BindView(R.id.arrow)
    ImageView arrow;

    @BindView(R.id.tags)
    FlowLayout tags;

    public StoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode())
            return;

        ButterKnife.bind(this);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setIconsVisible(boolean hn, boolean reddit, boolean lobsters, boolean feed) {
        this.hn.setVisibility(hn ? View.VISIBLE : View.GONE);
        this.reddit.setVisibility(reddit ? View.VISIBLE : View.GONE);
        this.lobsters.setVisibility(lobsters ? View.VISIBLE : View.GONE);
        this.feed.setVisibility(feed ? View.VISIBLE : View.GONE);
    }

    public void setTags(List<String> tagStrings) {
        // Clear out the old tags
        while (tags.getChildAt(tags.getChildCount() - 1) instanceof LinearLayout) {
            tags.removeViewAt(tags.getChildCount() - 1);
        }

        if (tagStrings != null) {
            for (String tag : tagStrings) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.story_tag_item, tags, false);
                ((TextView)view.findViewById(R.id.tag_text)).setText(tag);
                tags.addView(view);
            }
        }
    }

    public void setHasHref(boolean hasHref) {
        this.arrow.setVisibility(hasHref ? View.VISIBLE : View.GONE);
    }
}
