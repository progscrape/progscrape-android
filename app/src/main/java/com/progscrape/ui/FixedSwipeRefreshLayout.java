package com.progscrape.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ViewAnimator;

import java.lang.reflect.Field;

// https://code.google.com/p/android/issues/detail?id=78191
public class FixedSwipeRefreshLayout extends SwipeRefreshLayout {
    public FixedSwipeRefreshLayout(Context context) {
        super(context);
    }

    public FixedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        View target;

        try {
            Field f = getClass().getSuperclass().getDeclaredField("mTarget");
            f.setAccessible(true);
            target = (View) f.get(this);
        } catch (Exception e) {
            // Should not happen
            e.printStackTrace();
            return super.canChildScrollUp();
        }

        if (target instanceof ViewAnimator) {
            target = ((ViewAnimator)target).getCurrentView();
        }

        if (target instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) target;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int position = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                return position != 0;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] positions = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null);
                for (int i = 0; i < positions.length; i++) {
                    if (positions[i] == 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        return super.canChildScrollUp();
    }
}
