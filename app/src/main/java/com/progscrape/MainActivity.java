package com.progscrape;

import android.app.Activity;
import android.os.Bundle;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.progscrape.data.Data;
import com.progscrape.modules.Injector;
import com.progscrape.modules.MainActivityModule;

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
}
