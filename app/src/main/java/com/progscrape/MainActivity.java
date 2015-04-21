package com.progscrape;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.progscrape.app.data.Story;
import com.progscrape.data.Data;
import com.progscrape.modules.Injector;
import com.progscrape.modules.MainActivityModule;
import com.progscrape.ui.WebViewFragment;

import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class MainActivity extends Activity {
    private ObjectGraph activityGraph;

    @Inject
    protected Data data;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ObjectGraph appGraph = Injector.obtain(getApplication());
        appGraph.inject(this);
        activityGraph = appGraph.plus(new MainActivityModule(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        data.getTopTags(new RequestListener<List<String>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                System.out.println(spiceException);
            }

            @Override
            public void onRequestSuccess(List<String> strings) {

            }
        }, false);
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_content, f, "webView").addToBackStack("open web view");
        fragmentTransaction.commit();
    }
}
