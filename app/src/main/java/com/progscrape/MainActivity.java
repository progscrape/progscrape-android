package com.progscrape;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.progscrape.app.data.Story;
import com.progscrape.data.Data;
import com.progscrape.event.ActivityEvent;
import com.progscrape.event.SearchEvent;
import com.progscrape.event.StoryEvent;
import com.progscrape.modules.Injector;
import com.progscrape.modules.MainActivityModule;
import com.progscrape.ui.ActivityComponent;
import com.progscrape.ui.StoriesFragment;
import com.progscrape.ui.WebViewFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @Inject
    protected Data data;

    @BindView(R.id.main_drawer_layout)
    protected DrawerLayout drawerLayout;

    @BindView(R.id.top_level_view)
    protected View topLevel;

    @Inject
    protected Bus bus;

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

        ButterKnife.bind(this);

        if (savedInstanceState == null)
            searchTag(null, true);

        bus.register(this);
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        activityGraph = null;
        super.onDestroy();
    }

    @Override
    public Object getSystemService(String name) {
        if (Injector.matchesService(name))
            return activityGraph;

        return super.getSystemService(name);
    }

    @Subscribe
    public void onStoryEvent(StoryEvent storyEvent) {
        switch (storyEvent.getWhat()) {
            case ACTIVATE:
                activateStory(storyEvent.getStory());
                break;
            case MENU:
                showStoryMenu(storyEvent.getStory(), storyEvent.getView(), true);
                break;
            case STORY_MENU:
                showStoryMenu(storyEvent.getStory(), storyEvent.getView(), false);
                break;
        }
    }

    @Subscribe
    public void onActivityEvent(ActivityEvent what) {
        switch (what) {
            case POP_BACK:
                getFragmentManager().popBackStack();
                break;
            case TOGGLE_DRAWER:
                openDrawer();
                break;
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSearchEvent(SearchEvent searchEvent) {
        searchTag(searchEvent.getTag(), searchEvent.isInitial());
    }

    protected void activateStory(Story story) {
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

    protected void searchTag(String tag, boolean initial) {
        Log.i("main", "Setting search fragment to " + tag);

        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.main_content, StoriesFragment.create(tag));
        if (!initial)
            tx.addToBackStack("search");
        tx.commit();

        drawerLayout.closeDrawers();
    }

    protected void openDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    protected void showStoryMenu(final Story story, View view, boolean showFilters) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        if (story.getHackerNewsUrl() == null)
            popup.getMenu().findItem(R.id.hn).setVisible(false);
        if (story.getRedditUrl() == null)
            popup.getMenu().findItem(R.id.reddit).setVisible(false);
        if (story.getLobstersUrl() == null)
            popup.getMenu().findItem(R.id.lobsters).setVisible(false);

        if (showFilters) {
            int index = 0;
            for (String tag : story.getTags()) {
                index++;
                popup.getMenu().add(Menu.NONE, -index, index, "\ud83d\udd0d " + tag);
            }
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
