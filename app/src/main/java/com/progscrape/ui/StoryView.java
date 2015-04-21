package com.progscrape.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.progscrape.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryView extends RelativeLayout {
    @InjectView(R.id.title)
    TextView text;

    @InjectView(R.id.icon_hn)
    ImageView hn;

    @InjectView(R.id.icon_reddit)
    ImageView reddit;

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
}
