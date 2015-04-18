package com.progscrape;

import android.app.Activity;
import android.os.Bundle;

import com.progscrape.modules.Injector;
import com.progscrape.modules.MainActivityModule;

import dagger.ObjectGraph;

public class MainActivity extends Activity {
    private ObjectGraph activityGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);
        ObjectGraph appGraph = Injector.obtain(getApplication());
        appGraph.inject(this);

        activityGraph = appGraph.plus(new MainActivityModule(this));
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
