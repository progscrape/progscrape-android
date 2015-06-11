package com.progscrape;

import android.app.Activity;

import com.octo.android.robospice.SpiceManager;
import com.progscrape.data.SpiceService;

public class BaseActivity extends Activity {
    private SpiceManager spiceManager;

    public SpiceManager getSpiceManager() {
        if (spiceManager == null) {
            spiceManager = new SpiceManager(SpiceService.class);
            spiceManager.start(this);
        }

        return spiceManager;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spiceManager != null) {
            spiceManager.shouldStop();
            spiceManager = null;
        }
    }
}
