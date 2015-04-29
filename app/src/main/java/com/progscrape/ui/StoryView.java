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
import butterknife.InjectView;

public class StoryView extends LinearLayout {
    @InjectView(R.id.title)
    TextView text;

    @InjectView(R.id.icon_hn)
    ImageView hn;

    @InjectView(R.id.icon_reddit)
    ImageView reddit;

    @InjectView(R.id.tags)
    FlowLayout tags;

    public StoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode())
            return;

        ButterKnife.inject(this);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setIconsVisible(boolean hn, boolean reddit) {
        this.hn.setVisibility(hn ? View.VISIBLE : View.GONE);
        this.reddit.setVisibility(reddit ? View.VISIBLE : View.GONE);
    }

    public void setTags(List<String> tagStrings) {
        // Clear out the old tags
        while (tags.getChildAt(tags.getChildCount() - 1) instanceof TextView) {
            tags.removeViewAt(tags.getChildCount() - 1);
        }

        for (String tag : tagStrings) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.story_tag_item, tags, false);
            textView.setText(tag);
            tags.addView(textView);
        }
    }
}
