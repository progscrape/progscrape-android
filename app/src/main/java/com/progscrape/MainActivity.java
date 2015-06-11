package com.progscrape;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.progscrape.app.data.Story;
import com.progscrape.data.Data;
import com.progscrape.modules.Injector;
import com.progscrape.modules.MainActivityModule;
import com.progscrape.ui.ActivityComponent;
import com.progscrape.ui.StoriesFragment;
import com.progscrape.ui.WebViewFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {
    @Inject
    protected Data data;

    @InjectView(R.id.main_drawer_layout)
    protected DrawerLayout drawerLayout;

    @InjectView(R.id.top_level_view)
    protected View topLevel;

    private ActivityComponent activityGraph;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppComponent appGraph = Injector.obtain(getApplicationContext(), AppComponent.class);
        activityGraph = appGraph.plus(new MainActivityModule(this));
        activityGraph.inject(this);

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

    public void showStoryMenu(final Story story, View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        if (story.getHackerNewsUrl() == null)
            popup.getMenu().findItem(R.id.hn).setVisible(false);
        if (story.getRedditUrl() == null)
            popup.getMenu().findItem(R.id.reddit).setVisible(false);
        if (story.getLobstersUrl() == null)
            popup.getMenu().findItem(R.id.lobsters).setVisible(false);

        int index = 0;
        for (String tag : story.getTags()) {
            index++;
            popup.getMenu().add(Menu.NONE, -index, index, "\ud83d\udd0d " + tag);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String url;

                if (item.getItemId() < 0) {
                    int n = -item.getItemId() - 1;
                    searchTag(story.getTags().get(n), false);
                    return false;
                }

                switch (item.getItemId()) {
                    case R.id.menu_item_share:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, story.getTitle());
                        shareIntent.putExtra(Intent.EXTRA_TEXT, story.getHref());

                        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_post_to)));
                        return false;
                    case R.id.hn:
                        url = story.getHackerNewsUrl();
                        break;
                    case R.id.reddit:
                        url = story.getRedditUrl();
                        break;
                    case R.id.lobsters:
                        url = story.getLobstersUrl();
                        break;
                    case R.id.browser:
                        url = story.getHref();
                        break;
                    default:
                        return false;
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                return false;
            }
        });
        popup.show();
    }
}
