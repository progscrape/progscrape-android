package com.progscrape;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.progscrape.app.data.Story;
import com.progscrape.data.Data;
import com.progscrape.modules.Injector;
import com.progscrape.modules.MainActivityModule;
import com.progscrape.ui.StoriesFragment;
import com.progscrape.ui.WebViewFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;

public class MainActivity extends Activity {
    @Inject
    protected Data data;

    @InjectView(R.id.main_drawer_layout)
    protected DrawerLayout drawerLayout;

    private ObjectGraph activityGraph;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ObjectGraph appGraph = Injector.obtain(getApplication());
        appGraph.inject(this);
        activityGraph = appGraph.plus(new MainActivityModule(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        ButterKnife.inject(this);

        if (savedInstanceState == null)
            searchTag(null, true);
    }

    @Override
    protected void onDestroy() {
        activityGraph = null;
        super.onDestroy();
    }

    @Override
    public Object getSystemService(String name) {
        if (Injector.matchesService(name))
            return activityGraph;

        return super.getSystemService(name);
    }

    public void activateStory(Story story) {
        Fragment f = WebViewFragment.newInstance(story);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.setCustomAnimations(R.animator.slide_in,
                0,
                0,
                R.animator.slide_out);
        tx.add(R.id.top_level_view, f, "webView").addToBackStack("open web view");
        tx.commit();
    }

    public void searchTag(String tag, boolean initial) {
        Log.i("main", "Setting search fragment to " + tag);

        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.main_content, StoriesFragment.create(tag));
        if (!initial)
            tx.addToBackStack("search");
        tx.commit();

        drawerLayout.closeDrawers();
    }

    public void openDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }
}
