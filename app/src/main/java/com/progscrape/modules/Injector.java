package com.progscrape.modules;

import android.content.Context;

public final class Injector {
    private static final String INJECTOR_SERVICE = "com.progscrape.injector";

    @SuppressWarnings("ResourceType") // Explicitly doing a custom service.
    public static <T> T obtain(Context context, Class<T> clazz) {
        return clazz.cast(context.getSystemService(INJECTOR_SERVICE));
    }

    public static boolean matchesService(String name) {
        return INJECTOR_SERVICE.equals(name);
    }

    private Injector() {
        throw new AssertionError("No instances.");
    }
}
